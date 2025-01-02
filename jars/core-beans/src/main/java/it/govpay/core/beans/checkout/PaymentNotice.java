/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans.checkout;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"noticeNumber",
"fiscalCode",
"amount",
"companyName",
"description",
})
public class PaymentNotice extends JSONSerializable{

	@JsonProperty("noticeNumber")
	private String noticeNumber; // Numero Avviso

	@JsonProperty("fiscalCode")
	private String fiscalCode; // codDominio

	@JsonProperty("amount")
	private Integer amount; // importo in centesimi

	@JsonProperty("companyName")
	private String companyName; // ragione sociale

	@JsonProperty("description")
	private String description; // causale 140 caratteri


	public PaymentNotice noticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
		return this;
	}

	@JsonProperty("noticeNumber")
	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public PaymentNotice fiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
		return this;
	}

	@JsonProperty("fiscalCode")
	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public PaymentNotice amount(Integer amount) {
		this.amount = amount;
		return this;
	}

	@JsonProperty("amount")	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public PaymentNotice companyName(String companyName) {
		this.companyName = companyName;
		return this;
	}

	@JsonProperty("companyName")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public PaymentNotice description(String description) {
		this.description = description;
		return this;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentNotice paymentNotice = (PaymentNotice) o;
		return Objects.equals(noticeNumber, paymentNotice.noticeNumber) &&
				Objects.equals(fiscalCode, paymentNotice.fiscalCode) &&
				Objects.equals(amount, paymentNotice.amount) &&
				Objects.equals(companyName, paymentNotice.companyName) &&
				Objects.equals(description, paymentNotice.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(noticeNumber, fiscalCode, amount, companyName, description);
	}

	public static PaymentNotice parse(String json) throws IOException {
		return (PaymentNotice) parse(json, PaymentNotice.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "paymentNotice";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentNotice {\n");

		sb.append("    noticeNumber: ").append(toIndentedString(noticeNumber)).append("\n");
		sb.append("    fiscalCode: ").append(toIndentedString(fiscalCode)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
