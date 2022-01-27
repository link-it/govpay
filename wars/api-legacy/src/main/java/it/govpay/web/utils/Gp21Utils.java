/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.web.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Versionabile;
import it.govpay.model.Versionabile.Versione;
import it.govpay.pagamento.v2.beans.NuovaPendenza;
import it.govpay.pagamento.v2.beans.NuovoPagamento;
import it.govpay.pagamento.v2.beans.TipoAutenticazioneSoggetto;
import it.govpay.pagamento.v2.beans.converter.PagamentiPortaleConverter;
import it.govpay.bd.pagamento.filters.VersamentoFilter.SortFields;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.dao.commons.VersamentoKey;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoAvviso;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoModello4;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoPendenza;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoTransazione;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.StatoRevoca;
import it.govpay.servizi.commons.StatoTransazione;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.TipoAutenticazione;
import it.govpay.servizi.commons.TipoRendicontazione;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.commons.Versamento.SingoloVersamento;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.gpapp.GpCaricaIuv;
import it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata;
import it.govpay.servizi.gpprt.GpChiediStatoRichiestaStornoResponse.Storno;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamentoResponse;

public class Gp21Utils {

	public static Transazione toTransazione(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException {
		Transazione t = new Transazione();
		Canale canale = new Canale();
		canale.setCodCanale(rpt.getCodCanale());
		canale.setCodPsp(rpt.getCodPsp());
		canale.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		if(rpt.getTipoVersamento() != null)
			canale.setTipoVersamento(TipoVersamento.valueOf(rpt.getTipoVersamento().getCodifica()));
		t.setCanale(canale);
		t.setCcp(rpt.getCcp());
		t.setCodDominio(rpt.getCodDominio());
		t.setDescrizioneStato(rpt.getDescrizioneStato());
		if(rpt.getEsitoPagamento() != null) {
			t.setEsito(EsitoTransazione.valueOf(rpt.getEsitoPagamento().toString()));
		}
		t.setIuv(rpt.getIuv());
		if(rpt.getModelloPagamento() != null)
			t.setModello(ModelloPagamento.valueOf(rpt.getModelloPagamento().toString()));
		t.setRpt(rpt.getXmlRpt());
		t.setRt(rpt.getXmlRt());

//		if(versione.compareTo(Versione.GP_02_02_00) >=0) { // Versione 2.2
			t.setData(rpt.getDataMsgRichiesta());
//		}

		try {
			t.setStato(StatoTransazione.valueOf(rpt.getStato().toString()));
		} catch (Exception e) {
			t.setStato(StatoTransazione.RPT_ACCETTATA_NODO);
		}
		for(Pagamento pagamento : rpt.getPagamenti(configWrapper)) {
			t.getPagamento().add(toPagamento(pagamento));
		}
		return t;
	}

	public static it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento toVersamento(Versione versione, Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException {
		it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento v = new it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento();
		v.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
		v.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		v.setDataScadenza(versamento.getDataScadenza());
		v.setImportoTotale(versamento.getImportoTotale());
		v.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSemplice)
			v.setCausale(((Versamento.CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoni)
			v.getSpezzoneCausale().addAll(((Versamento.CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoniStrutturati) {
			Versamento.CausaleSpezzoniStrutturati c = (Versamento.CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
			for(int i = 0; i<c.getImporti().size(); i++) {
				SpezzoneCausaleStrutturata s = new SpezzoneCausaleStrutturata();
				s.setCausale(c.getSpezzoni().get(i));
				s.setImporto(c.getImporti().get(i));
				v.getSpezzoneCausaleStrutturata().add(s);
			}
		}
//		if(versione.compareTo(Versione.GP_02_02_00) >=0) { // Versione 2.2
			v.setCodDominio(versamento.getUo(configWrapper).getDominio(configWrapper).getCodDominio());
			Iuv iuv = versamento.getIuv(configWrapper);
			if(iuv != null) {
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(configWrapper), versamento.getUo(configWrapper).getDominio(configWrapper), iuv, versamento.getImportoTotale());
				v.setIuv(iuv.getIuv());
				v.setBarCode(iuvGenerato.getBarCode());
				v.setQrCode(iuvGenerato.getQrCode());
//				if(versione.compareTo(Versione.GP_02_03_00) >=0) { // Versione 2.3
//					v.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
//				}
			}
//		}
		return v;
	}

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento) throws ServiceException {
		it.govpay.servizi.commons.Pagamento p = new it.govpay.servizi.commons.Pagamento();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(pagamento.getAllegato());
			allegato.setTipo(TipoAllegato.valueOf(pagamento.getTipoAllegato().toString()));
			p.setAllegato(allegato);
		}
		p.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		p.setCommissioniPsp(pagamento.getCommissioniPsp());
		p.setDataPagamento(pagamento.getDataPagamento());
		p.setImportoPagato(pagamento.getImportoPagato());
		p.setIur(pagamento.getIur());
		p.setCausaleRevoca(pagamento.getCausaleRevoca());
		p.setDatiEsitoRevoca(pagamento.getDatiEsitoRevoca());
		p.setDatiRevoca(pagamento.getDatiRevoca());
		p.setEsitoRevoca(pagamento.getEsitoRevoca());
		p.setImportoRevocato(pagamento.getImportoRevocato());
//		if(versione.compareTo(Versione.GP_02_02_00) >= 0) { // Versione 2.2
			p.setDataAcquisizione(pagamento.getDataAcquisizione());
			p.setDataAcquisizioneRevoca(pagamento.getDataAcquisizioneRevoca());
//		}
//		if(versione.compareTo(Versione.GP_02_05_00) >= 0) { // Versione 2.5
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAccredito(bd) != null)
//				p.setIbanAccredito(pagamento.getSingoloVersamento(bd).getIbanAccredito(bd).getCodIban());
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd) != null)	
//				p.setIbanAppoggio(pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd).getCodIban());
//		}
		return p;
	}

