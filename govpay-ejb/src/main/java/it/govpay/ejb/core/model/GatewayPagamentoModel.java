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

import it.govpay.ejb.utils.EnumChiaveDescrizione;

import java.util.Date;

/**
 * Modello di un GatewayPagamento GovPay
 * 
 * @author RepettiS
 *
 */
public class GatewayPagamentoModel {	
	public enum EnumStato {
		ATTIVO, DISATTIVO, DA_ATTIVARE   
	}
	public enum EnumModalitaPagamento implements EnumChiaveDescrizione {
		BONIFICO_BANCARIO_TESORERIA("Bonifico bancario"), 
		BOLLETTINO_POSTALE("Bollettino postale"), 
		ADDEBITO_DIRETTO("Addebito diretto"), 
		CARTA_PAGAMENTO("Carta pagamento"), 
		ATTIVATO_PRESSO_PSP("Attivato presso PSP");
		
		private String chiave;
		private String descrizione;

		EnumModalitaPagamento(String descrizione) {
			this.chiave = name();
			this.descrizione = descrizione;
		}
		public String getChiave() {
			return chiave;
		}
		public String getDescrizione() {
			return descrizione;
		}
	}
	public enum EnumStrumentoPagamento {
		BONIFICO, ADDEBITO_DIRETTO, CARTA_PAGAMENTO, DOCUMENTO_PAGAMENTO
	}
	public enum EnumCanalePagamento {
		WEB, BANCA, POSTE, PSP
	}
	public enum EnumDocumentoPagamento {
		BOLLETTINO_NDP
	}	
	public enum EnumFornitoreGateway {
		NODO_PAGAMENTI_SPC
	}

	public enum EnumModelloVersamento implements EnumChiaveDescrizione {
		IMMEDIATO("0"), IMMEDIATO_MULTIBENEFICIARIO("1"), DIFFERITO("2"), ATTIVATO_PRESSO_PSP("4");

		private String chiave;
		private String descrizione;

		EnumModelloVersamento(String chiave) {
			this.chiave = chiave;
			this.descrizione = null;
		}
		public String getChiave() {
			return chiave;
		}
		public String getDescrizione() {
			return descrizione;
		}
	}

	/**
	 * Chiave fisica del gateway
	 */
	private long idGateway; 	
	
	/**
	 * Chiave logica mnemonica univoca del gateway
	 */	
	private String bundleKey;		 
	
	/**
	 * Descrizione estesa del gateway 
	 */
	private String descrizione;
	
	/**
	 * Descrizione sintetica
	 */
	private String descrizioneGateway;
	
	/**
	 * EnumStatoOperazione del Gateway, solo se è Attivo può essere utilizzato dai processi di pagamento
	 */
	private EnumStato stato;
	
	/**
	 * Data inizio operatività del gateway
	 */	
	private Date dataInizioValidita;
	
	/**
	 * Data termine operatività del gateway 
	 */		
	private Date dataFineValidita;
	
	/**
	 * Data pubblicazione del gateway  
	 */			
	private Date dataPubblicazione;
	
	/**
	 * Identificativo del PSP che offre il gateway (e.g. codice BIC del prestatore servizio)
	 */				
	private String systemId;
	
	/**
	 * Identificativo dell'applicazione specifica messa a disposizione del PSP (codice canale, modalità)
	 */					
	private String applicationId;
	
	/**
	 * Nome del prestatore servizi di pagamento
	 */						
	private String systemName;
	
	/**
	 * Identificativo del sottosistema / intermediario del gateway
	 */
	private String subSystemId;
	
	/**
	 * Priorità del Gateway tra quelli offerti dal prestatore servizi pagamento
	 */
	private String priorita;
	
	/**
	 * Descrizione testuale degli orari di disponibilità del servizio 
	 */
	private String disponibilitaServizio;
	
	/**
	 * URL messo a disposizione del PSP per le info sul servizio erogato
	 */
	private String urlInformazioniPsp;
	
	/**
	 * URL a disposizione per info addizionali  (per usi futuri)
	 */	
	private String urlInformazioniCanale;
	
	/**
	 * Modalità pagamento utilizzata 
	 */
    private EnumModalitaPagamento modalitaPagamento;

	/**
	 * Strumento pagamento (categoria generica tipo strumento)
	 */
    private EnumStrumentoPagamento strumentoPagamento;
    
	/**
	 * Tipo di bollettino / documento utilizzato per il pagamento
	 */   
    private EnumDocumentoPagamento documentoPagamento;
    
