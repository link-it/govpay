package it.govpay.pagamento.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.pagamento.v1.beans.Avviso;
import it.govpay.pagamento.v1.beans.converter.PendenzeConverter;

public class AvvisiController extends BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response avvisiIdDominioIuvGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String idDebitore) {
    	String methodName = "avvisiIdDominioIuvGET";  
		IContext ctx = null;
		String transactionId = null;

		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, iuv);
			getAvvisoDTO.setAccessoAnonimo(true);
			getAvvisoDTO.setCfDebitore(idDebitore);
			
			String accept = "";
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				this.logResponse(uriInfo, httpHeaders, methodName, getAvvisoDTOResponse.getAvvisoPdf(), 200);
				this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				Avviso avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				this.logResponse(uriInfo, httpHeaders, methodName, avviso.toJSON(null), Status.OK.getStatusCode());
				this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
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


