package it.govpay.pagopa.v2.endpoint;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import it.gov.pagopa.pagopa_api.pa.pafornode.CtFaultBean;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentOptionDescriptionPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentOptionsDescriptionListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtQrCode;
import it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StAmountOption;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.gde.GdeInvoker;
import it.govpay.gde.v1.model.DatiPagoPA;
import it.govpay.gde.v1.model.NuovoEvento;
import it.govpay.gde.v1.model.NuovoEvento.EsitoEnum;
import it.govpay.pagopa.v2.business.Applicazione;
import it.govpay.pagopa.v2.business.Versamento;
import it.govpay.pagopa.v2.causale.Causale;
import it.govpay.pagopa.v2.causale.CausaleSemplice;
import it.govpay.pagopa.v2.causale.utils.CausaleUtils;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.DominioEntity;
import it.govpay.pagopa.v2.entity.IntermediarioEntity;
import it.govpay.pagopa.v2.entity.PagamentoEntity;
import it.govpay.pagopa.v2.entity.SingoloVersamentoEntity;
import it.govpay.pagopa.v2.entity.StazioneEntity;
import it.govpay.pagopa.v2.entity.UoEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity.StatoVersamento;
import it.govpay.pagopa.v2.entity.VersamentoEntity.TipologiaTipoVersamento;
import it.govpay.pagopa.v2.enumeration.ModelloPagamento;
import it.govpay.pagopa.v2.enumeration.TipoVersamento;
import it.govpay.pagopa.v2.repository.DominioRepository;
import it.govpay.pagopa.v2.repository.IntermediarioRepository;
import it.govpay.pagopa.v2.repository.PagamentoRepository;
import it.govpay.pagopa.v2.repository.StazioneRepository;
import it.govpay.pagopa.v2.repository.VersamentoRepository;
import it.govpay.pagopa.v2.utils.DateUtils;
import it.govpay.pagopa.v2.utils.IuvUtils;
import it.govpay.pagopa.v2.utils.VersamentoUtils;

