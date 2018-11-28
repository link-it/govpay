package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;

import it.govpay.backoffice.v1.beans.converter.PagamentiPortaleConverter;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentoPatchDTO;
import it.govpay.backoffice.v1.beans.ListaPagamentiPortale;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.IAutorizzato;



public class PagamentiController extends BaseController {

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response pagamentiIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getPagamentoPortaleById";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setId(id);
			leggiPagamentoPortaleDTO.setRisolviLink(true);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.backoffice.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response pagamentiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String stato, String versante, String idSessionePortale, Boolean verificato, String dataDa, String dataA) {
    	String methodName = "getListaPagamenti";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
//			String principal = this.getPrincipal();
			
			// Parametri - > DTO Input
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
			listaPagamentiPortaleDTO.setPagina(pagina);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			listaPagamentiPortaleDTO.setStato(stato);
			
			if(dataDa!=null) {
				Date dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPagamentiPortaleDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPagamentiPortaleDTO.setDataA(dataADate);
			}
			
			listaPagamentiPortaleDTO.setVerificato(verificato);
			
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			
			if(idSessionePortale != null)
				listaPagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.PagamentoIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.PagamentoPortale pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				results.add(PagamentiPortaleConverter.toRsModelIndex(pagamentoPortale));
			}
			
			ListaPagamentiPortale response = new ListaPagamentiPortale(results, this.getServicePath(uriInfo),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    @SuppressWarnings("unchecked")
	public Response pagamentiIdPATCH(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String id) {
    	String methodName = "pagamentiIdPATCH";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();

			PagamentoPatchDTO verificaPagamentoDTO = new PagamentoPatchDTO(user);
			verificaPagamentoDTO.setIdSessione(id);
			
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
			} catch (ServiceException e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}
			
			verificaPagamentoDTO.setOp(PatchOpConverter.toModel(lstOp));

			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.patch(verificaPagamentoDTO);
			
			it.govpay.backoffice.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleDTOResponse);
			

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

}


