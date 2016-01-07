/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.EnteFilter;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.dars.bd.EntiBD;
import it.govpay.dars.model.EnteExt;
import it.govpay.dars.model.ListaEntiEntry;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

 
@Path("/dars/enti")
public class Enti extends BaseRsService {

	public Enti() {
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "codDominio") String codDominio,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {

		initLogger("findEnti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] codDominio["+codDominio+"] offset["+offset+"] limit["+limit+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			Operatore operatore = this.getOperatoreByPrincipal(bd);

			EntiBD entiBD = new EntiBD(bd);
			EnteFilter filter = entiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setCodDominio(codDominio); 
			
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Ente.model().COD_ENTE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<ListaEntiEntry> findAll = null;			
			// Se l'utente non e' admin puo'vedere solo gli enti che sono associati a lui.
			if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())){
				if(operatore.getIdEnti() == null || operatore.getIdEnti().isEmpty())
					findAll = new ArrayList<ListaEntiEntry>();
				else 
					filter.setListaIdEnti(operatore.getIdEnti());
			}
			if(findAll == null)
				findAll = entiBD.findAllEntry(filter);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca degli enti:" +e.getMessage() , e);
			throw e;
		}catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca delle enti:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse get(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id) throws GovPayException {
		initLogger("getEnti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] id["+id+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			EntiBD entiBD = new EntiBD(bd);
			EnteExt enteExt = entiBD.getEnteExt(id);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(enteExt);
		} catch(WebApplicationException e) {
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'ente:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dell'ente:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
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
	public DarsResponse update(
			@QueryParam(value = "operatore") String principalOperatore,
			Ente ente) throws GovPayException {
		initLogger("updateEnti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] ente["+ente+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			EntiBD entiBD = new EntiBD(bd);
			Ente oldEnte = entiBD.getEnte(ente.getCodEnte());
			this.checkEnte(ente, oldEnte);
			entiBD.updateEnte(ente);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(ValidationException e) {
			log.error("Riscontrato errore di validazione durante l'aggiornamento dell'Ente:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento dell'ente:" +e.getMessage() , e);
			throw e;
		}catch (Exception e) {
			log.error("Riscontrato errore durante l'aggioramento dell'ente:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	public DarsResponse insert(
			@QueryParam(value = "operatore") String principalOperatore,
			Ente ente) throws GovPayException {
		initLogger("insertEnti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] ente["+ente+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			this.checkEnte(ente, null);
			EntiBD entiBD = new EntiBD(bd);
			try {
				entiBD.getEnte(ente.getCodEnte());
				throw new ValidationException("L'ente con id [" + ente.getCodEnte() + "] e' gia' presente in anagrafica.");
			} catch (NotFoundException e) {
				// Ok non esiste gia'. posso inserire
			}
			entiBD.insertEnte(ente);
			Long id = ente.getId();
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(id);
		} catch(ValidationException e) {
			log.error("Riscontrato errore di validazione durante l'aggiornamento dell'Ente:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento dell'ente:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento dell'ente:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	private void checkEnte(Ente ente, Ente oldEnte) throws ValidationException {
		if(ente == null || ente.getCodEnte() == null || ente.getCodEnte().isEmpty()) throw new ValidationException("Il campo Cod Ente e' obbligatorio");
		if(ente.getAnagraficaEnte() == null || ente.getAnagraficaEnte().getRagioneSociale() == null || ente.getAnagraficaEnte().getRagioneSociale().isEmpty()) throw new ValidationException("Il campo Ragione Sociale e' obbligatorio");
		if(ente.getAnagraficaEnte() == null || ente.getAnagraficaEnte().getCodUnivoco() == null || ente.getAnagraficaEnte().getCodUnivoco().isEmpty()) throw new ValidationException("Il campo Cod Univoco e' obbligatorio");
		if(ente.getIdDominio() == 0) throw new ValidationException("Il campo Dominio e' obbligatorio");
		if(oldEnte != null) {
			if(!ente.getCodEnte().equals(oldEnte.getCodEnte())) throw new ValidationException("Il campo Cod Ente non e' modificabile");
			if(ente.getIdDominio() == oldEnte.getIdDominio()) throw new ValidationException("Il campo Dominio non e' modificabile");
		}
	}
}