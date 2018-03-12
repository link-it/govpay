package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.business.model.RichiestaIncassoDTO;
import it.govpay.core.rs.v1.beans.Incasso;
import it.govpay.core.rs.v1.beans.IncassoPost;

public class IncassiConverter {

	
	public static RichiestaIncassoDTO toRichiestaIncassoDTO(IncassoPost incassoPost) {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO();
		dto.setCausale(incassoPost.getCausale());
		dto.setDataValuta(incassoPost.getDataValuta());
		dto.setDataContabile(incassoPost.getDataContabile());
		dto.setImporto(new BigDecimal(incassoPost.getImporto()));
		return dto;
	}
	
	
	public static Incasso toRsModel(it.govpay.bd.model.Incasso i) throws ServiceException {
		Incasso rsModel = new Incasso();
		
		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto().doubleValue());
		rsModel.setId(i.getId()+ "");
		rsModel.setRiscossioni(null);
		
		return rsModel;
	}
}
