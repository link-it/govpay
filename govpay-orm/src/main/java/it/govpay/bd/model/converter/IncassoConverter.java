package it.govpay.bd.model.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Incasso;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdOperatore;

public class IncassoConverter {

	public static Incasso toDTO(it.govpay.orm.Incasso vo)throws ServiceException {
			Incasso dto = new Incasso();
			dto.setCausale(vo.getCausale());
			dto.setCodDominio(vo.getCodDominio());
			dto.setDataContabile(vo.getDataContabile());
			dto.setDataIncasso(vo.getDataOraIncasso());
			dto.setDataValuta(vo.getDataValuta());
			dto.setDispositivo(vo.getNomeDispositivo());
			dto.setId(vo.getId());
			if(vo.getIdApplicazione() != null)
				dto.setIdApplicazione(vo.getIdApplicazione().getId());
			if(vo.getIdOperatore() != null)
				dto.setIdApplicazione(vo.getIdOperatore().getId());
			
			dto.setImporto(BigDecimal.valueOf(vo.getImporto()));
			dto.setTrn(vo.getTrn());
			dto.setIbanAccredito(vo.getIbanAccredito()); //TODO
			return dto;
	}

	public static it.govpay.orm.Incasso toVO(Incasso dto)throws ServiceException {
		it.govpay.orm.Incasso vo = new it.govpay.orm.Incasso();
		vo.setCausale(dto.getCausale());
		vo.setCodDominio(dto.getCodDominio());
		vo.setDataContabile(dto.getDataContabile());
		vo.setDataOraIncasso(dto.getDataIncasso());
		vo.setDataValuta(dto.getDataValuta());
		vo.setNomeDispositivo(dto.getDispositivo());
		vo.setId(dto.getId());
		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApplicazione);
		}
		if(dto.getIdOperatore() != null) {
			IdOperatore idOperatore = new IdOperatore();
			idOperatore.setId(dto.getIdOperatore());
			vo.setIdOperatore(idOperatore);
		}
		vo.setImporto(dto.getImporto().doubleValue());
		vo.setTrn(dto.getTrn());
		vo.setIbanAccredito(dto.getIbanAccredito());
		return vo;
}
}
