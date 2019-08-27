package it.govpay.core.dao.autorizzazione;

import it.govpay.core.dao.commons.BaseDAO;

public class BaseAutenticazioneDAO extends BaseDAO {

	protected boolean checkSubject = false;
	protected boolean checkPassword = false;
	
	private String apiName;
	private String authType;
		
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
