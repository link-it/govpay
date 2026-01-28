/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class Allegato   {
  public enum TipoEnum {
    ESITO_PAGAMENTO("Esito pagamento"),
    MARCA_DA_BOLLO("Marca da bollo");

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    
    public static TipoEnum fromCodifica(String codifica) {
		switch (codifica) {
		case "BD":
			return MARCA_DA_BOLLO;
		case "ES":
			return ESITO_PAGAMENTO;
		default: return null;
		}
	}
  }  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "Tipologia di allegato")
 /**
   * Tipologia di allegato  
  **/
  private TipoEnum tipo = null;
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "allegato codificato in base64")
 /**
   * allegato codificato in base64  
  **/
  private String testo = null;
 /**
   * Tipologia di allegato
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public String getTipo() {
    return tipo != null ? tipo.getValue() : "";
  }

  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  public Allegato tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * allegato codificato in base64
   * @return testo
  **/
  @JsonProperty("testo")
  @NotNull
  public String getTesto() {
    return testo;
  }

  public void setTesto(String testo) {
    this.testo = testo;
  }

  public Allegato testo(String testo) {
    this.testo = testo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Allegato {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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
