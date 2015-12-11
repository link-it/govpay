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

import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OperatoriTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "operatore"}, dependsOnGroups = { "ente", "applicazione" })
	public void inserisciOperatore() throws Exception {
		
		setupEnte();
		setupApplicazioni();
		
		Operatore operatore = new Operatore();
		operatore.setAbilitato(true);
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap("Cap");
		anagrafica.setCellulare("Cellulare");
		anagrafica.setCivico("Civico");
		anagrafica.setCodUnivoco("CodUnivoco");
		anagrafica.setEmail("Email");
		anagrafica.setFax("Fax");
		anagrafica.setIndirizzo("Indirizzo");
		anagrafica.setLocalita("Località");
		anagrafica.setNazione("Nazione");
		anagrafica.setProvincia("Provincia");
		anagrafica.setRagioneSociale("RagioneSociale");
		anagrafica.setTelefono("Telefono");
		operatore.setNome("Nome Cognome");
		List<Long> enti = new ArrayList<Long>();
		enti.add(enteAA.getId());
		operatore.setIdEnti(enti);
		operatore.setPrincipal("Principal");
		operatore.setProfilo(ProfiloOperatore.ENTE);
		
		List<Long> idApplicazioni = new ArrayList<Long>();
		idApplicazioni.add(applicazioneAA.getId());
		operatore.setIdApplicazioni(idApplicazioni);
		OperatoriBD operatoriBD = new OperatoriBD(bd);
		operatoriBD.insertOperatore(operatore);
		
		Assert.assertTrue(operatore.getId() != null, "L'operatore inserito non ha l'Id valorizzato.");
		
		Operatore operatoreLetto = operatoriBD.getOperatore(operatore.getId());
		Assert.assertTrue(operatore.equals(operatoreLetto), "L'operatore letto da database con Id e' diverso da quello inserito");
		
		operatoreLetto = operatoriBD.getOperatore(operatore.getPrincipal());
		Assert.assertTrue(operatore.equals(operatoreLetto), "L'operatore letto da database con Principal e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "operatore"}, dependsOnGroups = { "ente" }, dependsOnMethods = { "inserisciOperatore" })
	public void aggiornaOperatore() throws Exception {
		Operatore operatore = new Operatore();
		operatore.setAbilitato(true);
		operatore.setProfilo(ProfiloOperatore.ADMIN);
		operatore.setPrincipal("Principal");

		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCodUnivoco("CodUnivoco");
		anagrafica.setCap("Cap");
		anagrafica.setCellulare("Cellulare");
		anagrafica.setCivico("Civico");
		anagrafica.setEmail("Email");
		anagrafica.setFax("Fax");
		anagrafica.setIndirizzo("Indirizzo");
		anagrafica.setLocalita("Località");
		anagrafica.setNazione("Nazione");
		anagrafica.setProvincia("Provincia");
		anagrafica.setRagioneSociale("RagioneSociale");
		anagrafica.setTelefono("Telefono");
		operatore.setNome("Nome Cognome");

		OperatoriBD operatoriBD = new OperatoriBD(bd);
		operatoriBD.updateOperatore(operatore);
		
		Assert.assertTrue(operatore.getId() != null, "L'operatore aggiornato non ha l'Id valorizzato.");
		
		Operatore operatoreLetto = operatoriBD.getOperatore(operatore.getId());
		Assert.assertTrue(operatore.equals(operatoreLetto), "L'operatore letto da database con Id e' diverso da quello aggiornato");
		
	}
	
	@Test(groups = {"anagrafica", "operatore"}, dependsOnGroups = { "ente" }, dependsOnMethods = { "aggiornaOperatore" })
	public void cercaOperatore() throws Exception {
		Operatore operatore = new Operatore();
		operatore.setAbilitato(true);
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCodUnivoco("CodUnivoco2");
		anagrafica.setCap("Cap");
		anagrafica.setCellulare("Cellulare");
		anagrafica.setCivico("Civico");
		anagrafica.setEmail("Email");
		anagrafica.setFax("Fax");
		anagrafica.setIndirizzo("Indirizzo");
		anagrafica.setLocalita("Località");
		anagrafica.setNazione("Nazione");
		anagrafica.setProvincia("Provincia");
		anagrafica.setRagioneSociale("RagioneSociale");
		anagrafica.setTelefono("Telefono");
		
		operatore.setNome("Nome Cognome");
		List<Long> enti = new ArrayList<Long>();
		enti.add(enteAB.getId());
		operatore.setIdEnti(enti);
		operatore.setPrincipal("Principal2");
		operatore.setProfilo(ProfiloOperatore.ENTE);
		
		OperatoriBD operatoriBD = new OperatoriBD(bd);
		operatoriBD.insertOperatore(operatore);

		List<Operatore> operatori = operatoriBD.getOperatori();
		
		Assert.assertTrue(operatori.size() == 2, "Attesi 2 operatori, trovati " + operatori.size() + ".");
		Assert.assertTrue(operatori.contains(operatore), "La lista restituita dalla findAll non contiene l'operatore precedentemente inserito");
	}
}
