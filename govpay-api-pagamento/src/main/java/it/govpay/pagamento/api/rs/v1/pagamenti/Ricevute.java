package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.core.dao.pagamenti.RicevuteDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.pagamento.api.rs.v1.model.FaultBean;
import it.govpay.pagamento.api.rs.v1.model.FaultBean.CATEGORIA;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;

@Path("/ricevute")
public class Ricevute extends BaseRsServiceV1{
	
	
	public Ricevute() {
		super("ricevute");
	}

	@GET
	@Path("/{idDominio}/{iuv}/{ccp}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_XML,"application/pdf"})
	public Response getByIdDominioIuvCcp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("ccp") String ccp) {
		String methodName = "getByIdDominioIuvCcp";  
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
			leggiPagamentoPortaleDTO.setCcp(ccp);
			
			
			RicevuteDAO ricevuteDAO = new RicevuteDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
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
			log.error("Errore interno durante la " + methodName, e);
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
