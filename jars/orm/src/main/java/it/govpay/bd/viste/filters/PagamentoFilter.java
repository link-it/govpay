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
package it.govpay.bd.viste.filters;

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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.orm.VistaPagamento;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoFieldConverter;
import it.govpay.orm.model.VistaPagamentoModel;

public class PagamentoFilter extends AbstractFilter {

	private Long idIncasso;
	private Long idRr;
	private Long idRpt;
	private List<String> idDomini;
	private String codDominio;
	private Date dataInizio;
	private Date dataFine;
	private Date dataPagamentoRitardoIncasso;
	private List<Long> idVersamenti;
	private List<Long> idPagamenti;
	private List<Stato> stati;
	private Integer sogliaRitardo = null;
	private String codSingoloVersamentoEnte = null;
	private String codVersamentoEnte = null;
	private String iur;
	private String iuv;
	private String idA2A;
	private List<TipoPagamento> tipo;
	private String idUnita;
	private String idTipoPendenza;
	private List<String> direzione;
	private List<String> divisione;
	private List<String> tassonomia;

	public enum SortFields {
		DATA
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VistaPagamento.model().SNG_COD_SING_VERS_ENTE);
		this.listaFieldSimpleSearch.add(VistaPagamento.model().IUR);
		this.listaFieldSimpleSearch.add(VistaPagamento.model().IUV);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			VistaPagamentoFieldConverter pagamentoFieldConverter = new VistaPagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			if(this.getIdRr() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(new CustomField("id_rr", Long.class, "id_rr", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRr());
				addAnd = true;
			}

			if(this.getIdRpt() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(new CustomField("id_rpt", Long.class, "id_rpt", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRpt());
				addAnd = true;
			}

			if(this.idA2A != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(VistaPagamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE, this.idA2A);
				addAnd = true;
			}

			if(this.tipo != null  && !this.tipo.isEmpty()) {
				this.tipo.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				List<String> tipiS = this.tipo.stream().map(e -> e.toString()).collect(Collectors.toList());
				newExpression.in(VistaPagamento.model().TIPO, tipiS);
				addAnd = true;
			}

			if(this.getIdIncasso() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(new CustomField("id_incasso", Long.class, "id_incasso", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdIncasso());
				addAnd = true;
			}

			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(VistaPagamento.model().DATA_ACQUISIZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(VistaPagamento.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(VistaPagamento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.dataPagamentoRitardoIncasso != null) {
				if(addAnd)
					newExpression.and();

				newExpression.lessThan(VistaPagamento.model().DATA_PAGAMENTO, this.dataPagamentoRitardoIncasso);
				newExpression.notEquals(VistaPagamento.model().STATO,Stato.INCASSATO.name());
				addAnd = true;
			}

			if(this.stati != null && !this.stati.isEmpty()){
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaPagamento.model().STATO, this.stati.stream().map(e -> e.toString()).collect(Collectors.toList()));

				addAnd = true;
			}

			if(this.idVersamenti != null && this.idVersamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField idVersamentoField = new CustomField("vrs_id",  Long.class, "vrs_id",  pagamentoFieldConverter.toTable(VistaPagamento.model()));
				newExpression.in(idVersamentoField, this.idVersamenti);
				addAnd = true;
			}

			if(this.idPagamenti != null && this.idPagamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, pagamentoFieldConverter.toTable(VistaPagamento.model()));
				newExpression.in(cf, this.idPagamenti);
				addAnd = true;
			}

			if(this.codSingoloVersamentoEnte != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaPagamento.model().SNG_COD_SING_VERS_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codVersamentoEnte != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE, this.codVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iur != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaPagamento.model().IUR, this.iur, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaPagamento.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.idUnita != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(VistaPagamento.model().VRS_ID_UO.COD_UO, this.idUnita);
				addAnd = true;
			}
			
			if(this.idTipoPendenza != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.idTipoPendenza);
				addAnd = true;
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaPagamento.model().VRS_DIREZIONE, this.direzione);
				addAnd = true;
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaPagamento.model().VRS_DIVISIONE, this.divisione);
				addAnd = true;
			}
			
