package it.govpay.core.autorizzazione.beans;

import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class GovpayLdapUserDetails extends LdapUserDetailsImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
	private Applicazione applicazione;
	private Operatore operatore;
	private Utenza utenza;
	
	public String getIdentificativo() {
		return this.utenza != null ? this.utenza.getIdentificativo() : this.getDn();
	}
	public TIPO_UTENZA getTipoUtenza() {
		return tipoUtenza;
	}
	public void setTipoUtenza(TIPO_UTENZA tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}
	public Applicazione getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public Utenza getUtenza() {
		return utenza;
	}
	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	
}
