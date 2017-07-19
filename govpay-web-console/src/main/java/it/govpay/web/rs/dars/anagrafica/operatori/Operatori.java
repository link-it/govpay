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
package it.govpay.web.rs.dars.anagrafica.operatori;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Operatore;
import it.govpay.model.Ruolo;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.anagrafica.ruoli.Ruoli;
import it.govpay.web.rs.dars.anagrafica.ruoli.RuoliHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.utils.Utils;

@Path("/dars/operatori")
public class Operatori extends DarsService {

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
	
	@Override
	public Servizio getFunzionalita() {
		return Servizio.Anagrafica_Utenti;
	}

	@GET
	@Path("/user")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse getOperatore() throws ConsoleException {
		this.initLogger("getOperatore");
		this.log.info("Ricevuta richiesta carico profilo operatore ["+this.getPrincipal()+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(this.codOperazione);
			Operatore operatore = this.getOperatoreByPrincipal(bd);

			// Reset ID DB
			operatore.setId(null);
			
			// ruoli in formato descrizione (codice)
			List<Ruolo> ruoliOperatore = this.getRuoliOperatore(bd,operatore);
			List<String> ruoliOperatoreString = new ArrayList<String>();
			Ruoli ruoliDars = new Ruoli();
			RuoliHandler ruoliDarsHandler = (RuoliHandler) ruoliDars.getDarsHandler();
			for (Ruolo ruolo : ruoliOperatore) {
				if(!ruolo.getCodRuolo().equals(Operatore.RUOLO_SYSTEM)){
					ruoliOperatoreString.add(ruoliDarsHandler.getTitolo(ruolo, bd));
				}else {
					ruoliOperatoreString.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio() + ".ruoli."+Operatore.RUOLO_SYSTEM+".label"));
				}
			}
			
			operatore.setRuoli(ruoliOperatoreString);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(operatore);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante la ricerca dell'operatore ["+this.getPrincipal()+"], utente non riconosciuto.", e);
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
