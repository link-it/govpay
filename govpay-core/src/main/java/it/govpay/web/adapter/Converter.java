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
package it.govpay.web.adapter;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.rs.AutenticazioneSoggetto;
import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.rs.DatiSingoloVersamento;
import it.govpay.rs.ModelloVersamento;
import it.govpay.rs.Pagamento;
import it.govpay.rs.Psp;
import it.govpay.rs.Soggetto;
import it.govpay.rs.StatoPagamento;
import it.govpay.rs.TipoFirmaRicevuta;
import it.govpay.rs.TipoVersamento;
import it.govpay.rs.VerificaPagamento;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;

public class Converter {

	public static Anagrafica toOrm(Soggetto soggetto) {
		if(soggetto == null) return null;
		Anagrafica a = new Anagrafica();
		a.setCap(soggetto.getCap());
		a.setCivico(soggetto.getCivico());
		a.setCodUnivoco(soggetto.getIdentificativo());
		a.setEmail(soggetto.getEmail());
		a.setIndirizzo(soggetto.getIndirizzo());
		a.setLocalita(soggetto.getLocalita());
		a.setNazione(soggetto.getNazione());
		a.setProvincia(soggetto.getProvincia());
		a.setRagioneSociale(soggetto.getAnagrafica());
		return a;
	}

