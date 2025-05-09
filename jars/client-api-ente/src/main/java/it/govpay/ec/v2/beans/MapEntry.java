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
package it.govpay.ec.v2.beans;


import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class MapEntry   {
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "chiave del metadata")
 /**
   * chiave del metadata  
  **/
  private String key = null;
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "valore del metadata")
 /**
   * valore del metadata  
  **/
  private String value = null;
 /**
   * chiave del metadata
   * @return key
  **/
  @JsonProperty("key")
  @Size(min=1,max=140)
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public MapEntry key(String key) {
    this.key = key;
    return this;
  }

 /**
   * valore del metadata
   * @return value
  **/
  @JsonProperty("value")
  @Size(min=1,max=140)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public MapEntry value(String value) {
    this.value = value;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapEntry {\n");
    
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
