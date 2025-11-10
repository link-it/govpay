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
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"url",
"auth",
"subscriptionKey",
})
public class ConnettorePagopaFR extends JSONSerializable implements IValidable{

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("auth")
  private TipoAutenticazione auth = null;

  @JsonProperty("subscriptionKey")
  private String subscriptionKey = null;

  /**
   * Dati di integrazione al servizio PagoPA FR (Flussi di Rendicontazione)
   **/
  public ConnettorePagopaFR url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  public ConnettorePagopaFR auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return auth;
  }
  public void setAuth(TipoAutenticazione auth) {
    this.auth = auth;
  }

  /**
   **/
  public ConnettorePagopaFR subscriptionKey(String subscriptionKey) {
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettorePagopaFR connettorePagopaFR = (ConnettorePagopaFR) o;
    return Objects.equals(url, connettorePagopaFR.url) &&
        Objects.equals(auth, connettorePagopaFR.auth) &&
        Objects.equals(subscriptionKey, connettorePagopaFR.subscriptionKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, auth, subscriptionKey);
  }

  public static ConnettorePagopaFR parse(String json) throws IOException {
    return parse(json, ConnettorePagopaFR.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettorePagopaFR";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettorePagopaFR {\n");

    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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
    vf.getValidator("url", this.url).notNull().minLength(1).maxLength(255).isUrl();
    vf.getValidator("auth", this.auth).validateFields();
    vf.getValidator("subscriptionKey", this.subscriptionKey).minLength(1).maxLength(255);
  }
}
