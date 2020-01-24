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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoPortaleFieldConverter;

public class PagamentoPortaleFilter extends AbstractFilter {

	private Date dataInizio;
	private Date dataFine;
	private STATO stato;
	private String versante;
	private String idSessionePortale;
	private String idSessionePsp;
	private String idSessione;
	private List<String> codDominiMultibeneficiario;
	private Boolean ack;
	private String cfCittadino;
	private List<Long> idPagamentiPortale;
	private String tipoUtenza;
	private Long idApplicazione;
	private List<Long> idTipiVersamento = null;
	private List<Long> idDomini;
	private List<Long> idUo;
	
	public enum SortFields {
		DATA
	}

	public PagamentoPortaleFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public PagamentoPortaleFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			VistaPagamentoPortaleFieldConverter converter = new VistaPagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.dataInizio != null) {
				newExpression.greaterEquals(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, this.dataInizio);
				addAnd = true;
			}
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, this.dataFine);
				addAnd = true;
			}
			if(this.stato != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().STATO, this.stato.toString());
				addAnd = true;
			}
			if(this.versante!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO, this.versante);
				addAnd = true;
			}
			if(this.idSessionePortale!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE_PORTALE, this.idSessionePortale);
				addAnd = true;
			}
			if(this.idSessionePsp!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE_PSP, this.idSessionePsp);
				addAnd = true;
			}
			
			if(this.codDominiMultibeneficiario != null && this.codDominiMultibeneficiario.size() > 0) {
				if(addAnd)
					newExpression.and();
				this.codDominiMultibeneficiario.removeAll(Collections.singleton(null));
				
				newExpression.in(it.govpay.orm.VistaPagamentoPortale.model().MULTI_BENEFICIARIO, this.codDominiMultibeneficiario);
				newExpression.isNotNull(it.govpay.orm.VistaPagamentoPortale.model().MULTI_BENEFICIARIO);
			
				addAnd = true;
			}
			
			if(this.ack!=null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ACK, this.ack);
				
				addAnd = true;
			}
			
			if(this.cfCittadino!= null) {
				if(addAnd)
					newExpression.and();
				
				IExpression cfExpr = this.newExpression();
				
				cfExpr.equals(it.govpay.orm.VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO, this.cfCittadino);
				
				newExpression.and(cfExpr);
				
				addAnd = true;
			}
			
			if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
				//PagamentoPortaleFieldConverter converter = new PagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
				newExpression.in(cf, this.idPagamentiPortale);
				addAnd = true;
			}
			
			if(this.tipoUtenza!=null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().TIPO_UTENZA, this.tipoUtenza);
				
				addAnd = true;
			}
			
			if(this.idApplicazione != null){
				if(addAnd)
					newExpression.and();

				CustomField cf = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
				newExpression.equals(cf, this.idApplicazione);
				addAnd = true;
			}
			
			if(this.idSessione!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, this.idSessione);
				addAnd = true;
			}
			
			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
				newExpression.in(cf, this.idTipiVersamento);
				addAnd = true;
			}
			
			if(this.getIdUo() != null && !this.getIdUo().isEmpty()){
				this.getIdUo().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_uo", Long.class, "id_uo", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
				newExpression.in(cf, this.getIdUo());
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
	public IExpression _toSimpleSearchExpression() throws ServiceException {
//		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			return newExpression;
//		} catch (ExpressionNotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		}
	}

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		if(field.equals(SortFields.DATA)) 
			filterSortWrapper.setField(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
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

	public String getVersante() {
		return this.versante;
	}

	public void setVersante(String versante) {
		this.versante = versante;
	}

	public STATO getStato() {
		return this.stato;
	}

	public void setStato(STATO stato) {
		this.stato = stato;
	}

	public List<String> getCodDominiMultibeneficiario() {
		return this.codDominiMultibeneficiario;
	}

	public void setCodDominiMultibeneficiario(List<String> codDomini) {
		this.codDominiMultibeneficiario = codDomini;
	}

	public Boolean getAck() {
		return this.ack;
	}

	public void setAck(Boolean ack) {
		this.ack = ack;
	}

	public String getIdSessionePortale() {
		return this.idSessionePortale;
	}

	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}

	public String getIdSessionePsp() {
		return idSessionePsp;
	}

	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}

	public String getCfCittadino() {
		return cfCittadino;
	}

	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}

	public List<Long> getIdPagamentiPortale() {
		return idPagamentiPortale;
	}

	public void setIdPagamentiPortale(List<Long> idPagamentiPortale) {
		this.idPagamentiPortale = idPagamentiPortale;
	}

	public String getTipoUtenza() {
		return tipoUtenza;
	}

	public void setTipoUtenza(String tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public List<Long> getIdUo() {
		return idUo;
	}

	public void setIdUo(List<Long> idUo) {
		this.idUo = idUo;
	}

	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VistaPagamentoPortaleFieldConverter converter = new VistaPagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.dataInizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, true) + " >= ? ");
			}
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, true) + " <= ? ");
			}
			if(this.stato != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().STATO, true) + " = ? ");
			}
			if(this.versante!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO, true) + " = ? ");
			}
			if(this.idSessionePortale!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE_PORTALE, true) + " = ? ");
			}
			if(this.idSessionePsp!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE_PSP, true) + " = ? ");
			}
			
			if(this.codDominiMultibeneficiario != null && this.codDominiMultibeneficiario.size() > 0) {
				this.codDominiMultibeneficiario.removeAll(Collections.singleton(null));
				
				String [] codiciMultibeneficiario = this.codDominiMultibeneficiario.toArray(new String[this.codDominiMultibeneficiario.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().MULTI_BENEFICIARIO, true), true, codiciMultibeneficiario);
				sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().MULTI_BENEFICIARIO, true));
			}
			
			if(this.ack!=null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().ACK, true) + " = ? ");
			}
			
			if(this.cfCittadino!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
				String [] idsPagamentiPortale = this.idPagamentiPortale.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idPagamentiPortale.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + ".id", false, idsPagamentiPortale );
			}
			
			if(this.tipoUtenza!=null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().TIPO_UTENZA, true) + " = ? ");
			}
			
			if(this.idApplicazione != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + ".id_applicazione = ? ");
			}
			
			if(this.idSessione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + " = ? ");
			}
			
			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDomini = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + ".id_dominio", false, idsDomini );
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] idsTipiVersamento = this.idTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + ".id_tipo_versamento", false, idsTipiVersamento );
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				
				String [] idsUo = this.idUo.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idUo.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE, true) + ".id_uo", false, idsUo );
			}
			
			return sqlQueryObject;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		}
	}
	
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		List<Object> lst = new ArrayList<Object>();
			
			
		if(this.dataInizio != null) {
			lst.add(this.dataInizio);
		}
		if(this.dataFine != null) {
			lst.add(this.dataFine);
		}
		if(this.stato != null) {
			lst.add(this.stato.toString());
		}
		if(this.versante!= null) {
			lst.add(this.versante);
		}
		if(this.idSessionePortale!= null) {
			lst.add(this.idSessionePortale);
		}
		if(this.idSessionePsp!= null) {
			lst.add(this.idSessionePsp);
		}
		
		if(this.codDominiMultibeneficiario != null && this.codDominiMultibeneficiario.size() > 0) {
			// donothing
		}
		
		if(this.ack!=null) {
			lst.add(this.ack);
		}
		
		if(this.cfCittadino!= null) {
			lst.add(this.cfCittadino);
		}
		
		if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
			// donothing
		}
		
		if(this.tipoUtenza!=null) {
			lst.add(this.tipoUtenza);
		}
		
		if(this.idApplicazione != null){
			lst.add(this.idApplicazione);
		}
		
		if(this.idSessione!= null) {
			lst.add(this.idSessione);
		}
		
		if(this.idDomini != null && !this.idDomini.isEmpty()){
			// donothing
		}
		
		if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
			// donothing
		}
		
		if(this.idUo != null && !this.idUo.isEmpty()){
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}
}
