package it.govpay.core.autorizzazione.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaAnonima;
import it.govpay.bd.model.UtenzaApplicazione;
import it.govpay.bd.model.UtenzaCittadino;
import it.govpay.bd.model.UtenzaOperatore;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.bd.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AutorizzazioneUtils {

	public static final String DIRITTI = "diritti";
	public static final String PASSWORD_DEFAULT_VALUE = "secret";

	public static GovpayLdapUserDetails getAuthenticationDetails(Authentication authentication) {
		if(authentication == null)
			return null;

		Object object = authentication.getPrincipal();
		if(object instanceof GovpayLdapUserDetails)
			return (GovpayLdapUserDetails) object;
		
		return null;
	}

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

	public static UserDetails getUserDetailFromUtenzaRegistrata(String username, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth, Map<String, List<String>> headerValues, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		Utenza utenza = null;
		Applicazione applicazione = null;
		Operatore operatore = null;
		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;

		List<Acl> aclsRuolo = new ArrayList<>();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			AclBD aclBD = new AclBD(bd);
			for (GrantedAuthority grantedAuthority : authFromPreauth) {
				AclFilter aclFilter = aclBD.newFilter();
				aclFilter.setRuolo(grantedAuthority.getAuthority());
				List<Acl> aclsRuolo2 = aclBD.findAll(aclFilter);
				if(aclsRuolo2 != null && !aclsRuolo2.isEmpty())
					aclsRuolo.addAll(aclsRuolo2);

				authorities.add(grantedAuthority);
			}
		}

		try {
			applicazione = checkSubject ? AnagraficaManager.getApplicazioneBySubject(bd, username) : AnagraficaManager.getApplicazioneByPrincipal(bd, username); 
			tipoUtenza = TIPO_UTENZA.APPLICAZIONE;
			Utenza utenzaTmp = applicazione.getUtenza();
			utenzaTmp.setAclRuoli(aclsRuolo);
			utenza = new UtenzaApplicazione(utenzaTmp, applicazione.getCodApplicazione());
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { 
			try {
				operatore = checkSubject ? AnagraficaManager.getOperatoreBySubject(bd, username) : AnagraficaManager.getOperatoreByPrincipal(bd, username);
				tipoUtenza = TIPO_UTENZA.OPERATORE;
				Utenza utenzaTmp  = operatore.getUtenza();
				utenzaTmp.setAclRuoli(aclsRuolo);
				utenza = new UtenzaOperatore(utenzaTmp, operatore.getNome(), headerValues);
			} catch (org.openspcoop2.generic_project.exception.NotFoundException ex) {
				throw new UsernameNotFoundException("Utenza non trovata.",ex);				
			} 
		}

		utenza.setCheckSubject(checkSubject);

		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setApplicazione(applicazione);
		userDetails.setOperatore(operatore);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);

		return userDetails;
	}

	public static GovpayLdapUserDetails getUserDetail(String username, String password, String identificativo, List<GrantedAuthority> authorities) {
		GovpayLdapUserDetails details = new GovpayLdapUserDetails();

		LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setAccountNonExpired(true);
		essence.setAccountNonLocked(true);
		essence.setCredentialsNonExpired(true);
		essence.setEnabled(true);
		essence.setUsername(username);
		essence.setPassword(password);
		essence.setAuthorities(authorities);
		essence.setDn(identificativo);

		details.setLdapUserDetailsImpl(essence.createUserDetails());

		return details;
	}

	public static UserDetails getUserDetailFromUtenzaCittadino(String username, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth,Map<String, List<String>> headerValues, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.CITTADINO;
		List<Acl> aclsRuolo = new ArrayList<>();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			AclBD aclBD = new AclBD(bd);
			for (GrantedAuthority grantedAuthority : authFromPreauth) {
				AclFilter aclFilter = aclBD.newFilter();
				aclFilter.setRuolo(grantedAuthority.getAuthority());
				List<Acl> aclsRuolo2 = aclBD.findAll(aclFilter);
				if(aclsRuolo2 != null && !aclsRuolo2.isEmpty())
					aclsRuolo.addAll(aclsRuolo2);

				authorities.add(grantedAuthority);
			}
		}

		Utenza utenza = new UtenzaCittadino(username,headerValues);
		utenza.setAclRuoli(aclsRuolo);
		List<Acl> aclPrincipal = new ArrayList<>();
		Acl acl = new Acl();
		acl.setUtenza(utenza);
		acl.setListaDiritti(Diritti.LETTURA.getCodifica() + Diritti.SCRITTURA.getCodifica());
		acl.setServizio(Servizio.API_PAGAMENTI); 
		acl.getProprieta().put(AuthorizationManager.CODICE_FISCALE_CITTADINO, username);
		aclPrincipal.add(acl);
		utenza.setAclPrincipal(aclPrincipal);
		utenza.setAbilitato(true);
		utenza.setPrincipalOriginale(username);
		utenza.setPrincipal(username);
		utenza.setCheckSubject(checkSubject);

		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);


		return userDetails;
	}

	public static UserDetails getUserDetailFromUtenzaAnonima(String username, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth) throws UsernameNotFoundException , ServiceException {
		return getUserDetailFromUtenzaAnonima(username, checkPassword, checkSubject, authFromPreauth, null);
	}

	public static UserDetails getUserDetailFromUtenzaAnonima(String username, boolean checkPassword, boolean checkSubject, 
			Collection<? extends GrantedAuthority> authFromPreauth, BasicBD bd) throws UsernameNotFoundException , ServiceException {

		TIPO_UTENZA tipoUtenza = TIPO_UTENZA.ANONIMO;
		List<Acl> aclsRuolo = new ArrayList<>();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(authFromPreauth != null && !authFromPreauth.isEmpty()) {
			AclBD aclBD = new AclBD(bd);

			for (GrantedAuthority grantedAuthority : authFromPreauth) {
				AclFilter aclFilter = aclBD.newFilter();
				aclFilter.setRuolo(grantedAuthority.getAuthority());
				List<Acl> aclsRuolo2 = aclBD.findAll(aclFilter);
				if(aclsRuolo2 != null && !aclsRuolo2.isEmpty())
					aclsRuolo.addAll(aclsRuolo2);

				authorities.add(grantedAuthority);
			}
		}

		Utenza utenza = new UtenzaAnonima();
		utenza.setAclRuoli(aclsRuolo);
		List<Acl> aclPrincipal = new ArrayList<>();
		Acl acl = new Acl();
		acl.setUtenza(utenza);
		acl.setListaDiritti(Diritti.LETTURA.getCodifica() + Diritti.SCRITTURA.getCodifica());
		acl.setServizio(Servizio.API_PAGAMENTI); 
		acl.getProprieta().put(AuthorizationManager.UTENZA_ANONIMA, "true");
		aclPrincipal.add(acl);
		utenza.setAclPrincipal(aclPrincipal);
		utenza.setAbilitato(true);
		utenza.setPrincipalOriginale(username);
		utenza.setPrincipal(username);
		utenza.setCheckSubject(checkSubject);

		GovpayLdapUserDetails userDetails = getUserDetail(username, PASSWORD_DEFAULT_VALUE, username, authorities);
		userDetails.setUtenza(utenza);
		userDetails.setTipoUtenza(tipoUtenza);

		return userDetails;
	}
}
