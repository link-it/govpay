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

import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingolaRevoca;
import it.govpay.bd.model.SingolaRevoca.Stato;
import it.govpay.bd.revoca.RrBD;
import it.govpay.test.BasicTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"pagamenti"})
public class RrTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"revoche", "rr"})
	public void inserisciRr() throws Exception {
		setupRt();
		Rr rr = new Rr();
		rr.setCodMsgRevoca("codMsgRevoca");
		rr.setDataOraCreazione(new Date());
		rr.setDataOraMsgRevoca(new Date());
		rr.setDescrizioneStato("descrizioneStato");
		rr.setIdRt(rt.getId());
		rr.setImportoTotaleRevocato(2000.01);
		rr.setStato(it.govpay.bd.model.Rr.Stato.RR_DA_INVIARE_A_NODO);
		List<SingolaRevoca> singolaRevocaList = new ArrayList<SingolaRevoca>();
		SingolaRevoca singolaRevoca = new SingolaRevoca();
		singolaRevoca.setCausaleRevoca("causaleRevoca");
		singolaRevoca.setDatiAggiuntiviEsito("datiAggiuntiviEsito");
		singolaRevoca.setDatiAggiuntiviRevoca("datiAggiuntiviRevoca");
		singolaRevoca.setDescrizioneStato("descrizioneStato");
		singolaRevoca.setSingoloImporto(10.01);
		singolaRevoca.setIdSingoloVersamento(versamentoAA1_1.getSingoliVersamenti().get(0).getId());
		singolaRevoca.setIdEr(1l);
		singolaRevoca.setStato(Stato.SINGOLA_REVOCA_REVOCATA);
		singolaRevocaList.add(singolaRevoca);
		rr.setSingolaRevocaList(singolaRevocaList);
		byte[] xml = "<xmlRR />".getBytes();
		RrBD rrBD = new RrBD(bd);
		rrBD.insertRr(rr, xml);
		
		Assert.assertTrue(rr.getId() != null, "L'rr inserito non ha l'id valorizzato");
		Assert.assertTrue(rr.getIdTracciatoXML() != null, "L'rr inserito non ha l'id tracciato valorizzato");
		
		Rr rrLetto = rrBD.getRr(rr.getId());
		Assert.assertTrue(rr.equals(rrLetto), "L'rr letto tramite chiave fisica e' diverso da quello inserito:"+BasicModel.diff(rr, rrLetto));
		
		Rr rrLettoConChiaveLogica = rrBD.getRr(rr.getCodMsgRevoca());
		Assert.assertTrue(rr.equals(rrLettoConChiaveLogica), "L'rr letto tramite chiave logica e' diverso da quello inserito"+BasicModel.diff(rr, rrLettoConChiaveLogica));
	}
}
