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
package it.govpay.ragioneria.v3.beans;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Dati della pendenza con voci
 **/
@Schema(description="Dati della pendenza con voci")
public class PendenzaPagata extends Pendenza  {

  @Schema(description = "")
  private List<VocePendenzaPagata> voci = new ArrayList<>();
 /**
   * Get voci
   * @return voci
  **/
  @JsonProperty("voci")
 @Size(min=1,max=5)  public List<VocePendenzaPagata> getVoci() {
    return voci;
  }

  public void setVoci(List<VocePendenzaPagata> voci) {
    this.voci = voci;
  }

  public PendenzaPagata voci(List<VocePendenzaPagata> voci) {
    this.voci = voci;
    return this;
  }

  public PendenzaPagata addVociItem(VocePendenzaPagata vociItem) {
    this.voci.add(vociItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaPagata {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
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
