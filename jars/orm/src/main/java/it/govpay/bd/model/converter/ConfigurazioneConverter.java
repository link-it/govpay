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
				} else  {
					// carico tutte le properties rimanenti non categorizzate
					// oggetti complessi (es.gestione captcha) si inizializzano al bisogno
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
		
		// Non cancellare converte gli oggetti in properties
		dto.preparaSalvataggioConfigurazione();

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
