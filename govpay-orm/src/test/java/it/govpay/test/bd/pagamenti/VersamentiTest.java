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

import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.test.BasicTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"pagamenti", "versamenti"}, dependsOnGroups = { "anagrafica" })
public class VersamentiTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	public void generaIuv() throws Exception {
		
		setupAnagrafica();
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		
		Assert.assertTrue(iuv != null, "Lo iuv non e' stato generato");
		Assert.assertTrue(iuv.getCodDominio().equals(dominioA.getCodDominio()), "Lo iuv generato e' del CodDominio errato");
		Assert.assertTrue(iuv.getDataGenerazione() != null, "Lo iuv generato non ha la dataCreazione");
		Assert.assertTrue(iuv.getId() != null, "Lo iuv generato non ha l'Id");
		Assert.assertTrue(iuv.getIdApplicazione() == applicazioneAA.getId(), "Lo iuv generato e' del IdApplicazione errato");
		Assert.assertTrue(iuv.getIuv() != null, "Lo iuv generato non ha lo iuv");
		Assert.assertTrue(iuv.getPrg() != 0, "Lo iuv generato non ha il prg");
		
		Iuv iuvLetto = versamentiBD.getIuv(dominioA.getCodDominio(), iuv.getIuv());
		
		Assert.assertTrue(iuv.equals(iuvLetto), "Lo iuv letto e' diverso da quello generato");
		
		iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.NUMERICO);
		
		Assert.assertTrue(iuv != null, "Lo iuv non e' stato generato");
		Assert.assertTrue(iuv.getCodDominio().equals(dominioA.getCodDominio()), "Lo iuv generato e' del CodDominio errato");
		Assert.assertTrue(iuv.getDataGenerazione() != null, "Lo iuv generato non ha la dataCreazione");
		Assert.assertTrue(iuv.getId() != null, "Lo iuv generato non ha l'Id");
		Assert.assertTrue(iuv.getIdApplicazione() == applicazioneAA.getId(), "Lo iuv generato e' del IdApplicazione errato");
		Assert.assertTrue(iuv.getIuv() != null, "Lo iuv generato non ha lo iuv");
		Assert.assertTrue(iuv.getPrg() != 0, "Lo iuv generato non ha il prg");
		
		iuvLetto = versamentiBD.getIuv(dominioA.getCodDominio(), iuv.getIuv());
		
		Assert.assertTrue(iuv.equals(iuvLetto), "Lo iuv letto e' diverso da quello generato");
	}
	
	@Test(dependsOnMethods = { "generaIuv" })
	public void inserisciVersamento() throws Exception {
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		
		Versamento versamento = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivoco");
		anagraficaDebitore.setRagioneSociale("RagioneSociale");
		versamento.setAnagraficaDebitore(anagraficaDebitore);
		versamento.setCodDominio(dominioA.getCodDominio());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2020,12,31);
		versamento.setDataScadenza(calendar.getTime());
		versamento.setIdApplicazione(applicazioneAA.getId());
		versamento.setImportoTotale(BigDecimal.valueOf(90.10));
		versamento.setCodVersamentoEnte(iuv.getIuv());
		versamento.setIuv(iuv.getIuv());
		versamento.setIdEnte(enteAA.getId());
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte(null);
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		
		versamentiBD.insertVersamento(versamento);
		
		Assert.assertTrue(versamento.getId() != null, "Il versamento inserito non ha Id");
		for(SingoloVersamento singolo: versamento.getSingoliVersamenti()) {
			Assert.assertTrue(singolo.getId() != null, "Il singoloVersamento inserito non ha Id");
		}
		
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getId());
		
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto con Id e' diverso da quello inserito:" + BasicModel.diff(versamento, versamentoLetto));
		
		versamentoLetto = versamentiBD.getVersamento(applicazioneAA.getId(), versamento.getCodVersamentoEnte());
		
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto con IdApplicazione e IUV e' diverso da quello inserito");
		
		versamentoLetto = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getCodVersamentoEnte());
		
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto con CodDominio e IUV e' diverso da quello inserito");
		
	}
	
	
	@Test(dependsOnMethods = { "inserisciVersamento" })
	public void aggiornaVersamento() throws Exception {
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		
		Versamento versamento = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivoco");
		anagraficaDebitore.setRagioneSociale("RagioneSociale");
		versamento.setAnagraficaDebitore(anagraficaDebitore);
		versamento.setCodDominio(dominioA.getCodDominio());
		versamento.setIdEnte(enteAA.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2020,12,31);
		versamento.setDataScadenza(calendar.getTime());
		versamento.setIdApplicazione(applicazioneAA.getId());
		versamento.setImportoTotale(BigDecimal.valueOf(90.10));
		versamento.setCodVersamentoEnte(iuv.getIuv());
		versamento.setIuv(iuv.getIuv());
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte(null);
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		
		versamentiBD.insertVersamento(versamento);
		
		singoloVersamento.setAnnoRiferimento(2015);
		singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte(null);
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(9.90));
		singoloVersamento.setIndice(1);
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setImportoTotale(BigDecimal.valueOf(100.0));
		
		versamentiBD.replaceVersamento(versamento);
		
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getId());
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto e' diverso da quello aggiornato:"+BasicModel.diff(versamento, versamentoLetto));
	}
	
	
	@Test(dependsOnMethods = { "inserisciVersamento" })
	public void aggiornaStatoVersamento() throws Exception {
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		
		Versamento versamento = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivoco");
		anagraficaDebitore.setRagioneSociale("RagioneSociale");
		versamento.setAnagraficaDebitore(anagraficaDebitore);
		versamento.setCodDominio(dominioA.getCodDominio());
		versamento.setIdEnte(enteAA.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2020,12,31);
		versamento.setDataScadenza(calendar.getTime());
		versamento.setIdApplicazione(applicazioneAA.getId());
		versamento.setImportoTotale(BigDecimal.valueOf(90.10));
		versamento.setCodVersamentoEnte(iuv.getIuv());
		versamento.setIuv(iuv.getIuv());
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte(null);
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		
		versamentiBD.insertVersamento(versamento);
		
		versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ANNULLATO);
		
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getId());
		Assert.assertTrue(versamentoLetto.getStato().equals(StatoVersamento.ANNULLATO), "Lo stato versamento non si e' aggiornato");
	}
	

	@Test(dependsOnMethods = { "inserisciVersamento" })
	public void aggiornaSingoloVersamento() throws Exception {
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
		
		Versamento versamento = new Versamento();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("CodUnivoco");
		anagraficaDebitore.setRagioneSociale("RagioneSociale");
		versamento.setAnagraficaDebitore(anagraficaDebitore);
		versamento.setCodDominio(dominioA.getCodDominio());
		versamento.setIdEnte(enteAA.getId());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2020,12,31);
		versamento.setDataScadenza(calendar.getTime());
		versamento.setIdApplicazione(applicazioneAA.getId());
		versamento.setImportoTotale(BigDecimal.valueOf(90.10));
		versamento.setCodVersamentoEnte(iuv.getIuv());
		versamento.setIuv(iuv.getIuv());
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setAnnoRiferimento(2014);
		singoloVersamento.setCodSingoloVersamentoEnte(null);
		singoloVersamento.setIdTributo(tributoAA1.getId());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
		singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
		singoloVersamento.setIndice(0);
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
		versamento.getSingoliVersamenti().add(singoloVersamento);
		versamento.setStato(StatoVersamento.IN_ATTESA);
		versamentiBD.insertVersamento(versamento);
		
		singoloVersamento.setIbanAccredito("IbanAccredito");
		singoloVersamento.setCausaleVersamento("CausaleVersamento");
		singoloVersamento.setDatiSpecificiRiscossione("DatiSpecificiRiscossione");
		
		singoloVersamento = versamento.getSingoliVersamenti().get(0);
		versamentiBD.updateSingoloVersamentoRPT(singoloVersamento.getId(), singoloVersamento.getIbanAccredito(), singoloVersamento.getCausaleVersamento(), singoloVersamento.getDatiSpecificiRiscossione());
		
		Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getId());
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto e' diverso da quello aggiornato");
		
		singoloVersamento.setEsitoSingoloPagamento("EsitoSingoloPagamento");
		singoloVersamento.setDataEsitoSingoloPagamento(new Date());
		singoloVersamento.setIur("Iur");
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.PAGATO);
		versamentiBD.updateSingoloVersamentoRT(singoloVersamento.getId(), singoloVersamento.getEsitoSingoloPagamento(),singoloVersamento.getDataEsitoSingoloPagamento(), singoloVersamento.getIur(), singoloVersamento.getStatoSingoloVersamento() );
		
		versamentoLetto = versamentiBD.getVersamento(versamento.getId());
		Assert.assertTrue(versamento.equals(versamentoLetto), "Il versamento letto e' diverso da quello aggiornato");
	}
	
	
	@Test(dependsOnMethods = { "inserisciVersamento" })
	public void cercaVersamento() throws Exception {
		reInitDataBase();
		setupAnagrafica();
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		int index = 1;
		
		List<Versamento> lstVersamenti = new ArrayList<Versamento>(); 
		for(StatoVersamento statoVersamento : StatoVersamento.values()) {
			for(int i = 0; i < index; i ++) {
				Iuv iuv = versamentiBD.generaIuv(applicazioneAA.getId(), Iuv.AUX_DIGIT, stazione.getApplicationCode(), dominioA.getCodDominio(), TipoIUV.ISO11694);
				Versamento versamento = new Versamento();
				Anagrafica anagraficaDebitore = new Anagrafica();
				anagraficaDebitore.setCodUnivoco("CodUnivoco");
				anagraficaDebitore.setRagioneSociale("RagioneSociale");
				versamento.setAnagraficaDebitore(anagraficaDebitore);
				versamento.setCodDominio(dominioA.getCodDominio());
				versamento.setIdEnte(enteAA.getId());
				Calendar calendar = Calendar.getInstance();
				calendar.clear();
				calendar.set(2020,12,31);
				versamento.setDataScadenza(calendar.getTime());
				versamento.setIdApplicazione(applicazioneAA.getId());
				versamento.setImportoTotale(BigDecimal.valueOf(90.10));
				versamento.setCodVersamentoEnte(iuv.getIuv());
				versamento.setIuv(iuv.getIuv());
				SingoloVersamento singoloVersamento = new SingoloVersamento();
				singoloVersamento.setAnnoRiferimento(2014);
				singoloVersamento.setCodSingoloVersamentoEnte(null);
				singoloVersamento.setIdTributo(tributoAA1.getId());
				singoloVersamento.setDatiSpecificiRiscossione(tributoAA1.getTipoContabilita() + "/" + tributoAA1.getCodContabilita());
				singoloVersamento.setIndice(0);
				singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(90.10));
				singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
				versamento.getSingoliVersamenti().add(singoloVersamento);
				versamento.setStato(statoVersamento);
				versamentiBD.insertVersamento(versamento);
				lstVersamenti.add(versamento);
			}
			index++;
		}
		
		index = 1;

		List<Versamento> lstVersamentiOttenuti = versamentiBD.findAll(versamentiBD.newFilter());
		
		for(Versamento versamento: lstVersamenti) {
			Assert.assertTrue(lstVersamentiOttenuti.contains(versamento), "La lista dei versamenti non contiene il versamento con stato ["+versamento.getStato()+"]");
		}
	}
}
