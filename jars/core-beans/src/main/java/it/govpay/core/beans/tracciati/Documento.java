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
package it.govpay.core.beans.tracciati;


import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"identificativo",
"descrizione",
"rata",
"soglia",
})
public class Documento extends it.govpay.core.beans.JSONSerializable {
  
  @JsonProperty("identificativo")
  private String identificativo = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("rata")
  private BigDecimal rata = null;

  @JsonProperty("soglia")
  private VincoloPagamento soglia = null;
  
  /**
   * Identificativo del documento
   **/
  public Documento identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

  @JsonProperty("identificativo")
  public String getIdentificativo() {
    return identificativo;
  }
  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  /**
   * descrizione del documento
   **/
  public Documento descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Rata del documento
   **/
  public Documento rata(BigDecimal rata) {
    this.rata = rata;
    return this;
  }

  @JsonProperty("rata")
  public BigDecimal getRata() {
    return rata;
  }
  public void setRata(BigDecimal rata) {
    this.rata = rata;
  }

  /**
   **/
  public Documento soglia(VincoloPagamento soglia) {
    this.soglia = soglia;
    return this;
  }

  @JsonProperty("soglia")
  public VincoloPagamento getSoglia() {
    return soglia;
  }
  public void setSoglia(VincoloPagamento soglia) {
    this.soglia = soglia;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Documento documento = (Documento) o;
    return Objects.equals(identificativo, documento.identificativo) &&
        Objects.equals(descrizione, documento.descrizione) &&
        Objects.equals(rata, documento.rata) &&
        Objects.equals(soglia, documento.soglia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificativo, descrizione, rata, soglia);
  }

  public static Documento parse(String json) throws IOException {
    return parse(json, Documento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "documento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Documento {\n");
    
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    rata: ").append(toIndentedString(rata)).append("\n");
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
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



