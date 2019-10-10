package it.govpay.bd.reportistica.statistiche.filters;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

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

			if(this.filtro.getCodApplicazione() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.filtro.getCodApplicazione());
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
			
			if(this.filtro.getCodDominio() != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(Pagamento.model().COD_DOMINIO, filtro.getCodDominio());
				addAnd = true;
			}
			
			if(this.filtro.getIdUo() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idUoCustomField = new CustomField("id_uo", Long.class, "id_uo", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idUoCustomField, this.filtro.getIdUo());
				addAnd = true;
				
			}
			
			if(this.filtro.getCodUo() != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO, this.filtro.getCodUo());
				addAnd = true;
			}
			
			if(this.filtro.getIdTipoVersamento() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idUoCustomField = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", this.getTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idUoCustomField, this.filtro.getIdTipoVersamento());
				addAnd = true;
				
			}
			
			if(this.filtro.getCodTipoVersamento() != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.filtro.getCodTipoVersamento());
				addAnd = true;
			}
			
			if(this.filtro.getDirezione() != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE, this.filtro.getDirezione());
				addAnd = true;
			}
			
			if(this.filtro.getDivisione() != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE, this.filtro.getDivisione());
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

	
}
