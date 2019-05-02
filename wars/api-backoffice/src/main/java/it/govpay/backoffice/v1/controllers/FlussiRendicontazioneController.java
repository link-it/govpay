package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.FlussoRendicontazione;
import it.govpay.backoffice.v1.beans.ListaFlussiRendicontazione;
import it.govpay.backoffice.v1.beans.converter.FlussiRendicontazioneConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class FlussiRendicontazioneController extends BaseController {

     public FlussiRendicontazioneController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response flussiRendicontazioneIdFlussoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idFlusso) {
    	String methodName = "flussiRendicontazioneIdFlussoGET";  
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
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));
			
			String accept = null;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			// Parametri - > DTO Input
			
			LeggiRendicontazioneDTO leggiRendicontazioneDTO = new LeggiRendicontazioneDTO(user, idFlusso);
			
			// INIT DAO
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRendicontazioneDTOResponse leggiRendicontazioneDTOResponse = rendicontazioniDAO.leggiRendicontazione(leggiRendicontazioneDTO);
					
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRendicontazioneDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,leggiRendicontazioneDTOResponse.getDominio().getCodDominio(), null);
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			if(accept.toLowerCase().contains(MediaType.APPLICATION_XML)) {
				byte[] response = leggiRendicontazioneDTOResponse.getFr().getXml();
				this.logResponse(uriInfo, httpHeaders, methodName, response, 200);
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(new String(response)).type(MediaType.APPLICATION_XML),transactionId).build();
			} else {
				FlussoRendicontazione response = FlussiRendicontazioneConverter.toRsModel(leggiRendicontazioneDTOResponse.getFr()); 
				this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)).type(MediaType.APPLICATION_JSON),transactionId).build();
			}
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response flussiRendicontazioneGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String ordinamento, String dataDa, String dataA) {
    	String methodName = "flussiRendicontazioneGET";  
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
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			if(idDominio != null)
				validatoreId.validaIdDominio("idDominio", idDominio);
			
			ListaRendicontazioniDTO findRendicontazioniDTO = new ListaRendicontazioniDTO(user);
			findRendicontazioniDTO.setIdDominio(idDominio);
			findRendicontazioniDTO.setLimit(risultatiPerPagina);
			findRendicontazioniDTO.setPagina(pagina);
			findRendicontazioniDTO.setOrderBy(ordinamento);
			if(dataDa != null)
				findRendicontazioniDTO.setDataDa(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(dataDa)); 
			if(dataA != null)
				findRendicontazioniDTO.setDataA(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(dataA));
			
			// Autorizzazione sui domini
			List<String> domini  = AuthorizationManager.getDominiAutorizzati(user);
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			findRendicontazioniDTO.setCodDomini(domini);
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRendicontazioniDTOResponse findRendicontazioniDTOResponse = rendicontazioniDAO.listaRendicontazioni(findRendicontazioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaFlussiRendicontazione response = new ListaFlussiRendicontazione(findRendicontazioniDTOResponse.getResults().stream().map(t -> FlussiRendicontazioneConverter.toRsIndexModel(t.getFr())).collect(Collectors.toList()), 
					this.getServicePath(uriInfo), findRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


