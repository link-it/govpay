package it.govpay.bd.model.converter;

import it.govpay.model.Rendicontazione;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdPagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazioneConverter {

	public static Rendicontazione toDTO(it.govpay.orm.Rendicontazione vo) throws ServiceException {
		Rendicontazione dto = new Rendicontazione();
		
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setIur(vo.getIur());
		dto.setImportoPagato(new BigDecimal(vo.getImportoPagato()));
		dto.setData(vo.getData());
		dto.setEsito(EsitoRendicontazione.toEnum(vo.getEsito()));
		dto.setStato(StatoRendicontazione.valueOf(vo.getStato()));
	
		//TODO Nardi
//		List<Anomalia> anomalie;
		
		dto.setIdFr(vo.getIdFR().getId());
		if(vo.getIdPagamento() != null)
			dto.setIdPagamento(vo.getIdPagamento().getId());
		
		return dto;
	}

	public static it.govpay.orm.Rendicontazione toVO(Rendicontazione dto) {
		it.govpay.orm.Rendicontazione vo = new it.govpay.orm.Rendicontazione();
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setIur(dto.getIur());
		vo.setImportoPagato(dto.getImportoPagato().doubleValue());
		vo.setData(dto.getData());
		vo.setEsito(dto.getEsito().getCodifica());
		vo.setStato(dto.getStato().toString());
	
		//TODO Nardi
//		List<Anomalia> anomalie;
		
		IdFr idFr = new IdFr();
		idFr.setId(dto.getIdFr());
		vo.setIdFR(idFr);
		if(dto.getIdPagamento() != null) {
			
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(dto.getIdPagamento());
			idPagamento.setIdPagamento(dto.getIdPagamento());
			vo.setIdPagamento(idPagamento);
		}
		return vo;
	}

	public static List<Rendicontazione> toDTO(
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst) throws ServiceException {
		List<Rendicontazione> dto = new ArrayList<Rendicontazione>();
		for(it.govpay.orm.Rendicontazione vo : rendicontazioneVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
