package it.govpay.bd.model.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Configurazione;

public class ConfigurazioneConverter {

	public static Configurazione toDTO(it.govpay.orm.Configurazione vo) throws ServiceException {
		Configurazione dto = new Configurazione();
		dto.setId(vo.getId());
		dto.setGiornaleEventi(vo.getGiornaleEventi());
		return dto;
	}
	
	public static it.govpay.orm.Configurazione toVO(Configurazione dto) {
		it.govpay.orm.Configurazione vo = new it.govpay.orm.Configurazione();
		vo.setId(dto.getId());
		vo.setGiornaleEventi(dto.getGiornaleEventi());
		return vo;
	}
}
