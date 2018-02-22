package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.beans.ListaPendenze;
import it.govpay.rs.v1.beans.Pendenza;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.rs.v1.costanti.Costanti;

@Path("/pendenze")
public class Pendenze extends BaseRsServiceV1{
	
	
	public Pendenze() {
		super("pendenze");
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("idDominio") String idDominio, @QueryParam("idDebitore") String idDebitore,
			@QueryParam("idA2A") String idA2A, @QueryParam("idPagamento") String idPagamento,   
			@QueryParam("dataDa") String dataDa,  @QueryParam("dataA") String dataA,  @QueryParam("stato") String stato, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
		String methodName = "getListaRpt";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			// Parametri - > DTO Input
			
			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(null); //TODO IAutorizzato
			
			listaPendenzeDTO.setPagina(pagina);
			listaPendenzeDTO.setLimit(risultatiPerPagina);
			
			if(dataDa != null || dataA != null) {
				SimpleDateFormat sdf = SimpleDateFormatUtils.newSimpleDateFormat();
				if(dataDa != null) {
					listaPendenzeDTO.setDataDa(sdf.parse(dataDa));
				}
				if(dataA != null) {
					listaPendenzeDTO.setDataA(sdf.parse(dataA));
				}
			}
			if(stato != null)
				listaPendenzeDTO.setStato(StatoVersamento.valueOf(stato));
			
			if(idDominio != null)
				listaPendenzeDTO.setIdDominio(idDominio);
			if(idA2A != null)
				listaPendenzeDTO.setIdA2A(idA2A);
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);
			
			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);
			
			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			// CHIAMATA AL DAO
			
			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Pendenza> results = new ArrayList<it.govpay.rs.v1.beans.Pendenza>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Pendenza(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getUnitaOperativa(), ricevutaDTOResponse.getApplicazione(), ricevutaDTOResponse.getDominio(), ricevutaDTOResponse.getLstSingoliVersamenti()));
			}
			
			ListaPendenze response = new ListaPendenze(results, uriInfo.getRequestUri(),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle RPT: " + e.getMessage(), e);
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
	@Path("/{idA2A}/{idPendenza}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getByIda2aIdPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza) {
		String methodName = "getByIda2aIdPendenza";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		
		
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(null); //TODO IAutorizzato
			
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Pendenza pendenza = new Pendenza(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getUnitaOperativa(), ricevutaDTOResponse.getApplicazione(), ricevutaDTOResponse.getDominio(), ricevutaDTOResponse.getLstSingoliVersamenti());
			return Response.status(Status.OK).entity(pendenza.toJSON(null)).build();
		}catch (PendenzaNonTrovataException e) {
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