			if(this.tassonomia != null && this.tassonomia.size() > 0){
				this.tassonomia.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(VistaPagamento.model().VRS_TASSONOMIA, this.tassonomia);
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
		try {
			VistaPagamentoFieldConverter pagamentoFieldConverter = new VistaPagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.getIdRr() != null) {
				IExpression newExpressionRR = this.newExpression();
				newExpressionRR.equals(new CustomField("id_rr", Long.class, "id_rr", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRr());
				newExpression.and(newExpressionRR);
			}

			if(this.getIdRpt() != null) {
				IExpression newExpressionRpt = this.newExpression();
				newExpressionRpt.equals(new CustomField("id_rpt", Long.class, "id_rpt", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRpt());
				newExpression.and(newExpressionRpt);
			}

			if(this.getIdIncasso() != null) {
				IExpression newExpressionIncasso = this.newExpression();
				newExpressionIncasso.equals(new CustomField("id_incasso", Long.class, "id_incasso", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdIncasso());
				newExpression.and(newExpressionIncasso);
			}

			if(this.idVersamenti != null && this.idVersamenti.size() >0){
				IExpression newExpressionVersamenti = this.newExpression();
				CustomField idVersamentoField = new CustomField("vrs_id",  Long.class, "vrs_id",  pagamentoFieldConverter.toTable(VistaPagamento.model()));
				newExpressionVersamenti.in(idVersamentoField, this.idVersamenti);
				newExpression.and(newExpressionVersamenti);
			}

			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(VistaPagamento.model().COD_DOMINIO, this.idDomini);
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

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		if(field.equals(SortFields.DATA)) 
			filterSortWrapper.setField(VistaPagamento.model().DATA_ACQUISIZIONE); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VistaPagamentoFieldConverter converter = new VistaPagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaPagamentoModel model = it.govpay.orm.VistaPagamento.model();
			
			boolean addTabellaTipiVersamento = false;
			boolean addTabellaApplicazioni = false;
			boolean addTabellaUO = false;
			
			if(this.getIdRr() != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.IUV, true) + ".id_rr" + " = ? ");
			}

			if(this.getIdRpt() != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.IUV, true) + ".id_rpt" + " = ? ");
			}

			if(this.idA2A != null) {
				String tableNameApplicazioni = converter.toAliasTable(model.VRS_ID_APPLICAZIONE);
				if(!addTabellaApplicazioni) {
					// V -> A
					sqlQueryObject.addFromTable(tableNameApplicazioni);
					sqlQueryObject.addWhereCondition(converter.toTable(model.IUV, true)+".vrs_id_applicazione="+tableNameApplicazioni+".id");
					addTabellaApplicazioni = true;
				}
				
				sqlQueryObject.addWhereCondition(true,tableNameApplicazioni + ".cod_applicazione" + " = ? ");
			}

			if(this.tipo != null  && !this.tipo.isEmpty()) {
				this.tipo.removeAll(Collections.singleton(null));
				
				String [] tipiS = this.tipo.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.tipo.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.TIPO, true), true, tipiS );
			}

			if(this.getIdIncasso() != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.IUV, true) + ".id_incasso" + " = ? ");
			}

			if(this.dataInizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ACQUISIZIONE, true) + " >= ? ");
			}
			
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ACQUISIZIONE, true) + " <= ? ");
			}
			
			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.idDomini.toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.COD_DOMINIO, true), true, codDomini );
			}
			
			if(this.codDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}

			if(this.dataPagamentoRitardoIncasso != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_PAGAMENTO, true) + " <= ? ");
				sqlQueryObject.addWhereCondition(true, true, converter.toColumn(model.STATO, true) + " = ? ");
				
