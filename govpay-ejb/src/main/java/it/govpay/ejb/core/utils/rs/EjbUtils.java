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
package it.govpay.ejb.core.utils.rs;

import it.govpay.ejb.core.model.DestinatarioPendenzaModel;
import it.govpay.ejb.core.model.EsitoPagamentoDistinta;
import it.govpay.ejb.core.model.EsitoRevocaDistinta;
import it.govpay.ejb.core.model.SoggettoModel;
import it.govpay.ejb.core.model.VersanteModel;
import it.govpay.ejb.core.model.DestinatarioPendenzaModel.EnumTipoDestinatario;
import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.core.model.DistintaModel.EnumTipoAutenticazioneSoggetto;
import it.govpay.ejb.core.model.DistintaModel.EnumTipoFirma;
import it.govpay.ejb.core.model.EsitoPagamentoDistinta.EsitoSingoloPagamento;
import it.govpay.ejb.core.model.EsitoRevocaDistinta.EsitoSingolaRevoca;
import it.govpay.ejb.core.model.RevocaDistintaModel.EnumStatoRevoca;
import it.govpay.ejb.core.utils.DataTypeUtils;
import it.govpay.rs.DatiSingolaRevoca;
import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.rs.EsitoRevoca;
import it.govpay.rs.ModelloVersamento;
import it.govpay.rs.Psp;
import it.govpay.rs.Soggetto;
import it.govpay.rs.StatoPagamento;
import it.govpay.rs.StatoRevoca;
import it.govpay.rs.StatoSingolaRevoca;
import it.govpay.rs.TipoFirmaRicevuta;
import it.govpay.rs.TipoVersamento;
import it.govpay.rs.VerificaPagamento;

import java.util.ArrayList;
import java.util.List;

public class EjbUtils {

	
	public static VersanteModel toEjbSoggettoVersante(Soggetto soggetto) {
		VersanteModel s = new VersanteModel();
		
		fillSoggettoModel(soggetto,s);
		
		return s;
	}
	
	public static DestinatarioPendenzaModel toEjbSoggettoPagatore(Soggetto soggetto) {
		DestinatarioPendenzaModel s = new DestinatarioPendenzaModel();

		fillSoggettoModel(soggetto,s);
		
		if (s.getIdFiscale()!=null) {
			if (s.getIdFiscale().trim().length()==16) {
				s.setTipoDestinatario(EnumTipoDestinatario.CI);
			} else {
				s.setTipoDestinatario(EnumTipoDestinatario.AL);
			}
		}
		
		return s;
	}
	
	private static void fillSoggettoModel(Soggetto soggetto, SoggettoModel s) {
		s.setAnagrafica(soggetto.getAnagrafica());
		s.setCap(soggetto.getCap());
		s.setCivico(soggetto.getCivico());
		s.seteMail(soggetto.getEmail());
		s.setIdFiscale(soggetto.getIdentificativo());
		s.setIndirizzo(soggetto.getIndirizzo());
		s.setLocalita(soggetto.getLocalita());
		s.setNazione(soggetto.getNazione());
		s.setProvincia(soggetto.getProvincia());		
	}
	

	public static List<Psp> toWebPsp(List<it.govpay.ejb.core.model.GatewayPagamentoModel> pspList) {
		List<Psp> pList = new ArrayList<Psp>(); 
		for(it.govpay.ejb.core.model.GatewayPagamentoModel psp : pspList) {
			pList.add(toWebPsp(psp));
		}
		return pList;
	}
	
	public static Psp toWebPsp(it.govpay.ejb.core.model.GatewayPagamentoModel psp) {
		Psp p = new Psp();
		p.setCondizioniEconomicheMassime(psp.getImportoCommissioneMassima());
		p.setDescrizione(psp.getDescrizione());
		p.setDisponibilita(psp.getDisponibilitaServizio());
		p.setIdPsp(psp.getIdGateway());
		p.setInfoUrl(psp.getUrlInformazioniPsp());
		p.setRagioneSociale(psp.getDescrizioneGateway());
		p.setStornoGestito(psp.isStornoGestito());
		switch (psp.getModalitaPagamento()) {
		case ADDEBITO_DIRETTO:
			p.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);
			break;
		case BONIFICO_BANCARIO_TESORERIA:
			p.setTipoVersamento(TipoVersamento.BONIFICO_BANCARIO_TESORERIA);
			break;
		case BOLLETTINO_POSTALE:
			p.setTipoVersamento(TipoVersamento.BOLLETTINO_POSTALE);
			break;
		case CARTA_PAGAMENTO:
			p.setTipoVersamento(TipoVersamento.CARTA_PAGAMENTO);
			break;
		case ATTIVATO_PRESSO_PSP:
			p.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
			break;
		case MYBANK:
			p.setTipoVersamento(TipoVersamento.MY_BANK);
			break;
		}
		
