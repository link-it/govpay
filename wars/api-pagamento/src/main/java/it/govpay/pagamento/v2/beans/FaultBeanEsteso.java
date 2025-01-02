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
package it.govpay.pagamento.v2.beans;

import java.util.Objects;

import it.govpay.core.exceptions.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"categoria",
"codice",
"descrizione",
"dettaglio",
"id",
"location",
})
public class FaultBeanEsteso extends JSONSerializable {
  
	  /**
	   * Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `INTERNO` - Errore interno
	   */
	  public enum CategoriaEnum {
	    
	    
	        
	            
	    AUTORIZZAZIONE("AUTORIZZAZIONE"),
	    
	            
	    RICHIESTA("RICHIESTA"),
	    
	            
	    OPERAZIONE("OPERAZIONE"),
	    
	            
	    PAGOPA("PAGOPA"),


	    EC("EC"),
	    
	            
	    INTERNO("INTERNO");
	            
	        
	    

	    private String value;

	    CategoriaEnum(String value) {
	      this.value = value;
	    }

	    @Override
	    @com.fasterxml.jackson.annotation.JsonValue
	    public String toString() {
	      return String.valueOf(this.value);
	    }

	    public static CategoriaEnum fromValue(String text) {
	      for (CategoriaEnum b : CategoriaEnum.values()) {
	        if (String.valueOf(b.value).equals(text)) {
	          return b;
	        }
	      }
	      return null;
	    }
	  }

	    
	    
  @JsonProperty("categoria")
  private CategoriaEnum categoria = null;
	  
  @JsonProperty("codice")
  private String codice = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("dettaglio")
  private String dettaglio = null;
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("location")
  private String location = null;
  
  /**
   * Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `INTERNO` - Errore interno
   **/
  public FaultBeanEsteso categoria(CategoriaEnum categoria) {
    this.categoria = categoria;
    return this;
  }

  @JsonProperty("categoria")
  public CategoriaEnum getCategoria() {
    return this.categoria;
  }
  public void setCategoria(CategoriaEnum categoria) {
    this.categoria = categoria;
  }

  /**
   * Codice di errore
   **/
  public FaultBeanEsteso codice(String codice) {
    this.codice = codice;
    return this;
  }

  @JsonProperty("codice")
  public String getCodice() {
    return this.codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   * Descrizione dell'errore
   **/
  public FaultBeanEsteso descrizione(String descrizione) {
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
   * Descrizione aggiuntiva
   **/
  public FaultBeanEsteso dettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
    return this;
  }

  @JsonProperty("dettaglio")
  public String getDettaglio() {
    return this.dettaglio;
  }
  public void setDettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
  }

  /**
   * identificativo del pagamento creato
   **/
  public FaultBeanEsteso id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Url del dettaglio del pagamento
   **/
  public FaultBeanEsteso location(String location) {
    this.location = location;
    return this;
  }

  @JsonProperty("location")
  public String getLocation() {
    return this.location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FaultBeanEsteso faultBeanEsteso = (FaultBeanEsteso) o;
    return Objects.equals(categoria, faultBeanEsteso.categoria) &&
        Objects.equals(codice, faultBeanEsteso.codice) &&
        Objects.equals(descrizione, faultBeanEsteso.descrizione) &&
        Objects.equals(dettaglio, faultBeanEsteso.dettaglio) &&
        Objects.equals(id, faultBeanEsteso.id) &&
        Objects.equals(location, faultBeanEsteso.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoria, codice, descrizione, dettaglio, id, location);
  }

  public static FaultBeanEsteso parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, FaultBeanEsteso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "faultBeanEsteso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FaultBeanEsteso {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    categoria: ").append(toIndentedString(categoria)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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



