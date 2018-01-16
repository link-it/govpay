/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.pagamento;

import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.converter.PagamentoConverter;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentiBD extends BasicBD {
	
	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService());
	}
	
	public PagamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService(),simpleSearch);
	}

	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public Pagamento getPagamento(long id) throws ServiceException {
		try {
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(id);
			it.govpay.orm.Pagamento pagamento = this.getPagamentoService().get(
					idPagamento);
			return PagamentoConverter.toDTO(pagamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il pagamento identificato dalla chiave logica
	 */
	public Pagamento getPagamento(String codDominio, String iuv, String iur, Integer indiceDati)
			throws ServiceException, NotFoundException, MultipleResultException {
		try {
			IPaginatedExpression exp = this.getPagamentoService().newPaginatedExpression();
			
			exp.equals(it.govpay.orm.Pagamento.model().COD_DOMINIO, codDominio);
			exp.equals(it.govpay.orm.Pagamento.model().IUV, iuv);
			if(iur != null) 
				exp.equals(it.govpay.orm.Pagamento.model().IUR, iur);
			if(indiceDati != null) 
				exp.equals(it.govpay.orm.Pagamento.model().INDICE_DATI, indiceDati);
			
			List<it.govpay.orm.Pagamento> pagamentoVO = this.getPagamentoService().findAll(exp);

			if(pagamentoVO.size() == 0) throw new NotFoundException();
			if(pagamentoVO.size() == 1) return PagamentoConverter.toDTO(pagamentoVO.get(0));
			throw new MultipleResultException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}
	
	public Pagamento getPagamento(String codDominio, String iuv)
			throws ServiceException, NotFoundException, MultipleResultException {
		return getPagamento(codDominio, iuv, null, null);
	}

	/**
	 * Crea un nuovo pagamento.
	 */
	public void insertPagamento(Pagamento pagamento) throws ServiceException {
		try {
			it.govpay.orm.Pagamento vo = PagamentoConverter.toVO(pagamento);
			this.getPagamentoService().create(vo);
			pagamento.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updatePagamento(Pagamento pagamento) throws ServiceException {
		try {
			it.govpay.orm.Pagamento vo = PagamentoConverter.toVO(pagamento);
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(pagamento.getId());
			this.getPagamentoService().update(idPagamento, vo);
			pagamento.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public List<Pagamento> getPagamenti(long idRpt) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getPagamentoService()
					.newPaginatedExpression();
			PagamentoFieldConverter fieldConverter = new PagamentoFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_rpt", Long.class, "id_rpt",
					fieldConverter.toTable(it.govpay.orm.Pagamento.model())),
					idRpt);
			List<it.govpay.orm.Pagamento> singoliPagamenti = this
					.getPagamentoService().findAll(exp);
			return PagamentoConverter.toDTO(singoliPagamenti);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}
	
	public List<Pagamento> getPagamentiBySingoloVersamento(long idSingoloVersamento) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getPagamentoService()
					.newPaginatedExpression();
			PagamentoFieldConverter fieldConverter = new PagamentoFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_singolo_versamento", Long.class, "id_singolo_versamento",
					fieldConverter.toTable(it.govpay.orm.Pagamento.model())),
					idSingoloVersamento);
			List<it.govpay.orm.Pagamento> singoliPagamenti = this
					.getPagamentoService().findAll(exp);
			return PagamentoConverter.toDTO(singoliPagamenti);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public List<Pagamento> getPagamentiByRr(Long idRr) throws ServiceException {
		PagamentoFilter filter = newFilter();
		filter.setIdRr(idRr);
		return findAll(filter);
	}

	public List<Pagamento> findAll(PagamentoFilter filter)
			throws ServiceException {
		try {
			List<it.govpay.orm.Pagamento> pagamentoVOLst = this
					.getPagamentoService().findAll(
							filter.toPaginatedExpression());
			return PagamentoConverter.toDTO(pagamentoVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(PagamentoFilter filter) throws ServiceException {
		try {
			return this.getPagamentoService().count(filter.toExpression())
					.longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
