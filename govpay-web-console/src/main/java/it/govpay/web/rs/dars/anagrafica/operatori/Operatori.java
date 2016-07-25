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
package it.govpay.web.rs.dars.anagrafica.operatori;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Operatore;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/operatori")
public class Operatori extends BaseDarsService {

	Logger log = LogManager.getLogger();
	
	public Operatori(){
		super();
	}
	
	@Override
	public String getNomeServizio() {
		return "operatori";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new OperatoriHandler(this.log,this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}

	@GET
	@Path("/user")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse getOperatore() throws ConsoleException {
		this.initLogger("getOperatore");
		this.log.info("Ricevuta richiesta");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(this.codOperazione);
			Operatore operatore = this.getOperatoreByPrincipal(bd);

			// Reset ID DB
			operatore.setId(null);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(operatore);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante la ricerca dell'operatore:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante la ricerca dell'operatore:" +e.getMessage() , e);

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(BaseRsService.ERRORE_INTERNO);
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;

	}
	
	@GET
	@Path("/userNonAutorizzato")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse getOperatoreNonAutorizzato() throws ConsoleException {
		this.initLogger("simulazioneUtenteNonAutorizzato");
		
		this.log.info("Ricevuta richiesta");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(this.codOperazione);
			Operatore operatore = this.getOperatoreByPrincipal(bd,"UTENTE_NON_ESISTENTE");

			// Reset ID DB
			operatore.setId(null);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(operatore);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante la ricerca dell'operatore:" +e.getMessage() , e);
			// Invalido la sessione appena creata dal container.
			this.invalidateSession(this.log);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante la ricerca dell'operatore:" +e.getMessage() , e);

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(BaseRsService.ERRORE_INTERNO);
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;

	}

}
