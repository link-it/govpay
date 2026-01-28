/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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