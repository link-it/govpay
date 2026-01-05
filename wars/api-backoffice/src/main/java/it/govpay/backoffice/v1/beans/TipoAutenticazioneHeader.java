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

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"headerName",
"headerValue",
})
public class TipoAutenticazioneHeader extends JSONSerializable {

  @JsonProperty("headerName")
  private String headerName = null;

  @JsonProperty("headerValue")
  private String headerValue = null;

  /**
   **/
  public TipoAutenticazioneHeader headerName(String headerName) {
    this.headerName = headerName;
    return this;
  }

  @JsonProperty("headerName")
  public String getHeaderName() {
    return headerName;
  }
  public void setHeaderName(String headerName) {
    this.headerName = headerName;
  }

  /**
   **/
  public TipoAutenticazioneHeader headerValue(String headerValue) {
    this.headerValue = headerValue;
    return this;
  }

  @JsonProperty("headerValue")
  public String getHeaderValue() {
    return headerValue;
  }
  public void setHeaderValue(String headerValue) {
    this.headerValue = headerValue;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneHeader tipoAutenticazioneHeader = (TipoAutenticazioneHeader) o;
    return Objects.equals(headerName, tipoAutenticazioneHeader.headerName) &&
        Objects.equals(headerValue, tipoAutenticazioneHeader.headerValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(headerName, headerValue);
  }

  public static TipoAutenticazioneHeader parse(String json) throws IOException {
    return parse(json, TipoAutenticazioneHeader.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneHeader";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneHeader {\n");

    sb.append("    headerName: ").append(toIndentedString(headerName)).append("\n");
    sb.append("    headerValue: ").append(toIndentedString(headerValue)).append("\n");
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



