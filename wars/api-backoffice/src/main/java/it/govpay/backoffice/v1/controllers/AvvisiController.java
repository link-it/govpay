package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Avviso;
import it.govpay.backoffice.v1.beans.converter.PendenzeConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AvvisiController extends BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response avvisiIdDominioIuvGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String numeroAvviso) {
    	String methodName = "avvisiIdDominioIuvGET";  
		IContext ctx = null;
		String transactionId = null;

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, numeroAvviso);
			
			String accept = "";
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			if(!AuthorizationManager.isDominioAuthorized(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio(),null);
			}
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				this.logResponse(uriInfo, httpHeaders, methodName, getAvvisoDTOResponse.getAvvisoPdf(), 200);
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				Avviso avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				this.logResponse(uriInfo, httpHeaders, methodName, avviso.toJSON(null), Status.OK.getStatusCode());
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(avviso.toJSON(null)),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
}

