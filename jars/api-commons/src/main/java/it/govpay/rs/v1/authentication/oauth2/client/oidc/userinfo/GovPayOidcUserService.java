/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.authentication.oauth2.client.oidc.userinfo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

import it.govpay.core.autorizzazione.beans.GovpayLdapOidcOauth2Details;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.autorizzazione.BaseAutenticazioneDAO;

/**
 * La classe estende le funzionalita' della classe {@link OidcUserService} per caricare le informazioni relative all'utenza GovPay all'interno dell'oggetto {@link OidcUser}
 * 
 * @author pintori@link.it
 *
 */
public class GovPayOidcUserService extends OidcUserService {

	private BaseAutenticazioneDAO userDetailService;
	
	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser loadUserFromSuperClass = super.loadUser(userRequest);
		Collection<? extends GrantedAuthority> authoritiesFromSuperClass = loadUserFromSuperClass.getAuthorities();
		OidcUserInfo userInfoFromSuperClass = loadUserFromSuperClass.getUserInfo();
		
		ProviderDetails providerDetails = userRequest.getClientRegistration().getProviderDetails();
		String userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();
		GovpayLdapOidcOauth2Details govpayLdapOidcOauth2Details = null;
		
		if (StringUtils.hasText(userNameAttributeName)) {
			govpayLdapOidcOauth2Details = new GovpayLdapOidcOauth2Details(authoritiesFromSuperClass, userRequest.getIdToken(), userInfoFromSuperClass, userNameAttributeName);
		} else {
			govpayLdapOidcOauth2Details = new GovpayLdapOidcOauth2Details(authoritiesFromSuperClass, userRequest.getIdToken(), userInfoFromSuperClass);
		}
	
		// creo un utenza ldap fittizzia per caricare le informazioni utente
		LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setAccountNonExpired(true);
		essence.setAccountNonLocked(true);
		essence.setCredentialsNonExpired(true);
		essence.setEnabled(true);
		essence.setUsername(govpayLdapOidcOauth2Details.getName());
		essence.setPassword(AutorizzazioneUtils.PASSWORD_DEFAULT_VALUE);
		essence.setAuthorities(authoritiesFromSuperClass);
		essence.setDn(govpayLdapOidcOauth2Details.getName());
		
		LdapUserDetails createUserDetails = essence.createUserDetails();
		govpayLdapOidcOauth2Details.setLdapUserDetailsImpl(createUserDetails);
		
		// leggo le informazioni sull'utenza nel formato GovPay
		GovpayLdapUserDetails details = new GovpayLdapUserDetails();
		details.setLdapUserDetailsImpl(createUserDetails);
		UserDetails loadUserByLdapUserDetail = this.userDetailService.loadUserByLdapUserDetail(govpayLdapOidcOauth2Details.getUsername(), details);
		if(loadUserByLdapUserDetail instanceof GovpayLdapUserDetails) {
			GovpayLdapUserDetails govpayDetails = (GovpayLdapUserDetails) loadUserByLdapUserDetail;
			govpayLdapOidcOauth2Details.setUtenza(govpayDetails.getUtenza());
			govpayLdapOidcOauth2Details.setApplicazione(govpayDetails.getApplicazione());
			govpayLdapOidcOauth2Details.setOperatore(govpayDetails.getOperatore());
			govpayLdapOidcOauth2Details.setIdTransazioneAutenticazione(govpayDetails.getIdTransazioneAutenticazione());
			govpayLdapOidcOauth2Details.setTipoUtenza(govpayDetails.getTipoUtenza());
		}
		
		return govpayLdapOidcOauth2Details;
	}

	public BaseAutenticazioneDAO getUserDetailService() {
		return userDetailService;
	}

	public void setUserDetailService(BaseAutenticazioneDAO userDetailService) {
		this.userDetailService = userDetailService;
	}

}
