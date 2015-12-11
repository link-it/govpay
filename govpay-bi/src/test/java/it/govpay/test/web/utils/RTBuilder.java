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
package it.govpay.test.web.utils;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivoco;

import java.math.BigDecimal;
import java.util.Date;

public class RTBuilder {
	
	public enum TipoRicevuta {
		ESEGUITO ("R01"), 
		ESEGUITO_ImportoTotalePagatoErrato ("R07"), 
		ESEGUITO_ConRitardoRT("R09"),
		NON_ESEGUITO ("R02"), 
		NON_ESEGUITO_NoDatiSingoloPagamento ("R08"),
		ESEGUITO_PARZIALE ("R03"), 
		DECORRENZA ("R04"), 
		DECORRENZA_PARZIALE ("R05"), 
		SINTASSI_ERRATA ("R06"), 
		MIXED("R10");
		
		String codice;
		private TipoRicevuta(String codice) {
			this.codice = codice;
		}
		
		public static TipoRicevuta getTipoRicevuta(String codice) {
			if(codice == null) return ESEGUITO;
			if(codice.endsWith("R01")) return ESEGUITO;
			if(codice.endsWith("R02")) return NON_ESEGUITO;
			if(codice.endsWith("R03")) return ESEGUITO_PARZIALE;
			if(codice.endsWith("R04")) return DECORRENZA;
			if(codice.endsWith("R05")) return DECORRENZA_PARZIALE;
			if(codice.endsWith("R06")) return SINTASSI_ERRATA;
			if(codice.endsWith("R07")) return ESEGUITO_ImportoTotalePagatoErrato;
			if(codice.endsWith("R08")) return NON_ESEGUITO_NoDatiSingoloPagamento;
			if(codice.endsWith("R09")) return ESEGUITO_ConRitardoRT;
			if(codice.endsWith("R10")) return MIXED;
			return ESEGUITO;
		}
		
		public String getCodice() {
			return codice;
		}
	}
	
	public static CtRicevutaTelematica buildRTFromRPT(CtRichiestaPagamentoTelematico rpt, TipoRicevuta tipoRicevuta) throws Exception {

		CtRicevutaTelematica rt = new CtRicevutaTelematica();
		//
		// obbligatori nell'XML
		//
		rt.setVersioneOggetto(rpt.getVersioneOggetto());
		rt.setDominio(rpt.getDominio());
		rt.setEnteBeneficiario(rpt.getEnteBeneficiario());
		rt.setSoggettoPagatore(rpt.getSoggettoPagatore());
		if(rpt.getSoggettoVersante() != null)
			rt.setSoggettoVersante(rpt.getSoggettoVersante());

		CtIstitutoAttestante istitutoAttestante = new CtIstitutoAttestante();
		istitutoAttestante.setDenominazioneAttestante("SIMULATORE PSP");
		CtIdentificativoUnivoco identificativoUnivoco = new CtIdentificativoUnivoco();
		identificativoUnivoco.setCodiceIdentificativoUnivoco("idSimulatorePsp");
		identificativoUnivoco.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.G);
		istitutoAttestante.setIdentificativoUnivocoAttestante(identificativoUnivoco);
		rt.setIstitutoAttestante(istitutoAttestante);
		rt.setIdentificativoMessaggioRicevuta(rpt.getIdentificativoMessaggioRichiesta());
		rt.setDataOraMessaggioRicevuta(rpt.getDataOraMessaggioRichiesta());
		
		//
		// utilizzati per riconciliare
		//
		rt.setRiferimentoMessaggioRichiesta(rpt.getIdentificativoMessaggioRichiesta());
		rt.setRiferimentoDataRichiesta(rpt.getDataOraMessaggioRichiesta());
		
		rt.setDatiPagamento(buildDatiVersamentoRTFromRPT(rpt.getDatiVersamento(), tipoRicevuta));
		
