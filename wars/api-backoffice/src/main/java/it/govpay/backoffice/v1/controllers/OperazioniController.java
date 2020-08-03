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
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaOperazioni;
import it.govpay.backoffice.v1.beans.converter.OperazioniConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.operazioni.OperazioniDAO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTO;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTOResponse;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class OperazioniController extends BaseController {

    public OperazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
    }
    
    public Response findOperazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "findOperazioni";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
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
			this.log(ContextThreadLocal.get());
		}
    }

    public Response getOperazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getOperazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName + ": " + id)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));
			
			LeggiOperazioneDTO leggiOperazioneDTO = new LeggiOperazioneDTO(user);
			leggiOperazioneDTO.setIdOperazione(id);
			
			OperazioniDAO operazioniDAO = new OperazioniDAO(); 
			
			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = operazioniDAO.eseguiOperazione(leggiOperazioneDTO);
			
			this.log.trace(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_OPERAZIONE_COMPLETATA, methodName + ": " + id, leggiOperazioneDTOResponse.getStato(), leggiOperazioneDTOResponse.getDescrizioneStato())); 
			
			it.govpay.backoffice.v1.beans.Operazione response = OperazioniConverter.toRsModel(leggiOperazioneDTOResponse);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName + ": " + id)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response getStatoOperazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getStatoOperazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			throw new NotImplementedException("Not implemented");
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

}
