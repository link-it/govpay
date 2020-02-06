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
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.Rendicontazione;

public class RendicontazioneFilter extends AbstractFilter{
	
	// Interni alla tabella
	private String iuv;
	private String iur;
	private EsitoRendicontazione esito;
	private StatoRendicontazione stato;
	private Long idFr;
	private Long idPagamento;
	private String tipo;
	private Integer indiceDati;
	
	// Esterni alla tabella
	private String codDominio; // fr 
	private Long idApplicazione; // versamenti
	private List<String> idDomini;
	private List<Long> idRendicontazione;
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Rendicontazione.model().IUV);
		this.listaFieldSimpleSearch.add(Rendicontazione.model().IUR);
		this.listaFieldSimpleSearch.add(Rendicontazione.model().ID_FR.COD_DOMINIO);
	}
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.idFr != null) {
				IExpression newExpressionFr = this.newExpression();
				
				CustomField idFrField = new CustomField("id_fr", Long.class, "id_fr", this.getRootTable());
				newExpressionFr.equals(idFrField, this.idFr);
				newExpression.and(newExpressionFr);
			}
			
			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(Rendicontazione.model().ID_FR.COD_DOMINIO, this.idDomini);
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

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			
			if(this.iuv != null) {
				exp.ilike(Rendicontazione.model().IUV, this.iuv,LikeMode.ANYWHERE);
			}

			if(this.iur != null) {
				exp.equals(Rendicontazione.model().IUR, this.iur);
			}
			
			if(this.indiceDati != null) {
				exp.equals(Rendicontazione.model().INDICE_DATI, this.indiceDati);
			}
			
			if(this.tipo!= null) {
				exp.equals(Rendicontazione.model().ESITO, EsitoRendicontazione.valueOf(this.tipo).getCodifica());
			}
			
			if(this.esito!= null) {
				exp.equals(Rendicontazione.model().ESITO, this.esito.getCodifica());
			}

			if(this.stato!= null) {
				exp.equals(Rendicontazione.model().STATO, this.stato.toString());
			}

			if(this.idFr != null) {
				CustomField idFrField = new CustomField("id_fr", Long.class, "id_fr", this.getRootTable());
				exp.equals(idFrField, this.idFr);
			}

			if(this.idPagamento != null) {
				CustomField idPagamentoField = new CustomField("id_pagamento", Long.class, "id_pagamento", this.getRootTable());
				exp.equals(idPagamentoField, this.idPagamento);
			}

			if(this.codDominio != null) {
				exp.equals(Rendicontazione.model().ID_FR.COD_DOMINIO, this.codDominio);
			}
			
			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				exp.in(Rendicontazione.model().ID_FR.COD_DOMINIO, this.idDomini);
			}

			if(this.idApplicazione != null) {
				exp.isNotNull(Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO); //per forzare la join
				CustomField idApplicazioneField = new CustomField("id_applicazione", Long.class, "id_applicazione", this.getTable(Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				exp.equals(idApplicazioneField, this.idApplicazione);
				
			}
			
			if(this.idRendicontazione != null && this.idRendicontazione.size() >0){ 
				CustomField cf = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, this.getTable(Rendicontazione.model()));
				exp.in(cf, this.idRendicontazione);
			}

			return exp;
			
			
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
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

	public String getIur() {
		return this.iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public EsitoRendicontazione getEsito() {
		return this.esito;
	}

	public void setEsito(EsitoRendicontazione esito) {
		this.esito = esito;
	}

	public StatoRendicontazione getStato() {
		return this.stato;
	}

	public void setStato(StatoRendicontazione stato) {
		this.stato = stato;
	}

	public Long getIdFr() {
		return this.idFr;
	}

	public void setIdFr(Long idFr) {
		this.idFr = idFr;
	}

	public Long getIdPagamento() {
		return this.idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public List<Long> getIdRendicontazione() {
		return this.idRendicontazione;
	}

	public void setIdRendicontazione(List<Long> idRendicontazione) {
		this.idRendicontazione = idRendicontazione;
	}

	public Integer getIndiceDati() {
		return this.indiceDati;
	}

	public void setIndiceDati(Integer indiceDati) {
		this.indiceDati = indiceDati;
	}
	
}
