package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaTracciati;
import it.govpay.backoffice.v1.beans.converter.TracciatiConverter;
import it.govpay.bd.model.Tracciato;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.TracciatiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class TracciatiController extends BaseController {

     public TracciatiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findTracciati(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "findTracciati";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaTracciatiDTO listaTracciatiDTO = new ListaTracciatiDTO(user);
			listaTracciatiDTO.setLimit(risultatiPerPagina);
			listaTracciatiDTO.setPagina(pagina);
			List<TIPO_TRACCIATO> tipo = new ArrayList<>();
			tipo.add(TIPO_TRACCIATO.AV);
			tipo.add(TIPO_TRACCIATO.AV_ESITO);
			listaTracciatiDTO.setTipoTracciato(tipo); 

			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaTracciatiDTO.setCodDomini(domini);

			TracciatiDAO tracciatiDAO = new TracciatiDAO();

			// CHIAMATA AL DAO

			ListaTracciatiDTOResponse listaTracciatiDTOResponse = tracciatiDAO.listaTracciati(listaTracciatiDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<it.govpay.backoffice.v1.beans.Tracciato> results = new ArrayList<>();
			for(Tracciato tracciato: listaTracciatiDTOResponse.getResults()) {
				results.add(TracciatiConverter.toRsModel(tracciato));
			}
			ListaTracciati response = new ListaTracciati(results, this.getServicePath(uriInfo), listaTracciatiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response getTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getTracciato";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			it.govpay.backoffice.v1.beans.Tracciato rsModel = TracciatiConverter.toRsModel(tracciato);
			return this.handleResponseOk(Response.status(Status.OK).entity(rsModel.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		} 
    }



    public Response getMessaggioRichiestaTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getMessaggioRichiestaTracciato";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 


		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			byte[] richiesta = tracciatiDAO.leggiRichiestaTracciato(leggiTracciatoDTO);
			return this.handleResponseOk(Response.status(Status.OK).entity(richiesta),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		} 
    }



    public Response getMessaggioRispostaTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getMessaggioRispostaTracciato";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			byte[] esito = tracciatiDAO.leggiEsitoTracciato(leggiTracciatoDTO);
			return this.handleResponseOk(Response.status(Status.OK).entity(esito),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		} 
    }


}


