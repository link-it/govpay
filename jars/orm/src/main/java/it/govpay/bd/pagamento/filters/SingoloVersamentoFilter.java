/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento.filters;

import java.util.Collections;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;

public class SingoloVersamentoFilter extends AbstractFilter {

	private String codSingoloVersamentoEnte = null;
	private List<String> codSingoliVersamentiEnte = null;
	private List<Long> idSingoloVersamento= null;
	
	public SingoloVersamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpressionEngine() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.codSingoloVersamentoEnte != null){
				newExpression.ilike(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codSingoliVersamentiEnte != null  && !this.codSingoliVersamentiEnte.isEmpty()){
				this.codSingoliVersamentiEnte.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoliVersamentiEnte);
				addAnd = true;
			}
			
			if(this.idSingoloVersamento != null && !this.idSingoloVersamento.isEmpty()){
				this.idSingoloVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				SingoloVersamentoFieldConverter converter = new SingoloVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model()));
				newExpression.in(cf, this.idSingoloVersamento);
				addAnd = true;
			}
			
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	
	public String getCodSingoloVersamentoEnte() {
		return this.codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public List<String> getCodSingoliVersamentiEnte() {
		return this.codSingoliVersamentiEnte;
	}

	public void setCodSingoliVersamentiEnte(List<String> codSingoliVersamentiEnte) {
		this.codSingoliVersamentiEnte = codSingoliVersamentiEnte;
	}

	public List<Long> getIdSingoloVersamento() {
		return this.idSingoloVersamento;
	}

	public void setIdSingoloVersamento(List<Long> idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}
	
	
}
