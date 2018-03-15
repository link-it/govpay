package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.rs.v1.beans.Incasso;
import it.govpay.core.rs.v1.beans.IncassoPost;
import it.govpay.core.rs.v1.beans.base.Riscossione;
import it.govpay.model.IAutorizzato;

public class IncassiConverter {

	
	public static RichiestaIncassoDTO toRichiestaIncassoDTO(IncassoPost incassoPost, String idDominio, IAutorizzato user) {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO(user);
		dto.setCausale(incassoPost.getCausale());
		dto.setDataValuta(incassoPost.getDataValuta());
		dto.setDataContabile(incassoPost.getDataContabile());
		dto.setImporto(BigDecimal.valueOf(incassoPost.getImporto().doubleValue()));
		dto.setCodDominio(idDominio);
		return dto;
	}
	
	
	public static Incasso toRsModel(it.govpay.bd.model.Incasso i) throws ServiceException {
		Incasso rsModel = new Incasso();
		
		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto().doubleValue());
		rsModel.setId(i.getId()+ "");
		
		if(i.getPagamenti(null)!= null) {
			List<Riscossione> riscossioni = new ArrayList<Riscossione>();
			for (Pagamento pagamento : i.getPagamenti(null)) {
				riscossioni.add(RiscossioniConverter.toRsModel(pagamento));
			}
			
			rsModel.setRiscossioni(riscossioni);
		}
		
		return rsModel;
	}
}
