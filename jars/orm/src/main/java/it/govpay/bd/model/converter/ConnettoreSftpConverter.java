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

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.ConnettoreSftp;

public class ConnettoreSftpConverter {

	public static ConnettoreSftp toDTO(List<it.govpay.orm.Connettore> connettoreLst) {
		ConnettoreSftp dto = new ConnettoreSftp();
		if(connettoreLst != null && !connettoreLst.isEmpty()) {
			for(it.govpay.orm.Connettore connettore: connettoreLst){
				dto = setProprietaConnettoreDTO(dto, connettore);
			}
		}
		return dto;
	}

	private static ConnettoreSftp setProprietaConnettoreDTO(ConnettoreSftp dto, it.govpay.orm.Connettore connettore) {
		dto.setIdConnettore(connettore.getCodConnettore());
		if(ConnettoreSftp.P_USER_NAME_IN.equals(connettore.getCodProprieta())) {
			dto.setHttpUserIn(connettore.getValore());
		}

		if(ConnettoreSftp.P_PASS_NAME_IN.equals(connettore.getCodProprieta())) {
			dto.setHttpPasswIn(connettore.getValore());
		}

		if(ConnettoreSftp.P_HOST_NAME_IN.equals(connettore.getCodProprieta())) {
			dto.setHostIn(connettore.getValore());
		}
		
		if(ConnettoreSftp.P_PORTA_NAME_IN.equals(connettore.getCodProprieta())) {
			dto.setPortaIn(connettore.getValore());
		}
		
		if(ConnettoreSftp.P_USER_NAME_OUT.equals(connettore.getCodProprieta())) {
			dto.setHttpUserOut(connettore.getValore());
		}

		if(ConnettoreSftp.P_PASS_NAME_OUT.equals(connettore.getCodProprieta())) {
			dto.setHttpPasswOut(connettore.getValore());
		}

		if(ConnettoreSftp.P_HOST_NAME_OUT.equals(connettore.getCodProprieta())) {
			dto.setHostOut(connettore.getValore());
		}
		
		if(ConnettoreSftp.P_PORTA_NAME_OUT.equals(connettore.getCodProprieta())) {
			dto.setPortaOut(connettore.getValore());
		}
		
		return dto;
	}

	public static List<it.govpay.orm.Connettore> toVOList(ConnettoreSftp connettore) {
		List<it.govpay.orm.Connettore> voList = new ArrayList<>();
		
		if(connettore.getHttpUserIn() != null && !connettore.getHttpUserIn().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreSftp.P_USER_NAME_IN);
				
			vo.setValore(connettore.getHttpUserIn());
			voList.add(vo);
		}

		if(connettore.getHttpPasswIn() != null && !connettore.getHttpPasswIn().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreSftp.P_PASS_NAME_IN);
			vo.setValore(connettore.getHttpPasswIn());
			voList.add(vo);
		}

		if(connettore.getHostIn() != null && !connettore.getHostIn().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			
			vo.setCodProprieta(ConnettoreSftp.P_HOST_NAME_IN);
			vo.setValore(connettore.getHostIn());
			voList.add(vo);
		}
		
		if(connettore.getPortaIn() != null && !connettore.getPortaIn().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			
			vo.setCodProprieta(ConnettoreSftp.P_PORTA_NAME_IN);
			vo.setValore(connettore.getPortaIn());
			voList.add(vo);
		}

		if(connettore.getHttpUserOut() != null && !connettore.getHttpUserOut().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreSftp.P_USER_NAME_OUT);
			vo.setValore(connettore.getHttpUserOut());
			voList.add(vo);
		}

		if(connettore.getHttpPasswOut() != null && !connettore.getHttpPasswOut().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreSftp.P_PASS_NAME_OUT);

			vo.setValore(connettore.getHttpPasswOut());
			voList.add(vo);
		}
		
		if(connettore.getHostOut() != null && !connettore.getHostOut().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			
			vo.setCodProprieta(ConnettoreSftp.P_HOST_NAME_OUT);
			vo.setValore(connettore.getHostOut());
			voList.add(vo);
		}

		if(connettore.getPortaOut() != null && !connettore.getPortaOut().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			
			vo.setCodProprieta(ConnettoreSftp.P_PORTA_NAME_OUT);
			vo.setValore(connettore.getPortaOut());
			voList.add(vo);
		}

		return voList;
	}

}
