/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoTransazione;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.StatoRevoca;
import it.govpay.servizi.commons.StatoTransazione;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.TipoRendicontazione;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata;
import it.govpay.servizi.gpprt.GpChiediStatoRichiestaStornoResponse.Storno;

public class Gp21Utils {

	public static Transazione toTransazione(Rpt rpt, BasicBD bd) throws ServiceException {
		Transazione t = new Transazione();
		Canale canale = new Canale();
		canale.setCodCanale(rpt.getCanale(bd).getCodCanale());
		canale.setCodPsp(rpt.getPsp(bd).getCodPsp());
		canale.setTipoVersamento(TipoVersamento.valueOf(rpt.getCanale(bd).getTipoVersamento().getCodifica()));
		t.setCanale(canale);
		t.setCcp(rpt.getCcp());
		t.setCodDominio(rpt.getCodDominio());
		t.setDescrizioneStato(rpt.getDescrizioneStato());
		if(rpt.getEsitoPagamento() != null) {
			t.setEsito(EsitoTransazione.valueOf(rpt.getEsitoPagamento().toString()));
		}
		t.setIuv(rpt.getIuv());
		t.setModello(ModelloPagamento.valueOf(rpt.getModelloPagamento().toString()));
		t.setRpt(rpt.getXmlRpt());
		t.setRt(rpt.getXmlRt());
		try {
			t.setStato(StatoTransazione.valueOf(rpt.getStato().toString()));
		} catch (Exception e) {
			t.setStato(StatoTransazione.RPT_ACCETTATA_NODO);
		}
		for(Pagamento pagamento : rpt.getPagamenti(bd)) {
			t.getPagamento().add(toPagamento(pagamento));
		}
		return t;
	}

	public static it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento toVersamento(Versamento versamento, BasicBD bd) throws ServiceException {
		it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento v = new it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento();
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
		return v;
	}

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento) {
		it.govpay.servizi.commons.Pagamento p = new it.govpay.servizi.commons.Pagamento();
		
		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(pagamento.getAllegato());
			allegato.setTipo(TipoAllegato.valueOf(pagamento.getTipoAllegato().toString()));
			p.setAllegato(allegato);
		}
		p.setCodSingoloVersamentoEnte(pagamento.getCodSingoloVersamentoEnte());
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
	
	public static FlussoRendicontazione.Pagamento toRendicontazionePagamento(Pagamento pagamento) {
		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		p.setCodSingoloVersamentoEnte(pagamento.getCodSingoloVersamentoEnte());
		p.setImportoRendicontato(pagamento.getImportoPagato());
		p.setIur(pagamento.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.valueOf(pagamento.getEsitoRendicontazione().toString()));
		p.setDataRendicontazione(pagamento.getDataRendicontazione());
		return p;
	}

	public static FlussoRendicontazione.Pagamento toRendicontazionePagamento(RendicontazioneSenzaRpt rendicontazione, BasicBD bd) throws ServiceException {
		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		p.setCodSingoloVersamentoEnte(rendicontazione.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		p.setImportoRendicontato(rendicontazione.getImportoPagato());
		p.setIur(rendicontazione.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.ESEGUITO_SENZA_RPT);
		p.setDataRendicontazione(rendicontazione.getDataRendicontazione());
		return p;
	}

	public static Storno toStorno(Rr rr, BasicBD bd) throws ServiceException {
		Storno storno = new Storno();
		storno.setCcp(rr.getCcp());
		storno.setCodDominio(rr.getCodDominio());
		storno.setDescrizioneStato(rr.getDescrizioneStato());
		storno.setEr(rr.getXmlEr());
		storno.setIuv(rr.getIuv());
		storno.setRr(rr.getXmlRr());
		storno.setStato(StatoRevoca.fromValue(rr.getStato().toString()));
		for(Pagamento p : rr.getPagamenti(bd)) {
			storno.getPagamento().add(toPagamento(p));
		}
		return storno;
	}

}
