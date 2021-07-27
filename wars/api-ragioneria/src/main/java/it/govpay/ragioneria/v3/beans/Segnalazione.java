package it.govpay.ragioneria.v3.beans;

import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"data",
"codice",
"descrizione",
"dettaglio",
})
public class Segnalazione extends JSONSerializable {
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("codice")
  private String codice = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("dettaglio")
  private String dettaglio = null;
  
  /**
   **/
  public Segnalazione data(Date data) {
    this.data = data;
    return this;
  }

  @JsonProperty("data")
  public Date getData() {
    return data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   **/
  public Segnalazione codice(String codice) {
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
   **/
  public Segnalazione descrizione(String descrizione) {
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
  public Segnalazione dettaglio(String dettaglio) {
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
    Segnalazione segnalazione = (Segnalazione) o;
    return Objects.equals(data, segnalazione.data) &&
        Objects.equals(codice, segnalazione.codice) &&
        Objects.equals(descrizione, segnalazione.descrizione) &&
        Objects.equals(dettaglio, segnalazione.dettaglio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, codice, descrizione, dettaglio);
  }

  public static Segnalazione parse(String json) throws ServiceException, ValidationException {
    return (Segnalazione) parse(json, Segnalazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "segnalazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Segnalazione {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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



