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
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("/")
public abstract class DarsService extends BaseDarsService {

	public static final String PATH_FIELD = "field";
	public static final String PATH_CANCELLA = "cancella";
	public static final String PATH_DELETE_FIELD = "deleteField";
	public static final String PATH_UPLOAD = "upload";
	public static final String IDS_TO_DELETE_PARAMETER_ID = "ids";

	public DarsService() {
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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			
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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
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
	
	@POST
	@Path("/deleteField/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse deleteField(List<RawParamValue> rawValues, 
			@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception,WebApplicationException{
		String methodName = "deleteField " + this.getNomeServizio() + "." + id; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			Object field = this.getDarsHandler().getDeleteField(uriInfo, rawValues, id, bd);

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
	@Path("/cancella")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse cancella(InputStream is, @Context UriInfo uriInfo) throws Exception{
		String methodName = "cancella " + this.getNomeServizio(); // + "[" + idsAsString + "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		String idsAsString = null;
		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			
			//	JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectFormCancellazione = JSONObject.fromObject( baos.toString() );
			JSONArray jsonIDS = jsonObjectFormCancellazione.getJSONArray(IDS_TO_DELETE_PARAMETER_ID);

			List<RawParamValue> rawValues = new ArrayList<RawParamValue>();
			for (Object key : jsonObjectFormCancellazione.keySet()) { 
				String value = jsonObjectFormCancellazione.getString((String) key);
				rawValues.add(new RawParamValue((String) key, value));
			}

			idsAsString = Utils.getValue(rawValues, IDS_TO_DELETE_PARAMETER_ID);
			this.log.info("Richiesta cancellazione degli elementi con id "+idsAsString+""); 

			List<Long> idsToDelete = new ArrayList<Long>();
			if(jsonIDS != null && jsonIDS.size() > 0)
				for (int i = 0; i < jsonIDS.size(); i++) {
					long id = jsonIDS.getLong(i);
					idsToDelete.add(id); 
				}

			Elenco elenco = this.getDarsHandler().delete(idsToDelete, rawValues, uriInfo, bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(elenco); 
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".cancella.ok"));
		}catch(DeleteException e){
			this.log.info("Esito operazione "+methodName+" [" + idsAsString + "] : " + e.getEsito() + ", causa: " +e.getMessaggi());
			Elenco elenco = this.getDarsHandler().getElenco(uriInfo, bd);
			darsResponse.setEsitoOperazione(e.getEsito());
			darsResponse.setResponse(elenco); 
			darsResponse.setDettaglioEsito(e.getMessaggi());
		}  catch(WebApplicationException e){
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
	@Path("/{id}/cancella")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse cancellaDettaglio(@PathParam("id") long id, InputStream is, @Context UriInfo uriInfo) throws Exception{
		String methodName = "cancellaDettaglio " + this.getNomeServizio() + "[" + id + "]";  
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectFormCancellazione = JSONObject.fromObject( baos.toString() );

			List<RawParamValue> rawValues = new ArrayList<RawParamValue>();
			for (Object key : jsonObjectFormCancellazione.keySet()) { 
				String value = jsonObjectFormCancellazione.getString((String) key);
				rawValues.add(new RawParamValue((String) key, value));
			}

			List<Long> idsToDelete = new ArrayList<Long>();
			idsToDelete.add(id);
			Elenco elenco = this.getDarsHandler().delete(idsToDelete, rawValues, uriInfo, bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(elenco); 
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".cancellaDettaglio.ok"));
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".cancellaDettaglio.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

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
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

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
	
	public abstract IDarsHandler<?> getDarsHandler();
}
