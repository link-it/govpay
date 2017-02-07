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
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
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
import it.govpay.web.rs.dars.model.About;
import it.govpay.web.rs.dars.model.Console;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.SezioneMenu;
import it.govpay.web.rs.dars.model.VoceMenu;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
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

			URI logout = new URI("/logout"); 

			Console console = new Console(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.appTitle"), logout);
			About about = new About();
			about.setTitolo(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.titolo"));
			about.setBuild(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.build"));
			about.setLicenza(new URI(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.licenza")));
			about.setManualeUso(new URI(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.manuale")));
			about.setProjectPage(new URI(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.paginaProgetto")));
			about.setVersione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.versione"));
			about.setCopyright(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("govpay.about.copyright"));
			console.setAbout(about);
			it.govpay.web.rs.dars.model.Menu menu = new it.govpay.web.rs.dars.model.Menu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".govpay"));

			Intermediari intermediariDars = new Intermediari();
			Versamenti versamentiDars = new Versamenti();


			URI versamentiURI = new URI(versamentiDars.getPathServizio()); 
			VoceMenu voceMenuVersamenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".titolo"),	versamentiURI, false);

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				menu.setHome(voceMenuVersamenti);

				SezioneMenu anagrafica = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anagrafica"));
				Psp pspDars = new Psp();
				URI pspURI = new URI(pspDars.getPathServizio());
				anagrafica.getVociMenu().add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pspDars.getNomeServizio() + ".titolo"), pspURI, false));

				URI intermediariURI = new URI(intermediariDars.getPathServizio());
				VoceMenu voceMenuIntermediari = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio() + ".titolo"),	intermediariURI, false);
				anagrafica.getVociMenu().add(voceMenuIntermediari);
				
				TipiTributo tipoTributiDars = new TipiTributo();
				URI tipiTributiURI = new URI(tipoTributiDars.getPathServizio());
				VoceMenu voceMenuTipoTributi = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tipoTributiDars.getNomeServizio() + ".titolo"),	tipiTributiURI, false);
				anagrafica.getVociMenu().add(voceMenuTipoTributi);

				Domini dominiDars = new Domini();
				URI dominiURI = new URI(dominiDars.getPathServizio());
				anagrafica.getVociMenu().add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".titolo"), dominiURI, false));

				Applicazioni applicazioniDars = new Applicazioni();
				URI applicazioniURI = new URI(applicazioniDars.getPathServizio());
				anagrafica.getVociMenu().add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(applicazioniDars.getNomeServizio() + ".titolo"),	applicazioniURI, false));

				Portali portaliDars = new Portali();
				URI portaliURI = new URI(portaliDars.getPathServizio());
				anagrafica.getVociMenu().add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(portaliDars.getNomeServizio() + ".titolo"), portaliURI, false));

				Operatori operatoriDars = new Operatori();
				URI operatoriURI = new URI(operatoriDars.getPathServizio());
				anagrafica.getVociMenu().add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".titolo"), operatoriURI, false));
				
				menu.getSezioni().add(anagrafica);

			} else {
				menu.setHome(voceMenuVersamenti);

			}
			
			// Sezione Monitoraggio
			SezioneMenu monitoraggio = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".monitoraggio"));

			monitoraggio.getVociMenu().add(voceMenuVersamenti);

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				Rendicontazioni rendicontazioniDars = new Rendicontazioni();
				URI rendicontazioniURI = new URI(rendicontazioniDars.getPathServizio());
				VoceMenu voceMenuRendicontazioni = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(rendicontazioniDars.getNomeServizio() + ".titolo"),	rendicontazioniURI, false);

				monitoraggio.getVociMenu().add(voceMenuRendicontazioni);
			}else {
				
				// TODO GIULIANO
				
//				FrApplicazioni frApplicazioniDars = new FrApplicazioni();
//				URI frApplicazioniURI = new URI(frApplicazioniDars.getPathServizio());
//				VoceMenu voceMenuFrApplicazioni = console.new VoceMenu(Utils.getInstance().getMessageFromResourceBundle(frApplicazioniDars.getNomeServizio() + ".titolo"),	frApplicazioniURI, false);
//
//				monitoraggio.getVociMenu().add(voceMenuFrApplicazioni);
			}

			if(profilo.equals(ProfiloOperatore.ADMIN)){

				Eventi eventiDars = new Eventi();
				URI eventiURI = new URI(eventiDars.getPathServizio());
				VoceMenu voceMenuEventi = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".titolo"),	eventiURI, false);

				monitoraggio.getVociMenu().add(voceMenuEventi);
			}

			menu.getSezioni().add(monitoraggio);
			
			// Sezione Reportistica
			SezioneMenu reportistica = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".reportistica"));
			
			Pagamenti reportisticaPagamentiDars = new Pagamenti();
			URI reportisticaPagamentiURI = new URI(reportisticaPagamentiDars.getPathServizio());
			VoceMenu voceMenuReportisticaPagamenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(reportisticaPagamentiDars.getNomeServizio() + ".titolo"), reportisticaPagamentiURI, false);

			reportistica.getVociMenu().add(voceMenuReportisticaPagamenti);
			
//			EstrattiConto estrattiContoDars = new EstrattiConto();
//			URI estrattiContoURI = new URI(estrattiContoDars.getPathServizio()).build();
//			VoceMenu voceMenuReportisticaEstrattiConto = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(estrattiContoDars.getNomeServizio() + ".titolo"), estrattiContoURI, false);

//			reportistica.getVociMenu().add(voceMenuReportisticaEstrattiConto);
			
			menu.getSezioni().add(reportistica);

			if(profilo.equals(ProfiloOperatore.ADMIN)){
				// sezione manutenzione
				SezioneMenu manutenzione = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".manutenzione"));

				Strumenti strumentiDars = new Strumenti();
				URI strumentiURI = new URI(strumentiDars.getPathServizio());
				VoceMenu voceMenuStrumenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(strumentiDars.getNomeServizio() + ".titolo"),	strumentiURI, false);
				manutenzione.getVociMenu().add(voceMenuStrumenti);
				menu.getSezioni().add(manutenzione);
			}

			console.setMenu(menu);
			
			// caricamento della sezione lingua
			console.setLingue(Utils.getInstance().getMapLingue()); 
			

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
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".erroreGenerico"));
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
