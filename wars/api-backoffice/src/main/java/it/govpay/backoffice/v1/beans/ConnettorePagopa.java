package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"urlRPT",
"urlAvvisatura",
"auth",
"subscriptionKey",
})
public class ConnettorePagopa extends JSONSerializable implements IValidable{

  @JsonProperty("urlRPT")
  private String urlRPT = null;

  @JsonProperty("urlAvvisatura")
  private String urlAvvisatura = null;

  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
  @JsonProperty("subscriptionKey")
  private String subscriptionKey = null;
  
  /**
   * Dati di integrazione al servizio web RPT
   **/
  public ConnettorePagopa urlRPT(String urlRPT) {
    this.urlRPT = urlRPT;
    return this;
  }

  @JsonProperty("urlRPT")
  public String getUrlRPT() {
    return urlRPT;
  }
  public void setUrlRPT(String urlRPT) {
    this.urlRPT = urlRPT;
  }

  /**
   * Dati di integrazione al servizio web di Avvisatura Digitale
   **/
  public ConnettorePagopa urlAvvisatura(String urlAvvisatura) {
    this.urlAvvisatura = urlAvvisatura;
    return this;
  }

  @JsonProperty("urlAvvisatura")
  public String getUrlAvvisatura() {
    return urlAvvisatura;
  }
  public void setUrlAvvisatura(String urlAvvisatura) {
    this.urlAvvisatura = urlAvvisatura;
  }

  /**
   **/
  public ConnettorePagopa auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return this.auth;
  }
  public void setAuth(TipoAutenticazione auth) {
    this.auth = auth;
  }

  /**
   **/
  public ConnettorePagopa subscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
    return this;
  }

  @JsonProperty("subscriptionKey")
  public String getSubscriptionKey() {
    return subscriptionKey;
  }
  public void setSubscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ConnettorePagopa connettorePagopa = (ConnettorePagopa) o;
    return Objects.equals(urlRPT, connettorePagopa.urlRPT) &&
        Objects.equals(urlAvvisatura, connettorePagopa.urlAvvisatura) &&
        Objects.equals(auth, connettorePagopa.auth) &&
        Objects.equals(subscriptionKey, connettorePagopa.subscriptionKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urlRPT, urlAvvisatura, auth, subscriptionKey);
  }

  public static ConnettorePagopa parse(String json) throws IOException {
    return parse(json, ConnettorePagopa.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettorePagopa";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettorePagopa {\n");

    sb.append("    urlRPT: ").append(toIndentedString(urlRPT)).append("\n");
    sb.append("    urlAvvisatura: ").append(toIndentedString(urlAvvisatura)).append("\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
    sb.append("    subscriptionKey: ").append(toIndentedString(subscriptionKey)).append("\n");
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

  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("urlRPT", this.urlRPT).notNull().minLength(1).maxLength(255).isUrl();
		vf.getValidator("urlAvvisatura", this.urlAvvisatura).minLength(1).maxLength(255).isUrl();
		vf.getValidator("auth", this.auth).validateFields();
		vf.getValidator("subscriptionKey", this.subscriptionKey).minLength(1).maxLength(255);
	}
}



