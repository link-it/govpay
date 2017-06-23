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
import java.util.ArrayList;
import java.util.List;

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
import it.govpay.model.Acl.Servizio;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.intermediari.Intermediari;
import it.govpay.web.rs.dars.anagrafica.operatori.Operatori;
import it.govpay.web.rs.dars.anagrafica.portali.Portali;
import it.govpay.web.rs.dars.anagrafica.psp.Psp;
import it.govpay.web.rs.dars.anagrafica.ruoli.Ruoli;
import it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo;
import it.govpay.web.rs.dars.caricamenti.tracciati.Tracciati;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.manutenzione.strumenti.Strumenti;
import it.govpay.web.rs.dars.model.About;
import it.govpay.web.rs.dars.model.Console;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.SezioneMenu;
import it.govpay.web.rs.dars.model.VoceMenu;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.rs.dars.monitoraggio.incassi.Incassi;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.Pagamenti;
import it.govpay.web.rs.dars.monitoraggio.rendicontazioni.Fr;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Transazioni;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Versamenti;
import it.govpay.web.rs.dars.statistiche.transazioni.StatisticheTransazioni;
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
			this.getOperatoreByPrincipal(bd);

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

			SezioneMenu anagrafica = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anagrafica"));
			List<VoceMenu> vociMenuAnagrafica = new ArrayList<VoceMenu>();

			if(this.checkDirittiServizioOperatore(bd, Servizio.Anagrafica_PagoPa)){
				Psp pspDars = new Psp();
				URI pspURI = new URI(pspDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pspDars.getNomeServizio() + ".titolo"), pspURI, VoceMenu.VOCE_ANAGRAFICA));

				Intermediari intermediariDars = new Intermediari();
				URI intermediariURI = new URI(intermediariDars.getPathServizio());
				VoceMenu voceMenuIntermediari = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio() + ".titolo"),	intermediariURI, VoceMenu.VOCE_ANAGRAFICA);
				vociMenuAnagrafica.add(voceMenuIntermediari);
			}

			if(this.checkDirittiServizioOperatore(bd, Servizio.Anagrafica_Contabile)){
				TipiTributo tipoTributiDars = new TipiTributo();
				URI tipiTributiURI = new URI(tipoTributiDars.getPathServizio());
				VoceMenu voceMenuTipoTributi = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tipoTributiDars.getNomeServizio() + ".titolo"),	tipiTributiURI, VoceMenu.VOCE_ANAGRAFICA);
				vociMenuAnagrafica.add(voceMenuTipoTributi);
			}

			if(this.checkDirittiServizioOperatore(bd, Servizio.Anagrafica_PagoPa)){
				Domini dominiDars = new Domini();
				URI dominiURI = new URI(dominiDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".titolo"), dominiURI, VoceMenu.VOCE_ANAGRAFICA));
			}

			if(this.checkDirittiServizioOperatore(bd, Servizio.Anagrafica_Applicazioni)){
				Applicazioni applicazioniDars = new Applicazioni();
				URI applicazioniURI = new URI(applicazioniDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(applicazioniDars.getNomeServizio() + ".titolo"),	applicazioniURI, VoceMenu.VOCE_ANAGRAFICA));

				Portali portaliDars = new Portali();
				URI portaliURI = new URI(portaliDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(portaliDars.getNomeServizio() + ".titolo"), portaliURI, VoceMenu.VOCE_ANAGRAFICA));
			}

			if(this.checkDirittiServizioOperatore(bd, Servizio.Anagrafica_Utenti)){
				Operatori operatoriDars = new Operatori();
				URI operatoriURI = new URI(operatoriDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".titolo"), operatoriURI, VoceMenu.VOCE_ANAGRAFICA));

				Ruoli ruoliDars = new Ruoli();
				URI ruoliURI = new URI(ruoliDars.getPathServizio());
				vociMenuAnagrafica.add(new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(ruoliDars.getNomeServizio() + ".titolo"), ruoliURI, VoceMenu.VOCE_ANAGRAFICA));
			}

			if(vociMenuAnagrafica.size() > 0){	
				anagrafica.getVociMenu().addAll(vociMenuAnagrafica);
				menu.getSezioni().add(anagrafica);
			}

			// Sezione Monitoraggio
			SezioneMenu monitoraggio = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".monitoraggio"));
			List<VoceMenu> vociMenuMonitoraggio = new ArrayList<VoceMenu>();

			if(this.checkDirittiServizioOperatore(bd, Servizio.Gestione_Pagamenti)){
				Versamenti versamentiDars = new Versamenti();
				URI versamentiURI = new URI(versamentiDars.getPathServizio()); 
				VoceMenu voceMenuVersamenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".titolo"),	versamentiURI, VoceMenu.VOCE_MONITORAGGIO);
				vociMenuMonitoraggio.add(voceMenuVersamenti);
				menu.setHome(voceMenuVersamenti);

				Transazioni transazioniDars = new Transazioni();
				URI transazioniURI = new URI(transazioniDars.getPathServizio());
				VoceMenu voceMenuTransazioni = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(transazioniDars.getNomeServizio() + ".titolo"), transazioniURI, VoceMenu.VOCE_MONITORAGGIO);
				vociMenuMonitoraggio.add(voceMenuTransazioni);

				Pagamenti pagamentiDars = new Pagamenti();
				URI pagamentiURI = new URI(pagamentiDars.getPathServizio());
				VoceMenu voceMenuPagamenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".titolo"), pagamentiURI, VoceMenu.VOCE_MONITORAGGIO);

				vociMenuMonitoraggio.add(voceMenuPagamenti);
			}

			if(this.checkDirittiServizioOperatore(bd, Servizio.Gestione_Rendicontazioni)){
				Fr frDars = new Fr();
				URI frURI = new URI(frDars.getPathServizio());
				VoceMenu voceMenuFlussiRendicontazioni = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(frDars.getNomeServizio() + ".titolo"),	frURI, VoceMenu.VOCE_MONITORAGGIO);

				vociMenuMonitoraggio.add(voceMenuFlussiRendicontazioni);

				Incassi incassiDars = new Incassi();
				URI incassiURI = new URI(incassiDars.getPathServizio());
				VoceMenu voceMenuIncassi = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(incassiDars.getNomeServizio() + ".titolo"), incassiURI, VoceMenu.VOCE_MONITORAGGIO);

				vociMenuMonitoraggio.add(voceMenuIncassi);
			}
			
			if(this.checkDirittiServizioOperatore(bd, Servizio.Giornale_Eventi)){
				Eventi eventiDars = new Eventi();
				URI eventiURI = new URI(eventiDars.getPathServizio());
				VoceMenu voceMenuEventi = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".titolo"),	eventiURI, VoceMenu.VOCE_MONITORAGGIO);
				vociMenuMonitoraggio.add(voceMenuEventi);
			}
			
			if(vociMenuMonitoraggio.size() > 0){	
				monitoraggio.getVociMenu().addAll(vociMenuMonitoraggio);
				menu.getSezioni().add(monitoraggio);
			}

			// sezione caricamenti
			SezioneMenu caricamenti = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".operazioniMassive"));
			List<VoceMenu> vociMenuCaricamenti = new ArrayList<VoceMenu>();

			if(this.isServizioAbilitatoScrittura(bd, Servizio.Gestione_Pagamenti) || this.isOperatoreAdminServizio(bd, Servizio.Gestione_Pagamenti)){ 
				Tracciati caricamentoTracciatiDars = new Tracciati();
				URI caricamentoTracciatiURI = new URI(caricamentoTracciatiDars.getPathServizio());
				VoceMenu voceMenuCaricamentoTracciati = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(caricamentoTracciatiDars.getNomeServizio() + ".titolo"),	caricamentoTracciatiURI, VoceMenu.VOCE_OPERAZIONIMASSIVE);
				vociMenuCaricamenti.add(voceMenuCaricamentoTracciati);

				if(vociMenuCaricamenti.size() > 0){	
					caricamenti.getVociMenu().addAll(vociMenuCaricamenti);
					menu.getSezioni().add(caricamenti);
				}
			}

			// sezione statistiche
			SezioneMenu statistiche = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statistiche"));
			List<VoceMenu> vociMenuStatistiche = new ArrayList<VoceMenu>();

			if(this.checkDirittiServizioOperatore(bd, Servizio.Statistiche)){
				StatisticheTransazioni statTransazioniDars = new StatisticheTransazioni();
				URI statTransazioniURI = new URI(statTransazioniDars.getPathServizio());
				VoceMenu voceMenuStatTransazioni = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(statTransazioniDars.getNomeServizio() + ".titolo"),	statTransazioniURI, VoceMenu.VOCE_STATISTICA);
				vociMenuStatistiche.add(voceMenuStatTransazioni);

				if(vociMenuStatistiche.size() > 0){	
					statistiche.getVociMenu().addAll(vociMenuStatistiche);
					menu.getSezioni().add(statistiche);
				}
			}

			// sezione manutenzione
			SezioneMenu manutenzione = new SezioneMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".manutenzione"));
			List<VoceMenu> vociMenuManutenzione = new ArrayList<VoceMenu>();

			if(this.checkDirittiServizioOperatore(bd, Servizio.Manutenzione)){
				Strumenti strumentiDars = new Strumenti();
				URI strumentiURI = new URI(strumentiDars.getPathServizio());
				VoceMenu voceMenuStrumenti = new VoceMenu(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(strumentiDars.getNomeServizio() + ".titolo"),	strumentiURI, VoceMenu.VOCE_MANUTENZIONE);
				vociMenuManutenzione.add(voceMenuStrumenti);
			}
			if(vociMenuManutenzione.size() > 0){	
				manutenzione.getVociMenu().addAll(vociMenuManutenzione);
				menu.getSezioni().add(manutenzione);
			}

			console.setMenu(menu);

			// caricamento della sezione lingua
			console.setLingue(Utils.getInstance(this.getLanguage()).getMapLingue()); 

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
