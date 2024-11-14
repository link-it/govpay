/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.FlussoRendicontazione;
import it.govpay.backoffice.v1.beans.FlussoRendicontazioneIndex;
import it.govpay.backoffice.v1.beans.Segnalazione;
import it.govpay.backoffice.v1.beans.StatoFlussoRendicontazione;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.model.Fr.Anomalia;
import it.govpay.model.Fr.StatoFr;

public class FlussiRendicontazioneConverter {
	
	private FlussiRendicontazioneConverter() {}

	public static FlussoRendicontazione toRsModel(it.govpay.bd.model.Fr fr, List<it.govpay.bd.viste.model.Rendicontazione> listaRendicontazioni) throws ServiceException, IOException, ValidationException {
		FlussoRendicontazione rsModel = new FlussoRendicontazione();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());

		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		rsModel.setRagioneSocialeDominio(fr.getRagioneSocialeDominio());
		rsModel.setRagioneSocialePsp(fr.getRagioneSocialePsp());

		List<it.govpay.backoffice.v1.beans.Rendicontazione> rendicontazioniLst = new ArrayList<>();
		if(listaRendicontazioni != null) {
			for(it.govpay.bd.viste.model.Rendicontazione rendicontazione: listaRendicontazioni) {
				rendicontazioniLst.add(toRendicontazioneRsModel(rendicontazione));
			}
		}
		rsModel.setRendicontazioni(rendicontazioniLst);

		StatoFr stato = fr.getStato();
		if(stato != null) {
			switch (stato) {
			case ACCETTATA:
				rsModel.setStato(StatoFlussoRendicontazione.ACQUISITO);
				break;
			case ANOMALA:
				rsModel.setStato(StatoFlussoRendicontazione.ANOMALO);
				break;
			case RIFIUTATA:
				rsModel.setStato(StatoFlussoRendicontazione.RIFIUTATO);
				break;
			}
		}

		return rsModel;
	}

	public static FlussoRendicontazioneIndex toRsIndexModel(it.govpay.bd.model.Fr fr) {
		FlussoRendicontazioneIndex rsModel = new FlussoRendicontazioneIndex();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		rsModel.setRagioneSocialeDominio(fr.getRagioneSocialeDominio());
		rsModel.setRagioneSocialePsp(fr.getRagioneSocialePsp());

		StatoFr stato = fr.getStato();
		if(stato != null) {
			switch (stato) {
			case ACCETTATA:
				rsModel.setStato(StatoFlussoRendicontazione.ACQUISITO);
				break;
			case ANOMALA:
				rsModel.setStato(StatoFlussoRendicontazione.ANOMALO);
				break;
			case RIFIUTATA:
				rsModel.setStato(StatoFlussoRendicontazione.RIFIUTATO);
				break;
			}
		}

		return rsModel;
	}

	public static it.govpay.backoffice.v1.beans.Rendicontazione toRendicontazioneRsModel(it.govpay.bd.viste.model.Rendicontazione dto) throws ServiceException, IOException, ValidationException {
		it.govpay.backoffice.v1.beans.Rendicontazione rsModel = new it.govpay.backoffice.v1.beans.Rendicontazione();

		Rendicontazione rendicontazione = dto.getRendicontazione();

		rsModel.setIuv(rendicontazione.getIuv());
		rsModel.setIur(rendicontazione.getIur());
		if(rendicontazione.getIndiceDati()!=null) {
			rsModel.setIndice(new BigDecimal(rendicontazione.getIndiceDati()));
		} else {
			rsModel.setIndice(BigDecimal.ONE);
		}

		rsModel.setImporto(rendicontazione.getImporto());

		if(rendicontazione.getEsito() != null)
			rsModel.setEsito(new BigDecimal(rendicontazione.getEsito().getCodifica()));
		rsModel.setData(rendicontazione.getData());
		if(rendicontazione.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(it.govpay.model.Rendicontazione.Anomalia anomalia: rendicontazione.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		rsModel.setRiscossione(RiscossioniConverter.toRsModel(dto.getPagamento(), dto.getSingoloVersamento(), dto.getVersamento(), dto.getRpt(), dto.getIncasso()));
		return rsModel;
	}

	public static it.govpay.backoffice.v1.beans.Rendicontazione toRendicontazioneRsModel(Rendicontazione rendicontazione, SingoloVersamento singoloVersamento) throws ServiceException, IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		it.govpay.backoffice.v1.beans.Rendicontazione rsModel = new it.govpay.backoffice.v1.beans.Rendicontazione();
		rsModel.setIuv(rendicontazione.getIuv());
		rsModel.setIur(rendicontazione.getIur());
		if(rendicontazione.getIndiceDati()!=null) {
			rsModel.setIndice(new BigDecimal(rendicontazione.getIndiceDati()));
		} else {
			rsModel.setIndice(BigDecimal.ONE);
		}

		rsModel.setImporto(rendicontazione.getImporto());

		if(rendicontazione.getEsito() != null)
			rsModel.setEsito(new BigDecimal(rendicontazione.getEsito().getCodifica()));
		rsModel.setData(rendicontazione.getData());
		if(rendicontazione.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(it.govpay.model.Rendicontazione.Anomalia anomalia: rendicontazione.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		Rpt rpt = null;
		Incasso incasso = null;
		if(rendicontazione.getPagamento(null) != null) {
			if(singoloVersamento == null)
				singoloVersamento = rendicontazione.getPagamento(null).getSingoloVersamento(null);
			rpt = rendicontazione.getPagamento(null).getRpt(null);
			incasso = rendicontazione.getPagamento(null).getIncasso(null);
		} else {
			if(singoloVersamento == null) {
				singoloVersamento = rendicontazione.getSingoloVersamento(configWrapper);
			}
		}
		Versamento versamento = singoloVersamento.getVersamentoBD(null);

		rsModel.setRiscossione(RiscossioniConverter.toRsModel(rendicontazione.getPagamento(null), singoloVersamento, versamento, rpt, incasso));
		return rsModel;
	}
}
