/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.orm.profilazione;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "indirizzi_postali")
public class IndirizzoPostale implements Serializable {

	private static final long serialVersionUID = 1L;
	/*** Persistent Properties ***/
	private Integer postalCode;
	private String capCode;
	private String postBox;
	private String fiscalCode;
	private String city;
	private String faxNumber;
	private String flagResidence;
	private String address;
	private String country;
	private String vatCode;
	private String province;
	private String phoneNumber;
	private String mobilePhone;
	// private String email;
	private String numeroCivico;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "CAP")
	public String getCapCode() {
		return this.capCode;
	}

	public void setCapCode(String capCode) {
		this.capCode = capCode;
	}

	@Column(name = "CASELLAPOSTALE")
	public String getPostBox() {
		return this.postBox;
	}

	public void setPostBox(String postBox) {
		this.postBox = postBox;
	}

	@Column(name = "CODICEFISCALE")
	public String getFiscalCode() {
		return this.fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	@Column(name = "COMUNE")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "FAX")
	public String getFaxNumber() {
		return this.faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	@Column(name = "FLAGRESIDENTE")
	public String getFlagResidence() {
		return this.flagResidence;
	}

	public void setFlagResidence(String flagResidence) {
		this.flagResidence = flagResidence;
	}

	@Column(name = "INDIRIZZO")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "NAZIONE")
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "PARTITAIVA")
	public String getVatCode() {
		return this.vatCode;
	}

	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}

	@Column(name = "PROVINCIA")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "TELEFONO")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "TELEFONOCELLULARE")
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	// @Column(name="EMAIL")
	// public String getEmail() {
	// return this.email;
	// }
	// public void setEmail(String email){
	// this.email=email;
	// }

	@Column(name = "NUMEROCIVICO")
	public String getNumeroCivico() {
		return numeroCivico;
	}

	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

}
