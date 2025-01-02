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
package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Documento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDominio;

public class DocumentoConverter {
	
	private DocumentoConverter() {}

	public static List<Documento> toDTOList(List<it.govpay.orm.Documento> anagraficaLst) {
		List<Documento> lstDTO = new ArrayList<>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Documento anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Documento toDTO(it.govpay.orm.Documento vo) {
		Documento dto = new Documento();
		dto.setCodDocumento(vo.getCodDocumento());
		dto.setDescrizione(vo.getDescrizione());
		dto.setId(vo.getId());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		return dto;
	}

	public static it.govpay.orm.Documento toVO(Documento dto) {
		it.govpay.orm.Documento vo = new it.govpay.orm.Documento();
		vo.setId(dto.getId());
	
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		vo.setCodDocumento(dto.getCodDocumento());
		vo.setDescrizione(dto.getDescrizione());
		return vo;
	}
}
