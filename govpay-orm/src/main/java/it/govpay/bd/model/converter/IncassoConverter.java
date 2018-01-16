package it.govpay.bd.model.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Incasso;
import it.govpay.orm.IdApplicazione;

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
			if(vo.getIdApplicazione()!= null)
				dto.setIdApplicazione(vo.getIdApplicazione().getId());
			dto.setImporto(BigDecimal.valueOf(vo.getImporto()));
			dto.setTrn(vo.getTrn());
			
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
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		vo.setImporto(dto.getImporto().doubleValue());
		vo.setTrn(dto.getTrn());
		
		return vo;
}
}
