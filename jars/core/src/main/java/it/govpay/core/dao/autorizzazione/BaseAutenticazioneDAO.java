package it.govpay.core.dao.autorizzazione;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.dao.commons.BaseDAO;

public class BaseAutenticazioneDAO extends BaseDAO {
	
	public static final String SESSION_PRINCIPAL_ATTRIBUTE_NAME = "GP_PRINCIPAL";
	public static final String SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME = "GP_PRINCIPAL_OBJECT";

	protected boolean checkSubject = false;
	protected boolean checkPassword = false;
	
	private String apiName;
	private String authType;
	protected boolean leggiUtenzaDaSessione = false;
	private boolean userAutoSignup = false;
	private String userAutoSignupDefaultRole = null;
	
	public UserDetails loadUserByLdapUserDetail(String username, GovpayLdapUserDetails userDetail) throws UsernameNotFoundException {
		throw new NotImplementedException("Operazione implementata");
	}
		
	public BaseAutenticazioneDAO() {
		super();
	}

	public BaseAutenticazioneDAO(boolean useCacheData) {
		super(useCacheData);
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

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
	public boolean isLeggiUtenzaDaSessione() {
		return leggiUtenzaDaSessione;
	}

	public void setLeggiUtenzaDaSessione(boolean leggiUtenzaDaSessione) {
		this.leggiUtenzaDaSessione = leggiUtenzaDaSessione;
	}

	public boolean isUserAutoSignup() {
		return userAutoSignup;
	}

	public void setUserAutoSignup(boolean userAutoSignup) {
		this.userAutoSignup = userAutoSignup;
	}

	public String getUserAutoSignupDefaultRole() {
		return userAutoSignupDefaultRole;
	}

	public void setUserAutoSignupDefaultRole(String userAutoSignupDefaultRole) {
		this.userAutoSignupDefaultRole = userAutoSignupDefaultRole;
	}

	public void debug(String transactionId, String msg) {
		StringBuilder sb = new StringBuilder();
		
		// API Name / Auth Type
		if(this.apiName != null) {
			sb.append("API: ").append(this.apiName);
		}
		if(this.authType != null) {
			if(sb.length() > 0)
				sb.append(" | ");
			
			sb.append("AUTH: ").append(this.authType);
		}
		if(this.apiName != null) {
			sb.append(" | Leggi Utenza da Sessione: ").append(this.leggiUtenzaDaSessione);
		}
		
		if(sb.length() > 0)
			sb.append(" | ");
		
		if(this.userAutoSignup) {
			sb.append("Autocensimento utenze attivo con ruolo default: " + this.userAutoSignupDefaultRole);
		} else {
			sb.append("Autocensimento utenze disabilitato");
		}
		
		// Id transazione accesso DB
		if(transactionId != null) {
			if(sb.length() > 0)
				sb.append(" | ");
			
			sb.append("Id Transazione Autenticazione: ").append(transactionId);
		}
	
		// messaggio
		if(sb.length() > 0)
			sb.append(" | ");
		
		sb.append(msg);
		
		this.log.debug(sb.toString());
	}
}
