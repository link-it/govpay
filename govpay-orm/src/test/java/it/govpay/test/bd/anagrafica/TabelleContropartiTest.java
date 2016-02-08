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

import it.govpay.bd.anagrafica.TabellaContropartiBD;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.TabellaControparti;
import it.govpay.test.BasicTest;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"dominio"})
public class TabelleContropartiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "tabellaControparti"})
	public void inserisciTabellaControparti() throws Exception {
		setupDominio();

		TabellaControparti tabellaControparti = new TabellaControparti();
		tabellaControparti.setIdDominio(dominioA.getId());
		tabellaControparti.setDataOraInizioValidita(new Date());
		tabellaControparti.setDataOraPubblicazione(new Date());
		tabellaControparti.setIdFlusso("idFlusso");
		tabellaControparti.setXml("<tabellaControparti />".getBytes());
		
		TabellaContropartiBD tabellaContropartiBD = new TabellaContropartiBD(bd);
		tabellaContropartiBD.insertTabellaControparti(tabellaControparti);
		
		Assert.assertTrue(tabellaControparti.getId() != null, "La tabella controparti inserita non ha l'Id valorizzato.");
		
		TabellaControparti tabellaContropartiLetta = tabellaContropartiBD.getTabellaControparti(tabellaControparti.getId());
		Assert.assertTrue(tabellaControparti.equals(tabellaContropartiLetta), "La tabella controparti letta da database con Id e' diversa da quella inserita: "+ BasicModel.diff(tabellaControparti,tabellaContropartiLetta));
		
	}
	
	@Test(groups = {"anagrafica", "tabellaControparti"}, dependsOnMethods = { "inserisciTabellaControparti" })
	public void aggiornaTabellaControparti() throws Exception {
		TabellaControparti tabellaControparti = new TabellaControparti();
		tabellaControparti.setIdDominio(dominioA.getId());
		tabellaControparti.setDataOraInizioValidita(new Date());
		tabellaControparti.setDataOraPubblicazione(new Date());
		tabellaControparti.setIdFlusso("idFlusso");
		tabellaControparti.setXml("<tabellaContropartiUpd />".getBytes());

		
		TabellaContropartiBD tabellaContropartiiBD = new TabellaContropartiBD(bd);
		tabellaContropartiiBD.updateTabellaControparti(tabellaControparti);
		
		Assert.assertTrue(tabellaControparti.getId() != null, "L'tabellaControparti aggiornato non ha l'Id valorizzato.");
		
		TabellaControparti tabellaContropartiLetto = tabellaContropartiiBD.getTabellaControparti(tabellaControparti.getId());
		Assert.assertTrue(tabellaControparti.equals(tabellaContropartiLetto), "L'tabellaControparti letto da database con Id e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "tabellaControparti"} , dependsOnMethods = { "aggiornaTabellaControparti" })
	public void cercaTabellaControparti() throws Exception {
		TabellaControparti tabellaControparti = new TabellaControparti();
		tabellaControparti.setIdDominio(dominioA.getId());
		tabellaControparti.setDataOraInizioValidita(new Date());
		tabellaControparti.setDataOraPubblicazione(new Date());
		tabellaControparti.setIdFlusso("idFlusso2");
		tabellaControparti.setXml("<tabellaControparti />".getBytes());
		
		TabellaContropartiBD tabellaContropartiBD = new TabellaContropartiBD(bd);
		tabellaContropartiBD.insertTabellaControparti(tabellaControparti);

		
		TabellaControparti tabellaLetta = tabellaContropartiBD.getLastTabellaControparti(tabellaControparti.getIdDominio());
		Assert.assertTrue(tabellaControparti.equals(tabellaLetta), "La tabellaControparti restituita dalla getLastTabellaControparti non corrisponde alla tabella controparti inserita");
	}
	
}
