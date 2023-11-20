package it.govpay.core.autorizzazione.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.model.Utenza.TIPO_UTENZA;

/***
 * Questa classe estende la {@link DefaultOAuth2User} per includere le formazioni sull'utenza GovPay
 * 
 * @author pintori@link.it
 *
 */
public class GovpayLdapOauth2Details extends DefaultOAuth2User implements LdapUserDetails{


	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
	private Applicazione applicazione;
	private Operatore operatore;
	private Utenza utenza;
	private LdapUserDetails ldapUserDetailsImpl;
	private String idTransazioneAutenticazione;
	
	/**
	 * Constructs a {@code DefaultOAuth2User} using the provided parameters.
	 * @param authorities the authorities granted to the user
	 * @param attributes the attributes about the user
	 * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
	 * {@link #getAttributes()}
	 */
	public GovpayLdapOauth2Details(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
		super(authorities, attributes, nameAttributeKey);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
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
		Collection<? extends GrantedAuthority> authorities2 = (this.ldapUserDetailsImpl.getAuthorities() != null)
				? Collections.unmodifiableSet(new LinkedHashSet<>(this.ldapUserDetailsImpl.getAuthorities()))
				: Collections.unmodifiableSet(new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES)); 
				this.ldapUserDetailsImpl.getAuthorities();
		
		Set<GrantedAuthority> authoritiesTmp = new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES);
				
		if(!super.getAuthorities().isEmpty()) {
			authoritiesTmp.addAll(super.getAuthorities());
		}
		
		if(!authorities2.isEmpty()) {
			authoritiesTmp.addAll(authorities2);
		}
		
		return Collections.unmodifiableSet(new LinkedHashSet<>(authoritiesTmp));
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