	public static FlussoRendicontazione toFr(Fr frModel, List<Rendicontazione> rends, BDConfigWrapper configWrapper) throws ServiceException {

		FlussoRendicontazione fr = new FlussoRendicontazione();
		int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(frModel.getDataFlusso()));
		fr.setAnnoRiferimento(annoFlusso);
		fr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		fr.setCodFlusso(frModel.getCodFlusso());
		fr.setCodPsp(frModel.getCodPsp());
		fr.setDataFlusso(frModel.getDataFlusso());
		fr.setDataRegolamento(frModel.getDataRegolamento());
		fr.setImportoTotale(BigDecimal.ZERO);
		fr.setNumeroPagamenti(0l);
		fr.setIur(frModel.getIur());

		for(Rendicontazione rend : rends) {
			it.govpay.servizi.commons.FlussoRendicontazione.Pagamento rendicontazionePagamento = Gp21Utils.toRendicontazionePagamento(rend, frModel, configWrapper);
			if(rendicontazionePagamento != null) {
				fr.setImportoTotale(rend.getImporto().add(fr.getImportoTotale()));
				fr.setNumeroPagamenti(fr.getNumeroPagamenti() + 1);
				fr.getPagamento().add(rendicontazionePagamento);
			}
		}

		return fr;
	}

	public static it.govpay.servizi.commons.FlussoRendicontazione.Pagamento toRendicontazionePagamento(Rendicontazione rend, Fr frModel, BDConfigWrapper configWrapper) throws ServiceException {

		if(rend.getVersamento(null) == null) return null;

		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		if(rend.getPagamento(null) != null)
			p.setCodSingoloVersamentoEnte(rend.getPagamento(null).getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		else
			p.setCodSingoloVersamentoEnte(rend.getVersamento(null).getSingoliVersamenti().get(0).getCodSingoloVersamentoEnte());
		p.setImportoRendicontato(rend.getImporto().abs());
		p.setIur(rend.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.valueOf(rend.getEsito().toString()));
		p.setDataRendicontazione(rend.getData());
//		if(versione.compareTo(Versione.GP_02_02_00) >= 0) { // Versione 2.2
			p.setCodApplicazione(rend.getVersamento(null).getApplicazione(configWrapper).getCodApplicazione());
			p.setIuv(rend.getIuv());
			p.setCodDominio(frModel.getCodDominio());
//		}

		return p;
	}

	public static List<it.govpay.bd.model.Versamento.StatoVersamento> toStatiVersamento(List<StatoVersamento> stati) {
		if(stati == null || stati.size() == 0) return null;

		List<it.govpay.bd.model.Versamento.StatoVersamento> statiVersamento = new ArrayList<it.govpay.bd.model.Versamento.StatoVersamento>();
		for(StatoVersamento stato : stati) {
			statiVersamento.add(it.govpay.bd.model.Versamento.StatoVersamento.valueOf(stato.toString()));
		}
		return statiVersamento;
	}

	public static SortFields toFilterSort(String ordinamento) {
		if(ordinamento == null) return null;

		if(ordinamento.equals("DATA_SCADENZA_ASC"))
			return SortFields.SCADENZA_ASC;

		if(ordinamento.equals("DATA_SCADENZA_DES"))
			return SortFields.SCADENZA_DESC;

		if(ordinamento.equals("DATA_CARICAMENTO_ASC"))
			return SortFields.CARICAMENTO_ASC;

		if(ordinamento.equals("DATA_CARICAMENTO_DES"))
			return SortFields.CARICAMENTO_DESC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_ASC"))
			return SortFields.AGGIORNAMENTO_ASC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_DES"))
			return SortFields.AGGIORNAMENTO_DESC;

		return null;
	}

	public static SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");
	public static it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione toFr(Fr frModel) {
		GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione efr = new GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione();
		int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(frModel.getDataFlusso()));
		efr.setAnnoRiferimento(annoFlusso);
		efr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		efr.setCodDominio(frModel.getCodDominio());
		efr.setCodFlusso(frModel.getCodFlusso());
		efr.setCodPsp(frModel.getCodPsp());
		efr.setDataFlusso(frModel.getDataFlusso());
		efr.setDataRegolamento(frModel.getDataRegolamento());
		efr.setIur(frModel.getIur());
		efr.setImportoTotale(frModel.getImportoTotalePagamenti());
		efr.setNumeroPagamenti(frModel.getNumeroPagamenti());
		return efr;
	}


	public static List<GpAvviaTransazionePagamentoResponse.RifTransazione> toRifTransazione(List<it.govpay.core.business.model.AvviaTransazioneDTOResponse.RifTransazione> rifTransazioniModel) {
		List<GpAvviaTransazionePagamentoResponse.RifTransazione> rifTransazioni = new ArrayList<GpAvviaTransazionePagamentoResponse.RifTransazione>();

		for(it.govpay.core.business.model.AvviaTransazioneDTOResponse.RifTransazione rifTransazioneModel : rifTransazioniModel) {
			GpAvviaTransazionePagamentoResponse.RifTransazione rifTransazione = new GpAvviaTransazionePagamentoResponse.RifTransazione();
			rifTransazione.setCcp(rifTransazioneModel.getCcp());
			rifTransazione.setCodApplicazione(rifTransazioneModel.getCodApplicazione());
			rifTransazione.setCodDominio(rifTransazioneModel.getCodDominio());
			rifTransazione.setCodVersamentoEnte(rifTransazioneModel.getCodVersamentoEnte());
			rifTransazione.setIuv(rifTransazioneModel.getIuv());
			rifTransazioni.add(rifTransazione);
		}

		return rifTransazioni;
	}

	public static List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> toIuvRichiesto(List<it.govpay.servizi.gpapp.GpGeneraIuv.IuvRichiesto> iuvRichiesti) {
		List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto>();
		for (it.govpay.servizi.gpapp.GpGeneraIuv.IuvRichiesto iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto iuvRichiestoModel = new it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}

	public static List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> toIuvDaCaricare(List<it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato> iuvRichiesti) {
		List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.CaricaIuvDTO.Iuv>();
		for (it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.CaricaIuvDTO.Iuv iuvRichiestoModel = new it.govpay.core.business.model.CaricaIuvDTO.Iuv();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestoModel.setIuv(iuvRichiesto.getIuv());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}

	public static List<IuvGenerato> toIuvGenerato(List<it.govpay.core.business.model.Iuv> iuvGeneratiModel, Applicazione applicazione) {
		List<IuvGenerato> iuvGenerati = new ArrayList<IuvGenerato>();
		for (it.govpay.core.business.model.Iuv iuvGeneratoModel : iuvGeneratiModel) {
			iuvGenerati.add(toIuvGenerato(iuvGeneratoModel, applicazione));
		}
		return iuvGenerati;
	}

	public static IuvGenerato toIuvGenerato(it.govpay.core.business.model.Iuv iuvGeneratoModel, Applicazione applicazione) {
		IuvGenerato iuvGenerato = new IuvGenerato();
		iuvGenerato.setBarCode(iuvGeneratoModel.getBarCode());
		iuvGenerato.setCodApplicazione(iuvGeneratoModel.getCodApplicazione());
		iuvGenerato.setCodDominio(iuvGeneratoModel.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuvGeneratoModel.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuvGeneratoModel.getIuv());
		iuvGenerato.setQrCode(iuvGeneratoModel.getQrCode());
		//		if(applicazione.getVersione().compareTo(Versione.GP_02_03_00) >= 0) { // Versione 2.3
		//			iuvGenerato.setNumeroAvviso(iuvGeneratoModel.getNumeroAvviso());
		//		}
		return iuvGenerato;
	}

	public static Collection<? extends IuvGenerato> toIuvCaricato(BDConfigWrapper configWrapper, GpCaricaIuv bodyrichiesta, Applicazione applicazione) throws ServiceException, NotFoundException {
		List<IuvGenerato> iuvCaricati = new ArrayList<IuvGenerato>();
		for (it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvDaCaricareModel : bodyrichiesta.getIuvGenerato()) {
			Dominio dominio = AnagraficaManager.getDominio(configWrapper, bodyrichiesta.getCodDominio());

			it.govpay.model.Iuv iuv = new it.govpay.model.Iuv();
			iuv.setIdDominio(dominio.getId());
			iuv.setPrg(0);
			iuv.setIuv(iuvDaCaricareModel.getIuv());
			iuv.setDataGenerazione(new Date());
			iuv.setIdApplicazione(applicazione.getId());
			iuv.setTipo(TipoIUV.NUMERICO);
			iuv.setCodVersamentoEnte(iuvDaCaricareModel.getCodVersamentoEnte());
			iuv.setApplicationCode(dominio.getStazione().getApplicationCode());

			it.govpay.core.business.model.Iuv iuvCaricatoModel = IuvUtils.toIuv(applicazione, dominio, iuv, iuvDaCaricareModel.getImportoTotale());

			iuvCaricati.add(toIuvGenerato(iuvCaricatoModel, applicazione)); 
		}
		return iuvCaricati;
	}

	public static it.govpay.core.dao.commons.Versamento toVersamentoCommons(it.govpay.servizi.commons.Versamento pendenza) throws ServiceException, IOException, GovPayException {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoTributario() != null)
			versamento.setAnnoTributario(pendenza.getAnnoTributario().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getCodApplicazione());

		versamento.setCodDominio(pendenza.getCodDominio());
		versamento.setCodUnitaOperativa(pendenza.getCodUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getCodVersamentoEnte());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		//		versamento.setDataValidita(pendenza.getDataValidita());
		//		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getDebitore()));

//		versamento.setTassonomia(pendenza.getTassonomia());
//		if(pendenza.getDatiAllegati() != null)
//			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati(),null));

//		if(pendenza.getTassonomiaAvviso() != null) {
//			// valore tassonomia avviso non valido
//			if(TassonomiaAvviso.fromValue(pendenza.getTassonomiaAvviso()) == null) {
//				throw new ValidationException("Codifica inesistente per tassonomiaAvviso. Valore fornito [" + pendenza.getTassonomiaAvviso() + "] valori possibili " + ArrayUtils.toString(TassonomiaAvviso.values()));
//			}
//
//			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso());
//		}

		versamento.setIuv(pendenza.getIuv());

		// voci pagamento
		BigDecimal importoVociPendenza = fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getSingoloVersamento());

		// importo pendenza puo' essere null
		versamento.setImportoTotale(pendenza.getImportoTotale() != null ? pendenza.getImportoTotale() : importoVociPendenza); 

		// tipo Pendenza
//		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza()); TODO

		//		versamento.setDirezione(pendenza.getDirezione());
		//		versamento.setDivisione(pendenza.getDivisione()); 
		versamento.setCodLotto(pendenza.getCodDebito());

		//		if(pendenza.getDocumento() != null) {
		//			it.govpay.core.dao.commons.Versamento.Documento documento = new it.govpay.core.dao.commons.Versamento.Documento();
		//			
		//			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
		//			if(pendenza.getDocumento().getRata() != null)
		//				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
		//			if(pendenza.getDocumento().getSoglia() != null) {
		//				// valore tassonomia avviso non valido
		//				if(TipoSogliaVincoloPagamento.fromValue(pendenza.getDocumento().getSoglia().getTipo()) == null) {
		//					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" 
		//								+ pendenza.getDocumento().getSoglia().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
		//				}
		//				
		//				documento.setGiorniSoglia(pendenza.getDocumento().getSoglia().getGiorni().intValue());
		//				documento.setTipoSoglia(pendenza.getDocumento().getSoglia().getTipo());
		//			}
		//			documento.setDescrizione(pendenza.getDocumento().getDescrizione());
		//
		//			versamento.setDocumento(documento );
		//		}

		//		versamento.setDataNotificaAvviso(pendenza.getDataNotificaAvviso());
		//		versamento.setDataPromemoriaScadenza(pendenza.getDataPromemoriaScadenza());

		//		versamento.setProprieta(toProprietaPendenzaDTO(pendenza.getProprieta()));

		return versamento;
	}

	public static BigDecimal fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<SingoloVersamento> voci) throws ServiceException, IOException, GovPayException {

		BigDecimal importoTotale = BigDecimal.ZERO;

		if(voci != null && voci.size() > 0) {
			for (SingoloVersamento vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getCodSingoloVersamentoEnte());
//				if(vocePendenza.getDatiAllegati() != null)
//					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
//				sv.setDescrizione(vocePendenza.getDescrizione());
//				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setImporto(vocePendenza.getImporto());
				sv.setCodDominio(versamento.getCodDominio());

				importoTotale = importoTotale.add(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getBolloTelematico() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getBolloTelematico().getHash());
					bollo.setProvincia(vocePendenza.getBolloTelematico().getProvincia());
					bollo.setTipo(vocePendenza.getBolloTelematico().getTipo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodTributo());

				} else { // Definisce i dettagli di incasso della singola entrata.
					if(vocePendenza.getTributo() != null) {
						it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
						tributo.setCodContabilita(vocePendenza.getTributo().getCodContabilita());
						tributo.setIbanAccredito(vocePendenza.getTributo().getIbanAccredito());
						tributo.setIbanAppoggio(vocePendenza.getTributo().getIbanAppoggio());
						tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(vocePendenza.getTributo().getTipoContabilita().name()));
						sv.setTributo(tributo);
					}
				}
				
