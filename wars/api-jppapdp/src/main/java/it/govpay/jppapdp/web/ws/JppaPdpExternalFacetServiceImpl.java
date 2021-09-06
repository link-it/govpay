package it.govpay.jppapdp.web.ws;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.MaggioliJPPAUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Rpt.EsitoPagamento;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.CtRichiestaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.CtRispostaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.JppaPdpExternalServicesEndpoint;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.StEsito;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.StOperazione;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.ObjectFactory;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.RecuperaRTRichiesta;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.RecuperaRTRisposta;

@WebService(serviceName = "JppaPdpExternalFacetService",
endpointInterface = "it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.JppaPdpExternalServicesEndpoint",
targetNamespace = "http://jcitygov.informatica.maggioli.it/pagopa/payservice/pdp/connector/jppapdp/external",
portName = "JppaPdpExternalServicesPort",
wsdlLocation="/wsdl/jppapdp-ws-external.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)
public class JppaPdpExternalFacetServiceImpl implements JppaPdpExternalServicesEndpoint {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(JppaPdpExternalFacetServiceImpl.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private ObjectFactory objectFactory = new ObjectFactory();

	@Override
	public CtRispostaStandard recuperaRT(CtRichiestaStandard recuperaRTRichiesta) {

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		String codDominio = recuperaRTRichiesta.getIdentificativoDominio();
		appContext.getEventoCtx().setCodDominio(codDominio);

		Actor from = new Actor();
		from.setName(GpContext.MaggioliJPPA);
		from.setType(GpContext.TIPO_SOGGETTO_MAGGIOLI_JPPA);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(GpContext.GovPay);
		from.setType(GpContext.GovPay);
		appContext.getTransaction().setTo(to);

		CtRispostaStandard response = new CtRispostaStandard();
		try {
			XMLGregorianCalendar dataOperazione = MaggioliJPPAUtils.impostaDataOperazione(new Date());
			response.setDataOperazione(dataOperazione);
			
			try {
				ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRT");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}
			log.info("Ricevuta richiesta RecuperaRT per il Dominio ["+codDominio+"]");

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));

			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new RuntimeException("Dominio ["+codDominio+"] indicato non censito in anagrafica.");
			}

			// autorizzazione principal ricevuto
			ConnettoreNotificaPagamenti connettoreMaggioliJPPA = dominio.getConnettoreMaggioliJPPA();
			if(connettoreMaggioliJPPA == null || !connettoreMaggioliJPPA.isAbilitato()) {
				throw new RuntimeException("Servizio RecuperaRT non abilitato per il Dominio ["+codDominio+"].");
			}

			String principalMaggioli = connettoreMaggioliJPPA.getPrincipalMaggioli();
			boolean authOk = AuthorizationManager.checkPrincipal(authentication, principalMaggioli); 

			if(!authOk) {
				GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
				String principal = details.getIdentificativo(); 
				ctx.getApplicationLogger().log("jppapdp.erroreAutorizzazione", principal);
				throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per il servizio Maggioli JPPA (" + principalMaggioli + ").");
			}

			// validazione operazione
			if(recuperaRTRichiesta.getOperazione() == null || !StOperazione.RECUPERA_RT.equals(recuperaRTRichiesta.getOperazione())) {
				throw new RuntimeException("Operazione richiesta ["+recuperaRTRichiesta.getOperazione()+"] non valida.");
			}

			String xmlDettaglioRichiesta = recuperaRTRichiesta.getXmlDettaglioRichiesta();
			// unmarshal della richiesta interna
			log.debug("Ricevuta Richiesta: " + xmlDettaglioRichiesta);
			
			RecuperaRTRichiesta recuperaRT = (RecuperaRTRichiesta) MaggioliJPPAUtils.unmarshalJPPAPdPExternal(xmlDettaglioRichiesta, null);

			String iuv = recuperaRT.getIdentificativoUnivocoVersamento();
			String ccp = recuperaRT.getCodiceContestoPagamento();
			log.info("Ricevuta richiesta RecuperaRT [" + codDominio + "][" + iuv + "][" + ccp + "]");
			
			appContext.setCorrelationId(codDominio + iuv + ccp);
			appContext.getEventoCtx().setIuv(iuv);
			appContext.getEventoCtx().setCcp(ccp);
			appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
			appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
			
			try {
				ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRTParametri");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}

			// lettura RPT
			RptBD rptBD = new RptBD(configWrapper);

			try {
				log.debug("Lettura RT [" + codDominio + "][" + iuv + "][" + ccp + "] in corso...");
				Rpt rpt = rptBD.getRpt(codDominio, iuv, ccp, true);
				
				if(!(rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_ESEGUITO) || rpt.getEsitoPagamento().equals(EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO))) {
					// stato non valido
					log.debug("Lettura RT [" + codDominio + "][" + iuv + "][" + ccp + "] in stato ["+rpt.getEsitoPagamento()+"] non valido per il recupero.");
					response.setEsito(StEsito.ERROR);
					response.setOperazione(StOperazione.RECUPERA_RT);
					response.setDataOperazione(dataOperazione);
					return response;
				}
				
				RecuperaRTRisposta recuperaRTRisposta = MaggioliJPPAUtils.buildRTRisposta(this.objectFactory,rpt,configWrapper);
				JAXBElement<RecuperaRTRisposta> jaxbElement = new JAXBElement<RecuperaRTRisposta>(new QName("",  MaggioliJPPAUtils.RECUPERA_RT_RISPOSTA_ROOT_ELEMENT_NAME), RecuperaRTRisposta.class, null, recuperaRTRisposta);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JaxbUtils.marshalJPPAPdPExternalService(jaxbElement, baos);
				String xmlDettaglioRisposta = baos.toString();
				response.setXmlDettaglioRisposta( MaggioliJPPAUtils.CDATA_TOKEN_START + xmlDettaglioRisposta +  MaggioliJPPAUtils.CDATA_TOKEN_END);
				response.setEsito(StEsito.OK);
				ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRTOk");
				appContext.getEventoCtx().setEsito(Esito.OK);
			} catch (ServiceException e) {
				log.error("Lettura RT [" + codDominio + "][" + iuv + "][" + ccp + "] completata con errore: " + e.getMessage(),e);
				throw e;
			} catch (NotFoundException e) {
				log.error("Lettura RT [" + codDominio + "][" + iuv + "][" + ccp + "] completata con errore: RPT non trovata.");
				response.setEsito(StEsito.ERROR);
				response.setOperazione(StOperazione.RECUPERA_RT);
				response.setDataOperazione(dataOperazione);
				return response;
			} finally {
				if(rptBD != null) {
					rptBD.closeConnection();
				}
			}
		}catch (Exception e) {
			log.error("Errore durante l'esecuzione della procedura di recupero RT: "+ e.getMessage(),e);
			String faultDescription = e.getMessage() == null ? "<Nessuna descrizione>" : e.getMessage(); 
			try {
				ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRTKo", "FAIL", "FAIL", faultDescription); // TODO controllare
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito("FAIL");
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			
			response.setEsito(StEsito.ERROR);
		} finally {
			GpContext.setResult(appContext.getTransaction(), "FAIL"); // TODO controllare
		}

		return response;
	}

	@Override
	public CtRispostaStandard inviaCarrelloRPT(CtRichiestaStandard inviaCarrelloRPTRichiesta) {
		throw new RuntimeException("Operazione non implementata.");
	}

	@Override
	public CtRispostaStandard chiediStatoRPT(CtRichiestaStandard chiediStatoRPTRichiesta) {
		throw new RuntimeException("Operazione non implementata.");
	}

}
