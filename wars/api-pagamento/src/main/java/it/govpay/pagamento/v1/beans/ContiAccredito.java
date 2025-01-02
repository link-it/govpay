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
package it.govpay.pagamento.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"bic",
"postale",
"mybank",
"abilitato",
"iban",
})
public class ContiAccredito extends JSONSerializable {

  @JsonProperty("bic")
  private String bic = null;

  @JsonProperty("postale")
  private Boolean postale = false;

  @JsonProperty("mybank")
  private Boolean mybank = false;

  @JsonProperty("abilitato")
  private Boolean abilitato = true;

  @JsonProperty("iban")
  private String iban = null;

  /**
   **/
  public ContiAccredito bic(String bic) {
    this.bic = bic;
    return this;
  }

  @JsonProperty("bic")
  public String getBic() {
    return this.bic;
  }
  public void setBic(String bic) {
    this.bic = bic;
  }

  /**
   * indica se e' un c/c postale
   **/
  public ContiAccredito postale(Boolean postale) {
    this.postale = postale;
    return this;
  }

  @JsonProperty("postale")
  public Boolean isPostale() {
    return this.postale;
  }
  public void setPostale(Boolean postale) {
    this.postale = postale;
  }

  /**
   * indica se e' un iban abilitato sul circuito mybank
   **/
  public ContiAccredito mybank(Boolean mybank) {
    this.mybank = mybank;
    return this;
  }

  @JsonProperty("mybank")
  public Boolean isMybank() {
    return this.mybank;
  }
  public void setMybank(Boolean mybank) {
    this.mybank = mybank;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public ContiAccredito abilitato(Boolean abilitato) {
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
  public ContiAccredito iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return this.iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ContiAccredito contiAccredito = (ContiAccredito) o;
    return Objects.equals(this.bic, contiAccredito.bic) &&
        Objects.equals(this.postale, contiAccredito.postale) &&
        Objects.equals(this.mybank, contiAccredito.mybank) &&
        Objects.equals(this.abilitato, contiAccredito.abilitato) &&
        Objects.equals(this.iban, contiAccredito.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.bic, this.postale, this.mybank, this.abilitato, this.iban);
  }

  public static ContiAccredito parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, ContiAccredito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contiAccredito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContiAccredito {\n");

    sb.append("    bic: ").append(this.toIndentedString(this.bic)).append("\n");
    sb.append("    postale: ").append(this.toIndentedString(this.postale)).append("\n");
    sb.append("    mybank: ").append(this.toIndentedString(this.mybank)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    iban: ").append(this.toIndentedString(this.iban)).append("\n");
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



