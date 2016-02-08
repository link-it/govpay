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
package it.govpay.test.bd.rendicontazione;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Fr.StatoFr;
import it.govpay.bd.model.SingolaRendicontazione;
import it.govpay.bd.rendicontazione.FrBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"versamenti"})
public class FrTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"rendicontazioni", "fr"})
	public void inserisciFr() throws Exception {
		setupVersamenti();
		Fr fr = new Fr();
		fr.setAnnoRiferimento(2015);
		fr.setCodFlusso("codFlusso");
		fr.setDataOraFlusso(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015, 10, 1);
		fr.setDataRegolamento(calendar.getTime());
		fr.setDescrizioneStato("descrizioneStato");
		fr.setIdPsp(psp.getId());
		fr.setImportoTotalePagamenti(10);
		fr.setIur("iur");
		fr.setNumeroPagamenti(1);
		fr.setStato(StatoFr.ACQUISITA);
		fr.setIdDominio(dominioA.getId());
		
		List<SingolaRendicontazione> singolaRendicontazioneList = new ArrayList<SingolaRendicontazione>();
		SingolaRendicontazione singolaRendicontazione = new SingolaRendicontazione();
		singolaRendicontazione.setSingoloImporto(10.01);
		singolaRendicontazione.setDataEsito(calendar.getTime());
		singolaRendicontazione.setIur("iur");
		singolaRendicontazione.setIuv("iuv");
		singolaRendicontazione.setIdSingoloVersamento(versamentoAA1_1.getSingoliVersamenti().get(0).getId());
		singolaRendicontazioneList.add(singolaRendicontazione);
		fr.setSingolaRendicontazioneList(singolaRendicontazioneList);
		byte[] xml = "<xmlFr />".getBytes();
		FrBD frBD = new FrBD(bd);
		frBD.insertFr(fr, xml);
		
		Assert.assertTrue(fr.getId() != null, "L'fr inserito non ha l'id valorizzato");
		Assert.assertTrue(fr.getIdTracciatoXML() != null, "L'fr inserito non ha l'id tracciato valorizzato");
		
		Fr frLetto = frBD.getFr(fr.getId());
		Assert.assertTrue(fr.equals(frLetto), "L'fr letto tramite chiave fisica e' divfrso da quello inserito");
		
		Fr frLettoConChiaveLogica = frBD.getFr(fr.getAnnoRiferimento(),fr.getCodFlusso());
		Assert.assertTrue(fr.equals(frLettoConChiaveLogica), "L'fr letto tramite chiave logica e' divfrso da quello inserito");
	}
}
