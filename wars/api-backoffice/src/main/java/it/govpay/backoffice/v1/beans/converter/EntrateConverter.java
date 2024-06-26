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

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.TipoEntrata;
import it.govpay.backoffice.v1.beans.TipoEntrataPost;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.core.exceptions.ValidationException;

public class EntrateConverter {

	public static PutEntrataDTO getPutEntrataDTO(TipoEntrataPost entrataPost, String idEntrata, Authentication user) throws ValidationException {
		PutEntrataDTO entrataDTO = new PutEntrataDTO(user);

		it.govpay.model.TipoTributo tipoTributo = new it.govpay.model.TipoTributo();

		tipoTributo.setCodContabilitaDefault(entrataPost.getCodiceContabilita());

		tipoTributo.setCodTributo(idEntrata);
		tipoTributo.setDescrizione(entrataPost.getDescrizione());
		if(entrataPost.getTipoContabilita() != null) {

			// valore tipo contabilita non valido
			if(TipoContabilita.fromValue(entrataPost.getTipoContabilita()) == null) {
				throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + entrataPost.getTipoContabilita() + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
			}

			entrataPost.setTipoContabilitaEnum(TipoContabilita.fromValue(entrataPost.getTipoContabilita()));

			switch (entrataPost.getTipoContabilitaEnum()) {
			case ALTRO:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SIOPE);
				break;
			case SPECIALE:
			default:
				tipoTributo.setTipoContabilitaDefault(it.govpay.model.Tributo.TipoContabilita.SPECIALE);
				break;
			}
		}

		entrataDTO.setCodTributo(idEntrata);
		entrataDTO.setTipoTributo(tipoTributo);

		return entrataDTO;
	}

	public static TipoEntrata toTipoEntrataRsModel(it.govpay.model.TipoTributo tributo) {
		TipoEntrata rsModel = new TipoEntrata();

		rsModel.codiceContabilita(tributo.getCodContabilitaDefault())
		.descrizione(tributo.getDescrizione())
		.idEntrata(tributo.getCodTributo());

		if(tributo.getTipoContabilitaDefault() != null) {
			switch (tributo.getTipoContabilitaDefault()) {
			case ALTRO:
				rsModel.tipoContabilita(TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				rsModel.tipoContabilita(TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				rsModel.tipoContabilita(TipoContabilita.SIOPE);
				break;
			case SPECIALE:
				rsModel.tipoContabilita(TipoContabilita.SPECIALE);
				break;
			}
		}

		return rsModel;
	}
}
