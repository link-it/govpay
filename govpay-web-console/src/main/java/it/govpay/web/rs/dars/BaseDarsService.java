/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.web.rs.dars;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;

@Path("/")
public abstract class BaseDarsService extends BaseRsService {

	public static final String PATH_FIELD = "field";
	public static final String PATH_ESPORTA = "esporta";
	public static final String PATH_CANCELLA = "cancella";
	public static final String PATH_UPLOAD = "upload";
	
	public static final String SIMPLE_SEARCH_PARAMETER_ID = "simpleSearch";

	protected Logger log = LogManager.getLogger();

	public BaseDarsService() {
		super();
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find( 	@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "find " + this.getNomeServizio(); 
		this.initLogger(methodName);

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance(this.codOperazione);
			Elenco elenco = this.getDarsHandler().getElenco(uriInfo,bd);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(elenco);
		}catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".elenco.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/field/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse field(List<RawParamValue> rawValues, 
			@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception,WebApplicationException{
		String methodName = "field " + this.getNomeServizio() + "." + id; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			Object field = this.getDarsHandler().getField(uriInfo, rawValues, id, bd);

			// Field richiesto non valido
			if(field == null){
				darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
				darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle("field.fieldNonPresente", id,this.getNomeServizio()));
				return darsResponse;
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(field);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle("field.erroreGenerico", id,this.getNomeServizio()));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse get(
			@PathParam("id") long id,
			@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "Dettaglio " + this.getNomeServizio() + ".id [" + id + "]"; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);

			Dettaglio dettaglio = this.getDarsHandler().getDettaglio(id,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".dettaglio.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/cancella")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse cancella(List<Long> idsToDelete, @Context UriInfo uriInfo) throws Exception{
		StringBuffer sb = new StringBuffer();

		if(idsToDelete != null && idsToDelete.size() > 0)
			for (Long long1 : idsToDelete) {
				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "cancella " + this.getNomeServizio() + "[" + sb.toString() + "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);

			this.getDarsHandler().delete(idsToDelete, uriInfo, bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".cancella.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/esporta")
	@Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response esporta(List<Long> idsToExport, @Context UriInfo uriInfo) throws Exception{
		StringBuffer sb = new StringBuffer();

		if(idsToExport != null && idsToExport.size() > 0)
			for (Long long1 : idsToExport) {

				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "esporta " + this.getNomeServizio() + "[" + sb.toString() + "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			if(idsToExport != null && idsToExport.size() > 0){
				bd = BasicBD.newInstance(this.codOperazione);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zout = new ZipOutputStream(baos);

				String fileName = this.getDarsHandler().esporta(idsToExport, uriInfo, bd, zout);
				this.log.info("Richiesta "+methodName +" evasa con successo, creato file: " + fileName);
				return Response.ok(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=\""+fileName+"\"").build();
			}else{
				this.log.error("Riscontrato errore di input durante l'esecuzione del metodo "+methodName+": La selezione degli elementi da esportare e' vuota.");
				darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
				darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".esporta.erroreSelezioneVuota"));
				return Response.ok(darsResponse,MediaType.APPLICATION_JSON).build();
			}
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();
			return Response.serverError().build();
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			this.response.setHeader("Access-Control-Expose-Headers", "content-disposition");
			if(bd != null) bd.closeConnection();
		}

	}

	@GET
	@Path("/{id}/esporta")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response esportaDettaglio(@PathParam("id") long id, @Context UriInfo uriInfo) throws Exception{

		String methodName = "esporta " + this.getNomeServizio() + "[" + id+ "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zout = new ZipOutputStream(baos);

			String fileName = this.getDarsHandler().esporta(id, uriInfo, bd, zout);
			this.log.info("Richiesta "+methodName +" evasa con successo, creato file: " + fileName);
			return Response.ok(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=\""+fileName+"\"").build();
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();
			return Response.serverError().build();
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			this.response.setHeader("Access-Control-Expose-Headers", "content-disposition");
			if(bd != null) bd.closeConnection();
		}

	}

	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse insert(InputStream is, @Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "insert " + this.getNomeServizio(); 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);

			Dettaglio dettaglio = this.getDarsHandler().insert(is,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".creazione.ok")); 
		} catch(ValidationException e){
			this.log.error("Riscontrato errore di validazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".creazione.erroreValidazione")+ " " + e.getMessage());
			return darsResponse;
		} catch(DuplicatedEntryException e){
			this.log.error("Riscontrata errore di entry duplicata durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".creazione.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@PUT
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse update(InputStream is, @Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "update " + this.getNomeServizio(); 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		try {
			bd = BasicBD.newInstance(this.codOperazione);

			Dettaglio dettaglio = this.getDarsHandler().update(is,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.ok")); 
		} catch(ValidationException e){
			this.log.error("Riscontrato errore di validazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.erroreValidazione")+ " " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/upload")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse upload(MultipartFormDataInput input, @Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "upload " + this.getNomeServizio(); 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			
			Object res = this.getDarsHandler().uplaod(input, uriInfo, bd);
			
			darsResponse.setResponse(res); 
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".upload.ok")); 
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".upload.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	public abstract String getNomeServizio();

	public abstract String getPathServizio();

	public abstract IDarsHandler<?> getDarsHandler();

}
