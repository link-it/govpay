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
package it.govpay.ejb.core.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Rappresenta una posizione debitoria. 
 * Ogni posizione può avere una o più condizioni di pagamento
 * (pagamento singolo o rate).
 * Una pendenza ha un proprio codice identificativo univoco noto all'ente (idDebitoEnte)
 * e una serie di condizioni di pagamento ognuna caratterizzata da un proprio identificativo univoco noto all'ente (idPagamentoEnte)
 * 
 * @author RepettiS
 *
 */
public class PendenzaModel {
	
	public enum EnumModalitaPagamento {
		/** SOLUZIONE UNICA */ S, 
		/** A RATE */ R, 
		/** ENTRAMBE */ E
	}
	public enum EnumStatoPendenza {
		/** APERTA */ A, 
		/** CHIUSA */ C, 
		/** NASCOSTA */ H
	}
	
	/**
	 * Identificativo fisico della pendenza
	 */
	private String idPendenza; 
	
	/**
	 * Id fisico dell'ente Creditore
	 */
	private String idEnteCreditore; 
	
	/**
	 * Id della categoria debito.
	 * e.g. PrestazioniSanitarie
	 */	
	private String idTributo; //IdTributo
	
	/**
	 * Identificativo del tipo debito assegnato dall'ente creditore
	 * e.g. TICKET_SANITARIO_CUP
	 */
	private String codiceTributo; //cd_trb_ente
	
	/**
	 * Identificativo del debito noto al creditore.
	 * 
	 */
	private String idDebitoEnte; //idPagamentoente
	
	/**
	 * Data decorrenza
	 */
	private Date dataDecorrenza;
	
	/**
	 * Timestamp di inserimento del debito nel sistema Ente
	 */
	private Date dataCreazione;
	
	/**
	 * Timestamp di emissione del debito
	 */
	private Date dataEmissione;
	
	/**
	 * Data prescrizione
	 */
	private Date dataPrescrizione;
	
	/**
	 * Anno di riferimento del debito
	 */
	private int annoRiferimento;
	
	/**
	 * Importo totale del debito (nel caso di rateizzazioni, somma dei totali delle singole condizioni di pagamento)
	 */
	private BigDecimal importoTotale;
	
	/**
	 * Soggetto Debitore
	 */
	private DestinatarioPendenzaModel debitore;
	
	/**
	 * Causale testuale del debito
	 */
	private String causale;
	
	/**
	 * EnumStatoOperazione della posizione
	 */
	private EnumStatoPendenza statoPendenza;
	
	/**
	 * Modalità di pagamento della posizione (rate, soluzione unica, entrambe ammesse)
	 */
	private EnumModalitaPagamento modalitaPagamento;
	
	/**
	 * Condizioni di pagamento 
	 */
	private List<CondizionePagamentoModel> condizioniPagamento;
	
	
	public class PendenzaComparator implements Comparator<PendenzaModel> {
		@Override
		public int compare(PendenzaModel o1, PendenzaModel o2) {
			if(o1 == null) return -1;
			if(o2 == null) return 1;
			if(o1 == o2) return 0;
			if(o1.getIdPendenza() == null) return -1;
			if(o2.getIdPendenza() == null) return 1;
			return o1.getIdPendenza().compareTo(o2.getIdPendenza());
		}
	}

	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public String getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(String idCreditore) {
		this.idEnteCreditore = idCreditore;
	}

	public String getIdTributo() {
		return idTributo;
	}

	public void setIdTributo(String idTributo) {
		this.idTributo = idTributo;
	}

	public String getCodiceTributo() {
		return codiceTributo;
	}

	public void setCodiceTributo(String codiceTributo) {
		this.codiceTributo = codiceTributo;
	}

	public String getIdDebitoEnte() {
		return idDebitoEnte;
	}

	public void setIdDebitoEnte(String idDebitoEnte) {
		this.idDebitoEnte = idDebitoEnte;
	}

	public Date getDataDecorrenza() {
		return dataDecorrenza;
	}

	public void setDataDecorrenza(Date dataDecorrenza) {
		this.dataDecorrenza = dataDecorrenza;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public Date getDataPrescrizione() {
		return dataPrescrizione;
	}

	public void setDataPrescrizione(Date dataPrescrizione) {
		this.dataPrescrizione = dataPrescrizione;
	}

	public int getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public DestinatarioPendenzaModel getDebitore() {
		return debitore;
	}

	public void setDebitore(DestinatarioPendenzaModel debitore) {
		this.debitore = debitore;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public EnumStatoPendenza getStatoPendenza() {
		return statoPendenza;
	}

	public void setStatoPendenza(EnumStatoPendenza statoPendenza) {
		this.statoPendenza = statoPendenza;
	}

	public EnumModalitaPagamento getModalitaPagamento() {
		return modalitaPagamento;
	}

	public void setModalitaPagamento(EnumModalitaPagamento modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}

	public List<CondizionePagamentoModel> getCondizioniPagamento() {
		return condizioniPagamento;
	}

	public void setCondizioniPagamento(List<CondizionePagamentoModel> condizioniPagamento) {
		this.condizioniPagamento = condizioniPagamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPendenza == null) ? 0 : idPendenza.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PendenzaModel other = (PendenzaModel) obj;
		if (idPendenza == null) {
			if (other.idPendenza != null)
				return false;
		} else if (!idPendenza.equals(other.idPendenza))
			return false;
		return true;
	}
		
	
}
