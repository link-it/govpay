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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Pagamento.Stato;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {

	public enum TIPO_PAGAMENTO {ENTRATA, MBT}
	
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
	private List<String> stati;
	private Integer sogliaRitardo = null;
	private String codSingoloVersamentoEnte = null;
	private String iur;
	private String iuv;
	private String idA2A;
	private TIPO_PAGAMENTO tipo;

	public enum SortFields {
		DATA
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE);
		this.listaFieldSimpleSearch.add(Pagamento.model().IUR);
		this.listaFieldSimpleSearch.add(Pagamento.model().IUV);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

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

				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.idA2A);
				addAnd = true;
			}

			if(this.tipo != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Pagamento.model().TIPO,this.tipo.toString());
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

				newExpression.between(Pagamento.model().DATA_ACQUISIZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(Pagamento.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(Pagamento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.dataPagamentoRitardoIncasso != null) {
				if(addAnd)
					newExpression.and();

				newExpression.lessThan(Pagamento.model().DATA_PAGAMENTO, this.dataPagamentoRitardoIncasso);
				newExpression.notEquals(Pagamento.model().STATO,Stato.INCASSATO.name());
				addAnd = true;
			}

			if(this.stati != null && !this.stati.isEmpty()){
				if(addAnd)
					newExpression.and();

				newExpression.in(Pagamento.model().STATO,this.stati);

				addAnd = true;
			}

			if(this.idVersamenti != null && this.idVersamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField idVersamentoField = new CustomField(ALIAS_ID, Long.class, ALIAS_ID,
						pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.in(idVersamentoField, this.idVersamenti);
				// forzo la join con singoliversamenti
				newExpression.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE); 
				newExpression.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO);
				addAnd = true;
			}

			if(this.idPagamenti != null && this.idPagamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, pagamentoFieldConverter.toTable(Pagamento.model()));
				newExpression.in(cf, this.idPagamenti);
				addAnd = true;
			}

			if(this.codSingoloVersamentoEnte != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iur != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Pagamento.model().IUR, this.iur, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Pagamento.model().IUV, this.iuv, LikeMode.ANYWHERE);
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
			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

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
				CustomField idVersamentoField = new CustomField(ALIAS_ID, Long.class, ALIAS_ID,
						pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpressionVersamenti.in(idVersamentoField, this.idVersamenti);
				// forzo la join con singoliversamenti
				newExpressionVersamenti.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE); 
				newExpressionVersamenti.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO);
				newExpression.and(newExpressionVersamenti);
			}

			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(Pagamento.model().COD_DOMINIO, this.idDomini);
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
			filterSortWrapper.setField(Pagamento.model().DATA_ACQUISIZIONE); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
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

	public List<String> getStati() {
		return this.stati;
	}

	public void setStati(List<String> stati) {
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

	public TIPO_PAGAMENTO getTipo() {
		return this.tipo;
	}

	public void setTipo(TIPO_PAGAMENTO tipo) {
		this.tipo = tipo;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

}
