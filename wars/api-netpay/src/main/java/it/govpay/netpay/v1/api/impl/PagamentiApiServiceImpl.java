package it.govpay.netpay.v1.api.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.core.business.Applicazione;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.serialization.JsonJacksonDeserializer;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.netpay.v1.api.PagamentiApi;
import it.govpay.netpay.v1.beans.ActivePaymentRequest;
import it.govpay.netpay.v1.beans.ActivePaymentResponse;
import it.govpay.netpay.v1.beans.ActivePaymentResponse.ResultEnum;
import it.govpay.netpay.v1.beans.ErrorReason;
import it.govpay.netpay.v1.beans.ErrorReasonAgID;
import it.govpay.netpay.v1.beans.converter.PagamentiConverter;


/**
 * GovPay - Net@Pay API RT e Attiva Pagamento
 *
 * <p>API di integrazione a Net@Pay esposte da GovPay per:  - Consultare le ricevute di pagamento  - Avvio di un pagamento di pendenze   ## Ricevute Telematiche  La piattaforma mette a disposizione un servizio per la lettura delle ricevute per i pagamenti andati a buon fine.   ## Attivazione Pagamenti  L'operazione consente di avviare una sessione di pagamento per una pendenza.
 *
 */
public class PagamentiApiServiceImpl extends BaseApiServiceImpl implements PagamentiApi {
    
	
	public PagamentiApiServiceImpl() {
		super("pagamenti", PagamentiApiServiceImpl.class);
	}

	/**
     * Attivzione di un pagamento verso il WISP 2.0
     *
     */
    public Response activePayment(InputStream is) {
    	this.buildContext();
    	Authentication user = this.getUser();
    	String methodName = "activePayment";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		ActivePaymentResponse response = new ActivePaymentResponse();
		Dominio dominio = null;
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			ActivePaymentRequest activePaymentRequest = JsonJacksonDeserializer.parse(baos.toString(), ActivePaymentRequest.class);
			activePaymentRequest.validate();
			
			String idSession = transactionId.replace("-", "");
			
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, activePaymentRequest.getDomainId());
			} catch (NotFoundException e) {
				response.setResult(ResultEnum.KO);
				response.setErrorReasonAgID(ErrorReasonAgID.DOMINIO_SCONOSCIUTO);
				response.setErrorReason(ErrorReason.DOMAIN_ID_NOT_VALID);
				response.setErrorMessage(e.getMessage());
				return this.handleResponseKo(Response.status(Status.BAD_REQUEST).entity(response),transactionId).build();
			}
			
			// Cerco un'applicazione per gestire il dominio
			it.govpay.bd.model.Applicazione applicazione = new Applicazione().getApplicazioneDominio(configWrapper, dominio, activePaymentRequest.getCreditorTxId());
			
			if(applicazione == null) {
				response.setResult(ResultEnum.KO);
				response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
				response.setErrorReason(ErrorReason.WS_ERRORE_INTERNO);
				response.setErrorMessage("Iuv non censito su GovPay ma nessuna applicazione censita puo' gestirlo.");
				return this.handleResponseKo(Response.status(Status.INTERNAL_SERVER_ERROR).entity(response),transactionId).build();
			}
			
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiConverter.getPagamentiPortaleDTO(activePaymentRequest, applicazione, dominio, user, idSession, this.log);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
				
			response.setResult(ResultEnum.OK);
			response.setRedirectURL(pagamentiPortaleDTOResponse.getRedirectUrl());
			response.setSessionID(activePaymentRequest.getSessionID());
			response.setCreditorTxId(activePaymentRequest.getCreditorTxId());
			
			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.CREATED).entity(response),transactionId).build();
		} catch (ValidationException e) {
			this.log.warn("Richiesta rifiutata per errori di validazione: " + e);
			
			response.setResult(ResultEnum.KO);
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.INVALID_REQUEST);
			response.setErrorMessage(e.getMessage());
			
			return this.handleResponseKo(Response.status(Status.BAD_REQUEST).entity(response),transactionId).build();
		} catch (CodificaInesistenteException | IOException e) {
			this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
			
			response.setResult(ResultEnum.KO);
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.WS_GENERIC_ERROR);
			response.setErrorMessage(e.getMessage());
			
			return this.handleResponseKo(Response.status(Status.INTERNAL_SERVER_ERROR).entity(response),transactionId).build();
		} catch (GovPayException e) {
			this.log.error("Rilevata GovPayException durante l'esecuzione del metodo: "+methodName+", causa: "+ e.getCausa() + ", messaggio: " + e.getMessageV3(), e);
			
			response.setResult(ResultEnum.KO);
			response.setErrorMessage(e.getMessage());
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.WS_GENERIC_ERROR);
			
			return this.handleResponseKo(Response.status(Status.INTERNAL_SERVER_ERROR).entity(response),transactionId).build();
		} catch (Exception e) {
			this.log.error("Errore interno durante "+methodName+": " + e.getMessage(), e);
			
			response.setResult(ResultEnum.KO);
			response.setErrorMessage(e.getMessage());
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.WS_ERRORE_INTERNO);
			
			return this.handleResponseKo(Response.status(Status.INTERNAL_SERVER_ERROR).entity(response),transactionId).build();
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
    
}

