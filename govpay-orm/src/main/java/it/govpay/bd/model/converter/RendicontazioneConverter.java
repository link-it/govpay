package it.govpay.bd.model.converter;

import it.govpay.bd.model.Rendicontazione;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.IdSingoloVersamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazioneConverter {

	public static Rendicontazione toDTO(it.govpay.orm.Rendicontazione vo) throws ServiceException {
		Rendicontazione dto = new Rendicontazione();
		dto.setAnomalie(vo.getAnomalie());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setIur(vo.getIur());
		dto.setIndiceDati(vo.getIndiceDati());
		dto.setImporto(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setData(vo.getData());
		dto.setEsito(EsitoRendicontazione.toEnum(vo.getEsito()));
		dto.setStato(StatoRendicontazione.valueOf(vo.getStato()));
		dto.setIdFr(vo.getIdFR().getId());
		if(vo.getIdPagamento() != null)
			dto.setIdPagamento(vo.getIdPagamento().getId());
		if(vo.getIdSingoloVersamento() != null)
			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		
		return dto;
	}

	public static it.govpay.orm.Rendicontazione toVO(Rendicontazione dto) {
		it.govpay.orm.Rendicontazione vo = new it.govpay.orm.Rendicontazione();
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setIur(dto.getIur());
		vo.setIndiceDati(dto.getIndiceDati());

		vo.setImportoPagato(dto.getImporto().doubleValue());
		vo.setData(dto.getData());
		vo.setEsito(dto.getEsito().getCodifica());
		vo.setStato(dto.getStato().toString());
		vo.setAnomalie(dto.getAnomalieString());
		
		IdFr idFr = new IdFr();
		idFr.setId(dto.getIdFr());
		vo.setIdFR(idFr);
		if(dto.getIdPagamento() != null) {
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(dto.getIdPagamento());
			idPagamento.setIdPagamento(dto.getIdPagamento());
			vo.setIdPagamento(idPagamento);
		}
		
		if(dto.getIdSingoloVersamento() != null) {
			IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
			idSingoloVersamento.setId(dto.getIdSingoloVersamento());
			vo.setIdSingoloVersamento(idSingoloVersamento);
		}
		return vo;
	}

	public static List<Rendicontazione> toDTO(
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst) throws ServiceException {
		List<Rendicontazione> dto = new ArrayList<>();
		for(it.govpay.orm.Rendicontazione vo : rendicontazioneVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
