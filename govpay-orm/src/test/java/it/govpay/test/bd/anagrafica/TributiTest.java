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

import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Tributo.TipoContabilta;
import it.govpay.test.BasicTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"ente", "ibanAccredito"})
public class TributiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "tributo"})
	public void inserisciTributo() throws Exception {
		setupEnte();
		setupIbanAccredito();
		Tributo tributo = new Tributo();
		tributo.setAbilitato(true);
		tributo.setCodContabilita("CodContabilita");
		tributo.setIdEnte(enteAA.getId());
		tributo.setCodTributo("CodTributo");
		tributo.setDescrizione("Descrizione");
		tributo.setIbanAccredito(ibanAccredito.getId());
		tributo.setTipoContabilita(TipoContabilta.ALTRO);

		TributiBD tributiBD = new TributiBD(bd);
		tributiBD.insertTributo(tributo);
		
		Assert.assertTrue(tributo.getId() != null, "Il tributo inserito non ha l'Id valorizzato.");
		
		Tributo tributoLetto = tributiBD.getTributo(tributo.getIdEnte(), tributo.getCodTributo());
		Assert.assertTrue(tributo.equals(tributoLetto), "Il tributo letto da database con IdEnte e CodTributo e' diverso da quello inserito");
		
		tributoLetto = tributiBD.getTributo(tributo.getId());
		Assert.assertTrue(tributo.equals(tributoLetto), "Il tributo letto da database con Id e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "tributo"}, dependsOnMethods = { "inserisciTributo" })
	public void aggiornaTributo() throws Exception {
		
		Tributo tributo = new Tributo();
		tributo.setAbilitato(true);
		tributo.setCodContabilita("CodContabilita");
		tributo.setIdEnte(enteAA.getId());
		tributo.setCodTributo("CodTributo");
		tributo.setDescrizione("Descrizione");
		tributo.setIbanAccredito(ibanAccredito.getId());
		tributo.setTipoContabilita(TipoContabilta.SIOPE);
		
		TributiBD tributiBD = new TributiBD(bd);
		tributiBD.updateTributo(tributo);
		
		Assert.assertTrue(tributo.getId() != null, "Il tributo aggiornato non ha l'Id valorizzato.");
		
		Tributo tributoLetto = tributiBD.getTributo(tributo.getId());
		Assert.assertTrue(tributo.equals(tributoLetto), "Il tributo letto da database con Id e' diverso da quello inserito");
	}

	@Test(groups = {"anagrafica", "tributo"}, dependsOnMethods = { "inserisciTributo" })
	public void listaTributo() throws Exception {
		setupEnte();
		
		Tributo tributo = new Tributo();
		tributo.setAbilitato(true);
		tributo.setCodContabilita("CodContabilita");
		tributo.setIdEnte(enteAA.getId());
		tributo.setCodTributo("CodTributo2");
		tributo.setDescrizione("Descrizione");
		tributo.setIbanAccredito(ibanAccredito.getId());
		tributo.setTipoContabilita(TipoContabilta.ALTRO);

		TributiBD tributiBD = new TributiBD(bd);
		tributiBD.insertTributo(tributo);

		List<Tributo> tributi = tributiBD.findAll(tributiBD.newFilter());
		
		Assert.assertTrue(tributi.contains(tributo), "La lista restituita non contiene il tributo precedementemente inserito");
	}
	
	@Test(groups = {"anagrafica", "tributo"}, dependsOnMethods = { "inserisciTributo" })
	public void trovaIbanAccreditoPerDominio() throws Exception {

		DominiBD dominiBD = new DominiBD(bd);
		List<IbanAccredito> lst = dominiBD.getIbanAccreditoByIdDominio(dominioA.getId());
		Assert.assertEquals(lst.size(), 1, "Il numero di ibanAccredito letti con codice Dominio ["+dominioA.getId()+"]non e' quello atteso");

	}

	
}
