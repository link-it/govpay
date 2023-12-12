/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.openspcoop2.utils.certificate.PrincipalType;

import it.govpay.bd.model.Operatore;
import it.govpay.orm.IdUtenza;

public class OperatoreConverter {


	public static Operatore toDTO(it.govpay.orm.Operatore vo) {
		Operatore dto = new Operatore();
		dto.setId(vo.getId());
		if(vo.getIdUtenza() != null)
			dto.setIdUtenza(vo.getIdUtenza().getId());
		dto.setNome(vo.getNome());
		return dto;
	}

	public static it.govpay.orm.Operatore toVO(Operatore dto) {
		it.govpay.orm.Operatore vo = new it.govpay.orm.Operatore();
		vo.setId(dto.getId());
		IdUtenza idUtenza = new IdUtenza();
		idUtenza.setId(dto.getIdUtenza());
		try {
			idUtenza.setPrincipal(CertificateUtils.formatPrincipal(dto.getPrincipal(), PrincipalType.subject));
		} catch (UtilsException e) {
			idUtenza.setPrincipal(dto.getPrincipal());
		}
		vo.setIdUtenza(idUtenza);
		vo.setNome(dto.getNome());
		return vo;
	}
}