//				sv.setContabilita(ContabilitaConverter.toStringDTO(vocePendenza.getContabilita()));
				
//				if(vocePendenza.getContabilita() != null) {
//					if(vocePendenza.getContabilita().getQuote() != null) {
//						BigDecimal somma = BigDecimal.ZERO;
//						for (QuotaContabilita voceContabilita : vocePendenza.getContabilita().getQuote()) {
//							somma = somma.add(voceContabilita.getImporto());
//						}
//						
//						if(somma.compareTo(vocePendenza.getImporto()) != 0) {
//							throw new GovPayException(EsitoOperazione.VER_035, vocePendenza.getIdVocePendenza(),  versamento.getCodApplicazione(), versamento.getCodVersamentoEnte(),
//								Double.toString(sv.getImporto().doubleValue()), Double.toString(somma.doubleValue()));
//						}
//					}
//				}

				versamento.getSingoloVersamento().add(sv);
			}
		}

		return importoTotale;
	}

	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		it.govpay.core.dao.commons.Anagrafica anagraficaModel = new it.govpay.core.dao.commons.Anagrafica();
		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setCodUnivoco(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setRagioneSociale(anagrafica.getRagioneSociale());
		anagraficaModel.setTelefono(anagrafica.getTelefono());
		return anagraficaModel;
	}

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(GpAvviaTransazionePagamento bodyrichiesta,
			MetaInfo metaInfo, Authentication user, String idSessione, Logger log) throws Exception {

		String idSessionePortale = bodyrichiesta.getCodSessionePortale();
		
		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);
