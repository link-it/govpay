package it.govpay.bd.model.converter;

import it.govpay.bd.BasicBD;
import it.govpay.model.Utenza;

public class UtenzaConverter {

    public static Utenza toDTO(it.govpay.orm.Utenza vo, BasicBD bd) {
        Utenza dto = new Utenza();
        dto.setPrincipal(vo.getPrincipal());
        dto.setId(vo.getId());
        dto.setAbilitato(vo.isAbilitato());
        
//		List<String> listaCodDomini = new ArrayList<String>();
//		if(vo.i != null && ids.size() > 0) {
//				
//			}
//			
//			return listaCodDomini;
//		}
        
        
        
        return dto;
}

public static it.govpay.orm.Utenza toVO(Utenza dto) {
		it.govpay.orm.Utenza vo = new it.govpay.orm.Utenza();
        vo.setId(dto.getId());
        vo.setPrincipal(dto.getPrincipal());
        vo.setAbilitato(dto.isAbilitato());
        return vo;
}
	
}
