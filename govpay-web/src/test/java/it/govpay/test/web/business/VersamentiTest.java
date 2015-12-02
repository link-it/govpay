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

import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "versamento"})
public class VersamentiTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	public void generaIuvISO() throws Exception {
		setupAnagrafica();
		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
		Assert.assertTrue(iuv != null && !iuv.isEmpty(), "L'Iuv generato e' vuoto");
		Assert.assertTrue(IuvUtils.checkISO11640(iuv), "L'Iuv generato non e' ISO11640");
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Iuv iuvLetto = versamentiBD.getIuv(dominioA.getCodDominio(), iuv);
		Assert.assertTrue(iuvLetto != null, "L'Iuv generato non e' presente in DB.");
		Assert.assertTrue(iuvLetto.getIdApplicazione() == applicazioneAA.getId(), "L'Iuv letto non e' associato all'applicazione richiedente.");
	}
	
	public void generaIuvNumerico() throws Exception {
		setupAnagrafica();
		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAA.getId(), dominioA.getCodDominio(), TipoIUV.NUMERICO, Iuv.AUX_DIGIT);
		Assert.assertTrue(iuv != null && !iuv.isEmpty(), "L'Iuv generato e' vuoto");
		try {
			Long.parseLong(iuv);
		} catch (NumberFormatException e) {
			Assert.fail("L'Iuv generato non e' numerico");
		}
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Iuv iuvLetto = versamentiBD.getIuv(dominioA.getCodDominio(), iuv);
		Assert.assertTrue(iuvLetto != null, "L'Iuv generato non e' presente in DB.");
		Assert.assertTrue(iuvLetto.getIdApplicazione() == applicazioneAA.getId(), "L'Iuv letto non e' associato all'applicazione richiedente.");
	}
	
	
	@Test(dependsOnMethods= {"generaIuvISO"})
	public void inserisciPagamento() throws Exception {
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
		versamento.setCodVersamentoEnte(iuv);
		versamento.setIuv(iuv);
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita().getCodifica() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita().getCodifica() + "/" + tributoAA2.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		versamenti.caricaPagamento(versamento);
		
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamentoLetto = versamentiBD.getVersamento(applicazioneAA.getId(), iuv);
		
		Assert.assertTrue(versamentoLetto != null, "Il versamento non e' su base dati.");
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto da DB e' diverso da quello inserito: " + BasicModel.diff(versamento, versamentoLetto));
	}
	
	
	@Test(dependsOnMethods= {"inserisciPagamento"})
	public void erroreAutorizzazioneIuv() throws Exception {
		Pagamenti versamenti = new Pagamenti(bd);
		String iuv = versamenti.generaIuv(applicazioneAB.getId(), dominioA.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
		
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
		versamento.setCodVersamentoEnte(iuv);
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita().getCodifica() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita().getCodifica() + "/" + tributoAA2.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		
		try {
			versamenti.caricaPagamento(versamento);
		} catch (GovPayException e) {
			Assert.assertTrue(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE), "Atteso ERRORE_AUTORIZZAZIONE, ricevuto " + e.getTipoException());
			return;
		}
		Assert.fail("Atteso GovPayException.");
	}
	
	@Test(dependsOnMethods= {"inserisciPagamento"})
	public void erroreAutorizzazioneTipoDebito() throws Exception {
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
		versamento.setCodVersamentoEnte(iuv);
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita().getCodifica() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAB1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAB1.getTipoContabilita().getCodifica() + "/" + tributoAB1.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		
		try {
			versamenti.caricaPagamento(versamento);
		} catch (GovPayException e) {
			Assert.assertTrue(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE), "Atteso ERRORE_AUTORIZZAZIONE, ricevuto " + e.getTipoException());
			return;
		}
		Assert.fail("Atteso GovPayException.");
	}
	
	
	
	@Test(dependsOnMethods= {"inserisciPagamento"})
	public void aggiornaPagamento() throws Exception {
		
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
		versamento.setCodVersamentoEnte(iuv);
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_0");
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita().getCodifica() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita().getCodifica() + "/" + tributoAA2.getCodContabilita());
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
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		
		Assert.assertTrue(versamentoLetto != null, "Il versamento non e' su base dati.");
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto da DB e' diverso da quello inserito.");
	}
	
	
	@Test(dependsOnMethods= {"inserisciPagamento"})
	public void annullaPagamento() throws Exception {
		
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
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita().getCodifica() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(90.10));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte("CodVersamentoEnte_1");
		singoloVersamento.setIdTributo(tributoAA2.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA2.getTipoContabilita().getCodifica() + "/" + tributoAA2.getCodContabilita());
		singoloVersamento.setIndice(1);
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal(9.90));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		versamenti.caricaPagamento(versamento);
		versamenti.annullaPagamento(versamento.getIdApplicazione(), versamento.getCodDominio(), versamento.getIuv());
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
		
		Assert.assertTrue(versamentoLetto.getStato().equals(Versamento.StatoVersamento.ANNULLATO), "Il versamento non e' stato annullato.");
	}
}
