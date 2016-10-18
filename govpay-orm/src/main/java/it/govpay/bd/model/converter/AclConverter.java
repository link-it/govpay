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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.orm.ACL;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoTributo;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class AclConverter {

	public static Acl toDTO(ACL vo, BasicBD bd) throws ServiceException, NotFoundException {
		Acl dto = new Acl();
		dto.setServizio(Servizio.toEnum(vo.getCodServizio()));
		dto.setTipo(Tipo.toEnum(vo.getCodTipo()));

		if(vo.getIdDominio() != null){
			try{
				dto.setCodDominio(new DominiBD(bd).getDominio(vo.getIdDominio().getId()).getCodDominio());
			} catch(MultipleResultException e) {}
			
			dto.setIdDominio(vo.getIdDominio().getId());
		}
		if(vo.getIdTipoTributo() != null){
			try {
				dto.setCodTributo(new TipiTributoBD(bd).getTipoTributo(vo.getIdTipoTributo().getId()).getCodTributo());
			} catch(MultipleResultException e) {}
			dto.setIdTributo(vo.getIdTipoTributo().getId());
		}
		return dto;
	}

	public static it.govpay.orm.ACL toVO(Acl dto, BasicBD bd) throws ServiceException, NotFoundException {
		ACL vo = new ACL();
		vo.setCodServizio(dto.getServizio().getCodifica());
		vo.setCodTipo(dto.getTipo().getCodifica());

		if(dto.getIdDominio() != null || dto.getCodDominio() != null){
			IdDominio idDominio = new IdDominio();
			if(dto.getIdDominio() == null) {
				try {
					idDominio.setId(new DominiBD(bd).getDominio(dto.getCodDominio()).getId());
				} catch(MultipleResultException e) {}
			} else {
				idDominio.setId(dto.getIdDominio());
			}

			vo.setIdDominio(idDominio);
		}

		if(dto.getIdTributo() != null || dto.getCodTributo() != null){
			IdTipoTributo idTributo = new IdTipoTributo();
			if(dto.getIdTributo() == null){
				try {
					idTributo.setId(new TipiTributoBD(bd).getTipoTributo(dto.getCodTributo()).getId());	
				} catch(MultipleResultException e) {}
			} else { 
				idTributo.setId(dto.getIdTributo());
			}
			vo.setIdTipoTributo(idTributo);
		}

		return vo;
	}
}
