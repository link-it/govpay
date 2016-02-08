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
package it.govpay.test.bd.registro;

import it.govpay.bd.model.Sla;
import it.govpay.bd.registro.SlaBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SlaTest extends BasicTest {
	
	@Test(groups = {"registro", "sla"})
	public void inserisciSla() throws Exception {
		Sla sla = new Sla();
		sla.setDescrizione("Descrizione");

		sla.setTipoEventoIniziale("TipoEventoIniziale");
		sla.setSottotipoEventoIniziale("sottotipoEventoIniziale");
		
		sla.setTipoEventoFinale("TipoEventoFinale");
		sla.setSottotipoEventoFinale("sottotipoEventoFinale");
		
		sla.setTempoA(10l);
		sla.setTempoB(30l);
		sla.setTolleranzaA(0.1d);
		sla.setTolleranzaB(0.3d);
		SlaBD slaBD = new SlaBD(bd);
		slaBD.insertSLA(sla);
		
		Assert.assertTrue(sla.getId() != null, "Lo SLA inserito non ha l'id valorizzato");
		
		Sla slaLetto = slaBD.getSLA(sla.getId());
		Assert.assertTrue(sla.equals(slaLetto), "Lo SLA letto e' diverso da quello inserito");
	}
}
