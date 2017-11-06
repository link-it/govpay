package it.govpay.bd.model.converter;

import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.orm.Avviso;

public class AvvisoPagamentoConverter {

	
	public static AvvisoPagamento toDTO(Avviso vo) {
		AvvisoPagamento dto = new AvvisoPagamento();
		
		dto.setCodDominio(vo.getCodDominio());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setPdf(vo.getPdf());
		dto.setStato(StatoAvviso.valueOf(vo.getStato()));
		
		return dto;
	}
	
	public static Avviso toVO(AvvisoPagamento dto) {
		Avviso vo = new Avviso();
		
		vo.setCodDominio(dto.getCodDominio());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setPdf(dto.getPdf());
		vo.setStato(dto.getStato().toString());
		
		return vo;
	}
}
