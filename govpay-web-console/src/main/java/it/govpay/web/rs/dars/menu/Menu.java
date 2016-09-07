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
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.intermediari.Intermediari;
import it.govpay.web.rs.dars.anagrafica.operatori.Operatori;
import it.govpay.web.rs.dars.anagrafica.portali.Portali;
import it.govpay.web.rs.dars.anagrafica.psp.Psp;
import it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.manutenzione.strumenti.Strumenti;
import it.govpay.web.rs.dars.model.Console;
import it.govpay.web.rs.dars.model.Console.About;
import it.govpay.web.rs.dars.model.Console.SezioneMenu;
import it.govpay.web.rs.dars.model.Console.VoceMenu;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.rs.dars.monitoraggio.rendicontazioni.FrApplicazioni;
import it.govpay.web.rs.dars.monitoraggio.rendicontazioni.Rendicontazioni;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Versamenti;
import it.govpay.web.rs.dars.reportistica.pagamenti.Pagamenti;
import it.govpay.web.utils.Utils;

@Path("/dars/")
public class Menu extends BaseRsService {

	private Logger log = LogManager.getLogger();
	private String pathServizio;
	private String nomeServizio;


	public Menu() {
		this.nomeServizio = "menu";
		this.pathServizio = "/dars/";
	}

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse creaMenu(
			@Context UriInfo uriInfo) throws ConsoleException, URISyntaxException {
		this.initLogger(this.nomeServizio);
		String methodName = "Sezione " + this.nomeServizio ; 

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance(this.codOperazione);

			// controllo delle autorizzazioni dell'utente
			Operatore operatore = this.getOperatoreByPrincipal(bd);
			ProfiloOperatore profilo = operatore.getProfilo(); 

			URI logout = BaseRsService.checkDarsURI(uriInfo).path("../logout").build();

			Console console = new Console(Utils.getInstance().getMessageFromResourceBundle("govpay.appTitle"), logout);
			About about = console.new About();
			about.setTitolo(Utils.getInstance().getMessageFromResourceBundle("govpay.about.titolo"));
			about.setBuild(Utils.getInstance().getMessageFromResourceBundle("govpay.about.build"));
			about.setLicenza(new URI(Utils.getInstance().getMessageFromResourceBundle("govpay.about.licenza")));
			about.setManualeUso(new URI(Utils.getInstance().getMessageFromResourceBundle("govpay.about.manuale")));
			about.setProjectPage(new URI(Utils.getInstance().getMessageFromResourceBundle("govpay.about.paginaProgetto")));
			about.setVersione(Utils.getInstance().getMessageFromResourceBundle("govpay.about.versione"));
			about.setCopyright(Utils.getInstance().getMessageFromResourceBundle("govpay.about.copyright"));
			console.setAbout(about);
			it.govpay.web.rs.dars.model.Console.Menu menu = console.new Menu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".govpay"));

			Intermediari intermediariDars = new Intermediari();
			Versamenti versamentiDars = new Versamenti();


			URI versamentiURI = BaseRsService.checkDarsURI(uriInfo).path(versamentiDars.getPathServizio()).build();
			VoceMenu voceMenuVersamenti = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".titolo"),	versamentiURI, false);

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				menu.setHome(voceMenuVersamenti);

				SezioneMenu anagrafica = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".anagrafica"));
				Psp pspDars = new Psp();
				URI pspURI = BaseRsService.checkDarsURI(uriInfo).path(pspDars.getPathServizio()).build();
				anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(pspDars.getNomeServizio() + ".titolo"), pspURI, false));

				URI intermediariURI = BaseRsService.checkDarsURI(uriInfo).path(intermediariDars.getPathServizio()).build();
				VoceMenu voceMenuIntermediari = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(intermediariDars.getNomeServizio() + ".titolo"),	intermediariURI, false);
				anagrafica.getVociMenu().add(voceMenuIntermediari);
				
				TipiTributo tipoTributiDars = new TipiTributo();
				URI tipiTributiURI = BaseRsService.checkDarsURI(uriInfo).path(tipoTributiDars.getPathServizio()).build();
				VoceMenu voceMenuTipoTributi = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(tipoTributiDars.getNomeServizio() + ".titolo"),	tipiTributiURI, false);
				anagrafica.getVociMenu().add(voceMenuTipoTributi);

				Domini dominiDars = new Domini();
				URI dominiURI = BaseRsService.checkDarsURI(uriInfo).path(dominiDars.getPathServizio()).build();
				anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".titolo"), dominiURI, false));

				Applicazioni applicazioniDars = new Applicazioni();
				URI applicazioniURI = BaseRsService.checkDarsURI(uriInfo).path(applicazioniDars.getPathServizio()).build();
				anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(applicazioniDars.getNomeServizio() + ".titolo"),	applicazioniURI, false));

				Portali portaliDars = new Portali();
				URI portaliURI = BaseRsService.checkDarsURI(uriInfo).path(portaliDars.getPathServizio()).build();
				anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(portaliDars.getNomeServizio() + ".titolo"), portaliURI, false));

				Operatori operatoriDars = new Operatori();
				URI operatoriURI = BaseRsService.checkDarsURI(uriInfo).path(operatoriDars.getPathServizio()).build();
				anagrafica.getVociMenu().add(console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".titolo"), operatoriURI, false));
				menu.getSezioni().add(anagrafica);

			} else {
				menu.setHome(voceMenuVersamenti);

			}
			
			// Sezione Monitoraggio
			SezioneMenu monitoraggio = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".monitoraggio"));

			monitoraggio.getVociMenu().add(voceMenuVersamenti);

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				Rendicontazioni rendicontazioniDars = new Rendicontazioni();
				URI rendicontazioniURI = BaseRsService.checkDarsURI(uriInfo).path(rendicontazioniDars.getPathServizio()).build();
				VoceMenu voceMenuRendicontazioni = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(rendicontazioniDars.getNomeServizio() + ".titolo"),	rendicontazioniURI, false);

				monitoraggio.getVociMenu().add(voceMenuRendicontazioni);
			}else {
				FrApplicazioni frApplicazioniDars = new FrApplicazioni();
				URI frApplicazioniURI = BaseRsService.checkDarsURI(uriInfo).path(frApplicazioniDars.getPathServizio()).build();
				VoceMenu voceMenuFrApplicazioni = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(frApplicazioniDars.getNomeServizio() + ".titolo"),	frApplicazioniURI, false);

				monitoraggio.getVociMenu().add(voceMenuFrApplicazioni);
			}

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				Eventi eventiDars = new Eventi();
				URI eventiURI = BaseRsService.checkDarsURI(uriInfo).path(eventiDars.getPathServizio()).build();
				VoceMenu voceMenuEventi = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".titolo"),	eventiURI, false);

				monitoraggio.getVociMenu().add(voceMenuEventi);
			}

			menu.getSezioni().add(monitoraggio);
			
			// Sezione Reportistica
			SezioneMenu reportistica = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".reportistica"));
			
			Pagamenti reportisticaPagamentiDars = new Pagamenti();
			URI reportisticaPagamentiURI = BaseRsService.checkDarsURI(uriInfo).path(reportisticaPagamentiDars.getPathServizio()).build();
			VoceMenu voceMenuReportisticaPagamenti = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(reportisticaPagamentiDars.getNomeServizio() + ".titolo"), reportisticaPagamentiURI, false);

			reportistica.getVociMenu().add(voceMenuReportisticaPagamenti);
			
			menu.getSezioni().add(reportistica);

			if(profilo.equals(ProfiloOperatore.ADMIN)){
				// sezione manutenzione
				SezioneMenu manutenzione = console.new SezioneMenu(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".manutenzione"));

				Strumenti strumentiDars = new Strumenti();
				URI strumentiURI = BaseRsService.checkDarsURI(uriInfo).path(strumentiDars.getPathServizio()).build();
				VoceMenu voceMenuStrumenti = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(strumentiDars.getNomeServizio() + ".titolo"),	strumentiURI, false);
				manutenzione.getVociMenu().add(voceMenuStrumenti);
				menu.getSezioni().add(manutenzione);
			}

			console.setMenu(menu);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(console);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");

		return darsResponse;
	}

	public String getPathServizio() {
		return this.pathServizio;
	}

	public String getNomeServizio() {
		return this.nomeServizio;
	}


}
