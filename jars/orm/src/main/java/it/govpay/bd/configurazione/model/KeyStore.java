package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class KeyStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type = null;
	private String location = null;
	private String password = null;
	private String managementAlgorithm = null;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getManagementAlgorithm() {
		return managementAlgorithm;
	}
	public void setManagementAlgorithm(String managementAlgorithm) {
		this.managementAlgorithm = managementAlgorithm;
	}


}
