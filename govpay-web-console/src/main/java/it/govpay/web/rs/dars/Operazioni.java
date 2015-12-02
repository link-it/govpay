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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
//import it.govpay.bd.anagrafica.AnagraficaManager;
//import it.govpay.business.Pagamenti;
//import it.govpay.business.RegistroPSP;
//import it.govpay.business.Rendicontazioni;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/operazioni")
public class Operazioni extends BaseRsService {

	public Operazioni() {
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/aggiornaPsp")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse aggiornamentoRegistroPsp(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {

		initLogger("DARSAggiornamentoRegistroPsp");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			this.checkOperatoreAdmin(bd);

//			new RegistroPSP(bd).aggiornaRegistro();
//			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": comando non implementato");
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento del Registro Psp:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggiornamento del Registro Psp: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		return darsResponse;
	}

	@GET
	@Path("/recuperoRptPendenti")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse recuperoRptPendenti(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSRecuperoRptPendenti");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			this.checkOperatoreAdmin(bd);
			
//			new Pagamenti(bd).verificaRptPedenti();
//			log.info("Acquisizione Rpt pendenti completata");
//			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": comando non implementato");
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'acquisizione delle Rpt pendenti:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'acquisizione delle Rpt pendenti:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		return darsResponse;
	}

	@GET
	@Path("/acquisizioneRendicontazioni")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse acquisizioneRendicontazioni(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSAcquisizioneRendicontazioni");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			this.checkOperatoreAdmin(bd);
//			new Rendicontazioni(bd).downloadRendicontazioni();
//			log.info("Acquisizione rendicontazioni completata");
//			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": comando non implementato");
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'acquisizione delle Rendicontazioni:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'acquisizione delle Rendicontazioni:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		return darsResponse;
	}

	@GET
	@Path("/resetCache")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse resetCache(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSResetCache");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			this.checkOperatoreAdmin(BasicBD.getInstance());
//			log.info("Cache BasicClient svuotata");
//			AnagraficaManager.cleanCache();
//			log.info("Cache AnagraficaManager svuotata");
//			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": comando non implementato");
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante il reset della cache:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante il reset della cache: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
		}
		return darsResponse;
	}
}