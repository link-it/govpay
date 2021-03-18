package it.govpay.web.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.telematici.pagamenti.ws.ccp.CtSpezzoneStrutturatoCausaleVersamento;
import gov.telematici.pagamenti.ws.ccp.CtSpezzoniCausaleVersamento;
import gov.telematici.pagamenti.ws.ccp.EsitoAttivaRPT;
import gov.telematici.pagamenti.ws.ccp.EsitoVerificaRPT;
import gov.telematici.pagamenti.ws.ccp.FaultBean;
import gov.telematici.pagamenti.ws.ccp.PaaAttivaRPTRisposta;
import gov.telematici.pagamenti.ws.ccp.PaaTipoDatiPagamentoPA;
import gov.telematici.pagamenti.ws.ccp.PaaVerificaRPTRisposta;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtFaultBean;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtQrCode;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.gov.pagopa.pagopa_api.pa.pafornode_wsdl.PaForNodePortType;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.Applicazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.CausaleSpezzoni;
import it.govpay.model.Versamento.CausaleSpezzoniStrutturati;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;



@WebService(serviceName = "paForNode_Service",
endpointInterface = "it.gov.pagopa.pagopa_api.pa.pafornode_wsdl.PaForNodePortType",
targetNamespace = "http://pagopa-api.pagopa.gov.it/pa/paForNode.wsdl",
portName = "paForNode_PortType",
wsdlLocation = "/wsdl/paForNode.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)
public class PaForNodeImpl implements PaForNodePortType{

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PaForNodeImpl.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public PaSendRTRes paSendRT(PaSendRTReq requestBody) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaVerifyPaymentNoticeRes paVerifyPaymentNotice(PaVerifyPaymentNoticeReq requestBody) {
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
		String idDominio = requestBody.getIdStation();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		PaVerifyPaymentNoticeRes response = new PaVerifyPaymentNoticeRes();
		
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			String iuv = IuvUtils.toIuv(numeroAvviso);
			String ccp = null; // TODO
			String psp =  null; // TODO
			
			appContext.setCorrelationId(codDominio + iuv + ccp);
			
			appContext.getEventoCtx().setCodDominio(codDominio);
			appContext.getEventoCtx().setIuv(iuv);
			appContext.getEventoCtx().setCcp(ccp);
			
			Actor from = new Actor();
			from.setName(GpContext.NodoDeiPagamentiSPC);
			from.setType(GpContext.TIPO_SOGGETTO_NDP);
			appContext.getTransaction().setFrom(from);

			Actor to = new Actor();
			to.setName(codIntermediario);
			from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
			appContext.getTransaction().setTo(to);

			appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
			appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
			appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
			appContext.getRequest().addGenericProperty(new Property("codPsp", psp));
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerifica");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}
		
			log.info("Ricevuta richiesta paVerifyPaymentNotice [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
//			BasicBD bd = null;

			DatiPagoPA datiPagoPA = new DatiPagoPA();
			datiPagoPA.setCodStazione(codStazione);
			datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
			appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
			datiPagoPA.setCodDominio(codDominio);
//			appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.paaVerificaRPT.name());
			datiPagoPA.setCodPsp(psp);
			datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setErogatore(codIntermediario);
			datiPagoPA.setCodIntermediario(codIntermediario);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
				ctx.getApplicationLogger().log("ccp.iuvPresente", versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					ctx.getApplicationLogger().log("ccp.iuvNonPresenteNoAppGestireIuv");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
				ctx.getApplicationLogger().log("ccp.iuvNonPresente", applicazioneGestisceIuv.getCodApplicazione());
			}
		
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento);

					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}

				} catch (NotFoundException e) {
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					// Versamento non trovato, devo interrogare l'applicazione.
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					versamento = VersamentoUtils.acquisisciVersamento(applicazioneGestisceIuv, null, null, null, codDominio, iuv,  TipologiaTipoVersamento.DOVUTO);
					
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento);
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
					
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk",applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante la verifica del versamento: " + e1, codDominio, e1);
			}
			
		
			RptBD rptBD = new RptBD(configWrapper);
			if(GovpayConfig.getInstance().isTimeoutPendentiModello3()) {
				// Controllo che non ci sia un pagamento in corso
				// Prendo tutte le RPT pendenti
				RptFilter filter = rptBD.newFilter();
				filter.setStato(Rpt.stati_pendenti);
				filter.setIdVersamento(versamento.getId());
				List<Rpt> rpt_pendenti = rptBD.findAll(filter);
				
				// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
				// Altrimenti lancio il fault
				Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() * 60000);
				
				for(Rpt rpt_pendente : rpt_pendenti) {
					if(rpt_pendente.getPagamentoPortale() != null)
						appContext.getEventoCtx().setIdPagamento(rpt_pendente.getPagamentoPortale().getIdSessione());
					Date dataMsgRichiesta = rpt_pendente.getDataMsgRichiesta();
					// se l'RPT e' bloccata allora controllo che il blocco sia indefinito oppure definito, altrimenti passo
					if(rpt_pendente.isBloccante() && (GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() == 0 || dataSoglia.before(dataMsgRichiesta))) {
						throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, "Pagamento in corso [CCP:" + rpt_pendente.getCcp() + "].", codDominio);
					}
				}
			}

			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti().size() != 1) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiene piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.", codDominio);
			}

			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("OK");
			PaaTipoDatiPagamentoPA datiPagamento =  new PaaTipoDatiPagamentoPA();
			datiPagamento.setImportoSingoloVersamento(versamento.getImportoTotale());

			if(versamento.getCausaleVersamento() != null) {
				if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
					datiPagamento.setCausaleVersamento(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoni) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().addAll(((CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoniStrutturati) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					CausaleSpezzoniStrutturati causale = (CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
					for(int i=0; i < causale.getSpezzoni().size(); i++) {
						CtSpezzoneStrutturatoCausaleVersamento spezzone = new CtSpezzoneStrutturatoCausaleVersamento();
						spezzone.setCausaleSpezzone(causale.getSpezzoni().get(i));
						spezzone.setImportoSpezzone(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			} else {
				datiPagamento.setCausaleVersamento(" ");
			}

			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(configWrapper)));
			SingoloVersamento singoloVersamento = versamento.getSingoliVersamenti().get(0);
			
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
			
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBic());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			// response.setPaaVerificaRPTRisposta(esito); TODO
			ctx.getApplicationLogger().log("ccp.ricezioneVerificaOk", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
//			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
//			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
//			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	@Override
	public PaGetPaymentRes paGetPayment(PaGetPaymentReq requestBody) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private <T> T buildRisposta(Exception e, String codDominio, T risposta) {
		return this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, e.getMessage(), codDominio, e), risposta);
	}

	private <T> T buildRisposta(NdpException e, T risposta) {
		if(risposta instanceof PaVerifyPaymentNoticeRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaVerifyPaymentNotice: " + e);
			} else {
				log.warn("Rifiutata PaVerifyPaymentNotice con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaVerifyPaymentNoticeRes r = (PaVerifyPaymentNoticeRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}

		if(risposta instanceof PaGetPaymentRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaGetPayment: " + e);
			} else {
				log.warn("Rifiutata PaGetPayment con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaGetPaymentRes r = (PaGetPaymentRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaSendRTRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaSendRT: " + e);
			} else {
				log.warn("Rifiutata PaSendRT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaSendRTRes r = (PaSendRTRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}

		return risposta;
	}

}
