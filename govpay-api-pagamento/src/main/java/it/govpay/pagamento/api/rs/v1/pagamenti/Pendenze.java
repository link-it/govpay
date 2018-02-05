package it.govpay.pagamento.api.rs.v1.pagamenti;

import java.io.ByteArrayOutputStream;

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
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.pagamento.api.rs.v1.model.FaultBean;
import it.govpay.pagamento.api.rs.v1.model.FaultBean.CATEGORIA;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.beans.Pendenza;

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
			@PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, @QueryParam("fields") String fields) {
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

			Pendenza pendenza = new Pendenza(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getUnitaOperativa(), ricevutaDTOResponse.getApplicazione(), ricevutaDTOResponse.getDominio());
			return Response.status(Status.OK).entity(pendenza.toJSON(fields)).build();
		}catch (PendenzaNonTrovataException e) {
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
