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

import java.util.Calendar;

import it.govpay.bd.model.Rilevamento;
import it.govpay.bd.registro.RilevamentiBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;
@Test(dependsOnGroups = {"eventi", "sla"})
public class RilevamentiTest extends BasicTest {
	
	@Test(groups = {"registro", "rilevamenti"})
	public void inserisciRilevamento() throws Exception {
		setupEventi();
		setupSla();
		Rilevamento rilevamento = new Rilevamento();
		rilevamento.setIdSLA(sla.getId());
		rilevamento.setIdEventoIniziale(eventoIniziale.getId());
		rilevamento.setIdEventoFinale(eventoFinale.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015,9,29);
		rilevamento.setDataRilevamento(calendar.getTime());
		rilevamento.setDurata(8);
		RilevamentiBD rilevamentiBD = new RilevamentiBD(bd);
		rilevamentiBD.insertRilevamento(rilevamento);
		
		Assert.assertTrue(rilevamento.getId() != null, "Il rilevamento inserito non ha l'id valorizzato");

		Rilevamento rilevamentoLetto = rilevamentiBD.getRilevamento(rilevamento.getId());
		Assert.assertTrue(rilevamento.equals(rilevamentoLetto), "Il rilevamento letto e' diverso da quello inserito");
	}

	@Test(groups = {"registro", "rilevamenti"}, dependsOnMethods = { "inserisciRilevamento" })
	public void getMedia() throws Exception {
		Rilevamento rilevamento = new Rilevamento();
		rilevamento.setIdSLA(sla.getId());
		rilevamento.setIdApplicazione(1l);
		rilevamento.setIdEventoIniziale(eventoIniziale.getId());
		rilevamento.setIdEventoFinale(eventoFinale.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015,8,15);
		rilevamento.setDataRilevamento(calendar.getTime());
		rilevamento.setDurata(11);
		RilevamentiBD rilevamentiBD = new RilevamentiBD(bd);
		rilevamentiBD.insertRilevamento(rilevamento);

		Calendar calendarAgosto = Calendar.getInstance();
		calendarAgosto.clear();
		calendarAgosto.set(2015,8,1);

		double media = rilevamentiBD.getMedia(rilevamento.getIdSLA(), null, calendarAgosto.getTime(), null);
		double mediaAttesa = (8+11)/2.0;
		Assert.assertEquals(media, mediaAttesa, "La media dei rilevamenti con id Sla e Data Inizio e' errata");

		double mediaPerApplicazione = rilevamentiBD.getMedia(rilevamento.getIdSLA(), rilevamento.getIdApplicazione(), calendarAgosto.getTime(), null);
		double mediaAttesaPerApplicazione = 11;
		Assert.assertEquals(mediaPerApplicazione, mediaAttesaPerApplicazione, "La media dei rilevamenti con id Sla, id Applicazione e Data Inizio e' errata");
	}

	@Test(groups = {"registro", "rilevamenti"}, dependsOnMethods = { "getMedia" })
	public void countOverSoglia() throws Exception {

		Rilevamento rilevamento = new Rilevamento();
		rilevamento.setIdSLA(sla.getId());
		rilevamento.setIdApplicazione(1l);
		rilevamento.setIdEventoIniziale(eventoIniziale.getId());
		rilevamento.setIdEventoFinale(eventoFinale.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015,10,15);
		rilevamento.setDataRilevamento(calendar.getTime());
		rilevamento.setDurata(1);
		RilevamentiBD rilevamentiBD = new RilevamentiBD(bd);
		rilevamentiBD.insertRilevamento(rilevamento);

		Calendar calendarSettembre = Calendar.getInstance();
		calendarSettembre.clear();
		calendarSettembre.set(2015,9,1);

		Calendar calendarOttobre = Calendar.getInstance();
		calendarOttobre.clear();
		calendarOttobre.set(2015,10,1);


		long overSoglia = rilevamentiBD.countOverSoglia(rilevamento.getIdSLA(), 1, calendarSettembre.getTime(), null);
		long overSogliaAttesa = 2;
		Assert.assertEquals(overSoglia, overSogliaAttesa, "Il totale dei rilevamenti con limite 1 e Data Inizio e' errata");

		long overSoglia2 = rilevamentiBD.countOverSoglia(rilevamento.getIdSLA(), 1l, calendarSettembre.getTime(), calendarOttobre.getTime());
		long overSogliaAttesa2 = 1;
		Assert.assertEquals(overSoglia2, overSogliaAttesa2, "Il totale dei rilevamenti con limite 1, Data Inizio e Data Fine e' errata");

	}
}
