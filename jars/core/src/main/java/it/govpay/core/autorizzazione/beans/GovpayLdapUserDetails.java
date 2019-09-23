package it.govpay.core.autorizzazione.beans;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class GovpayLdapUserDetails implements LdapUserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
	private Applicazione applicazione;
	private Operatore operatore;
	private Utenza utenza;
	private LdapUserDetails ldapUserDetailsImpl;
	private String idTransazioneAutenticazione;
	
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
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.ldapUserDetailsImpl.getAuthorities();
	}
	@Override
	public String getPassword() {
		return this.ldapUserDetailsImpl.getPassword();
	}
	@Override
	public String getUsername() {
		return this.ldapUserDetailsImpl.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return this.ldapUserDetailsImpl.isAccountNonExpired();
	}
	@Override
	public boolean isAccountNonLocked() {
		return this.ldapUserDetailsImpl.isAccountNonLocked();
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return this.ldapUserDetailsImpl.isCredentialsNonExpired();
	}
	@Override
	public boolean isEnabled() {
		return this.ldapUserDetailsImpl.isEnabled();
	}
	@Override
	public void eraseCredentials() {
		this.ldapUserDetailsImpl.eraseCredentials();
	}
	@Override
	public String getDn() {
		return this.ldapUserDetailsImpl.getDn();
	}
	
	public LdapUserDetails getLdapUserDetails() {
		return ldapUserDetailsImpl;
	}
	public void setLdapUserDetailsImpl(LdapUserDetails ldapUserDetailsImpl) {
		this.ldapUserDetailsImpl = ldapUserDetailsImpl;
	}
	public boolean isAbilitato() {
		return this.utenza != null ? this.utenza.isAbilitato() : false;
	}
	
	public String getMessaggioUtenzaDisabilitata() {
		return this.utenza != null ? this.utenza.getMessaggioUtenzaDisabilitata() : "Utenza non abilitata";
	}
	
	public String getMessaggioUtenzaNonAutorizzata() {
		return this.utenza != null ? this.utenza.getMessaggioUtenzaNonAutorizzata() : "Utenza non autorizzata ad accedere alla risorsa richiesta";
	}
	public String getIdTransazioneAutenticazione() {
		return idTransazioneAutenticazione;
	}
	public void setIdTransazioneAutenticazione(String idTransazioneAutenticazione) {
		this.idTransazioneAutenticazione = idTransazioneAutenticazione;
	}
}
