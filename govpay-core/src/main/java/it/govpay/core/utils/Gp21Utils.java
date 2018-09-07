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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.filters.VersamentoFilter.SortFields;
import it.govpay.core.rs.v1.beans.base.Allegato.TipoEnum;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoTransazione;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.commons.StatoTransazione;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.TipoRendicontazione;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.Transazione;

public class Gp21Utils {

	public static Transazione toTransazione(Rpt rpt, BasicBD bd) throws ServiceException {
		Transazione t = new Transazione();
		Canale canale = new Canale();
		canale.setCodCanale(rpt.getCodCanale());
		canale.setCodPsp(rpt.getCodPsp());
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

		t.setData(rpt.getDataMsgRichiesta());

		try {
			t.setStato(StatoTransazione.valueOf(rpt.getStato().toString()));
		} catch (Exception e) {
			t.setStato(StatoTransazione.RPT_ACCETTATA_NODO);
		}
		for(Pagamento pagamento : rpt.getPagamenti(bd)) {
			t.getPagamento().add(toPagamento(pagamento, bd));
		}
		return t;
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

	public static FlussoRendicontazione toFr(Fr frModel, List<Rendicontazione> rends, BasicBD bd) throws ServiceException {
		
		FlussoRendicontazione fr = new FlussoRendicontazione();
		int annoFlusso = Integer.parseInt(SimpleDateFormatUtils.newSimpleDateFormatSoloAnno().format(frModel.getDataFlusso()));
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
			it.govpay.servizi.commons.FlussoRendicontazione.Pagamento rendicontazionePagamento = Gp21Utils.toRendicontazionePagamento(rend, bd);
			if(rendicontazionePagamento != null) {
				fr.setImportoTotale(rend.getImporto().add(fr.getImportoTotale()));
				fr.setNumeroPagamenti(fr.getNumeroPagamenti() + 1);
				fr.getPagamento().add(rendicontazionePagamento);
			}
		}
		
		return fr;
	}

	public static it.govpay.servizi.commons.FlussoRendicontazione.Pagamento toRendicontazionePagamento(Rendicontazione rend, BasicBD bd) throws ServiceException {
		
		if(rend.getVersamento(bd) == null) return null;
		
		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		if(rend.getPagamento(bd) != null)
			p.setCodSingoloVersamentoEnte(rend.getPagamento(bd).getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		else
			p.setCodSingoloVersamentoEnte(rend.getVersamento(bd).getSingoliVersamenti(bd).get(0).getCodSingoloVersamentoEnte());
		p.setImportoRendicontato(rend.getImporto().abs());
		p.setIur(rend.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.valueOf(rend.getEsito().toString()));
		p.setDataRendicontazione(rend.getData());
		p.setCodApplicazione(rend.getVersamento(bd).getApplicazione(bd).getCodApplicazione());
		p.setIuv(rend.getIuv());
		p.setCodDominio(rend.getFr(bd).getCodDominio());
		
		return p;
	}

	public static List<it.govpay.bd.model.Versamento.StatoVersamento> toStatiVersamento(List<StatoVersamento> stati) {
		if(stati == null || stati.size() == 0) return null;

		List<it.govpay.bd.model.Versamento.StatoVersamento> statiVersamento = new ArrayList<>();
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
	
	
	public static it.govpay.core.rs.v1.beans.base.Riscossione toRiscossione(Pagamento pagamento, BasicBD bd, int idx, String urlPendenza, String urlRpt) throws ServiceException {
		it.govpay.core.rs.v1.beans.base.Riscossione riscossione = new it.govpay.core.rs.v1.beans.base.Riscossione();

		if(pagamento.getAllegato() != null) {
			it.govpay.core.rs.v1.beans.base.Allegato allegato = new it.govpay.core.rs.v1.beans.base.Allegato();
			allegato.setTesto(Base64.encodeBase64String(pagamento.getAllegato()));
			allegato.setTipo(TipoEnum.valueOf(pagamento.getTipoAllegato().toString()));
			riscossione.setAllegato(allegato);
		}
		
		riscossione.setIndice(new BigDecimal(idx));
		riscossione.setIdDominio(pagamento.getCodDominio());
		riscossione.setIuv(pagamento.getIuv()); 
		riscossione.setIdVocePendenza(pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		riscossione.setCommissioni(pagamento.getCommissioniPsp());
		riscossione.setData(pagamento.getDataPagamento());
		riscossione.setImporto(pagamento.getImportoPagato());
		riscossione.setIur(pagamento.getIur());
		riscossione.setPendenza(urlPendenza); 
		riscossione.setRpp(urlRpt);
//		riscossione.setCausaleRevoca(pagamento.getCausaleRevoca());
//		riscossione.setDatiEsitoRevoca(pagamento.getDatiEsitoRevoca());
//		riscossione.setDatiRevoca(pagamento.getDatiRevoca());
//		riscossione.setEsitoRevoca(pagamento.getEsitoRevoca());
//		riscossione.setImportoRevocato(pagamento.getImportoRevocato());
//		riscossione.setDataAcquisizione(pagamento.getDataAcquisizione());
//		riscossione.setDataAcquisizioneRevoca(pagamento.getDataAcquisizioneRevoca());
		
		return riscossione;
	}
}
