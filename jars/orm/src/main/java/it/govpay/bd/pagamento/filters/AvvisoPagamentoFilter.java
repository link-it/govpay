package it.govpay.bd.pagamento.filters;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.orm.Avviso;
import it.govpay.orm.dao.jdbc.converter.AvvisoFieldConverter;

public class AvvisoPagamentoFilter extends AbstractFilter {
	
	private StatoAvviso stato;
	private StatoOperazioneType statoOperazione;
	private TipoOperazioneType tipoOperazione;
	private String codDominio;
	private String iuv;
	private Long idTracciato;

	public AvvisoPagamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public AvvisoPagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Avviso.model().COD_DOMINIO);
		this.listaFieldSimpleSearch.add(Avviso.model().IUV);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.stato != null) {
				newExpression.equals(Avviso.model().STATO, this.stato.toString());
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Avviso.model().COD_DOMINIO, this.codDominio);
			}
			
			if(this.iuv != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Avviso.model().IUV, this.iuv);
			}
			
			if(this.statoOperazione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Avviso.model().OPERAZIONE.STATO, this.statoOperazione.toString());
				addAnd = true;
			}
			
			if(this.tipoOperazione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Avviso.model().OPERAZIONE.TIPO_OPERAZIONE, this.tipoOperazione.toString());
				addAnd = true;
			}
			
			if(this.idTracciato != null){
				AvvisoFieldConverter converter = new AvvisoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				
				if(addAnd)
					newExpression.and();
				
				// TODO fare model
				CustomField idOperatoreCustomField = new CustomField("id_tracciato",  Long.class, "id_tracciato",converter.toTable(it.govpay.orm.Avviso.model().OPERAZIONE));
				newExpression.equals(idOperatoreCustomField, this.idTracciato);
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

	public StatoAvviso getStato() {
		return this.stato;
	}

	public void setStato(StatoAvviso stato) {
		this.stato = stato;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public StatoOperazioneType getStatoOperazione() {
		return this.statoOperazione;
	}

	public void setStatoOperazione(StatoOperazioneType statoOperazione) {
		this.statoOperazione = statoOperazione;
	}

	public TipoOperazioneType getTipoOperazione() {
		return this.tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
 

}
