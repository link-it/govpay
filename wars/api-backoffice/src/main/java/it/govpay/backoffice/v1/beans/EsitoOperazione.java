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
package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"percentuale",
"descrizione",
"stato",
})
public class EsitoOperazione extends JSONSerializable {

  @JsonProperty("percentuale")
  private BigDecimal percentuale = null;

  @JsonProperty("descrizione")
  private List<DominioIndex> descrizione = new ArrayList<>();

  @JsonProperty("stato")
  private String stato = null;

  /**
   * Percentuale di avanzamento
   **/
  public EsitoOperazione percentuale(BigDecimal percentuale) {
    this.percentuale = percentuale;
    return this;
  }

  @JsonProperty("percentuale")
  public BigDecimal getPercentuale() {
    return this.percentuale;
  }
  public void setPercentuale(BigDecimal percentuale) {
    this.percentuale = percentuale;
  }

  /**
   * testo descrittivo
   **/
  public EsitoOperazione descrizione(List<DominioIndex> descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public List<DominioIndex> getDescrizione() {
    return this.descrizione;
  }
  public void setDescrizione(List<DominioIndex> descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * stato dell'operazione (OK/KO/WARN)
   **/
  public EsitoOperazione stato(String stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public String getStato() {
    return this.stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    EsitoOperazione esitoOperazione = (EsitoOperazione) o;
    return Objects.equals(this.percentuale, esitoOperazione.percentuale) &&
        Objects.equals(this.descrizione, esitoOperazione.descrizione) &&
        Objects.equals(this.stato, esitoOperazione.stato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.percentuale, this.descrizione, this.stato);
  }

  public static EsitoOperazione parse(String json) throws IOException {
    return parse(json, EsitoOperazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "esitoOperazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoOperazione {\n");

    sb.append("    percentuale: ").append(this.toIndentedString(this.percentuale)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
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



