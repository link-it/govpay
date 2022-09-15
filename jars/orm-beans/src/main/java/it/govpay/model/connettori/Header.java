package it.govpay.model.connettori;

import java.io.Serializable;

public class Header implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Header() {
	}
	
	private String name;
	private String value;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
