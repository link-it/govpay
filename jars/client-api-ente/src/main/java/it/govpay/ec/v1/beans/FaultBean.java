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
package it.govpay.ec.v1.beans;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


public class FaultBean  {
  public enum CategoriaEnum {
    AUTORIZZAZIONE("AUTORIZZAZIONE"),
    RICHIESTA("RICHIESTA"),
    OPERAZIONE("OPERAZIONE"),
    EC("EC"),
    PAGOPA("PAGOPA"),
    INTERNO("INTERNO");

    private String value;

    CategoriaEnum(String value) {
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
    public static CategoriaEnum fromValue(String text) {
      for (CategoriaEnum b : CategoriaEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }  
  // @Schema(requiredMode = RequiredMode.REQUIRED, description = "Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `INTERNO` - Errore interno")
 /**
   * Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `INTERNO` - Errore interno  
  **/
  private CategoriaEnum categoria = null;
  
  // @Schema(requiredMode = RequiredMode.REQUIRED, description = "Codice di errore")
 /**
   * Codice di errore  
  **/
  private String codice = null;
  
  // @Schema(requiredMode = RequiredMode.REQUIRED, description = "Descrizione dell'errore")
 /**
   * Descrizione dell'errore  
  **/
  private String descrizione = null;
  
  // @Schema(description = "Descrizione aggiuntiva")
 /**
   * Descrizione aggiuntiva  
  **/
  private String dettaglio = null;
 /**
   * Categoria dell&#x27;errore riscontrato:  * &#x60;AUTORIZZAZIONE&#x60; - Operazione non autorizzata  * &#x60;RICHIESTA&#x60; - Richiesta non valida  * &#x60;OPERAZIONE&#x60; - Operazione non eseguibile  * &#x60;PAGOPA&#x60; - Errore da PagoPA  * &#x60;INTERNO&#x60; - Errore interno
   * @return categoria
  **/
  @JsonProperty("categoria")
  @NotNull
  @Valid
  public CategoriaEnum getCategoria() {
    return categoria;
  }

  public void setCategoria(CategoriaEnum categoria) {
    this.categoria = categoria;
  }

  public FaultBean categoria(CategoriaEnum categoria) {
    this.categoria = categoria;
    return this;
  }

 /**
   * Codice di errore
   * @return codice
  **/
  @JsonProperty("codice")
  @NotNull
  @Valid
  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public FaultBean codice(String codice) {
    this.codice = codice;
    return this;
  }

 /**
   * Descrizione dell&#x27;errore
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
  @Valid
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public FaultBean descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Descrizione aggiuntiva
   * @return dettaglio
  **/
  @JsonProperty("dettaglio")
  @Valid
  public String getDettaglio() {
    return dettaglio;
  }

  public void setDettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
  }

  public FaultBean dettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FaultBean {\n");
    
    sb.append("    categoria: ").append(toIndentedString(categoria)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
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
