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
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Stazione;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.expression.SortOrder;

@Path("/dars/stazioni")
public class Stazioni extends BaseRsService {



	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "codIntermediario") String codIntermediario,
			@QueryParam(value = "idIntermediario") long idIntermediario,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findStazioni");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] codIntermediario["+codIntermediario+"]"
				+ " idIntermediario["+idIntermediario+"] offset["+offset+"] limit["+limit+"]");
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

			List<Stazione> stazioni = new ArrayList<Stazione>();
			if(idIntermediario > 0){
				IntermediariBD intermediariBD = new IntermediariBD(bd);
				Intermediario intermediario = intermediariBD.getIntermediario(idIntermediario);
				StazioniBD stazioniBD = new StazioniBD(bd);
				stazioni = stazioniBD.getStazioni(intermediario.getId());

			}else {
				StazioniBD stazioniBD = new StazioniBD(bd);
				StazioneFilter filter = stazioniBD.newFilter();
				filter.setOffset(offset);
				filter.setLimit(limit);
				filter.setCodIntermediario(codIntermediario);
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);
				stazioni = stazioniBD.findAll(filter);
			}

			darsResponse.setResponse(stazioni);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca delle stazioni:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca delle stazioni:" +e.getMessage() , e);

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

	@PUT
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	public DarsResponse update(
			@QueryParam(value = "operatore") String principalOperatore,
			Stazione stazione) throws GovPayException {
		initLogger("updateStazioni");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] stazione["+stazione+"]");
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
		stazione.setApplicationCode(getApplicationCode(stazione.getCodStazione()));

			StazioniBD stazioniBD = new StazioniBD(bd);
			stazioniBD.updateStazione(stazione);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA); //TODO discriminare tra ESEGUITA e NONESEGUITA

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento della stazione:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggiornamento della stazione:" +e.getMessage() , e);

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
			Stazione stazione) throws GovPayException {
		initLogger("insertStazioni");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] stazione["+stazione+"]");

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
		stazione.setApplicationCode(getApplicationCode(stazione.getCodStazione()));
		
			StazioniBD stazioniBD = new StazioniBD(bd);
			stazioniBD.insertStazione(stazione);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(stazione.getId());

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento della stazione:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento della stazione:" +e.getMessage() , e);

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
	
	private static int getApplicationCode(String codStazione) throws Exception {
		if(codStazione == null) throw new Exception("Impossibile ricavare il codApplicazione dal codStazione: codStazione nullo");
		if(codStazione.length() < 2) throw new Exception("Impossibile ricavare il codApplicazione dal codStazione ["+codStazione+"]: codStazione di lunghezza ["+codStazione.length()+"] inferiore a 2");
		
		String codApplString = codStazione.substring(codStazione.length() - 2);
		try {
			return Integer.parseInt(codApplString);
		} catch(NumberFormatException e) {
			 throw new Exception("Impossibile ricavare il codApplicazione dal codStazione ["+codStazione+"]: ultimi due caratteri del codStazione ["+codApplString+"] non corrispondono ad un intero");
		} 
	}
}

