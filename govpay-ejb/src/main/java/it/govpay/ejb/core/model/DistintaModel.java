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

import it.govpay.ejb.core.utils.EnumChiaveDescrizione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mapping in NodoDeiPagamenti
 *  
 * codPagamento > IUV
 * codTransazione > idMessaggioRichiesta
 * codTransazionePSP > CCP
 */



/**
 * Rappresenta una distinta di pagamento, ovvero una transazione  
 * di pagamento che incude in modo atomico il pagamento di più posizioni.
 * In NDP coincide con una RPT.
 * 
 * @author RepettiS
 *
 */
public class DistintaModel {
	
	
	/**
	 * EnumStatoOperazione della distinta
	 */
	public enum EnumStatoDistinta implements EnumChiaveDescrizione {
		
		IN_CORSO("IN CORSO", "In corso"), 
		IN_ERRORE("IN ERRORE", "In errore"),
		ESEGUITO("ESEGUITO", "Eseguito"),
		ESEGUITO_SBF("ESEGUITO SBF", "Eseguito salvo buon fine"),
		PARZIALMENTE_ESEGUITO("PARZ. ESEGUITO", "Parzialmente eseguito"),
		NON_ESEGUITO("NON ESEGUITO", "Non eseguito"),
		STORNATO("STORNATO", "Stornato"),
		ANNULLATO("ANNULLATO", "Annullato"),
		ANNULLATO_OPE("ANNULLATO OPE", "Annullato operatore");

		private String chiave;
		private String descrizione;

		EnumStatoDistinta(String chiave, String descrizione) {
			this.chiave = chiave;
			this.descrizione = descrizione;
		}

		public String getChiave() {
			return chiave;
		}
		public String getDescrizione() {
			return descrizione;
		}

	}
	
	
	/**
	 * Tipo autenticazione del soggetto versante.
	 */
	public enum EnumTipoAutenticazioneSoggetto {
	    CNS,
	    USR,
	    OTH,
	    N_A;
	}
	
	public enum EnumTipoFirma {
	    CA_DES,
	    XA_DES,
	    AVANZATA,
	    NESSUNA;
	}
	
	/**
	 * Id Fisico della distinta su database
	 */
	private Long idDistinta;	
		
	/**
	 * Lista dei singoli pagamenti associati a questa distinta.
	 */
	private List<PagamentoModel> pagamenti;	
	
	/**
	 * Associazione con il Gateway pagamento a cui è indirizzata la distinta
	 */
	private Long idGatewayPagamento;	
	
	/**
	 * Codice Transazione associato all'intera distinta, noto e condiviso con il gatteway
	 * di pagamento e riferito alle configurazioni. In NDP coincide con RPT.idMessaggioRichiesta.
	 */
	private String codTransazione;
	
	/**
	 * Codice Transazione assegnato alla distinta dal PSP  fornito dal gateway di pagamento.
	 * In NDP coincide con RPT.codiceContestoPagamento.
	 */
	private String codTransazionePsp;	
	
	/**
	 * Importo Totale della distinta. Somma degli importi dei singoli pagamenti in essa contenuti.
	 */
	private BigDecimal importoTotale;
	
	/**
	 * Dati del soggetto che effettua il pagamento. 
	 */	
	private VersanteModel soggettoVersante;
	
	/**
	 * Iban di addebito da utilizzare per i metodi di pagamento che prevedono
	 * addebito diretto del conto corrente del versante
	 */
	private String ibanAddebito;
	
	/**
	 * Tipo di autenticazione del soggetto versante 
	 */	
	private EnumTipoAutenticazioneSoggetto autenticazione;
	
	/**
	 * Tipo firma applicata al messaggio che veicola la distinta al gateway di pagamento
	 * 
	 */
	private EnumTipoFirma firma;
	
	/**
	 * Timestamp di richiesta del processo di pagamento
	 */
	private Date dataOraRichiesta;
	
	
	/**
	 * Email del soggetto versante al quale inviare la notifica di pagamento avvenuto.
	 */
	private String emailNotifiche;
	
	/**
	 * EnumStatoOperazione della distinta, rappresenta lo stato del processo di pagamento
	 */
	private EnumStatoDistinta stato;
	
	/**
	 * Identificativo Univoco Versamento (dato NDP)
	 */
	private String iuv;

	/**
	 * Identificativo Fiscale Creditore
	 */	
	private String identificativoFiscaleCreditore; 

	/**
	 * Identificativo che raggruppa più distinte in un singolo carrello.
	 */	
	private String idGruppo;
		
	
	/** Estrae la lista degli id delle condizioni di pagamento
	 *  collegate ai pagamenti della distinta
	 */
	public List<String> extractIdCondizioni() {
		List<String> idCondizioni = new ArrayList<String>();
		for (PagamentoModel pm:pagamenti) {
			idCondizioni.add(pm.getIdCondizionePagamento());
		}
		return idCondizioni;
	}

	
	public VersanteModel getSoggettoVersante() {
		return soggettoVersante;
	}
	public void setSoggettoVersante(VersanteModel soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}
	public String getCodTransazione() {
		return codTransazione;
	}
	public void setCodTransazione(String codTransazione) {
		this.codTransazione = codTransazione;
	}
	public String getCodTransazionePsp() {
		return codTransazionePsp;
	}
	public void setCodTransazionePsp(String codTransazionePsp) {
		this.codTransazionePsp = codTransazionePsp;
	}
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}
	
	public Long getIdDistinta() {
		return idDistinta;
	}
	public void setIdDistinta(Long idDistinta) {
		this.idDistinta = idDistinta;
	}	
	public List<PagamentoModel> getPagamenti() {
		return pagamenti;
	}
	public void setPagamenti(List<PagamentoModel> pagamenti) {
		this.pagamenti = pagamenti;
	}
	public Long getIdGatewayPagamento() {
		return idGatewayPagamento;
	}
	public void setIdGatewayPagamento(Long idGatewayPagamento) {
		this.idGatewayPagamento = idGatewayPagamento;
	}
	public String getIbanAddebito() {
		return ibanAddebito;
	}
	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}
	public EnumTipoAutenticazioneSoggetto getAutenticazione() {
		return autenticazione;
	}
	public void setAutenticazione(EnumTipoAutenticazioneSoggetto autenticazione) {
		this.autenticazione = autenticazione;
	}
	public EnumTipoFirma getFirma() {
		return firma;
	}
	public void setFirma(EnumTipoFirma firma) {
		this.firma = firma;
	}
	public String getEmailNotifiche() {
		return emailNotifiche;
	}
	public void setEmailNotifiche(String emailNotifiche) {
		this.emailNotifiche = emailNotifiche;
	}
	public EnumStatoDistinta getStato() {
		return stato;
	}
	public void setStato(EnumStatoDistinta stato) {
		this.stato = stato;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIdGruppo() {
		return idGruppo;
	}
	public void setIdGruppo(String idGruppo) {
		this.idGruppo = idGruppo;
	}
	public String getIdentificativoFiscaleCreditore() {
		return identificativoFiscaleCreditore;
	}
	public void setIdentificativoFiscaleCreditore(String identificativoCreditore) {
		this.identificativoFiscaleCreditore = identificativoCreditore;
	}
	public Date getDataOraRichiesta() {
		return dataOraRichiesta;
	}
	public void setDataOraRichiesta(Date dataOraRichiesta) {
		this.dataOraRichiesta = dataOraRichiesta;
	}
	
	
}
