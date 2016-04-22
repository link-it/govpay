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
package it.govpay.web.rs.dars.menu;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.intermediari.Intermediari;
import it.govpay.web.rs.dars.anagrafica.operatori.Operatori;
import it.govpay.web.rs.dars.anagrafica.portali.Portali;
import it.govpay.web.rs.dars.anagrafica.psp.Psp;
import it.govpay.web.rs.dars.anagrafica.tributi.Tributi;
import it.govpay.web.rs.dars.anagrafica.uo.UnitaOperative;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.Console;
import it.govpay.web.rs.dars.model.Console.About;
import it.govpay.web.rs.dars.model.Console.SezioneMenu;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Versamenti;
import it.govpay.web.utils.Utils;
import it.govpay.web.rs.dars.model.DarsResponse;

@Path("/dars/")
public class Menu extends BaseRsService {

	private Logger log = LogManager.getLogger();
	private String basePathServizi;
	private String nomeServizio;


	public Menu() {
		this.nomeServizio = "menu";
		this.basePathServizi = "dars";
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@Context UriInfo uriInfo) throws ConsoleException, URISyntaxException {
		initLogger(this.nomeServizio);
		String methodName = "Sezione " + this.nomeServizio ; 

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance();

			// necessario autorizzare l'utente per vedere il menu'?
			this.getOperatoreByPrincipal(bd);

			URI logout = uriInfo.getBaseUriBuilder().path("../logout").build();

			Console console = new Console(Utils.getInstance().getMessageFromResourceBundle("govpay.appTitle"), logout);
			About about = console.new About();
			about.setTitolo("GovPay - Porta di Accesso al Nodo dei Pagamenti SPC");
			about.setBuild("build09122016");
			about.setLicenza(new URI("https://raw.githubusercontent.com/link-it/GovPay/master/LICENSE"));
			about.setManualeUso(new URI("http://www.gov4j.it/gov4j/download/GovPay-ManualeUtente.pdf"));
			about.setProjectPage(new URI("http://www.gov4j.it/govpay"));
			about.setVersione("2.1");
			about.setCopyright("Copyright (c) 2014-2016 Link.it srl (http://www.link.it)");
			console.setAbout(about);
			it.govpay.web.rs.dars.model.Console.Menu menu = console.new Menu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".govpay"));
			Intermediari intermediariDars = new Intermediari();
			menu.setHome(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(intermediariDars.getNomeServizio() + ".titolo"), uriInfo.getBaseUriBuilder().path(intermediariDars.getPathServizio()).build(), false));
			SezioneMenu anagrafica = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".anagrafica"));
			Psp pspDars = new Psp();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(pspDars.getNomeServizio() + ".titolo"), uriInfo.getBaseUriBuilder().path(pspDars.getPathServizio()).build(), false));
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(intermediariDars.getNomeServizio() + ".titolo"), uriInfo.getBaseUriBuilder().path(intermediariDars.getPathServizio()).build(), false));
			
			Domini dominiDars = new Domini();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".titolo"), uriInfo.getBaseUriBuilder().path(dominiDars.getPathServizio()).build(), false));
			
			UnitaOperative uoDars = new UnitaOperative();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(uoDars.getNomeServizio() + ".titolo"), uriInfo.getBaseUriBuilder().path(uoDars.getPathServizio()).build(), false));
			
			Tributi tributiDars = new Tributi();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(tributiDars.getNomeServizio() + ".titolo"),
					uriInfo.getBaseUriBuilder().path(tributiDars.getPathServizio()).build(), false));
			
			Applicazioni applicazioniDars = new Applicazioni();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(applicazioniDars.getNomeServizio() + ".titolo"),
					uriInfo.getBaseUriBuilder().path(applicazioniDars.getPathServizio()).build(), false));
			
			Portali portaliDars = new Portali();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(portaliDars.getNomeServizio() + ".titolo"),
					uriInfo.getBaseUriBuilder().path(portaliDars.getPathServizio()).build(), false));
			
			Operatori operatoriDars = new Operatori();
			anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".titolo"),
					uriInfo.getBaseUriBuilder().path(operatoriDars.getPathServizio()).build(), false));
			menu.getSezioni().add(anagrafica);
			
			SezioneMenu monitoraggio = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".monitoraggio"));
			Versamenti versamentiDars = new Versamenti();
			monitoraggio.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".titolo"),
					uriInfo.getBaseUriBuilder().path(versamentiDars.getPathServizio()).build(), false));
			menu.getSezioni().add(monitoraggio);
			
//			SezioneMenu avanzate = console.new SezioneMenu("Avanzate");
//			avanzate.getVociMenu().add(console.new VoceMenu("Test non funzionante", uriInfo.getBaseUriBuilder().path(basePathServizi).path("test").build(), true));
//			menu.getSezioni().add(avanzate);
			console.setMenu(menu);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(console);

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito("ERR");
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta "+methodName +" evasa con successo");

		return darsResponse;
	}
}