// URL DEPLOY: http://HOST:PORT/WAR_NAME/ws/service/pa/paForNode
@Endpoint
public class PaForNodeEndpoint {

	
	private static Logger log = LoggerFactory.getLogger(PaForNodeEndpoint.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private static final String NAMESPACE_URI = "http://pagopa-api.pagopa.gov.it/pa/paForNode.xsd";
	private static final String paVerifyPaymentNotice = "paVerifyPaymentNotice";
	
	@Value("${it.govpay.pdd.auth:true}")
	private boolean pddAuthEnable;
	
	@Autowired
	private IntermediarioRepository intermediarioRepository;
	
	@Autowired
	private StazioneRepository stazioneRepository;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private VersamentoRepository versamentoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private Applicazione applicazioneBusiness;
	
	@Autowired
	private Versamento versamentoBusiness;
	
//	@Autowired
	private GdeInvoker gdeInvoker;
	

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paVerifyPaymentNoticeReq")
	@ResponsePayload
	@Transactional
	public JAXBElement<PaVerifyPaymentNoticeRes> paVerifyPaymentNotice(@RequestPayload JAXBElement<PaVerifyPaymentNoticeReq> request) {
		PaVerifyPaymentNoticeReq requestBody = request.getValue();
		
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
		//String idDominio = requestBody.getIdPA();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		NuovoEvento eventoCtx = new NuovoEvento();
		PaVerifyPaymentNoticeRes response = new PaVerifyPaymentNoticeRes();
		
		try {
			String iuv = IuvUtils.toIuv(numeroAvviso);
			
			log.info("Ricevuta richiesta di verifica del pagamento [Dominio:{} NumeroAvviso:{}] dal Nodo dei Pagamenti.", codDominio, numeroAvviso);
			
			DatiPagoPA datiPagoPA = new DatiPagoPA();
			datiPagoPA.setIdStazione(codStazione);
			eventoCtx.setDatiPagoPA(datiPagoPA);
			datiPagoPA.setIdDominio(codDominio);
			eventoCtx.setTipoEvento(paVerifyPaymentNotice);
			datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP.getCodifica());
			datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP.getCodifica() + "");
			datiPagoPA.setIdIntermediario(codIntermediario);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(this.pddAuthEnable && authentication == null) {
				log.error("Nessuna autorizzazione fornita");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			Optional<IntermediarioEntity> findIntermediario = this.intermediarioRepository.findOneByCodIntermediario(codIntermediario);
			
			if(!findIntermediario.isPresent()) {
				// lanciare errore PAA_ID_INTERMEDIARIO_ERRATO
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			
			IntermediarioEntity intermediario = findIntermediario.get();
			// Controllo autorizzazione
			if(this.pddAuthEnable){
//				boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
//				
//				if(!authOk) {
//					GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
//					String principal = details.getIdentificativo(); 
//					log.error("Il principal fornito non e' corretto {}.", principal);
//					throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
//				}
			}
			 
			// leggo stazione
			Optional<StazioneEntity> findStazione = this.stazioneRepository.findOneByCodStazione(codStazione);
			
			if(!findStazione.isPresent()) {
				// lanciare errore PAA_STAZIONE_INT_ERRATA
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}
			
			// leggo dominio
			Optional<DominioEntity> findDominio = this.dominioRepository.findOneByCodDominio(codDominio);
			
			if(!findDominio.isPresent()) {
				// lanciare errore PAA_ID_DOMINIO_ERRATO
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			
			DominioEntity dominio = findDominio.get();
			
			// lettura del versamento
			Optional<VersamentoEntity> findVersamento = this.versamentoRepository.findOneByDominioAndIuvVersamento(dominio, iuv);
			
			VersamentoEntity versamento = null;
			ApplicazioneEntity applicazioneGestisceIuv = null;
			if(!findVersamento.isPresent()) {
				// cerco l'applicazione per gestire lo iuv
				applicazioneGestisceIuv = applicazioneBusiness.getApplicazioneDominio(dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					// non ho trovato un'applicazione in grado di gestire il versamento allora termino
					log.error("Iuv non censito su GovPay ma nessuna applicazione censita puo' gestirlo.");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				log.debug("Iuv non censito su GovPay. Applicazione selezionata per la verifica: {}.", applicazioneGestisceIuv.getCodApplicazione());
			} else {
				versamento = findVersamento.get();
				log.debug("Iuv censito su GovPay ed associato al versamento {}.", versamento.getCodVersamentoEnte());
				eventoCtx.setIdA2A(versamento.getApplicazione().getCodApplicazione());
				eventoCtx.setIdPendenza(versamento.getCodVersamentoEnte());
			}
			
			try {
				if(versamento == null) { // Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = applicazioneBusiness.getApplicazioneDominio(dominio,iuv); 
						eventoCtx.setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					// Versamento non trovato, devo interrogare l'applicazione.
					log.debug("Versamento non associato ad uno iuv censito e non disponibile nel repository interno.\nAcquisizione dall'applicazione [Applicazione:{} Dominio:{} Iuv:{}] in corso...", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					versamento = versamentoBusiness.acquisisciVersamento(applicazioneGestisceIuv, null, null, null, codDominio, iuv,  TipologiaTipoVersamento.DOVUTO);
					
					eventoCtx.setIdA2A(versamento.getApplicazione().getCodApplicazione());
					eventoCtx.setIdPendenza(versamento.getCodVersamentoEnte());
					
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = versamentoBusiness.aggiornaVersamento(versamento);
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);
	
					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							List<SingoloVersamentoEntity> singoliVersamenti = new ArrayList<SingoloVersamentoEntity>(versamento.getSingoliVersamenti());
							List<PagamentoEntity> pagamenti = this.pagamentoRepository.findAllBySingoloVersamento(singoliVersamenti.get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								PagamentoEntity pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
					log.info("Versamento non associato ad uno iuv censito e non disponibile nel repository interno.\nAcquisizione dall'applicazione [Applicazione:{} Dominio:{} Iuv:{}] eseguita con successo.",applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				} else {
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = versamentoBusiness.aggiornaVersamento(versamento);
	
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);
	
					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							List<SingoloVersamentoEntity> singoliVersamenti = new ArrayList<SingoloVersamentoEntity>(versamento.getSingoliVersamenti());
							List<PagamentoEntity> pagamenti = this.pagamentoRepository.findAllBySingoloVersamento(singoliVersamenti.get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								PagamentoEntity pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
				}
			} catch (VersamentoScadutoException e1) {
				eventoCtx.setIdA2A(e1.getCodApplicazione());
				eventoCtx.setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio);
			} catch (VersamentoAnnullatoException e1) {
				eventoCtx.setIdA2A(e1.getCodApplicazione());
				eventoCtx.setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio);
			} catch (VersamentoDuplicatoException e1) {
				eventoCtx.setIdA2A(e1.getCodApplicazione());
				eventoCtx.setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio);
			} catch (VersamentoSconosciutoException e1) {
				eventoCtx.setIdA2A(e1.getCodApplicazione());
				eventoCtx.setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio);
			} catch (VersamentoNonValidoException e1) {
				eventoCtx.setIdA2A(e1.getCodApplicazione());
				eventoCtx.setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio);
//			} catch (ClientException e1) {
//				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante la verifica del versamento: " + e1, codDominio, e1);
			}
			response.setOutcome(StOutcome.OK);
			DominioEntity dominioVersamento = versamento.getDominio();
			response.setFiscalCodePA(dominioVersamento.getCodDominio());
			response.setCompanyName(dominioVersamento.getRagioneSociale());
			
			UoEntity uo = versamento.getUo();
			if(uo != null && 
					!uo.getCodUo().equals(DominioEntity.EC) && uo.getUoDenominazione() != null) {
				response.setOfficeName(uo.getUoDenominazione());
			}
			String causaleString = "";
			if(versamento.getCausaleVersamento() != null) {
				Causale decode = CausaleUtils.decode(versamento.getCausaleVersamento());
				causaleString = decode.getSimple();
				if(decode instanceof CausaleSemplice) {
					response.setPaymentDescription(((CausaleSemplice)decode).getCausale());
				}

			} else {
				response.setPaymentDescription(" ");
			}
			
			CtPaymentOptionsDescriptionListPA paymentList = new CtPaymentOptionsDescriptionListPA();

			// Inserisco come opzione di pagamento solo quella con importo esatto del versamento
			// 30/05/2022 come indicato in https://github.com/pagopa/pagopa-api/issues/216 il supporto alle opzioni multiple e' stato sospeso
			// TODO capire come proporre eventuali altre soluzioni (es. rate o pagamenti con soglie.)
			CtPaymentOptionDescriptionPA ctPaymentOptionDescriptionPA = new CtPaymentOptionDescriptionPA();
			StAmountOption stAmountOption = StAmountOption.EQ;
			ctPaymentOptionDescriptionPA.setOptions(stAmountOption );
			ctPaymentOptionDescriptionPA.setAmount(new BigDecimal(versamento.getImportoTotale()));
			ctPaymentOptionDescriptionPA.setDetailDescription(causaleString);
			ctPaymentOptionDescriptionPA.setDueDate(DateUtils.fromLocalDateTime(versamento.getDataValidita())); 
			ctPaymentOptionDescriptionPA.setAllCCP(VersamentoUtils.isAllIBANPostali(versamento));
//			paymentList.getPaymentOptionDescription().add(ctPaymentOptionDescriptionPA);
			paymentList.setPaymentOptionDescription(ctPaymentOptionDescriptionPA);

			response.setPaymentList(paymentList);
			log.info("Verifica completata con successo. Inviato esito OK al Nodo dei Pagamenti [Importo:{}\u20AC Iban:{} Causale:'{}'].", versamento.getImportoTotale().toString(), "", versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			eventoCtx.setEsito(EsitoEnum.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			log.error("Verifica rifiutata con esito KO. Inviato codice di errore {}: {}. Dettaglio errore: {}.", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			eventoCtx.setDettaglioEsito(faultDescription);
			eventoCtx.setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				eventoCtx.setEsito(EsitoEnum.FAIL);
			else 
				eventoCtx.setEsito(EsitoEnum.KO);
		} catch (IllegalArgumentException e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			log.error("Verifica rifiutata con esito KO. Inviato codice di errore {}: {}. Dettaglio errore: {}.", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			eventoCtx.setDettaglioEsito(faultDescription);
			eventoCtx.setSottotipoEsito(response.getFault().getFaultCode());
			eventoCtx.setEsito(EsitoEnum.FAIL);
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			log.error("Verifica rifiutata con esito KO. Inviato codice di errore {}: {}. Dettaglio errore: {}.", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			eventoCtx.setDettaglioEsito(faultDescription);
			eventoCtx.setSottotipoEsito(response.getFault().getFaultCode());
			eventoCtx.setEsito(EsitoEnum.FAIL);
		} finally {
//			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
//			this.gdeInvoker.salvaEvento(eventoCtx);
		}

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaVerifyPaymentNoticeRes> jaxbResponse = objectFactory.createPaVerifyPaymentNoticeRes(response);

		return jaxbResponse;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paGetPaymentReq")
	@ResponsePayload
	public JAXBElement<PaGetPaymentRes> paGetPayment(@RequestPayload JAXBElement<PaGetPaymentReq> request) {
		PaGetPaymentRes paVerifyPaymentNoticeRes = new PaGetPaymentRes();

		paVerifyPaymentNoticeRes.setOutcome(StOutcome.OK);

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaGetPaymentRes> response = objectFactory.createPaGetPaymentRes(paVerifyPaymentNoticeRes);

		return response;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paSendRTReq")
	@ResponsePayload
	public JAXBElement<PaSendRTRes> paSendRT(@RequestPayload JAXBElement<PaSendRTReq> request) {
		PaSendRTRes paVerifyPaymentNoticeRes = new PaSendRTRes();

		paVerifyPaymentNoticeRes.setOutcome(StOutcome.OK);

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaSendRTRes> response = objectFactory.createPaSendRTRes(paVerifyPaymentNoticeRes);

		return response;
	}

	private <T> T buildRisposta(Exception e, String codDominio, T risposta) {
		return this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, e.getMessage(), codDominio, e), risposta);
	}
	
	private <T> T buildRisposta(NdpException e, T risposta) {
		if(risposta instanceof PaVerifyPaymentNoticeRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaVerifyPaymentNotice: " + e.getMessage(), e);
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
				log.warn("Errore in PaGetPayment: " + e.getMessage(), e);
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
				log.warn("Errore in PaSendRT: " + e.getMessage(), e);
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
