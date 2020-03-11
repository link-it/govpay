package it.govpay.bd.viste.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoConservazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.VistaRptVersamento;
import it.govpay.orm.dao.jdbc.converter.VistaRptVersamentoFieldConverter;

public class RptFilter extends AbstractFilter {

	private List<Long> idVersamento;
	private String iuv;
	private String ccp;
	private String codDominio;
	private List<String> idDomini;
	private Boolean conservato;
	private List<String> stato;
	private List<Long> idRpt= null;
	private Long idPagamentoPortale = null;
	private String codPagamentoPortale = null;
	private Date dataInizio;
	private Date dataFine;
	private String codApplicazione = null;
	private String idPendenza = null;
	private EsitoPagamento esitoPagamento = null;
	
	private String cfCittadinoPagamentoPortale = null;
	private String codApplicazionePagamentoPortale = null;
	
	private Date dataRtA;
	private Date dataRtDa;
	private String idDebitore;
	private String idUnita;
	private String idTipoPendenza;
	private List<String> direzione;
	private List<String> divisione;
	private String tassonomia;
	private String anagraficaDebitore;

	public RptFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public RptFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VistaRptVersamento.model().IUV);
		this.listaFieldSimpleSearch.add(VistaRptVersamento.model().CCP);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.idVersamento != null && !this.idVersamento.isEmpty()) {
				this.idVersamento.removeAll(Collections.singleton(null));				
				addAnd = true;
				VistaRptVersamentoFieldConverter rptFieldConverter = new VistaRptVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(VistaRptVersamento.model()));
				newExpression.in(idRptCustomField, this.idVersamento);
			}

			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaRptVersamento.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ccp != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaRptVersamento.model().CCP, this.ccp, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(VistaRptVersamento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(VistaRptVersamento.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(this.idRpt != null && !this.idRpt.isEmpty()){
				if(addAnd)
					newExpression.and();
				VistaRptVersamentoFieldConverter converter = new VistaRptVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model()));
				newExpression.in(cf, this.idRpt);
				addAnd = true;
			}
			
			if(this.conservato != null){
				if(addAnd)
					newExpression.and();
				
				IExpression newExpression2 = this.newExpression();
				if(this.conservato) {
					newExpression2.notEquals(VistaRptVersamento.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).and().isNotNull(VistaRptVersamento.model().STATO_CONSERVAZIONE);
				} else {
					newExpression2.equals(VistaRptVersamento.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).or().isNull(VistaRptVersamento.model().STATO_CONSERVAZIONE);					
				}
				
				newExpression.and(newExpression2);
				addAnd = true;
			}

			if(this.stato != null && !this.stato.isEmpty()){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(VistaRptVersamento.model().STATO, this.stato);
				addAnd = true;
			}
			
			if(this.idPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				
				VistaRptVersamentoFieldConverter rptFieldConverter = new VistaRptVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_pagamento_portale",  Long.class, "id_pagamento_portale",  rptFieldConverter.toTable(VistaRptVersamento.model()));
				newExpression.equals(idRptCustomField, this.idPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codApplicazionePagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazionePagamentoPortale);
				addAnd = true;
			}
			
			if(this.cfCittadinoPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				IExpression newExpression2 = this.newExpression();
				newExpression2.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO, this.cfCittadinoPagamentoPortale)
					.or().equals(VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO, this.cfCittadinoPagamentoPortale);
				
				
				newExpression.and(newExpression2);
				addAnd = true;
			}
			
			if(this.codApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idPendenza != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE, this.idPendenza);
				addAnd = true;
			}
			
			if(this.dataInizio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(VistaRptVersamento.model().DATA_MSG_RICHIESTA, this.dataInizio);
				addAnd = true;
			}
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(VistaRptVersamento.model().DATA_MSG_RICHIESTA, this.dataFine);
				addAnd = true;
			}
			
			if(this.esitoPagamento != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().COD_ESITO_PAGAMENTO, this.esitoPagamento.getCodifica());
				addAnd = true;
			}
			
			if(this.idDebitore != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO, this.idDebitore);
				addAnd = true;
			}
			
			if(this.dataRtDa != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(VistaRptVersamento.model().DATA_MSG_RICEVUTA, this.dataRtDa);
				addAnd = true;
			}
			if(this.dataRtA != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(VistaRptVersamento.model().DATA_MSG_RICEVUTA, this.dataRtA);
				addAnd = true;
			}
			
			if(this.idUnita != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(VistaRptVersamento.model().VRS_ID_UO.COD_UO, this.idUnita);
				addAnd = true;
			}
			
			if(this.idTipoPendenza != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.idTipoPendenza);
				addAnd = true;
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaRptVersamento.model().VRS_DIREZIONE, this.direzione);
				addAnd = true;
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaRptVersamento.model().VRS_DIVISIONE, this.divisione);
				addAnd = true;
			}
			
			if(this.tassonomia != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(VistaRptVersamento.model().VRS_TASSONOMIA, this.tassonomia);
				addAnd = true;
			}
			
			if(this.anagraficaDebitore != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA, this.anagraficaDebitore, LikeMode.ANYWHERE);
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
			IExpression newExpression = super._toSimpleSearchExpression();
			
			if(this.idVersamento != null){
				IExpression newExpressionVersamento = this.newExpression();
				VistaRptVersamentoFieldConverter rptFieldConverter = new VistaRptVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(VistaRptVersamento.model()));
				newExpressionVersamento.equals(idRptCustomField, this.idVersamento);
				newExpression.and(newExpressionVersamento);
			}

			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(VistaRptVersamento.model().COD_DOMINIO, this.idDomini);
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

	public List<Long> getIdVersamenti() {
		return this.idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = new ArrayList<>();
		this.idVersamento.add(idVersamento);
	}
	
	public void setIdVersamenti(List<Long> ids) {
		this.idVersamento = new ArrayList<>();
		if(ids != null && !ids.isEmpty())
			this.idVersamento.addAll(ids);
	}

	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public void setStato(StatoRpt stato) {
		this.stato = new ArrayList<>();
		if(stato!= null)
			this.stato.add(stato.name());
	}
	
	public void setStato(List<StatoRpt> stati) {
		this.stato = new ArrayList<String>();
		for(StatoRpt s : stati)
			this.stato.add(s.name());
	}

	public Boolean getConservato() {
		return this.conservato;
	}

	public void setConservato(Boolean conservato) {
		this.conservato = conservato;
	}

	public List<Long> getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(List<Long> idRpt) {
		this.idRpt = idRpt;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Long getIdPagamentoPortale() {
		return this.idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}
	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPagamentoPortale() {
		return this.codPagamentoPortale;
	}

	public void setCodPagamentoPortale(String codPagamentoPortale) {
		this.codPagamentoPortale = codPagamentoPortale;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCfCittadinoPagamentoPortale() {
		return cfCittadinoPagamentoPortale;
	}

	public void setCfCittadinoPagamentoPortale(String cfCittadinoPagamentoPortale) {
		this.cfCittadinoPagamentoPortale = cfCittadinoPagamentoPortale;
	}

	public String getCodApplicazionePagamentoPortale() {
		return codApplicazionePagamentoPortale;
	}

	public void setCodApplicazionePagamentoPortale(String codApplicazionePagamentoPortale) {
		this.codApplicazionePagamentoPortale = codApplicazionePagamentoPortale;
	}

	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}

	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}

	public Date getDataRtA() {
		return dataRtA;
	}

	public void setDataRtA(Date dataRtA) {
		this.dataRtA = dataRtA;
	}

	public Date getDataRtDa() {
		return dataRtDa;
	}

	public void setDataRtDa(Date dataRtDa) {
		this.dataRtDa = dataRtDa;
	}

	public String getIdDebitore() {
		return idDebitore;
	}

	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}

	public String getIdUnita() {
		return idUnita;
	}

	public void setIdUnita(String idUnita) {
		this.idUnita = idUnita;
	}

	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}

	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}

	public List<String> getDirezione() {
		return direzione;
	}

	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}

	public List<String> getDivisione() {
		return divisione;
	}

	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}

	public String getTassonomia() {
		return tassonomia;
	}

	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	public String getAnagraficaDebitore() {
		return anagraficaDebitore;
	}

	public void setAnagraficaDebitore(String anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
}
