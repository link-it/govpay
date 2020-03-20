package it.govpay.bd.reportistica.statistiche.filters;

import java.util.Collections;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.reportistica.statistiche.FiltroRendicontazioni;
import it.govpay.orm.Rendicontazione;

public class StatisticaRendicontazioniFilter extends AbstractFilter {


	private FiltroRendicontazioni filtro = null;

	public StatisticaRendicontazioniFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			if(this.filtro == null) 
				throw new ServiceException("Filtro non definito");


			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.filtro.getDataFlussoDa() != null && this.filtro.getDataFlussoA() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO, this.filtro.getDataFlussoDa(),this.filtro.getDataFlussoA());
				addAnd = true;
			} else {
				if(this.filtro.getDataFlussoDa() != null) {
					if(addAnd)
						newExpression.and();

					newExpression.greaterEquals(Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO, this.filtro.getDataFlussoDa());
					addAnd = true;
				} 

				if(this.filtro.getDataFlussoA() != null) {
					if(addAnd)
						newExpression.and();

					newExpression.lessEquals(Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO, this.filtro.getDataFlussoA());
					addAnd = true;
				}
			}

			if(this.filtro.getDataRendicontazioneDa() != null && this.filtro.getDataRendicontazioneA() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Rendicontazione.model().DATA, this.filtro.getDataRendicontazioneDa(),this.filtro.getDataRendicontazioneA());
				addAnd = true;
			} else {
				if(this.filtro.getDataRendicontazioneDa() != null) {
					if(addAnd)
						newExpression.and();

					newExpression.greaterEquals(Rendicontazione.model().DATA, this.filtro.getDataRendicontazioneDa());
					addAnd = true;
				} 

				if(this.filtro.getDataRendicontazioneA() != null) {
					if(addAnd)
						newExpression.and();

					newExpression.lessEquals(Rendicontazione.model().DATA, this.filtro.getDataRendicontazioneA());
					addAnd = true;
				}
			}

			if(this.filtro.getCodFlusso() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Rendicontazione.model().ID_FR.COD_FLUSSO, this.filtro.getCodFlusso());
				addAnd = true;
			} 

			if(this.filtro.getIuv() != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Rendicontazione.model().IUV, this.filtro.getIuv());
				addAnd = true;
			}

			if(this.filtro.getDirezione() != null && this.filtro.getDirezione().size() > 0){
				this.filtro.getDirezione().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE, this.filtro.getDirezione());
				addAnd = true;
			}

			if(this.filtro.getDivisione() != null && this.filtro.getDivisione().size() > 0){
				this.filtro.getDivisione().removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE, this.filtro.getDivisione());
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
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	public FiltroRendicontazioni getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroRendicontazioni filtro) {
		this.filtro = filtro;
	}
}
