package it.govpay.backoffice.v1.controllers;

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

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.ListaAcl;
import it.govpay.backoffice.v1.beans.ListaRuoli;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.Ruolo;
import it.govpay.backoffice.v1.beans.RuoloIndex;
import it.govpay.backoffice.v1.beans.RuoloPost;
import it.govpay.backoffice.v1.beans.converter.AclConverter;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.backoffice.v1.beans.converter.RuoliConverter;
import it.govpay.bd.model.Acl;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.RuoliDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PatchRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class RuoliController extends BaseController {

     public RuoliController(String nomeServizio,Logger log) {
 		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }

    public Response ruoliGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "ruoliGET";  
		IContext ctx = null;
		String transactionId = null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaRuoliDTO listaRptDTO = new ListaRuoliDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);

			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO(false);

			// CHIAMATA AL DAO

			ListaRuoliDTOResponse listaRptDTOResponse = rptDAO.listaRuoli(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RuoloIndex> results = new ArrayList<>();
			for(String leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RuoliConverter.toRsModelIndex(leggiRuoloDtoResponse));
			}
			ListaRuoli response = new ListaRuoli(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response ruoliIdRuoloGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
		String methodName = "ruoliIdRuoloGET";  
		IContext ctx = null;
		String transactionId = null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdRuolo("idRuolo", idRuolo);

			// Parametri - > DTO Input

			LeggiRuoloDTO leggiRuoloDTO = new LeggiRuoloDTO(user);
			leggiRuoloDTO.setRuolo(idRuolo);
			
			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO(false);

			// CHIAMATA AL DAO

			LeggiRuoloDTOResponse listaRptDTOResponse = rptDAO.leggiRuoli(leggiRuoloDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<AclPost> results = new ArrayList<>();
			for(Acl leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				AclPost aclRsModel = AclConverter.toRsModel(leggiRuoloDtoResponse);
				if(aclRsModel != null)
					results.add(aclRsModel);
			}
			ListaAcl response = new ListaAcl();
			response.setAcl(results);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


    @SuppressWarnings("unchecked")
	public Response ruoliIdRuoloPATCH(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idRuolo) {
    	String methodName = "ruoliIdRuoloPATCH";  
		IContext ctx = null;
		String transactionId = null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();

			PatchRuoloDTO ruoloPatchDTO = new PatchRuoloDTO(user);
			ruoloPatchDTO.setIdRuolo(idRuolo);
			
			
			List<PatchOp> lstOp = new ArrayList<>();
			
			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					op.setOp(OpEnum.fromValue((String) map.get("op")));
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (Exception e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
//				PatchOp op = PatchOp.parse(jsonRequest);
//				op.validate();
//				lstOp.add(op);
			}
			
			ruoloPatchDTO.setOp(PatchOpConverter.toModel(lstOp));
			RuoliDAO ruoliDAO = new RuoliDAO(false);
			ruoliDAO.patch(ruoloPatchDTO);
			
			LeggiRuoloDTO leggiRuoliDTO = new LeggiRuoloDTO(user);
			leggiRuoliDTO.setRuolo(idRuolo);
			LeggiRuoloDTOResponse leggiRuoloDTOResponse = ruoliDAO.leggiRuoli(leggiRuoliDTO);
			
			Ruolo response = RuoliConverter.toRsModel(idRuolo,leggiRuoloDTOResponse.getResults());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


    public Response ruoliIdRuoloPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
    	String methodName = "ruoliIdRuoloPUT";  
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
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.SCRITTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdRuolo("idRuolo", idRuolo);
			
			String jsonRequest = baos.toString();

			RuoloPost ruoloPost = RuoloPost.parse(jsonRequest);
			ruoloPost.validate();
			
			List<AclPost> listaAcl = ruoloPost.getAcl();
			
			PutRuoloDTO putRuoloDTO = RuoliConverter.getPutRuoloDTO(listaAcl, idRuolo, user); 
			
			RuoliDAO applicazioniDAO = new RuoliDAO(false);
			
			PutRuoloDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putRuoloDTO);
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
}


