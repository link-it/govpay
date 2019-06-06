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
package it.govpay.bd.pagamento.filters;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.EsitoAvvisatura;
import it.govpay.orm.dao.jdbc.converter.EsitoAvvisaturaFieldConverter;

public class EsitoAvvisaturaFilter extends AbstractFilter {

	private Long idTracciato;

	public EsitoAvvisaturaFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public EsitoAvvisaturaFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
//			boolean addAnd = false;
			EsitoAvvisaturaFieldConverter converter = new EsitoAvvisaturaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			if(this.idTracciato != null){
				CustomField cf = new CustomField("id_tracciato", Long.class, "id_tracciato", converter.toTable(EsitoAvvisatura.model()));
				exp.equals(cf, this.idTracciato); 
//				addAnd = true;
			}

			return exp;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

}
