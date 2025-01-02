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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Metadata Custom da inserire nella ricevuta di pagamento
 **/
@Schema(description="Metadata Custom da inserire nella ricevuta di pagamento")
public class Metadata   {
  
  @Schema(description = "")
  private List<MapEntry> mapEntries = null;
 /**
   * Get mapEntries
   * @return mapEntries
  **/
  @JsonProperty("mapEntries")
  public List<MapEntry> getMapEntries() {
    return mapEntries;
  }

  public void setMapEntries(List<MapEntry> mapEntries) {
    this.mapEntries = mapEntries;
  }

  public Metadata mapEntries(List<MapEntry> mapEntries) {
    this.mapEntries = mapEntries;
    return this;
  }

  public Metadata addMapEntriesItem(MapEntry mapEntriesItem) {
    this.mapEntries.add(mapEntriesItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Metadata {\n");
    
    sb.append("    mapEntries: ").append(toIndentedString(mapEntries)).append("\n");
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
