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
import java.util.Map;

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
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

public class VersamentoFilter extends AbstractFilter {

	private List<StatoVersamento> statiVersamento =  null;
	private String codUnivocoDebitore;
	private List<Long> idDomini;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idVersamento= null;
	private String codVersamento = null;
	private List<String> codVersamentoEnte = null;
	private List<Long> idApplicazione = null;
	
	public enum SortFields {
		STATO_ASC, STATO_DESC, SCADENZA_ASC, SCADENZA_DESC, AGGIORNAMENTO_ASC, AGGIORNAMENTO_DESC, CARICAMENTO_ASC, CARICAMENTO_DESC
	}

	public VersamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.statiVersamento != null){
				newExpression.in(Versamento.model().STATO_VERSAMENTO, toString(this.statiVersamento));
				addAnd = true;
			}

			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.datainizio,this.dataFine);
				addAnd = true;
			}

			if(this.codUnivocoDebitore != null) {
				if(addAnd)
					newExpression.and();
				newExpression.ilike(Versamento.model().DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore,LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				if(addAnd)
					newExpression.and();
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(Versamento.model()));
				newExpression.in(cf, this.idVersamento);
				addAnd = true;
			}

			if(this.idDomini != null){
				idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(Versamento.model().ID_UO));
				newExpression.in(cf, this.idDomini);
				newExpression.isNotNull(Versamento.model().ID_UO.COD_UO); //Sempre not null, solo per forzare la join
				addAnd = true;
			}

			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Versamento.model().COD_VERSAMENTO_ENTE, this.codVersamento, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			
			if(this.idApplicazione!= null && this.idApplicazione.size() > 0 && this.codVersamentoEnte!= null && this.codVersamentoEnte.size() > 0) {
				if(this.idApplicazione.size() == this.codVersamentoEnte.size()){
					if(addAnd)
						newExpression.and();

					IExpression orExpr = this.newExpression();
					List<IExpression> lstOrExpr = new ArrayList<IExpression>();
					
					VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
					CustomField cf = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(Versamento.model()));
					
					for (int i = 0; i < this.codVersamentoEnte.size(); i++) {
						String codV = this.codVersamentoEnte.get(i);
						Long idApp = this.idApplicazione.get(i);
						
						IExpression vExpr = this.newExpression();
						vExpr.equals(Versamento.model().COD_VERSAMENTO_ENTE, codV).and().equals(cf, idApp);
						
						lstOrExpr.add(vExpr);
					}
					
					orExpr.or(lstOrExpr.toArray(new IExpression[lstOrExpr.size()]));
					
					newExpression.and(orExpr);
					
					addAnd = true;
				}
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

	private List<String> toString(List<StatoVersamento> statiVersamento) {
		List<String> stati = new ArrayList<String>();
		for(StatoVersamento stato : statiVersamento)
			stati.add(stato.toString());
		return stati;
	}	

	public void addSortField(SortFields field) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();

		switch (field) {
		case AGGIORNAMENTO_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case AGGIORNAMENTO_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case CARICAMENTO_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case CARICAMENTO_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case SCADENZA_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case SCADENZA_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case STATO_ASC:
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case STATO_DESC:
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;
		}

		this.filterSortList.add(filterSortWrapper);
	}

	public List<StatoVersamento> getStatiVersamento() {
		return statiVersamento;
	}

	public void setStatiPagamento(List<StatoVersamento> statiVersamento) {
		this.statiVersamento = statiVersamento;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public List<Long> getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getCodVersamento() {
		return codVersamento;
	}

	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public void setStatoVersamento(StatoVersamento stato) {
		this.statiVersamento = new ArrayList<StatoVersamento>();
		this.statiVersamento.add(stato);
	}

	public List<String> getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(List<String> codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public List<Long> getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(List<Long> idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	
	

}
