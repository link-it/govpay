package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.converter.AvvisoPagamentoConverter;
import it.govpay.bd.pagamento.filters.AvvisoPagamentoFilter;
import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.orm.dao.jdbc.JDBCAvvisoServiceSearch;

public class AvvisiPagamentoBD extends BasicBD{
	
	public AvvisiPagamentoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public AvvisoPagamentoFilter newFilter() throws ServiceException {
		return new AvvisoPagamentoFilter(this.getAvvisoService());
	}

	public AvvisoPagamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new AvvisoPagamentoFilter(this.getAvvisoService(),simpleSearch);
	}

	
	public long count(AvvisoPagamentoFilter filter) throws ServiceException {
		try {
			return this.getAvvisoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<AvvisoPagamento> findAll(AvvisoPagamentoFilter filter) throws ServiceException {
		try {
			List<AvvisoPagamento> avvisoLst = new ArrayList<AvvisoPagamento>();

			List<it.govpay.orm.Avviso> avvisoVOLst = this.getAvvisoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Avviso incassoVO: avvisoVOLst) {
				avvisoLst.add(AvvisoPagamentoConverter.toDTO(incassoVO));
			}
			return avvisoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public AvvisoPagamento getAvviso(long id) throws ServiceException {
		try {
			it.govpay.orm.Avviso avvisoVO = ((JDBCAvvisoServiceSearch)this.getAvvisoService()).get(id);
			return AvvisoPagamentoConverter.toDTO(avvisoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public AvvisoPagamento getAvviso(String codDominio, String iuv) throws ServiceException, NotFoundException {
		try {
			IExpression exp = this.getAvvisoService().newExpression();
			
			exp.equals(it.govpay.orm.Avviso.model().COD_DOMINIO, codDominio);
			exp.equals(it.govpay.orm.Avviso.model().IUV, iuv);
			
			it.govpay.orm.Avviso avvisoVO = this.getAvvisoService().find(exp);
			return AvvisoPagamentoConverter.toDTO(avvisoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertAvviso(AvvisoPagamento avviso) throws ServiceException {
		try {
			it.govpay.orm.Avviso vo = AvvisoPagamentoConverter.toVO(avviso);
			this.getAvvisoService().create(vo);
			avviso.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateAvviso(AvvisoPagamento avviso) throws NotFoundException,ServiceException {
		try {
			it.govpay.orm.Avviso vo = AvvisoPagamentoConverter.toVO(avviso);
			this.getAvvisoService().update(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}  
	}
}
