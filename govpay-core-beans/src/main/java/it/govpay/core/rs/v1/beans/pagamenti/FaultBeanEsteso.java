package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
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
	    
	            
	    INTERNO("INTERNO");
	            
	        
	    

	    private String value;

	    CategoriaEnum(String value) {
	      this.value = value;
	    }

	    @Override
	    @org.codehaus.jackson.annotate.JsonValue
	    public String toString() {
	      return String.valueOf(value);
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
    return categoria;
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
    return codice;
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
    return descrizione;
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
    return dettaglio;
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
    return id;
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
    return location;
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
    return Objects.equals(codice, faultBeanEsteso.codice) &&
        Objects.equals(descrizione, faultBeanEsteso.descrizione) &&
        Objects.equals(dettaglio, faultBeanEsteso.dettaglio) &&
        Objects.equals(id, faultBeanEsteso.id) &&
        Objects.equals(location, faultBeanEsteso.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, dettaglio, id, location);
  }

  public static FaultBeanEsteso parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (FaultBeanEsteso) parse(json, FaultBeanEsteso.class);
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



