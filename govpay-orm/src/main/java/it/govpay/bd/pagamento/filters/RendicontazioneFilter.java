package it.govpay.bd.pagamento.filters;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;

public class RendicontazioneFilter extends AbstractFilter{
	
	// Interni alla tabella
	private String iuv;
	private String iur;
	private EsitoRendicontazione esito;
	private StatoRendicontazione stato;
	private Long idFr;
	private Long idPagamento;
	
	// Esterni alla tabella
	private String codDominio; // fr 
	private Long idApplicazione; // versamenti
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		//TODO GP-439.3 Bussu 
		return null;
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



	
}
