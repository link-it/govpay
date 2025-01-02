/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.core.autorizzazione.beans;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.oauth2.jwt.Jwt;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class GovPayLdapJwt extends Jwt implements LdapUserDetails {
	
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
	private Applicazione applicazione;
	private Operatore operatore;
	private Utenza utenza;
	private LdapUserDetails ldapUserDetailsImpl;
	private String idTransazioneAutenticazione;

	public GovPayLdapJwt(String tokenValue, Instant issuedAt, Instant expiresAt, Map<String, Object> headers, Map<String, Object> claims) {
		super(tokenValue, issuedAt, expiresAt, headers, claims);
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

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
		return this.utenza != null && this.utenza.isAbilitato();
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

	public GovpayLdapUserDetails toGovpayLdapUserDetails() {
		GovpayLdapUserDetails details = new GovpayLdapUserDetails();

		LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setAccountNonExpired(this.isAccountNonExpired());
		essence.setAccountNonLocked(this.isAccountNonLocked());
		essence.setCredentialsNonExpired(this.isCredentialsNonExpired());
		essence.setEnabled(this.isEnabled());
		essence.setUsername(this.getUsername());
		essence.setPassword(this.getPassword());
		essence.setAuthorities(this.getAuthorities());
		essence.setDn(this.getIdentificativo());

		details.setLdapUserDetailsImpl(essence.createUserDetails());
		
		details.setUtenza(this.getUtenza());
		details.setApplicazione(this.getApplicazione());
		details.setOperatore(this.getOperatore());
		details.setIdTransazioneAutenticazione(this.getIdTransazioneAutenticazione());
		details.setTipoUtenza(this.getTipoUtenza());

		return details;
	}

}
