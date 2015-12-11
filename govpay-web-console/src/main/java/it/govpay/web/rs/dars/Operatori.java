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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.EnteFilter;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.dars.bd.ApplicazioniBD;
import it.govpay.dars.bd.EntiBD;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaEntiEntry;
import it.govpay.dars.model.OperatoreExt;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/operatori")
public class Operatori extends BaseRsService {
	
	Logger log = LogManager.getLogger();
	
	public Operatori(){
			
	}

	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findOperatori");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] offset["+offset+"] limit["+limit+"]");
		
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			
			this.checkOperatoreAdmin(bd);
			
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			OperatoreFilter filter = operatoriBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operatore.model().PRINCIPAL);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			
			List<Operatore> findAll = operatoriBD.findAll(filter);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca degli operatori:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca degli operatori:" +e.getMessage() , e);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
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
		initLogger("getOperatori");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] id["+id+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			this.checkOperatoreAdmin(bd);

			darsResponse.setResponse(getOperatore(bd, id));
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dell'operatore:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();


			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	private OperatoreExt getOperatore(BasicBD bd, long id) throws Exception {
		OperatoriBD operatoriBD = new OperatoriBD(bd);
		Operatore operatore = operatoriBD.getOperatore(id);
		List<ListaEntiEntry> listaEnti = null;
		if(operatore.getIdEnti() != null && operatore.getIdEnti().size() > 0){
			EntiBD entiBD = new EntiBD(bd);
			EnteFilter filter = entiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Ente.model().COD_ENTE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			filter.setListaIdEnti(operatore.getIdEnti());
			listaEnti = entiBD.findAll(filter);
		}
				
		List<ListaApplicazioniEntry> listaApplicazioni = null;
		if(operatore.getIdApplicazioni() != null && operatore.getIdApplicazioni().size() > 0){
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = applicazioniBD.newFilter();
			filter.setListaIdApplicazioni(operatore.getIdApplicazioni());
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			listaApplicazioni = applicazioniBD.findAll(filter);
		}

		return new OperatoreExt(operatore, listaEnti, listaApplicazioni);
		
	}
	
	@GET
	@Path("/ruoliutente")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse getRuoliOperatore() throws GovPayException {
		initLogger("getRuoliOperatore");
		log.info("Ricevuta richiesta");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		List<String> listaRuoli = new ArrayList<String>();
		try {
			bd = BasicBD.getInstance();
			Operatore operatore = getOperatoreByPrincipal(bd);
						
			listaRuoli.add(operatore.getProfilo().getCodifica());
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(listaRuoli);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dei ruoli dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei ruoli operatore:" +e.getMessage() , e);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
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
			OperatoreExt operatore) throws GovPayException {
		initLogger("updateOperatori");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] operatore["+operatore+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			this.checkOperatoreAdmin(bd);
			
			OperatoreExt oldOperatore = getOperatore(bd, operatore.getOperatore().getId());
			this.checkOperatore(operatore, oldOperatore);

			if(operatore != null){
				OperatoriBD operatoriBD = new OperatoriBD(bd);
				operatoriBD.updateOperatore(operatore.getOperatore());
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);

		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'aggiornamento dell'operatore:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggioramento dell'operatore:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();


			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
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
			OperatoreExt operatore) throws GovPayException {
		initLogger("insertOperatori");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] operatore["+operatore+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			this.checkOperatoreAdmin(bd);

			this.checkOperatore(operatore, null);
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore2 = operatore.getOperatore();
			operatoriBD.insertOperatore(operatore2);
			Long id = operatore2.getId();

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(id);

		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'inserimento dell'operatore:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento dell'operatore:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	public void checkOperatore(OperatoreExt operatore, OperatoreExt oldOperatore) throws ValidationException {

		
	}
	
	
	@GET
	@Path("/user")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse getOperatore() throws GovPayException {
		initLogger("getOperatore");
		log.info("Ricevuta richiesta");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			Operatore operatore = getOperatoreByPrincipal(bd);
			
			// Reset ID DB
			operatore.setId(null);
						
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(operatore);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dell'operatore:" +e.getMessage() , e);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
		
	}
}
