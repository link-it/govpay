package it.govpay.web.rs;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Iuv.TipoIUV;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.business.EstrattoConto;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.web.rs.converter.VersamentoConverter;
import it.govpay.web.rs.model.EstrattoContoRequest;
import it.govpay.web.rs.model.Versamento;
import it.govpay.web.rs.model.VersamentoResponse;
import it.govpay.web.rs.utils.PagamentoUtils;
import it.govpay.web.rs.utils.RestUtils;
import it.govpay.web.rs.utils.ValidationUtils;
import it.govpay.web.rs.utils.VersamentoUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.NotFoundException;
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
	public Response caricaVersamento(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders ){
		String methodName = "CaricaVersamento"; 
		
		BasicBD bd = null;
		Applicazione applicazioneAutenticata = null;
		it.govpay.core.business.Versamento versamentoBusiness = null;
		it.govpay.servizi.commons.Versamento versamentoCommons = null;
		it.govpay.bd.model.Versamento versamentoModel = null;
		GpContext ctx = null; 
		VersamentoResponse versamentoResponse = new VersamentoResponse();
		try{
			Versamento request = VersamentoUtils.readVersamentoFromRequest(this, log, is, uriInfo, httpHeaders, methodName);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();

			log.info("Validazione della entry in corso...");
			// validazione richiesta
			ValidationUtils.validaRichiestaCaricaVersamento(request);
			log.info("Validazione della entry completata.");
			
			applicazioneAutenticata = getApplicazioneAutenticata(bd); 
			versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			
			boolean generaIuv = true;
			boolean aggiornaSeEsiste = true;

			this.log.info("Caricamento del Versamento ["+request.getIdentificativoVersamento()+"] in corso...");
			
			it.govpay.bd.model.Iuv iuv = null;
			try{
				ctx.getContext().getRequest().addGenericProperty(new Property("identificativoVersamento", request.getIdentificativoVersamento())); 
				ctx.getContext().getRequest().addGenericProperty(new Property("codiceCreditore", request.getCodiceCreditore()));
				ctx.log("rest.richiestaVersamento");
				versamentoResponse.setIdOperazione(ctx.getTransactionId());
				// porto il versamento alla versione definita nei servizi.
				versamentoCommons = VersamentoConverter.toVersamentoCommons(request,applicazioneAutenticata, false, bd);
				// porto il versamento alla versione model, utilizzando tutti i controlli previsti nella versione commons.
				versamentoModel = it.govpay.core.utils.VersamentoUtils.toVersamentoModel(versamentoCommons, bd);
				iuv = versamentoBusiness.caricaVersamento(applicazioneAutenticata, versamentoModel, generaIuv , aggiornaSeEsiste );

				if(iuv != null) {
					Double importoVersamento = request.getImporto();
					BigDecimal importoVersamentoAsBigDecimal  = new BigDecimal(importoVersamento.doubleValue());
					IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(versamentoModel.getApplicazione(bd), versamentoModel.getUo(bd).getDominio(bd), iuv, importoVersamentoAsBigDecimal);
					versamentoResponse.setBarCode(new String(iuvGenerato.getBarCode()));
					versamentoResponse.setQrCode(new String(iuvGenerato.getQrCode()));
					versamentoResponse.setIuv(iuvGenerato.getIuv());
				}

				versamentoResponse.setIdOperazione(ctx.getTransactionId());
				versamentoResponse.setEsito("OK");

				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", versamentoResponse.getIuv()));
				ctx.log("rest.versamentoOk");
			} catch (GovPayException e) {
				e.log(log);
				versamentoResponse.setEsito(e.getCodEsito().name() + ": " + e.getMessage());
				ctx.log("rest.versamentoKo",e.getMessage());
			} catch (Exception e) {
				GovPayException ge = new GovPayException(e);
				ge.log(log);
				versamentoResponse.setEsito(ge.getCodEsito().name() + ": " + ge.getMessage());
				ctx.log("rest.versamentoKo",ge.getMessage());
			}
			// Se ho rilevato un eccezione provo a recuperare comunque lo iuv
			if(iuv == null && versamentoModel != null) {
				IuvBD iuvBD = new IuvBD(bd);
				try {
					iuv = iuvBD.getIuv(versamentoModel.getIdApplicazione(), versamentoModel.getCodVersamentoEnte(), TipoIUV.NUMERICO);
					versamentoResponse.setIuv(iuv.getIuv()); 
				} catch (NotFoundException e) {
					versamentoResponse.setIuv(null);
				}
			}

			this.log.info("Caricamento del Versamento ["+request.getIdentificativoVersamento()+"] completato con esito ["+versamentoResponse.getEsito()+"].");
			ByteArrayOutputStream baos = VersamentoUtils.writeVersamentoResponse(this, log, versamentoResponse, uriInfo, httpHeaders, bd, methodName);
			return Response.ok(baos.toString()).build();
		} catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",e.getMessage());
			return Response.serverError().entity(baos.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return e.getResponse();
		} catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baos = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return Response.serverError().entity(baos.toString()).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
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
