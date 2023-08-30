package it.govpay.bd;

import java.io.Serializable;

public class BDConfigWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transactionID;
	private boolean useCache;
	private Long idOperatore;
	
	public BDConfigWrapper() {
		this(null);
	}
	
	public BDConfigWrapper(String transactionID) {
		this(transactionID, true);
	}
	
	public BDConfigWrapper(String transactionID, boolean useCache) {
		this(transactionID, useCache, null);
	}
	
	public BDConfigWrapper(String transactionID, boolean useCache, Long idOperatore) {
		this.transactionID = transactionID;
		this.useCache = useCache;
		this.idOperatore = idOperatore;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public Long getIdOperatore() {
		return idOperatore;
	}

	public void setIdOperatore(Long idOperatore) {
		this.idOperatore = idOperatore;
	}

}
