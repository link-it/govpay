package it.govpay.user.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"categoria",
"codice",
"descrizione",
"dettaglio",
})
public class FaultBean extends JSONSerializable {
  
    
  /**
   * Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `EC` - Errore da Ente Creditore  * `INTERNO` - Errore interno
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
  
  /**
   * Categoria dell'errore riscontrato:  * `AUTORIZZAZIONE` - Operazione non autorizzata  * `RICHIESTA` - Richiesta non valida  * `OPERAZIONE` - Operazione non eseguibile  * `PAGOPA` - Errore da PagoPA  * `EC` - Errore da Ente Creditore  * `INTERNO` - Errore interno
   **/
  public FaultBean categoria(CategoriaEnum categoria) {
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
  public FaultBean codice(String codice) {
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
  public FaultBean descrizione(String descrizione) {
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
  public FaultBean dettaglio(String dettaglio) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FaultBean faultBean = (FaultBean) o;
    return Objects.equals(categoria, faultBean.categoria) &&
        Objects.equals(codice, faultBean.codice) &&
        Objects.equals(descrizione, faultBean.descrizione) &&
        Objects.equals(dettaglio, faultBean.dettaglio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoria, codice, descrizione, dettaglio);
  }

  public static FaultBean parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, FaultBean.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "faultBean";
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}



