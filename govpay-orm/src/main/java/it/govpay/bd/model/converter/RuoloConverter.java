package it.govpay.bd.model.converter;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Acl;
import it.govpay.model.Ruolo;

public class RuoloConverter {


	public static Ruolo toDTO(it.govpay.orm.Ruolo vo, List<Acl> acls) throws ServiceException {
		Ruolo dto = new Ruolo();
		
		dto.setId(vo.getId());
		dto.setCodRuolo(vo.getCodRuolo());
		dto.setDescrizione(vo.getDescrizione());
		dto.setAcls(acls);
		
		return dto;
	}

	public static it.govpay.orm.Ruolo toVO(Ruolo dto) throws ServiceException {
		it.govpay.orm.Ruolo vo = new it.govpay.orm.Ruolo();
		vo.setId(dto.getId());
		vo.setCodRuolo(dto.getCodRuolo());
		vo.setDescrizione(dto.getDescrizione());
		
		return vo;
	}

}
