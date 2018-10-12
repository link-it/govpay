package it.govpay.rs.v1.controllers.pagamenti;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.rs.v1.beans.pagamenti.PendenzaVerificata;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.converter.PendenzeConverter;

public class AvvisiController extends it.govpay.rs.BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response avvisiIdDominioIuvGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String numeroAvviso) {
		String methodName = "getByIdDominioNumeroAvviso";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

			leggiPendenzaDTO.setIdDominio(idDominio);
			leggiPendenzaDTO.setNumeroAvviso(numeroAvviso);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			PendenzaVerificata pendenzaVerificata = PendenzeConverter.toPendenzaVerificataModel(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getDominio(), ricevutaDTOResponse.getApplicazione());
					this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					
					return this.handleResponseOk(Response.status(Status.OK).entity(pendenzaVerificata.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

}


