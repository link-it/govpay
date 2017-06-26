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
package it.govpay.web.rs.dars.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.model.Acl.Servizio;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.handler.IBaseDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("/")
public abstract class BaseDarsService extends BaseRsService {

	public static final String PATH_SEARCH_FIELD = "searchField";
	public static final String PATH_EXPORT_FIELD = "exportField";
	public static final String PATH_ESPORTA = "esporta";
	public static final String SIMPLE_SEARCH_PARAMETER_ID = "simpleSearch";
	public static final String IDS_TO_EXPORT_PARAMETER_ID = "ids";

	protected Logger log = LogManager.getLogger();

	public BaseDarsService() {
		super();
	}

	@POST
	@Path("/searchField/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse searchField(List<RawParamValue> rawValues, 
			@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception,WebApplicationException{
		String methodName = "searchField " + this.getNomeServizio() + "." + id; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			Object field = this.getDarsHandler().getSearchField(uriInfo, rawValues, id, bd);

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
	
	@POST
	@Path("/exportField/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse exportField(List<RawParamValue> rawValues, 
			@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception,WebApplicationException{
		String methodName = "exportField " + this.getNomeServizio() + "." + id; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			Object field = this.getDarsHandler().getExportField(uriInfo, rawValues, id, bd);

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

	@POST
	@Path("/esporta")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM})
	public Response esporta(InputStream is, @Context UriInfo uriInfo) throws Exception{
		String methodName = "esporta " + this.getNomeServizio() ; //+ "[" + sb.toString() + "]";
		
		  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		String idsAsString = null;
		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			
			ByteArrayOutputStream baosIn = new ByteArrayOutputStream();
			Utils.copy(is, baosIn);

			baosIn.flush();
			baosIn.close();

			JSONObject jsonObjectFormExport = JSONObject.fromObject( baosIn.toString() );
			JSONArray jsonIDS = jsonObjectFormExport.getJSONArray(IDS_TO_EXPORT_PARAMETER_ID);

			List<RawParamValue> rawValues = new ArrayList<RawParamValue>();
			for (Object key : jsonObjectFormExport.keySet()) {
				String value = jsonObjectFormExport.getString((String) key);
				if(StringUtils.isNotEmpty(value) && !"null".equals(value))
				rawValues.add(new RawParamValue((String) key, value));
			}
			
			idsAsString = Utils.getValue(rawValues, IDS_TO_EXPORT_PARAMETER_ID);
			this.log.info("Richiesto export degli elementi con id "+idsAsString+""); 

			List<Long> idsToExport = new ArrayList<Long>();
			if(jsonIDS != null && jsonIDS.size() > 0)
				for (int i = 0; i < jsonIDS.size(); i++) {
					long id = jsonIDS.getLong(i);
					idsToExport.add(id); 
				}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zout = new ZipOutputStream(baos);

			String fileName = this.getDarsHandler().esporta(idsToExport, rawValues, uriInfo, bd, zout);
			this.log.info("Richiesta "+methodName +" evasa con successo, creato file: " + fileName);
			return Response.ok(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=\""+fileName+"\"").build();
		} catch(ExportException e){
			this.log.info("Esito operazione "+methodName+" [" + idsAsString + "] : " + e.getEsito() + ", causa: " +e.getMessaggi());
			darsResponse.setEsitoOperazione(e.getEsito());
			darsResponse.setDettaglioEsito(e.getMessaggi());
			return Response.ok(darsResponse,MediaType.APPLICATION_JSON).build();
		}catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Esito operazione "+methodName+" [" + idsAsString + "], causa: " +e.getMessage());
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
	@Path("/{id}/esporta")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response esportaDettaglio(@PathParam("id") long id, InputStream is, @Context UriInfo uriInfo) throws Exception{

		String methodName = "esporta " + this.getNomeServizio() + "[" + id+ "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			
			ByteArrayOutputStream baosIn = new ByteArrayOutputStream();
			Utils.copy(is, baosIn);

			baosIn.flush();
			baosIn.close();

			JSONObject jsonObjectFormExport = JSONObject.fromObject( baosIn.toString() );
			
			List<RawParamValue> rawValues = new ArrayList<RawParamValue>();
			for (Object key : jsonObjectFormExport.keySet()) {
				String value = jsonObjectFormExport.getString((String) key);
				if(StringUtils.isNotEmpty(value) && !"null".equals(value))
				rawValues.add(new RawParamValue((String) key, value));
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zout = new ZipOutputStream(baos);

			String fileName = this.getDarsHandler().esporta(id, rawValues,uriInfo, bd, zout);
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

	public abstract String getNomeServizio();

	public abstract String getPathServizio();
	
	public abstract Servizio getFunzionalita();
	
	public abstract IBaseDarsHandler<?> getDarsHandler();

}
