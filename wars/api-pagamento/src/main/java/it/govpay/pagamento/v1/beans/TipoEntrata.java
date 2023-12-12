/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.pagamento.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
"idEntrata",
})
public class TipoEntrata extends JSONSerializable {

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita = null;

  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;

  @JsonProperty("idEntrata")
  private String idEntrata = null;

  /**
   **/
  public TipoEntrata descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return this.descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  public TipoEntrata tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public TipoEntrata codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  /**
   **/
  public TipoEntrata idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  @JsonProperty("idEntrata")
  public String getIdEntrata() {
    return this.idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TipoEntrata tipoEntrata = (TipoEntrata) o;
    return Objects.equals(this.descrizione, tipoEntrata.descrizione) &&
        Objects.equals(this.tipoContabilita, tipoEntrata.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, tipoEntrata.codiceContabilita) &&
        Objects.equals(this.idEntrata, tipoEntrata.idEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.descrizione, this.tipoContabilita, this.codiceContabilita, this.idEntrata);
  }

  public static TipoEntrata parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, TipoEntrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoEntrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrata {\n");

    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    idEntrata: ").append(this.toIndentedString(this.idEntrata)).append("\n");
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



