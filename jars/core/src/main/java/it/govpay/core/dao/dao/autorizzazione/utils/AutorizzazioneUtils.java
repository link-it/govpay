package it.govpay.core.dao.autorizzazione.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthority;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AutorizzazioneUtils {
	
	public static final String DIRITTI = "diritti";
	public static final String PASSWORD_DEFAULT_VALUE = "secret";
	
	public static String getPrincipal(Authentication authentication) {
		if(authentication == null)
			return null;
		
		Object principalObj = authentication.getPrincipal();
		if(principalObj instanceof GovpayLdapUserDetails)
			return ((GovpayLdapUserDetails) principalObj).getIdentificativo();
		
		if(principalObj instanceof UserDetails)
			return ((UserDetails) principalObj).getUsername();
		
		return null;
	}

	public static UserDetails getUserDetailFromUtenzaRegistrata(String username, Utenza utenza, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		Applicazione applicazione = null;
		Operatore operatore = null;
		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
		try {
			applicazione = checkSubject ? AnagraficaManager.getApplicazioneBySubject(bd, utenza.getPrincipal())
					: AnagraficaManager.getApplicazioneByPrincipal(bd, utenza.getPrincipal()); 
			tipoUtenza = TIPO_UTENZA.APPLICAZIONE;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { 
			try {
				operatore = checkSubject ? AnagraficaManager.getOperatoreBySubject(bd, utenza.getPrincipal())
						: AnagraficaManager.getOperatoreByPrincipal(bd, utenza.getPrincipal());
				tipoUtenza = TIPO_UTENZA.OPERATORE;
			} catch (org.openspcoop2.generic_project.exception.NotFoundException ex) {
				throw new UsernameNotFoundException("Utenza non trovata.",ex);				
			} 
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for (Acl acl : utenza.getAcls()) {
			authorities.add(getAuthorirtyFromAcl(acl));
		}
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			authorities.addAll(authFromPreauth);
		}
		
		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setApplicazione(applicazione);
		userDetails.setOperatore(operatore);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);

		return userDetails;
	}

	public static GovpayLdapUserDetails getUserDetail(String username, String password, String identificativo, List<GrantedAuthority> authorities) {
		GovpayLdapUserDetails.Essence essence = new GovpayLdapUserDetails.Essence();
		essence.setAccountNonExpired(true);
		essence.setAccountNonLocked(true);
		essence.setCredentialsNonExpired(true);
		essence.setEnabled(true);
		essence.setUsername(username);
		essence.setPassword(password);
		essence.setAuthorities(authorities);
		essence.setDn(identificativo);

		return (GovpayLdapUserDetails) essence.createUserDetails();
	}

	/***
	 * 
	 * Mapping Acl Authorities:
	 * 
	 * DN = Servizio
	 * Role = Ruolo/Principal
	 * List<Attributes> = R,W,X
	 * 
	 * @param acl
	 * @return
	 */
	public static LdapAuthority getAuthorirtyFromAcl(Acl acl) {
		String role = acl.getServizio().getCodifica();
		String dn = acl.getPrincipal() != null ? acl.getPrincipal() : acl.getRuolo();
		
		Map<String, List<String>> attributes = new HashMap<>();
		List<String> dirittiList = new ArrayList<>();
		
		if(acl.getListaDiritti() != null && acl.getListaDiritti().size() >0 ) {
			for (Diritti diritti : acl.getListaDiritti()) {
				dirittiList.add(diritti.getCodifica());
			}
		}
		attributes.put(DIRITTI, dirittiList);
		LdapAuthority authority = new LdapAuthority(role, dn, attributes );
		
		return authority;
	}
	
	public static UserDetails getUserDetailFromUtenzaCittadino(String username, Utenza utenza, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.CITTADINO;
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for (Acl acl : utenza.getAcls()) {
			authorities.add(getAuthorirtyFromAcl(acl));
		}
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			authorities.addAll(authFromPreauth);
		}
		
		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);

		return userDetails;
	}
	
	public static UserDetails getUserDetailFromUtenzaAnonima(String username, Utenza utenza, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for (Acl acl : utenza.getAcls()) {
			authorities.add(getAuthorirtyFromAcl(acl));
		}
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			authorities.addAll(authFromPreauth);
		}
		
		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);

		return userDetails;
	}
}
