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
package it.govpay.rs.v1.authentication.oauth2.client.userinfo;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import it.govpay.core.autorizzazione.beans.GovpayLdapOauth2Details;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.autorizzazione.BaseAutenticazioneDAO;


/**
 * La classe estende le funzionalita' della classe {@link DefaultOAuth2UserService} per caricare le informazioni relative all'utenza GovPay all'interno dell'oggetto {@link OAuth2User}
 *
 * @author pintori@link.it
 *
 */
public class GovPayOAuth2UserService extends DefaultOAuth2UserService {

	private BaseAutenticazioneDAO userDetailService;

	public GovPayOAuth2UserService() {
		super();
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User loadUserFromSuperClass = super.loadUser(userRequest);
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();
		Collection<? extends GrantedAuthority> authoritiesFromSuperClass = loadUserFromSuperClass.getAuthorities();
		Map<String, Object> attributesFromSuperClass = loadUserFromSuperClass.getAttributes();
		GovpayLdapOauth2Details govpayLdapOauth2Details = new GovpayLdapOauth2Details(authoritiesFromSuperClass, attributesFromSuperClass, userNameAttributeName);

		// creo un utenza ldap fittizzia per caricare le informazioni utente
		LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setAccountNonExpired(true);
		essence.setAccountNonLocked(true);
		essence.setCredentialsNonExpired(true);
		essence.setEnabled(true);
		essence.setUsername(govpayLdapOauth2Details.getName());
		essence.setPassword(AutorizzazioneUtils.generaPasswordUtenza());
		essence.setAuthorities(authoritiesFromSuperClass);
		essence.setDn(govpayLdapOauth2Details.getName());

		LdapUserDetails createUserDetails = essence.createUserDetails();
		govpayLdapOauth2Details.setLdapUserDetailsImpl(createUserDetails);

		// leggo le informazioni sull'utenza nel formato GovPay
		GovpayLdapUserDetails details = new GovpayLdapUserDetails();
		details.setLdapUserDetailsImpl(createUserDetails);
		UserDetails loadUserByLdapUserDetail = this.userDetailService.loadUserByLdapUserDetail(govpayLdapOauth2Details.getUsername(), details);
		if(loadUserByLdapUserDetail instanceof GovpayLdapUserDetails) {
			GovpayLdapUserDetails govpayDetails = (GovpayLdapUserDetails) loadUserByLdapUserDetail;
			govpayLdapOauth2Details.setUtenza(govpayDetails.getUtenza());
			govpayLdapOauth2Details.setApplicazione(govpayDetails.getApplicazione());
			govpayLdapOauth2Details.setOperatore(govpayDetails.getOperatore());
			govpayLdapOauth2Details.setIdTransazioneAutenticazione(govpayDetails.getIdTransazioneAutenticazione());
			govpayLdapOauth2Details.setTipoUtenza(govpayDetails.getTipoUtenza());
		}

		return govpayLdapOauth2Details;
	}

	public BaseAutenticazioneDAO getUserDetailService() {
		return userDetailService;
	}

	public void setUserDetailService(BaseAutenticazioneDAO userDetailService) {
		this.userDetailService = userDetailService;
	}
}
