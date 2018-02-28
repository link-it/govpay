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
package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Iuv;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.Anomalia;
import it.govpay.servizi.commons.EsitoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.v2_3.commons.EstremiFlussoRendicontazione;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.StatoFr;
import it.govpay.servizi.commons.StatoRendicontazione;
import it.govpay.servizi.commons.StatoRevoca;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata;
import it.govpay.servizi.v2_3.gpprt.GpChiediStatoRichiestaStornoResponse.Storno;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione.Rendicontazione;
import it.govpay.servizi.v2_3.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaPspResponse;
import it.govpay.servizi.v2_3.gpprt.GpChiediListaVersamentiResponse;

public class Gp23Utils {

	public static Rendicontazione toRendicontazione(it.govpay.bd.model.Rendicontazione rend, BasicBD bd) throws ServiceException {
		Rendicontazione rendicontazione = new Rendicontazione();
		rendicontazione.setData(rend.getData());
		rendicontazione.setEsito(EsitoRendicontazione.valueOf(rend.getEsito().toString()));
		rendicontazione.setImportoRendicontato(rend.getImporto());
		rendicontazione.setIur(rend.getIur());
		rendicontazione.setIuv(rend.getIuv());
		rendicontazione.setStato(StatoRendicontazione.valueOf(rend.getStato().toString()));
		for(it.govpay.model.Rendicontazione.Anomalia a : rend.getAnomalie()) {
			Anomalia anomalia = new Anomalia();
			anomalia.setCodice(a.getCodice());
			anomalia.setValue(a.getDescrizione());
			rendicontazione.getAnomalia().add(anomalia);
		}
		if(rend.getVersamento(bd) != null) {
			Rendicontazione.Pagamento pagamento = new Rendicontazione.Pagamento();
			pagamento.setCodApplicazione(rend.getVersamento(bd).getApplicazione(bd).getCodApplicazione());
			pagamento.setCodVersamentoEnte(rend.getVersamento(bd).getCodVersamentoEnte());
			if(rend.getEsito().equals(it.govpay.model.Rendicontazione.EsitoRendicontazione.ESEGUITO_SENZA_RPT))
				pagamento.setCodSingoloVersamentoEnte(rend.getVersamento(bd).getSingoliVersamenti(bd).get(0).getCodSingoloVersamentoEnte());
			else
				pagamento.setCodSingoloVersamentoEnte(rend.getPagamento(bd).getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
			rendicontazione.setPagamento(pagamento);
		}
		return rendicontazione;
	}

	public static FlussoRendicontazione toFr(Fr frModel, List<it.govpay.bd.model.Rendicontazione> rends, BasicBD bd) throws ServiceException {
		FlussoRendicontazione fr = new FlussoRendicontazione();
		fr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		fr.setCodDominio(frModel.getCodDominio());
		fr.setCodFlusso(frModel.getCodFlusso());
		fr.setCodPsp(frModel.getCodPsp());
		fr.setDataFlusso(frModel.getDataFlusso());
		fr.setDataRegolamento(frModel.getDataRegolamento());
		fr.setImportoTotale(frModel.getImportoTotalePagamenti());
		fr.setNumeroPagamenti(frModel.getNumeroPagamenti());
		fr.setStato(StatoFr.valueOf(frModel.getStato().toString()));
		fr.setTrn(frModel.getIur());
		for(it.govpay.model.Fr.Anomalia a : frModel.getAnomalie()) {
			Anomalia anomalia = new Anomalia();
			anomalia.setCodice(a.getCodice());
			anomalia.setValue(a.getDescrizione());
			fr.getAnomalia().add(anomalia);
		}
		for(it.govpay.bd.model.Rendicontazione rend : rends) {
			fr.getRendicontazione().add(toRendicontazione(rend, bd));
		}
		return fr;
	}

	public static EstremiFlussoRendicontazione toFr(Fr frModel, BasicBD bd) {
		EstremiFlussoRendicontazione fr = new EstremiFlussoRendicontazione();
		fr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		fr.setCodDominio(frModel.getCodDominio());
		fr.setCodFlusso(frModel.getCodFlusso());
		fr.setCodPsp(frModel.getCodPsp());
		fr.setDataFlusso(frModel.getDataFlusso());
		fr.setDataRegolamento(frModel.getDataRegolamento());
		fr.setImportoTotale(frModel.getImportoTotalePagamenti());
		fr.setNumeroPagamenti(frModel.getNumeroPagamenti());
		fr.setStato(StatoFr.valueOf(frModel.getStato().toString()));
		fr.setTrn(frModel.getIur());
		for(it.govpay.model.Fr.Anomalia a : frModel.getAnomalie()) {
			Anomalia anomalia = new Anomalia();
			anomalia.setCodice(a.getCodice());
			anomalia.setValue(a.getDescrizione());
			fr.getAnomalia().add(anomalia);
		}
		return fr;
	}

	public static List<GpChiediListaPspResponse.Psp> toPsp(List<Psp> pspsModel) {
		List<GpChiediListaPspResponse.Psp> psps = new ArrayList<GpChiediListaPspResponse.Psp>();

		for(it.govpay.bd.model.Psp pspModel : pspsModel) {
			GpChiediListaPspResponse.Psp psp = new GpChiediListaPspResponse.Psp();
			psp.setBollo(pspModel.isBolloGestito());
			psp.setCodPsp(pspModel.getCodPsp());
			psp.setLogo(PspUtils.getLogo160(pspModel.getCodPsp()));
			psp.setRagioneSociale(pspModel.getRagioneSociale());
			psp.setStorno(pspModel.isStornoGestito());
			psp.setUrlInfo(pspModel.getUrlInfo());
			for(it.govpay.bd.model.Canale canaleModel : pspModel.getCanalis()) {
				GpChiediListaPspResponse.Psp.Canale canale = new GpChiediListaPspResponse.Psp.Canale();
				canale.setCodCanale(canaleModel.getCodCanale());
				canale.setCondizioni(canaleModel.getCondizioni());
				canale.setDescrizione(canaleModel.getDescrizione());
				canale.setDisponibilita(canaleModel.getDisponibilita());
				canale.setLogoServizio(PspUtils.getLogo(canaleModel.getModelloPagamento()));
				canale.setModelloPagamento(PspUtils.toWeb(canaleModel.getModelloPagamento()));
				canale.setTipoVersamento(PspUtils.toWeb(canaleModel.getTipoVersamento()));
				canale.setUrlInfo(canaleModel.getUrlInfo());
				psp.getCanale().add(canale);
			}
			psps.add(psp);
		}
		return psps;
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

	public static GpChiediListaVersamentiResponse.Versamento toVersamento(Versamento versamento, BasicBD bd) throws ServiceException {
		GpChiediListaVersamentiResponse.Versamento v = new GpChiediListaVersamentiResponse.Versamento();
		v.setCodApplicazione(versamento.getApplicazione(bd).getCodApplicazione());
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
		v.setCodDominio(versamento.getUo(bd).getDominio(bd).getCodDominio());
		Iuv iuv = versamento.getIuv(bd);
		if(iuv != null) {
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(bd), versamento.getUo(bd).getDominio(bd), iuv, versamento.getImportoTotale());
			v.setIuv(iuv.getIuv());
			v.setBarCode(iuvGenerato.getBarCode());
			v.setQrCode(iuvGenerato.getQrCode());
			v.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
		}
		return v;
	}

	public static Storno toStorno(Rr rr, Versione versione, BasicBD bd) throws ServiceException {
		Storno storno = new Storno();
		storno.setCcp(rr.getCcp());
		storno.setCodDominio(rr.getCodDominio());
		storno.setDescrizioneStato(rr.getDescrizioneStato());
		storno.setEr(rr.getXmlEr());
		storno.setIuv(rr.getIuv());
		storno.setRr(rr.getXmlRr());
		storno.setStato(StatoRevoca.fromValue(rr.getStato().toString()));
		for(Pagamento p : rr.getPagamenti(bd)) {
			storno.getPagamento().add(toPagamento(p, versione, bd));
		}
		return storno;
	}

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento, Versione versione, BasicBD bd) throws ServiceException {
		it.govpay.servizi.commons.Pagamento p = new it.govpay.servizi.commons.Pagamento();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(pagamento.getAllegato());
			allegato.setTipo(TipoAllegato.valueOf(pagamento.getTipoAllegato().toString()));
			p.setAllegato(allegato);
		}
		p.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		p.setCommissioniPsp(pagamento.getCommissioniPsp());
		p.setDataPagamento(pagamento.getDataPagamento());
		p.setImportoPagato(pagamento.getImportoPagato());
		p.setIur(pagamento.getIur());
		p.setCausaleRevoca(pagamento.getCausaleRevoca());
		p.setDatiEsitoRevoca(pagamento.getDatiEsitoRevoca());
		p.setDatiRevoca(pagamento.getDatiRevoca());
		p.setEsitoRevoca(pagamento.getEsitoRevoca());
		p.setImportoRevocato(pagamento.getImportoRevocato());
		p.setDataAcquisizione(pagamento.getDataAcquisizione());
		p.setDataAcquisizioneRevoca(pagamento.getDataAcquisizioneRevoca());
		if(versione.compareVersione(Versione.GP_SOAP_02_05) >= 0) {
			p.setIbanAccredito(pagamento.getIbanAccredito());
		}
		return p;
	}
	
	public static List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> toIuvRichiesto(List<it.govpay.servizi.v2_3.gpapp.GpGeneraIuv.IuvRichiesto> iuvRichiesti) {
		List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto>();
		for (it.govpay.servizi.v2_3.gpapp.GpGeneraIuv.IuvRichiesto iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto iuvRichiestoModel = new it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}
	
	public static List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> toIuvDaCaricare(List<it.govpay.servizi.v2_3.gpapp.GpCaricaIuv.IuvGenerato> iuvRichiesti) {
		List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.CaricaIuvDTO.Iuv>();
		for (it.govpay.servizi.v2_3.gpapp.GpCaricaIuv.IuvGenerato iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.CaricaIuvDTO.Iuv iuvRichiestoModel = new it.govpay.core.business.model.CaricaIuvDTO.Iuv();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestoModel.setIuv(iuvRichiesto.getIuv());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}
	
	public static List<IuvGenerato> toIuvGenerato(List<it.govpay.core.business.model.Iuv> iuvGeneratiModel) {
		List<IuvGenerato> iuvGenerati = new ArrayList<IuvGenerato>();
		for (it.govpay.core.business.model.Iuv iuvGeneratoModel : iuvGeneratiModel) {
			iuvGenerati.add(toIuvGenerato(iuvGeneratoModel));
		}
		return iuvGenerati;
	}
	
	public static IuvGenerato toIuvGenerato(it.govpay.core.business.model.Iuv iuvGeneratoModel) {
		IuvGenerato iuvGenerato = new IuvGenerato();
		iuvGenerato.setBarCode(iuvGeneratoModel.getBarCode());
		iuvGenerato.setCodApplicazione(iuvGeneratoModel.getCodApplicazione());
		iuvGenerato.setCodDominio(iuvGeneratoModel.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuvGeneratoModel.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuvGeneratoModel.getIuv());
		iuvGenerato.setQrCode(iuvGeneratoModel.getQrCode());
		iuvGenerato.setNumeroAvviso(iuvGeneratoModel.getNumeroAvviso());
		return iuvGenerato;
	}

	public static Collection<? extends IuvGenerato> toIuvCaricato(List<it.govpay.core.business.model.Iuv> iuvCaricatiModel) {
		List<IuvGenerato> iuvCaricati = new ArrayList<IuvGenerato>();
		for (it.govpay.core.business.model.Iuv iuvCaricatoModel : iuvCaricatiModel) {
			IuvGenerato iuvCaricato = new IuvGenerato();
			iuvCaricato.setBarCode(iuvCaricatoModel.getBarCode());
			iuvCaricato.setCodApplicazione(iuvCaricatoModel.getCodApplicazione());
			iuvCaricato.setCodDominio(iuvCaricatoModel.getCodDominio());
			iuvCaricato.setCodVersamentoEnte(iuvCaricatoModel.getCodVersamentoEnte());
			iuvCaricato.setIuv(iuvCaricatoModel.getIuv());
			iuvCaricato.setQrCode(iuvCaricatoModel.getQrCode());
			iuvCaricato.setNumeroAvviso(iuvCaricatoModel.getNumeroAvviso());
			iuvCaricati.add(iuvCaricato);
		}
		return iuvCaricati;
	}
}
