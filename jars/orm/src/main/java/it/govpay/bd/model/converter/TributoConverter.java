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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Tributo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.TipoTributo;

public class TributoConverter {
	
	private TributoConverter() {}
	
	public static List<Tributo> toDTOList(List<it.govpay.orm.Tributo> lstVO, BDConfigWrapper configWrapper) throws ServiceException, CodificaInesistenteException {
		List<Tributo> lst = new ArrayList<>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.Tributo vo: lstVO) {
				lst.add(toDTO(vo, configWrapper));
			}
		}
		return lst;
	}

	public static Tributo toDTO(it.govpay.orm.Tributo vo, BDConfigWrapper configWrapper) throws ServiceException, CodificaInesistenteException {
		
		Tributo dto = new Tributo();
		
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setCodTributo(vo.getTipoTributo().getCodTributo());
		dto.setAbilitato(vo.getAbilitato());
		dto.setDescrizione(vo.getTipoTributo().getDescrizione()); 
		dto.setIdTipoTributo(vo.getTipoTributo().getId()); 
		if(vo.getIdIbanAccredito() != null)
			dto.setIbanAccredito(configWrapper, vo.getIdIbanAccredito().getId());
		if(vo.getIdIbanAppoggio() != null)
			dto.setIbanAppoggio(configWrapper, vo.getIdIbanAppoggio().getId());
		if(vo.getTipoContabilita() != null)
			dto.setTipoContabilitaCustom(TipoContabilita.toEnum(vo.getTipoContabilita()));
		dto.setCodContabilitaCustom(vo.getCodiceContabilita());
		
		if(vo.getTipoTributo().getTipoContabilita() != null) 
			dto.setTipoContabilitaDefault(TipoContabilita.toEnum(vo.getTipoTributo().getTipoContabilita()));
		dto.setCodContabilitaDefault(vo.getTipoTributo().getCodContabilita());
		
		return dto;
	}

	public static it.govpay.orm.Tributo toVO(it.govpay.model.Tributo dto) {
		it.govpay.orm.Tributo vo = new it.govpay.orm.Tributo();
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		
		
		TipoTributo tipoTributo = new TipoTributo();
		tipoTributo.setId(dto.getIdTipoTributo());
		tipoTributo.setCodTributo(dto.getCodTributo());
		tipoTributo.setDescrizione(dto.getDescrizione());
		
		if(dto.getTipoContabilitaCustom() != null)
			vo.setTipoContabilita(dto.getTipoContabilitaCustom().getCodifica());
		vo.setCodiceContabilita(dto.getCodContabilitaCustom());
		
		if(dto.getTipoContabilitaDefault() != null)
			tipoTributo.setTipoContabilita(dto.getTipoContabilitaDefault().getCodifica());
		tipoTributo.setCodContabilita(dto.getCodContabilitaDefault());
		
		vo.setTipoTributo(tipoTributo);

		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		if(dto.getIdIbanAccredito() != null) {
			IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
			idIbanAccredito.setId(dto.getIdIbanAccredito());
			vo.setIdIbanAccredito(idIbanAccredito);
		}
		
		if(dto.getIdIbanAppoggio() != null) {
			IdIbanAccredito idIbanAppoggio = new IdIbanAccredito();
			idIbanAppoggio.setId(dto.getIdIbanAppoggio());
			vo.setIdIbanAppoggio(idIbanAppoggio);
		}
		
		return vo;
	}


}
