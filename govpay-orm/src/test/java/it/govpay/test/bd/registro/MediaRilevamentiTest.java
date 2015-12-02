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

import it.govpay.bd.model.MediaRilevamento;
import it.govpay.bd.registro.MediaRilevamentoBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;
@Test(dependsOnGroups = {"rilevamenti"})
public class MediaRilevamentiTest extends BasicTest {
	
	@Test(groups = {"registro", "mediaRilevamenti"})
	public void inserisciMediaRilevamento() throws Exception {
		setupEventi();
		setupSla();
		MediaRilevamento mediaRilevamento = new MediaRilevamento();
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015,9,29);
		mediaRilevamento.setDataOsservazione(calendar.getTime());
		mediaRilevamento.setIdSLA(sla.getId());
		mediaRilevamento.setNumRilevamentiA(1);
		mediaRilevamento.setNumRilevamentiB(2);
		mediaRilevamento.setNumRilevamentiOver(3);
		mediaRilevamento.setPercentualeA(0.02);
		mediaRilevamento.setPercentualeB(0.03);
		MediaRilevamentoBD mediaMediaRilevamentiBD = new MediaRilevamentoBD(bd);
		mediaMediaRilevamentiBD.insertMediaRilevamento(mediaRilevamento);
		
		Assert.assertTrue(mediaRilevamento.getId() != null, "Il mediaRilevamento inserito non ha l'id valorizzato");
		
		MediaRilevamento mediaRilevamentoLetto = mediaMediaRilevamentiBD.getMediaRilevamento(mediaRilevamento.getId());
		Assert.assertTrue(mediaRilevamento.equals(mediaRilevamentoLetto), "Il mediaRilevamento letto e' diverso da quello inserito");
	}
}
