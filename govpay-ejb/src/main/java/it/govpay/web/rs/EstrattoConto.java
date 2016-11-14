package it.govpay.web.rs;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.logger.beans.Property;

import it.govpay.bd.BasicBD;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.reportistica.EstrattoContoMetadata;
import it.govpay.web.rs.model.EstrattoContoRequest;
import it.govpay.web.rs.utils.PagamentoUtils;
import it.govpay.web.rs.utils.RestUtils;
import it.govpay.web.rs.utils.ValidationUtils;

@Path("/estrattoConto")
public class EstrattoConto extends BaseRsService{
	
	public static final String NOME_SERVIZIO = "PagamentiTelematiciAPPjson";
	public static final int LIMIT = 50;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	

	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response estrattoConto(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		String methodName = "EstrattoConto"; 
		BasicBD bd = null;
		GpContext ctx = null; 
//		List<Pagamento> lst = new ArrayList<Pagamento>();
		try{
			
			EstrattoContoRequest request = PagamentoUtils.readEstrattoContoRequestFromRequest(this, log, is, uriInfo, httpHeaders, methodName);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx = GpThreadLocal.get();
			
			log.info("Validazione della entry in corso...");
			// validazione richiesta
			ValidationUtils.validaRichiestaEstrattoConto(request);
			log.info("Validazione della entry completata.");

			// la data fine puo' essere null, default e' ieri
			if(request.getDataFine() == null){
				Calendar c= Calendar.getInstance();
				Date d = new Date();
				c.setTime(d);
				c.set(Calendar.MILLISECOND, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.add(Calendar.MILLISECOND, -1);
				c.add(Calendar.DATE, -1); 

				request.setDataFine(c.getTime());
			}

			String dataInizio = sdf.format(request.getDataInizio());
			String dataFine = sdf.format(request.getDataFine());

			ctx.getContext().getRequest().addGenericProperty(new Property("codiceCreditore", request.getCodiceCreditore()));
			ctx.getContext().getRequest().addGenericProperty(new Property("dataInizio", dataInizio));
			ctx.getContext().getRequest().addGenericProperty(new Property("dataFine", dataFine));
			ctx.log("rest.richiestaEstrattoConto");

			Integer offset = ( request.getPagina() != null ? (request.getPagina() - 1 ): 0 ) * LIMIT;

			List<it.govpay.model.EstrattoConto> findAll = new EstrattiContoBD(bd).estrattoContoFromCodDominioIntervalloDate(request.getCodiceCreditore(), request.getDataInizio(), request.getDataFine(), offset, LIMIT); 

			ByteArrayOutputStream baos = PagamentoUtils.writeEstrattoContoResponse(this, log, findAll, uriInfo, httpHeaders, bd, methodName);
			ctx.getContext().getRequest().addGenericProperty(new Property("numeroPagamenti", findAll.size()+""));
			ctx.log("rest.estrattoContoOk");
			return Response.ok(baos.toString()).build();
		}catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.estrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ctx.log("rest.estrattoContoKo",e.getMessage());
			return e.getResponse();
		}catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.estrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).build(); 
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response listaEstrattoConto(
			@PathParam("codDominio") String codDominio,
			@QueryParam("formato") String formatoFile,
			@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		
		String methodName = "ListaEstrattoConto"; 
		BasicBD bd = null;
		GpContext ctx = null;

		// formato dei file estratto conto
		if(StringUtils.isEmpty(formatoFile))
			formatoFile = EstrattoContoMetadata.FORMATO_STAR;
		try{
			PagamentoUtils.readGetRequest(this, log, uriInfo, httpHeaders, methodName);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx = GpThreadLocal.get();
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
			ctx.getContext().getRequest().addGenericProperty(new Property("formatoFile",  formatoFile));
			ctx.log("rest.listaEstrattoConto");
			
			List<EstrattoContoMetadata> lst = new it.govpay.core.business.EstrattoConto(bd).getListaEstrattoConto(codDominio, formatoFile);
			
			ByteArrayOutputStream baos = PagamentoUtils.writeListaEstrattoContoResponse(this, log, lst, uriInfo, httpHeaders, bd, methodName);
			ctx.getContext().getRequest().addGenericProperty(new Property("numeroFile", lst.size()+""));
			ctx.log("rest.listaEstrattoContoOk");
			return Response.ok(baos.toString()).build();
		}catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.listaEstrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ctx.log("rest.listaEstrattoContoKo",e.getMessage());
			return e.getResponse();
		}catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.listaEstrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).build(); 
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
		}
	}
	
	
	@GET
	@Path("/{codDominio}/{nomeFile}")
	@Produces({MediaType.TEXT_PLAIN,MediaType.APPLICATION_OCTET_STREAM})
	public Response scaricaEstrattoConto(
			@PathParam("codDominio") String codDominio,
			@PathParam("nomeFile") String nomeFile,
			@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		
		
		String methodName = "ScaricaEstrattoConto"; 
		BasicBD bd = null;
		GpContext ctx = null;
		
		String applicationJson = MediaType.APPLICATION_JSON;
		try{
			PagamentoUtils.readGetRequest(this, log, uriInfo, httpHeaders, methodName);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx = GpThreadLocal.get();
			
			String fileNameExt = FilenameUtils.getExtension(nomeFile);
			String formato = fileNameExt.equals( EstrattoContoMetadata.FORMATO_CSV) ?  EstrattoContoMetadata.FORMATO_CSV :  EstrattoContoMetadata.FORMATO_PDF;
			String contentType = fileNameExt.equals( EstrattoContoMetadata.FORMATO_CSV) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_OCTET_STREAM;
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
			ctx.getContext().getRequest().addGenericProperty(new Property("nomeFile", nomeFile));
			ctx.getContext().getRequest().addGenericProperty(new Property("formatoFile", formato));
			ctx.log("rest.scaricaEstrattoConto");
			
			InputStream res = new  it.govpay.core.business.EstrattoConto(bd).scaricaEstrattoConto(codDominio,nomeFile, formato);
			
			ByteArrayOutputStream baos = PagamentoUtils.writeScaricaEstrattoContoResponse(this, log, res, uriInfo, httpHeaders, bd, methodName,formato);
			ctx.log("rest.scaricaEstrattoContoOk");
			
			Object contenutoResponse  = fileNameExt.equals( EstrattoContoMetadata.FORMATO_CSV) ? baos.toString() : baos.toByteArray();
			
			return Response.ok(contenutoResponse,contentType).header("Content-Type", contentType.toString()).build();
		}catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.scaricaEstrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).header("Content-Type", applicationJson.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ctx.log("rest.scaricaEstrattoContoKo",e.getMessage());
			return e.getResponse();
		}catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.scaricaEstrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).header("Content-Type", applicationJson.toString()).build(); 
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
		}	
	}

}
