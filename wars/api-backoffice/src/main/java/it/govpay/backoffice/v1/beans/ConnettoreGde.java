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
"abilitato",
"url",
"auth",
})
public class ConnettoreGde extends JSONSerializable implements IValidable {

  @JsonProperty("abilitato")
  private Boolean abilitato = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("auth")
  private TipoAutenticazione auth = null;

  /**
   * Indica se il connettore GDE e' abilitato
   **/
  public ConnettoreGde abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean getAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * URL del microservizio Giornale Degli Eventi
   **/
  public ConnettoreGde url(String url) {
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
  public ConnettoreGde auth(TipoAutenticazione auth) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreGde connettoreGde = (ConnettoreGde) o;
    return Objects.equals(abilitato, connettoreGde.abilitato) &&
        Objects.equals(url, connettoreGde.url) &&
        Objects.equals(auth, connettoreGde.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, url, auth);
  }

  public static ConnettoreGde parse(String json) throws IOException {
    return parse(json, ConnettoreGde.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreGde";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreGde {\n");

    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
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
    vf.getValidator("abilitato", this.abilitato).notNull();

    if(Boolean.TRUE.equals(this.abilitato)) {
      vf.getValidator("url", this.url).notNull().minLength(1).maxLength(255).isUrl();
      vf.getValidator("auth", this.auth).validateFields();
    }
  }
}
