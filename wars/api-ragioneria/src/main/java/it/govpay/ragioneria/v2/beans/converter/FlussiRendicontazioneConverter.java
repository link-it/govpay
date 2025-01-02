/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v2.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Rendicontazione;
import it.govpay.core.exceptions.IOException;
import it.govpay.model.Fr.Anomalia;
import it.govpay.model.Fr.StatoFr;
import it.govpay.ragioneria.v2.beans.FlussoRendicontazione;
import it.govpay.ragioneria.v2.beans.FlussoRendicontazioneIndex;
import it.govpay.ragioneria.v2.beans.Segnalazione;
import it.govpay.ragioneria.v2.beans.StatoFlussoRendicontazione;

public class FlussiRendicontazioneConverter {

	public static FlussoRendicontazione toRsModel(it.govpay.bd.model.Fr fr, List<it.govpay.bd.viste.model.Rendicontazione> listaRendicontazioni) throws ServiceException, NotFoundException, IOException {
		FlussoRendicontazione rsModel = new FlussoRendicontazione();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		rsModel.setIdPsp(fr.getCodPsp());
		rsModel.setIdDominio(fr.getCodDominio());

		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		List<it.govpay.ragioneria.v2.beans.Rendicontazione> rendicontazioniLst = new ArrayList<>();
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

	public static FlussoRendicontazioneIndex toRsIndexModel(it.govpay.bd.model.Fr fr) throws ServiceException, NotFoundException {
		FlussoRendicontazioneIndex rsModel = new FlussoRendicontazioneIndex();
		rsModel.setIdFlusso(fr.getCodFlusso());
		rsModel.setDataFlusso(fr.getDataFlusso());
		rsModel.setTrn(fr.getIur());
		rsModel.setDataRegolamento(fr.getDataRegolamento());
		rsModel.setBicRiversamento(fr.getCodBicRiversamento());
		rsModel.setIdDominio(fr.getCodDominio());
		rsModel.setNumeroPagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		rsModel.setImportoTotale(fr.getImportoTotalePagamenti().doubleValue());
		rsModel.setIdPsp(fr.getCodPsp());
		if(fr.getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: fr.getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

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

	public static it.govpay.ragioneria.v2.beans.Rendicontazione toRendicontazioneRsModel(it.govpay.bd.viste.model.Rendicontazione dto) throws ServiceException, NotFoundException, IOException {
		it.govpay.ragioneria.v2.beans.Rendicontazione rsModel = new it.govpay.ragioneria.v2.beans.Rendicontazione();

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

		if(rendicontazione.getPagamento(null) != null)
			rsModel.setRiscossione(RiscossioniConverter.toRsModelIndexOld(rendicontazione.getPagamento(null)));
		return rsModel;
	}

	public static it.govpay.ragioneria.v2.beans.Rendicontazione toRendicontazioneRsModel(Rendicontazione rendicontazione) throws ServiceException, NotFoundException, IOException {
		it.govpay.ragioneria.v2.beans.Rendicontazione rsModel = new it.govpay.ragioneria.v2.beans.Rendicontazione();
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

		if(rendicontazione.getPagamento(null) != null)
			rsModel.setRiscossione(RiscossioniConverter.toRsModelIndexOld(rendicontazione.getPagamento(null)));
		return rsModel;
	}
}
