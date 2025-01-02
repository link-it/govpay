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
package it.govpay.core.dao.autorizzazione;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;

public class AutenticazioneUtenzeAnonimeDAO extends BaseAutenticazioneDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication>, AuthenticationDetailsSource<HttpServletRequest, Authentication> {	

	public AutenticazioneUtenzeAnonimeDAO() {
		super();
	}

	public AutenticazioneUtenzeAnonimeDAO(boolean useCacheData) {
		super(useCacheData);
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();
		return this.loadUserDetails(username, token.getAuthorities());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.loadUserDetails(username, null);
	}

	@Override
	public UserDetails loadUserByLdapUserDetail(String username, GovpayLdapUserDetails userDetail) throws UsernameNotFoundException {
		return this._loadUserDetailsFromLdapUserDetail(username, userDetail.getAuthorities(), userDetail);
	}

	public UserDetails loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth) {
		try {
			String transactionId = UUID.randomUUID().toString();
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId, "Caricamento informazioni dell'utenza ["+username+"] in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaAnonima = AutorizzazioneUtils.getUserDetailFromUtenzaAnonima(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaAnonima.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId, "Caricamento informazioni dell'utenza ["+username+"] completato.");
			return userDetailFromUtenzaAnonima;
		} catch(ServiceException e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni dell'utenza", e);
		}	finally {
			// donothing
		}
	}
	
	private UserDetails _loadUserDetailsFromLdapUserDetail(String username, Collection<? extends GrantedAuthority> authFromPreauth, GovpayLdapUserDetails userDetail) throws UsernameNotFoundException {
		Map<String, Object> attributeValues = new HashMap<>();
		
		try {
			String transactionId = UUID.randomUUID().toString();
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ldap ["+username+"] in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaLdap = AutorizzazioneUtils.getUserDetailFromUtenzaAnonimaInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetail, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaLdap.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId, "Caricamento informazioni dell'utenza ldap ["+username+"] completato.");
			return userDetailFromUtenzaLdap;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni dell'utenza ldap ["+username+"]: ", e);
		}	finally {
			// donothing
		}
	}
	
	@Override
	public Authentication buildDetails(HttpServletRequest context) {
		return null;
	}
}
