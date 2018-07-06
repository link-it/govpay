package it.govpay.bd.model.converter;

import it.govpay.bd.model.Tracciato;

public class TracciatoConverter {

	public static Tracciato toDTO(it.govpay.orm.Tracciato vo) {
		Tracciato dto = new Tracciato();
		dto.setTipo(vo.getTipo());
		dto.setStato(vo.getStato());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setDataCaricamento(vo.getDataCaricamento());
		dto.setDataCompletamento(vo.getDataCompletamento());
		dto.setBeanDati(vo.getBeanDati());
		dto.setFileNameRichiesta(vo.getFileNameRichiesta());
		dto.setRawRichiesta(vo.getRawRichiesta());
		dto.setFileNameEsito(vo.getFileNameEsito());
		dto.setRawEsito(vo.getRawEsito());

		return dto;
	}

	public static it.govpay.orm.Tracciato toVO(it.govpay.model.Tracciato dto)  {
		it.govpay.orm.Tracciato vo = new it.govpay.orm.Tracciato();
		vo.setId(dto.getId());

		vo.setTipo(dto.getTipo());
		vo.setStato(dto.getStato());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setDataCaricamento(dto.getDataCaricamento());
		vo.setDataCompletamento(dto.getDataCompletamento());
		vo.setBeanDati(dto.getBeanDati());
		vo.setFileNameRichiesta(dto.getFileNameRichiesta());
		vo.setRawRichiesta(dto.getRawRichiesta());
		vo.setFileNameEsito(dto.getFileNameEsito());
		vo.setRawEsito(dto.getRawEsito());

		return vo;
	}

}
