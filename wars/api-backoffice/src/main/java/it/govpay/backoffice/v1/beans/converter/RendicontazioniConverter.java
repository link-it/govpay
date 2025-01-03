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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.RendicontazioneConFlussoEVocePendenza;
import it.govpay.backoffice.v1.beans.Segnalazione;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.model.Rendicontazione.Anomalia;

public class RendicontazioniConverter {

	public static RendicontazioneConFlussoEVocePendenza toRsModel(it.govpay.bd.viste.model.Rendicontazione rendicontazione, BDConfigWrapper configWrapper) throws ServiceException, ValidationException, IOException {

		RendicontazioneConFlussoEVocePendenza rsModel = new RendicontazioneConFlussoEVocePendenza();

		rsModel.setFlussoRendicontazione(FlussiRendicontazioneConverter.toRsIndexModel(rendicontazione.getFr()));

		if(rendicontazione.getSingoloVersamento() != null) {
			rsModel.setVocePendenza(PendenzeConverter.toVocePendenzaRendicontazioneRsModel(rendicontazione.getSingoloVersamento(), rendicontazione.getVersamento(), configWrapper));
		}

		rsModel.setData(rendicontazione.getRendicontazione().getData());
		if(rendicontazione.getRendicontazione().getEsito() != null)
			rsModel.setEsito(new BigDecimal(rendicontazione.getRendicontazione().getEsito().getCodifica()));
		rsModel.setImporto(rendicontazione.getRendicontazione().getImporto());
		if(rendicontazione.getRendicontazione().getIndiceDati()!=null) {
			rsModel.setIndice(new BigDecimal(rendicontazione.getRendicontazione().getIndiceDati()));
		} else {
			rsModel.setIndice(BigDecimal.ONE);
		}

		rsModel.setIur(rendicontazione.getRendicontazione().getIur());
		rsModel.setIuv(rendicontazione.getRendicontazione().getIuv());

		if(rendicontazione.getRendicontazione().getAnomalie() != null) {
			List<Segnalazione> segnalazioni = new ArrayList<>();
			for(Anomalia anomalia: rendicontazione.getRendicontazione().getAnomalie()) {
				segnalazioni.add(new Segnalazione().codice(anomalia.getCodice()).descrizione(anomalia.getDescrizione()));
			}
			rsModel.setSegnalazioni(segnalazioni);
		}

		return rsModel;
	}
}
