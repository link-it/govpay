package it.govpay.core.dao.autorizzazione;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BasicBD;
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
		UserDetails user = this._loadUserDetails(username, token.getAuthorities());
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this._loadUserDetails(username, null);
		return user;
	}


	private UserDetails _loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth) throws UsernameNotFoundException {
		BasicBD bd = null;

		try {
			this.log.info("Lettura delle informazioni per l'utenza ["+username+"] in corso...");
			String transactionId = UUID.randomUUID().toString();
			bd = BasicBD.newInstance(transactionId, this.useCacheData); 
			return AutorizzazioneUtils.getUserDetailFromUtenzaCittadino(username, this.checkPassword, this.checkSubject, authFromPreauth, bd);
//		}  catch(NotFoundException e){
//			throw new UsernameNotFoundException("Utenza "+username+" non trovata.",e);
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile autenticare l'utenza", e);
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
