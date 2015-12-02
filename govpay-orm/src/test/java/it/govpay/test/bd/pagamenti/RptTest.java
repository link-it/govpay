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

import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FaultNodo;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "versamenti" })
public class RptTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"pagamenti", "rpt"})
	public void inserisciRpt() throws Exception {
		setupVersamenti();
		Rpt rpt = new Rpt();
		rpt.setAutenticazioneSoggetto(Autenticazione.N_A);
		rpt.setCallbackURL("http://www.govpay.it/callbackUrl");
		rpt.setCcp(Rpt.CCP_NA);
		rpt.setCodCarrello(null);
		rpt.setCodDominio(versamentoAA1_1.getCodDominio());
		rpt.setCodMsgRichiesta(UUID.randomUUID().toString().replaceAll("-", ""));
		rpt.setCodSessione(null);
		rpt.setDataOraMsgRichiesta(new Date());
		rpt.setDataOraCreazione(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setFaultCode(null);
		rpt.setFirmaRichiesta(FirmaRichiesta.NESSUNA);
		rpt.setIbanAddebito(null);;
		rpt.setIdPsp(psp.getId());
		rpt.setIdPortale(portale.getId());
		rpt.setIdStazione(stazione.getId());
		rpt.setIdVersamento(versamentoAA1_1.getId());
		rpt.setIuv(versamentoAA1_1.getIuv());
		rpt.setStatoRpt(StatoRpt.RPT_ATTIVATA);
		rpt.setDescrizioneStato("Descrizione stato");
		rpt.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
		
		byte[] rptByte = "<xmlRpt />".getBytes();
		
		RptBD rptBD = new RptBD(bd);
		rptBD.insertRpt(rpt, rptByte);
		
		Assert.assertTrue(rpt.getId() != null, "L'RPT inserita non ha l'id valorizzato");
		Assert.assertTrue(rpt.getIdTracciatoXML() != null, "L'RPT inserita non ha l'idTracciato valorizzato");
		
		Rpt rptLetta = rptBD.getRpt(rpt.getCodMsgRichiesta());
		Assert.assertTrue(rpt.equals(rptLetta), "L'RPT letta con CodMsgRichiesta e' diversa da quella inserita");
		
		rptLetta = rptBD.getRpt(rpt.getId());
		Assert.assertTrue(rpt.equals(rptLetta), "L'RPT letta con Id e' diversa da quella inserita");
		
		rptLetta = rptBD.getLastRpt(rpt.getIdVersamento());
		Assert.assertTrue(rpt.equals(rptLetta), "L'RPT letta con IdVersamento e' diversa da quella inserita");
		
		TracciatiBD tracciatiBD = new TracciatiBD(bd);
		byte[] tracciatoLetto = tracciatiBD.getTracciato(TipoTracciato.RPT, rpt.getCodMsgRichiesta());
		Assert.assertTrue("<xmlRpt />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database con CodMsgRichiesta e' diverso da quello inserito");
		
		tracciatoLetto = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		Assert.assertTrue("<xmlRpt />".equals(new String(tracciatoLetto)), "Il tracciatoXML letto da database con IdTracciato e' diverso da quello inserito");
	}
	
	@Test(groups = {"pagamenti", "rpt"}, dependsOnMethods = {"inserisciRpt"})
	public void aggiornaCodSessione() throws Exception {
		RptBD rptBD = new RptBD(bd);
		Rpt rpt = rptBD.getLastRpt(versamentoAA1_1.getId());
		rpt.setCodSessione("CodSessione");
		rptBD.updateRpt(rpt.getId(), rpt.getCodSessione(), null);
		Rpt rptLetta = rptBD.getRpt(rpt.getCodMsgRichiesta());
		Assert.assertTrue(rpt.getCodSessione().equals(rptLetta.getCodSessione()), "L'aggiornamento del CodSessione non e' avvenuto con successo.");
	}
	
	@Test(groups = {"pagamenti", "rpt"}, dependsOnMethods = {"inserisciRpt"})
	public void aggiornaPspRedirectUrl() throws Exception {
		RptBD rptBD = new RptBD(bd);
		Rpt rpt = rptBD.getLastRpt(versamentoAA1_1.getId());
		rpt.setPspRedirectURL("http://www.psp.it/back");
		rptBD.updateRpt(rpt.getId(), rpt.getCodSessione(), "http://www.psp.it/back");
		Rpt rptLetta = rptBD.getRpt(rpt.getCodMsgRichiesta());
		Assert.assertTrue(rpt.getCodSessione().equals(rptLetta.getCodSessione()), "L'aggiornamento del PspRedirectUrl non e' avvenuto con successo.");
	}
	
	@Test(groups = {"pagamenti", "rpt"}, dependsOnMethods = {"inserisciRpt"})
	public void aggiornaStatoRpt() throws Exception {
		RptBD rptBD = new RptBD(bd);
		Rpt rpt = rptBD.getLastRpt(versamentoAA1_1.getId());
		rpt.setStatoRpt(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
		rpt.setFaultCode(FaultNodo.PPT_CANALE_SCONOSCIUTO);
		rpt.setDescrizioneStato("DescrizioneStato");
		rptBD.updateStatoRpt(rpt.getId(), rpt.getStatoRpt(), rpt.getFaultCode(), rpt.getDescrizioneStato());
		Rpt rptLetta = rptBD.getRpt(rpt.getCodMsgRichiesta());
		Assert.assertTrue(rpt.getCodSessione().equals(rptLetta.getCodSessione()), "L'aggiornamento dello StatoRpt non e' avvenuto con successo.");
		
		rpt.setStatoRpt(StatoRpt.RPT_INVIATA_A_PSP);
		rpt.setFaultCode(null);
		rpt.setDescrizioneStato(null);
		rptBD.updateStatoRpt(rpt.getId(), rpt.getStatoRpt(), rpt.getFaultCode(), rpt.getDescrizioneStato());
		rptLetta = rptBD.getRpt(rpt.getCodMsgRichiesta());
		Assert.assertTrue(rpt.getCodSessione().equals(rptLetta.getCodSessione()), "L'aggiornamento dello StatoRpt non e' avvenuto con successo.");
	}
}
