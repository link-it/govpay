/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.ejb.model;

import java.math.BigDecimal;
import java.util.Date;

public class CondizionePagamentoModel {

	/**
	 * Tipo Pagamento
	 *
	 */
	public enum EnumTipoPagamento {
		/** A RATE */ R, 
		/** SINGOLO PAGAMENTO */ S
	}
	
	/**
	 * EnumStatoOperazione del pagamento comunicato dall'ente 
	 * (per usi futuri)
	 */
	public enum EnumStatoPagamento {   //TODO: [SR] cambiare nome, fa confusione è duplicato
		/** Pagamento IRREGOLARE */ E, 
		/** NON Pagato */ N, 
		/** PAGATO */ P, 
		/** NON Pagabile */ X, 
	}
		
	/**
	 * Identificativo della condizione di pagamento
	 */
	private String idCondizione; // Identificativo fisico della condizione
	
	/**
	 * Data Decorrenza  
	 */
	private Date dataDecorrenza;
	
	/**	
	 * Tipo del pagamento (A rate, Soluzione unica)
	 */
	private EnumTipoPagamento tipoPagamento;
	
	/**
	 * Identificativo dell'ente creditore
	 */
	private String idCreditore; // Identificativo fisico dell'ente
	
	/**
	 * Identificativo del tipo tributo noto all'ente creditore
	 */
	private String codiceTributo; 
	
	/**
	 * Identificativo della condizione di pagamento, conosciuto dall'ente creditore
	 * Per NDP pagamenti ad iniziativa PSP questo coincide con lo IUV.
	 * Per NDP pagamenti ad immediati e differiti questo identificativo non può coincidere con lo IUV
	 * ma coinciderà con l'identificativo del singolo versamento (Noto come IUSV nel servizio GovPay) 
	 */
	private String idPagamentoEnte; 
	
	/**
	 * Data scadenza del pagamento
	 */
	private Date dataScadenza;

	/**
	 * Data inizio di validità del pagamento, prima di questa data non è pagabile su GovPay
	 */	
	private Date dataInizioValidita;
	
	/**
	 * Data fine di validità del pagamento, dopo di questa data non è pagabile su GovPay
	 */	
	private Date dataFineValidita;
	
	/**
	 * Importo totale della condizione di pagamento
	 */	
	private BigDecimal importoTotale;
	
	/**
	 * Causale specifica della condizione di pagamento (facoltativa)
	 * Ad. esempio: "Prima Rata".
	 */
	private String causale;
	
	/**
	 * Iban beneficiario per il riversamento delle somme di questo pagamento.
	 * Se non specificato il sistema utilizza quello configurato a livello del tributo
	 * Se specificato deve corrispondere ad un IBAN inserito in una white list in configurazione
	 */
	private String ibanBeneficiario;
	
	/**
	 * Ragione sociale dell'intestatario del conto ibanBeneficiario.
	 * Se non specificato il sistema utilizza la ragione sociale dell'ente come da configurazione 
	 */
	private String ragioneSocaleBeneficiario;
	
	
	public String getIdCondizione() {
		return idCondizione;
	}
	public void setIdCondizione(String idCondizione) {
		this.idCondizione = idCondizione;
	}
	public Date getDataDecorrenza() {
		return dataDecorrenza;
	}
	public void setDataDecorrenza(Date dataDecorrenza) {
		this.dataDecorrenza = dataDecorrenza;
	}
	public EnumTipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}
	public void setTipoPagamento(EnumTipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	public String getIdCreditore() {
		return idCreditore;
	}
	public void setIdCreditore(String idCreditore) {
		this.idCreditore = idCreditore;
	}
	public String getCodiceTributo() {
		return codiceTributo;
	}
	public void setCodiceTributo(String idTributo) {
		this.codiceTributo = idTributo;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getIbanBeneficiario() {
		return ibanBeneficiario;
	}
	public void setIbanBeneficiario(String ibanBeneficiario) {
		this.ibanBeneficiario = ibanBeneficiario;
	}
	public String getRagioneSocaleBeneficiario() {
		return ragioneSocaleBeneficiario;
	}
	public void setRagioneSocaleBeneficiario(String ragioneSocaleBeneficiario) {
		this.ragioneSocaleBeneficiario = ragioneSocaleBeneficiario;
	}
	public String getIdPagamentoEnte() {
		return idPagamentoEnte;
	}
	public void setIdPagamentoEnte(String iusv) {
		this.idPagamentoEnte = iusv;
	}
	
	
}
