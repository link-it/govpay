/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.io.InputStream;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


	protected Logger log = LogManager.getLogger();

	public BaseDarsService() {
		super();
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find( 	@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "find " + this.getNomeServizio(); 
		initLogger(methodName);

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance();
			Elenco elenco = getDarsHandler().getElenco(uriInfo,bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(elenco);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".elenco.erroreGenerico"));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/field/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse field(List<RawParamValue> rawValues, 
			@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception,WebApplicationException{
		String methodName = "field " + this.getNomeServizio() + "." + id; 
		initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance();
			Object field = getDarsHandler().getField(uriInfo, rawValues, id, bd);

			// Field richiesto non valido
			if(field == null){
				darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
				darsResponse.setDettaglioEsito(Utils.getInstance().getMessageWithParamsFromResourceBundle("field.fieldNonPresente", id,this.getNomeServizio()));
				return darsResponse;
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(field);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageWithParamsFromResourceBundle("field.erroreGenerico", id,this.getNomeServizio()));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse get(
			@PathParam("id") long id,
			@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "get " + this.getNomeServizio() + "." + id; 
		initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance();

			Dettaglio dettaglio = getDarsHandler().getDettaglio(id,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".dettaglio.erroreGenerico"));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/cancella")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse delete(List<Long> idsToDelete, @Context UriInfo uriInfo) throws Exception{
		StringBuffer sb = new StringBuffer();

		if(idsToDelete != null && idsToDelete.size() > 0)
			for (Long long1 : idsToDelete) {
				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "delete " + this.getNomeServizio() + "[" + sb.toString() + "]";  
		initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance();

			getDarsHandler().delete(idsToDelete, uriInfo, bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".cancella.erroreGenerico"));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse insert(InputStream is, @Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "insert " + this.getNomeServizio(); 
		initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance();

			Dettaglio dettaglio = getDarsHandler().insert(is,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".creazione.ok")); 
		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".creazione.erroreValidazione")+ " " + e.getMessage());
			return darsResponse;
		} catch(DuplicatedEntryException e){
			log.error("Riscontrata errore di entry duplicata durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".creazione.erroreGenerico"));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	@PUT
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse update(InputStream is, @Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "update " + this.getNomeServizio(); 
		initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		try {
			bd = BasicBD.newInstance();

			Dettaglio dettaglio = this.getDarsHandler().update(is,uriInfo,bd);

			darsResponse.setResponse(dettaglio);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.ok")); 
		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.erroreValidazione")+ " " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.getNomeServizio()+".aggiornamento.erroreGenerico"));
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
		

	public abstract String getNomeServizio();

	public abstract String getPathServizio();

	public abstract IDarsHandler<?> getDarsHandler();

}
