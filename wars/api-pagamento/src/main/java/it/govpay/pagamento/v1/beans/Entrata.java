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
package it.govpay.pagamento.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"contoAccredito",
"contoAppoggio",
"tipoContabilita",
"codiceContabilita",
"abilitato",
"idEntrata",
"tipoEntrata",
})
public class Entrata extends JSONSerializable {

  @JsonProperty("contoAccredito")
  private String contoAccredito = null;

  @JsonProperty("contoAppoggio")
  private String contoAppoggio = null;

  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita = null;

  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;

  @JsonProperty("abilitato")
  private Boolean abilitato = true;

  @JsonProperty("idEntrata")
  private String idEntrata = null;

  @JsonProperty("tipoEntrata")
  private TipoEntrata tipoEntrata = null;

  /**
   **/
  public Entrata contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return this.contoAccredito;
  }
  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  /**
   **/
  public Entrata contoAppoggio(String contoAppoggio) {
    this.contoAppoggio = contoAppoggio;
    return this;
  }

  @JsonProperty("contoAppoggio")
  public String getContoAppoggio() {
    return this.contoAppoggio;
  }
  public void setContoAppoggio(String contoAppoggio) {
    this.contoAppoggio = contoAppoggio;
  }

  /**
   **/
  public Entrata tipoContabilita(TipoContabilita tipoContabilita) {
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
  public Entrata codiceContabilita(String codiceContabilita) {
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
   * Indicazione l'entrata e' abilitata
   **/
  public Entrata abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   **/
  public Entrata idEntrata(String idEntrata) {
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

  /**
   **/
  public Entrata tipoEntrata(TipoEntrata tipoEntrata) {
    this.tipoEntrata = tipoEntrata;
    return this;
  }

  @JsonProperty("tipoEntrata")
  public TipoEntrata getTipoEntrata() {
    return this.tipoEntrata;
  }
  public void setTipoEntrata(TipoEntrata tipoEntrata) {
    this.tipoEntrata = tipoEntrata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Entrata entrata = (Entrata) o;
    return Objects.equals(this.contoAccredito, entrata.contoAccredito) &&
        Objects.equals(this.contoAppoggio, entrata.contoAppoggio) &&
        Objects.equals(this.tipoContabilita, entrata.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, entrata.codiceContabilita) &&
        Objects.equals(this.abilitato, entrata.abilitato) &&
        Objects.equals(this.idEntrata, entrata.idEntrata) &&
        Objects.equals(this.tipoEntrata, entrata.tipoEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contoAccredito, this.contoAppoggio, this.tipoContabilita, this.codiceContabilita, this.abilitato, this.idEntrata, this.tipoEntrata);
  }

  public static Entrata parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Entrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entrata {\n");

    sb.append("    contoAccredito: ").append(this.toIndentedString(this.contoAccredito)).append("\n");
    sb.append("    contoAppoggio: ").append(this.toIndentedString(this.contoAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    idEntrata: ").append(this.toIndentedString(this.idEntrata)).append("\n");
    sb.append("    tipoEntrata: ").append(this.toIndentedString(this.tipoEntrata)).append("\n");
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