		return rt;
	}
	

	private static CtDatiVersamentoRT buildDatiVersamentoRTFromRPT(CtDatiVersamentoRPT datiVersamentoRPT, TipoRicevuta tipoRicevuta) {
		
		CtDatiVersamentoRT datiVersamentoRT = new CtDatiVersamentoRT();
		
		datiVersamentoRT.setCodiceContestoPagamento(datiVersamentoRPT.getCodiceContestoPagamento());
		datiVersamentoRT.setIdentificativoUnivocoVersamento(datiVersamentoRPT.getIdentificativoUnivocoVersamento());
		
		int index = 0;
		BigDecimal importoTotale = BigDecimal.ZERO;
		
		switch (tipoRicevuta) {

		case ESEGUITO:
		case ESEGUITO_ConRitardoRT:
			datiVersamentoRT.setCodiceEsitoPagamento("0");
			datiVersamentoRT.setImportoTotalePagato(datiVersamentoRPT.getImportoTotaleDaVersare());
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				datiVersamentoRT.getDatiSingoloPagamento().add(buildDatiSingoloVersamentoRTFromRPT(datiVersamentoRPT.getIdentificativoUnivocoVersamento(), index, datiSingoloVersamentoRPT));
				index++;
			}
			break;
			
		case ESEGUITO_ImportoTotalePagatoErrato:
			datiVersamentoRT.setCodiceEsitoPagamento("0");
			datiVersamentoRT.setImportoTotalePagato(datiVersamentoRPT.getImportoTotaleDaVersare().add(BigDecimal.TEN));
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				datiVersamentoRT.getDatiSingoloPagamento().add(buildDatiSingoloVersamentoRTFromRPT(datiVersamentoRPT.getIdentificativoUnivocoVersamento(), index, datiSingoloVersamentoRPT));
				index++;
			}
			break;

		case NON_ESEGUITO:
			datiVersamentoRT.setCodiceEsitoPagamento("1");
			datiVersamentoRT.setImportoTotalePagato(BigDecimal.ZERO);
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoNonEseguitoRTFromRPT(datiSingoloVersamentoRPT);
				singoloPagamento.setEsitoSingoloPagamento("Conto insufficiente");
				datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				index++;
			}
			break;
			
		case NON_ESEGUITO_NoDatiSingoloPagamento:
			datiVersamentoRT.setCodiceEsitoPagamento("1");
			datiVersamentoRT.setImportoTotalePagato(BigDecimal.ZERO);
			break;
			
		case ESEGUITO_PARZIALE:
			datiVersamentoRT.setCodiceEsitoPagamento("2");
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				if(index%2 ==0) {
					CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoNonEseguitoRTFromRPT(datiSingoloVersamentoRPT);
					singoloPagamento.setEsitoSingoloPagamento("Conto insufficiente");
					datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				} else {
					CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoRTFromRPT(datiVersamentoRPT.getIdentificativoUnivocoVersamento(), index, datiSingoloVersamentoRPT);
					importoTotale = importoTotale.add(singoloPagamento.getSingoloImportoPagato());
					datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				}
				index++;
			}
			datiVersamentoRT.setImportoTotalePagato(importoTotale);
			break;
			
		case DECORRENZA:
			datiVersamentoRT.setCodiceEsitoPagamento("3");
			datiVersamentoRT.setImportoTotalePagato(BigDecimal.ZERO);
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoNonEseguitoRTFromRPT(datiSingoloVersamentoRPT);
				singoloPagamento.setEsitoSingoloPagamento("Termini di pagamento decorsi");
				datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				index++;
			}
			break;
			
		case DECORRENZA_PARZIALE:
			datiVersamentoRT.setCodiceEsitoPagamento("4");
			
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				if(index%2 ==0) {
					CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoNonEseguitoRTFromRPT(datiSingoloVersamentoRPT);
					singoloPagamento.setEsitoSingoloPagamento("Termini di pagamento decorsi");
					datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				} else {
					CtDatiSingoloPagamentoRT singoloPagamento = buildDatiSingoloVersamentoRTFromRPT(datiVersamentoRPT.getIdentificativoUnivocoVersamento(), index, datiSingoloVersamentoRPT);
					importoTotale = importoTotale.add(singoloPagamento.getSingoloImportoPagato());
					datiVersamentoRT.getDatiSingoloPagamento().add(singoloPagamento);
				}
				index++;
			}
			datiVersamentoRT.setImportoTotalePagato(importoTotale);
			break;
			
		default:
			datiVersamentoRT.setCodiceEsitoPagamento("0");
			datiVersamentoRT.setImportoTotalePagato(datiVersamentoRPT.getImportoTotaleDaVersare());
			for (CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT : datiVersamentoRPT.getDatiSingoloVersamento()) {
				datiVersamentoRT.getDatiSingoloPagamento().add(buildDatiSingoloVersamentoRTFromRPT(datiVersamentoRPT.getIdentificativoUnivocoVersamento(), index, datiSingoloVersamentoRPT));
				index++;
			}
			break;
		}		
		
		return datiVersamentoRT;
	}

	private static CtDatiSingoloPagamentoRT buildDatiSingoloVersamentoRTFromRPT(String iuv, int index, CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT) {

		CtDatiSingoloPagamentoRT datiSingoloPagamentoRT = new CtDatiSingoloPagamentoRT();

		datiSingoloPagamentoRT.setCausaleVersamento(datiSingoloVersamentoRPT.getCausaleVersamento());
		datiSingoloPagamentoRT.setIdentificativoUnivocoRiscossione("idRisc-" + iuv + "-" + index);
		datiSingoloPagamentoRT.setSingoloImportoPagato(datiSingoloVersamentoRPT.getImportoSingoloVersamento());
		datiSingoloPagamentoRT.setDataEsitoSingoloPagamento(new Date());
		datiSingoloPagamentoRT.setDatiSpecificiRiscossione(datiSingoloVersamentoRPT.getDatiSpecificiRiscossione());

		return datiSingoloPagamentoRT;
	}
	
	private static CtDatiSingoloPagamentoRT buildDatiSingoloVersamentoNonEseguitoRTFromRPT(CtDatiSingoloVersamentoRPT datiSingoloVersamentoRPT) {

		CtDatiSingoloPagamentoRT datiSingoloPagamentoRT = new CtDatiSingoloPagamentoRT();
		datiSingoloPagamentoRT.setCausaleVersamento(datiSingoloVersamentoRPT.getCausaleVersamento());
		datiSingoloPagamentoRT.setSingoloImportoPagato(BigDecimal.ZERO);
		datiSingoloPagamentoRT.setIdentificativoUnivocoRiscossione("n/a");
		datiSingoloPagamentoRT.setDataEsitoSingoloPagamento(new Date());
		datiSingoloPagamentoRT.setEsitoSingoloPagamento("Non eseguito");
		datiSingoloPagamentoRT.setDatiSpecificiRiscossione(datiSingoloVersamentoRPT.getDatiSpecificiRiscossione());
		return datiSingoloPagamentoRT;
	}

}
