package it.govpay.core.dao.autorizzazione;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.commons.BaseDAO;

public class AutenticazioneUtenzeRegistrateDAO extends BaseDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	private boolean checkSubject = false;
	private boolean checkPassword = false;
	
	public AutenticazioneUtenzeRegistrateDAO() {
		super();
	}

	public AutenticazioneUtenzeRegistrateDAO(boolean useCacheData) {
		super(useCacheData);
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();
		
		Map<String, List<String>> headerValues = new HashMap<>();
		if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
			headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
		}
		
		UserDetails user = this._loadUserDetails(username, token.getAuthorities(), headerValues);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this._loadUserDetails(username, null, null);
		return user;
	}


	private UserDetails _loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) throws UsernameNotFoundException {
		BasicBD bd = null;

		try {
			this.log.info("Lettura delle informazioni per l'utenza ["+username+"] in corso...");
			String transactionId = UUID.randomUUID().toString();
			bd = BasicBD.newInstance(transactionId, this.useCacheData);
			UtenzeBD utenzeBD = new UtenzeBD(bd);

			boolean exists = false;
			
			if(this.checkSubject)
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);
			
			if(!exists)
				throw new NotFoundException("Utenza "+username+" non trovata.");
			
			this.log.info("Utenza ["+username+"] trovata, lettura del dettaglio in corso...");
			return AutorizzazioneUtils.getUserDetailFromUtenzaRegistrata(username, this.checkPassword, this.checkSubject, authFromPreauth, headerValues, bd);
		}  catch(NotFoundException e){
			throw new UsernameNotFoundException("Utenza "+username+" non trovata.",e);
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
