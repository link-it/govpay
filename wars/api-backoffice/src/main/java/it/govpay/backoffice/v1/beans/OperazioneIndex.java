package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idOperazione",
"descrizione",
"location",
})
public class OperazioneIndex extends it.govpay.core.beans.JSONSerializable {
  
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
    return this.idOperazione;
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
    return this.descrizione;
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
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    OperazioneIndex operazioneIndex = (OperazioneIndex) o;
    return Objects.equals(this.idOperazione, operazioneIndex.idOperazione) &&
        Objects.equals(this.descrizione, operazioneIndex.descrizione) &&
        Objects.equals(this.location, operazioneIndex.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idOperazione, this.descrizione, this.location);
  }

  public static OperazioneIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, OperazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazioneIndex {\n");
    
    sb.append("    idOperazione: ").append(this.toIndentedString(this.idOperazione)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    location: ").append(this.toIndentedString(this.location)).append("\n");
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



