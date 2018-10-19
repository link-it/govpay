package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.cache.AclCache;
import it.govpay.core.dao.anagrafica.RuoliDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PatchRuoloDTO;
import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.ListaAcl;
import it.govpay.core.rs.v1.beans.base.ListaRuoli;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.core.rs.v1.beans.base.PatchOp.OpEnum;
import it.govpay.core.rs.v1.beans.base.Ruolo;
import it.govpay.core.rs.v1.beans.base.RuoloIndex;
import it.govpay.core.rs.v1.beans.base.RuoloPost;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.AclConverter;
import it.govpay.rs.v1.beans.converter.RuoliConverter;



public class RuoliController extends BaseController {

     public RuoliController(String nomeServizio,Logger log) {
 		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
     }

    public Response ruoliGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "ruoliGET";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// Parametri - > DTO Input

			ListaRuoliDTO listaRptDTO = new ListaRuoliDTO(user);
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setLimit(risultatiPerPagina);

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
			this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response ruoliIdRuoloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
		String methodName = "ruoliIdRuoloGET";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

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
				results.add(AclConverter.toRsModel(leggiRuoloDtoResponse));
			}
			ListaAcl response = new ListaAcl();
			response.setAcl(results);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    @SuppressWarnings("unchecked")
	public Response ruoliIdRuoloPATCH(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idRuolo) {
    	String methodName = "ruoliIdRuoloPATCH";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try (ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
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
			
			ruoloPatchDTO.setOp(lstOp);
			RuoliDAO ruoliDAO = new RuoliDAO(false);
			ruoliDAO.patch(ruoloPatchDTO);
			
			// reload ruolo nella cache
			AclCache.getInstance().reloadRuolo(idRuolo); 
			
			LeggiRuoloDTO leggiRuoliDTO = new LeggiRuoloDTO(user);
			leggiRuoliDTO.setRuolo(idRuolo);
			LeggiRuoloDTOResponse leggiRuoloDTOResponse = ruoliDAO.leggiRuoli(leggiRuoliDTO);
			
			Ruolo response = RuoliConverter.toRsModel(idRuolo,leggiRuoloDTOResponse.getResults());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    public Response ruoliIdRuoloPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
    	String methodName = "ruoliIdRuoloPUT";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();

			RuoloPost ruoloPost = RuoloPost.parse(jsonRequest);
			List<AclPost> listaAcl = ruoloPost.getAcl();

			PutRuoloDTO putRuoloDTO = RuoliConverter.getPutRuoloDTO(listaAcl, idRuolo, user); 
			
			RuoliDAO applicazioniDAO = new RuoliDAO(false);
			
			PutRuoloDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putRuoloDTO);
			
			if(!putApplicazioneDTOResponse.isCreated()) {
				// 	reload ruolo nella cache
				AclCache.getInstance().reloadRuolo(idRuolo);
			}
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }
}