//				newExpression.lessThan(Pagamento.model().DATA_PAGAMENTO, this.dataPagamentoRitardoIncasso);
//				newExpression.notEquals(Pagamento.model().STATO,Stato.INCASSATO.name());
			}

			if(this.stati != null && !this.stati.isEmpty()){
				this.stati.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.stati.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.stati.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.STATO, true), true, codDomini );
			}

			if(this.idVersamenti != null && this.idVersamenti.size() >0){ 
				this.idVersamenti.removeAll(Collections.singleton(null));
				
				String [] idsVersamento = this.idVersamenti.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idVersamenti.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_ID, true), false, idsVersamento );	
			}

			if(this.idPagamenti != null && this.idPagamenti.size() >0){ 
				this.idPagamenti.removeAll(Collections.singleton(null));
				
				String [] idsPagamento = this.idPagamenti.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idPagamenti.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.IUV, true) + ".id", false, idsPagamento );	
			}

			if(this.codSingoloVersamentoEnte != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.SNG_COD_SING_VERS_ENTE, true), this.codSingoloVersamentoEnte, true, true);
			}
			
			if(this.codVersamentoEnte != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.VRS_COD_VERSAMENTO_ENTE, true), this.codVersamentoEnte, true, true);
			}

			if(this.iur != null && StringUtils.isNotEmpty(this.iur)){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.IUR, true), this.iur, true, true);
			}

			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.IUV, true), this.iuv, true, true);
			}
			
			if(this.idUnita != null){
				String tableNameUO = converter.toAliasTable(model.VRS_ID_UO);
				if(!addTabellaUO) {
					// V -> UO
					sqlQueryObject.addFromTable(tableNameUO);
					sqlQueryObject.addWhereCondition(converter.toTable(model.IUV, true)+".vrs_id_uo="+tableNameUO+".id");
					addTabellaUO = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.VRS_ID_UO.COD_UO, true) + " = ? ");
			}
			
			if(this.idTipoPendenza != null){
				String tableNameTipoVersamento = converter.toAliasTable(model.VRS_ID_TIPO_VERSAMENTO);
				if(!addTabellaTipiVersamento) {
					// V -> TV
					sqlQueryObject.addFromTable(tableNameTipoVersamento);
					sqlQueryObject.addWhereCondition(converter.toTable(model.IUV, true)+".vrs_id_tipo_versamento="+tableNameTipoVersamento+".id");
					addTabellaTipiVersamento = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true) + " = ? ");
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				String [] direzioniS = this.direzione.toArray(new String[this.direzione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_DIREZIONE, true), true, direzioniS);
			}
			
			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				String [] divisioniS = this.divisione.toArray(new String[this.divisione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_DIVISIONE, true), true, divisioniS);
			}
			
			if(this.tassonomia != null && this.tassonomia.size() > 0){
				this.tassonomia.removeAll(Collections.singleton(null));
				String [] tassonomiaS = this.tassonomia.toArray(new String[this.tassonomia.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_TASSONOMIA, true), true, tassonomiaS);
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
		
		if(this.getIdRr() != null) {
			lst.add(this.getIdRr());
		}

		if(this.getIdRpt() != null) {
			lst.add(this.getIdRpt());
		}

		if(this.idA2A != null) {
			lst.add(this.idA2A);
		}

		if(this.tipo != null) {
			// donothing
		}

		if(this.getIdIncasso() != null) {
			lst.add(this.getIdIncasso());
		}

		if(this.dataInizio != null) {
			lst.add(this.dataInizio);
		}
		
		if(this.dataFine != null) {
			lst.add(this.dataFine);
		}
		
		if(this.idDomini != null  && !this.idDomini.isEmpty()){
			// donothing
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}

		if(this.dataPagamentoRitardoIncasso != null) {
			lst.add(this.dataPagamentoRitardoIncasso);
			lst.add(Stato.INCASSATO.name());
		}

		if(this.stati != null && !this.stati.isEmpty()){
			// donothing
		}

		if(this.idVersamenti != null && this.idVersamenti.size() >0){ 
			// donothing
		}

		if(this.idPagamenti != null && this.idPagamenti.size() >0){
			// donothing
		}

		if(this.codSingoloVersamentoEnte != null){
			// donothing
		}
		
		if(this.codVersamentoEnte != null){
			// donothing
		}

		if(this.iur != null){
			// donothing
		}

		if(this.iuv != null){
			// donothing
		}
		
		if(this.idUnita != null){
			lst.add(this.idUnita);
		}
		
		if(this.idTipoPendenza != null){
			lst.add(this.idTipoPendenza);
		}
		
		if(this.direzione != null && this.direzione.size() >0){
			// donothing
		}
		
		if(this.divisione != null && this.divisione.size() >0){
			// donothing
		}
		
		if(this.tassonomia != null && this.tassonomia.size() >0){
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public Long getIdRr() {
		return this.idRr;
	}

	public void setIdRr(Long idRr) {
		this.idRr = idRr;
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

	public List<Long> getIdVersamenti() {
		return this.idVersamenti;
	}

	public void setIdVersamenti(List<Long> idVersamenti) {
		this.idVersamenti = idVersamenti;
	}

	public Long getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}

	public Long getIdIncasso() {
		return this.idIncasso;
	}

	public void setIdIncasso(Long idIncasso) {
		this.idIncasso = idIncasso;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public List<Stato> getStati() {
		return this.stati;
	}

	public void setStati(List<Stato> stati) {
		this.stati = stati;
	}

	public Integer getSogliaRitardo() {
		return this.sogliaRitardo;
	}

	public void setSogliaRitardo(Integer sogliaRitardo) {
		this.sogliaRitardo = sogliaRitardo;
	}

	public String getCodSingoloVersamentoEnte() {
		return this.codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getIur() {
		return this.iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public List<Long> getIdPagamenti() {
		return this.idPagamenti;
	}

	public void setIdPagamenti(List<Long> idPagamenti) {
		this.idPagamenti = idPagamenti;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public Date getDataPagamentoRitardoIncasso() {
		return this.dataPagamentoRitardoIncasso;
	}

	public void setDataPagamentoRitardoIncasso(Date dataPagamentoRitardoIncasso) {
		this.dataPagamentoRitardoIncasso = dataPagamentoRitardoIncasso;
	}

	public String getIdA2A() {
		return this.idA2A;
	}

	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}

	public List<TipoPagamento> getTipo() {
		return this.tipo;
	}

	public void setTipo(List<TipoPagamento> tipo) {
		this.tipo = tipo;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIdUnita() {
		return idUnita;
	}

	public void setIdUnita(String idUnita) {
		this.idUnita = idUnita;
	}

	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}

	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}

	public List<String> getDirezione() {
		return direzione;
	}

	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}

	public List<String> getDivisione() {
		return divisione;
	}

	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}

	public List<String> getTassonomia() {
		return tassonomia;
	}

	public void setTassonomia(List<String> tassonomia) {
		this.tassonomia = tassonomia;
	}

}
