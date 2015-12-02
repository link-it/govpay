/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.bd.model.ContoAccredito;
import it.govpay.orm.IdDominio;

import java.util.ArrayList;
import java.util.List;

public class ContoAccreditoConverter {

	public static List<ContoAccredito> toDTOList(List<it.govpay.orm.ContoAccredito> applicazioneLst) {
		List<ContoAccredito> lstDTO = new ArrayList<ContoAccredito>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.ContoAccredito applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static ContoAccredito toDTO(it.govpay.orm.ContoAccredito vo) {
		ContoAccredito dto = new ContoAccredito();
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setIdFlusso(vo.getIdFlusso());
		dto.setDataOraPubblicazione(vo.getDataOraPubblicazione());
		dto.setDataOraInizioValidita(vo.getDataOraInizioValidita());
		dto.setXml(vo.getXml());

		return dto;
	}

	public static it.govpay.orm.ContoAccredito toVO(ContoAccredito dto) {
		it.govpay.orm.ContoAccredito vo = new it.govpay.orm.ContoAccredito();
		vo.setId(dto.getId());
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		vo.setIdFlusso(dto.getIdFlusso());
		vo.setDataOraPubblicazione(dto.getDataOraPubblicazione());
		vo.setDataOraInizioValidita(dto.getDataOraInizioValidita());
		vo.setXml(dto.getXml());

		return vo;
	}

}
