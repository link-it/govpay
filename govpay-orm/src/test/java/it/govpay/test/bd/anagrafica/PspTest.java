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


import java.util.List;

import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PspTest extends BasicTest {

	@Test(groups = {"anagrafica", "psp"})
	public void inserisciPsp() throws Exception {
		PspBD pspBD = new PspBD(bd);
		
		Psp psp = new Psp();
		psp.setAttivo(true);
		psp.setBolloGestito(false);
		psp.setCodFlusso("CodFlusso");
		psp.setCodPsp("CodPsp");
		psp.setRagioneSociale("Prestatore Servizi Pagamento");
		psp.setStornoGestito(false);
		psp.setUrlInfo("http://www.govpay.it/psp");
		
		Canale canale = psp.new Canale();
		canale.setCodCanale("CodCanale");
		canale.setCodIntermediario("CodIntermediario");
		canale.setCondizioni("Condizioni");
		canale.setDescrizione("Descrizione");
		canale.setDisponibilita("Disponibilita");
		canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
		canale.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		canale.setUrlInfo("http://www.govpay.it/canale");
		canale.setPsp(psp);
		psp.getCanali().add(canale);
		
		Canale canale2 = psp.new Canale();
		canale2.setCodCanale("CodCanale2");
		canale2.setCodIntermediario("CodIntermediario");
		canale2.setCondizioni("Condizioni2");
		canale2.setDescrizione("Descrizione2");
		canale2.setDisponibilita("Disponibilita2");
		canale2.setModelloPagamento(ModelloPagamento.DIFFERITO);
		canale2.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);
		canale2.setUrlInfo("http://www.govpay.it/canale2/");
		canale2.setPsp(psp);
		psp.getCanali().add(canale2);
		
		pspBD.insertPsp(psp, "<xml />".getBytes());
		
		Assert.assertTrue(psp.getId() != null, "Il psp inserito non ha l'Id valorizzato.");
		for(Canale c : psp.getCanali()) {
			Assert.assertTrue(c.getId() != null, "Il canale inserito non ha l'Id valorizzato.");
		}
		
		Psp pspLetto = pspBD.getPsp(psp.getCodPsp());
		Assert.assertTrue(psp.equals(pspLetto), "Il psp letto da database con CodPsp e' diverso da quello inserito");
		
		pspLetto = pspBD.getPsp(psp.getId());
		Assert.assertTrue(psp.equals(pspLetto), "Il psp letto da database con Id e' diverso da quello inserito");
		
		TracciatiBD tracciatiBD = new TracciatiBD(bd);
		byte[] tracciatoLetto = tracciatiBD.getTracciato(TipoTracciato.PSP, psp.getCodFlusso());
		Assert.assertTrue("<xml />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database e' diverso da quello inserito");
	}
	
	
	@Test(groups = {"anagrafica", "psp"} , dependsOnMethods = { "inserisciPsp" })
	public void aggiornaPsp() throws Exception {
		PspBD pspBD = new PspBD(bd);
		
		Psp psp =  pspBD.getPsp("CodPsp");
		psp.getCanali().remove(0);
		
		psp.setCodFlusso("codFlussoNuovoPerUpdate");
		Canale canale = psp.new Canale();
		canale.setCodCanale("CodCanale");
		canale.setCodIntermediario("CodIntermediario");
		canale.setCondizioni("CondizioniUpd");
		canale.setDescrizione("DescrizioneUpd");
		canale.setDisponibilita("DisponibilitaUpd");
		canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
		canale.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		canale.setUrlInfo("http://www.govpay.it/canale");
		psp.getCanali().add(canale);
		
		pspBD.updatePsp(psp, "<xml_upd />".getBytes());
		
		Assert.assertTrue(canale.getId() != null, "Il canale aggionto nell'aggiornamento non e' valorizzato");
		
		Psp pspLetto = pspBD.getPsp(psp.getCodPsp());
		Assert.assertTrue(psp.equals(pspLetto), "Il psp letto da database con CodPsp e' diverso da quello inserito");
		
		pspLetto = pspBD.getPsp(psp.getId());
		Assert.assertTrue(psp.equals(pspLetto), "Il psp letto da database con Id e' diverso da quello inserito");
		
		TracciatiBD tracciatiBD = new TracciatiBD(bd);
		byte[] tracciatoLetto = tracciatiBD.getTracciato(TipoTracciato.PSP, psp.getCodFlusso());
		Assert.assertTrue("<xml_upd />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database e' diverso da quello aggiornato");
	}
	
	@Test(groups = {"anagrafica", "psp"} , dependsOnMethods = { "aggiornaPsp" })
	public void cercaPsp() throws Exception {
		PspBD pspBD = new PspBD(bd);
		
		Psp psp = new Psp();
		psp.setAttivo(false);
		psp.setBolloGestito(false);
		psp.setCodFlusso("CodFlusso3");
		psp.setCodPsp("CodPsp2");
		psp.setRagioneSociale("Prestatore Servizi Pagamento 2");
		psp.setStornoGestito(false);
		psp.setUrlInfo("http://www.govpay.it/psp2");
		
		Canale canale = psp.new Canale();
		canale.setCodCanale("CodCanale2");
		canale.setCodIntermediario("CodIntermediario2");
		canale.setCondizioni("Condizioni");
		canale.setDescrizione("Descrizione");
		canale.setDisponibilita("Disponibilita");
		canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
		canale.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		canale.setUrlInfo("http://www.govpay.it/canale2");
		psp.getCanali().add(canale);
		
		pspBD.insertPsp(psp, "<xml2 />".getBytes());
		
		psp.setCodFlusso("CodFlusso4");
		psp.setCodPsp("CodPsp3");
		psp.setId(null);
		canale.setId(null);
		
		pspBD.insertPsp(psp, "<xml3 />".getBytes());
		
		List<Psp> allPsp = pspBD.getPsp();
		Assert.assertTrue(allPsp.size()==3, "Attesi 3 psp, trovati " + allPsp.size() + " psp nella lista psp.");
		Assert.assertTrue(allPsp.contains(psp), "La lista restituita non contiene il psp precedentemente inserito");
		
		List<Psp> attiviPsp = pspBD.getPsp(true);
		Assert.assertTrue(attiviPsp.size()==1, "Attesi 1 psp, trovati " + attiviPsp.size() + " psp nella lista psp attivi.");
		
		List<Psp> disattiviPsp = pspBD.getPsp(false);
		Assert.assertTrue(disattiviPsp.size()==2, "Attesi 2 psp, trovati " + disattiviPsp.size() + " psp nella lista psp disattivi.");
	}
	
	@Test(groups = {"anagrafica", "psp"} , dependsOnMethods = { "cercaPsp" })
	public void cercaCanale() throws Exception {
		PspBD pspBD = new PspBD(bd);
		
		Psp psp = new Psp();
		psp.setAttivo(false);
		psp.setBolloGestito(false);
		psp.setCodFlusso("CodFlusso4");
		psp.setCodPsp("CodPsp4");
		psp.setRagioneSociale("Prestatore Servizi Pagamento 4");
		psp.setStornoGestito(false);
		psp.setUrlInfo("http://www.govpay.it/psp3");
		
		Canale canale = psp.new Canale();
		canale.setCodCanale("CodCanale4a");
		canale.setCodIntermediario("CodIntermediario4");
		canale.setCondizioni("Condizioni");
		canale.setDescrizione("Descrizione");
		canale.setDisponibilita("Disponibilita");
		canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
		canale.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		canale.setUrlInfo("http://www.govpay.it/canale4a");
		canale.setPsp(psp);
		psp.getCanali().add(canale);
		
		canale = psp.new Canale();
		canale.setCodCanale("CodCanale4b");
		canale.setCodIntermediario("CodIntermediario4");
		canale.setCondizioni("Condizioni");
		canale.setDescrizione("Descrizione");
		canale.setDisponibilita("Disponibilita");
		canale.setModelloPagamento(ModelloPagamento.IMMEDIATO);
		canale.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		canale.setUrlInfo("http://www.govpay.it/canale4b");
		canale.setPsp(psp);
		psp.getCanali().add(canale);
		
		pspBD.insertPsp(psp, "<xml4 />".getBytes());
		
		Canale canaleLetto = pspBD.getCanale(canale.getId());
		
		Assert.assertTrue(canale.equals(canaleLetto), "Il canale inserito e quello cercato tramite Id non sono uguali:"+ BasicModel.diff(canale, canaleLetto));
	}
}
