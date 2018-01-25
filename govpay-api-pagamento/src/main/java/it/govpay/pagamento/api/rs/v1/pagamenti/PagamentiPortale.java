package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.pagamento.api.rs.BaseRsService;
import it.govpay.pagamento.api.rs.v1.converter.PagamentiPortaleConverter;
import it.govpay.pagamento.api.rs.v1.model.FaultBean;
import it.govpay.pagamento.api.rs.v1.model.FaultBean.CATEGORIA;
import it.govpay.pagamento.api.rs.v1.model.PagamentiPortaleRequest;
import it.govpay.pagamento.api.rs.v1.model.PagamentiPortaleResponseOk;
import it.govpay.pagamento.api.rs.v1.model.PagamentoPortale;

@Path("/pagamenti")
public class PagamentiPortale extends BaseRsService{
	
	
	public PagamentiPortale() {
		super("pagamentiPortale");
	}

	@POST
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response inserisciPagamenti(InputStream is , @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSessionePortale") String idSessionePortale) {
		String methodName = "pagamenti";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			PagamentiPortaleRequest pagamentiPortaleRequest = PagamentiPortaleConverter.readFromJson(baos);
			
			String transactionId = ctx.getTransactionId();
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, baos.toString(), principal,idSession, idSessionePortale);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(transactionId)); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentiPortaleResponseOk responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, responseOk, 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(responseOk).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CATEGORIA.INTERNO);
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
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam("from") String from,  @QueryParam("size") String size,  
			@QueryParam("dataDa") String dataDa,  @QueryParam("dataA") String dataA,  @QueryParam("stato") String stato,@QueryParam("versante") String versante	) {
		String methodName = "getListaPagamenti";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			// Parametri - > DTO Input
			
//			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO();
//			leggiPagamentoPortaleDTO.setIdSessione(id);
//			leggiPagamentoPortaleDTO.setPrincipal(principal);
			
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = new LeggiPagamentoPortaleDTOResponse(); //pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.bd.model.Psp psp = pagamentoPortaleDTOResponse.getPsp();
			it.govpay.bd.model.Canale canale = pagamentoPortaleDTOResponse.getCanale();
			
			PagamentoPortale response = PagamentiPortaleConverter.toJsonPagamentoPortale(pagamentoPortaleModel, psp, canale);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response, 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response).build();
			
//		}catch (PagamentoPortaleNonTrovatoException e) {
//			log.error(e.getMessage(), e);
//			FaultBean respKo = new FaultBean();
//			respKo.setCategoria(CATEGORIA.OPERAZIONE);
//			respKo.setCodice("");
//			respKo.setDescrizione(e.getMessage());
//			try {
//				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
//			}catch(Exception e1) {
//				log.error("Errore durante il log della risposta", e1);
//			}
//			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		}catch (Exception e) {
			log.error("Errore interno durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CATEGORIA.INTERNO);
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
	
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPagamentoPortaleById(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id) {
		String methodName = "getPagamentoPortaleById";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO();
			leggiPagamentoPortaleDTO.setIdSessione(id);
			leggiPagamentoPortaleDTO.setPrincipal(principal);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.bd.model.Psp psp = pagamentoPortaleDTOResponse.getPsp();
			it.govpay.bd.model.Canale canale = pagamentoPortaleDTOResponse.getCanale();
			
			PagamentoPortale response = PagamentiPortaleConverter.toJsonPagamentoPortale(pagamentoPortaleModel, psp, canale);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response, 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response).build();
		}catch (PagamentoPortaleNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CATEGORIA.OPERAZIONE);
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
			respKo.setCategoria(CATEGORIA.INTERNO);
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
