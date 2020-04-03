package it.govpay.bd.pagamento.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.Evento;
import it.govpay.orm.dao.jdbc.converter.EventoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VistaEventiVersamentoFieldConverter;
import it.govpay.orm.model.EventoModel;

public class EventiFilter extends AbstractFilter{
	
	public enum VISTA { VERSAMENTI, PAGAMENTI, RPT};
	
	private String codDominio= null;
	private List<String> codDomini;
	private String iuv;
	private String ccp;
	private String codApplicazione;
	private String codVersamentoEnte;
	private String idSessione;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idEventi= null;
	private String esito;
	private String componente;
	private String tipoEvento;
	private String sottotipoEvento;
	private String categoria;
	private String ruolo;
	private VISTA vista;
	
	public EventiFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false, null);
	}

	public EventiFilter(IExpressionConstructor expressionConstructor, VISTA vista) {
		this(expressionConstructor,false, vista);
	}
	
	public EventiFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch, VISTA vista) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Evento.model().COMPONENTE);
		this.listaFieldSimpleSearch.add(Evento.model().TIPO_EVENTO);
		this.setVista(vista);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();

			boolean addAnd = false;
			
			if(this.codDominio != null){
				newExpression.equals(Evento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codDomini != null && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				newExpression.in(Evento.model().COD_DOMINIO, this.codDomini);
				addAnd = true;
			}

			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Evento.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ccp != null && StringUtils.isNotEmpty(this.ccp)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Evento.model().CCP, this.ccp, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Evento.model().DATA, this.datainizio,this.dataFine);
				addAnd = true;
			} else {
				if(this.datainizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Evento.model().DATA, this.datainizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Evento.model().DATA, this.dataFine);
					addAnd = true;
				}
			}
			
			if(this.codVersamentoEnte!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COD_VERSAMENTO_ENTE, this.codVersamentoEnte);
				addAnd = true;
			}
			
			if(this.codApplicazione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idSessione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ID_SESSIONE, this.idSessione);
				addAnd = true;
			}
			
			if(this.esito != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ESITO, this.esito);
				addAnd = true;
			}
			
			if(this.idEventi != null && !this.idEventi.isEmpty()){
				CustomField idEventoField = new CustomField("id",  Long.class, "id",  this.getTableName(Evento.model()));
				newExpression.in(idEventoField, this.idEventi);
				addAnd = true;
			}
			
			if(this.componente != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COMPONENTE, this.componente);
				addAnd = true;
			}
			
			if(this.categoria != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().CATEGORIA_EVENTO, this.categoria);
				addAnd = true;
			}
			
			if(this.ruolo != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().RUOLO, this.ruolo);
				addAnd = true;
			}
			
			if(this.tipoEvento != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().TIPO_EVENTO, this.tipoEvento);
				addAnd = true;
			}
			
			if(this.sottotipoEvento != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().SOTTOTIPO_EVENTO, this.sottotipoEvento);
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
	
	private String getTableName(EventoModel model) throws ServiceException, ExpressionException {
		String tableName = null;

		if(this.vista == null) {
			EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			tableName = eventoFieldConverter.toTable(model);
		} else {
			switch (this.vista) {
			case PAGAMENTI:
			case RPT:
				EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				tableName = eventoFieldConverter.toTable(model);
				break;
			case VERSAMENTI:
				VistaEventiVersamentoFieldConverter vistaEventiVersamentoFieldConverter = new VistaEventiVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				tableName = vistaEventiVersamentoFieldConverter.toTable(model);
				break;
			}
		}
		return tableName;
	}
	
	public AbstractSQLFieldConverter getFieldConverter(EventoModel model) throws ServiceException, ExpressionException {
		if(this.vista == null) {
			return new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		} else {
			switch (this.vista) {
			case PAGAMENTI:
			case RPT:
				return new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			case VERSAMENTI:
				return new VistaEventiVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			}
		}
		return null;
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			EventoModel model = it.govpay.orm.Evento.model();
			AbstractSQLFieldConverter converter = this.getFieldConverter(model);
			
			if(this.codDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.codDomini != null && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.codDomini.toArray(new String[this.codDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_DOMINIO, true) + ".cod_dominio", true, codDomini );
			}

			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.IUV, true), this.iuv, true, true);
			}
			
			if(this.ccp != null && StringUtils.isNotEmpty(this.ccp)) {
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.CCP, true), this.ccp, true, true);
			}
			
			
			if(this.datainizio != null && this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA, true) + " <= ? ");
			} else {
				if(this.datainizio != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA, true) + " >= ? ");
				} 
				
				if(this.dataFine != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA, true) + " <= ? ");
				}
			}
			
			if(this.codVersamentoEnte!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_VERSAMENTO_ENTE, true) + " = ? ");
			}
			
			if(this.codApplicazione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.idSessione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_SESSIONE, true) + " = ? ");
			}
			
			if(this.esito != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ESITO, true) + " = ? ");
			}
			
			if(this.idEventi != null && !this.idEventi.isEmpty()){
				this.idEventi.removeAll(Collections.singleton(null));
				
				String [] idsEvento = this.idEventi.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idEventi.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id", false, idsEvento );
			}
			
			if(this.componente != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COMPONENTE, true) + " = ? ");
			}
			
			if(this.categoria != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.CATEGORIA_EVENTO, true) + " = ? ");
			}
			
			if(this.ruolo != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RUOLO, true) + " = ? ");
			}
			
			if(this.tipoEvento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_EVENTO, true) + " = ? ");
			}
			
			if(this.sottotipoEvento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SOTTOTIPO_EVENTO, true) + " = ? ");
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
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}
		
		if(this.codDomini != null && !this.codDomini.isEmpty()){
			// donothing
		}

		
		if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
			// donothing
		}
		
		if(this.ccp != null && StringUtils.isNotEmpty(this.ccp)) {
			// donothing
		}
		
		
		if(this.datainizio != null && this.dataFine != null) {
			lst.add(this.datainizio);
			lst.add(this.dataFine);
		} else {
			if(this.datainizio != null) {
				lst.add(this.datainizio);
			} 
			
			if(this.dataFine != null) {
				lst.add(this.dataFine);
			}
		}
		
		if(this.codVersamentoEnte!= null) {
			lst.add(this.codVersamentoEnte);
		}
		
		if(this.codApplicazione!= null) {
			lst.add(this.codApplicazione);
		}
		
		if(this.idSessione!= null) {
			lst.add(this.idSessione);
		}
		
		if(this.esito != null) {
			lst.add(this.esito);
		}
		
		if(this.idEventi != null && !this.idEventi.isEmpty()){
			// donothing
		}
		
		if(this.componente != null) {
			lst.add(this.componente);
		}
		
		if(this.categoria != null) {
			lst.add(this.categoria);
		}
		
		if(this.ruolo != null) {
			lst.add(this.ruolo);
		}
		
		if(this.tipoEvento != null) {
			lst.add(this.tipoEvento);
		}
		
		if(this.sottotipoEvento != null) {
			lst.add(this.sottotipoEvento);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}
	
	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Date getDatainizio() {
		return this.datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public List<Long> getIdEventi() {
		return this.idEventi;
	}

	public void setIdEventi(List<Long> idEventi) {
		this.idEventi = idEventi;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getSottotipoEvento() {
		return sottotipoEvento;
	}

	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}
	
	public VISTA getVista() {
		return vista;
	}

	public void setVista(VISTA vista) {
		this.vista = vista;
	}

}
