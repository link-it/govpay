/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.openspcoop2.utils.certificate.PrincipalType;

import it.govpay.model.Intermediario;

public class IntermediarioConverter {

	public static List<Intermediario> toDTOList(List<it.govpay.orm.Intermediario> intermediarioLst) {
		List<Intermediario> lstDTO = new ArrayList<>();
		if(intermediarioLst != null && !intermediarioLst.isEmpty()) {
			for(it.govpay.orm.Intermediario intermediario: intermediarioLst){
				lstDTO.add(toDTO(intermediario));
			}
		}
		return lstDTO;
	}

	public static Intermediario toDTO(it.govpay.orm.Intermediario vo) {
		Intermediario dto = new Intermediario();
		dto.setCodIntermediario(vo.getCodIntermediario());
		dto.setId(vo.getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setDenominazione(vo.getDenominazione());
		dto.setPrincipal(vo.getPrincipal());
		dto.setPrincipalOriginale(vo.getPrincipalOriginale());
		return dto; 
	}

	public static it.govpay.orm.Intermediario toVO(Intermediario dto) {
		it.govpay.orm.Intermediario vo = new it.govpay.orm.Intermediario();
		vo.setCodIntermediario(dto.getCodIntermediario());
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		vo.setDenominazione(dto.getDenominazione());
		vo.setCodConnettorePdd(dto.getCodIntermediario());
		
		if(dto.getConnettorePdd()!= null) {
			dto.getConnettorePdd().setIdConnettore(dto.getCodIntermediario());
		}
		
		if(dto.getConnettoreSftp()!=null)
			vo.setCodConnettoreFtp(dto.getConnettoreSftp().getIdConnettore());
		
		try {
			vo.setPrincipal(CertificateUtils.formatPrincipal(dto.getPrincipal(), PrincipalType.subject));
		} catch (UtilsException e) {
			vo.setPrincipal(dto.getPrincipal());
		}
		vo.setPrincipalOriginale(dto.getPrincipalOriginale());
		
		return vo;
	}

}
