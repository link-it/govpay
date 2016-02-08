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
package it.govpay.test.bd.pagamenti;

import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Esito.StatoSpedizione;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.test.BasicTest;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "applicazione" })
public class EsitiTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"pagamenti", "esiti"})
	public void inserisciEsito() throws Exception {
		setupApplicazioni();
		Esito esito = new Esito();
		
		esito.setIuv("iuv");
		esito.setCodDominio("CodDominio");
		esito.setDataOraCreazione(new Date());
		esito.setDataOraProssimaSpedizione(new Date());
		esito.setIdApplicazione(applicazioneAA.getId());
		esito.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		esito.setXml("<esito />".getBytes());
		
		EsitiBD esitoBD = new EsitiBD(bd);
		esitoBD.insertEsito(esito);
		
		Assert.assertTrue(esito.getId() != null, "L'Esito inserito non ha l'id valorizzato");
		
		Esito esitoLetto = esitoBD.getEsito(esito.getId());
		Assert.assertTrue(esito.equals(esitoLetto), "L'Esito letto con Id e' diversa da quello inserito");
		
	}
}
