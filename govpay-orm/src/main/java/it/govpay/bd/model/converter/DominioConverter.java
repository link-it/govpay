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

import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.Disponibilita.TipoDisponibilita;
import it.govpay.bd.model.Disponibilita.TipoPeriodo;
import it.govpay.bd.model.Dominio;
import it.govpay.orm.IdStazione;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class DominioConverter {

	private static final String FASCIA_SEPARATOR = "\\|";
	private static final String FASCIA_FIELD_SEPARATOR = ",";

	private static final SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
	
	
	public static List<Dominio> toDTOList(List<it.govpay.orm.Dominio> anagraficaLst) throws ServiceException {
		List<Dominio> lstDTO = new ArrayList<Dominio>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Dominio anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Dominio toDTO(it.govpay.orm.Dominio vo) throws ServiceException {
		Dominio dto = new Dominio();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setPluginClass(vo.getPluginClass());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdStazione(vo.getIdStazione().getId());
		dto.setDisponibilita(toDTOListDisponibilita(vo.getDisponibilitaList()));

		return dto;
	}

	public static it.govpay.orm.Dominio toVO(Dominio dto) {
		it.govpay.orm.Dominio vo = new it.govpay.orm.Dominio();
		vo.setId(dto.getId());
		vo.setCodDominio(dto.getCodDominio());
		vo.setRagioneSociale(dto.getRagioneSociale());
		vo.setGln(dto.getGln());
		vo.setPluginClass(dto.getPluginClass());
		vo.setAbilitato(dto.isAbilitato());
		
		IdStazione idStazione = new IdStazione();
		idStazione.setId(dto.getIdStazione());
		vo.setIdStazione(idStazione);
		vo.setDisponibilitaList(toVOList(dto.getDisponibilita()));
		return vo;
	}

	public static List<it.govpay.orm.Disponibilita> toVOList(List<Disponibilita> lstVO) {
		List<it.govpay.orm.Disponibilita> lstDTO = new ArrayList<it.govpay.orm.Disponibilita>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(Disponibilita anagrafica: lstVO){
				lstDTO.add(toVO(anagrafica));
			}
		}
		return lstDTO;

	}

	public static it.govpay.orm.Disponibilita toVO(Disponibilita dto) {
		it.govpay.orm.Disponibilita vo = new it.govpay.orm.Disponibilita();
		vo.setId(dto.getId());
		vo.setTipoPeriodo(dto.getTipoPeriodo().toString());
		vo.setGiorno(dto.getGiorno());
		if(dto.getFasceOrarieLst() != null && !dto.getFasceOrarieLst().isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for(Periodo fascia: dto.getFasceOrarieLst()) {
				if(sb.length() > 0) {
					sb.append(FASCIA_SEPARATOR);
				}
				sb.append(hourFormat.format(fascia.getDa())).append(FASCIA_FIELD_SEPARATOR).append(hourFormat.format(fascia.getA()));
			}
			vo.setFasceOrarie(sb.toString());
		}
		vo.setTipoDisponibilita(dto.getTipoDisponibilita().toString());

		return vo;
	}

	public static List<Disponibilita> toDTOListDisponibilita(List<it.govpay.orm.Disponibilita> lst) throws ServiceException {
		List<Disponibilita> lstDTO = new ArrayList<Disponibilita>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.Disponibilita anagrafica: lst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Disponibilita toDTO(it.govpay.orm.Disponibilita vo) throws ServiceException {
		Disponibilita dto = new Disponibilita();
		
		dto.setId(vo.getId());
		dto.setTipoPeriodo(TipoPeriodo.valueOf(vo.getTipoPeriodo()));
		dto.setGiorno(vo.getGiorno());

		String[] fasce = vo.getFasceOrarie().split(FASCIA_SEPARATOR);
		List<Periodo> periodoLst = new ArrayList<Periodo>();
		for (int i = 0; i < fasce.length; i++) {
			String[] fascia = fasce[i].split(FASCIA_FIELD_SEPARATOR);
			if(fascia.length != 2) {
				throw new ServiceException("Fasce orarie non correttamente salvate");
			}
			try {
				Periodo periodo =   new Periodo();
				periodo.setDa(hourFormat.parse(fascia[0]));
				periodo.setA(hourFormat.parse(fascia[1]));
				periodoLst.add(periodo);
			} catch(Exception e) {
				throw new ServiceException("Fasce orarie non correttamente salvate");
			}
		}
		dto.setFasceOrarieLst(periodoLst);
		
		dto.setTipoDisponibilita(TipoDisponibilita.valueOf(vo.getTipoDisponibilita()));

		return dto;

	}


}
