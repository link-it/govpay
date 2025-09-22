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

import java.text.MessageFormat;
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
import it.govpay.core.dao.commons.exception.UtenzaNonAutorizzataException;
import it.govpay.core.utils.LogUtils;

public class AutenticazioneUtenzeRegistrateDAO extends BaseAutenticazioneDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	private static final String LOG_MSG_UTENZA_0_NON_TROVATA = "Utenza [{0}] non trovata.";

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
			if(token.getDetails() instanceof GovpayWebAuthenticationDetails govpayWebAuthenticationDetails) {
				attributeValues = govpayWebAuthenticationDetails.getAttributesValues();
			}
			try {
				return this.loadUserDetailsFromSessionEngine(username, token.getAuthorities(),attributeValues );
			}catch(it.govpay.core.exceptions.NotFoundException e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		} else {
			Map<String, List<String>> headerValues = new HashMap<>();
			if(token.getDetails() instanceof GovpayWebAuthenticationDetails govpayWebAuthenticationDetails) {
				headerValues = govpayWebAuthenticationDetails.getHeaderValues();
			}
			try {
				return this.loadUserDetailsEngine(username, token.getAuthorities(), headerValues);
			}catch(it.govpay.core.exceptions.NotFoundException e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return this.loadUserDetailsEngine(username, null, null);
		}catch(it.govpay.core.exceptions.NotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public UserDetails loadUserByLdapUserDetail(String username, GovpayLdapUserDetails userDetail) throws UsernameNotFoundException {
		try {
			return this.loadUserDetailsFromLdapUserDetailEngine(username, userDetail.getAuthorities(), userDetail);
		}catch(it.govpay.core.exceptions.NotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}


	private UserDetails loadUserDetailsEngine(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) throws it.govpay.core.exceptions.NotFoundException {
		String transactionId = UUID.randomUUID().toString();
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,MessageFormat.format("Lettura delle informazioni per l''utenza [{0}] in corso...", username));

			try {
				if(this.isCheckSubject()) {
					AnagraficaManager.getUtenzaBySubject(configWrapper, username);
				} else {
					AnagraficaManager.getUtenza(configWrapper, username);
				}
			}  catch(NotFoundException e){
				if(!this.isUserAutoSignup()) {				
					throw new it.govpay.core.exceptions.NotFoundException(MessageFormat.format("Utenza {0} non trovata.", username),e);
				}

				// autocensimento utenza
				this.debug(transactionId,MessageFormat.format("Autocensimento Utenza [{0}]...", username));
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

			this.debug(transactionId,MessageFormat.format("Utenza [{0}] trovata, lettura del dettaglio in corso...", username));
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrata(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, headerValues, configWrapper, this.getApiName(), this.getAuthType());
			userDetails.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,MessageFormat.format("Utenza [{0}] trovata, lettura del dettaglio completata.", username));
			return userDetails;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,MessageFormat.format(LOG_MSG_UTENZA_0_NON_TROVATA, username));
			throw e;
		} catch(ServiceException e){
			LogUtils.logError(log, "Errore Interno: " +e.getMessage(),e);
			throw new UtenzaNonAutorizzataException("Errore interno, impossibile autenticare l'utenza", e);
		}	finally {
			// donothing
		}
	}

	private UserDetails loadUserDetailsFromSessionEngine(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, Object> attributeValues) throws it.govpay.core.exceptions.NotFoundException {
		if(attributeValues == null) {
			attributeValues = new HashMap<>();
		}
		String transactionId = UUID.randomUUID().toString();

		try {
			GovpayLdapUserDetails userDetailFromSession = (GovpayLdapUserDetails) attributeValues.get(AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME);
			if(userDetailFromSession == null)
				throw new UtenzaNonAutorizzataException(MessageFormat.format("Dati utenza [{0}] non presenti in sessione.", username));

			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,MessageFormat.format("Lettura delle informazioni per l''utenza [{0}] in corso...", username));
			UtenzeBD utenzeBD = new UtenzeBD(configWrapper);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new it.govpay.core.exceptions.NotFoundException("Utenza "+username+" non trovata.");

			this.debug(transactionId,MessageFormat.format("Utenza [{0}] trovata, lettura del dettaglio in corso...", username));
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrataInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetailFromSession, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,MessageFormat.format("Utenza [{0}] trovata, lettura del dettaglio completata.", username));
			return userDetailFromUtenzaCittadino;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,MessageFormat.format(LOG_MSG_UTENZA_0_NON_TROVATA, username));
			throw e;
		} catch(ServiceException e){
			throw new UtenzaNonAutorizzataException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			// donothing
		}
	}

	private UserDetails loadUserDetailsFromLdapUserDetailEngine(String username, Collection<? extends GrantedAuthority> authFromPreauth, GovpayLdapUserDetails userDetail) throws it.govpay.core.exceptions.NotFoundException {
		Map<String, Object> attributeValues = new HashMap<>();

		String transactionId = UUID.randomUUID().toString();
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId,MessageFormat.format("Lettura delle informazioni per l''utenza ldap [{0}] in corso...", username));
			UtenzeBD utenzeBD = new UtenzeBD(configWrapper);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new it.govpay.core.exceptions.NotFoundException(MessageFormat.format("Utenza ldap {0} non trovata.", username));

			this.debug(transactionId,MessageFormat.format("Utenza ldap [{0}] trovata, lettura del dettaglio in corso...", username));
			GovpayLdapUserDetails userDetailFromUtenzaLdap = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrataInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetail, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaLdap.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,MessageFormat.format("Utenza ldap [{0}] trovata, lettura del dettaglio completata.", username));
			return userDetailFromUtenzaLdap;
		} catch(it.govpay.core.exceptions.NotFoundException e){
			this.debug(transactionId,MessageFormat.format(LOG_MSG_UTENZA_0_NON_TROVATA, username));
			throw e;
		} catch(Exception e){
			throw new UtenzaNonAutorizzataException("Errore interno, impossibile caricare le informazioni dell'utenza ldap ["+username+"]: ", e);
		}	finally {
			// donothing
		}
	}
}
