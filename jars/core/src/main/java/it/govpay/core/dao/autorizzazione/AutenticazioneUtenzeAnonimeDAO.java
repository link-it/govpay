package it.govpay.core.dao.autorizzazione;

import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
		UserDetails user = this.loadUserDetails(username, token.getAuthorities());
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this.loadUserDetails(username, null);
		return user;
	}


	public UserDetails loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth) throws UsernameNotFoundException {
		try {
			String transactionId = UUID.randomUUID().toString();
			BDConfigWrapper configWrapper = new BDConfigWrapper(transactionId, this.useCacheData);
			this.debug(transactionId, "Caricamento informazioni dell'utenza ["+username+"] in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaAnonima = AutorizzazioneUtils.getUserDetailFromUtenzaAnonima(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, configWrapper, this.getApiName(), this.getAuthType());
			userDetailFromUtenzaAnonima.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId, "Caricamento informazioni dell'utenza ["+username+"] completato.");
			return userDetailFromUtenzaAnonima;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni dell'utenza", e);
		}	finally {
		}
	}
	
	@Override
	public Authentication buildDetails(HttpServletRequest context) {
		return null;
	}
}
