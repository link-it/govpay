/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
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
package it.govpay.ndp.util;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingolaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.model.DistintaModel.EnumTipoFirma;
import it.govpay.ejb.model.EsitoRevocaDistinta;
import it.govpay.ejb.model.PagamentoModel.EnumStatoPagamento;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.EsitoPagamentoDistinta;
import it.govpay.ejb.model.EsitoPagamentoDistinta.EsitoSingoloPagamento;
import it.govpay.ejb.model.EsitoRevocaDistinta.EsitoSingolaRevoca;
import it.govpay.ejb.model.RevocaDistintaModel.EnumStatoRevoca;
import it.govpay.ejb.utils.DataTypeUtils;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.ndp.pojo.NdpFaultCode;
import it.govpay.ndp.util.exception.GovPayNdpException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

public class ValidatoreNdP {

	public enum StatoRicevuta {
		PagamentoEseguito,
		PagamentoNonEseguito,
		PagamentoParzialmenteEseguito,
		DecorrenzaTermini,
		DecorrenzaTerminiParziale;
	}

	
	public void validaDatiDominioEnte(String idDominio, String idIntermediario, String idStazioneIntermediario, DominioEnteModel dominioEnte) throws GovPayNdpException {
		if(idDominio == null || idDominio.compareTo(dominioEnte.getIdDominio()) != 0) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_DOMINIO_ERRATO, null);
		}

		if(idIntermediario == null || idIntermediario.compareTo(dominioEnte.getIntermediario().getIdIntermediarioPA()) != 0) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_INTERMEDIARIO_ERRATO, null);
		}

		if(idStazioneIntermediario == null || idStazioneIntermediario.compareTo(dominioEnte.getStazione().getIdStazioneIntermediarioPA()) != 0) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_INTERMEDIARIO_ERRATO, null);
		}		
	}

	public byte[] validaFirma(EnumTipoFirma firmaAttesa,  byte[] rt, String tipoFirma) throws GovPayNdpException {

		switch (firmaAttesa) {
		case CA_DES:
			if(tipoFirma.equals("1"))
				return validaFirmaCades(rt);
		case XA_DES:
			if(tipoFirma.equals("3"))
				return validaFirmaXades(rt);
		case AVANZATA:
			if(tipoFirma.equals("4"))
				return validaFirmaAvanzata(rt);
		default:
			if(tipoFirma == null || tipoFirma.isEmpty() || tipoFirma.equals("0"))
				return rt;
		}

		// Se arrivo qui, la ricevuta ha una firma diversa da quella richiesta nella RPT
		// TODO: Valutare la possibilita' di accettare la ricevuta se firmata.
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_FIRMA_ERRATA, "La firma non e' quella richiesta nella RPT");

	}

	private byte[] validaFirmaAvanzata(byte[] rt) throws GovPayNdpException {
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_FIRMA_ERRATA, "Firma Avanzata non supportata");
	}

	private byte[] validaFirmaXades(byte[] rt) throws GovPayNdpException {
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_FIRMA_ERRATA, "Firma XaDes non supportata");
	}

	private byte[] validaFirmaCades(byte[] rt) throws GovPayNdpException {		
		// TODO DA IMPLEMENTARE
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_FIRMA_ERRATA, "Firma CaDes non supportata");
	}

	public CtRicevutaTelematica validaXSD_RT(byte[] rt) throws GovPayNdpException {
		try {
			return (CtRicevutaTelematica) NdpUtils.toRT(rt);
		} catch (JAXBException e) {
			if(e.getCause() != null)
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SINTASSI_XSD, e.getCause().getMessage());
			else 
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SINTASSI_XSD, e.getMessage());
		} catch (SAXException e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, e.getMessage());
		}
	}
	
	public CtEsitoRevoca validaXSD_ER(byte[] er) throws GovPayNdpException {
		try {
			return (CtEsitoRevoca) NdpUtils.toER(er);
		} catch (JAXBException e) {
			if(e.getCause() != null)
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SINTASSI_XSD, e.getCause().getMessage());
			else 
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SINTASSI_XSD, e.getMessage());
		} catch (SAXException e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, e.getMessage());
		}
	}

	public EsitoPagamentoDistinta validaSemantica(CtRichiestaPagamentoTelematico rpt, CtRicevutaTelematica rt, List<PendenzaModel> pendenze) throws GovPayNdpException {
		
		if(!equals(rpt.getIdentificativoMessaggioRichiesta(), rt.getRiferimentoMessaggioRichiesta())) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, "RiferimentoMessaggioRichiesta non corrisponde all'RPT");
		}
		String errore = null;
		if( (errore = validaSemantica(rpt.getDominio(), rt.getDominio())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getEnteBeneficiario(), rt.getEnteBeneficiario()) )!= null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoPagatore(), rt.getSoggettoPagatore())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoVersante(), rt.getSoggettoVersante())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		
		CtDatiVersamentoRT datiVersamentoRT = rt.getDatiPagamento();

		EsitoPagamentoDistinta esitoDistinta = new EsitoPagamentoDistinta();
		esitoDistinta.setIdentificativoFiscaleCreditore(rt.getDominio().getIdentificativoDominio());
		esitoDistinta.setDataOraMessaggio(DataTypeUtils.xmlGregorianCalendartoDate(rt.getDataOraMessaggioRicevuta()));
		esitoDistinta.setIdentificativoMessaggio(rt.getIdentificativoMessaggioRicevuta());
		
		if ((errore = validaSemantica(esitoDistinta, rpt.getDatiVersamento(), datiVersamentoRT, pendenze)) != null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		
		rt.getIstitutoAttestante(); //TODO Questo?? Ce lo perdiamo?
		
		return esitoDistinta;
	}

	private String validaSemantica(EsitoPagamentoDistinta esitoDistinta, CtDatiVersamentoRPT rpt, CtDatiVersamentoRT rt, List<PendenzaModel> pendenze) throws GovPayNdpException {
		
		if (!equals(rpt.getCodiceContestoPagamento(), rt.getCodiceContestoPagamento())) 
			return "CodiceContestoPagamento non corrisponde all'RPT";
		

		if (!equals(rpt.getIdentificativoUnivocoVersamento(), rt.getIdentificativoUnivocoVersamento())) 
			return "IdentificativoUnivocoVersamento non corrisponde all'RPT";
		
		StatoRicevuta srt = validaSemanticaCodiceEsitoPagamento(rt.getCodiceEsitoPagamento());
		
		// Se siamo in pagamento eseguito, parzialmente eseguito o parzialmente decorso, devono esserci tanti versamenti quanti pagamenti.
		switch (srt) {
		case DecorrenzaTerminiParziale:
		case PagamentoEseguito:
		case PagamentoParzialmenteEseguito:
			if(rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + srt.name();
			break;
		case DecorrenzaTermini:
		case PagamentoNonEseguito:
			if(rt.getDatiSingoloPagamento().size() != 0 && rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di vagamenti diverso dal numero di versamenti per una ricevuta di tipo " + srt.name();
		}
		
		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;
		
		java.util.Collections.sort(pendenze, new PendenzaModel().new PendenzaComparator());
		for (int i = 0; i < rpt.getDatiSingoloVersamento().size(); i++) {
			
			CtDatiSingoloVersamentoRPT singoloVersamento = rpt.getDatiSingoloVersamento().get(i);
			CtDatiSingoloPagamentoRT singoloPagamento = null;
			if(rt.getDatiSingoloPagamento().size() != 0) {
				singoloPagamento = rt.getDatiSingoloPagamento().get(i);
				String validazione = validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento);
				if(validazione != null) return validazione;
			}
			
			PendenzaModel pendenza = pendenze.get(i);
			EsitoSingoloPagamento esitoPagamento = esitoDistinta.new EsitoSingoloPagamento();

			if (singoloPagamento == null) {
				esitoPagamento.setIdPagamentoEnte(pendenza.getCondizioniPagamento().get(0).getIdPagamentoEnte());
				esitoPagamento.setIdPendenza(pendenza.getIdPendenza());
				esitoPagamento.setDataPagamento(new Date());
				esitoPagamento.setDescrizioneEsito("Pagamento relativo a pendenza in stato " + srt.name());
				esitoPagamento.setIdRiscossionePSP("n/a");
				esitoPagamento.setImportoPagato(BigDecimal.ZERO);
				esitoPagamento.setStato(EnumStatoPagamento.NE);
			} else {
				esitoPagamento.setIdPagamentoEnte(pendenza.getCondizioniPagamento().get(0).getIdPagamentoEnte());
				esitoPagamento.setIdPendenza(pendenza.getIdPendenza());
				esitoPagamento.setDataPagamento(DataTypeUtils.xmlGregorianCalendartoDate(singoloPagamento.getDataEsitoSingoloPagamento()));
				esitoPagamento.setDescrizioneEsito(singoloPagamento.getEsitoSingoloPagamento());
				esitoPagamento.setIdRiscossionePSP(singoloPagamento.getIdentificativoUnivocoRiscossione());  
				esitoPagamento.setImportoPagato(singoloPagamento.getSingoloImportoPagato());
				BigDecimal importoVersamento = singoloVersamento.getImportoSingoloVersamento();
				BigDecimal importoPagato = singoloPagamento.getSingoloImportoPagato();
				if(importoVersamento.compareTo(importoPagato) == 0)
					esitoPagamento.setStato(EnumStatoPagamento.ES);
				else if(importoPagato.compareTo(BigDecimal.ZERO) == 0)
					esitoPagamento.setStato(EnumStatoPagamento.NE);
				else
					return "Importo Versamento non conforme alla richiesta di pagamento.";
			}
			
			importoTotaleCalcolato = importoTotaleCalcolato.add(esitoPagamento.getImportoPagato()); 
			esitoDistinta.getDatiSingoliPagamenti().add(esitoPagamento);
		}
		
		esitoDistinta.setIdTransazionePSP(rt.getCodiceContestoPagamento());
		esitoDistinta.setIuv(rt.getIdentificativoUnivocoVersamento());
		esitoDistinta.setDescrizioneStatoPSP(srt.toString());
		esitoDistinta.setStato(mapStatoRicevutaToStatoDistinta(srt));
		esitoDistinta.setImportoTotalePagato(rt.getImportoTotalePagato());

		if (importoTotaleCalcolato.compareTo(esitoDistinta.getImportoTotalePagato()) != 0)
			return "ImportoTotalePagato non corrisponde alla somma dei SingoliImportiPagati";
		if (srt == StatoRicevuta.PagamentoNonEseguito && esitoDistinta.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Non Eseguito'.";
		if (srt == StatoRicevuta.DecorrenzaTermini && esitoDistinta.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Decorrenza temini'.";
		if (srt == StatoRicevuta.PagamentoEseguito && esitoDistinta.getImportoTotalePagato().compareTo(rpt.getImportoTotaleDaVersare()) != 0)
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

	private StatoRicevuta validaSemanticaCodiceEsitoPagamento(String codiceEsitoPagamento) throws GovPayNdpException {
		try{
			switch (Integer.parseInt(codiceEsitoPagamento)) {
			case 0:
				return StatoRicevuta.PagamentoEseguito;
			case 1:
				return StatoRicevuta.PagamentoNonEseguito;
			case 2:
				return StatoRicevuta.PagamentoParzialmenteEseguito;
			case 3:
				return StatoRicevuta.DecorrenzaTermini;
			case 4:
				return StatoRicevuta.DecorrenzaTerminiParziale;
			default:
				break;
			} 
		} catch (Throwable e) {
		}
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, "CodiceEsitoPagamento sconosciuto");
	}

	private String validaSemantica(CtEnteBeneficiario rpt, CtEnteBeneficiario rt) {
		if(!equals(rpt.getDenominazioneBeneficiario(), rt.getDenominazioneBeneficiario())) return "DenominazioneBeneficiario non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco(), rt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoBeneficiario non corrisponde";
		if(!equals(rpt.getProvinciaBeneficiario(), rt.getProvinciaBeneficiario())) return "ProvinciaBeneficiario non corrisponde";
		if(!equals(rpt.getNazioneBeneficiario(), rt.getNazioneBeneficiario())) return "NazioneBeneficiario non corrisponde";
		return null;
	}
	
	public EsitoRevocaDistinta validaSemantica(CtRichiestaRevoca rr, CtEsitoRevoca er, List<PendenzaModel> pendenze) throws GovPayNdpException {
		if(!equals(rr.getIdentificativoMessaggioRevoca(), er.getRiferimentoMessaggioRevoca())) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, "RiferimentoMessaggioRevoca oggetto non corrispone all'IdentificativoMessaggioRevoca");
		}
	
		String errore = null;
		if( (errore = validaSemantica(rr.getDominio(), er.getDominio())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rr.getIstitutoAttestante(), er.getIstitutoAttestante()) )!= null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rr.getSoggettoPagatore(), er.getSoggettoPagatore())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		if( (errore = validaSemantica(rr.getSoggettoVersante(), er.getSoggettoVersante())) != null){
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		
		EsitoRevocaDistinta esitoDistinta = new EsitoRevocaDistinta();
		esitoDistinta.setIdentificativoFiscaleCreditore(rr.getDominio().getIdentificativoDominio());
		esitoDistinta.setDataOraEsitoRevoca(DataTypeUtils.xmlGregorianCalendartoDate(er.getDataOraMessaggioEsito()));
		esitoDistinta.setIdentificativoEsitoRevoca(er.getIdentificativoMessaggioEsito());
		
		if ((errore = validaSemantica(esitoDistinta, rr.getDatiRevoca(), er.getDatiRevoca(), pendenze)) != null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, errore);
		}
		
		return esitoDistinta;
	}
	
	private String validaSemantica(EsitoRevocaDistinta esitoRevoca, CtDatiRevoca rr, CtDatiEsitoRevoca er, List<PendenzaModel> pendenze) throws GovPayNdpException {
		
		if (!equals(rr.getCodiceContestoPagamento(), er.getCodiceContestoPagamento())) 
			return "CodiceContestoPagamento non corrisponde";
		

		if (!equals(rr.getIdentificativoUnivocoVersamento(), er.getIdentificativoUnivocoVersamento())) 
			return "IdentificativoUnivocoVersamento non corrisponde";
		
		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;
		
		java.util.Collections.sort(pendenze, new PendenzaModel().new PendenzaComparator());
		for (int i = 0; i < rr.getDatiSingolaRevoca().size(); i++) {
			
			CtDatiSingolaRevoca datiSingolaRevoca = rr.getDatiSingolaRevoca().get(i);
			CtDatiSingoloEsitoRevoca datiSingoloEsitoRevoca = null;
			if(er.getDatiSingolaRevoca().size() != 0) {
				datiSingoloEsitoRevoca = er.getDatiSingolaRevoca().get(i);
				String validazione = validaSemanticaSingolaRevoca(datiSingolaRevoca, datiSingoloEsitoRevoca);
				if(validazione != null) return validazione;
			}
			
			PendenzaModel pendenza = pendenze.get(i);
			EsitoSingolaRevoca esitoSingolaRevoca = esitoRevoca.new EsitoSingolaRevoca();

			if (datiSingoloEsitoRevoca == null) {
				return "DatiSingolaRevoca mancanti";
			} else {
				esitoSingolaRevoca.setCausaleEsito(datiSingoloEsitoRevoca.getCausaleEsito());
				esitoSingolaRevoca.setDatiAggiuntiviEsito(datiSingoloEsitoRevoca.getDatiAggiuntiviEsito());
				esitoSingolaRevoca.setIdPagamentoEnte(pendenza.getIdDebitoEnte());
				esitoSingolaRevoca.setIdRiscossionePSP(datiSingoloEsitoRevoca.getIdentificativoUnivocoRiscossione());
				esitoSingolaRevoca.setImportoRevocato(datiSingoloEsitoRevoca.getSingoloImportoRevocato());
				BigDecimal importoRevocato = esitoSingolaRevoca.getImportoRevocato();
				BigDecimal importoDaRevocare = datiSingolaRevoca.getSingoloImportoRevocato();
				if(importoRevocato.compareTo(importoDaRevocare) == 0)
					esitoSingolaRevoca.setStato(EnumStatoRevoca.ESEGUITO);
				else if(importoRevocato.compareTo(BigDecimal.ZERO) == 0)
					esitoSingolaRevoca.setStato(EnumStatoRevoca.NON_ESEGUITO);
				else
					return "Importo Revocato non conforme alla richiesta di revoca.";
			}
			
			importoTotaleCalcolato = importoTotaleCalcolato.add(esitoSingolaRevoca.getImportoRevocato()); 
			esitoRevoca.getDatiSingoleRevoche().add(esitoSingolaRevoca);
		}
		
		esitoRevoca.setIdTransazionePSP(er.getCodiceContestoPagamento());
		esitoRevoca.setIuv(er.getIdentificativoUnivocoVersamento());
		esitoRevoca.setImportoTotaleRevocato(er.getImportoTotaleRevocato());

		if (importoTotaleCalcolato.compareTo(esitoRevoca.getImportoTotaleRevocato()) != 0)
			return "ImportoTotalePagato non corrisponde alla somma dei SingoliImportiPagati";
		if(importoTotaleCalcolato.compareTo(rr.getImportoTotaleRevocato()) == 0)
			esitoRevoca.setStato(EnumStatoRevoca.ESEGUITO);
		else if(importoTotaleCalcolato.compareTo(BigDecimal.ZERO) == 0)
			esitoRevoca.setStato(EnumStatoRevoca.NON_ESEGUITO);
		else
			esitoRevoca.setStato(EnumStatoRevoca.PARZIALMENTE_ESEGUITO);

		
		return null;
	}

	private String validaSemanticaSingolaRevoca(CtDatiSingolaRevoca datiSingolaRevoca, CtDatiSingoloEsitoRevoca datiSingoloEsitoRevoca) {
		if(!equals(datiSingolaRevoca.getIdentificativoUnivocoRiscossione(), datiSingoloEsitoRevoca.getIdentificativoUnivocoRiscossione())) return "IdentificativoUnivocoRiscossione non corrisponde";
		if(datiSingoloEsitoRevoca.getSingoloImportoRevocato().compareTo(datiSingolaRevoca.getSingoloImportoRevocato()) != 0 && datiSingoloEsitoRevoca.getSingoloImportoRevocato().compareTo(BigDecimal.ZERO) != 0) {
			return "SingoloImportoRevocato non valido";
		}
		return null;
	}

	private String validaSemantica(CtIstitutoAttestante ist, CtIstitutoAttestante ist2) {
		if(ist == null && ist2 == null) return null;
		if(ist == null || ist2 == null) return "IstitutoAttestante non corriponde";

		if(!equals(ist.getCapAttestante(), ist2.getCapAttestante())) return "CapAttestante non corrisponde";
		if(!equals(ist.getCivicoAttestante(), ist2.getCivicoAttestante())) return "CivicoAttestante non corrisponde";
		if(!equals(ist.getCodiceUnitOperAttestante(), ist2.getCodiceUnitOperAttestante())) return "CodiceUnitOperAttestante non corrisponde";
		if(!equals(ist.getDenominazioneAttestante(), ist2.getDenominazioneAttestante())) return "DenominazioneAttestante non corrisponde";
		if(!equals(ist.getDenomUnitOperAttestante(), ist2.getDenomUnitOperAttestante())) return "DenomUnitOperAttestante non corrisponde";
		if(ist.getIdentificativoUnivocoAttestante() != null && ist.getIdentificativoUnivocoAttestante() != null) {
			if(!equals(ist.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco().name(), ist2.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco().name())) return "TipoIdentificativoUnivocoAttestante non corrisponde";
			if(!equals(ist.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco(), ist2.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco())) return "CodiceIdentificativoUnivocoAttestante non corrisponde";
		} 
		if(ist.getIdentificativoUnivocoAttestante() == null && ist.getIdentificativoUnivocoAttestante() != null) {
			return "IdentificativoUnivocoAttestante non corrisponde";
		}
		if(ist.getIdentificativoUnivocoAttestante() != null && ist.getIdentificativoUnivocoAttestante() == null) {
			return "IdentificativoUnivocoAttestante non corrisponde";
		}
		if(!equals(ist.getIndirizzoAttestante(), ist2.getIndirizzoAttestante())) return "IndirizzoAttestante non corrisponde";
		if(!equals(ist.getLocalitaAttestante(), ist2.getLocalitaAttestante())) return "LocalitaAttestante non corrisponde";
		if(!equals(ist.getNazioneAttestante(), ist2.getNazioneAttestante())) return "NazioneAttestante non corrisponde";
		if(!equals(ist.getProvinciaAttestante(), ist2.getProvinciaAttestante())) return "ProvinciaAttestante non corrisponde";

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
	
	
	private DistintaModel.EnumStatoDistinta mapStatoRicevutaToStatoDistinta(StatoRicevuta srt) {
		
		if (srt==null) 
			return null;
		
		DistintaModel.EnumStatoDistinta returnValue =null;
		
		switch (srt) {
		case DecorrenzaTermini:
			returnValue= EnumStatoDistinta.NON_ESEGUITO;
			break;
		case DecorrenzaTerminiParziale:
			returnValue= EnumStatoDistinta.PARZIALMENTE_ESEGUITO;
			break;
		case PagamentoEseguito:
			returnValue= EnumStatoDistinta.ESEGUITO;
			break;
		case PagamentoNonEseguito:
			returnValue= EnumStatoDistinta.NON_ESEGUITO;
			break;
		case PagamentoParzialmenteEseguito:
			returnValue= EnumStatoDistinta.PARZIALMENTE_ESEGUITO;
			break;
		}

		return returnValue;
		
	}
	
}
