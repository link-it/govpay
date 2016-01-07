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

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Tributo;
import it.govpay.dars.bd.TributiBD;
import it.govpay.dars.model.ListaTributiEntry;
import it.govpay.dars.model.TributoExt;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

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

@Path("/dars/tributi")
public class Tributi extends BaseRsService {

	public Tributi() {
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "codEnte") String codEnte,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findTributi");
		
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] codEnte["+codEnte+"] offset["+offset+"] limit["+limit+"]");

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
			TributiBD tributiBD = new TributiBD(bd);
			List<ListaTributiEntry> findAll  = new ArrayList<ListaTributiEntry>();
			TributoFilter filter = tributiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setCodEnte(codEnte);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tributo.model().COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			findAll = tributiBD.findAllEntry(filter);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dei tributi:" +e.getMessage() , e);
			throw e;
		}catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei tributi:" +e.getMessage() , e);
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

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse get(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id) throws GovPayException {
		initLogger("getTributi");
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
			TributiBD tributiBD = new TributiBD(bd);
			TributoExt tributoExt = tributiBD.getTributoExt(id);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(tributoExt);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca del tributo:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca del tributo:" +e.getMessage() , e);
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
			Tributo tributo) throws GovPayException {
		initLogger("updateTributi");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] tributo["+tributo+"]");
		
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
			TributiBD tributiBD = new TributiBD(bd);
			Tributo oldTributo = tributiBD.getTributo(tributo.getId());
			this.checkTributo(tributo, oldTributo);
			tributiBD.updateTributo(tributo); 
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA); 
		} catch(ValidationException e) {
			log.error("Riscontrato errore di validazione durante l'aggiornamento del tributo:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento del tributo:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggioramento del tributo:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		} finally {
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
			Tributo tributo) throws GovPayException {
		initLogger("insertTributi");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] tributo["+tributo+"]");
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
			this.checkTributo(tributo, null);
			TributiBD tributiBD = new TributiBD(bd);
			try {
				tributiBD.getTributo(tributo.getIdEnte(), tributo.getCodTributo());
				throw new ValidationException("Tributo [CodEnte: " + tributo.getIdEnte() + "] [CodTributo: " + tributo.getCodTributo() + "] gia' presente in anagrafica.");
			} catch (NotFoundException e) {
				// Ok non esiste gia'. posso inserire
			}
			tributiBD.insertTributo(tributo);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(tributo.getId());
		} catch(ValidationException e) {
			log.error("Riscontrato errore di validazione durante l'inserimento del tributo:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento del tributo:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento del tributo:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	private void checkTributo(Tributo tributo, Tributo oldTributo) throws ValidationException {
		if(tributo == null || tributo.getCodTributo() == null || tributo.getCodTributo().isEmpty()) throw new ValidationException("Il campo Cod Tributo e' obbligatorio");
		if(tributo == null || tributo.getDescrizione() == null || tributo.getDescrizione().isEmpty()) throw new ValidationException("Il campo Descrizione e' obbligatorio");
		if(tributo == null || tributo.getIdEnte() == 0) throw new ValidationException("Il campo Ente e' obbligatorio");
		if(tributo == null || tributo.getIbanAccredito() == 0 ) throw new ValidationException("Il campo Iban Accredito e' obbligatorio");
		if(tributo == null || tributo.getTipoContabilita() == null || tributo.getDescrizione().isEmpty()) throw new ValidationException("Il campo Tipo Contabilita' e' obbligatorio");
		if(tributo == null || tributo.getCodContabilita() == null || tributo.getCodContabilita().isEmpty()) throw new ValidationException("Il campo Cod Contabilita e' obbligatorio");
		
		if(oldTributo != null) {
			if(!tributo.getCodTributo().equals(oldTributo.getCodTributo())) throw new ValidationException("Il campo Cod Tributo non e' modificabile");
			if(tributo.getIdEnte() == oldTributo.getIdEnte()) throw new ValidationException("Il campo Ente non e' modificabile");
		}
	}
}