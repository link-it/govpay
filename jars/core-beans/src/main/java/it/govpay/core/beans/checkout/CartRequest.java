package it.govpay.core.beans.checkout;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"emailNotice",
"paymentNotices",
"returnUrls",
})
public class CartRequest extends JSONSerializable {

	@JsonProperty("emailNotice")
	private String emailNotice;
	
	@JsonProperty("paymentNotices")
	private List<PaymentNotice> paymentNotices;
	
	@JsonProperty("returnUrls")
	private ReturnUrls returnUrls;
	
	
	public CartRequest emailNotice(String emailNotice) {
		this.emailNotice = emailNotice;
		return this;
	}
	
	@JsonProperty("emailNotice")
	public String getEmailNotice() {
		return emailNotice;
	}
	
	public void setEmailNotice(String emailNotice) {
		this.emailNotice = emailNotice;
	}
	
	public CartRequest paymentNotices(List<PaymentNotice> paymentNotices) {
		this.paymentNotices = paymentNotices;
		return this;
	}
	
	@JsonProperty("paymentNotices")
	public List<PaymentNotice> getPaymentNotices() {
		return paymentNotices;
	}
	
	public void setPaymentNotices(List<PaymentNotice> paymentNotices) {
		this.paymentNotices = paymentNotices;
	}
	
	public CartRequest returnUrls(ReturnUrls returnUrls) {
		this.returnUrls = returnUrls;
		return this;
	}
	
	@JsonProperty("returnUrls")
	public ReturnUrls getReturnUrls() {
		return returnUrls;
	}
	
	public void setReturnUrls(ReturnUrls returnUrls) {
		this.returnUrls = returnUrls;
	}
	
	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CartRequest cartRequest = (CartRequest) o;
		return Objects.equals(emailNotice, cartRequest.emailNotice) &&
				Objects.equals(paymentNotices, cartRequest.paymentNotices) &&
				Objects.equals(returnUrls, cartRequest.returnUrls);
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailNotice, paymentNotices, returnUrls);
	}

	public static CartRequest parse(String json) throws IOException {
		return (CartRequest) parse(json, CartRequest.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "cartRequest";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentNotice {\n");

		sb.append("    emailNotice: ").append(toIndentedString(emailNotice)).append("\n");
		sb.append("    paymentNotices: ").append(toIndentedString(paymentNotices)).append("\n");
		sb.append("    returnUrls: ").append(toIndentedString(returnUrls)).append("\n");
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
