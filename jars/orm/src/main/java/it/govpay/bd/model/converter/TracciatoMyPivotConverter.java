package it.govpay.bd.model.converter;

import it.govpay.bd.model.TracciatoMyPivot;
import it.govpay.model.TracciatoMyPivot.STATO_ELABORAZIONE;
import it.govpay.orm.IdDominio;

public class TracciatoMyPivotConverter {

	public static TracciatoMyPivot toDTO(it.govpay.orm.TracciatoMyPivot vo) {
		TracciatoMyPivot dto = new TracciatoMyPivot();
		dto.setId(vo.getId());
		dto.setAuthorizationToken(vo.getAuthorizationToken());
		dto.setBeanDati(vo.getBeanDati());
		dto.setDataCaricamento(vo.getDataCaricamento());
		dto.setDataCompletamento(vo.getDataCompletamento());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setDataRtA(vo.getDataRtA());
		dto.setDataRtDa(vo.getDataRtDa());
		if(vo.getIdDominio() != null) {
			dto.setIdDominio(vo.getIdDominio().getId());
		}
		dto.setNomeFile(vo.getNomeFile());
		dto.setRawContenuto(vo.getRawContenuto());
		dto.setRequestToken(vo.getRequestToken());
		dto.setStato(STATO_ELABORAZIONE.valueOf(vo.getStato()));
		dto.setUploadUrl(vo.getUploadUrl());

		return dto;
	}

	public static it.govpay.orm.TracciatoMyPivot toVO(TracciatoMyPivot dto)  {
		it.govpay.orm.TracciatoMyPivot vo = new it.govpay.orm.TracciatoMyPivot();
		vo.setId(dto.getId());
		
		vo.setAuthorizationToken(dto.getAuthorizationToken());
		vo.setBeanDati(dto.getBeanDati());
		vo.setDataCaricamento(dto.getDataCaricamento());
		vo.setDataCompletamento(dto.getDataCompletamento());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setDataRtA(dto.getDataRtA());
		vo.setDataRtDa(dto.getDataRtDa());
		if(dto.getIdDominio() != null) {
			IdDominio idDominio = new IdDominio();
			idDominio.setId(dto.getIdDominio());
			vo.setIdDominio(idDominio);
		}
		vo.setNomeFile(dto.getNomeFile());
		vo.setRawContenuto(dto.getRawContenuto());
		vo.setRequestToken(dto.getRequestToken());
		vo.setStato(dto.getStato().name());
		vo.setUploadUrl(dto.getUploadUrl());

		return vo;
	}

}
