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
package it.govpay.test.web.business;

import it.govpay.bd.anagrafica.ContiAccreditoBD;
import it.govpay.bd.anagrafica.TabellaContropartiBD;
import it.govpay.business.TabellaControparti;
import it.govpay.test.web.BasicTest;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "tabellaControparti"})
public class TabellaContropartiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"business", "tabellaControparti"})
	public void getTabellaControparti() throws Exception {
		setupDominio();
		TabellaControparti tab = new TabellaControparti(bd);
		it.govpay.bd.model.TabellaControparti tabellaControparti = tab.getInformativa(dominioA.getId(), new Date());
		
		TabellaContropartiBD tabellaContropartiBD = new TabellaContropartiBD(bd);
		it.govpay.bd.model.TabellaControparti tabellaContropartiLetta = tabellaContropartiBD.getTabellaControparti(tabellaControparti.getId());
		
		Assert.assertEquals(tabellaControparti, tabellaContropartiLetta, "La tabellaControparti restituita dal metodo getInformativa non e' la stessa memorizzata sul DB");
	}

	@Test(groups = {"business", "tabellaControparti"})
	public void getContiAccredito() throws Exception {
		setupDominio();
		TabellaControparti tab = new TabellaControparti(bd);
		it.govpay.bd.model.ContoAccredito contoAccredito = tab.getContoAccredito(dominioA.getId(), new Date());
		
		ContiAccreditoBD tabellaContropartiBD = new ContiAccreditoBD(bd);
		it.govpay.bd.model.ContoAccredito contoAccreditoLetto = tabellaContropartiBD.getContoAccredito(contoAccredito.getId());
		
		Assert.assertEquals(contoAccredito, contoAccreditoLetto, "Il contoAccredito restituito dal metodo getContoAccredito non e' lo stesso memorizzato sul DB");
	}


}
