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
package it.govpay.utils;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.exception.GovPayNdpException;

import java.math.BigDecimal;

public class ValidatoreRT {

	public enum StatoRicevuta {
		PagamentoEseguito,
		PagamentoNonEseguito,
		PagamentoParzialmenteEseguito,
		DecorrenzaTermini,
		DecorrenzaTerminiParziale;
	}

	public byte[] validaFirma(FirmaRichiesta tipoFirma,  byte[] rt) throws GovPayNdpException {

		switch (tipoFirma) {
		case CA_DES:
			if(tipoFirma.equals("1"))
				return validaFirmaCades(rt);
		case XA_DES:
			if(tipoFirma.equals("3"))
				return validaFirmaXades(rt);
		case AVANZATA:
			if(tipoFirma.equals("4"))
				return validaFirmaAvanzata(rt);
		case NESSUNA:
			return rt;
		}
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_FIRMA_ERRATA, "La firma non e' quella richiesta nella RPT");
	}

	private byte[] validaFirmaAvanzata(byte[] rt) throws GovPayNdpException {
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_FIRMA_ERRATA, "Firma Avanzata non supportata");
	}

	private byte[] validaFirmaXades(byte[] rt) throws GovPayNdpException {
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_FIRMA_ERRATA, "Firma XaDes non supportata");
	}

	private byte[] validaFirmaCades(byte[] rt) throws GovPayNdpException {		
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_FIRMA_ERRATA, "Firma CaDes non supportata");
	}

	public void validaSemantica(CtRichiestaPagamentoTelematico rpt, CtRicevutaTelematica rt) throws GovPayNdpException {

		if(!equals(rpt.getIdentificativoMessaggioRichiesta(), rt.getRiferimentoMessaggioRichiesta())) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, "RiferimentoMessaggioRichiesta non corrisponde all'RPT");
		}
		String errore = null;
		if( (errore = validaSemantica(rpt.getDominio(), rt.getDominio())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getEnteBeneficiario(), rt.getEnteBeneficiario()) )!= null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoPagatore(), rt.getSoggettoPagatore())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoVersante(), rt.getSoggettoVersante())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, errore);
		}

		CtDatiVersamentoRT datiVersamentoRT = rt.getDatiPagamento();

		if ((errore = validaSemantica(rpt.getDatiVersamento(), datiVersamentoRT)) != null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, errore);
		}
	}

	private String validaSemantica(CtDatiVersamentoRPT rpt, CtDatiVersamentoRT rt) throws GovPayNdpException {

		if (!equals(rpt.getCodiceContestoPagamento(), rt.getCodiceContestoPagamento())) 
			return "CodiceContestoPagamento non corrisponde all'RPT";


		if (!equals(rpt.getIdentificativoUnivocoVersamento(), rt.getIdentificativoUnivocoVersamento())) 
			return "IdentificativoUnivocoVersamento non corrisponde all'RPT";

		Rt.EsitoPagamento esitoPagamento = validaSemanticaCodiceEsitoPagamento(rt.getCodiceEsitoPagamento());

		// Se siamo in pagamento eseguito, parzialmente eseguito o parzialmente decorso, devono esserci tanti versamenti quanti pagamenti.
		switch (esitoPagamento) {
		case DECORRENZA_TERMINI_PARZIALE:
		case PAGAMENTO_ESEGUITO:
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			if(rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name();
			break;
		case DECORRENZA_TERMINI:
		case PAGAMENTO_NON_ESEGUITO:
			if(rt.getDatiSingoloPagamento().size() != 0 && rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name();
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < rpt.getDatiSingoloVersamento().size(); i++) {

			CtDatiSingoloVersamentoRPT singoloVersamento = rpt.getDatiSingoloVersamento().get(i);
			CtDatiSingoloPagamentoRT singoloPagamento = null;
			if(rt.getDatiSingoloPagamento().size() != 0) {
				singoloPagamento = rt.getDatiSingoloPagamento().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getSingoloImportoPagato());
			}
		}

		if (importoTotaleCalcolato.compareTo(rt.getImportoTotalePagato()) != 0)
			return "ImportoTotalePagato non corrisponde alla somma dei SingoliImportiPagati";
		if (esitoPagamento == Rt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Non Eseguito'.";
		if (esitoPagamento == Rt.EsitoPagamento.DECORRENZA_TERMINI && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Decorrenza temini'.";
		if (esitoPagamento == Rt.EsitoPagamento.PAGAMENTO_ESEGUITO && rt.getImportoTotalePagato().compareTo(rpt.getImportoTotaleDaVersare()) != 0)
			return "ImportoTotalePagato diverso dall'ImportoTotaleDaVersare per un pagamento con esito 'Eseguito'.";
		return null;
	}

	private String validaSemanticaSingoloVersamento(CtDatiSingoloVersamentoRPT singoloVersamento, CtDatiSingoloPagamentoRT singoloPagamento) {
		if(!equals(singoloPagamento.getCausaleVersamento(), singoloVersamento.getCausaleVersamento())) return "CausaleVersamento del pagamento non corrisponde a quella nell'RPT.";
		if(!equals(singoloPagamento.getDatiSpecificiRiscossione(), singoloVersamento.getDatiSpecificiRiscossione())) return "DatiSpecificiRiscossione del pagamento non corrisponde a quella nell'RPT.";

		if(singoloPagamento.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0) {
			if(singoloPagamento.getEsitoSingoloPagamento() == null || singoloPagamento.getEsitoSingoloPagamento().isEmpty()) return "EsitoSingoloPagamento obbligatorio per pagamenti non eseguiti.";
			if(!singoloPagamento.getIdentificativoUnivocoRiscossione().equals("n/a")) return "IdentificativoUnivocoRiscossione deve essere n/a per pagamenti non eseguiti.";
		} else if(singoloPagamento.getSingoloImportoPagato().compareTo(singoloVersamento.getImportoSingoloVersamento()) != 0) {
			return "SingoloImportoPagato di un pagamento eseguito non corrisponde all'RPT";
		}

		return null;
	}

	private Rt.EsitoPagamento validaSemanticaCodiceEsitoPagamento(String codiceEsitoPagamento) throws GovPayNdpException {
		try{
			switch (Integer.parseInt(codiceEsitoPagamento)) {
			case 0:
				return Rt.EsitoPagamento.PAGAMENTO_ESEGUITO;
			case 1:
				return Rt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
			case 2:
				return Rt.EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
			case 3:
				return Rt.EsitoPagamento.DECORRENZA_TERMINI;
			case 4:
				return Rt.EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
			default:
				break;
			} 
		} catch (Throwable e) {
		}
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, Rt.FaultPa.PAA_SEMANTICA, "CodiceEsitoPagamento sconosciuto");
	}

	private String validaSemantica(CtEnteBeneficiario rpt, CtEnteBeneficiario rt) {
		if(!equals(rpt.getDenominazioneBeneficiario(), rt.getDenominazioneBeneficiario())) return "DenominazioneBeneficiario non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco(), rt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoBeneficiario non corrisponde";
		if(!equals(rpt.getProvinciaBeneficiario(), rt.getProvinciaBeneficiario())) return "ProvinciaBeneficiario non corrisponde";
		if(!equals(rpt.getNazioneBeneficiario(), rt.getNazioneBeneficiario())) return "NazioneBeneficiario non corrisponde";
		return null;
	}

	private String validaSemantica(CtDominio rpt, CtDominio rt) {
		if(!equals(rpt.getIdentificativoDominio(),rt.getIdentificativoDominio())) return "IdentificativoDominio non corrisponde";
		if(!equals(rpt.getIdentificativoStazioneRichiedente(),rt.getIdentificativoStazioneRichiedente())) return "IdentificativoStazioneRichiedente non corrisponde";
		return null;
	}

	private String validaSemantica(CtSoggettoVersante rpt, CtSoggettoVersante rt) {
		if(rpt == null && rt == null) return null;
		if(rpt == null || rt == null) return "SoggettoVersante non corriponde";

		if(!equals(rpt.getAnagraficaVersante(),rt.getAnagraficaVersante())) return "AnagraficaVersante non corrisponde";
		if(!equals(rpt.getCapVersante(),rt.getCapVersante())) return "CapVersante non corrisponde";
		if(!equals(rpt.getCivicoVersante(),rt.getCivicoVersante())) return "CivicoVersante non corrisponde";
		if(!equals(rpt.getEMailVersante(),rt.getEMailVersante())) return "EMailVersante non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoVersante non corrisponde";
		if(!equals(rpt.getIndirizzoVersante(),rt.getIndirizzoVersante())) return "IndirizzoVersante non corrisponde";
		if(!equals(rpt.getLocalitaVersante(),rt.getLocalitaVersante())) return "LocaltaVersante non corrisponde";
		if(!equals(rpt.getNazioneVersante(),rt.getNazioneVersante())) return "NazioneVersante non corrisponde";
		if(!equals(rpt.getProvinciaVersante(),rt.getProvinciaVersante())) return "ProvinciaVersante non corrisponde";
		return null;
	}

	private String validaSemantica(CtSoggettoPagatore rpt, CtSoggettoPagatore rt) {
		if(!equals(rpt.getAnagraficaPagatore(),rt.getAnagraficaPagatore())) return "AnagraficaPagatore non corrisponde";
		if(!equals(rpt.getCapPagatore(),rt.getCapPagatore())) return "CapPagatore non corrisponde";
		if(!equals(rpt.getCivicoPagatore(),rt.getCivicoPagatore())) return "CivicoPagatore non corrisponde";
		if(!equals(rpt.getEMailPagatore(),rt.getEMailPagatore())) return "EMailPagatore non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoPagatore non corrisponde";
		if(!equals(rpt.getIndirizzoPagatore(),rt.getIndirizzoPagatore())) return "IndirizzoPagatore non corrisponde";
		if(!equals(rpt.getLocalitaPagatore(),rt.getLocalitaPagatore())) return "LocaltaPagatore non corrisponde";
		if(!equals(rpt.getNazionePagatore(),rt.getNazionePagatore())) return "NazionePagatore non corrisponde";
		if(!equals(rpt.getProvinciaPagatore(),rt.getProvinciaPagatore())) return "ProvinciaPagatore non corrisponde";
		return null;
	}

	private boolean equals(String s1, String s2) {
		if(s1==null && s2==null) return true;
		if(s1==null) return false;
		return s1.compareTo(s2)==0;
	}



}
