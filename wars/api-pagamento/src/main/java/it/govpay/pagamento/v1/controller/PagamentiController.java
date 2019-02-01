package it.govpay.pagamento.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.pagamento.v1.beans.FaultBean;
import it.govpay.pagamento.v1.beans.FaultBeanEsteso;
import it.govpay.pagamento.v1.beans.FaultBeanEsteso.CategoriaEnum;
import it.govpay.pagamento.v1.beans.ListaPagamentiIndex;
import it.govpay.pagamento.v1.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pagamento.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.pagamento.v1.beans.PagamentoPost;
import it.govpay.pagamento.v1.beans.PendenzaIndex;
import it.govpay.pagamento.v1.beans.RppIndex;
import it.govpay.pagamento.v1.beans.converter.PagamentiPortaleConverter;
import it.govpay.pagamento.v1.beans.converter.PendenzeConverter;
import it.govpay.pagamento.v1.beans.converter.RptConverter;



public class PagamentiController extends BaseController {

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
     }


    public Response pagamentiPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idSessionePortale, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
    	String methodName = "pagamentiPOST";  
		IContext ctx = null;
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
			PagamentoPost pagamentiPortaleRequest= JSONSerializable.parse(jsonRequest, PagamentoPost.class);
			pagamentiPortaleRequest.validate();
			
			
			
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, jsonRequest, user,idSession, idSessionePortale, avvisaturaDigitale,modalitaAvvisaturaDigitale);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentiPortaleResponseOk responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, responseOk.toJSON(null), Status.CREATED.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.CREATED).entity(responseOk.toJSON(null)),transactionId).build();
		} catch (Exception e) {
			Response response = this.handleException(uriInfo, httpHeaders, methodName, e,transactionId);
			if(e instanceof GovPayException && (PagamentoPortale) ((GovPayException) e).getParam() != null) {
				try {
					FaultBean fb = JSONSerializable.parse((String) response.getEntity(), FaultBean.class);
					FaultBeanEsteso fbe = new FaultBeanEsteso();
					fbe.setCodice(fb.getCodice());
					fbe.setCategoria(CategoriaEnum.fromValue(fb.getCategoria().toString()));
					fbe.setDescrizione(fb.getDescrizione());
					fbe.setDettaglio(fb.getDettaglio());
					fbe.setId(((PagamentoPortale) ((GovPayException) e).getParam()).getIdSessione());
					fbe.setLocation(UriBuilderUtils.getFromPagamenti(fbe.getId()));
					return Response.fromResponse(response).entity(fbe.toJSON(null)).build();
				} catch (Exception e1) {
					return Response.fromResponse(response).build();
				}
			}
			return response;
		} finally {
			this.log(ctx);
		}
    }
    
    public Response pagamentiIdSessionGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idSessione) {
    	String methodName = "getPagamentoPortaleById";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setIdSessionePsp(idSessione);
			leggiPagamentoPortaleDTO.setRisolviLink(true); 
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.pagamento.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel);
			
			List<RppIndex> rpp = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
			}
			response.setRpp(rpp);

			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiPendenzaDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
				pendenze.add(PendenzeConverter.toRsModelIndex(leggiRptDtoResponse.getVersamentoIncasso()));
			}
			response.setPendenze(pendenze);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
    
    public Response pagamentiIdGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getPagamentoPortaleById";  
		IContext ctx = null;
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
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.pagamento.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel);
			
			List<RppIndex> rpp = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
			}
			response.setRpp(rpp);

			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiPendenzaDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
				pendenze.add(PendenzeConverter.toRsModelIndex(leggiRptDtoResponse.getVersamentoIncasso()));
			}
			response.setPendenze(pendenze);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

    public Response pagamentiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String stato, String versante, String idSessionePortale, String idSessionePsp) {
    	String methodName = "getListaPagamenti";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
			listaPagamentiPortaleDTO.setPagina(pagina);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			listaPagamentiPortaleDTO.setStato(stato);
			listaPagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
			listaPagamentiPortaleDTO.setIdSessionePsp(idSessionePsp);
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v1.beans.PagamentoIndex> results = new ArrayList<>();
			for(LeggiPagamentoPortaleDTOResponse pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				this.log.info("get Pagamenti portale: " + pagamentoPortale.getPagamento().getIdSessione());
				results.add(PagamentiPortaleConverter.toRsModelIndex(pagamentoPortale));
			}
			
			ListaPagamentiIndex response = new ListaPagamentiIndex(results, this.getServicePath(uriInfo),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

}


