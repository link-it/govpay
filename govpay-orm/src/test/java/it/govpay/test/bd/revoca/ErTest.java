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
package it.govpay.test.bd.revoca;

import java.util.Date;

import it.govpay.bd.model.Er;
import it.govpay.bd.model.Er.Stato;
import it.govpay.bd.revoca.ErBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"rr"})
public class ErTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"revoche", "er"})
	public void inserisciEr() throws Exception {
		setupRr();
		Er er = new Er();
		er.setCodMsgEsito("codMsgEsito");
		er.setDataOraCreazione(new Date());
		er.setDataOraMsgEsito(new Date());
		er.setImportoTotaleRevocato(10);
		er.setStato(Stato.ER_ACCETTATA);
		er.setIdRr(rr.getId());
		byte[] xml = "<xmlEr />".getBytes();
		ErBD erBD = new ErBD(bd);
		erBD.insertEr(er, xml);
		
		Assert.assertTrue(er.getId() != null, "L'er inserito non ha l'id valorizzato");
		Assert.assertTrue(er.getIdTracciatoXML() != null, "L'er inserito non ha l'id tracciato valorizzato");
		
		Er erLetto = erBD.getEr(er.getId());
		Assert.assertTrue(er.equals(erLetto), "L'er letto tramite chiave fisica e' diverso da quello inserito");
		
		Er erLettoConChiaveLogica = erBD.getEr(er.getCodMsgEsito());
		Assert.assertTrue(er.equals(erLettoConChiaveLogica), "L'er letto tramite chiave logica e' diverso da quello inserito");
	}
}
