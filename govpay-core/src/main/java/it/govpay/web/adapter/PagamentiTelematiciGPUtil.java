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

import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.CtNumeroAvviso;
import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.InformazioniVersamento;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.SingolaRendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentoFilter;
import it.govpay.bd.pagamento.VersamentoFilter.SortFields;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.servizi.pa.CodErrore;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.GpCercaVersamentiRequest;
import it.govpay.servizi.pa.GpChiediStatoPagamentoResponse;
import it.govpay.servizi.pa.IdPagamento;
import it.govpay.servizi.pa.PaInviaEsitoPagamento;
import it.govpay.servizi.pa.Pagamento;
import it.govpay.servizi.pa.StatoPagamento;
import it.govpay.servizi.pa.StatoSingoloPagamento;
import it.govpay.servizi.pa.TipoVersamento;
import it.govpay.utils.GovPayConfiguration;
import it.govpay.utils.JaxbUtils;
import it.govpay.utils.NdpUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

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

		esitoOperazione.setVersamento(toVersamento(basicBD, applicazione.getCodApplicazione(), versamento, rtByte));
		return esitoOperazione;
	}

	public static it.govpay.servizi.pa.Versamento toVersamento(
			BasicBD basicBD,
			String codApplicazione, 
			Versamento versamento, byte[] rtByte) throws ServiceException, NotFoundException, MultipleResultException, GovPayException {

		it.govpay.servizi.pa.Versamento.Pagamento p = new it.govpay.servizi.pa.Versamento.Pagamento();
		p.setCausale(codApplicazione);
		p.setDataScadenza(versamento.getDataScadenza());
		p.setDebitore(toAnagrafica(versamento.getAnagraficaDebitore()));

		Ente ente = AnagraficaManager.getEnte(basicBD, versamento.getIdEnte());

		IdPagamento idPagamento = new IdPagamento();
		idPagamento.setCodEnte(ente.getCodEnte());
		idPagamento.setIuv(versamento.getIuv());

		p.setIdPagamento(idPagamento);

		if(versamento.getImportoTotale() != null) 
			p.setImportoTotale(versamento.getImportoTotale());
		else
			p.setImportoTotale(BigDecimal.ZERO);

		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			it.govpay.servizi.pa.Versamento.Pagamento.SingoloPagamento datiSingoloPagamento = new it.govpay.servizi.pa.Versamento.Pagamento.SingoloPagamento();
			datiSingoloPagamento.setAnnoRiferimento(sv.getAnnoRiferimento());
			datiSingoloPagamento.setCodTributo(AnagraficaManager.getTributo(basicBD, sv.getIdTributo()).getCodTributo());
			datiSingoloPagamento.setCodVersamentoEnte(sv.getCodSingoloVersamentoEnte());
			datiSingoloPagamento.setImporto(sv.getImportoSingoloVersamento());
			datiSingoloPagamento.setImportoPagato(sv.getSingoloImportoPagato());
			datiSingoloPagamento.setIur(sv.getIur());
			datiSingoloPagamento.setStato(toStatoSingoloPagamento(sv.getStatoSingoloVersamento()));
			p.getSingoloPagamento().add(datiSingoloPagamento);
		}


		it.govpay.servizi.pa.Versamento v = new it.govpay.servizi.pa.Versamento();
		v.setPagamento(p);
		v.setStato(toStatoPagamento(versamento.getStato()));
		v.setXmlRT(rtByte);

		return v;
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
		IdPagamento idPagamento = new IdPagamento();
		idPagamento.setCodEnte(AnagraficaManager.getEnte(bd, versamento.getIdEnte()).getCodEnte());
		idPagamento.setIuv(versamento.getIuv());
		p.setIdPagamento(idPagamento);
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

	public static it.govpay.servizi.pa.Canale toCanale(Canale canale) {
		it.govpay.servizi.pa.Canale c = new it.govpay.servizi.pa.Canale();
		c.setCodPsp(canale.getPsp().getCodPsp());
		c.setCodCanale(canale.getCodCanale());
		return c;
	}

	public static String buildQrCodeTxt(String codDominio, int applicationCode, String iuv, BigDecimal importoTotale) throws JAXBException, SAXException {
		InformazioniVersamento info = new InformazioniVersamento();
		info.setCodiceIdentificativoEnte(codDominio);
		info.setImportoVersamento(importoTotale);
		CtNumeroAvviso numeroAvviso = new CtNumeroAvviso();
		numeroAvviso.setApplicationCode("" + applicationCode);
		numeroAvviso.setAuxDigit("0");
		numeroAvviso.setIUV(iuv);
		info.setNumeroAvviso(numeroAvviso);
		byte[] infoByte = JaxbUtils.toByte(info);
		return new String(infoByte);
	}

	public static String buildBarCodeTxt(String gln, int applicationCode, String iuv, BigDecimal importoTotale) {
		// Da Guida Tecnica di Adesione PA 3.8 pag 25 
		String payToLoc = "415";
		String refNo = "8020";
		String amount = "3902";
		String importo = NdpUtils.nFormatter.format(importoTotale).replace(".", "");
		return payToLoc + gln + refNo + "0" + applicationCode + iuv + amount + importo;
	}

	public static List<it.govpay.servizi.pa.Versamento> findVersamentiByFilter(GpCercaVersamentiRequest bodyrichiesta,
			BasicBD bd) throws ServiceException, GovPayException {
		VersamentiBD versamentiBD = new VersamentiBD(bd);

		VersamentoFilter filter = versamentiBD.newFilter();

		if(bodyrichiesta.getLimit() != null) {
			filter.setLimit(bodyrichiesta.getLimit().intValue());
		} else {
			filter.setLimit(GovPayConfiguration.newInstance().getRicercaVersamentiDefaultLimit());
		}

		if(bodyrichiesta.getOffset() != null) {
			filter.setOffset(bodyrichiesta.getOffset().intValue());
		} else {
			filter.setOffset(0);
		}

		if(bodyrichiesta.getCodEnte() != null) {
			filter.setCodEnte(bodyrichiesta.getCodEnte());
		}

		if(bodyrichiesta.getCodApplicazione() != null) {
			try {
				Applicazione app = AnagraficaManager.getApplicazione(bd, bodyrichiesta.getCodApplicazione());
				filter.getIdApplicazioni().add(app.getId());
			} catch(MultipleResultException e) {
			} catch(NotFoundException e) {}
		}

		if(bodyrichiesta.getCodPortale() != null) {
			try {
				Portale port = AnagraficaManager.getPortale(bd, bodyrichiesta.getCodPortale());
				for(Long idApp: port.getIdApplicazioni())
					filter.getIdApplicazioni().add(idApp);
			} catch(MultipleResultException e) {
			} catch(NotFoundException e) {}
		}

		if(bodyrichiesta.getCodUnivocoDebitore() != null) {
			filter.setCodUnivocoDebitore(bodyrichiesta.getCodUnivocoDebitore());
		}

		if(bodyrichiesta.isTerminato() != null) {
			filter.setTerminato(bodyrichiesta.isTerminato());
		}

		//[TODO] chiedere a Lorenzo come gestire gli ordinamenti
		// Ordino per stato ASC
		filter.addSortField(SortFields.STATO, true);

		List<Versamento> versamentiLst = versamentiBD.findAll(filter);

		List<it.govpay.servizi.pa.Versamento> lstVersamenti = new ArrayList<it.govpay.servizi.pa.Versamento>(versamentiLst.size());

		if(versamentiLst.size() > 0) {

			RptBD rptBD = new RptBD(bd);
			RtBD rtBD = new RtBD(bd);

			for(Versamento versamento: versamentiLst) {
				try {
					Rpt rpt = rptBD.getLastRpt(versamento.getId());

					Rt rt = null;
					byte[] rtByte = null;
					rt = rtBD.getLastRt(rpt.getId());
					TracciatiBD tracciatiBD = new TracciatiBD(bd);
					rtByte = tracciatiBD.getTracciato(rt.getIdTracciatoXML());

					lstVersamenti.add(toVersamento(bd, bodyrichiesta.getCodApplicazione(), versamento, rtByte));
				} catch (Exception e){
					try {
						lstVersamenti.add(toVersamento(bd, bodyrichiesta.getCodApplicazione(), versamento, null));
					} catch (Exception e1){} 

				} 
			}
		}

		return lstVersamenti;

	}

	public static it.govpay.servizi.pa.Fr toFr(Fr fr, BasicBD bd) throws ServiceException, NotFoundException, MultipleResultException {
		VersamentiBD versamentiBD = new VersamentiBD(bd);

		it.govpay.servizi.pa.Fr f = new it.govpay.servizi.pa.Fr();
		f.setAnnoRiferimento(fr.getAnnoRiferimento());
		f.setCodFlusso(fr.getCodFlusso());
		f.setDataOraFlusso(fr.getDataOraFlusso());
		f.setDataRegolamento(fr.getDataRegolamento());
		f.setDescrizioneStato(fr.getDescrizioneStato());
		f.setImportoTotale(new BigDecimal(fr.getImportoTotalePagamenti()));
		f.setIur(fr.getIur());
		f.setNumeroPagamenti(fr.getNumeroPagamenti());
		f.setStato(fr.getStato().toString());
		for(SingolaRendicontazione sr : fr.getSingolaRendicontazioneList()) {
			SingoloVersamento sv = versamentiBD.getSingoloVersamento(sr.getIdSingoloVersamento());
			Tributo tributo = AnagraficaManager.getTributo(bd, sv.getIdTributo());
			it.govpay.servizi.pa.Fr.Rendicontazione r = new it.govpay.servizi.pa.Fr.Rendicontazione();
			r.setCodVersamentoEnte(sv.getCodSingoloVersamentoEnte());
			r.setDataEsito(sr.getDataEsito());
			r.setEsito(sr.getCodiceEsito());
			r.setImporto(new BigDecimal(sr.getSingoloImporto()));
			r.setIur(sr.getIur());
			r.setIuv(sr.getIuv());
			r.setCodContabilita(tributo.getCodContabilita());
			r.setCodTributo(tributo.getCodTributo());
			r.setTipoContabilita(tributo.getTipoContabilita().toString());
			IbanAccredito iban = AnagraficaManager.getIbanAccredito(bd, tributo.getIbanAccredito());
			r.setIbanAccredito(iban.getCodIban());
			r.setDescrizioneTributo(tributo.getDescrizione());
			f.getRendicontazione().add(r);
		}
		return f;
	}
}
