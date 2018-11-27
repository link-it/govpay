package it.govpay.core.dao.autorizzazione;

import java.util.Collection;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.utils.GpThreadLocal;

public class AutenticazioneUtenzeRegistrateDAO extends BaseDAO implements UserDetailsService, AuthenticationUserDetailsService<Authentication> {	

	private boolean checkSubject = false;
	
	
	public AutenticazioneUtenzeRegistrateDAO() {
		super();
	}

	public AutenticazioneUtenzeRegistrateDAO(boolean useCacheData) {
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
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId(), useCacheData);
			UtenzeBD utenzeBD = new UtenzeBD(bd);

			Utenza utenza = null;
			
			if(this.checkSubject)
				utenza = utenzeBD.getUtenzaBySubject(username);
			else 
				utenza = utenzeBD.getUtenzaByPrincipal(username);
			

			return AutorizzazioneUtils.getUserDetailFromUtenzaRegistrata(username, utenza, false, checkSubject, authFromPreauth, bd);
		}  catch(NotFoundException e){
			throw new UsernameNotFoundException("Utenza non trovata.",e);
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
}
