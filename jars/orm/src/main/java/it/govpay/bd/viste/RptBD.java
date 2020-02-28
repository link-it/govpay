package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.viste.filters.RptFilter;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.viste.model.converter.RptConverter;

public class RptBD extends BasicBD {

	public RptBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RptFilter newFilter() throws ServiceException {
		return new RptFilter(this.getVistaRptVersamentoServiceSearch());
	}
	
	public RptFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RptFilter(this.getVistaRptVersamentoServiceSearch(),simpleSearch);
	}

	public long count(RptFilter filter) throws ServiceException {
		try {
			return this.getVistaRptVersamentoServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Rpt> findAll(RptFilter filter) throws ServiceException {
		try {
			List<Rpt> rptLst = new ArrayList<>();
			List<it.govpay.orm.VistaRptVersamento> rptVOLst = this.getVistaRptVersamentoServiceSearch().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.VistaRptVersamento rptVO: rptVOLst) {
				rptLst.add(RptConverter.toDTO(rptVO));
			}
			return rptLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
