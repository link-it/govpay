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

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Versionabile.Versione;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class PortaleConverter {

	public static Portale toDTO(it.govpay.orm.Portale vo, List<Acl> acls) throws ServiceException {
		Portale dto = new Portale();
		dto.setId(vo.getId());
		dto.setCodPortale(vo.getCodPortale());
		dto.setDefaultCallbackURL(vo.getDefaultCallbackURL());
		dto.setPrincipal(vo.getPrincipal());
		dto.setAbilitato(vo.isAbilitato());
		dto.setTrusted(vo.isTrusted());
		dto.setAcls(acls);
		dto.setVersione(Versione.toEnum(vo.getVersione()));
		return dto;
	}

	public static it.govpay.orm.Portale toVO(Portale dto) {
		it.govpay.orm.Portale vo = new it.govpay.orm.Portale();
		vo.setId(dto.getId());
		vo.setCodPortale(dto.getCodPortale());
		vo.setDefaultCallbackURL(dto.getDefaultCallbackURL());
		vo.setPrincipal(dto.getPrincipal());
		vo.setAbilitato(dto.isAbilitato());
		vo.setTrusted(dto.isTrusted());
		vo.setVersione(dto.getVersione().getLabel()); 
		return vo;
	}

}
