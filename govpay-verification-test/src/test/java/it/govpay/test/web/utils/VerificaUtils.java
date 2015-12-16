/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.govpay.it
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.test.web.utils;

import java.math.BigDecimal;

import org.testng.Assert;

import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.rs.DatiSingoloVersamento;
import it.govpay.rs.Pagamento;
import it.govpay.rs.StatoPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.GpChiediStatoPagamentoResponse;
import it.govpay.servizi.pa.SingoloPagamento;

public class VerificaUtils {

	public static void checkVerificaPagamento(VerificaPagamento verifica, Pagamento p, StatoPagamento stato) {
		Assert.assertTrue(verifica != null, "[" + p.getDatiVersamento().getIuv() + "] Nessuna verifica ricevuta.");
		Assert.assertTrue(verifica.getStatoPagamento() != null, "[" + p.getDatiVersamento().getIuv() + "] Non c'e' uno stato nella verifica.");
		Assert.assertTrue(verifica.getStatoPagamento().equals(stato), "[" + p.getDatiVersamento().getIuv() + "] Stato pagamento [" + verifica.getStatoPagamento() + "] diverso da " + stato);
		switch (stato) {
		case ANNULLATO:
		case NON_ESEGUITO:
		case IN_CORSO:
		case IN_ERRORE:
			Assert.assertTrue(verifica.getImportoTotalePagato().compareTo(BigDecimal.ZERO) == 0, "[" + p.getDatiVersamento().getIuv() + "] ImportoTotalePagato diverso da 0 per esito " + stato);
			for(DatiSingoloPagamento dati : verifica.getDatiSingoloPagamento()) {
				Assert.assertTrue(dati.getImportoPagato().equals(BigDecimal.ZERO), "[" + p.getDatiVersamento().getIuv() + "] ImportoPagato diverso da 0 per esito " + stato);
				Assert.assertTrue(dati.getIur()== null ||  dati.getIur().equals("n/a"), "[" + p.getDatiVersamento().getIuv() + "] IUR diverso da 'n/a' per esito " + stato);
			}
			break;
		case ESEGUITO:
			Assert.assertTrue(verifica.getImportoTotalePagato().compareTo(p.getDatiVersamento().getImportoTotaleDaVersare())== 0, "[" + p.getDatiVersamento().getIuv() + "] ImportoTotalePagato diverso da ImportoDaVersare per esito " + stato);
			for(DatiSingoloPagamento datiP : verifica.getDatiSingoloPagamento()) {
				boolean found = false;
				for(DatiSingoloVersamento datiV : p.getDatiVersamento().getDatiSingoloVersamento()) {
					if(datiV.getIusv().equals(datiP.getIusv())) {
						found = true;
						Assert.assertTrue(datiP.getImportoPagato().compareTo(datiV.getImportoSingoloVersamento())== 0, "[" + p.getDatiVersamento().getIuv() + "] Pagamento e versamento non hanno il solito importo [" + datiV.getImportoSingoloVersamento() + " != " + datiP.getImportoPagato() + " ]");
						Assert.assertTrue(datiP.getIur()!= null && !datiP.getIur().equals("n/a"), "[" + p.getDatiVersamento().getIuv() + "] IUR non valirizzato per un pagamento ESEGUITO");
					} 
				}
				Assert.assertTrue(found, "[" + p.getDatiVersamento().getIuv() + "] Manca un pagamento!");
			}
			break;
		case PARZIALMENTE_ESEGUITO:
			BigDecimal somma = BigDecimal.ZERO;
			for(DatiSingoloPagamento datiP : verifica.getDatiSingoloPagamento()) {
				boolean found = false;
				for(DatiSingoloVersamento datiV : p.getDatiVersamento().getDatiSingoloVersamento()) {
					if(datiV.getIusv().equals(datiP.getIusv())) {
						found = true;
						somma = somma.add(datiP.getImportoPagato());
						if(datiP.getImportoPagato().compareTo(BigDecimal.ZERO) != 0) {
							Assert.assertTrue(datiP.getImportoPagato().compareTo(datiV.getImportoSingoloVersamento())== 0, "[" + p.getDatiVersamento().getIuv() + "] Pagamento eseguito e versamento non hanno il solito importo");
							Assert.assertTrue(datiP.getIur() != null && !datiP.getIur().equals("n/a"), "[" + p.getDatiVersamento().getIuv() + "] IUR non valirizzato per un pagamento ESEGUITO");
						} else {
							Assert.assertTrue(datiP.getIur()== null || datiP.getIur().equals("n/a"), "[" + p.getDatiVersamento().getIuv() + "] IUR diverso da 'n/a' per esito " + stato);
						}
					} 
				}
				Assert.assertTrue(found, "[" + p.getDatiVersamento().getIuv() + "] Manca un pagamento!");
			}
			Assert.assertTrue(verifica.getImportoTotalePagato().equals(somma), "[" + p.getDatiVersamento().getIuv() + "] ImportoTotalePagato diverso dalla somma degli importi " + stato);
			break;
		case STORNATO:
			Assert.fail("[" + p.getDatiVersamento().getIuv() + "] STORNO NON SUPPORTATO");
			break;
		}

	}

	public static void checkVerificaPagamento(GpChiediStatoPagamentoResponse r,
			it.govpay.servizi.pa.Pagamento p,
			it.govpay.servizi.pa.StatoPagamento s) {

		Assert.assertEquals(r.getCodApplicazione(), p.getCodApplicazione());
		Assert.assertEquals(r.getCodEnte(),p.getCodEnte());
		Assert.assertNull(r.getCodErrore());
		Assert.assertEquals(r.getCodEsito(), CodEsito.OK);
		Assert.assertNotNull(r.getCodOperazione());
		Assert.assertNotNull(r.getCodPortale());
		Assert.assertNull(r.getDescrizioneErrore());
		Assert.assertTrue(r.getIdPagamento().isEmpty());
		Assert.assertEquals(r.getStato(), s);
		
		
		switch (s) {
			case PAGAMENTO_ESEGUITO:
				Assert.assertNotNull(r.getXmlRT());
				Assert.assertTrue(r.getPagamento().getImportoTotale().compareTo(p.getImportoTotale())== 0, "ImportoTotalePagato diverso da ImportoDaVersare per esito " + s);
				for(SingoloPagamento datiP : p.getSingoloPagamento()) {
					boolean found = false;
					for(it.govpay.servizi.pa.GpChiediStatoPagamentoResponse.Pagamento.SingoloPagamento datiV : r.getPagamento().getSingoloPagamento()) {
						if(datiV.getCodVersamentoEnte().equals(datiP.getCodVersamentoEnte())) {
							found = true;
							Assert.assertTrue(datiP.getImporto().compareTo(datiV.getImportoPagato())== 0, "Pagamento e versamento non hanno il solito importo");
							Assert.assertTrue(datiV.getIur()!= null && !datiV.getIur().equals("n/a"), "IUR non valirizzato per un pagamento ESEGUITO");
						} 
					}
					Assert.assertTrue(found, "Manca un pagamento!");
				}
			break;
			default:
				Assert.fail("Stato non supportato dal metodo di verifica");
			}
	}
}
