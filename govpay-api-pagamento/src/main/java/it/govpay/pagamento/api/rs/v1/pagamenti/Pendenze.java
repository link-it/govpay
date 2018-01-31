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

@Path("/pendenze")
public class Pendenze extends BaseRsServiceV1{
	
	
	public Pendenze() {
		super("pendenze");
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response get(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
		return Response.status(Status.OK).build();
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
			
			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(null); //TODO IAutorizzato
//			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
//			leggiPagamentoPortaleDTO.setIuv(iuv);
//			leggiPagamentoPortaleDTO.setCcp(ccp);
			
			
			RicevuteDAO ricevuteDAO = new RicevuteDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiRicevutaDTOResponse ricevutaDTOResponse = ricevuteDAO.leggiRpt(leggiPagamentoPortaleDTO);
			
			String tipoFirma = ricevutaDTOResponse.getRpt().getFirmaRichiesta().getCodifica();
			byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, ricevutaDTOResponse.getRpt().getXmlRt(), ricevutaDTOResponse.getRpt().getCodDominio());
			CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
			
			return Response.status(Status.OK).entity(rt).build();
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
