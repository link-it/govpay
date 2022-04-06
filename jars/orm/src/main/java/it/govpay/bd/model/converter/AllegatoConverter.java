package it.govpay.bd.model.converter;

import it.govpay.bd.model.Allegato;
import it.govpay.orm.IdVersamento;

public class AllegatoConverter {

	public static Allegato toDTO(it.govpay.orm.Allegato vo) {
		Allegato dto = new Allegato();
		dto.setId(vo.getId());
//		dto.setAuthorizationToken(vo.getAuthorizationToken());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setNome(vo.getNome());
		dto.setTipo(vo.getTipo());
		if(vo.getIdVersamento() != null) {
			dto.setIdVersamento(vo.getIdVersamento().getId());
		}
		dto.setRawContenuto(vo.getRawContenuto());

		return dto;
	}

	public static it.govpay.orm.Allegato toVO(Allegato dto)  {
		it.govpay.orm.Allegato vo = new it.govpay.orm.Allegato();
		vo.setId(dto.getId());
		
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setNome(dto.getNome());
		vo.setTipo(dto.getTipo());
		if(dto.getIdVersamento() != null) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		vo.setRawContenuto(dto.getRawContenuto());
		
		return vo;
	}
}
