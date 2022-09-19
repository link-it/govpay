package it.govpay.netpay.v1.api.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.serialization.JsonJacksonDeserializer;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.netpay.v1.api.RicevuteApi;
import it.govpay.netpay.v1.beans.ErrorReason;
import it.govpay.netpay.v1.beans.ErrorReasonAgID;
import it.govpay.netpay.v1.beans.GetRTReceipt;
import it.govpay.netpay.v1.beans.GetRTRequest;
import it.govpay.netpay.v1.beans.GetRTResponse;
import it.govpay.netpay.v1.beans.GetRTResponse.ResultEnum;
import it.govpay.netpay.v1.beans.converter.RicevuteConverter;


/**
 * GovPay - Net@Pay API RT e Attiva Pagamento
 *
 * <p>API di integrazione a Net@Pay esposte da GovPay per:  - Consultare le ricevute di pagamento  - Avvio di un pagamento di pendenze   ## Ricevute Telematiche  La piattaforma mette a disposizione un servizio per la lettura delle ricevute per i pagamenti andati a buon fine.   ## Attivazione Pagamenti  L'operazione consente di avviare una sessione di pagamento per una pendenza.
 *
 */
public class RicevuteApiServiceImpl extends BaseApiServiceImpl implements RicevuteApi {
    
	public RicevuteApiServiceImpl() {
    	super("ricevute", RicevuteApiServiceImpl.class);
	}

	/**
     * Consultazione di una ricevuta di pagamento
     *
     */
    public Response getRt(InputStream is) {
    	this.buildContext();
    	Authentication user = this.getUser();
    	String methodName = "getRt";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		
		GetRTResponse response = new GetRTResponse();
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			GetRTRequest getRTRequest = JsonJacksonDeserializer.parse(baos.toString(), GetRTRequest.class);
			getRTRequest.validate();
			
			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(getRTRequest.getDomainId());
			leggiRptDTO.setIuv(getRTRequest.getCreditorTxId());
//			idRicevuta = idRicevuta.contains("%") ? URLDecoder.decode(idRicevuta,"UTF-8") : idRicevuta;
//			leggiRptDTO.setCcp(idRicevuta);

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			GetRTReceipt getRTReceipt =  RicevuteConverter.toRsModel(leggiRptDTOResponse.getRpt());
			response.setResult(ResultEnum.OK);
			response.setReceipt(getRTReceipt); 

			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response),transactionId).build();
		} catch (ValidationException e) {
			this.log.warn("Richiesta rifiutata per errori di validazione: " + e);
			
			response.setResult(ResultEnum.KO);
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.WS_PARAMETRI_MANCANTI);
			response.setErrorMessage(e.getMessage());
			
			return this.handleResponseKo(Response.status(Status.BAD_REQUEST).entity(response),transactionId).build();
		} catch (RicevutaNonTrovataException e) {
			this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
			
			response.setResult(ResultEnum.KO);
			response.setErrorReasonAgID(ErrorReasonAgID.RT_SCONOSCIUTA);
			response.setErrorReason(ErrorReason.PDP_RPT_RT_NON_TROVATA);
			response.setErrorMessage(e.getMessage());
			
			return this.handleResponseKo(Response.status(Status.NOT_FOUND).entity(response),transactionId).build();
		} catch (CodificaInesistenteException | UnsupportedEncodingException e) {
			this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
			
			response.setResult(ResultEnum.KO);
			response.setErrorReasonAgID(ErrorReasonAgID.SYSTEM_ERROR);
			response.setErrorReason(ErrorReason.WS_GENERIC_ERROR);
			response.setErrorMessage(e.getMessage());
			
			return this.handleResponseKo(Response.status(Status.BAD_REQUEST).entity(response),transactionId).build();
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

