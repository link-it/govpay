package it.govpay.bd;

import java.io.Serializable;

public class BDConfigWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transactionID;
	private boolean useCache;
	
	public BDConfigWrapper() {
		this(null);
	}
	
	public BDConfigWrapper(String transactionID) {
		this(transactionID, true);
	}
	
	public BDConfigWrapper(String transactionID, boolean useCache) {
		this.transactionID = transactionID;
		this.useCache = useCache;
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

}