	public static Autenticazione toOrm(AutenticazioneSoggetto autenticazione) throws GovPayException {
		switch (autenticazione) {
		case CNS: return Autenticazione.CNS;
		case N_A: return Autenticazione.N_A;
		case OTH: return Autenticazione.OTH;
		case USR: return Autenticazione.USR;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	public static FirmaRichiesta toOrm(TipoFirmaRicevuta firma) {
		switch (firma) {
		case AVANZATA: return FirmaRichiesta.AVANZATA;
		case CA_DES: return FirmaRichiesta.CA_DES;
		case NESSUNA: return FirmaRichiesta.NESSUNA;
		case XA_DES: return FirmaRichiesta.XA_DES;
		}
		return FirmaRichiesta.NESSUNA;
	}

	public static Versamento toVersamento(Dominio dominio, Long idEnte, Applicazione applicazione, Pagamento pagamento, Soggetto soggettoVersante, BasicBD bd) throws GovPayException {
		Versamento versamento = new Versamento();
		versamento.setAnagraficaDebitore(toOrm(pagamento.getSoggettoPagatore()));
		versamento.setDataScadenza(new Date());
		versamento.setImportoTotale(pagamento.getDatiVersamento().getImportoTotaleDaVersare());
		versamento.setCodVersamentoEnte(pagamento.getDatiVersamento().getIuv());
		versamento.setIuv(pagamento.getDatiVersamento().getIuv());
		versamento.setIdApplicazione(applicazione.getId());
		versamento.setIdEnte(idEnte);

		int indice = 0;
		for(DatiSingoloVersamento sv : pagamento.getDatiVersamento().getDatiSingoloVersamentos()) {
			Tributo tributo;
			try {
				tributo = AnagraficaManager.getTributo(bd, idEnte, pagamento.getDatiVersamento().getTipoDebito());
			} catch (NotFoundException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Il tributo non e' censito o non e' associato all'applicazione chiedente");
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			SingoloVersamento singoloVersamento = new SingoloVersamento();
			singoloVersamento.setAnnoRiferimento(sv.getAnnoRiferimento());
			singoloVersamento.setCodSingoloVersamentoEnte(sv.getIusv());
			singoloVersamento.setIdTributo(tributo.getId());
			singoloVersamento.setDatiSpecificiRiscossione(tributo.getTipoContabilita() + "/" + tributo.getCodContabilita());
			singoloVersamento.setIndice(indice);
			singoloVersamento.setImportoSingoloVersamento(sv.getImportoSingoloVersamento());
			singoloVersamento.setCausaleVersamento(sv.getCausaleVersamento());
			singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
			versamento.getSingoliVersamenti().add(singoloVersamento);
			indice++;
		}
		versamento.setStato(StatoVersamento.IN_ATTESA);
		versamento.setCodDominio(dominio.getCodDominio());
		return versamento;
	}



	public static Psp toPsp(Canale canale) {
		Psp psp = new Psp();
		psp.setCondizioniEconomicheMassime(canale.getCondizioni());
		psp.setDescrizione(canale.getDescrizione());
		psp.setDisponibilita(canale.getDisponibilita());
		psp.setIdPsp(canale.getId());
		psp.setInfoUrl(canale.getUrlInfo());
		psp.setModelloVersamento(toModelloVersamento(canale.getModelloPagamento()));
		psp.setRagioneSociale(canale.getPsp().getRagioneSociale());
		psp.setStornoGestito(canale.getPsp().isBolloGestito());
		psp.setTipoVersamento(toTipoVersamento(canale.getTipoVersamento()));
		return psp;
	}

	private static TipoVersamento toTipoVersamento(it.govpay.bd.model.Rpt.TipoVersamento tipoVersamento) {
		switch (tipoVersamento) {
		case ADDEBITO_DIRETTO: 
			return TipoVersamento.ADDEBITO_DIRETTO;
		case ATTIVATO_PRESSO_PSP: 
			return TipoVersamento.ATTIVATO_PRESSO_PSP;
		case BOLLETTINO_POSTALE: 
			return TipoVersamento.BOLLETTINO_POSTALE;
		case BONIFICO_BANCARIO_TESORERIA: 
			return TipoVersamento.BONIFICO_BANCARIO_TESORERIA;
		case CARTA_PAGAMENTO: 
			return TipoVersamento.CARTA_PAGAMENTO;
		case MYBANK: 
			return TipoVersamento.MY_BANK;
		default:
			return null;
		}
	}

	private static ModelloVersamento toModelloVersamento(it.govpay.bd.model.Psp.ModelloPagamento modelloVersamento) {
		switch (modelloVersamento) {
		case ATTIVATO_PRESSO_PSP: 
			return ModelloVersamento.ATTIVATO_PRESSO_PSP;
		case DIFFERITO: 
			return ModelloVersamento.DIFFERITO;
		case IMMEDIATO: 
			return ModelloVersamento.IMMEDIATO;
		case IMMEDIATO_MULTIBENEFICIARIO: 
			return ModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO;
		default:
			return null;
		}
	}



	public static VerificaPagamento toVerificaPagamento(Versamento versamento, Rpt rpt, byte[] rt) throws GovPayException{
		VerificaPagamento verificaPagamento = new VerificaPagamento();
		
		verificaPagamento.setIdentificativoBeneficiario(versamento.getCodDominio());
		verificaPagamento.setIuv(versamento.getIuv());
		verificaPagamento.setStatoPagamento(toStatoPagamento(versamento.getStato()));
		
		if(versamento.getImportoPagato()!= null) 
			verificaPagamento.setImportoTotalePagato(versamento.getImportoPagato());
		else
			verificaPagamento.setImportoTotalePagato(BigDecimal.ZERO);
		
		if(rpt != null) {
			verificaPagamento.setCcp(rpt.getCcp());
			verificaPagamento.setUrlPagamento(rpt.getPspRedirectURL());
		}
		if(rt != null) verificaPagamento.setRt(rt);
		
		
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			DatiSingoloPagamento datiSingoloPagamento = new DatiSingoloPagamento();
			if(sv.getDataEsitoSingoloPagamento() != null) datiSingoloPagamento.setData(sv.getDataEsitoSingoloPagamento());
			if(sv.getEsitoSingoloPagamento() != null) datiSingoloPagamento.setEsito(sv.getEsitoSingoloPagamento());
			if(sv.getSingoloImportoPagato()!= null) 
				datiSingoloPagamento.setImportoPagato(sv.getSingoloImportoPagato());
			else
				datiSingoloPagamento.setImportoPagato(BigDecimal.ZERO);
			if(sv.getIur() != null) datiSingoloPagamento.setIur(sv.getIur());
			if(sv.getCodSingoloVersamentoEnte() != null) datiSingoloPagamento.setIusv(sv.getCodSingoloVersamentoEnte());
			verificaPagamento.getDatiSingoloPagamentos().add(datiSingoloPagamento);
		}
		return verificaPagamento;
	}

	private static StatoPagamento toStatoPagamento(StatoVersamento stato) throws GovPayException {
		switch (stato) {
			case ANNULLATO: return StatoPagamento.ANNULLATO;
			case AUTORIZZATO_DIFFERITO: return StatoPagamento.IN_CORSO;
			case AUTORIZZATO_IMMEDIATO: return StatoPagamento.IN_CORSO;
			case AUTORIZZATO: return StatoPagamento.IN_CORSO;
			case IN_ATTESA: return StatoPagamento.IN_ERRORE;
			case DECORRENZA_TERMINI: return StatoPagamento.NON_ESEGUITO;
			case DECORRENZA_TERMINI_PARZIALE: return StatoPagamento.PARZIALMENTE_ESEGUITO;
			case PAGAMENTO_ESEGUITO: return StatoPagamento.ESEGUITO;
			case PAGAMENTO_NON_ESEGUITO: return StatoPagamento.NON_ESEGUITO;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO: return StatoPagamento.PARZIALMENTE_ESEGUITO;
			case IN_CORSO: return StatoPagamento.IN_CORSO;
			case FALLITO: return StatoPagamento.IN_ERRORE;
			case SCADUTO: return StatoPagamento.ANNULLATO;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "StatoVersamento non mappato in Stato Pagamento: " + stato.name());
	}

}
