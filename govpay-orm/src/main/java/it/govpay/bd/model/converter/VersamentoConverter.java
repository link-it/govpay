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

import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoRendicontazione;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdEnte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VersamentoConverter {

	public static List<Versamento> toDTOList(List<it.govpay.orm.Versamento> applicazioneLst) {
		List<Versamento> lstDTO = new ArrayList<Versamento>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.Versamento applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Versamento toDTO(it.govpay.orm.Versamento vo) {
		Versamento dto = new Versamento();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setDataScadenza(vo.getDataScadenza());
		dto.setIuv(vo.getIuv());
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setImportoTotale(BigDecimal.valueOf(vo.getImportoTotale()));

		if(vo.getImportoPagato() != null)
			dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setStato(StatoVersamento.valueOf(vo.getStatoVersamento()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());

		if(vo.getStatoRendicontazione() != null)
			dto.setStatoRendicontazione(StatoRendicontazione.valueOf(vo.getStatoRendicontazione()));
		
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIdEnte(vo.getIdEnte().getId());

		return dto;
	}

	public static it.govpay.orm.Versamento toVO(Versamento dto) {
		it.govpay.orm.Versamento vo = new it.govpay.orm.Versamento();
		vo.setId(dto.getId());
		vo.setCodDominio(dto.getCodDominio());
		vo.setDataScadenza(dto.getDataScadenza());
		vo.setIuv(dto.getIuv());
		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());

		vo.setIdApplicazione(idApplicazione);
		
		IdEnte idEnte = new IdEnte();
		idEnte.setId(dto.getIdEnte());

		vo.setIdEnte(idEnte);
		
		vo.setImportoTotale(dto.getImportoTotale().doubleValue());
		
		if(dto.getImportoPagato() != null)
			vo.setImportoPagato(dto.getImportoPagato().doubleValue());
		
		vo.setStatoVersamento(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());

		if(dto.getStatoRendicontazione() != null)
			vo.setStatoRendicontazione(dto.getStatoRendicontazione().toString());
		

		return vo;
	}
}
