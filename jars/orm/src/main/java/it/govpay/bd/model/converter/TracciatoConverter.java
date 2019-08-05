package it.govpay.bd.model.converter;

import it.govpay.bd.model.Tracciato;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.IdOperatore;

public class TracciatoConverter {

	public static Tracciato toDTO(it.govpay.orm.Tracciato vo) {
		Tracciato dto = new Tracciato();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setTipo(TIPO_TRACCIATO.valueOf(vo.getTipo()));
		dto.setStato(STATO_ELABORAZIONE.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setDataCaricamento(vo.getDataCaricamento());
		dto.setDataCompletamento(vo.getDataCompletamento());
		dto.setBeanDati(vo.getBeanDati());
		dto.setFileNameRichiesta(vo.getFileNameRichiesta());
		dto.setRawRichiesta(vo.getRawRichiesta());
		dto.setFileNameEsito(vo.getFileNameEsito());
		dto.setRawEsito(vo.getRawEsito());
		if(vo.getIdOperatore() != null) {
			dto.setIdOperatore(vo.getIdOperatore().getId());
		}
		
		dto.setFormato(FORMATO_TRACCIATO.valueOf(vo.getFormato()));
		dto.setCodTipoVersamento(vo.getCodTipoVersamento());

		return dto;
	}

	public static it.govpay.orm.Tracciato toVO(Tracciato dto)  {
		it.govpay.orm.Tracciato vo = new it.govpay.orm.Tracciato();
		vo.setId(dto.getId());

		vo.setCodDominio(dto.getCodDominio());
		vo.setTipo(dto.getTipo().name());
		vo.setStato(dto.getStato().name());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setDataCaricamento(dto.getDataCaricamento());
		vo.setDataCompletamento(dto.getDataCompletamento());
		vo.setBeanDati(dto.getBeanDati());
		vo.setFileNameRichiesta(dto.getFileNameRichiesta());
		vo.setRawRichiesta(dto.getRawRichiesta());
		vo.setFileNameEsito(dto.getFileNameEsito());
		vo.setRawEsito(dto.getRawEsito());
		
		if(dto.getIdOperatore() != null) {
			IdOperatore idOperatore = new IdOperatore();
			idOperatore.setId(dto.getIdOperatore());
			vo.setIdOperatore(idOperatore);
		}

		vo.setFormato(dto.getFormato().name());
		vo.setCodTipoVersamento(dto.getCodTipoVersamento());
		

		return vo;
	}

}
