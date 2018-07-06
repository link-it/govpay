package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.JSONUtils;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import it.govpay.core.dao.pagamenti.RuoliDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PutRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.PutRuoloDTOResponse;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.ListaAcl;
import it.govpay.core.rs.v1.beans.base.ListaRuoli;
import it.govpay.core.rs.v1.beans.base.RuoloIndex;
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
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// Parametri - > DTO Input

			ListaRuoliDTO listaRptDTO = new ListaRuoliDTO(user);
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setLimit(risultatiPerPagina);

			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO();

			// CHIAMATA AL DAO

			ListaRuoliDTOResponse listaRptDTOResponse = rptDAO.listaRuoli(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RuoloIndex> results = new ArrayList<RuoloIndex>();
			for(String leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RuoliConverter.toRsModelIndex(leggiRuoloDtoResponse));
			}
			ListaRuoli response = new ListaRuoli(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response ruoliIdRuoloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
		String methodName = "ruoliIdRuoloGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// Parametri - > DTO Input

			LeggiRuoloDTO leggiRuoloDTO = new LeggiRuoloDTO(user);

			
			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO();

			// CHIAMATA AL DAO

			LeggiRuoloDTOResponse listaRptDTOResponse = rptDAO.leggiRuoli(leggiRuoloDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<AclPost> results = new ArrayList<AclPost>();
			for(Acl leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(AclConverter.toRsModel(leggiRuoloDtoResponse));
			}
			ListaAcl response = new ListaAcl();
			response.setAcl(results);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    public Response ruoliIdRuoloPATCH(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idRuolo) {
//    	String methodName = "ruoliIdRuoloPATCH";  
//		GpContext ctx = null;
//		String transactionId = null;
//		ByteArrayOutputStream baos= null;
//		this.log.info("Esecuzione " + methodName + " in corso..."); 
//		try{
//			baos = new ByteArrayOutputStream();
//			// salvo il json ricevuto
//			BaseRsService.copy(is, baos);
//			this.logRequest(uriInfo, httpHeaders, methodName, baos);
//			
//			ctx =  GpThreadLocal.get();
//			transactionId = ctx.getTransactionId();
//			
//			String jsonRequest = baos.toString();
//
//			RuoloPatchDTO ruoloPatchDTO = new RuoloPatchDTO(user);
//			ruoloPatchDTO.setIdRuolo(idRuolo);
//			
//			List<java.util.LinkedHashMap<?,?>> lst = PatchOp.parse(jsonRequest, List.class);
//			List<PatchOp> lstOp = new ArrayList<>();
//			for(java.util.LinkedHashMap<?,?> map: lst) {
//				PatchOp op = new PatchOp();
//				op.setOp(OpEnum.fromValue((String) map.get("op")));
//				op.setPath((String) map.get("path"));
//				op.setValue(map.get("value"));
//				lstOp.add(op);
//			}
//			ruoloPatchDTO.setOp(lstOp );
//
//			RuoliDAO ruoliDAO = new RuoliDAO();
//			
//			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = ruoliDAO.patch(ruoloPatchDTO);
//			
//			it.govpay.core.rs.v1.beans.base.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleDTOResponse);
//			
//
//			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
//			this.log.info("Esecuzione " + methodName + " completata."); 
//			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
//			
//		}catch (Exception e) {
//			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
//		} finally {
//			if(ctx != null) ctx.log();
//		}
    	return null;
    }


    public Response ruoliIdRuoloPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
    	String methodName = "ruoliIdRuoloPUT";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();

			List<AclPost> listaAcl = deserialize(jsonRequest, AclPost.class); 

			PutRuoloDTO putRuoloDTO = RuoliConverter.getPutRuoloDTO(listaAcl, idRuolo, user); 
			
			RuoliDAO applicazioniDAO = new RuoliDAO();
			
			PutRuoloDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putRuoloDTO);
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

	@SuppressWarnings("unchecked")
	private static <T> List<T> deserialize(String json, Class<T> t) throws UtilsException, IOException {

		IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, new SerializationConfig());

		JSONUtils jsonUtils = JSONUtils.getInstance();
		JsonNode asNode = jsonUtils.getAsNode(json);
		ArrayNode array = (ArrayNode) asNode;
		Iterator<JsonNode> iterator = array.iterator();
		
		List<T> lst= new ArrayList<>();
		while(iterator.hasNext()) {
			lst.add((T)deserializer.getObject(jsonUtils.toString(iterator.next()), t));
		}
		return lst;
	}


}


