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
package it.govpay.test.bd.anagrafica;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Applicazione.Versione;
import it.govpay.bd.model.Connettore.EnumAuthType;
import it.govpay.bd.model.Connettore.EnumSslType;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@Test(dependsOnGroups = {"stazione", "tributo" })
public class ApplicazioniTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "applicazione"} )
	public void inserisciApplicazione() throws Exception {
		setupTributi();
		setupStazione();
		Applicazione applicazione = new Applicazione();
		applicazione.setAbilitato(true);
		applicazione.setCodApplicazione("CodApplicazione");
		applicazione.setIdStazione(stazione.getId());
		applicazione.setVersione(Versione.GPv1);
		applicazione.setPolicyRispedizione("it.govpay.test.Policy");
		Connettore connettoreEsito = new Connettore();
		connettoreEsito.setAzioneInUrl(false);
		connettoreEsito.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettoreEsito.setHttpPassw("HttpPassw");
		connettoreEsito.setHttpUser("HttpUser");
		
		applicazione.setConnettoreEsito(connettoreEsito);
		
		Connettore connettoreVerifica = new Connettore();
		connettoreVerifica.setAzioneInUrl(true);
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.SSL);
		connettoreVerifica.setTipoSsl(EnumSslType.SERVER);
		connettoreVerifica.setSslTsLocation("sslTsLocation");
		connettoreVerifica.setSslTsPasswd("sslTsPasswd");
		connettoreVerifica.setSslTsType("sslTsType");
		
		applicazione.setConnettoreVerifica(connettoreVerifica);
		
		List<Long> idTributi = new ArrayList<Long>();
		idTributi.add(tributoAA1.getId());
		
		applicazione.setIdTributi(idTributi);
		applicazione.setPrincipal("Principal");
		
		
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		applicazioniBD.insertApplicazione(applicazione);
		
		Assert.assertTrue(applicazione.getId() != null, "L'applicazione inserita non ha l'Id valorizzato.");
		
		Applicazione applicazioneLetto = applicazioniBD.getApplicazione(applicazione.getCodApplicazione());
		Assert.assertTrue(applicazione.equals(applicazioneLetto), "L'applicazione letto da database con CodApplicazione e' diverso da quello inserito");
		
		applicazioneLetto = applicazioniBD.getApplicazione(applicazione.getId());
		Assert.assertTrue(applicazione.equals(applicazioneLetto), "L'applicazione letto da database con Id e' diverso da quello inserito");
		
		applicazioneLetto = applicazioniBD.getApplicazioneByPrincipal(applicazione.getPrincipal());
		Assert.assertTrue(applicazione.equals(applicazioneLetto), "L'applicazione letto da database con Principal e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "applicazione"}, dependsOnMethods = { "inserisciApplicazione" })
	public void aggiornaApplicazione() throws Exception {
		Applicazione applicazione = new Applicazione();
		applicazione.setAbilitato(true);
		applicazione.setCodApplicazione("CodApplicazione");
		applicazione.setIdStazione(stazione.getId());
		applicazione.setVersione(Versione.GPv2);
		Connettore connettoreEsito = new Connettore();
		connettoreEsito.setAzioneInUrl(true);
		connettoreEsito.setTipoAutenticazione(EnumAuthType.SSL);
		connettoreEsito.setTipoSsl(EnumSslType.SERVER);
		connettoreEsito.setSslTsLocation("sslTsLocation");
		connettoreEsito.setSslTsPasswd("sslTsPasswd");
		connettoreEsito.setSslTsType("sslTsType");
		
		applicazione.setConnettoreEsito(connettoreEsito);
		
		Connettore connettoreVerifica = new Connettore();
		connettoreVerifica.setAzioneInUrl(false);
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettoreVerifica.setHttpPassw("HttpPassw");
		connettoreVerifica.setHttpUser("HttpUser");
		
		applicazione.setConnettoreVerifica(connettoreVerifica);
		
		List<Long> idTributi = new ArrayList<Long>();
		idTributi.add(tributoAA2.getId());
		
		applicazione.setIdTributi(idTributi);
		applicazione.setPrincipal("Principal");
		
		
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		applicazioniBD.updateApplicazione(applicazione);
		
		Assert.assertTrue(applicazione.getId() != null, "L'applicazione inserita non ha l'Id valorizzato.");
		
		Applicazione applicazioneLetto = applicazioniBD.getApplicazione(applicazione.getCodApplicazione());
		Assert.assertTrue(applicazione.equals(applicazioneLetto), "L'applicazione letto da database con CodApplicazione e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "applicazione"}, dependsOnMethods = { "aggiornaApplicazione" })
	public void cercaTributi() throws Exception {
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		Applicazione applicazione = applicazioniBD.getApplicazione("CodApplicazione");
		
		TributiBD tributiBD = new TributiBD(bd);
		List<Tributo> tributi = tributiBD.getTributi(applicazione.getId());
		
		Assert.assertTrue(tributi.size() == applicazione.getIdTributi().size(), "Trovati " + tributi.size() + " tributi associati all'applicazione. Attesi " + applicazione.getIdTributi().size() + " tributi.");
		
		for(Tributo t : tributi) {
			Assert.assertTrue(applicazione.getIdTributi().contains(t.getId()), "Il tributo " + t.getCodTributo() + " non risulta nella lista dei tributi associati all'applicazione.");
		}
	}
	
	@Test(groups = {"anagrafica", "applicazione"}, dependsOnMethods = { "inserisciApplicazione" })
	public void findAllApplicazioni() throws Exception {

		Applicazione applicazione = new Applicazione();
		applicazione.setAbilitato(true);
		applicazione.setCodApplicazione("CodApplicazione2");
		applicazione.setIdStazione(stazione.getId());
		applicazione.setVersione(Versione.GPv1);
		Connettore connettoreEsito = new Connettore();
		connettoreEsito.setAzioneInUrl(false);
		connettoreEsito.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettoreEsito.setHttpPassw("HttpPassw");
		connettoreEsito.setHttpUser("HttpUser");
		
		applicazione.setConnettoreEsito(connettoreEsito);
		
		Connettore connettoreVerifica = new Connettore();
		connettoreVerifica.setAzioneInUrl(true);
		connettoreVerifica.setTipoAutenticazione(EnumAuthType.SSL);
		connettoreVerifica.setTipoSsl(EnumSslType.SERVER);
		connettoreVerifica.setSslTsLocation("sslTsLocation");
		connettoreVerifica.setSslTsPasswd("sslTsPasswd");
		connettoreVerifica.setSslTsType("sslTsType");
		
		applicazione.setConnettoreVerifica(connettoreVerifica);
		
		applicazione.setPrincipal("Principal2");
		
		
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		applicazioniBD.insertApplicazione(applicazione);

		List<Applicazione> lst = applicazioniBD.findAll(applicazioniBD.newFilter());
		
		Assert.assertTrue(lst.contains(applicazione), "La findAll non restituisce tra le applicazioni quella precedentemente inserita");
	}
	

	
}
