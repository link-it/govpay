package it.govpay.bd.model.converter;

import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.orm.IdDominio;

public class TracciatoNotificaPagamentiConverter {

	public static TracciatoNotificaPagamenti toDTO(it.govpay.orm.TracciatoNotificaPagamenti vo) {
		TracciatoNotificaPagamenti dto = new TracciatoNotificaPagamenti();
		dto.setId(vo.getId());
//		dto.setAuthorizationToken(vo.getAuthorizationToken());
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
//		dto.setRequestToken(vo.getRequestToken());
		dto.setStato(STATO_ELABORAZIONE.valueOf(vo.getStato()));
//		dto.setUploadUrl(vo.getUploadUrl());

		return dto;
	}

	public static it.govpay.orm.TracciatoNotificaPagamenti toVO(TracciatoNotificaPagamenti dto)  {
		it.govpay.orm.TracciatoNotificaPagamenti vo = new it.govpay.orm.TracciatoNotificaPagamenti();
		vo.setId(dto.getId());
		
//		vo.setAuthorizationToken(dto.getAuthorizationToken());
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
//		vo.setRequestToken(dto.getRequestToken());
		vo.setStato(dto.getStato().name());
//		vo.setUploadUrl(dto.getUploadUrl());

		return vo;
	}

}
