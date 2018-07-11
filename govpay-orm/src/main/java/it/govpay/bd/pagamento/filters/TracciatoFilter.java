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

import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.orm.Tracciato;

public class TracciatoFilter extends AbstractFilter {

	private String filenameRichiestaLike;
	private List<it.govpay.model.Tracciato.TIPO_TRACCIATO> tipo;
	private it.govpay.model.Tracciato.STATO_ELABORAZIONE stato;
	private List<String> domini;

	public String getFilenameRichiestaLike() {
		return filenameRichiestaLike;
	}

	public void setFilenameRichiestaLike(String filenameRichiestaLike) {
		this.filenameRichiestaLike = filenameRichiestaLike;
	}

	public TracciatoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public TracciatoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			boolean addAnd = false;
			
			if(this.filenameRichiestaLike != null){
				exp.like(Tracciato.model().FILE_NAME_RICHIESTA, this.filenameRichiestaLike,LikeMode.END); 
				addAnd = true;
			}
			
			if(this.tipo != null && !this.tipo.isEmpty()){
				if(addAnd)
					exp.and();
				exp.in(Tracciato.model().TIPO, this.tipo.stream().map(t -> t.toString()).collect(Collectors.toList()));
				addAnd = true;
			}
			
			if(this.stato != null){
				if(addAnd)
					exp.and();
				exp.equals(Tracciato.model().STATO, this.stato.toString());
				
				addAnd = true;
			}

			if(this.domini != null && !this.domini.isEmpty()){
				if(addAnd)
					exp.and();
				
				exp.in(Tracciato.model().COD_DOMINIO, this.domini); 
				addAnd = true;
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

	public it.govpay.model.Tracciato.STATO_ELABORAZIONE getStato() {
		return stato;
	}

	public void setStato(it.govpay.model.Tracciato.STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}

	public List<String> getDomini() {
		return domini;
	}

	public void setDomini(List<String> domini) {
		this.domini = domini;
	}

	public List<it.govpay.model.Tracciato.TIPO_TRACCIATO> getTipo() {
		return tipo;
	}

	public void setTipo(List<it.govpay.model.Tracciato.TIPO_TRACCIATO> tipo) {
		this.tipo = tipo;
	}


}
