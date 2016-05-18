package it.govpay.bd.pagamento.filters;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import it.govpay.orm.Evento;
import it.govpay.orm.dao.jdbc.converter.EventoFieldConverter;

public class EventiFilter extends AbstractFilter{
	
	private String codDominio= null;
	private String iuv;
	private String ccp;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idEventi= null;
	
	public EventiFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = newExpression();
			
			boolean addAnd = false;
			
			if(this.codDominio != null){
				newExpression.equals(Evento.model().COD_DOMINIO, this.codDominio);
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

				newExpression.between(Evento.model().DATA_1, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			
			if(this.idEventi != null && !this.idEventi.isEmpty()){
				EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idEventoField = new CustomField("id",  Long.class, "id",  eventoFieldConverter.toTable(Evento.model()));
				newExpression.in(idEventoField, this.idEventi);
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

	public String getCcp() {
		return ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Date getDatainizio() {
		return datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public List<Long> getIdEventi() {
		return idEventi;
	}

	public void setIdEventi(List<Long> idEventi) {
		this.idEventi = idEventi;
	}

	
}
