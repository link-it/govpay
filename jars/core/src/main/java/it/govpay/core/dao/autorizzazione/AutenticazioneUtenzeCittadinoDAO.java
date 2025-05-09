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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;

public class AutenticazioneUtenzeCittadinoDAO extends BaseAutenticazioneDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	public AutenticazioneUtenzeCittadinoDAO() {
		super();
	}

	public AutenticazioneUtenzeCittadinoDAO(boolean useCacheData) {
		super(useCacheData);
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();
		
		if(this.leggiUtenzaDaSessione) {
			Map<String, Object> attributeValues = new HashMap<>();
			if(token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				attributeValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getAttributesValues();
			}
			return this.loadUserDetailsFromSessionEngine(username, token.getAuthorities(),attributeValues );
		} else {
			Map<String, List<String>> headerValues = new HashMap<>();
			if(token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
			}
			return this.loadUserDetailsEngine(username, token.getAuthorities(),headerValues);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.loadUserDetailsEngine(username, null,null);
	}


	private UserDetails loadUserDetailsEngine(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) {
		if(headerValues == null) {
			headerValues = new HashMap<>();
		}
		
		try {
			String transactionId = UUID.randomUUID().toString();
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaCittadino(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth,headerValues, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] completato.");
			return userDetailFromUtenzaCittadino;
		} catch(ServiceException e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			// donothing
		}
	}
	
	private UserDetails loadUserDetailsFromSessionEngine(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, Object> attributeValues) {
		if(attributeValues == null) {
			attributeValues = new HashMap<>();
		}
		
		try {
			GovpayLdapUserDetails userDetailFromSession = (GovpayLdapUserDetails) attributeValues.get(AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME);
			if(userDetailFromSession == null)
				throw new RuntimeException("Dati utenza ["+username+"] non presenti in sessione.");
			
			String transactionId = UUID.randomUUID().toString();
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetailFromSession, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] completato.");
			return userDetailFromUtenzaCittadino;
		} catch(ServiceException e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			// donothing
		}
	}
}
