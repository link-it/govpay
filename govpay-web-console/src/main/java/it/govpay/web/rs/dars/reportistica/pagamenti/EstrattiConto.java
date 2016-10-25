package it.govpay.web.rs.dars.reportistica.pagamenti;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.EstrattoConto;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.utils.Utils;

@Path("/dars/reportisticaEstrattiConto")
public class EstrattiConto extends BaseDarsService {

	public EstrattiConto() {
		super();
	}

	Logger log = LogManager.getLogger();

	@Override
	public String getNomeServizio() {
		return "reportisticaEstrattiConto";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new EstrattiContoHandler(this.log, this);
	}

	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}
	
	@GET
	@Path("/{id}/estrattoConto")
	@Produces({MediaType.TEXT_PLAIN,MediaType.APPLICATION_OCTET_STREAM})
	public Response getContenutoEstrattoConto(
			@PathParam("id") long id,
			@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "Get Estratto Conto " + this.getNomeServizio() + ".id [" + id + "]"; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String fileName = ((EstrattiContoHandler)this.getDarsHandler()).getFile(id,uriInfo,bd,baos);
			String fileNameExt = FilenameUtils.getExtension(fileName);
			String contentType = fileNameExt.equals(EstrattoConto.FORMATO_CSV) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_OCTET_STREAM;

			darsResponse.setResponse(baos.toString());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			
			this.log.info("Richiesta "+methodName +" evasa con successo, creato file: " + fileName);
			Object contenutoResponse  = fileNameExt.equals(EstrattoConto.FORMATO_CSV) ? baos.toString() : baos.toByteArray();
			
			return Response.ok(contenutoResponse,contentType).header("Content-Type", contentType.toString()).header("content-disposition", "attachment; filename=\""+fileName+"\"").build();
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".dettaglio.erroreGenerico"));
			
			return Response.serverError().entity(darsResponse).build();
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
	}

}
