package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idOperazione",
"descrizione",
"location",
})
public class OperazioneIndex extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idOperazione")
  private String idOperazione = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("location")
  private String location = null;
  
  /**
   **/
  public OperazioneIndex idOperazione(String idOperazione) {
    this.idOperazione = idOperazione;
    return this;
  }

  @JsonProperty("idOperazione")
  public String getIdOperazione() {
    return idOperazione;
  }
  public void setIdOperazione(String idOperazione) {
    this.idOperazione = idOperazione;
  }

  /**
   **/
  public OperazioneIndex descrizione(String descrizione) {
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
   **/
  public OperazioneIndex location(String location) {
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
    OperazioneIndex operazioneIndex = (OperazioneIndex) o;
    return Objects.equals(idOperazione, operazioneIndex.idOperazione) &&
        Objects.equals(descrizione, operazioneIndex.descrizione) &&
        Objects.equals(location, operazioneIndex.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idOperazione, descrizione, location);
  }

  public static OperazioneIndex parse(String json) {
    return (OperazioneIndex) parse(json, OperazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazioneIndex {\n");
    
    sb.append("    idOperazione: ").append(toIndentedString(idOperazione)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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



