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
package it.govpay.bd.model.converter;

import it.govpay.bd.model.FrApplicazione;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdFr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class FrApplicazioneConverter {

	public static List<FrApplicazione> toDTOList(List<it.govpay.orm.FrApplicazione> lst) throws ServiceException {
		List<FrApplicazione> lstDTO = new ArrayList<FrApplicazione>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.FrApplicazione vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static FrApplicazione toDTO(it.govpay.orm.FrApplicazione vo) throws ServiceException {
		FrApplicazione dto = new FrApplicazione();
		dto.setId(vo.getId());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIdFr(vo.getIdFr().getId());
		dto.setImportoTotalePagamenti(BigDecimal.valueOf(vo.getImportoTotalePagamenti()));
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());
		return dto;
	}

	public static it.govpay.orm.FrApplicazione toVO(FrApplicazione dto) {
		it.govpay.orm.FrApplicazione vo = new it.govpay.orm.FrApplicazione();
		vo.setId(dto.getId());
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		IdFr idFr = new IdFr();
		idFr.setId(dto.getIdFr());
		vo.setIdFr(idFr);
		vo.setImportoTotalePagamenti(dto.getImportoTotalePagamenti().doubleValue());
		vo.setNumeroPagamenti(dto.getNumeroPagamenti());
		return vo;
	}

}
