/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.core.dao.autorizzazione.AutenticazioneUtenzeAnonimeDAO;

public class AnonymousAuthenticationFilter extends org.springframework.security.web.authentication.AnonymousAuthenticationFilter {

	private static final String AUTH_TYPE_PUBLIC = "PUBLIC";
	private static final String API_NAME_API_PAGAMENTO = "API_PAGAMENTO";
	private static final String USERNAME_ANONYMOUS_USER = "anonymousUser";
	private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	public AnonymousAuthenticationFilter(String key) {
		super(key, getPrincipalUtenzaAnonima(), getAuthoritiesUtenzaAnonima());
	}

	public static Object getPrincipalUtenzaAnonima() {
		List<GrantedAuthority> authFromPreauth = AuthorityUtils.createAuthorityList(ROLE_ANONYMOUS);
		try {
			AutenticazioneUtenzeAnonimeDAO autenticazioneUtenzeAnonimeDAO = new AutenticazioneUtenzeAnonimeDAO();
			autenticazioneUtenzeAnonimeDAO.setApiName(API_NAME_API_PAGAMENTO);
			autenticazioneUtenzeAnonimeDAO.setAuthType(AUTH_TYPE_PUBLIC);
			return autenticazioneUtenzeAnonimeDAO.loadUserDetails(USERNAME_ANONYMOUS_USER, authFromPreauth);
		} catch (UsernameNotFoundException e) {
			//donothing
		}
		return USERNAME_ANONYMOUS_USER;
	}

	public static List<GrantedAuthority> getAuthoritiesUtenzaAnonima() {
		List<GrantedAuthority> authFromPreauth = AuthorityUtils.createAuthorityList(ROLE_ANONYMOUS);
		UserDetails utenzaAnonima = null;
		try {
			AutenticazioneUtenzeAnonimeDAO autenticazioneUtenzeAnonimeDAO = new AutenticazioneUtenzeAnonimeDAO();
			autenticazioneUtenzeAnonimeDAO.setApiName(API_NAME_API_PAGAMENTO);
			autenticazioneUtenzeAnonimeDAO.setAuthType(AUTH_TYPE_PUBLIC);
			utenzaAnonima = autenticazioneUtenzeAnonimeDAO.loadUserDetails(USERNAME_ANONYMOUS_USER, authFromPreauth);
		} catch (UsernameNotFoundException e) {
			//donothing
		}

		if(utenzaAnonima != null) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.addAll(utenzaAnonima.getAuthorities());
			return authorities;
		}

		return AuthorityUtils.createAuthorityList(ROLE_ANONYMOUS);
	}
}

