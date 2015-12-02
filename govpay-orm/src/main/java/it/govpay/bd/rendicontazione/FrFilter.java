/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.rendicontazione;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.orm.Evento;
import it.govpay.orm.FR;

public class FrFilter extends AbstractFilter {
	
	private String codFlusso;
	private String codPsp;
	private String stato;
	private int annoRiferimento;
	private Date datainizio;
	private Date dataFine;

	public FrFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = newExpression();
			
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.stato != null && StringUtils.isNotEmpty(this.stato)){
				newExpression.equals(FR.model().STATO, this.stato);
				addAnd = true;
			}
			
			if(this.annoRiferimento > 0){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().ANNO_RIFERIMENTO, this.annoRiferimento);
				addAnd = true;
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(FR.model().COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
			}
			
			if(this.codPsp != null && StringUtils.isNotEmpty(this.codPsp)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().ID_PSP.COD_PSP, this.codPsp);
			}
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.between(Evento.model().DATA_ORA_EVENTO, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			return newExpression;
		}  catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
 
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

	public Date getDatainizio() {
		return datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

}
