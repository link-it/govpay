package it.govpay.bd.model.converter;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Utenza;

public class UtenzaConverter {

	public static Utenza toDTO(it.govpay.orm.Utenza vo, List<Long> utenzaDominioLst, List<Long> utenzaTributoLst, BasicBD bd) throws ServiceException {
		Utenza dto = new Utenza();
		dto.setPrincipal(vo.getPrincipal());
		dto.setPrincipalOriginale(vo.getPrincipalOriginale());

//		dto.setSubjectOriginale(vo.getSubjectOriginale()); 
		
		dto.setId(vo.getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setIdTributi(utenzaTributoLst);
		dto.setIdDomini(utenzaDominioLst);
		dto.getDomini(bd);
		dto.getTributi(bd);

		return dto;
	}

	public static it.govpay.orm.Utenza toVO(it.govpay.model.Utenza dto) {
		it.govpay.orm.Utenza vo = new it.govpay.orm.Utenza();
		vo.setId(dto.getId());
		vo.setPrincipal(dto.getPrincipal());
		vo.setPrincipalOriginale(dto.getPrincipalOriginale());

//		vo.setSubjectOriginale(dto.getSubjectOriginale()); 
		vo.setAbilitato(dto.isAbilitato());
		return vo;
	}

}
