package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.rs.v1.beans.FaultBean;
import it.govpay.core.rs.v1.beans.ListaPagamentiPortale;
import it.govpay.core.rs.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.core.rs.v1.beans.base.PagamentoPost;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.PagamentiPortaleConverter;
import net.sf.json.JsonConfig;



public class PagamentiController extends it.govpay.rs.BaseController {

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }


    public Response pagamentiPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idSessionePortale) {
    	String methodName = "pagamentiPOST";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			String jsonRequest = baos.toString();
			JsonConfig jsonConfig = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("autenticazioneSoggetto", String.class);
			jsonConfig.setClassMap(classMap);
			PagamentoPost pagamentiPortaleRequest= (PagamentoPost) PagamentoPost.parse(jsonRequest, PagamentoPost.class, jsonConfig);
			
			String transactionId = ctx.getTransactionId();
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, jsonRequest, user,idSession, idSessionePortale);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentiPortaleResponseOk responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, responseOk.toJSON(null), 201);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.CREATED).entity(responseOk.toJSON(null)).build();
		} catch(GovPayException e) {
			log.error("Errore durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice(e.getCodEsito().name());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response pagamentiIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getPagamentoPortaleById";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setIdSessione(id);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.core.rs.v1.beans.PagamentoPortale response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
		}catch (PagamentoPortaleNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		}catch (Exception e) {
			log.error("Errore interno durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    public Response pagamentiIdDominioIuvPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, java.io.InputStream is) {
    	//client
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response pagamentiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String stato, String versante, String idSessionePortale) {
    	String methodName = "getListaPagamenti";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
//			String principal = this.getPrincipal();
			
			// Parametri - > DTO Input
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
			listaPagamentiPortaleDTO.setPagina(pagina);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			
			if(stato != null)
				listaPagamentiPortaleDTO.setStato(STATO.valueOf(stato));
			
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.PagamentoPortale> results = new ArrayList<it.govpay.core.rs.v1.beans.PagamentoPortale>();
			for(it.govpay.bd.model.PagamentoPortale pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				results.add(PagamentiPortaleConverter.toRsModel(pagamentoPortale));
			}
			
			ListaPagamentiPortale response = new ListaPagamentiPortale(results, uriInfo.getRequestUri(),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }

}


