/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
"id",
})
public class RuoloIndex extends JSONSerializable {

  @JsonProperty("id")
  private String id = null;

  /**
   * identificativo del ruolo
   **/
  public RuoloIndex id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    RuoloIndex ruoloIndex = (RuoloIndex) o;
    return Objects.equals(this.id, ruoloIndex.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  public static RuoloIndex parse(String json) throws IOException {
    return parse(json, RuoloIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ruoloIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RuoloIndex {\n");

    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
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



