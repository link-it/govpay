package it.govpay.web.rs;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.core.business.EstrattoConto;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.web.rs.caricatore.ICaricatore;
import it.govpay.web.rs.model.EstrattoContoRequest;
import it.govpay.web.rs.utils.PagamentoUtils;
import it.govpay.web.rs.utils.RestUtils;
import it.govpay.web.rs.utils.ValidationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import org.openspcoop2.utils.logger.beans.Property;

@Path("/caricatore")
public class Caricatore extends BaseRsService{

	public static final String NOME_SERVIZIO = "PagamentiTelematiciAPPjson";
	public static final int LIMIT = 50;
	
	private static final String FORMATO_CSV = "CSV";

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 

	public Caricatore() {
		super(NOME_SERVIZIO);
	}

	@POST
	@Path("/caricaVersamento")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response caricaVersamento(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("caricatoreImplClass") String caricatoreImplClass){
		String methodName = "CaricaVersamento"; 
		
		Applicazione applicazioneAutenticata = null; 

		BasicBD bd = null;
		GpContext ctx = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		ByteArrayOutputStream baosResponse = null;
		try{
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			applicazioneAutenticata = getApplicazioneAutenticata(bd);

			baos = new ByteArrayOutputStream();
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName,baos);

			if(caricatoreImplClass == null) {
				caricatoreImplClass = "it.govpay.web.rs.caricatore.CaricatoreImpl";
			}
			
			Class<?> caricatoreImpl = Class.forName(caricatoreImplClass);
			
			Object caricatoreImplObjectInstance = caricatoreImpl.newInstance();
			
			if(!(caricatoreImplObjectInstance instanceof ICaricatore)) {
				throw new Exception("La classe ["+caricatoreImplClass+"] deve implementare l'interfaccia " + ICaricatore.class);
			}
			
			ICaricatore caricatore = (ICaricatore) caricatoreImplObjectInstance; 
			
			bais = new ByteArrayInputStream(baos.toByteArray());
			String response = caricatore.caricaVersamento(bais, uriInfo, httpHeaders, bd, applicazioneAutenticata);


			baosResponse = new ByteArrayOutputStream();

			baosResponse.write(response.getBytes());
			this.logResponse(uriInfo, httpHeaders, methodName, baosResponse);

			return Response.ok(response).build();
		} catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baosGovPayExc = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",e.getMessage());
			return Response.serverError().entity(baosGovPayExc.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return e.getResponse();
		} catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baosExc = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return Response.serverError().entity(baosExc.toString()).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
			if(bais != null) try { bais.close();} catch (IOException e) {}
			if(baos != null) try { baos.flush();} catch (IOException e) {}
			if(baos != null) try { baos.close();} catch (IOException e) {}
			if(baosResponse != null) try { baosResponse.flush();} catch (IOException e) {}
			if(baosResponse != null) try { baosResponse.close();} catch (IOException e) {}
		}
	}

	@POST
	@Path("/estrattoConto")
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

			List<it.govpay.bd.model.EstrattoConto> findAll = new EstrattoConto(bd).getEstrattoContoExt(request.getCodiceCreditore(), request.getDataInizio(), request.getDataFine(), offset, LIMIT); 

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
	@Path("/estrattoConto/{codDominio}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response listaEstrattoConto(
			@PathParam("codDominio") String codDominio,
			@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		
		String methodName = "ListaEstrattoConto"; 
		BasicBD bd = null;
		GpContext ctx = null;
		
		try{
			PagamentoUtils.readGetRequest(this, log, uriInfo, httpHeaders, methodName);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx = GpThreadLocal.get();
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
			ctx.getContext().getRequest().addGenericProperty(new Property("formatoFile", FORMATO_CSV));
			ctx.log("rest.listaEstrattoConto");
			
			List<String> lst = new EstrattoConto(bd).getListaEstrattoConto(codDominio,FORMATO_CSV);
			
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
	@Path("/estrattoConto/{codDominio}/{nomeFile}")
	@Produces({MediaType.TEXT_PLAIN})
	public Response scaricaEstrattoConto(
			@PathParam("codDominio") String codDominio,
			@PathParam("nomeFile") String nomeFile,
			@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		
		
		String methodName = "ScaricaEstrattoConto"; 
		BasicBD bd = null;
		GpContext ctx = null;
		
		try{
			PagamentoUtils.readGetRequest(this, log, uriInfo, httpHeaders, methodName);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx = GpThreadLocal.get();
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
			ctx.getContext().getRequest().addGenericProperty(new Property("nomeFile", nomeFile));
			ctx.getContext().getRequest().addGenericProperty(new Property("formatoFile", FORMATO_CSV));
			ctx.log("rest.scaricaEstrattoConto");
			
			InputStream res = new EstrattoConto(bd).scaricaEstrattoConto(codDominio,nomeFile, FORMATO_CSV);
			
			ByteArrayOutputStream baos = PagamentoUtils.writeScaricaEstrattoContoResponse(this, log, res, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.scaricaEstrattoContoOk");
			return Response.ok(baos.toString()).build();
		}catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			ctx.log("rest.scaricaEstrattoContoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).header("Content-Type", MediaType.APPLICATION_JSON.toString()).build();
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
			return Response.serverError().entity(baos.toString()).header("Content-Type", MediaType.APPLICATION_JSON.toString()).build(); 
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
		}	
	}
}
