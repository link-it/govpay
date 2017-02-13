package it.govpay.bd.pagamento.filters;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.Rendicontazione;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

public class RendicontazioneFilter extends AbstractFilter{
	
	// Interni alla tabella
	private String iuv;
	private String iur;
	private EsitoRendicontazione esito;
	private StatoRendicontazione stato;
	private Long idFr;
	private Long idPagamento;
	private String tipo;
	
	// Esterni alla tabella
	private String codDominio; // fr 
	private Long idApplicazione; // versamenti
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			
			if(this.iuv != null) {
				exp.ilike(Rendicontazione.model().IUV, this.iuv,LikeMode.ANYWHERE);
			}

			if(this.iur != null) {
				exp.equals(Rendicontazione.model().IUR, this.iur);
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

			if(this.idApplicazione != null) {
				exp.isNotNull(Rendicontazione.model().ID_PAGAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO); //per forzare la join
				CustomField idApplicazioneField = new CustomField("id_applicazione", Long.class, "id_applicazione", this.getTable(Rendicontazione.model().ID_PAGAMENTO.ID_VERSAMENTO));
				exp.equals(idApplicazioneField, this.idApplicazione);
				
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

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public EsitoRendicontazione getEsito() {
		return esito;
	}

	public void setEsito(EsitoRendicontazione esito) {
		this.esito = esito;
	}

	public StatoRendicontazione getStato() {
		return stato;
	}

	public void setStato(StatoRendicontazione stato) {
		this.stato = stato;
	}

	public Long getIdFr() {
		return idFr;
	}

	public void setIdFr(Long idFr) {
		this.idFr = idFr;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
