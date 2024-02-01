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


import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"ragioneSociale",
"unitaOperative",
})
public class DominioProfiloIndex extends JSONSerializable {

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;

  @JsonProperty("unitaOperative")
  private List<UnitaOperativaIndex> unitaOperative = null;

  /**
   * Codice fiscale del beneficiario
   **/
  public DominioProfiloIndex idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Ragione sociale del beneficiario
   **/
  public DominioProfiloIndex ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  @JsonProperty("ragioneSociale")
  public String getRagioneSociale() {
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  /**
   **/
  public DominioProfiloIndex unitaOperative(List<UnitaOperativaIndex> unitaOperative) {
    this.unitaOperative = unitaOperative;
    return this;
  }

  @JsonProperty("unitaOperative")
  public List<UnitaOperativaIndex> getUnitaOperative() {
    return unitaOperative;
  }
  public void setUnitaOperative(List<UnitaOperativaIndex> unitaOperative) {
    this.unitaOperative = unitaOperative;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DominioProfiloIndex dominioProfiloIndex = (DominioProfiloIndex) o;
    return Objects.equals(idDominio, dominioProfiloIndex.idDominio) &&
        Objects.equals(ragioneSociale, dominioProfiloIndex.ragioneSociale) &&
        Objects.equals(unitaOperative, dominioProfiloIndex.unitaOperative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, ragioneSociale, unitaOperative);
  }

  public static DominioProfiloIndex parse(String json) throws IOException {
    return parse(json, DominioProfiloIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioProfiloIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioProfiloIndex {\n");

    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    unitaOperative: ").append(toIndentedString(unitaOperative)).append("\n");
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



