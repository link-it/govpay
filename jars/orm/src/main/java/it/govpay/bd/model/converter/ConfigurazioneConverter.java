package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.model.Configurazione;

public class ConfigurazioneConverter {

	public static Configurazione toDTO(List<it.govpay.orm.Configurazione> voList) {
		Configurazione dto = new Configurazione();

		if(voList != null && !voList.isEmpty()) {
			for(it.govpay.orm.Configurazione vo: voList){

				if(Configurazione.GIORNALE_EVENTI.equals(vo.getNome())){
					dto.setGiornaleEventi(vo.getValore());
				}
				else if(Configurazione.TRACCIATO_CSV.equals(vo.getNome())){
					dto.setTracciatoCSV(vo.getValore());
				} else if(Configurazione.HARDENING.equals(vo.getNome())){
					dto.setConfHardening(vo.getValore());
				} else if(Configurazione.MAIL_BATCH.equals(vo.getNome())){
					dto.setMailBatch(vo.getValore());
				} else if(Configurazione.MAIL_PROMEMORIA.equals(vo.getNome())){
					dto.setMailPromemoria(vo.getValore());
				} else if(Configurazione.MAIL_RICEVUTA.equals(vo.getNome())){
					dto.setMailRicevuta(vo.getValore());
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
		
		it.govpay.orm.Configurazione voPromemoriaMail = new it.govpay.orm.Configurazione();
		voPromemoriaMail.setNome(Configurazione.MAIL_PROMEMORIA);
		voPromemoriaMail.setValore(dto.getPromemoriaMailJson());
		voList.add(voPromemoriaMail);
		
		it.govpay.orm.Configurazione voRicevutaMail = new it.govpay.orm.Configurazione();
		voRicevutaMail.setNome(Configurazione.MAIL_RICEVUTA);
		voRicevutaMail.setValore(dto.getRicevutaMailJson());
		voList.add(voRicevutaMail);
		
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
