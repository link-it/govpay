package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.viste.filters.EntrataPrevistaFilter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.bd.viste.model.converter.EntrataPrevistaConverter;

public class EntratePrevisteBD extends BasicBD {

	public EntratePrevisteBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public EntrataPrevistaFilter newFilter() throws ServiceException {
		return new EntrataPrevistaFilter(this.getVistaRiscossioniServiceSearch());
	}

	public EntrataPrevistaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new EntrataPrevistaFilter(this.getVistaRiscossioniServiceSearch(),simpleSearch);
	}

	public long count(EntrataPrevistaFilter filter) throws ServiceException {
		try {
			return this.getVistaRiscossioniServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<EntrataPrevista> findAll(EntrataPrevistaFilter filter) throws ServiceException {
		try {
			List<EntrataPrevista> entratePrevisteLst = new ArrayList<>();

			IPaginatedExpression paginatedExpression = filter.toPaginatedExpression();
			List<it.govpay.orm.VistaRiscossioni> riscossioniVOLst = this.getVistaRiscossioniServiceSearch().findAll(paginatedExpression); 
			for(it.govpay.orm.VistaRiscossioni riscossioneVO: riscossioniVOLst) {
				entratePrevisteLst.add(EntrataPrevistaConverter.toDTO(riscossioneVO));
			}
			return entratePrevisteLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
