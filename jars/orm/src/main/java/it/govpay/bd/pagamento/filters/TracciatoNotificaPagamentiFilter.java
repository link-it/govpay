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
import it.govpay.orm.dao.jdbc.converter.TracciatoNotificaPagamentiFieldConverter;
import it.govpay.orm.model.TracciatoNotificaPagamentiModel;

public class TracciatoNotificaPagamentiFilter extends AbstractFilter {

	private String nomeFileLike;
	private String nomeFile;
	private it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE stato;
	private String dettaglioStato;
	private List<String> domini;
	private String codDominio;
	boolean includiRawContenuto = false; 
	private List<it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE> stati;
	private Date dataCompletamentoDa;
	private Date dataCompletamentoA;
	private String tipo;
	private String versione;
	
	private TracciatoNotificaPagamentiFieldConverter converter = null;
	private	TracciatoNotificaPagamentiModel model = null;

	public TracciatoNotificaPagamentiFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
		
		try {
			this.model = it.govpay.orm.TracciatoNotificaPagamenti.model();
			this.converter = new TracciatoNotificaPagamentiFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		} catch (ServiceException e) { 	} 
	}
	
	public TracciatoNotificaPagamentiFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
		try {
			this.model = it.govpay.orm.TracciatoNotificaPagamenti.model();
			this.converter = new TracciatoNotificaPagamentiFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		} catch (ServiceException e) { 	} 
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			boolean addAnd = false;
			
			if(this.nomeFileLike != null){
				exp.like(model.NOME_FILE, this.nomeFileLike,LikeMode.END); 
				addAnd = true;
			}
			
			if(this.nomeFile != null){
				exp.equals(model.NOME_FILE, this.nomeFile); 
				addAnd = true;
			}
			
			if(this.stato != null){
				if(addAnd)
					exp.and();
				
				exp.equals(model.STATO, this.stato.toString());
				
				if(this.getDettaglioStato() != null) {
					exp.like(model.BEAN_DATI, this.dettaglioStato,LikeMode.ANYWHERE);
				}
				
				addAnd = true;
			}
			
			if(this.domini != null && !this.domini.isEmpty()){
				if(addAnd)
					exp.and();
				
				exp.in(model.ID_DOMINIO.COD_DOMINIO, this.domini); 
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					exp.and();
				
				exp.equals(model.ID_DOMINIO.COD_DOMINIO, this.codDominio); 
				addAnd = true;
			}
			
			if(this.stati != null && !this.stati.isEmpty()){
				if(addAnd)
					exp.and();
				
				this.stati.removeAll(Collections.singleton(null));
				List<String> statiS = this.stati.stream().map(e -> e.toString()).collect(Collectors.toList());
				
				exp.in(model.STATO, statiS); 
				addAnd = true;
			}
			
			if(this.dataCompletamentoDa != null && this.dataCompletamentoA != null) {
				if(addAnd)
					exp.and();

				exp.between(model.DATA_COMPLETAMENTO, this.dataCompletamentoDa,this.dataCompletamentoA);
				addAnd = true;
			} else {
				if(this.dataCompletamentoDa != null) {
					if(addAnd)
						exp.and();
	
					exp.greaterEquals(model.DATA_COMPLETAMENTO, this.dataCompletamentoDa);
					addAnd = true;
				} 
				
				if(this.dataCompletamentoA != null) {
					if(addAnd)
						exp.and();
	
					exp.lessEquals(model.DATA_COMPLETAMENTO, this.dataCompletamentoA);
					addAnd = true;
				}
			}
			
			if(this.tipo != null){
				if(addAnd)
					exp.and();
				
				exp.equals(model.TIPO, this.tipo);
				addAnd = true;
			}
			
			if(this.versione != null){
				if(addAnd)
					exp.and();
				
				exp.equals(model.VERSIONE, this.versione);
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
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			boolean addTabellaDomini = false;
			
			if(this.nomeFileLike != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.NOME_FILE, true), this.nomeFileLike, true, true);
			}
			
			if(this.nomeFile != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.NOME_FILE, true) + " = ? ");
			}
			
			if(this.stato != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO, true) + " = ? ");
				
				if(this.getDettaglioStato() != null) {
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.BEAN_DATI, true), this.dettaglioStato, true, true);
				}
			}
			
			if(this.domini != null && !this.domini.isEmpty()){
				this.domini.removeAll(Collections.singleton(null));
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.NOME_FILE, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}
				
				String [] codDomini = this.domini.toArray(new String[this.domini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true), true, codDomini );
			}
			
			if(this.codDominio != null){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.NOME_FILE, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.stati != null && !this.stati.isEmpty()){
				this.stati.removeAll(Collections.singleton(null));
				String [] statiS = this.stati.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.stati.size()]);
				
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.STATO, true), true, statiS );
			}
			
			if(this.dataCompletamentoDa != null && this.dataCompletamentoA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_COMPLETAMENTO, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_COMPLETAMENTO, true) + " <= ? ");
			} else {
				if(this.dataCompletamentoDa != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_COMPLETAMENTO, true) + " >= ? ");
				} 
				
				if(this.dataCompletamentoA != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_COMPLETAMENTO, true) + " <= ? ");
				}
			}
			
			if(this.tipo != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO, true) + " = ? ");
			}
			
			if(this.versione != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.VERSIONE, true) + " = ? ");
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
		
		if(this.nomeFileLike != null){
			// donothing
		}
		
		if(this.nomeFile != null){
			lst.add(this.nomeFile);
		}
		
		if(this.stato != null){
			lst.add(this.stato.toString());
		}
		
		if(this.domini != null && !this.domini.isEmpty()){
			// donothing
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}
		
		if(this.stati != null && !this.stati.isEmpty()){
			// donothing
		}
		
		if(this.dataCompletamentoDa != null && this.dataCompletamentoA != null) {
			lst.add(this.dataCompletamentoDa);
			lst.add(this.dataCompletamentoA);
		} else {
			if(this.dataCompletamentoDa != null) {
				lst.add(this.dataCompletamentoDa);
			} 
			
			if(this.dataCompletamentoA != null) {
				lst.add(this.dataCompletamentoA);
			}
		}
		
		if(this.tipo != null){
			lst.add(this.tipo);
		}
		
		if(this.versione != null){
			lst.add(this.versione);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE getStato() {
		return this.stato;
	}

	public void setStato(it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}

	public List<String> getDomini() {
		return this.domini;
	}

	public void setDomini(List<String> domini) {
		this.domini = domini;
	}

	public String getDettaglioStato() {
		return dettaglioStato;
	}

	public void setDettaglioStato(String dettaglioStato) {
		this.dettaglioStato = dettaglioStato;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getNomeFileLike() {
		return nomeFileLike;
	}

	public void setNomeFileLike(String nomeFileLike) {
		this.nomeFileLike = nomeFileLike;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public boolean isIncludiRawContenuto() {
		return includiRawContenuto;
	}

	public void setIncludiRawContenuto(boolean includiRawContenuto) {
		this.includiRawContenuto = includiRawContenuto;
	}
	
	public List<it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE> getStati() {
		return stati;
	}
	
	public void setStati(List<it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE> stati) {
		this.stati = stati;
	}

	public Date getDataCompletamentoDa() {
		return dataCompletamentoDa;
	}

	public void setDataCompletamentoDa(Date dataCompletamentoDa) {
		this.dataCompletamentoDa = dataCompletamentoDa;
	}

	public Date getDataCompletamentoA() {
		return dataCompletamentoA;
	}

	public void setDataCompletamentoA(Date dataCompletamentoA) {
		this.dataCompletamentoA = dataCompletamentoA;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVersione() {
		return versione;
	}

	public void setVersione(String versione) {
		this.versione = versione;
	}
	
}
