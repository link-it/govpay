/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.Utilities;

import it.govpay.bd.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.orm.ACL;
import it.govpay.orm.IdUtenza;

public class AclConverter {

	public static Acl toDTO(ACL vo) throws ServiceException {
		Acl dto = new Acl();
		dto.setDiritti(vo.getDiritti());
		dto.setListaDiritti(vo.getDiritti());
		if(vo.getIdUtenza() != null)
			dto.setIdUtenza(vo.getIdUtenza().getId());
		dto.setRuolo(vo.getRuolo());
		dto.setServizio(Servizio.toEnum(vo.getServizio()));
		dto.setId(vo.getId());
		return dto;
	}

	public static it.govpay.orm.ACL toVO(Acl dto) throws ServiceException {
		ACL vo = new ACL();
		vo.setDiritti(dto.getListaDirittiString()); 
		vo.setServizio(dto.getServizio().getCodifica());
		
		if(dto.getUtenzaPrincipal() != null) {
			IdUtenza idUtenza = new IdUtenza();
			idUtenza.setId(dto.getIdUtenza());
			try {
				idUtenza.setPrincipal(Utilities.formatSubject(dto.getUtenzaPrincipal()));
			} catch (Exception e) {
				idUtenza.setPrincipal(dto.getUtenzaPrincipal());
			}
			try {
				idUtenza.setPrincipalOriginale(Utilities.formatSubject(dto.getUtenzaPrincipalOriginale()));
			} catch (Exception e) {
				idUtenza.setPrincipalOriginale(dto.getUtenzaPrincipalOriginale());
			}
			idUtenza.setAbilitato(dto.isUtenzaAbilitato());
			
			vo.setIdUtenza(idUtenza);
		}
		vo.setRuolo(dto.getRuolo());
		vo.setId(dto.getId());
		return vo;
	}
}
