/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.dao.IDBSingoloVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCSingoloVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class SingoliVersamentiBD extends BasicBD {

	public SingoliVersamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public SingoloVersamento getSingoloVersamento(long idSingoloVersamento) throws ServiceException {
		try {
			it.govpay.orm.SingoloVersamento singoloVersamentoVO = ((IDBSingoloVersamentoServiceSearch)this.getSingoloVersamentoService()).get(idSingoloVersamento);
			return SingoloVersamentoConverter.toDTO(singoloVersamentoVO);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<SingoloVersamento> getSingoliVersamenti(long idVersamento) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getSingoloVersamentoService().newPaginatedExpression();
			SingoloVersamentoFieldConverter fieldConverter = new SingoloVersamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_versamento", Long.class, "id_versamento", fieldConverter.toTable(it.govpay.orm.SingoloVersamento.model())), idVersamento);
			List<it.govpay.orm.SingoloVersamento> singoliVersamenti =  this.getSingoloVersamentoService().findAll(exp);
			return SingoloVersamentoConverter.toDTO(singoliVersamenti);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public it.govpay.model.SingoloVersamento getSingoloVersamento(long id, String codSingoloVersamentoEnte) throws ServiceException, NotFoundException {
		try {
			IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
			idSingoloVersamento.setCodSingoloVersamentoEnte(codSingoloVersamentoEnte);
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(id);
			idSingoloVersamento.setIdVersamento(idVersamento);
			it.govpay.orm.SingoloVersamento singoloVersamentoVO = ((IDBSingoloVersamentoServiceSearch)this.getSingoloVersamentoService()).get(idSingoloVersamento);
			return SingoloVersamentoConverter.toDTO(singoloVersamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public void updateStatoSingoloVersamento(long idVersamento, StatoSingoloVersamento statoSingoloVersamento) throws ServiceException {
		try {
			IdSingoloVersamento idVO = ((JDBCSingoloVersamentoServiceSearch)this.getSingoloVersamentoService()).findId(idVersamento, true);
	
			List<UpdateField> lstUpdateFields = new ArrayList<UpdateField>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO, statoSingoloVersamento.toString()));
	
			this.getSingoloVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
