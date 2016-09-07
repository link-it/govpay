package it.govpay.bd.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.reportistica.Pagamento;
import it.govpay.bd.reportistica.filters.PagamentoFilter;

public class PagamentiBD extends BasicBD{

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService());
	}

	public List<Pagamento> findAll(PagamentoFilter filter) throws ServiceException {
//		try {
			return new ArrayList<Pagamento>();
//			List<it.govpay.orm.Pagamento>pagamentoVOLst = this.getPagamentoService().findAll(filter.toPaginatedExpression()); 
//			return PagamentoConverter.toDTO(pagamentoVOLst);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		}
	}
	public long count(PagamentoFilter filter) throws ServiceException {
//		try {
			return 0; //return this.getPagamentoService().count(filter.toExpression()).longValue();
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		}
	}
	
	
}
