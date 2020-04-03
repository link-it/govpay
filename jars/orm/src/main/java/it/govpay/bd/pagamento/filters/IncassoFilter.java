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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.Incasso;
import it.govpay.orm.dao.jdbc.converter.IncassoFieldConverter;
import it.govpay.orm.model.IncassoModel;

public class IncassoFilter extends AbstractFilter{

	private List<String> codDomini;
	private Date dataInizio;
	private Date dataFine;
	private String trn;
	private String dispositivo;
	private String causale;
	private String codApplicazione;
	private List<Long> idIncasso= null;
	private String codDominio =null;

	public IncassoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public IncassoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Incasso.model().TRN);
		this.listaFieldSimpleSearch.add(Incasso.model().NOME_DISPOSITIVO);
		this.listaFieldSimpleSearch.add(Incasso.model().CAUSALE);
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.codDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.codDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(Incasso.model().COD_DOMINIO, this.codDomini);
				newExpression.and(newExpressionDomini);
			}

			return newExpression;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.dataInizio != null && this.dataFine != null) {
				newExpression.between(Incasso.model().DATA_ORA_INCASSO, this.dataInizio,this.dataFine);
				addAnd = true;
			}  else {
				if(this.dataInizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Incasso.model().DATA_ORA_INCASSO, this.dataInizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Incasso.model().DATA_ORA_INCASSO, this.dataFine);
					addAnd = true;
				}
			}

			if(this.idIncasso != null && !this.idIncasso.isEmpty()){
				if(addAnd)
					newExpression.and();
				IncassoFieldConverter converter = new IncassoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(Incasso.model()));
				newExpression.in(cf, this.idIncasso);
				addAnd = true;
			}

			if(this.codDomini != null && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(Incasso.model().COD_DOMINIO, this.codDomini);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(Incasso.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.trn != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().TRN, this.trn, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.dispositivo != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().NOME_DISPOSITIVO, this.dispositivo, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.causale != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().CAUSALE, this.causale, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(Incasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
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
		try {
			IncassoFieldConverter converter = new IncassoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			IncassoModel model = it.govpay.orm.Incasso.model();
			
			boolean addTabellaApplicazioni = false;
			
			if(this.dataInizio != null && this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_INCASSO, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_INCASSO, true) + " <= ? ");
			} else {
				if(this.dataInizio != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_INCASSO, true) + " >= ? ");
				} 
				
				if(this.dataFine != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_INCASSO, true) + " <= ? ");
				}
			}

			if(this.idIncasso != null && !this.idIncasso.isEmpty()){
				this.idIncasso.removeAll(Collections.singleton(null));
				
				String [] idsIncasso = this.idIncasso.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idIncasso.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.TRN, true) + ".id", false, idsIncasso );
			}

			if(this.codDomini != null && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.codDomini.toArray(new String[this.codDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_DOMINIO, true) + ".cod_dominio", true, codDomini );
			}
			
			if(this.codDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}

			if(this.trn != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TRN, true), this.trn, true, true);
			}

			if(this.dispositivo != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.NOME_DISPOSITIVO, true), this.dispositivo, true, true);
			}

			if(this.causale != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.CAUSALE, true), this.causale, true, true);
			}
			
			if(this.codApplicazione != null){
				if(!addTabellaApplicazioni) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_APPLICAZIONE));
					sqlQueryObject.addWhereCondition(converter.toTable(model.TRN, true) + ".id_applicazione="
							+converter.toTable(model.ID_APPLICAZIONE, true)+".id");

					addTabellaApplicazioni = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			return sqlQueryObject;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		List<Object> lst = new ArrayList<Object>();
		
		if(this.dataInizio != null && this.dataFine != null) {
			lst.add(this.dataInizio);
			lst.add(this.dataFine);
		} else {
			if(this.dataInizio != null) {
				lst.add(this.dataInizio);
			} 
			
			if(this.dataFine != null) {
				lst.add(this.dataFine);
			}
		}

		if(this.idIncasso != null && !this.idIncasso.isEmpty()){
			// donothing
		}

		if(this.codDomini != null && !this.codDomini.isEmpty()){
			// donothing
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}

		if(this.trn != null){
			// donothing
		}

		if(this.dispositivo != null){
			// donothing
		}

		if(this.causale != null){
			// donothing
		}
		
		if(this.codApplicazione != null){
			lst.add(this.codApplicazione);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<String> getCodDomini() {
		return this.codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getTrn() {
		return this.trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getDispositivo() {
		return this.dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getCausale() {
		return this.causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}
	public List<Long> getIdIncasso() {
		return this.idIncasso;
	}

	public void setIdIncasso(List<Long> idIncasso) {
		this.idIncasso = idIncasso;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	
}
