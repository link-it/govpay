package it.govpay.bd.pagamento.filters;

import java.util.Collections;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;

public class SingoloVersamentoFilter extends AbstractFilter {

	private String codSingoloVersamentoEnte = null;
	private List<String> codSingoliVersamentiEnte = null;
	private List<Long> idSingoloVersamento= null;
	
	public SingoloVersamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.codSingoloVersamentoEnte != null){
				newExpression.ilike(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codSingoliVersamentiEnte != null){
				codSingoliVersamentiEnte.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoliVersamentiEnte);
				addAnd = true;
			}
			
			if(this.idSingoloVersamento != null && !this.idSingoloVersamento.isEmpty()){
				if(addAnd)
					newExpression.and();
				SingoloVersamentoFieldConverter converter = new SingoloVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(SingoloVersamento.model()));
				newExpression.in(cf, this.idSingoloVersamento);
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

	
	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public List<String> getCodSingoliVersamentiEnte() {
		return codSingoliVersamentiEnte;
	}

	public void setCodSingoliVersamentiEnte(List<String> codSingoliVersamentiEnte) {
		this.codSingoliVersamentiEnte = codSingoliVersamentiEnte;
	}

	public List<Long> getIdSingoloVersamento() {
		return idSingoloVersamento;
	}

	public void setIdSingoloVersamento(List<Long> idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}
	
	
}
