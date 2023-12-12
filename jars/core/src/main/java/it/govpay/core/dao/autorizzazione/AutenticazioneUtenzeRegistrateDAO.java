/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;

public class AutenticazioneUtenzeRegistrateDAO extends BaseAutenticazioneDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	public AutenticazioneUtenzeRegistrateDAO() {
		super();
	}

	public AutenticazioneUtenzeRegistrateDAO(boolean useCacheData) {
		super(useCacheData);
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();

		if(this.leggiUtenzaDaSessione) {
			Map<String, Object> attributeValues = new HashMap<>();
			if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				attributeValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getAttributesValues();
			}
			try {
				return this._loadUserDetailsFromSession(username, token.getAuthorities(),attributeValues );
			}catch(it.govpay.core.exceptions.NotFoundException e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		} else {
			Map<String, List<String>> headerValues = new HashMap<>();
			if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
			}
			try {
				return this._loadUserDetails(username, token.getAuthorities(), headerValues);
			}catch(it.govpay.core.exceptions.NotFoundException e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return this._loadUserDetails(username, null, null);
		}catch(it.govpay.core.exceptions.NotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public UserDetails loadUserByLdapUserDetail(String username, GovpayLdapUserDetails userDetail) throws UsernameNotFoundException {
		try {
			return this._loadUserDetailsFromLdapUserDetail(username, userDetail.getAuthorities(), userDetail);
		}catch(it.govpay.core.exceptions.NotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}


	private UserDetails _loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) throws it.govpay.core.exceptions.NotFoundException {
		String transactionId = UUID.randomUUID().toString();
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ["+username+"] in corso...");

			try {
				if(this.isCheckSubject()) {
					AnagraficaManager.getUtenzaBySubject(configWrapper, username);
				} else {
					AnagraficaManager.getUtenza(configWrapper, username);
				}
			}  catch(NotFoundException e){
				if(!this.isUserAutoSignup()) {				
					throw new it.govpay.core.exceptions.NotFoundException("Utenza "+username+" non trovata.",e);
				}

				// autocensimento utenza
				this.debug(transactionId,"Autocensimento Utenza ["+username+"]...");
				OperatoriBD operatoriBD = new OperatoriBD(configWrapper);
				Operatore operatore = new Operatore();
				String nome = username.length() > 35 ? username.substring(0, 35) : username;
				operatore.setNome(nome );
				Utenza utenza = new Utenza();
				utenza.setAbilitato(true);
				if(this.getUserAutoSignupDefaultRole() != null) {
					List<String> ruoli = new ArrayList<>();
					ruoli.add(this.getUserAutoSignupDefaultRole());
					utenza.setRuoli(ruoli);
				}
				utenza.setPrincipal(username);
				utenza.setPrincipalOriginale(username);
				utenza.setAutorizzazioneDominiStar(true);
				utenza.setAutorizzazioneTipiVersamentoStar(true);
				operatore.setUtenza(utenza);
				operatoriBD.insertOperatore(operatore);

				this.debug(transactionId,"Autocensimento Utenza ["+username+"] completato.");
				// reset della cache
				it.govpay.core.business.Operazioni.resetCacheAnagrafica(configWrapper);

			}

			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio in corso...");
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrata(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, headerValues, configWrapper, this.getApiName(), this.getAuthType());
			userDetails.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio completata.");
			return userDetails;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,"Utenza ["+username+"] non trovata.");
			throw e;
		} catch(ServiceException e){
			log.error("Errore Interno: " +e.getMessage(),e);
			throw new RuntimeException("Errore interno, impossibile autenticare l'utenza", e);
		}	finally {
		}
	}

	private UserDetails _loadUserDetailsFromSession(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, Object> attributeValues) throws it.govpay.core.exceptions.NotFoundException {
		if(attributeValues == null) {
			attributeValues = new HashMap<>();
		}
		String transactionId = UUID.randomUUID().toString();

		try {
			GovpayLdapUserDetails userDetailFromSession = (GovpayLdapUserDetails) attributeValues.get(AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME);
			if(userDetailFromSession == null)
				throw new RuntimeException("Dati utenza ["+username+"] non presenti in sessione.");

			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ["+username+"] in corso...");
			UtenzeBD utenzeBD = new UtenzeBD(configWrapper);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new it.govpay.core.exceptions.NotFoundException("Utenza "+username+" non trovata.");

			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrataInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetailFromSession, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio completata.");
			return userDetailFromUtenzaCittadino;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,"Utenza ["+username+"] non trovata.");
			throw e;
		} catch(ServiceException e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
		}
	}

	private UserDetails _loadUserDetailsFromLdapUserDetail(String username, Collection<? extends GrantedAuthority> authFromPreauth, GovpayLdapUserDetails userDetail) throws it.govpay.core.exceptions.NotFoundException {
		Map<String, Object> attributeValues = new HashMap<>();

		String transactionId = UUID.randomUUID().toString();
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ldap ["+username+"] in corso...");
			UtenzeBD utenzeBD = new UtenzeBD(configWrapper);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new it.govpay.core.exceptions.NotFoundException("Utenza ldap "+username+" non trovata.");

			this.debug(transactionId,"Utenza ldap ["+username+"] trovata, lettura del dettaglio in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaLdap = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrataInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetail, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaLdap.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Utenza ldap ["+username+"] trovata, lettura del dettaglio completata.");
			return userDetailFromUtenzaLdap;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,"Utenza ["+username+"] non trovata.");
			throw e;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni dell'utenza ldap ["+username+"]: ", e);
		}	finally {
		}
	}
}
