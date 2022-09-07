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
import it.govpay.orm.Allegato;
import it.govpay.orm.dao.jdbc.converter.AllegatoFieldConverter;
import it.govpay.orm.model.AllegatoModel;

public class AllegatoFilter extends AbstractFilter {

	private static AllegatoModel model = Allegato.model();
	private AllegatoFieldConverter converter = null;
	
	private List<Long> idAllegato = null;
	private Long idVersamento;
	private String nome;
	private String tipo;
	private Date dataInizio;
	private Date dataFine;
	private boolean includiRawContenuto = false;
	

	public AllegatoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public AllegatoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(model.NOME);
		this.converter = new AllegatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(model.DATA_CREAZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			} else {
				if(this.dataInizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(model.DATA_CREAZIONE, this.dataInizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(model.DATA_CREAZIONE, this.dataFine);
					addAnd = true;
				}
			}
			
			if(this.nome != null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(model.NOME, this.nome);
				addAnd = true;
			}
			
			if(this.tipo != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(model.TIPO, this.tipo, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.idVersamento != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField cf = new CustomField("id_versamento", Long.class, "id_versamento", converter.toTable(model.NOME));
				newExpression.equals(cf, this.idVersamento);
				
				addAnd = true;
			}
			
			if(this.idAllegato != null && !this.idAllegato.isEmpty()){
				this.idAllegato.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(model.NOME));
				newExpression.in(cf, this.idAllegato);
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
			if(this.dataInizio != null && this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " <= ? ");
			} else {
				if(this.dataInizio != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " >= ? ");
				} 
				
				if(this.dataFine != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " <= ? ");
				}
			}
			
			if(this.nome != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.NOME, true) + " = ? ");
			}
			
			if(this.tipo != null) {
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TIPO, true), this.tipo, true, true);
			}
			
			if(this.idVersamento != null) {
				sqlQueryObject.addWhereCondition(true, converter.toTable(model.NOME, true) + ".id_versamento" + " = ? ");
			}
			
			if(this.idAllegato != null && !this.idAllegato.isEmpty()){
				this.idAllegato.removeAll(Collections.singleton(null));
				
				String [] idsAllegato = this.idAllegato.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idAllegato.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.NOME, true) + ".id", false, idsAllegato );
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
		
		
		if(this.nome != null) {
			lst.add(this.nome);
		}
		
		if(this.tipo != null) {
			// donothing
		}
		
		if(this.idVersamento != null) {
			lst.add(this.idVersamento);
		}
		
		if(this.idAllegato != null && !this.idAllegato.isEmpty()){
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<Long> getIdAllegato() {
		return idAllegato;
	}

	public void setIdAllegato(List<Long> idAllegato) {
		this.idAllegato = idAllegato;
	}

	public Long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public boolean isIncludiRawContenuto() {
		return includiRawContenuto;
	}

	public void setIncludiRawContenuto(boolean includiRawContenuto) {
		this.includiRawContenuto = includiRawContenuto;
	}
	
	
	
}
