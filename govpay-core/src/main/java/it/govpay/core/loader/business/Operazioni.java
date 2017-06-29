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
package it.govpay.core.loader.business;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.loader.model.Operazione;
import it.govpay.bd.loader.model.OperazioneAnnullamento;
import it.govpay.bd.loader.model.OperazioneCaricamento;
import it.govpay.core.business.model.LeggiOperazioneDTO;
import it.govpay.core.business.model.LeggiOperazioneDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.loader.timers.model.AbstractOperazioneResponse;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoResponse;
import it.govpay.core.loader.utils.AcquisizioneUtils;

public class Operazioni extends BasicBD {

	public Operazioni(BasicBD basicBD) {
		super(basicBD);
	}

	public LeggiOperazioneDTOResponse leggiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws NotAuthorizedException, ServiceException {
		try {
			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();

			OperazioniBD operazioniBD = new OperazioniBD(this);

			Operazione operazione = operazioniBD.getOperazione(leggiOperazioneDTO.getId());

			AcquisizioneUtils acquisizioneUtils = new AcquisizioneUtils();

			switch (operazione.getTipoOperazione()) {
			case ADD:
				CaricamentoRequest caricamentoRequest = (CaricamentoRequest) acquisizioneUtils.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
				AbstractOperazioneResponse abstractOperazioneResponse = acquisizioneUtils.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getDatiRisposta());
				CaricamentoResponse caricamentoResponse = (abstractOperazioneResponse instanceof CaricamentoResponse) ?  (CaricamentoResponse) abstractOperazioneResponse : null;

				OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);

				operazioneCaricamento.setAnagraficaDebitore(caricamentoRequest.getAnagraficaDebitore());
				operazioneCaricamento.setBundleKey(caricamentoRequest.getBundleKey());
				operazioneCaricamento.setCausale(caricamentoRequest.getCausale());
				operazioneCaricamento.setCfDebitore(caricamentoRequest.getCfDebitore());
				operazioneCaricamento.setCodDominio(caricamentoRequest.getCodDominio());
				operazioneCaricamento.setCodTributo(caricamentoRequest.getCodTributo());
				operazioneCaricamento.setIdDebito(caricamentoRequest.getIdDebito());
				operazioneCaricamento.setImporto(caricamentoRequest.getImporto());
				operazioneCaricamento.setNote(caricamentoRequest.getNote());
				operazioneCaricamento.setScadenza(caricamentoRequest.getScadenza());

				if(caricamentoResponse != null) {
					operazioneCaricamento.setIuv(caricamentoResponse.getIuv());
					operazioneCaricamento.setBarCode(caricamentoResponse.getBarCode());
					operazioneCaricamento.setQrCode(caricamentoResponse.getQrCode());
				}

				leggiOperazioneDTOResponse.setOperazione(operazioneCaricamento);
				break;
			case DEL:
				AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) acquisizioneUtils.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
				//AnnullamentoResponse annullamentoResponse = (AnnullamentoResponse) acquisizioneUtils.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getDatiRisposta());

				OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);

				operazioneAnnullamento.setMotivoAnnullamento(annullamentoRequest.getMotivoAnnullamento());

				leggiOperazioneDTOResponse.setOperazione(operazioneAnnullamento);
				break;
			case N_V:
			default:
				leggiOperazioneDTOResponse.setOperazione(operazione);
				break;
			}

			return leggiOperazioneDTOResponse;
		} catch (NotFoundException e) {
			return null;
		} 
	}
}
