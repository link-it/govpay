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
package it.govpay.web.ws.utils;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Versamento;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.servizi.pa.CodErrore;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.GpChiediStatoPagamentoResponse;
import it.govpay.servizi.pa.PaInviaEsitoPagamento;
import it.govpay.servizi.pa.Pagamento;
import it.govpay.servizi.pa.StatoPagamento;
import it.govpay.servizi.pa.StatoSingoloPagamento;
import it.govpay.servizi.pa.TipoVersamento;
import it.govpay.utils.JaxbUtils;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

public class PagamentiTelematiciGPUtil {

	public static CodErrore toDescrizioneEsito(GovPayExceptionEnum gpEnum) {
		switch (gpEnum) {
		case APPLICAZIONE_DISABILITATA:
			return CodErrore.ERRORE_AUTORIZZAZIONE;
		case ERRORE_AUTENTICAZIONE:
			return CodErrore.ERRORE_AUTENTICAZIONE;
		case ERRORE_AUTORIZZAZIONE:
			return CodErrore.ERRORE_AUTORIZZAZIONE;
		case APPLICAZIONE_NON_TROVATA:
			return CodErrore.ERRORE_SEMANTICA;
		case IUV_NON_TROVATO:
			return CodErrore.PAGAMENTO_NON_TROVATO;
		case PSP_NON_TROVATO:
			return CodErrore.ERRORE_SEMANTICA;
		default:
			return CodErrore.ERRORE_INTERNO;
		}
	}

