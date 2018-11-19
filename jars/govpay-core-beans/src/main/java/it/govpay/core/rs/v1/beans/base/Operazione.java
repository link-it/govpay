package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idOperazione",
"descrizione",
"location",
"stato",
"esito",
"dettaglio",
})
public class Operazione extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idOperazione")
  private String idOperazione = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("location")
  private String location = null;
  
    
  /**
   * Gets or Sets stato
   */
  public enum StatoEnum {
    
    
        
            
    _0("0"),
    
            
    _1("1"),
    
            
    _2("2");
            
        
    

    private String value;

    StatoEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return this.value;
    }

    public static StatoEnum fromValue(String text) {
      for (StatoEnum b : StatoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("stato")
  private StatoEnum stato = null;
  
  @JsonProperty("esito")
  private String esito = null;
  
  @JsonProperty("dettaglio")
  private String dettaglio = null;
  
  /**
   **/
  public Operazione idOperazione(String idOperazione) {
    this.idOperazione = idOperazione;
    return this;
  }

  @JsonProperty("idOperazione")
  public String getIdOperazione() {
    return this.idOperazione;
  }
  public void setIdOperazione(String idOperazione) {
    this.idOperazione = idOperazione;
  }

  /**
   **/
  public Operazione descrizione(String descrizione) {
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
   **/
  public Operazione location(String location) {
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

  /**
   **/
  public Operazione stato(StatoEnum stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoEnum getStato() {
    return this.stato;
  }
  public void setStato(StatoEnum stato) {
    this.stato = stato;
  }

  /**
   * Descrizione dell'esito dell'esecuzione
   **/
  public Operazione esito(String esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public String getEsito() {
    return this.esito;
  }
  public void setEsito(String esito) {
    this.esito = esito;
  }

  /**
   **/
  public Operazione dettaglio(String dettaglio) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Operazione operazione = (Operazione) o;
    return Objects.equals(this.idOperazione, operazione.idOperazione) &&
        Objects.equals(this.descrizione, operazione.descrizione) &&
        Objects.equals(this.location, operazione.location) &&
        Objects.equals(this.stato, operazione.stato) &&
        Objects.equals(this.esito, operazione.esito) &&
        Objects.equals(this.dettaglio, operazione.dettaglio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idOperazione, this.descrizione, this.location, this.stato, this.esito, this.dettaglio);
  }

  public static Operazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Operazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operazione {\n");
    sb.append("    ").append(this.toIndentedString(super.toString())).append("\n");
    sb.append("    idOperazione: ").append(this.toIndentedString(this.idOperazione)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    location: ").append(this.toIndentedString(this.location)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    esito: ").append(this.toIndentedString(this.esito)).append("\n");
    sb.append("    dettaglio: ").append(this.toIndentedString(this.dettaglio)).append("\n");
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



