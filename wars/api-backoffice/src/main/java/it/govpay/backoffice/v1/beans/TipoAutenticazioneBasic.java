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
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"username",
"password",
})
public class TipoAutenticazioneBasic extends it.govpay.core.beans.JSONSerializable {

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("password")
  private String password = null;

  /**
   **/
  public TipoAutenticazioneBasic username(String username) {
    this.username = username;
    return this;
  }

  @JsonProperty("username")
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  public TipoAutenticazioneBasic password(String password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneBasic tipoAutenticazioneBasic = (TipoAutenticazioneBasic) o;
    return Objects.equals(this.username, tipoAutenticazioneBasic.username) &&
        Objects.equals(this.password, tipoAutenticazioneBasic.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.username, this.password);
  }

  public static TipoAutenticazioneBasic parse(String json) throws IOException {
    return parse(json, TipoAutenticazioneBasic.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneBasic";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneBasic {\n");

    sb.append("    username: ").append(this.toIndentedString(this.username)).append("\n");
    sb.append("    password: ").append(this.toIndentedString(this.password)).append("\n");
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



