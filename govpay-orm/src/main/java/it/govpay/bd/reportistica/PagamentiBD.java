package it.govpay.bd.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.reportistica.Pagamento;
import it.govpay.bd.reportistica.filters.PagamentoFilter;
import it.govpay.orm.dao.IPagamentoService;

public class PagamentiBD extends BasicBD{

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService());
	}

	public List<Pagamento> findAll(PagamentoFilter filter) throws ServiceException {
		try {
			
			IPagamentoService pagamentoService = this.getPagamentoService();
			IPaginatedExpression pagExpr = filter.toPaginatedExpression();
			
			FunctionField maxData = new FunctionField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO, Function.MAX, "dataPagamento");
			pagamentoService.groupBy(pagExpr, maxData);
			
			
			
			
			return new ArrayList<Pagamento>();
//			List<it.govpay.orm.Pagamento>pagamentoVOLst = this.getPagamentoService().findAll(filter.toPaginatedExpression()); 
//			return PagamentoConverter.toDTO(pagamentoVOLst);
		} catch (NotFoundException e) {
			return new ArrayList<Pagamento>();
		}catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
			}
	}
	public long count(PagamentoFilter filter) throws ServiceException {
//		try {
			return 0; //return this.getPagamentoService().count(filter.toExpression()).longValue();
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		}
	}
	
	public Pagamento getPagamento(long id) throws ServiceException {
		return null;
	}
	
}