		switch (psp.getModelloVersamento()) {
		case ATTIVATO_PRESSO_PSP:
			p.setModelloVersamento(ModelloVersamento.ATTIVATO_PRESSO_PSP);
			break;
		case DIFFERITO:
			p.setModelloVersamento(ModelloVersamento.DIFFERITO);
			break;
		case IMMEDIATO:
			p.setModelloVersamento(ModelloVersamento.IMMEDIATO);
			break;
		case IMMEDIATO_MULTIBENEFICIARIO:
			p.setModelloVersamento(ModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO);
			break;
		}
		return p;
	}

	public static EnumTipoAutenticazioneSoggetto toEjbAutorizzazioneSoggetto(
			it.govpay.rs.AutenticazioneSoggetto autenticazioneSoggetto) {
		switch (autenticazioneSoggetto) {
		case CNS:
			return EnumTipoAutenticazioneSoggetto.CNS;
		case OTH:
			return EnumTipoAutenticazioneSoggetto.OTH;
		case USR:
			return EnumTipoAutenticazioneSoggetto.USR;
		default: 
			return EnumTipoAutenticazioneSoggetto.N_A;
		}
	}

	public static EnumTipoFirma toEjbFirma(TipoFirmaRicevuta firma) {
		switch (firma) {
		case AVANZATA:
			return EnumTipoFirma.AVANZATA;
		case CA_DES:
			return EnumTipoFirma.CA_DES;
		case XA_DES:
			return EnumTipoFirma.XA_DES;
		default:
			return EnumTipoFirma.NESSUNA;
		}
	}
	

	public static VerificaPagamento toWebVerificaPagamento(EsitoPagamentoDistinta ricevuta) {
		VerificaPagamento verifica = new VerificaPagamento();
		verifica.setCcp(ricevuta.getIdTransazionePSP());
		verifica.setIdentificativoBeneficiario(ricevuta.getIdentificativoFiscaleCreditore());
		verifica.setImportoTotalePagato(ricevuta.getImportoTotalePagato());
		verifica.setIuv(ricevuta.getIuv());

		verifica.setStatoPagamento(EjbUtils.toWebStatoPagamento(ricevuta.getStato()));
		
		for(EsitoSingoloPagamento dp : ricevuta.getDatiSingoliPagamenti()) {
			DatiSingoloPagamento dsp = new DatiSingoloPagamento();
			dsp.setData(DataTypeUtils.dateToXmlGregorianCalendar(dp.getDataPagamento()));
			dsp.setEsito(dp.getDescrizioneEsito());
			dsp.setImportoPagato(dp.getImportoPagato());
			dsp.setIur(dp.getIdRiscossionePSP());
			dsp.setIusv(dp.getIdPagamentoEnte());
			verifica.getDatiSingoloPagamentos().add(dsp);
		}
		return verifica;
	}


	private static StatoPagamento toWebStatoPagamento(EnumStatoDistinta stato) {
		switch (stato) {
		case ANNULLATO:
		case ANNULLATO_OPE:
			return StatoPagamento.ANNULLATO;
		case ESEGUITO:
			return StatoPagamento.ESEGUITO;
		case ESEGUITO_SBF:
		case IN_CORSO:	
			return StatoPagamento.IN_CORSO;
		case IN_ERRORE:
			return StatoPagamento.IN_ERRORE;
		case NON_ESEGUITO:
			return StatoPagamento.NON_ESEGUITO;
		case PARZIALMENTE_ESEGUITO:
			return StatoPagamento.PARZIALMENTE_ESEGUITO;
		case STORNATO:
			return StatoPagamento.STORNATO;
		}
		return null;
	}
	
	private static StatoRevoca toWebStatoRevoca(EnumStatoRevoca stato) {
		switch (stato) {
		case ESEGUITO:
			return StatoRevoca.ESEGUITO;
		case ESEGUITO_SBF:
		case IN_CORSO:	
			return StatoRevoca.IN_CORSO;
		case IN_ERRORE:
			return StatoRevoca.IN_ERRORE;
		case NON_ESEGUITO:
			return StatoRevoca.NON_ESEGUITO;
		case PARZIALMENTE_ESEGUITO:
			return StatoRevoca.PARZIALMENTE_ESEGUITO;
		}
		return null;
	}
	
	private static StatoSingolaRevoca toWebStatoSingolaRevoca(EnumStatoRevoca stato) {
		switch (stato) {
		case ESEGUITO:
			return StatoSingolaRevoca.ESEGUITO;
		case NON_ESEGUITO:
			return StatoSingolaRevoca.NON_ESEGUITO;
		default:
			// La singola revoca ha solo questi due stati....
			break;
		}
		return null;
	}

	public static EsitoRevoca toWebEsitoRevoca(EsitoRevocaDistinta esitoRevocaDistinta) {
		EsitoRevoca esitoRevoca = new EsitoRevoca();
		esitoRevoca.setCcp(esitoRevocaDistinta.getIdTransazionePSP());
		esitoRevoca.setIdentificativoBeneficiario(esitoRevocaDistinta.getIdentificativoFiscaleCreditore());
		esitoRevoca.setImportoTotaleRevocato(esitoRevocaDistinta.getImportoTotaleRevocato());
		esitoRevoca.setIuv(esitoRevocaDistinta.getIuv());
		esitoRevoca.setStato(EjbUtils.toWebStatoRevoca(esitoRevocaDistinta.getStato()));
		
		for(EsitoSingolaRevoca dp : esitoRevocaDistinta.getDatiSingoleRevoche()) {
			DatiSingolaRevoca dsr = new DatiSingolaRevoca();
			dsr.setStato(toWebStatoSingolaRevoca(dp.getStato()));
			dsr.setImportoRevocato(dp.getImportoRevocato());
			dsr.setIur(dp.getIdRiscossionePSP());
			dsr.setIusv(dp.getIdPagamentoEnte());
			esitoRevoca.getDatiSingolaRevocas().add(dsr);
		}
		return esitoRevoca;
	}

	
}
