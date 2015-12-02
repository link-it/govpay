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

import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Mail.StatoSpedizione;
import it.govpay.bd.model.Mail.TipoMail;
import it.govpay.orm.IdTracciato;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailConverter {

	private static String CC_SEPARATOR = ";";
	public static List<Mail> toDTOList(List<it.govpay.orm.Mail> mailLst) {
		List<Mail> lstDTO = new ArrayList<Mail>();
		if(mailLst != null && !mailLst.isEmpty()) {
			for(it.govpay.orm.Mail mail: mailLst){
				lstDTO.add(toDTO(mail));
			}
		}
		return lstDTO;
	}

	public static Mail toDTO(it.govpay.orm.Mail vo) {
		Mail dto = new Mail();
		dto.setId(vo.getId());
		dto.setMittente(vo.getMittente());
		dto.setDestinatario(vo.getDestinatario());
		dto.setBundleKey(vo.getBundleKey());
		dto.setOggetto(vo.getOggetto());
		dto.setMessaggio(vo.getMessaggio());
		dto.setTipoMail(TipoMail.valueOf(vo.getTipoMail()));
		dto.setIdVersamento(vo.getIdVersamento());
		if(vo.getCc() != null){
			dto.setCc(Arrays.asList(vo.getCc().split(CC_SEPARATOR)));
		}

		dto.setStatoSpedizione(StatoSpedizione.valueOf(vo.getStatoSpedizione()));
		dto.setDataOraUltimaSpedizione(vo.getDataOraUltimaSpedizione());
		dto.setTentativiRispedizione(vo.getTentativiRispedizione());
		
		if(vo.getIdTracciatoRPT() != null)
			dto.setIdTracciatoRPT(vo.getIdTracciatoRPT().getId());
		
		if(vo.getIdTracciatoRT() != null)
			dto.setIdTracciatoRT(vo.getIdTracciatoRT().getId());
		
		return dto;
	}

	public static it.govpay.orm.Mail toVO(Mail dto) {
		it.govpay.orm.Mail vo = new it.govpay.orm.Mail();
		vo.setId(dto.getId());
		vo.setMittente(dto.getMittente());
		vo.setDestinatario(dto.getDestinatario());
		vo.setBundleKey(dto.getBundleKey());
		if(dto.getCc() != null && !dto.getCc().isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(String cc: dto.getCc()) {
				if(sb.length() > 0)
					sb.append(CC_SEPARATOR);
				
				sb.append(cc);
			}
			vo.setCc(sb.toString());
		}
		vo.setOggetto(dto.getOggetto());
		vo.setMessaggio(dto.getMessaggio());
		vo.setTipoMail(dto.getTipoMail().toString());
		vo.setIdVersamento(dto.getIdVersamento());

		vo.setStatoSpedizione(dto.getStatoSpedizione().toString());
		vo.setDataOraUltimaSpedizione(dto.getDataOraUltimaSpedizione());
		vo.setTentativiRispedizione(dto.getTentativiRispedizione());

		if(dto.getIdTracciatoRPT() != null) {
			IdTracciato id = new IdTracciato();
			id.setId(dto.getIdTracciatoRPT());
			vo.setIdTracciatoRPT(id);
		}
			
		
		if(dto.getIdTracciatoRT() != null) {
			IdTracciato id = new IdTracciato();
			id.setId(dto.getIdTracciatoRT());
			vo.setIdTracciatoRT(id);
		}

		return vo;
	}

}