	public static Versamento toVersamento(Pagamento richiesta, Ente ente, Dominio dominio, Applicazione applicazione, String iuv, BasicBD bd) throws GovPayException {
		Versamento versamento = new Versamento();
		versamento.setAnagraficaDebitore(toOrm(richiesta.getDebitore()));
		versamento.setCodDominio(dominio.getCodDominio());
		versamento.setIuv(iuv);
		versamento.setDataScadenza(richiesta.getDataScadenza());
		versamento.setImportoTotale(richiesta.getImportoTotale());
		versamento.setIdApplicazione(applicazione.getId());
		versamento.setIdEnte(ente.getId());
		versamento.setCodVersamentoEnte(iuv);
		int indice = 0;
		for(it.govpay.servizi.pa.SingoloPagamento sv : richiesta.getSingoloPagamento()) {
			Tributo tributo;
			try {
				tributo = AnagraficaManager.getTributo(bd, ente.getId(), sv.getCodTributo());
			} catch (NotFoundException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Il tributo non e' censito o non e' associato all'applicazione chiedente");
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			SingoloVersamento singoloVersamento = new SingoloVersamento();
			singoloVersamento.setAnnoRiferimento(sv.getAnnoRiferimento());
			singoloVersamento.setCodSingoloVersamentoEnte(sv.getCodVersamentoEnte());
			singoloVersamento.setIdTributo(tributo.getId());
			singoloVersamento.setDatiSpecificiRiscossione(tributo.getTipoContabilita() + "/" + tributo.getCodContabilita());
			singoloVersamento.setIndice(indice);
			singoloVersamento.setCausaleVersamento(richiesta.getCausale());
			singoloVersamento.setImportoSingoloVersamento(sv.getImporto());
			singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
			versamento.getSingoliVersamenti().add(singoloVersamento);
			indice++;
		}
		versamento.setStato(StatoVersamento.IN_ATTESA);
		return versamento;
	}

	public static it.govpay.bd.model.Rpt.TipoVersamento toOrm(TipoVersamento tipoVersamento) throws GovPayException {
		switch (tipoVersamento) {
			case AD: return it.govpay.bd.model.Rpt.TipoVersamento.ADDEBITO_DIRETTO;
			case BBT: return it.govpay.bd.model.Rpt.TipoVersamento.BONIFICO_BANCARIO_TESORERIA;
			case BP: return it.govpay.bd.model.Rpt.TipoVersamento.BOLLETTINO_POSTALE;
			case CP: return it.govpay.bd.model.Rpt.TipoVersamento.CARTA_PAGAMENTO;
			case OBEP: return it.govpay.bd.model.Rpt.TipoVersamento.MYBANK;
			case PO: return it.govpay.bd.model.Rpt.TipoVersamento.ATTIVATO_PRESSO_PSP;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	public static Anagrafica toOrm(it.govpay.servizi.pa.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Anagrafica a = new Anagrafica();
		a.setCap(anagrafica.getCap());
		a.setCivico(anagrafica.getCivico());
		a.setCodUnivoco(anagrafica.getCodUnivoco());
		a.setEmail(anagrafica.getEmail());
		a.setFax(anagrafica.getFax());
		a.setIndirizzo(anagrafica.getIndirizzo());
		a.setLocalita(anagrafica.getLocalita());
		a.setNazione(anagrafica.getNazione());
		a.setProvincia(anagrafica.getProvincia());
		a.setRagioneSociale(anagrafica.getRagioneSociale());
		a.setTelefono(anagrafica.getTelefono());
		a.setCellulare(anagrafica.getCellulare());
		return a;
	}

	public static Autenticazione toOrm(it.govpay.servizi.pa.Autenticazione autenticazione) throws GovPayException {
		switch (autenticazione) {
			case CNS: return Autenticazione.CNS;
			case N_A: return Autenticazione.N_A;
			case OTH: return Autenticazione.OTH;
			case USR: return Autenticazione.USR;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	public static it.govpay.bd.model.Rpt.TipoVersamento toTipoVersamento(TipoVersamento tipoVersamento) throws GovPayException {
		switch (tipoVersamento) {
			case AD: return it.govpay.bd.model.Rpt.TipoVersamento.ADDEBITO_DIRETTO;
			case BBT: return it.govpay.bd.model.Rpt.TipoVersamento.BONIFICO_BANCARIO_TESORERIA;
			case BP: return it.govpay.bd.model.Rpt.TipoVersamento.BOLLETTINO_POSTALE;
			case CP: return it.govpay.bd.model.Rpt.TipoVersamento.CARTA_PAGAMENTO;
			case OBEP: return it.govpay.bd.model.Rpt.TipoVersamento.MYBANK;
			case PO: return it.govpay.bd.model.Rpt.TipoVersamento.ATTIVATO_PRESSO_PSP;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	public static GpChiediStatoPagamentoResponse toGpChiediStatoPagamentoResponse(
			BasicBD basicBD,
			GpChiediStatoPagamentoResponse esitoOperazione,
			Applicazione applicazione, Ente ente, Dominio dominio,
			Versamento versamento, Rpt rpt, byte[] rtByte) throws ServiceException, NotFoundException, MultipleResultException, GovPayException {
		
		esitoOperazione.setCodApplicazione(applicazione.getCodApplicazione());
		esitoOperazione.setCodEnte(ente.getCodEnte());
		esitoOperazione.setCodEsito(CodEsito.OK);
		
		GpChiediStatoPagamentoResponse.Pagamento p = new GpChiediStatoPagamentoResponse.Pagamento();
		p.setCausale(applicazione.getCodApplicazione());
		p.setDataScadenza(versamento.getDataScadenza());
		p.setDebitore(toAnagrafica(versamento.getAnagraficaDebitore()));
		
		if(versamento.getImportoPagato() != null) 
			p.setImportoTotale(versamento.getImportoPagato());
		else
			p.setImportoTotale(BigDecimal.ZERO);
		
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			GpChiediStatoPagamentoResponse.Pagamento.SingoloPagamento datiSingoloPagamento = new GpChiediStatoPagamentoResponse.Pagamento.SingoloPagamento();
			datiSingoloPagamento.setAnnoRiferimento(sv.getAnnoRiferimento());
			datiSingoloPagamento.setCodTributo(AnagraficaManager.getTributo(basicBD, sv.getIdTributo()).getCodTributo());
			datiSingoloPagamento.setCodVersamentoEnte(sv.getCodSingoloVersamentoEnte());
			datiSingoloPagamento.setImporto(sv.getImportoSingoloVersamento());
			datiSingoloPagamento.setImportoPagato(sv.getSingoloImportoPagato());
			datiSingoloPagamento.setIur(sv.getIur());
			datiSingoloPagamento.setStato(toStatoSingoloPagamento(sv.getStatoSingoloVersamento()));
			p.getSingoloPagamento().add(datiSingoloPagamento);
		}
		
		esitoOperazione.setPagamento(p);
		esitoOperazione.setStato(toStatoPagamento(versamento.getStato()));
		esitoOperazione.setXmlRT(rtByte);
		
		return esitoOperazione;
	}

	private static StatoSingoloPagamento toStatoSingoloPagamento(StatoSingoloVersamento statoSingoloVersamento) throws GovPayException {
		switch (statoSingoloVersamento) {
		case DA_PAGARE:
			return StatoSingoloPagamento.DA_PAGARE;
		case PAGATO:
			return StatoSingoloPagamento.PAGATO;
		case RENDICONTATO:
			return StatoSingoloPagamento.RENDICONTATO;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	private static it.govpay.servizi.pa.Anagrafica toAnagrafica(Anagrafica anagraficaDebitore) {
		it.govpay.servizi.pa.Anagrafica a = new it.govpay.servizi.pa.Anagrafica();
		a.setCap(anagraficaDebitore.getCap());
		a.setCellulare(anagraficaDebitore.getCellulare());
		a.setCivico(anagraficaDebitore.getCivico());
		a.setCodUnivoco(anagraficaDebitore.getCodUnivoco());
		a.setEmail(anagraficaDebitore.getEmail());
		a.setFax(anagraficaDebitore.getFax());
		a.setIndirizzo(anagraficaDebitore.getIndirizzo());
		a.setLocalita(anagraficaDebitore.getLocalita());
		a.setNazione(anagraficaDebitore.getNazione());
		a.setProvincia(anagraficaDebitore.getProvincia());
		a.setRagioneSociale(anagraficaDebitore.getRagioneSociale());
		a.setTelefono(anagraficaDebitore.getTelefono());
		return a;
	}

	private static StatoPagamento toStatoPagamento(StatoVersamento stato) throws GovPayException {
		switch (stato) {
		case ANNULLATO:
			return StatoPagamento.ANNULLATO;
		case AUTORIZZATO:
			return StatoPagamento.AUTORIZZATO_DIFFERITO;
		case AUTORIZZATO_DIFFERITO:
			return StatoPagamento.AUTORIZZATO_DIFFERITO;
		case AUTORIZZATO_IMMEDIATO:
			return StatoPagamento.AUTORIZZATO_IMMEDIATO;
		case DECORRENZA_TERMINI:
			return StatoPagamento.DECORRENZA_TERMINI;
		case DECORRENZA_TERMINI_PARZIALE:
			return StatoPagamento.DECORRENZA_TERMINI_PARZIALE;
		case FALLITO:
			return StatoPagamento.FALLITO;
		case IN_ATTESA:
			return StatoPagamento.IN_ATTESA;
		case IN_CORSO:
			return StatoPagamento.IN_CORSO;
		case PAGAMENTO_ESEGUITO:
			return StatoPagamento.PAGAMENTO_ESEGUITO;
		case PAGAMENTO_NON_ESEGUITO:
			return StatoPagamento.PAGAMENTO_NON_ESEGUITO;
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			return StatoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
		case SCADUTO:
			return StatoPagamento.SCADUTO;
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
	}

	public static byte[] toEsitoPagamento(BasicBD bd, Applicazione applicazione, Versamento versamento, Rpt rpt, byte[] ctRtByte) throws GovPayException, ServiceException, NotFoundException, MultipleResultException, JAXBException, SAXException {
		PaInviaEsitoPagamento req = new PaInviaEsitoPagamento();
		
		PaInviaEsitoPagamento.Pagamento p = new PaInviaEsitoPagamento.Pagamento();
		p.setCausale(applicazione.getCodApplicazione());
		p.setDataScadenza(versamento.getDataScadenza());
		p.setDebitore(toAnagrafica(versamento.getAnagraficaDebitore()));
		
		if(versamento.getImportoPagato() != null) 
			p.setImportoTotale(versamento.getImportoPagato());
		else
			p.setImportoTotale(BigDecimal.ZERO);
		
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			PaInviaEsitoPagamento.Pagamento.SingoloPagamento datiSingoloPagamento = new PaInviaEsitoPagamento.Pagamento.SingoloPagamento();
			datiSingoloPagamento.setAnnoRiferimento(sv.getAnnoRiferimento());
			datiSingoloPagamento.setCodTributo(AnagraficaManager.getTributo(bd, sv.getIdTributo()).getCodTributo());
			datiSingoloPagamento.setCodVersamentoEnte(sv.getCodSingoloVersamentoEnte());
			datiSingoloPagamento.setImporto(sv.getImportoSingoloVersamento());
			datiSingoloPagamento.setImportoPagato(sv.getSingoloImportoPagato());
			datiSingoloPagamento.setIur(sv.getIur());
			datiSingoloPagamento.setStato(toStatoSingoloPagamento(sv.getStatoSingoloVersamento()));
			p.getSingoloPagamento().add(datiSingoloPagamento);
		}
		
		req.setPagamento(p);
		req.setStato(toStatoPagamento(versamento.getStato()));
		req.setXmlRT(ctRtByte);
		
		req.setPagamento(p);
		
		byte[] payload = JaxbUtils.toByte(req);
		return xml2soap(payload);
	}
	
	public static byte[] xml2soap(byte[] xml) {
		String head = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body>";
		xml = ArrayUtils.addAll(head.getBytes(), xml);
		String tail = "</soapenv:Body></soapenv:Envelope>";
		return ArrayUtils.addAll(xml, tail.getBytes());
	}
}
