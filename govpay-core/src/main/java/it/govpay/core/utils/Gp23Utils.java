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

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.Anomalia;
import it.govpay.servizi.commons.EsitoRendicontazione;
import it.govpay.servizi.v2_3.commons.EstremiFlussoRendicontazione;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.StatoFr;
import it.govpay.servizi.commons.StatoRendicontazione;
import it.govpay.servizi.v2_3.commons.FlussoRendicontazione.Rendicontazione;

public class Gp23Utils {

	public static Rendicontazione toRendicontazione(RendicontazionePagamento rend, Versione versione, BasicBD bd) throws ServiceException {
		Rendicontazione rendicontazione = new Rendicontazione();
		rendicontazione.setData(rend.getRendicontazione().getData());
		rendicontazione.setEsito(EsitoRendicontazione.valueOf(rend.getRendicontazione().getEsito().toString()));
		rendicontazione.setImportoRendicontato(rend.getRendicontazione().getImportoPagato());
		rendicontazione.setIur(rend.getRendicontazione().getIur());
		rendicontazione.setIuv(rend.getRendicontazione().getIuv());
		rendicontazione.setStato(StatoRendicontazione.valueOf(rend.getRendicontazione().getStato().toString()));
		for(it.govpay.model.Rendicontazione.Anomalia a : rend.getRendicontazione().getAnomalie()) {
			Anomalia anomalia = new Anomalia();
			anomalia.setCodice(a.getCodice());
			anomalia.setValue(a.getDescrizione());
			rendicontazione.getAnomalia().add(anomalia);
		}
		if(rend.getPagamento() != null) {
			Rendicontazione.Pagamento pagamento = new Rendicontazione.Pagamento();
			pagamento.setCodApplicazione(rend.getPagamento().getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd).getCodApplicazione());
			pagamento.setCodVersamentoEnte(rend.getPagamento().getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
			pagamento.setCodSingoloVersamentoEnte(rend.getPagamento().getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
			rendicontazione.setPagamento(pagamento);
		}
		return rendicontazione;
	}

	public static FlussoRendicontazione toFr(Fr frModel, List<RendicontazionePagamento> rends, Versione versione, BasicBD bd) throws ServiceException {
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
		for(RendicontazionePagamento rend : rends) {
			fr.getRendicontazione().add(toRendicontazione(rend, versione, bd));
		}
		return fr;
	}

	public static EstremiFlussoRendicontazione toFr(Fr frModel, Versione versione, BasicBD bd) {
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
}
