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
import java.util.Properties;

import it.govpay.bd.model.Configurazione;
import it.govpay.core.exceptions.IOException;

public class ConfigurazioneConverter {

	public static Configurazione toDTO(List<it.govpay.orm.Configurazione> voList) {
		Configurazione dto = new Configurazione();

		if(voList != null && !voList.isEmpty()) {
			for(it.govpay.orm.Configurazione vo: voList){

				if(Configurazione.KEY_GIORNALE_EVENTI.equals(vo.getNome())){
					dto.setGiornaleEventi(vo.getValore());
				} else if(Configurazione.KEY_TRACCIATO_CSV.equals(vo.getNome())){
					dto.setTracciatoCSV(vo.getValore());
				} else if(Configurazione.KEY_HARDENING.equals(vo.getNome())){
					dto.setConfHardening(vo.getValore());
				} else if(Configurazione.KEY_MAIL_BATCH.equals(vo.getNome())){
					dto.setMailBatch(vo.getValore());
				} else if(Configurazione.KEY_AVVISATURA_MAIL.equals(vo.getNome())){
					dto.setAvvisaturaMail(vo.getValore());
				} else if(Configurazione.KEY_AVVISATURA_APP_IO.equals(vo.getNome())){
					dto.setAvvisaturaAppIo(vo.getValore());
				}  else if(Configurazione.KEY_APP_IO_BATCH.equals(vo.getNome())){
					dto.setAppIOBatch(vo.getValore());
				} else  {
					// carico tutte le properties rimanenti non categorizzate
					dto.getProperties().setProperty(vo.getNome(), vo.getValore());
				}
			}
		}

		return dto;
	}

	public static List<it.govpay.orm.Configurazione> toVOList(Configurazione dto) throws IOException {

		List<it.govpay.orm.Configurazione> voList = new ArrayList<>();

		it.govpay.orm.Configurazione voGde = new it.govpay.orm.Configurazione();
		voGde.setNome(Configurazione.KEY_GIORNALE_EVENTI);
		voGde.setValore(dto.getGiornaleJson());
		voList.add(voGde);

		it.govpay.orm.Configurazione votracciatoCsv = new it.govpay.orm.Configurazione();
		votracciatoCsv.setNome(Configurazione.KEY_TRACCIATO_CSV);
		votracciatoCsv.setValore(dto.getTracciatoCsvJson());
		voList.add(votracciatoCsv);
		
		it.govpay.orm.Configurazione voHardening = new it.govpay.orm.Configurazione();
		voHardening.setNome(Configurazione.KEY_HARDENING);
		voHardening.setValore(dto.getHardeningJson());
		voList.add(voHardening);
		
		it.govpay.orm.Configurazione voMailBatch = new it.govpay.orm.Configurazione();
		voMailBatch.setNome(Configurazione.KEY_MAIL_BATCH);
		voMailBatch.setValore(dto.getBatchSpedizioneEmailJson());
		voList.add(voMailBatch);
		
		it.govpay.orm.Configurazione voAvvisaturaMail = new it.govpay.orm.Configurazione();
		voAvvisaturaMail.setNome(Configurazione.KEY_AVVISATURA_MAIL);
		voAvvisaturaMail.setValore(dto.getAvvisaturaViaMailJson());
		voList.add(voAvvisaturaMail);
		
		it.govpay.orm.Configurazione voAvvisaturaAppIo = new it.govpay.orm.Configurazione();
		voAvvisaturaAppIo.setNome(Configurazione.KEY_AVVISATURA_APP_IO);
		voAvvisaturaAppIo.setValore(dto.getAvvisaturaViaAppIoJson());
		voList.add(voAvvisaturaAppIo);
		
		it.govpay.orm.Configurazione voAppIOBatch = new it.govpay.orm.Configurazione();
		voAppIOBatch.setNome(Configurazione.KEY_APP_IO_BATCH);
		voAppIOBatch.setValore(dto.getBatchSpedizioneAppIoJson());
		voList.add(voAppIOBatch);
		
		Properties properties = dto.getProperties();
		if(!properties.isEmpty()) {
			for (Object keyObj : properties.keySet()) {
				String key = (String) keyObj;
				it.govpay.orm.Configurazione vo = new it.govpay.orm.Configurazione();
				vo.setNome(key);
				vo.setValore(properties.getProperty(key));
				voList.add(vo);
			}
		}

		return voList;
	}
}
