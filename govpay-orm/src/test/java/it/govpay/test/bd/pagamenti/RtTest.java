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


import java.util.Date;
import java.util.UUID;

import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Rt.EsitoPagamento;
import it.govpay.bd.model.Rt.StatoRt;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "rpt" })
public class RtTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"pagamenti", "rt"})
	public void inserisciRpt() throws Exception {
		setupRpt();

		Rt rt = new Rt();
		rt.setCodMsgRicevuta(UUID.randomUUID().toString().replaceAll("-", ""));
		rt.setDataOraMsgRicevuta(new Date());
		rt.setDescrizioneStato("DescrizioneStato");
		rt.setEsitoPagamento(EsitoPagamento.PAGAMENTO_ESEGUITO);
		Anagrafica anagraficaAttestante = new Anagrafica();
		anagraficaAttestante.setRagioneSociale("RagioneSocialeAttestante");
		anagraficaAttestante.setCodUnivoco("CodUnivocoAttestante");
		rt.setAnagraficaAttestante(anagraficaAttestante);
		rt.setIdRpt(rptAA1_1.getId());
		rt.setStato(StatoRt.ACCETTATA);
		
		byte[] rtByte = "<xmlRt />".getBytes();
		
		RtBD rtBD = new RtBD(bd);
		rtBD.insertRt(rt, rtByte);
		
		Assert.assertTrue(rt.getId() != null, "L'RT inserita non ha l'id valorizzato");
		Assert.assertTrue(rt.getIdTracciatoXML() != null, "L'RT inserita non ha l'idTracciato valorizzato");
		
		Rt rtLetta = rtBD.getRt(rt.getCodMsgRicevuta());
		Assert.assertTrue(rt.equals(rtLetta), "L'RT letta con CodMsgRicevuta e' diversa da quella inserita");
		
		rtLetta = rtBD.getRt(rt.getId());
		Assert.assertTrue(rt.equals(rtLetta), "L'RT letta con Id e' diversa da quella inserita");
		
		rtLetta = rtBD.getLastRt(rt.getIdRpt());
		Assert.assertTrue(rt.equals(rtLetta), "L'RT letta con IdRpt e' diversa da quella inserita");
		
		TracciatiBD tracciatiBD = new TracciatiBD(bd);
		byte[] tracciatoLetto = tracciatiBD.getTracciato(TipoTracciato.RT, rt.getCodMsgRicevuta());
		Assert.assertTrue("<xmlRt />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database con CodMsgRicevuta e' diverso da quello inserito");
		
		tracciatoLetto = tracciatiBD.getTracciato(rt.getIdTracciatoXML());
		Assert.assertTrue("<xmlRt />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database con IdTracciato e' diverso da quello inserito");
		
	}
	
}
