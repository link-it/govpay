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
			UserDetails user = this._loadUserDetailsFromSession(username, token.getAuthorities(),attributeValues );
			return user;
		} else {
			Map<String, List<String>> headerValues = new HashMap<>();
			if(token.getDetails() != null && token.getDetails() instanceof GovpayWebAuthenticationDetails) {
				headerValues = ((GovpayWebAuthenticationDetails) token.getDetails()).getHeaderValues();
			}

			UserDetails user = this._loadUserDetails(username, token.getAuthorities(), headerValues);
			return user;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this._loadUserDetails(username, null, null);
		return user;
	}


	private UserDetails _loadUserDetails(String username, Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues) throws UsernameNotFoundException {
		BasicBD bd = null;

		try {
			String transactionId = UUID.randomUUID().toString();
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ["+username+"] in corso...");
			bd = BasicBD.newInstance(transactionId, this.useCacheData);
			UtenzeBD utenzeBD = new UtenzeBD(bd);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new NotFoundException("Utenza "+username+" non trovata.");

			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio in corso...");
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrata(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, headerValues, bd);
			userDetails.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio completata.");
			return userDetails;
		}  catch(NotFoundException e){
			throw new UsernameNotFoundException("Utenza "+username+" non trovata.",e);
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile autenticare l'utenza", e);
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
			this.debug(transactionId,"Lettura delle informazioni per l'utenza ["+username+"] in corso...");
			bd = BasicBD.newInstance(transactionId, this.useCacheData);
			UtenzeBD utenzeBD = new UtenzeBD(bd);

			boolean exists = false;

			if(this.isCheckSubject())
				exists = utenzeBD.existsBySubject(username);
			else 
				exists = utenzeBD.existsByPrincipal(username);

			if(!exists)
				throw new NotFoundException("Utenza "+username+" non trovata.");
			
			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio in corso...");
			GovpayLdapUserDetails userDetailFromUtenzaCittadino = AutorizzazioneUtils.getUserDetailFromUtenzaRegistrataInSessione(username, this.isCheckPassword(), this.isCheckSubject(), authFromPreauth, attributeValues, userDetailFromSession, bd);
			userDetailFromUtenzaCittadino.setIdTransazioneAutenticazione(transactionId);
			this.debug(transactionId,"Utenza ["+username+"] trovata, lettura del dettaglio completata.");
			return userDetailFromUtenzaCittadino;
		} catch(NotFoundException e){
			throw new UsernameNotFoundException("Utenza "+username+" non trovata.",e);
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile caricare le informazioni del cittadino ["+username+"]: ", e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
