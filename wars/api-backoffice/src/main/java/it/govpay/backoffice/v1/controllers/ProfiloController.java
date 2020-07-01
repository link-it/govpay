package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.backoffice.v1.beans.converter.ProfiloConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ProfiloPatchDTO;

public class ProfiloController extends BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
 		super(nomeServizio,log);
     }

    public Response getProfilo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders) {
    	String methodName = "getProfilo";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			UtentiDAO utentiDAO = new UtentiDAO();
			
			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(user);
			
			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(profilo.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    @SuppressWarnings("unchecked")
	public Response updateProfilo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
    	String methodName = "updateProfilo";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
//			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.SCRITTURA));
			String jsonRequest = baos.toString();

			UtentiDAO utentiDAO = new UtentiDAO(false);
			
			ProfiloPatchDTO profiloPatchDTO = new ProfiloPatchDTO(user);
			
			List<PatchOp> lstOp = new ArrayList<>();
			
			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (Exception e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}
			
			profiloPatchDTO.setOp(PatchOpConverter.toModel(lstOp));
			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.patchProfilo(profiloPatchDTO);
			
			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(profilo.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


