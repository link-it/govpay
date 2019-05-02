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
import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.commons.BaseDAO;

public class AutenticazioneUtenzeCittadinoDAO extends BaseDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	private boolean checkSubject = false;
	private boolean checkPassword = false;
	
	public AutenticazioneUtenzeCittadinoDAO() {
		super();
	}

	public AutenticazioneUtenzeCittadinoDAO(boolean useCacheData) {
		super(useCacheData);
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();
		
		Map<String, List<String>> headerValues = new HashMap<>();
		if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
			headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
		}
		UserDetails user = this._loadUserDetails(username, token.getAuthorities(),headerValues);
		return user;
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
			this.log.debug("Caricamento informazioni del cittadino ["+username+"] in corso...");
			String transactionId = UUID.randomUUID().toString();
			bd = BasicBD.newInstance(transactionId, this.useCacheData); 
			UserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaCittadino(username, this.checkPassword, this.checkSubject, authFromPreauth,headerValues, bd);
			this.log.debug("Caricamento informazioni del cittadino ["+username+"] completato.");
			return userDetailFromUtenzaCittadino;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public boolean isCheckSubject() {
		return this.checkSubject;
	}

	public void setCheckSubject(boolean checkSubject) {
		this.checkSubject = checkSubject;
	}
	
	public boolean isCheckPassword() {
		return checkPassword;
	}

	public void setCheckPassword(boolean checkPassword) {
		this.checkPassword = checkPassword;
	}
}
