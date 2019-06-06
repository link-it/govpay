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

import it.govpay.bd.BasicBD;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.commons.BaseDAO;

public class AutenticazioneUtenzeAnonimeDAO extends BaseDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication>, AuthenticationDetailsSource<HttpServletRequest, Authentication> {	

	private boolean checkSubject = false;
	private boolean checkPassword = false;
	
	
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
		BasicBD bd = null;

		try {
			this.log.debug("Caricamento informazioni dell'utenza ["+username+"] in corso...");
			String transactionId = UUID.randomUUID().toString();
			bd = BasicBD.newInstance(transactionId, this.useCacheData);
			UserDetails userDetailFromUtenzaAnonima = AutorizzazioneUtils.getUserDetailFromUtenzaAnonima(username, this.checkPassword, this.checkSubject, authFromPreauth, bd);
			this.log.debug("Caricamento informazioni dell'utenza ["+username+"] completato.");
			return userDetailFromUtenzaAnonima;
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni dell'utenza", e);
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
	
	@Override
	public Authentication buildDetails(HttpServletRequest context) {
		return null;
	}
}
