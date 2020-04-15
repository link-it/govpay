package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.model.Configurazione;

public class ConfigurazioneConverter {

	public static Configurazione toDTO(List<it.govpay.orm.Configurazione> voList) {
		Configurazione dto = new Configurazione();

		if(voList != null && !voList.isEmpty()) {
			for(it.govpay.orm.Configurazione vo: voList){

				if(Configurazione.GIORNALE_EVENTI.equals(vo.getNome())){
					dto.setGiornaleEventi(vo.getValore());
				} else if(Configurazione.TRACCIATO_CSV.equals(vo.getNome())){
					dto.setTracciatoCSV(vo.getValore());
				} else if(Configurazione.HARDENING.equals(vo.getNome())){
					dto.setConfHardening(vo.getValore());
				} else if(Configurazione.MAIL_BATCH.equals(vo.getNome())){
					dto.setMailBatch(vo.getValore());
				} else if(Configurazione.AVVISATURA_MAIL.equals(vo.getNome())){
					dto.setAvvisaturaMail(vo.getValore());
				} else if(Configurazione.AVVISATURA_APP_IO.equals(vo.getNome())){
					dto.setAvvisaturaAppIo(vo.getValore());
				}  else if(Configurazione.APP_IO_BATCH.equals(vo.getNome())){
					dto.setAppIOBatch(vo.getValore());
				} else  {
					// carico tutte le properties rimanenti non categorizzate
					dto.getProperties().setProperty(vo.getNome(), vo.getValore());
				}
			}
		}

		return dto;
	}

	public static List<it.govpay.orm.Configurazione> toVOList(Configurazione dto) throws IOException, ServiceException {

		List<it.govpay.orm.Configurazione> voList = new ArrayList<>();

		it.govpay.orm.Configurazione voGde = new it.govpay.orm.Configurazione();
		voGde.setNome(Configurazione.GIORNALE_EVENTI);
		voGde.setValore(dto.getGiornaleJson());
		voList.add(voGde);

		it.govpay.orm.Configurazione votracciatoCsv = new it.govpay.orm.Configurazione();
		votracciatoCsv.setNome(Configurazione.TRACCIATO_CSV);
		votracciatoCsv.setValore(dto.getTracciatoCsvJson());
		voList.add(votracciatoCsv);
		
		it.govpay.orm.Configurazione voHardening = new it.govpay.orm.Configurazione();
		voHardening.setNome(Configurazione.HARDENING);
		voHardening.setValore(dto.getHardeningJson());
		voList.add(voHardening);
		
		it.govpay.orm.Configurazione voMailBatch = new it.govpay.orm.Configurazione();
		voMailBatch.setNome(Configurazione.MAIL_BATCH);
		voMailBatch.setValore(dto.getBatchSpedizioneEmailJson());
		voList.add(voMailBatch);
		
		it.govpay.orm.Configurazione voAvvisaturaMail = new it.govpay.orm.Configurazione();
		voAvvisaturaMail.setNome(Configurazione.AVVISATURA_MAIL);
		voAvvisaturaMail.setValore(dto.getAvvisaturaViaMailJson());
		voList.add(voAvvisaturaMail);
		
		it.govpay.orm.Configurazione voAvvisaturaAppIo = new it.govpay.orm.Configurazione();
		voAvvisaturaAppIo.setNome(Configurazione.AVVISATURA_APP_IO);
		voAvvisaturaAppIo.setValore(dto.getAvvisaturaViaAppIoJson());
		voList.add(voAvvisaturaAppIo);
		
		it.govpay.orm.Configurazione voAppIOBatch = new it.govpay.orm.Configurazione();
		voAppIOBatch.setNome(Configurazione.APP_IO_BATCH);
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
