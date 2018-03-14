package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.core.business.model.LeggiIncassoDTO;
import it.govpay.core.business.model.LeggiIncassoDTOResponse;
import it.govpay.core.business.model.ListaIncassiDTO;
import it.govpay.core.business.model.ListaIncassiDTOResponse;
import it.govpay.core.business.model.RichiestaIncassoDTO;
import it.govpay.core.business.model.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.Errore;
import it.govpay.core.rs.v1.beans.Incasso;
import it.govpay.core.rs.v1.beans.IncassoPost;
import it.govpay.core.rs.v1.beans.ListaIncassi;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.IncassiConverter;
import net.sf.json.JsonConfig;



public class IncassiController extends it.govpay.rs.BaseController {
	
	public IncassiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


    public Response incassiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
    	String methodName = "cercaIncassi"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{

			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			ListaIncassiDTO listaIncassoDTO = new ListaIncassiDTO(null);
			listaIncassoDTO.setPagina(pagina);
			listaIncassoDTO.setLimit(risultatiPerPagina);
			
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			ListaIncassiDTOResponse listaIncassiDTOResponse = incassi.listaIncassi(listaIncassoDTO);
			
			List<Incasso> listaIncassi = new ArrayList<Incasso>();
			for(it.govpay.bd.model.Incasso i : listaIncassiDTOResponse.getResults()) {
				listaIncassi.add(IncassiConverter.toRsModel(i));
			}
			
			return Response.status(Status.OK).entity(new ListaIncassi(listaIncassi, uriInfo.getBaseUri(), listaIncassiDTOResponse.getTotalResults(), pagina, risultatiPerPagina).toJSON(null)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
    }


    public Response incassiIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idIncasso) {
    	String methodName = "leggiIncasso"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			LeggiIncassoDTO leggiIncassoDTO = new LeggiIncassoDTO();
			leggiIncassoDTO.setIdDominio(idDominio);
			leggiIncassoDTO.setIdIncasso(idIncasso);
//			leggiIncassoDTO.setPrincipal(principal); //TODO pintori
			
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			LeggiIncassoDTOResponse leggiIncassoDTOResponse = incassi.leggiIncasso(leggiIncassoDTO);
			
			return Response.status(Status.OK).entity(IncassiConverter.toRsModel(leggiIncassoDTOResponse.getIncasso()).toJSON(null)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
    }


    public Response incassiIdDominioIdIncassoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idIncasso) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


    public Response incassiPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , InputStream is) {
    	String methodName = "inserisciIncasso"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		ByteArrayOutputStream baos = null, baosResponse = null;
		ByteArrayInputStream bais = null;
		
		try{
			baos = new ByteArrayOutputStream();
			BaseRsService.copy(is, baos);

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			it.govpay.core.rs.v1.beans.IncassoPost incasso = (IncassoPost) it.govpay.core.rs.v1.beans.IncassoPost.parse(baos.toString(), it.govpay.core.rs.v1.beans.IncassoPost.class, new JsonConfig());
			RichiestaIncassoDTO richiestaIncassoDTO = IncassiConverter.toRichiestaIncassoDTO(incasso);
			
			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, null); //TODO pintori
			richiestaIncassoDTO.setApplicazione(applicazione);
			
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			
			Incasso incassoExt = IncassiConverter.toRsModel(richiestaIncassoDTOResponse.getIncasso());
			
			this.logResponse(uriInfo, httpHeaders, methodName, incassoExt.toJSON(null));

			Status status = richiestaIncassoDTOResponse.isCreato() ? Status.CREATED : Status.OK;
			return Response.status(status).entity(incassoExt.toJSON(null)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (IncassiException e) {
			Errore errore = new Errore(e.getCode(),e.getMessage(),e.getDetails());
			try { this.logResponse(uriInfo, httpHeaders, methodName, errore); } catch (Exception e2) { log.error(e2.getMessage());}
			return Response.status(422).entity(errore).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
			if(bais != null) try { bais.close();} catch (IOException e) {}
			if(baos != null) try { baos.flush(); baos.close();} catch (IOException e) {}
			if(baosResponse != null) try { baosResponse.flush(); baosResponse.close();} catch (IOException e) {}
		}
    }


}


