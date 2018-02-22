package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
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

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.beans.ListaRpt;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.rs.v1.costanti.Costanti;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;

@Path("/rpt")
public class Rpt extends BaseRsServiceV1{
	
	
	public Rpt() {
		super("rpt");
	}
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") int pagina,
			@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") int risultatiPerPagina,
			@QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp,
			@QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("idPagamento") String idPagamento,   
			@QueryParam("dataDa") String dataDa,  @QueryParam("dataA") String dataA,  @QueryParam("esito") String esito, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi) {
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
			
			ListaRptDTO listaRptDTO = new ListaRptDTO(null); //TODO IAutorizzato
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setLimit(risultatiPerPagina);
			
			if(dataDa != null || dataA != null) {
				SimpleDateFormat sdf = SimpleDateFormatUtils.newSimpleDateFormat();
				if(dataDa != null) {
					listaRptDTO.setDataDa(sdf.parse(dataDa));
				}
				if(dataA != null) {
					listaRptDTO.setDataA(sdf.parse(dataA));
				}
			}
			if(esito != null)
				listaRptDTO.setStato(StatoRpt.valueOf(esito));
			
			if(idDominio != null)
				listaRptDTO.setIdDominio(idDominio);
			if(iuv != null)
				listaRptDTO.setIuv(iuv);
			if(ccp != null)
				listaRptDTO.setCcp(ccp);
			if(idA2A != null)
				listaRptDTO.setIdA2A(idA2A);
			if(idPendenza != null)
				listaRptDTO.setIdPendenza(idPendenza);
			
			if(idPagamento != null)
				listaRptDTO.setIdPagamento(idPagamento);
			
			if(ordinamento != null)
				listaRptDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			RptDAO rptDAO = new RptDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Rpt> results = new ArrayList<it.govpay.rs.v1.beans.Rpt>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Rpt(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),leggiRptDtoResponse.getCanale(),leggiRptDtoResponse.getPsp()));
			}
			
			ListaRpt response = new ListaRpt(results, uriInfo.getRequestUri(),
					listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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
	@Path("/{idDominio}/{iuv}/{ccp}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_XML,"application/pdf"})
	public Response getRptByIdDominioIuvCcp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp) {
		String methodName = "getRptByIdDominioIuvCcp";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(null); //TODO IAutorizzato
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);
			
			RptDAO ricevuteDAO = new RptDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			
			it.govpay.rs.v1.beans.Rpt response = new it.govpay.rs.v1.beans.Rpt(leggiRptDTOResponse.getRpt(),leggiRptDTOResponse.getVersamento(),leggiRptDTOResponse.getApplicazione(),leggiRptDTOResponse.getCanale(),leggiRptDTOResponse.getPsp());
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (RicevutaNonTrovataException e) {
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
			log.error("Errore interno durante la " + methodName, e);
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
	@Path("/{idDominio}/{iuv}/{ccp}/rt")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_XML,"application/pdf"})
	public Response getRtByIdDominioIuvCcp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp) {
		String methodName = "getRtByIdDominioIuvCcp";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		
		
		String accept = null;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}
		
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(null); //TODO IAutorizzato
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);
			
			RptDAO ricevuteDAO = new RptDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiRicevutaDTOResponse ricevutaDTOResponse = ricevuteDAO.leggiRpt(leggiPagamentoPortaleDTO);
			
			if(accept.equalsIgnoreCase(MediaType.APPLICATION_OCTET_STREAM)) {
				this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
				this.log.info("Esecuzione " + methodName + " completata."); 
				return Response.status(Status.OK).type(accept).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())).build();
			} else {
				String tipoFirma = ricevutaDTOResponse.getRpt().getFirmaRichiesta().getCodifica();
				byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, ricevutaDTOResponse.getRpt().getXmlRt(), ricevutaDTOResponse.getRpt().getCodDominio());
				CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
				
				if(accept.equalsIgnoreCase("application/pdf")) {
					ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
					String auxDigit = ricevutaDTOResponse.getDominio().getAuxDigit() + "";
					String applicationCode = String.format("%02d", ricevutaDTOResponse.getDominio().getStazione().getApplicationCode());
					RicevutaPagamentoUtils.getPdfRicevutaPagamento(ricevutaDTOResponse.getDominio().getLogo(), ricevutaDTOResponse.getVersamento().getCausaleVersamento(), rt, null, auxDigit, applicationCode, baos1, this.log);
					String rtPdfEntryName = "rt.pdf";
				
					byte[] b = baos1.toByteArray();
					
					this.logResponse(uriInfo, httpHeaders, methodName, b, 200);
					this.log.info("Esecuzione " + methodName + " completata."); 
					return Response.status(Status.OK).type(accept).entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\"").build();
				} else {
					return Response.status(Status.OK).type(accept).entity(rt).build();
				}
			}
		}catch (RicevutaNonTrovataException e) {
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
			log.error("Errore interno durante la " + methodName, e);
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
