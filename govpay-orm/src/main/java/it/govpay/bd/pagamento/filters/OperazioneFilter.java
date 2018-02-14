package it.govpay.bd.pagamento.filters;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
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
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.TipoTracciatoType;
import it.govpay.orm.Operazione;
import it.govpay.orm.dao.jdbc.converter.OperazioneFieldConverter;

public class OperazioneFilter extends AbstractFilter {
	
	private Long idTracciato = null;
	private StatoOperazioneType stato = null;
	private TipoOperazioneType tipoOperazione = null;
	private TipoTracciatoType tipoTracciato = null;
	private String codDominio =null;
	private String iuv;
	private String trn;
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor,simpleSearch);
	}

	public enum SortFields {
		LINEA
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.stato != null){
				newExpression.equals(it.govpay.orm.Operazione.model().STATO, this.stato.toString());
				addAnd = true;
			}
			
			if(this.tipoOperazione != null){
				newExpression.equals(it.govpay.orm.Operazione.model().TIPO_OPERAZIONE, this.tipoOperazione.toString());
				addAnd = true;
			}
			
			if(this.getIdTracciato() != null){
				if(addAnd)
					newExpression.and();
				OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idTracciatoCustomField = new CustomField("id_tracciato",  Long.class, "id_tracciato",converter.toTable(it.govpay.orm.Operazione.model()));
				
				newExpression.equals(idTracciatoCustomField, this.getIdTracciato());
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(Operazione.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Operazione.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.trn != null && StringUtils.isNotEmpty(this.trn)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Operazione.model().TRN, this.trn, LikeMode.ANYWHERE);
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
			OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			IExpression newExpression = super._toSimpleSearchExpression();
			if(this.getIdTracciato() != null){
				IExpression newExpressionIdTracciato = this.newExpression();
				CustomField idTracciatoCustomField = new CustomField("id_tracciato",  Long.class, "id_tracciato",converter.toTable(it.govpay.orm.Operazione.model()));
				newExpressionIdTracciato.equals(idTracciatoCustomField, this.idTracciato);
				newExpression.and(newExpressionIdTracciato);
				
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
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		IField ifield = null;
		switch(field){
		case LINEA: ifield = Operazione.model().LINEA_ELABORAZIONE;  
			break;
		default:
			break;
		
		}
		
		filterSortWrapper.setField(ifield);
		this.filterSortList.add(filterSortWrapper);
	}

	public Long getIdTracciato() {
		return idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public StatoOperazioneType getStato() {
		return stato;
	}

	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public TipoOperazioneType getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public void setTipoTracciato(TipoTracciatoType tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
		
		if(this.tipoTracciato != null) {
			this.listaFieldSimpleSearch = new ArrayList<IField>();
			this.listaFieldSimpleSearch.add(Operazione.model().COD_DOMINIO);
			switch (this.tipoTracciato) {
			case INCASSI:
				this.listaFieldSimpleSearch.add(Operazione.model().TRN);
				break;
			case VERSAMENTI:
				this.listaFieldSimpleSearch.add(Operazione.model().IUV);
			default:
				break;
			}
		}
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public TipoTracciatoType getTipoTracciato() {
		return tipoTracciato;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	
}
