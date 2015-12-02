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


import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.model.Stazione;
import it.govpay.test.BasicTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"intermediario"})
public class StazioniTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "stazione"})
	public void inserisciStazione() throws Exception {
		setupIntermediario();
		StazioniBD stazioniBD = new StazioniBD(bd);
		stazione = new Stazione();
		stazione.setCodStazione(intermediario.getCodIntermediario() + "_01");
		stazione.setIdIntermediario(intermediario.getId());
		stazione.setPassword("Password");
		stazione.setApplicationCode(APPLICATION_CODE);
		stazioniBD.insertStazione(stazione);
		
		Assert.assertTrue(stazione.getId() != null, "La stazione inserita non ha l'Id valorizzato.");
		
		Stazione stazioneLetto = stazioniBD.getStazione(stazione.getId());
		Assert.assertTrue(stazione.equals(stazioneLetto), "La stazione letta da database con Id e' diverso da quello inserito");
		
		stazioneLetto = stazioniBD.getStazione(stazione.getCodStazione());
		Assert.assertTrue(stazione.equals(stazioneLetto), "La stazione letta da database con CodStazione e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "stazione"}, dependsOnMethods = { "inserisciStazione" })
	public void aggiornaStazione() throws Exception {
		StazioniBD stazioniBD = new StazioniBD(bd);
		stazione = new Stazione();
		stazione.setCodStazione(intermediario.getCodIntermediario() + "_01");
		stazione.setIdIntermediario(intermediario.getId());
		stazione.setPassword("PasswordXXXXX");
		stazione.setApplicationCode(APPLICATION_CODE);
		stazioniBD.updateStazione(stazione);
		
		Assert.assertTrue(stazione.getId() != null, "La stazione aggiornata non ha l'Id valorizzato.");
		
		Stazione stazioneLetto = stazioniBD.getStazione(stazione.getId());
		Assert.assertTrue(stazione.equals(stazioneLetto), "La stazione letta da database con Id e' diverso da quello aggiornato");
		
		stazioneLetto = stazioniBD.getStazione(stazione.getCodStazione());
		Assert.assertTrue(stazione.equals(stazioneLetto), "La stazione letta da database con CodStazione e' diverso da quello aggiornato");
	}

	@Test(groups = {"anagrafica", "stazione"}, dependsOnMethods = { "inserisciStazione" })
	public void cercaStazione() throws Exception {
		StazioniBD stazioniBD = new StazioniBD(bd);
		Stazione stazione = new Stazione();
		stazione.setCodStazione("CodStazione2");
		stazione.setIdIntermediario(intermediario.getId());
		stazione.setPassword("PasswordXXXXX");
		stazione.setApplicationCode(APPLICATION_CODE);
		stazioniBD.insertStazione(stazione);
		
		List<Stazione> stazioni = stazioniBD.findAll(stazioniBD.newFilter());
		Assert.assertTrue(stazioni.contains(stazione), "La lista restituita non contiene la stazione precedentemente inserita");
	}
}
