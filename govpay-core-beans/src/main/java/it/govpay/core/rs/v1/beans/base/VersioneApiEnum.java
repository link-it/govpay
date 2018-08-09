/**
 * 
 */
package it.govpay.core.rs.v1.beans.base;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 giu 2018 $
 * 
 */
/**
 * Versione delle API di integrazione utilizzate
 */
public enum VersioneApiEnum {




	REST_1("REST v1");
	//SOAP_3("SOAP v3");




	private String value;

	VersioneApiEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	public static VersioneApiEnum fromValue(String text) {
		for (VersioneApiEnum b : VersioneApiEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
	
	public static VersioneApiEnum fromName(String text) {
		for (VersioneApiEnum b : VersioneApiEnum.values()) {
			if (String.valueOf(b.toNameString()).equals(text)) {
				return b;
			}
		}
		return null;
	}
	
	public String toNameString() {
		switch(this) {
		case REST_1: return "REST_1";
		//case SOAP_3: return "SOAP_3";
		default:  return "";
		}
	}
	
	
	
}

