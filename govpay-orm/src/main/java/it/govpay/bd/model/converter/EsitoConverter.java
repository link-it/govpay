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

import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Esito.StatoSpedizione;
import it.govpay.orm.IdApplicazione;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class EsitoConverter {

	public static List<Esito> toDTOList(List<it.govpay.orm.Esito> enteLst) throws ServiceException {
		List<Esito> lstDTO = new ArrayList<Esito>();
		if(enteLst != null && !enteLst.isEmpty()) {
			for(it.govpay.orm.Esito ente: enteLst){
				lstDTO.add(toDTO(ente));
			}
		}
		return lstDTO;
	}

	public static Esito toDTO(it.govpay.orm.Esito vo) throws ServiceException {
		Esito dto = new Esito();
		dto.setStatoSpedizione(StatoSpedizione.valueOf(vo.getStatoSpedizione()));
		dto.setIuv(vo.getIuv());
		dto.setCodDominio(vo.getCodDominio());
		dto.setDettaglioSpedizione(vo.getDettaglioSpedizione());
		dto.setTentativiSpedizione(vo.getTentativiSpedizione());
		dto.setDataOraCreazione(vo.getDataOraCreazione());
		dto.setDataOraUltimaSpedizione(vo.getDataOraUltimaSpedizione());
		dto.setDataOraProssimaSpedizione(vo.getDataOraProssimaSpedizione());
		dto.setXml(vo.getXml());
		dto.setId(vo.getId());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		return dto;
	}

	public static it.govpay.orm.Esito toVO(Esito dto) {
		it.govpay.orm.Esito vo = new it.govpay.orm.Esito();
		vo.setStatoSpedizione(dto.getStatoSpedizione().toString());
		vo.setIuv(dto.getIuv());
		vo.setCodDominio(dto.getCodDominio());
		vo.setDettaglioSpedizione(dto.getDettaglioSpedizione());
		vo.setTentativiSpedizione(dto.getTentativiSpedizione());
		vo.setDataOraCreazione(dto.getDataOraCreazione());
		vo.setDataOraUltimaSpedizione(dto.getDataOraUltimaSpedizione());
		vo.setDataOraProssimaSpedizione(dto.getDataOraProssimaSpedizione());
		vo.setXml(dto.getXml());
		vo.setId(dto.getId());
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		return vo;
	}

}
