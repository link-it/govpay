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

import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.MailTemplate.TipoAllegati;

import java.util.ArrayList;
import java.util.List;

public class MailTemplateConverter {

	private static final String ALLEGATI_SEPARATOR = ";";
	
	public static List<MailTemplate> toDTOList(List<it.govpay.orm.MailTemplate> mailLst) {
		List<MailTemplate> lstDTO = new ArrayList<MailTemplate>();
		if(mailLst != null && !mailLst.isEmpty()) {
			for(it.govpay.orm.MailTemplate mail: mailLst){
				lstDTO.add(toDTO(mail));
			}
		}
		return lstDTO;
	}

	public static MailTemplate toDTO(it.govpay.orm.MailTemplate vo) {
		MailTemplate dto = new MailTemplate();
		dto.setId(vo.getId());
		dto.setMittente(vo.getMittente());
		dto.setTemplateOggetto(vo.getTemplateOggetto());
		dto.setTemplateMessaggio(vo.getTemplateMessaggio());
		if(vo.getAllegati() != null){
			String[] allegati = vo.getAllegati().split(ALLEGATI_SEPARATOR);
			List<TipoAllegati> allegatiLst = new ArrayList<MailTemplate.TipoAllegati>();
			for (int i = 0; i < allegati.length; i++) {
				allegatiLst.add(TipoAllegati.valueOf(allegati[i]));
			}
			
			dto.setAllegati(allegatiLst);
		}

		return dto;
	}

	public static it.govpay.orm.MailTemplate toVO(MailTemplate dto) {
		it.govpay.orm.MailTemplate vo = new it.govpay.orm.MailTemplate();
		vo.setId(dto.getId());
		vo.setMittente(dto.getMittente());
		vo.setTemplateOggetto(dto.getTemplateOggetto());
		vo.setTemplateMessaggio(dto.getTemplateMessaggio());
		
		if(dto.getAllegati() != null && !dto.getAllegati().isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(TipoAllegati cc: dto.getAllegati()) {
				if(sb.length() > 0)
					sb.append(ALLEGATI_SEPARATOR);
				
				sb.append(cc.toString());
			}
			vo.setAllegati(sb.toString());
		}

		
		return vo;
	}

}
