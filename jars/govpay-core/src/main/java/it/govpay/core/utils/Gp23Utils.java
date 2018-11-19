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
import it.govpay.servizi.commons.Anomalia;
import it.govpay.servizi.commons.EsitoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.commons.StatoFr;
import it.govpay.servizi.commons.StatoRendicontazione;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.v2_3.commons.EstremiFlussoRendicontazione;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione.Rendicontazione;

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

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento, BasicBD bd) throws ServiceException {
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
		return p;
	}
	
	public static List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> toIuvRichiesto(List<it.govpay.servizi.v2_3.gpapp.GpGeneraIuv.IuvRichiesto> iuvRichiesti) {
		List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> iuvRichiestiModel = new ArrayList<>();
		for (it.govpay.servizi.v2_3.gpapp.GpGeneraIuv.IuvRichiesto iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto iuvRichiestoModel = new it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}
	
	public static List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> toIuvDaCaricare(List<it.govpay.servizi.v2_3.gpapp.GpCaricaIuv.IuvGenerato> iuvRichiesti) {
		List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> iuvRichiestiModel = new ArrayList<>();
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
		List<IuvGenerato> iuvGenerati = new ArrayList<>();
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
		List<IuvGenerato> iuvCaricati = new ArrayList<>();
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
