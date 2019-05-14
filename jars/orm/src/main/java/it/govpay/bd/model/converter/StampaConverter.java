package it.govpay.bd.model.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Stampa;
import it.govpay.model.Stampa.TIPO;
import it.govpay.orm.IdVersamento;

public class StampaConverter {

	public static Stampa toDTO(it.govpay.orm.Stampa vo) throws ServiceException {
		Stampa dto = new Stampa();
		
		dto.setId(vo.getId());
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setTipo(TIPO.valueOf(vo.getTipo()));
		dto.setPdf(vo.getPdf());

		return dto;
	}
	
	
	public static it.govpay.orm.Stampa toVO(Stampa dto) throws ServiceException {
		it.govpay.orm.Stampa vo = new it.govpay.orm.Stampa();
		
		vo.setId(dto.getId());
		
		if(dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setTipo(dto.getTipo().name());
		vo.setPdf(dto.getPdf());
		
		return vo;
	}
}
