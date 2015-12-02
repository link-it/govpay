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
package it.govpay.test.web.business;

import java.math.BigDecimal;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.test.web.BasicTest;
import it.govpay.test.web.utils.RTBuilder;
import it.govpay.test.web.utils.RTBuilder.TipoRicevuta;
import it.govpay.utils.JaxbUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "pagamentoErrore"}, dependsOnGroups = {"pagamento"})
public class PagamentiErroreTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	public void eseguiPagamentoEnte_ImportoErrato() throws Exception {
		setupRegistro();
		
		
		// Inserisco e pago un Versamento
		
		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");
		
		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

		Versamento versamento = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivocoDebitore");
		anagraficaDebitore.setRagioneSociale("RagioneSocialeDebitore");
		versamento.setAnagraficaDebitore(anagraficaDebitore);
		versamento.setCodDominio(dominioA.getCodDominio());
		versamento.setDataScadenza(sdf.parse("31/12/2020"));
		versamento.setIdApplicazione(applicazioneAA.getId());
		versamento.setIdEnte(enteAA.getId());
		versamento.setImportoTotale(new BigDecimal(100));
		versamento.setIuv(iuv);
		versamento.setCodVersamentoEnte(iuv);
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita() + "/" + tributoAA2.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		versamenti.caricaPagamento(versamento);

		Pagamenti pagamenti = new Pagamenti(bd);
		String burl = "http://www.govpay.it/back/pagamentoEnte";
		String pspUrl = pagamenti.eseguiPagamentoEnte(portale.getId(), versamento.getId(), psp.getCanale(TipoVersamento.CARTA_PAGAMENTO, ModelloPagamento.IMMEDIATO).getId(), null, FirmaRichiesta.NESSUNA, Autenticazione.N_A, null, burl);
		
		// Verifico che sia tornata una URL
	
		Assert.assertTrue(pspUrl != null, "Era attesa una url di ritorno");

		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.AUTORIZZATO_IMMEDIATO), "Stato versamento non corretto. Atteso: INOLTRATO; Trovato " + versamentoLetto.getStato());

		RptBD rptBD = new RptBD(bd);
		Rpt rpt = rptBD.getLastRpt(versamentoLetto.getId());

		// Acquisizione di ricevuta "Eseguito"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.ESEGUITO_ImportoTotalePagatoErrato);
		try {
			pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));
		} catch (GovPayException gpe) {
			Assert.assertTrue(gpe.getTipoException().equals(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP), "Tipo eccezione errato. Atteso ERRORE_VALIDAZIONE_NDP, trovato " + gpe.getTipoException());
			
			rpt = rptBD.getLastRpt(versamentoLetto.getId());
			Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_RIFIUTATA_PA), "Stato dell'RPT diverso da RT_RIFIUTATA_PA");
			Assert.assertTrue(rpt.getDescrizioneStato().contains("non corrisponde"), "Descrizione stato RPT non atteso: " + rpt.getDescrizioneStato());
			
			versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
			Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.AUTORIZZATO_IMMEDIATO), "Stato versamento non corretto. Atteso: INOLTRATO; Trovato " + versamentoLetto.getStato());
			Assert.assertTrue(versamentoLetto.getImportoPagato() == null || versamentoLetto.getImportoPagato().equals(BigDecimal.ZERO), "Importo totale diverso dall'importo pagato");
			for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
				Assert.assertTrue(sv.getDataEsitoSingoloPagamento() == null, "Data esito singolo pagamento impostata");
				Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE), "Stato singolo versamento non corretto. Atteso DA_PAGARE; Trovato " + sv.getStatoSingoloVersamento());
				Assert.assertTrue(sv.getIur() == null, "IUR impostato");
				Assert.assertTrue(sv.getSingoloImportoPagato() == null || sv.getSingoloImportoPagato().equals(BigDecimal.ZERO), "Importo singolo pagamento diverso da zero.");
			}
			return;
		}
		
		Assert.fail("Attesa eccezione per importo errato nella ricevuta telematica");
	}
}
