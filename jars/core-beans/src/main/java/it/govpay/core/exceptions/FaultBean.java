package it.govpay.core.exceptions;

import java.io.Serializable;

public class FaultBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private String faultCode;
    private String faultString;
    private String id;
    private String description;
    private Integer serial;
    private String originalFaultCode;
    private String originalFaultString;
    private String originalDescription;

	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	public String getFaultString() {
		return faultString;
	}
	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	public String getOriginalFaultCode() {
		return originalFaultCode;
	}
	public void setOriginalFaultCode(String originalFaultCode) {
		this.originalFaultCode = originalFaultCode;
	}
	public String getOriginalFaultString() {
		return originalFaultString;
	}
	public void setOriginalFaultString(String originalFaultString) {
		this.originalFaultString = originalFaultString;
	}
	public String getOriginalDescription() {
		return originalDescription;
	}
	public void setOriginalDescription(String originalDescription) {
		this.originalDescription = originalDescription;
	}
}