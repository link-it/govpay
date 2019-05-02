package it.govpay.ragioneria.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.IncassiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v1.beans.Incasso;
import it.govpay.ragioneria.v1.beans.IncassoIndex;
import it.govpay.ragioneria.v1.beans.IncassoPost;
import it.govpay.ragioneria.v1.beans.ListaIncassiIndex;
import it.govpay.ragioneria.v1.beans.converter.IncassiConverter;



public class IncassiController extends BaseController {
	
	public IncassiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
	}


    public Response incassiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
    	String methodName = "incassiGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		String campi = null;
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));
			
			ListaIncassiDTO listaIncassoDTO = new ListaIncassiDTO(user);
			
			listaIncassoDTO.setLimit(risultatiPerPagina);
			listaIncassoDTO.setPagina(pagina);
			
			// filtro sull'applicazione			
			listaIncassoDTO.setIdA2A(AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione()); 
			
			// autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user); 
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaIncassoDTO.setCodDomini(domini);
			
			IncassiDAO incassiDAO = new IncassiDAO();
			ListaIncassiDTOResponse listaIncassiDTOResponse = incassiDAO.listaIncassi(listaIncassoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<IncassoIndex> listaIncassi = new ArrayList<>();
			for(it.govpay.bd.model.Incasso i : listaIncassiDTOResponse.getResults()) {
				listaIncassi.add(IncassiConverter.toRsIndexModel(i));
			}
			
			ListaIncassiIndex response = new ListaIncassiIndex(listaIncassi, this.getServicePath(uriInfo),
					listaIncassiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


    public Response incassiIdDominioIdIncassoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idIncasso) {
    	String methodName = "incassiIdDominioIdIncassoGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			LeggiIncassoDTO leggiIncassoDTO = new LeggiIncassoDTO(user);
			leggiIncassoDTO.setIdDominio(idDominio);
			leggiIncassoDTO.setIdIncasso(idIncasso);

			if(!AuthorizationManager.isDominioAuthorized(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio(), null);
			}
			
			IncassiDAO incassiDAO = new IncassiDAO();
			
			// CHIAMATA AL DAO
			
			LeggiIncassoDTOResponse leggiIncassoDTOResponse = incassiDAO.leggiIncasso(leggiIncassoDTO);
			
			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(leggiIncassoDTOResponse.getIncasso().getApplicazione(null).getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Incasso response = IncassiConverter.toRsModel(leggiIncassoDTOResponse.getIncasso());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

    public Response incassiIdDominioPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
    	String methodName = "incassiIdDominioPOST"; 
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			IncassoPost incasso = JSONSerializable.parse(baos.toString(), IncassoPost.class);
			incasso.validate();
			
			RichiestaIncassoDTO richiestaIncassoDTO = IncassiConverter.toRichiestaIncassoDTO(incasso, idDominio, user);
			
			IncassiDAO incassiDAO = new IncassiDAO();
			
			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassiDAO.richiestaIncasso(richiestaIncassoDTO);
			
			Incasso incassoExt = IncassiConverter.toRsModel(richiestaIncassoDTOResponse.getIncasso());
			
			Status responseStatus = richiestaIncassoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, incassoExt.toJSON(null), responseStatus.getStatusCode());
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(incassoExt.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
}


