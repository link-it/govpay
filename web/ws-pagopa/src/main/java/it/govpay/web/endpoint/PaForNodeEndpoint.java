package it.govpay.web.endpoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import it.gov.pagopa.pagopa_api.pa.pafornode.CtFaultBean;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtQrCode;
import it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.govpay.web.business.Applicazione;
import it.govpay.web.entity.ApplicazioneEntity;
import it.govpay.web.entity.DominioEntity;
import it.govpay.web.entity.IntermediarioEntity;
import it.govpay.web.entity.PagamentoEntity;
import it.govpay.web.entity.SingoloVersamentoEntity;
import it.govpay.web.entity.StazioneEntity;
import it.govpay.web.entity.VersamentoEntity;
import it.govpay.web.entity.VersamentoEntity.StatoVersamento;
import it.govpay.web.exception.NdpException;
import it.govpay.web.exception.NdpException.FaultPa;
import it.govpay.web.filters.DominioFilter;
import it.govpay.web.filters.IntermediarioFilter;
import it.govpay.web.filters.StazioneFilter;
import it.govpay.web.filters.VersamentoFilter;
import it.govpay.web.repository.DominioRepository;
import it.govpay.web.repository.IntermediarioRepository;
import it.govpay.web.repository.PagamentoRepository;
import it.govpay.web.repository.StazioneRepository;
import it.govpay.web.repository.VersamentoRepository;
import it.govpay.web.utils.IuvUtils;

@Endpoint
public class PaForNodeEndpoint {
	
	private static Logger log = LoggerFactory.getLogger(PaForNodeEndpoint.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private static final String NAMESPACE_URI = "http://pagopa-api.pagopa.gov.it/pa/paForNode.xsd";
	
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


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paVerifyPaymentNoticeReq")
	@ResponsePayload
	public JAXBElement<PaVerifyPaymentNoticeRes> paVerifyPaymentNotice(@RequestPayload JAXBElement<PaVerifyPaymentNoticeReq> request) {
		PaVerifyPaymentNoticeReq requestBody = request.getValue();
		
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
		//String idDominio = requestBody.getIdPA();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		
		PaVerifyPaymentNoticeRes response = new PaVerifyPaymentNoticeRes();
		
		try {
			String iuv = IuvUtils.toIuv(numeroAvviso);
			
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			IntermediarioFilter intermediarioFilter = new IntermediarioFilter();
			intermediarioFilter.setCodIntermediario(Optional.of(codIntermediario));
			Optional<IntermediarioEntity> findIntermediario = this.intermediarioRepository.findOne(intermediarioFilter);
			
			if(findIntermediario.isEmpty()) {
				// lanciare errore PAA_ID_INTERMEDIARIO_ERRATO
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			
			// Controllo autenticazione
			
			// leggo stazione
			StazioneFilter stazioneFilter = new StazioneFilter();
			stazioneFilter.setCodStazione(Optional.of(codStazione));
			Optional<StazioneEntity> findStazione = this.stazioneRepository.findOne(stazioneFilter);
			
			if(findStazione.isEmpty()) {
				// lanciare errore PAA_STAZIONE_INT_ERRATA
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}
			
			// leggo dominio
			DominioFilter dominioFilter = new DominioFilter();
			dominioFilter.setCodDominio(Optional.of(codDominio));
			Optional<DominioEntity> findDominio = this.dominioRepository.findOne(dominioFilter);
			
			if(findDominio.isEmpty()) {
				// lanciare errore PAA_ID_DOMINIO_ERRATO
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			
			DominioEntity dominio = findDominio.get();
			
			// lettura del versamento
			VersamentoFilter versamentoFilter = new VersamentoFilter();
			versamentoFilter.setCodDominio(Optional.of(codDominio));
			versamentoFilter.setIuv(Optional.of(iuv));
			Optional<VersamentoEntity> findVersamento = this.versamentoRepository.findOne(versamentoFilter);
			
			VersamentoEntity versamento = null;
			ApplicazioneEntity applicazioneGestisceIuv = null;
			if(findVersamento.isEmpty()) {
				// cerco l'applicazione per gestire lo iuv
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					// non ho trovato un'applicazione in grado di gestire il versamento allora termino
//					ctx.getApplicationLogger().log("ccp.iuvNonPresenteNoAppGestireIuv");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
			} else {
				versamento = findVersamento.get();
			}
			
			if(versamento == null) { // Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
				
			} else {
				// Versamento trovato, gestisco un'eventuale scadenza
				versamento = VersamentoUtils.aggiornaVersamento(versamento);

				if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
					throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

				if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
						List<SingoloVersamentoEntity> singoliVersamenti = new ArrayList<SingoloVersamentoEntity>(versamento.getSingoliVersamenti());
						List<PagamentoEntity> pagamenti = this.pagamentoRepository.getPagamentiBySingoloVersamento(singoliVersamenti.get(0).getId());
						if(pagamenti.isEmpty())
							throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
						else {
							PagamentoEntity pagamento = pagamenti.get(0);
							throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
						}
							
					}
				}
			}
			
			
			
			response.setOutcome(StOutcome.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
		} finally {
			
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