	/**
	 * Canale accesso al sistema di pagamento 
	 */
    private EnumCanalePagamento canalePagamento;
    
	/**
	 * Fornitore, rete, circuito che rende disponibile tecnicamente il gateway di pagamento
	 */    
    private EnumFornitoreGateway fornitoreGateway;

	/**
	 * Modello versamento (tipo interazione: immediata, differita etc.) 
	 */
	private EnumModelloVersamento modelloVersamento;  

	/**
	 * Descrizione testuale delle condizioni applicate 
	 */
	private String importoCommissioneMassima;
	
	/**
	 * Flag che indica se il gateway ammette lo storno dei pagamenti 
	 */
	private boolean stornoGestito;   
	
	
	public long getIdGateway() {
		return idGateway;
	}
	public void setIdGateway(long idGateway) {
		this.idGateway = idGateway;
	}
	public String getBundleKey() {
		return bundleKey;
	}
	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public EnumStato getStato() {
		return stato;
	}
	public void setStato(EnumStato stato) {
		this.stato = stato;
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
	public Date getDataPubblicazione() {
		return dataPubblicazione;
	}
	public void setDataPubblicazione(Date dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSubSystemId() {
		return subSystemId;
	}
	public void setSubSystemId(String subSystemId) {
		this.subSystemId = subSystemId;
	}
	public String getPriorita() {
		return priorita;
	}
	public void setPriorita(String priorita) {
		this.priorita = priorita;
	}
	public String getDisponibilitaServizio() {
		return disponibilitaServizio;
	}
	public void setDisponibilitaServizio(String disponibilitaServizio) {
		this.disponibilitaServizio = disponibilitaServizio;
	}
	public String getUrlInformazioniPsp() {
		return urlInformazioniPsp;
	}
	public void setUrlInformazioniPsp(String urlInformazioniPsp) {
		this.urlInformazioniPsp = urlInformazioniPsp;
	}
	public String getUrlInformazioniCanale() {
		return urlInformazioniCanale;
	}
	public void setUrlInformazioniCanale(String urlInformazioniCanale) {
		this.urlInformazioniCanale = urlInformazioniCanale;
	}
	public EnumModalitaPagamento getModalitaPagamento() {
		return modalitaPagamento;
	}
	public void setModalitaPagamento(EnumModalitaPagamento modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}
	public EnumStrumentoPagamento getStrumentoPagamento() {
		return strumentoPagamento;
	}
	public void setStrumentoPagamento(EnumStrumentoPagamento strumentoPagamento) {
		this.strumentoPagamento = strumentoPagamento;
	}
	public EnumDocumentoPagamento getDocumentoPagamento() {
		return documentoPagamento;
	}
	public void setDocumentoPagamento(EnumDocumentoPagamento documentoPagamento) {
		this.documentoPagamento = documentoPagamento;
	}
	public EnumCanalePagamento getCanalePagamento() {
		return canalePagamento;
	}
	public void setCanalePagamento(EnumCanalePagamento canalePagamento) {
		this.canalePagamento = canalePagamento;
	}
	public EnumFornitoreGateway getFornitoreGateway() {
		return fornitoreGateway;
	}
	public void setFornitoreGateway(EnumFornitoreGateway fornitoreGateway) {
		this.fornitoreGateway = fornitoreGateway;
	}
	public EnumModelloVersamento getModelloVersamento() {
		return modelloVersamento;
	}
	public void setModelloVersamento(EnumModelloVersamento modelloVersamento) {
		this.modelloVersamento = modelloVersamento;
	}
	public String getImportoCommissioneMassima() {
		return importoCommissioneMassima;
	}
	public void setImportoCommissioneMassima(String importoCommissioneMassima) {
		this.importoCommissioneMassima = importoCommissioneMassima;
	}
	public boolean isStornoGestito() {
		return stornoGestito;
	}
	public void setStornoGestito(boolean stornoGestito) {
		this.stornoGestito = stornoGestito;
	}
	public String getDescrizioneGateway() {
		return descrizioneGateway;
	}
	public void setDescrizioneGateway(String descrizioneGateway) {
		this.descrizioneGateway = descrizioneGateway;
	}
	@Override
	public boolean equals(Object obj) {
		// CONTROLLA SOLO LA BUNDLE KEY
		if(obj instanceof GatewayPagamentoModel)
			return this.getBundleKey().equals(((GatewayPagamentoModel) obj).getBundleKey());
		else 
			return super.equals(obj);
	}


}
