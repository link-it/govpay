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
import java.util.ArrayList;
import java.util.List;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.business.Pagamenti;
import it.govpay.test.web.BasicTest;
import it.govpay.test.web.utils.RTBuilder;
import it.govpay.test.web.utils.RTBuilder.TipoRicevuta;
import it.govpay.utils.JaxbUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "pagamento"}, dependsOnGroups = {"psp","versamento"})
public class PagamentiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}

	public void eseguiPagamentoEnte_Eseguito() throws Exception {
		setupRegistro();


		// Inserisco e pago un Versamento

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");

		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(),dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

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

		versamento.setDataScadenza(sdf.parse("31/12/2010"));
		versamento.setImportoTotale(versamento.getImportoTotale().add(BigDecimal.TEN));
		versamento.getSingoliVersamenti().get(0).setImportoSingoloVersamento(versamento.getSingoliVersamenti().get(0).getImportoSingoloVersamento().add(BigDecimal.TEN));
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

		Assert.assertTrue(rpt != null, "Non e' stata trovata la RPT del pagamento effettuato");
		Assert.assertTrue(rpt.getAnagraficaVersante() == null, "AnagraficaVersante dell'RPT e' valorizzata.");
		Assert.assertTrue(rpt.getAutenticazioneSoggetto().equals(Autenticazione.N_A), "AutenticazioneSoggetto dell'RPT differisce da quella della richiesta: " + rpt.getAutenticazioneSoggetto());
		Assert.assertTrue(rpt.getCallbackURL().equals(burl), "CallbackURL dell'RPT differisce da quella della richiesta: " + rpt.getCallbackURL());
		Assert.assertTrue(rpt.getCcp().equals(Rpt.CCP_NA), "Ccp dell'RPT fuori specifica: " + rpt.getCcp());
		Assert.assertTrue(rpt.getCodCarrello() == null, "CodCarrello valorizzato: " + rpt.getCodCarrello());
		Assert.assertTrue(rpt.getCodMsgRichiesta() != null, "CodMsgRichiesta non valorizzato");
		Assert.assertTrue(rpt.getCodSessione() != null, "CodSessione non valorizzato");
		Assert.assertTrue(rpt.getDataOraCreazione() != null, "DataOraCreazione non valorizzata");
		Assert.assertTrue(rpt.getDataOraMsgRichiesta() != null, "DataOraMsgRichiesta non valorizzata");
		Assert.assertTrue(rpt.getDescrizioneStato() == null, "Descrizione stato valorizzata: " + rpt.getDescrizioneStato());
		Assert.assertTrue(rpt.getFaultCode() == null, "FaultCode valorizzato: " + rpt.getFaultCode());
		Assert.assertTrue(rpt.getFirmaRichiesta().equals(FirmaRichiesta.NESSUNA), "FirmaRichiesta dell'RPT differisce da quella della richiesta");
		Assert.assertTrue(rpt.getIbanAddebito() == null, "IbanAddebito dell'RPT differisce da quella della richiesta");
		Assert.assertEquals(rpt.getIdPortale(), portale.getId(), "Portale dell'RPT differisce da quella della richiesta");
		Assert.assertTrue(rpt.getIdPsp() == psp.getId(), "Psp dell'RPT differisce da quella della richiesta");
		Assert.assertTrue(rpt.getIdTracciatoXML() != null, "TracciatoXML dell'RPT non riferito");
		Assert.assertTrue(rpt.getIdVersamento() == versamentoLetto.getId(), "Versamento dell'RPT diverso da quello pagato");
		Assert.assertTrue(!rpt.getStatoRpt().equals(StatoRpt.RPT_ATTIVATA), "Stato dell'RPT ancora in RPT_DA_INVIARE_A_NODO");
		Assert.assertTrue(rpt.getTipoVersamento().equals(TipoVersamento.CARTA_PAGAMENTO), "TipoVersamento dell'RPT differisce da quella della richiesta");

		// Acquisizione di ricevuta "Eseguito"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.ESEGUITO);
		pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));

		rpt = rptBD.getLastRpt(versamentoLetto.getId());
		Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.PAGAMENTO_ESEGUITO), "Stato versamento non corretto. Atteso: PAGAMENTO_ESEGUITO; Trovato " + versamentoLetto.getStato());
		Assert.assertTrue(versamentoLetto.getImportoPagato().equals(versamentoLetto.getImportoTotale()), "Importo totale diverso dall'importo pagato");
		for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
			Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
			Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.PAGATO), "Stato singolo versamento non corretto. Atteso PAGAMENTO_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
			Assert.assertTrue(sv.getIur() != null, "IUR non imporstato");
			Assert.assertTrue(sv.getSingoloImportoPagato().equals(sv.getImportoSingoloVersamento()), "Importo singolo pagamento diverso dall'importo singolo versamento.");
		}
	}

	@Test(dependsOnMethods = {"eseguiPagamentoEnte_Eseguito"})
	public void eseguiPagamentoEnte_NonEseguito() throws Exception {

		// Inserisco e pago un Versamento

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");

		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

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

		// Acquisizione di ricevuta "Non eseguito"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.NON_ESEGUITO);
		pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));


		rpt = rptBD.getLastRpt(versamentoLetto.getId());
		Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.PAGAMENTO_NON_ESEGUITO), "Stato versamento non corretto. Atteso: PAGAMENTO_NON_ESEGUITO; Trovato " + versamentoLetto.getStato());
		Assert.assertTrue(versamentoLetto.getImportoPagato().compareTo(BigDecimal.ZERO) == 0, "Importo pagato diverso da 0");
		for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
			Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
			Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE), "Stato singolo versamento non corretto. Atteso PAGAMENTO_NON_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
			Assert.assertTrue(sv.getIur().equals(Rt.NO_IUR), "IUR impostato, atteso n/a");
			Assert.assertTrue(sv.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0, "Importo singolo pagamento diverso da 0.");
		}
	}


	public void eseguiPagamentoEnte_EseguitoParziale() throws Exception {

		// Inserisco e pago un Versamento

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");

		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

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

		// Acquisizione di ricevuta "ESEGUITO_PARZIALE"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.ESEGUITO_PARZIALE);
		pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));


		rpt = rptBD.getLastRpt(versamentoLetto.getId());
		Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.PAGAMENTO_PARZIALMENTE_ESEGUITO), "Stato versamento non corretto. Atteso: PAGAMENTO_PARZIALMENTE_ESEGUITO; Trovato " + versamentoLetto.getStato());
		Assert.assertTrue(versamentoLetto.getImportoPagato() != null && versamentoLetto.getImportoPagato().compareTo(BigDecimal.ZERO) != 0, "Importo pagato uguale a 0");
		BigDecimal totale = BigDecimal.ZERO;
		for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
			Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
			Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE) || sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.PAGATO), "Stato singolo versamento non corretto. Atteso PAGAMENTO_NON_ESEGUITO o PAGAMENTO_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
			if(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE)) {
				Assert.assertTrue(sv.getIur().equals(Rt.NO_IUR), "IUR impostato, atteso n/a");
				Assert.assertTrue(sv.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0, "Importo singolo pagamento diverso da 0.");
			} else {
				Assert.assertTrue(!sv.getIur().equals(Rt.NO_IUR), "IUR impostato ad n/a, atteso valore diverso per pagamento eseguito");
				Assert.assertTrue(sv.getSingoloImportoPagato() != null && !sv.getSingoloImportoPagato().equals(BigDecimal.ZERO), "Importo singolo pagamento uguale a 0.");
				totale = totale.add(sv.getSingoloImportoPagato());
			}
		}
		Assert.assertTrue(versamentoLetto.getImportoPagato().equals(totale), "Importo pagato diverso dal totale dei singoliImportiPagati");

	}



	public void eseguiPagamentoEnte_DecorrenzaParziale() throws Exception {
		// Inserisco e pago un Versamento

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");

		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

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

		// Acquisizione di ricevuta "DECORRENZA_PARZIALE"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.DECORRENZA_PARZIALE);
		pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));


		rpt = rptBD.getLastRpt(versamentoLetto.getId());
		Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.DECORRENZA_TERMINI_PARZIALE), "Stato versamento non corretto. Atteso: DECORRENZA_TERMINI_PARZIALE; Trovato " + versamentoLetto.getStato());
		Assert.assertTrue(versamentoLetto.getImportoPagato() != null && !versamentoLetto.getImportoPagato().equals(BigDecimal.ZERO), "Importo pagato uguale a 0");
		BigDecimal totale = BigDecimal.ZERO;
		for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
			Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
			Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE) || sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.PAGATO), "Stato singolo versamento non corretto. Atteso PAGAMENTO_NON_ESEGUITO o PAGAMENTO_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
			if(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE)) {
				Assert.assertTrue(sv.getIur().equals(Rt.NO_IUR), "IUR impostato, atteso n/a");
				Assert.assertEquals(sv.getSingoloImportoPagato().compareTo(BigDecimal.ZERO), 0, "Importo singolo pagamento diverso da quello atteso.");
			} else {
				Assert.assertTrue(!sv.getIur().equals(Rt.NO_IUR), "IUR impostato ad n/a, atteso valore diverso per pagamento eseguito");
				Assert.assertTrue(sv.getSingoloImportoPagato() != null && sv.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) != 0, "Importo singolo pagamento uguale a 0.");
				totale = totale.add(sv.getSingoloImportoPagato());
			}
		}
		Assert.assertTrue(versamentoLetto.getImportoPagato().equals(totale), "Importo pagato diverso dal totale dei singoliImportiPagati");
	}


	public void eseguiPagamentoEnte_Decorrenza() throws Exception {
		// Inserisco e pago un Versamento

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");

		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);

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

		// Acquisizione di ricevuta "DECORRENZA"
		TracciatiBD tracciatiBD = new TracciatiBD(bd); 
		byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
		CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
		CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.DECORRENZA);
		pagamenti.acquisisciRT(dominioA.getCodDominio(), iuv, Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));
		
		rpt = rptBD.getLastRpt(versamentoLetto.getId());
		Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.DECORRENZA_TERMINI), "Stato versamento non corretto. Atteso: DECORRENZA_TERMINI; Trovato " + versamentoLetto.getStato());
		Assert.assertEquals(versamentoLetto.getImportoPagato().compareTo(BigDecimal.ZERO), 0, "Importo pagato diverso da quello atteso");
		for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
			Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
			Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.DA_PAGARE), "Stato singolo versamento non corretto. Atteso PAGAMENTO_NON_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
			Assert.assertTrue(sv.getIur().equals(Rt.NO_IUR), "IUR impostato, atteso n/a");
			Assert.assertEquals(sv.getSingoloImportoPagato().compareTo(BigDecimal.ZERO), 0, "Importo singolo pagamento diverso da quello atteso.");
		}
	}


	public void eseguiPagamentoCarrelloEnte() throws Exception {
		setupRegistro();

		PspBD pspBD = new PspBD(bd);
		Psp psp = pspBD.getPsp("GovPAYPsp1");
		List<Versamento> carrello = new ArrayList<Versamento>();
		List<Long> idVersamenti = new ArrayList<Long>();
		Pagamenti versamenti = new Pagamenti(bd);

		{
			String iuv = versamenti.generaIuv(applicazioneAA.getId(), stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
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
			carrello.add(versamento);
			idVersamenti.add(versamento.getId());
		}

		{
			String iuv = versamenti.generaIuv(applicazioneBB.getId(), stazione.getApplicationCode(), dominioB.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
			Versamento versamento = new Versamento();
			Anagrafica anagraficaDebitore = new Anagrafica();
			anagraficaDebitore.setCodUnivoco("CodUnivocoDebitore");
			anagraficaDebitore.setRagioneSociale("RagioneSocialeDebitore");
			versamento.setAnagraficaDebitore(anagraficaDebitore);
			versamento.setCodDominio(dominioB.getCodDominio());
			versamento.setDataScadenza(sdf.parse("31/12/2020"));
			versamento.setIdApplicazione(applicazioneBB.getId());
			versamento.setIdEnte(enteBA.getId());
			versamento.setImportoTotale(new BigDecimal(90.10));
			versamento.setIuv(iuv);
			versamento.setCodVersamentoEnte(iuv);
			SingoloVersamento singoloVersamento = new SingoloVersamento();
			singoloVersamento.setAnnoRiferimento(2014);
			singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
			singoloVersamento.setIdTributo(tributoBB1.getId());
			singoloVersamento.setDatiSpecificiRiscossione(tributoBB1.getTipoContabilita() + "/" + tributoBB1.getCodContabilita());
			singoloVersamento.setIndice(0);
			singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
			singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
			versamento.getSingoliVersamenti().add(singoloVersamento);
			versamento.setStato(StatoVersamento.IN_ATTESA);
			versamenti.caricaPagamento(versamento);
			carrello.add(versamento);
			idVersamenti.add(versamento.getId());
		}

		Pagamenti pagamenti = new Pagamenti(bd);
		String burl = "http://www.govpay.it/back/pagamentoEnte";
		String pspUrl = pagamenti.eseguiPagamentoCarrelloEnte(portale.getId(), idVersamenti, psp.getCanale(TipoVersamento.CARTA_PAGAMENTO, ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO).getId(), null, FirmaRichiesta.NESSUNA, Autenticazione.N_A, null, burl);


		Assert.assertTrue(pspUrl != null, "Era attesa una url di ritorno");

		VersamentiBD versamentiBD = new VersamentiBD(bd);
		RptBD rptBD = new RptBD(bd);

		for (Versamento versamento : carrello) {
			Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
			Rpt rpt = rptBD.getLastRpt(versamentoLetto.getId());
			Assert.assertTrue(rpt != null, "Non e' stata trovata la RPT del pagamento effettuato");
			Assert.assertTrue(rpt.getAnagraficaVersante() == null, "AnagraficaVersante dell'RPT e' valorizzata.");
			Assert.assertTrue(rpt.getAutenticazioneSoggetto().equals(Autenticazione.N_A), "AutenticazioneSoggetto dell'RPT differisce da quella della richiesta: " + rpt.getAutenticazioneSoggetto());
			Assert.assertTrue(rpt.getCallbackURL().equals(burl), "CallbackURL dell'RPT differisce da quella della richiesta: " + rpt.getCallbackURL());
			Assert.assertTrue(rpt.getCcp().equals(Rpt.CCP_NA), "Ccp dell'RPT fuori specifica: " + rpt.getCcp());
			Assert.assertTrue(rpt.getCodCarrello() != null, "CodCarrello non valorizzato");
			Assert.assertTrue(rpt.getCodMsgRichiesta() != null, "CodMsgRichiesta non valorizzato");
			Assert.assertTrue(rpt.getCodSessione() != null, "CodSessione non valorizzato");
			Assert.assertTrue(rpt.getDataOraCreazione() != null, "DataOraCreazione non valorizzata");
			Assert.assertTrue(rpt.getDataOraMsgRichiesta() != null, "DataOraMsgRichiesta non valorizzata");
			Assert.assertTrue(rpt.getDescrizioneStato() == null, "Descrizione stato valorizzata: " + rpt.getDescrizioneStato());
			Assert.assertTrue(rpt.getFaultCode() == null, "FaultCode valorizzato: " + rpt.getFaultCode());
			Assert.assertTrue(rpt.getFirmaRichiesta().equals(FirmaRichiesta.NESSUNA), "FirmaRichiesta dell'RPT differisce da quella della richiesta");
			Assert.assertTrue(rpt.getIbanAddebito() == null, "IbanAddebito dell'RPT differisce da quella della richiesta");
			Assert.assertEquals(rpt.getIdPortale(), portale.getId(), "Portale dell'RPT differisce da quella della richiesta");
			Assert.assertTrue(rpt.getIdPsp() == psp.getId(), "Psp dell'RPT differisce da quella della richiesta");
			Assert.assertTrue(rpt.getIdTracciatoXML() != null, "TracciatoXML dell'RPT non riferito");
			Assert.assertTrue(rpt.getIdVersamento() == versamentoLetto.getId(), "Versamento dell'RPT diverso da quello pagato");
			Assert.assertTrue(!rpt.getStatoRpt().equals(StatoRpt.RPT_ATTIVATA), "Stato dell'RPT ancora in RPT_DA_INVIARE_A_NODO");
			Assert.assertTrue(rpt.getTipoVersamento().equals(TipoVersamento.CARTA_PAGAMENTO), "TipoVersamento dell'RPT differisce da quella della richiesta");



			// Acquisizione di ricevuta "Eseguito"
			TracciatiBD tracciatiBD = new TracciatiBD(bd); 
			byte[] rptXml = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rptXml);
			CtRicevutaTelematica ctRt = RTBuilder.buildRTFromRPT(ctRpt, TipoRicevuta.ESEGUITO);
			pagamenti.acquisisciRT(versamentoLetto.getCodDominio(), versamentoLetto.getIuv(), Rpt.CCP_NA, ctRt, JaxbUtils.toByte(ctRt));

			rpt = rptBD.getLastRpt(versamentoLetto.getId());
			Assert.assertTrue(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA), "Stato dell'RPT diverso da RT_ACCETTATA_PA");

			versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
			Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.PAGAMENTO_ESEGUITO), "Stato versamento non corretto. Atteso: PAGAMENTO_ESEGUITO; Trovato " + versamentoLetto.getStato());
			Assert.assertTrue(versamentoLetto.getImportoPagato().equals(versamentoLetto.getImportoTotale()), "Importo totale diverso dall'importo pagato");
			for(SingoloVersamento sv : versamentoLetto.getSingoliVersamenti()){
				Assert.assertTrue(sv.getDataEsitoSingoloPagamento() != null, "Data esito singolo pagamento non impostata");
				Assert.assertTrue(sv.getStatoSingoloVersamento().equals(StatoSingoloVersamento.PAGATO), "Stato singolo versamento non corretto. Atteso PAGAMENTO_ESEGUITO; Trovato " + sv.getStatoSingoloVersamento());
				Assert.assertTrue(sv.getIur() != null, "IUR non imporstato");
				Assert.assertTrue(sv.getSingoloImportoPagato().equals(sv.getImportoSingoloVersamento()), "Importo singolo pagamento diverso dall'importo singolo versamento.");
			}
		}
	}
}
