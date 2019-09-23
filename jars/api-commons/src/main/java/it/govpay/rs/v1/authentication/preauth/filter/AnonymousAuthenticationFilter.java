package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.core.dao.autorizzazione.AutenticazioneUtenzeAnonimeDAO;

public class AnonymousAuthenticationFilter extends org.springframework.security.web.authentication.AnonymousAuthenticationFilter {

	public AnonymousAuthenticationFilter(String key) {
		super(key, getPrincipalUtenzaAnonima(), getAuthoritiesUtenzaAnonima());
	}
	
	public static Object getPrincipalUtenzaAnonima() {
		List<GrantedAuthority> authFromPreauth = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
		try {
			AutenticazioneUtenzeAnonimeDAO autenticazioneUtenzeAnonimeDAO = new AutenticazioneUtenzeAnonimeDAO();
			autenticazioneUtenzeAnonimeDAO.setApiName("API_PAGAMENTO");
			autenticazioneUtenzeAnonimeDAO.setAuthType("PUBLIC");
			return autenticazioneUtenzeAnonimeDAO.loadUserDetails("anonymousUser", authFromPreauth);
		} catch (UsernameNotFoundException e) {
		}
		return "anonymousUser";
	}
	
	public static List<GrantedAuthority> getAuthoritiesUtenzaAnonima() {
		List<GrantedAuthority> authFromPreauth = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
		UserDetails utenzaAnonima = null;
		try {
			AutenticazioneUtenzeAnonimeDAO autenticazioneUtenzeAnonimeDAO = new AutenticazioneUtenzeAnonimeDAO(); 
			autenticazioneUtenzeAnonimeDAO.setApiName("API_PAGAMENTO");
			autenticazioneUtenzeAnonimeDAO.setAuthType("PUBLIC");
			utenzaAnonima = autenticazioneUtenzeAnonimeDAO.loadUserDetails("anonymousUser", authFromPreauth);
		} catch (UsernameNotFoundException e) {
		}
		
		if(utenzaAnonima != null) {
			List<GrantedAuthority> authorities = new ArrayList<>(); 
			authorities.addAll(utenzaAnonima.getAuthorities());
			return authorities;
		}
		
		return AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
	}
}

