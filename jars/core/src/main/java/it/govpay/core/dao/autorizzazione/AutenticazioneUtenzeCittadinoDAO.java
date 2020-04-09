package it.govpay.core.dao.autorizzazione;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BasicBD;
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
			if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				attributeValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getAttributesValues();
			}
			UserDetails user = this._loadUserDetailsFromSession(username, token.getAuthorities(),attributeValues );
			return user;
		} else {
			Map<String, List<String>> headerValues = new HashMap<>();
			if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
			}
			UserDetails user = this._loadUserDetails(username, token.getAuthorities(),headerValues);
			return user;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this._loadUserDetails(username, null,null);
		return user;
	}


	private UserDetails _loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) throws UsernameNotFoundException {
		BasicBD bd = null;

		if(headerValues == null) {
			headerValues = new HashMap<>();
		}
		
		try {
			String transactionId = UUID.randomUUID().toString();
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] in corso...");
			bd = BasicBD.newInstance(transactionId, this.useCacheData); 
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaCittadino(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth,headerValues, bd);
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] completato.");
			return userDetailFromUtenzaCittadino;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	private UserDetails _loadUserDetailsFromSession(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, Object> attributeValues) throws UsernameNotFoundException {
		BasicBD bd = null;

		if(attributeValues == null) {
			attributeValues = new HashMap<>();
		}
		
		try {
			GovpayLdapUserDetails userDetailFromSession = (GovpayLdapUserDetails) attributeValues.get(AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME);
			if(userDetailFromSession == null)
				throw new Exception("Dati utenza non presenti in sessione.");
			
			String transactionId = UUID.randomUUID().toString();
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] in corso...");
			bd = BasicBD.newInstance(transactionId, this.useCacheData); 
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetailFromSession, bd);
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Caricamento informazioni del cittadino ["+username+"] completato.");
			return userDetailFromUtenzaCittadino;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
