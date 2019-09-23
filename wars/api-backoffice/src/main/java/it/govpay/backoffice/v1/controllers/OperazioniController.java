package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaOperazioni;
import it.govpay.backoffice.v1.beans.converter.OperazioniConverter;
import it.govpay.core.dao.operazioni.OperazioniDAO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTO;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTOResponse;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class OperazioniController extends BaseController {

    public OperazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
    }
    
    public Response findOperazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "findOperazioni";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			ListaOperazioniDTO listaOperazioniDTO = new ListaOperazioniDTO(user);
			listaOperazioniDTO.setLimit(risultatiPerPagina);
			listaOperazioniDTO.setPagina(pagina);
			
			if(ordinamento != null)
				listaOperazioniDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			OperazioniDAO operazioniDAO = new OperazioniDAO(); 
			
			// CHIAMATA AL DAO
			
			ListaOperazioniDTOResponse listaOperazioniDTOResponse = operazioniDAO.listaOperazioni(listaOperazioniDTO); 
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.OperazioneIndex> results = new ArrayList<>();
			for(LeggiOperazioneDTOResponse pagamentoPortale: listaOperazioniDTOResponse.getResults()) {
				results.add(OperazioniConverter.toRsModelIndex(pagamentoPortale));
			}
			
			ListaOperazioni response = new ListaOperazioni(results, this.getServicePath(uriInfo),
					listaOperazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }

    public Response getOperazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getOperazione";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));
			
			LeggiOperazioneDTO leggiOperazioneDTO = new LeggiOperazioneDTO(user);
			leggiOperazioneDTO.setIdOperazione(id);
			
			OperazioniDAO operazioniDAO = new OperazioniDAO(); 
			
			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = operazioniDAO.eseguiOperazione(leggiOperazioneDTO);
			
			it.govpay.backoffice.v1.beans.Operazione response = OperazioniConverter.toRsModel(leggiOperazioneDTOResponse);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }

    public Response getStatoOperazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getStatoOperazione";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			throw new NotImplementedException("Not implemented");
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }

}
