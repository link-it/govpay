package it.govpay.jppapdp.web.ws;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.MaggioliJPPAUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.CtRichiestaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.CtRispostaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.JppaPdpExternalServicesEndpoint;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.StEsito;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.StOperazione;

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


	@Override
	public CtRispostaStandard recuperaRT(CtRichiestaStandard recuperaRTRichiesta) {

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();

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

		try {
			ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRT");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}

		CtRispostaStandard response = new CtRispostaStandard();
		try {
			XMLGregorianCalendar dataOperazione = MaggioliJPPAUtils.impostaDataOperazione(new Date());
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			log.info("Ricevuta richiesta RecuperaRT");

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
			

			// TODO decodifica dettaglio richiesta
			String iuv = null;
			String ccp = null;

			// lettura RPT
//			RptBD rptBD = new RptBD(configWrapper);
//
//			try {
//				rptBD.getRpt(codDominio, iuv, ccp, true);
//			} catch (ServiceException e) {
//				throw e;
//			} catch (NotFoundException e) {
//				response.setEsito(StEsito.ERROR);
//				response.setOperazione(StOperazione.RECUPERA_RT);
//				response.setDataOperazione(dataOperazione);
//			} finally {
//				if(rptBD != null) {
//					rptBD.closeConnection();
//				}
//			}

			response.setEsito(StEsito.OK);
		}catch (Exception e) {
			String faultDescription = e.getMessage() == null ? "<Nessuna descrizione>" : e.getMessage(); 
			try {
				ctx.getApplicationLogger().log("jppapdp.ricezioneRecuperaRTKo", "FAIL", "FAIL", faultDescription); // TODO controllare
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito("FAIL");
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
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
