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
package it.govpay.ragioneria.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"testo",
})
public class Allegato extends JSONSerializable {


  /**
   * Tipologia di allegato
   */
  public enum TipoEnum {




    ESITO_PAGAMENTO("Esito pagamento"),


    MARCA_DA_BOLLO("Marca da bollo");




    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }



  @JsonProperty("tipo")
  private TipoEnum tipo = null;

  @JsonProperty("testo")
  private String testo = null;

  /**
   * Tipologia di allegato
   **/
  public Allegato tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoEnum getTipo() {
    return this.tipo;
  }
  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  /**
   * allegato codificato in base64
   **/
  public Allegato testo(String testo) {
    this.testo = testo;
    return this;
  }

  @JsonProperty("testo")
  public String getTesto() {
    return this.testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Allegato allegato = (Allegato) o;
    return Objects.equals(this.tipo, allegato.tipo) &&
        Objects.equals(this.testo, allegato.testo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tipo, this.testo);
  }

  public static Allegato parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Allegato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "allegato";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Allegato {\n");

    sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
    sb.append("    testo: ").append(this.toIndentedString(this.testo)).append("\n");
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



