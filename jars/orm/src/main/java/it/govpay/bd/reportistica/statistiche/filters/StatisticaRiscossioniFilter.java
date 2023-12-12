/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.bd.reportistica.statistiche.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.reportistica.statistiche.FiltroRiscossioni;
import it.govpay.orm.Pagamento;

public class StatisticaRiscossioniFilter extends AbstractFilter {
	
	
	private FiltroRiscossioni filtro = null;
	
	public StatisticaRiscossioniFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			if(this.filtro == null) 
				throw new ServiceException("Filtro non definito");
			
			
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.filtro.getCodApplicazione() != null && this.filtro.getCodApplicazione().size() > 0) {
				this.filtro.getCodApplicazione().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.filtro.getCodApplicazione());
				addAnd = true;
			}
			
			if(this.filtro.getIdApplicazione() != null) {
				if(addAnd)
					newExpression.and();

				CustomField idApplicazioneCustomField = new CustomField("id_applicazione", Long.class, "id_applicazione", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idApplicazioneCustomField, this.filtro.getIdApplicazione());
				addAnd = true;
			}
			
			if(this.filtro.getDataDa() != null && this.filtro.getDataA() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Pagamento.model().DATA_PAGAMENTO, this.filtro.getDataDa(),this.filtro.getDataA());
				addAnd = true;
			} else {
				if(this.filtro.getDataDa() != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Pagamento.model().DATA_PAGAMENTO, this.filtro.getDataDa());
					addAnd = true;
				} 
				
				if(this.filtro.getDataA() != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Pagamento.model().DATA_PAGAMENTO, this.filtro.getDataA());
					addAnd = true;
				}
			}

			if(this.filtro.getIdDominio() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idDominioCustomField = new CustomField("id_dominio", Long.class, "id_dominio", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idDominioCustomField, this.filtro.getIdDominio());
				addAnd = true;
				
			}
			
			if(this.filtro.getCodDominio() != null && this.filtro.getCodDominio().size() > 0){
				this.filtro.getCodDominio().removeAll(Collections.singleton(null));
				
				if(addAnd)
					newExpression.and();
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO, filtro.getCodDominio());
				addAnd = true;
			}
			
			if(this.filtro.getIdUo() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idUoCustomField = new CustomField("id_uo", Long.class, "id_uo", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idUoCustomField, this.filtro.getIdUo());
				addAnd = true;
				
			}
			
			if(this.filtro.getCodUo() != null && this.filtro.getCodUo().size() > 0){
				this.filtro.getCodUo().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO, this.filtro.getCodUo());
				addAnd = true;
			}
			
			if(this.filtro.getIdTipoVersamento() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idUoCustomField = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idUoCustomField, this.filtro.getIdTipoVersamento());
				addAnd = true;
				
			}
			
			if(this.filtro.getCodTipoVersamento() != null  && this.filtro.getCodTipoVersamento().size() > 0){
				this.filtro.getCodTipoVersamento().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.filtro.getCodTipoVersamento());
				addAnd = true;
			}
			
			if(this.filtro.getDirezione() != null && this.filtro.getDirezione().size() > 0){
				this.filtro.getDirezione().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE, this.filtro.getDirezione());
				addAnd = true;
			}
			
			if(this.filtro.getDivisione() != null && this.filtro.getDivisione().size() > 0){
				this.filtro.getDivisione().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE, this.filtro.getDivisione());
				addAnd = true;
			}
			
			if(this.filtro.getTassonomia() != null && this.filtro.getTassonomia().size() > 0){
				this.filtro.getTassonomia().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				newExpression.in(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA, this.filtro.getTassonomia());
				addAnd = true;
			}
			
			if(this.filtro.getTipo() != null && this.filtro.getTipo().size() > 0){
				this.filtro.getTipo().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
 
				List<String> tipi = this.getFiltro().getTipo().stream().map(e -> e.toString()).collect(Collectors.toList());
				newExpression.in(Pagamento.model().TIPO, tipi);
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

	public FiltroRiscossioni getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroRiscossioni filtro) {
		this.filtro = filtro;
	}

	public List<IField> getGruppiDaFiltro(){
		List<IField> gruppiDaFiltro = new ArrayList<IField>();
		
		if(this.filtro.getCodApplicazione() != null && this.filtro.getCodApplicazione().size() > 0) {
			this.filtro.getCodApplicazione().removeAll(Collections.singleton(null));
			
			if(this.filtro.getCodApplicazione().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE);
		}
		
		if(this.filtro.getCodDominio() != null && this.filtro.getCodDominio().size() > 0){
			this.filtro.getCodDominio().removeAll(Collections.singleton(null));
			
			if(this.filtro.getCodDominio().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO);
		}
		
		if(this.filtro.getCodUo() != null && this.filtro.getCodUo().size() > 0){
			this.filtro.getCodUo().removeAll(Collections.singleton(null));
		
			if(this.filtro.getCodUo().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO);
		}
		
		if(this.filtro.getCodTipoVersamento() != null  && this.filtro.getCodTipoVersamento().size() > 0){
			this.filtro.getCodTipoVersamento().removeAll(Collections.singleton(null));

			if(this.filtro.getCodTipoVersamento().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
		}
		
		if(this.filtro.getDirezione() != null && this.filtro.getDirezione().size() > 0){
			this.filtro.getDirezione().removeAll(Collections.singleton(null));

			if(this.filtro.getDirezione().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE);
		}
		
		if(this.filtro.getDivisione() != null && this.filtro.getDivisione().size() > 0){
			this.filtro.getDivisione().removeAll(Collections.singleton(null));

			if(this.filtro.getDivisione().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE);
		}
		
		if(this.filtro.getTassonomia() != null && this.filtro.getTassonomia().size() > 0){
			this.filtro.getTassonomia().removeAll(Collections.singleton(null));

			if(this.filtro.getTassonomia().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA);
		}
		
		if(this.filtro.getTipo() != null && this.filtro.getTipo().size() > 0){
			this.filtro.getTipo().removeAll(Collections.singleton(null));

			if(this.filtro.getTipo().size() > 0)
				gruppiDaFiltro.add(it.govpay.orm.Pagamento.model().TIPO);
		}
		
		return gruppiDaFiltro;
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}
}
