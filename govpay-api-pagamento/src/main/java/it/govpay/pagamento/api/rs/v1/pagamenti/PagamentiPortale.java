package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.pagamento.api.rs.v1.converter.PagamentiPortaleConverter;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.ListaPagamentiPortale;
import it.govpay.rs.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.rs.v1.beans.PagamentoPost;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.rs.v1.costanti.Costanti;
import net.sf.json.JsonConfig;

@Path("/pagamenti")
public class PagamentiPortale extends BaseRsServiceV1{
	
	
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
			
			String jsonRequest = baos.toString();
			JsonConfig jsonConfig = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("autenticazioneSoggetto", String.class);
			jsonConfig.setClassMap(classMap);
			PagamentoPost pagamentiPortaleRequest= (PagamentoPost) PagamentoPost.parse(jsonRequest, PagamentoPost.class, jsonConfig);
			
			String transactionId = ctx.getTransactionId();
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, jsonRequest, principal,idSession, idSessionePortale);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(transactionId)); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentiPortaleResponseOk responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, responseOk.toJSON(null), 200);
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
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("dataDa") String dataDa,  @QueryParam("dataA") String dataA,  @QueryParam("stato") String stato,@QueryParam("versante") String versante,@QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
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
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(null); //TODO IAutorizzato
			listaPagamentiPortaleDTO.setPagina(pagina);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			
			if(dataDa != null || dataA != null) {
				SimpleDateFormat sdf = SimpleDateFormatUtils.newSimpleDateFormat();
				if(dataDa != null) {
					listaPagamentiPortaleDTO.setDataDa(sdf.parse(dataDa));
				}
				if(dataA != null) {
					listaPagamentiPortaleDTO.setDataA(sdf.parse(dataA));
				}
			}
			
			if(stato != null)
				listaPagamentiPortaleDTO.setStato(STATO.valueOf(stato));
			
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.PagamentoPortale> results = new ArrayList<it.govpay.rs.v1.beans.PagamentoPortale>();
			for(it.govpay.bd.model.PagamentoPortale pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.PagamentoPortale(pagamentoPortale));
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

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPagamentoPortaleById(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id, @QueryParam("campi") String campi) {
		String methodName = "getPagamentoPortaleById";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(null); //TODO IAutorizzato
			leggiPagamentoPortaleDTO.setIdSessione(id);
			leggiPagamentoPortaleDTO.setPrincipal(principal);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.rs.v1.beans.PagamentoPortale response = new it.govpay.rs.v1.beans.PagamentoPortale(pagamentoPortaleModel);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
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
}
