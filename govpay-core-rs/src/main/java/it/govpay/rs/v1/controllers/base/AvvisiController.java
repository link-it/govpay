package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.rs.v1.beans.base.Avviso;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.converter.PendenzeConverter;

public class AvvisiController extends it.govpay.rs.BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response avvisiIdDominioIuvGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv) {
    	String methodName = "avvisiIdDominioIuvGET";  
		GpContext ctx = null;
		String transactionId = null;

		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, iuv);
			
			String accept = null;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			
			
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				this.logResponse(uriInfo, httpHeaders, methodName, getAvvisoDTOResponse.getAvvisoPdf(), 200);
				this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				Avviso avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				this.logResponse(uriInfo, httpHeaders, methodName, avviso.toJSON(null), Status.OK.getStatusCode());
				this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(avviso.toJSON(null)),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }
}

