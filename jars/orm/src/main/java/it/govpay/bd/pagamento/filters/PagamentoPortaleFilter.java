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
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoPortaleFieldConverter;
import it.govpay.orm.model.VistaPagamentoPortaleModel;

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
	private List<IdUnitaOperativa> idUo;
	private String idDebitore = null;
	private Integer severitaDa;
	private Integer severitaA;
	private String codApplicazione = null;
	private String codDominio = null;
	private String iuv;
	private String codVersamento = null;
	
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
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO, this.versante.toUpperCase());
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
				
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO, this.cfCittadino.toUpperCase());
				addAnd = true;
			}
			
			if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
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
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				List<IExpression> listaUoExpr = new ArrayList<IExpression>();
				for (IdUnitaOperativa uo : this.idUo) {
					IExpression orExpr = this.newExpression();
					if(uo.getIdDominio() != null) {
						CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
						orExpr.equals(cf, uo.getIdDominio());
					}

					if(uo.getIdUnita() != null) {
						CustomField cf = new CustomField("id_uo", Long.class, "id_uo", converter.toTable(it.govpay.orm.VistaPagamentoPortale.model()));
						orExpr.and().equals(cf, uo.getIdUnita());
					}
					
					listaUoExpr.add(orExpr);
				}
				
				newExpression.or(listaUoExpr.toArray(new IExpression[listaUoExpr.size()]));
				addAnd = true;
			}
			
			if(this.idDebitore!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().SRC_DEBITORE_IDENTIFICATIVO, this.idDebitore.toUpperCase());
				addAnd = true;
			}
			
			if(this.severitaDa != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(it.govpay.orm.VistaPagamentoPortale.model().SEVERITA, this.severitaDa);
				addAnd = true;
			}
			if(this.severitaA != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(it.govpay.orm.VistaPagamentoPortale.model().SEVERITA, this.severitaA);
				addAnd = true;
			}
			
			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().COD_VERSAMENTO_ENTE, this.codVersamento);
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().ID_DOMINIO.COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(it.govpay.orm.VistaPagamentoPortale.model().SRC_IUV, this.iuv.toUpperCase());
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

	public List<IdUnitaOperativa> getIdUo() {
		return idUo;
	}

	public void setIdUo(List<IdUnitaOperativa> idUo) {
		this.idUo = idUo;
	}

	public String getIdDebitore() {
		return idDebitore;
	}

	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}
	
	public Integer getSeveritaDa() {
		return severitaDa;
	}
	public void setSeveritaDa(Integer severitaDa) {
		this.severitaDa = severitaDa;
	}
	public Integer getSeveritaA() {
		return severitaA;
	}
	public void setSeveritaA(Integer severitaA) {
		this.severitaA = severitaA;
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

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodVersamento() {
		return codVersamento;
	}

	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}

	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VistaPagamentoPortaleFieldConverter converter = new VistaPagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaPagamentoPortaleModel model = it.govpay.orm.VistaPagamentoPortale.model();
			
			boolean addTabellaDomini = false;
			boolean addTabellaApplicazioni = false;
			
			if(this.dataInizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_RICHIESTA, true) + " >= ? ");
			}
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_RICHIESTA, true) + " <= ? ");
			}
			if(this.stato != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO, true) + " = ? ");
			}
			if(this.versante!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_VERSANTE_IDENTIFICATIVO, true) + " = ? ");
			}
			if(this.idSessionePortale!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_SESSIONE_PORTALE, true) + " = ? ");
			}
			if(this.idSessionePsp!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_SESSIONE_PSP, true) + " = ? ");
			}
			
			if(this.codDominiMultibeneficiario != null && this.codDominiMultibeneficiario.size() > 0) {
				this.codDominiMultibeneficiario.removeAll(Collections.singleton(null));
				
				String [] codiciMultibeneficiario = this.codDominiMultibeneficiario.toArray(new String[this.codDominiMultibeneficiario.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.MULTI_BENEFICIARIO, true), true, codiciMultibeneficiario);
				sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.MULTI_BENEFICIARIO, true));
			}
			
			if(this.ack!=null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ACK, true) + " = ? ");
			}
			
			if(this.cfCittadino!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_VERSANTE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
				String [] idsPagamentiPortale = this.idPagamentiPortale.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idPagamentiPortale.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id", false, idsPagamentiPortale );
			}
			
			if(this.tipoUtenza!=null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_UTENZA, true) + " = ? ");
			}
			
			if(this.idApplicazione != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.ID_SESSIONE, true) + ".id_applicazione = ? ");
			}
			
			if(this.idSessione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_SESSIONE, true) + " = ? ");
			}
			
			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDomini = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_dominio", false, idsDomini );
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] idsTipiVersamento = this.idTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_tipo_versamento", false, idsTipiVersamento );
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				
				List<String> listaUoExpr = new ArrayList<String>();
				for (IdUnitaOperativa uo : this.idUo) {
					String orExpr = "";
					
					if(uo.getIdDominio() != null) {
						orExpr = converter.toTable(model.ID_SESSIONE, true) + ".id_dominio" + " = ? ";
					}

					if(uo.getIdUnita() != null) {
						orExpr += " and " ;
						orExpr += converter.toTable(model.ID_SESSIONE, true) + ".id_uo" + " = ? ";
					}
					
					listaUoExpr.add(orExpr);
				}
				
				sqlQueryObject.addWhereCondition(false, listaUoExpr.toArray(new String[listaUoExpr.size()]));
			}
			
			if(this.idDebitore!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_DEBITORE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.severitaDa != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SEVERITA, true) + " >= ? ");
			}
			if(this.severitaA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SEVERITA, true) + " <= ? ");
			}
			
			if(this.codVersamento != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_VERSAMENTO_ENTE, true) + " = ? ");
			}
			
			if(this.codApplicazione != null){
				if(!addTabellaApplicazioni) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_APPLICAZIONE));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_applicazione="
							+converter.toTable(model.ID_APPLICAZIONE, true)+".id");

					addTabellaApplicazioni = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.codDominio != null){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.iuv != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_IUV, true) + " = ? ");
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
			lst.add(this.versante.toUpperCase());
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
			lst.add(this.cfCittadino.toUpperCase());
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
			for (IdUnitaOperativa uo : this.idUo) {
				if(uo.getIdDominio() != null) {
					lst.add(uo.getIdDominio());
				}

				if(uo.getIdUnita() != null) {
					lst.add(uo.getIdUnita());
				}
			}
		}
		
		if(this.idDebitore!= null) {
			lst.add(this.idDebitore.toUpperCase());
		}
		
		if(this.severitaDa != null) {
			lst.add(this.severitaDa);
		}
		if(this.severitaA != null) {
			lst.add(this.severitaA);
		}
		
		if(this.codVersamento != null){
			lst.add(this.codVersamento);
		}
		
		if(this.codApplicazione != null){
			lst.add(this.codApplicazione);
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}
		
		if(this.iuv != null){
			lst.add(this.iuv);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}
}