//		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		if(bodyrichiesta.getAutenticazione() != null)
			pagamentiPortaleDTO.setAutenticazioneSoggetto(bodyrichiesta.getAutenticazione().toString());
		else 
			pagamentiPortaleDTO.setAutenticazioneSoggetto(TipoAutenticazione.N_A.toString());

//		pagamentiPortaleDTO.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
//		pagamentiPortaleDTO.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());

		if(bodyrichiesta.getIbanAddebito() != null) {
//			pagamentiPortaleDTO.setBicAddebito(pagamentiPortaleRequest.getContoAddebito().getBic());
			pagamentiPortaleDTO.setIbanAddebito(bodyrichiesta.getIbanAddebito());
		}

		pagamentiPortaleDTO.setUrlRitorno(bodyrichiesta.getUrlRitorno());


//		PagamentiPortaleConverter.controlloUtenzaVersante(pagamentiPortaleRequest, user);
		pagamentiPortaleDTO.setVersante(toAnagraficaCommons(bodyrichiesta.getVersante()));

		if(bodyrichiesta.getVersamentoOrVersamentoRef() != null && bodyrichiesta.getVersamentoOrVersamentoRef().size() > 0 ) {
			List<Object> listRefs = new ArrayList<>();

			int i =0;
			for (Object pendenzaObj: bodyrichiesta.getVersamentoOrVersamentoRef()) {
				if(pendenzaObj instanceof it.govpay.servizi.commons.Versamento) {
					it.govpay.core.dao.commons.Versamento versamento = toVersamentoCommons((it.govpay.servizi.commons.Versamento) pendenzaObj);

					listRefs.add(versamento);
				} else if(pendenzaObj instanceof it.govpay.servizi.commons.VersamentoKey) {
					it.govpay.servizi.commons.VersamentoKey pendenzaKey = (it.govpay.servizi.commons.VersamentoKey) pendenzaObj;
					
					VersamentoKey versamentoKey = new VersamentoKey();
					
					Iterator<JAXBElement<String>> iterator = pendenzaKey.getContent().iterator();
                    while(iterator.hasNext()){
                            JAXBElement<String> element = iterator.next();

                            if(element.getName().equals(VersamentoUtils._VersamentoKeyBundlekey_QNAME)) {
                            	versamentoKey.setBundlekey(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME)) {
                            	versamentoKey.setCodUnivocoDebitore(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodApplicazione_QNAME)) {
                            	versamentoKey.setCodApplicazione(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodDominio_QNAME)) {
                            	versamentoKey.setCodDominio(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME)) {
                            	versamentoKey.setCodVersamentoEnte(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyIuv_QNAME)) {
                            	versamentoKey.setIuv(element.getValue());
                            }
                    }

                    listRefs.add(versamentoKey);
				} else {
					throw new RequestValidationException("La pendenza "+(i+1)+" e' di un tipo non riconosciuto.");
				}
				i++;
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}
		
		if(bodyrichiesta.getCanale() != null) {
			Canale canale = bodyrichiesta.getCanale();
			
			pagamentiPortaleDTO.setIdentificativoCanale(canale.getCodCanale());
			pagamentiPortaleDTO.setIdentificativoIntermediarioPSP(canale.getCodIntermediarioPsp());
			pagamentiPortaleDTO.setIdentificativoPSP(canale.getCodPsp());
			if(canale.getTipoVersamento() != null) {
				pagamentiPortaleDTO.setTipoVersamento(canale.getTipoVersamento().toString());
			}
		}

		// Salvataggio del messaggio di richiesta sul db
		//		pagamentiPortaleDTO.setJsonRichiesta(jsonRichiesta);
//		pagamentiPortaleDTO.setJsonRichiesta(pagamentiPortaleRequest.toJSON(null));
		
		pagamentiPortaleDTO.setCodiceConvenzione(bodyrichiesta.getCodConvenzione());
		

		return pagamentiPortaleDTO;
	}

}
