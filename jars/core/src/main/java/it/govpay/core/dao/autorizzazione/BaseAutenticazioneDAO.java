package it.govpay.core.dao.autorizzazione;

import it.govpay.core.dao.commons.BaseDAO;

public class BaseAutenticazioneDAO extends BaseDAO {
	
	public static final String SESSION_PRINCIPAL_ATTRIBUTE_NAME = "GP_PRINCIPAL";
	public static final String SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME = "GP_PRINCIPAL_OBJECT";

	protected boolean checkSubject = false;
	protected boolean checkPassword = false;
	
	private String apiName;
	private String authType;
	protected boolean leggiUtenzaDaSessione = false;
		
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
