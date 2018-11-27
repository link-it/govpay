package it.govpay.pagamento.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"codice",
"descrizione",
})
public class Segnalazione extends JSONSerializable {
  
  @JsonProperty("codice")
  private String codice = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  /**
   **/
  public Segnalazione codice(String codice) {
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
   **/
  public Segnalazione descrizione(String descrizione) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Segnalazione segnalazione = (Segnalazione) o;
    return Objects.equals(this.codice, segnalazione.codice) &&
        Objects.equals(this.descrizione, segnalazione.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.codice, this.descrizione);
  }

  public static Segnalazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Segnalazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "segnalazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Segnalazione {\n");
    
    sb.append("    codice: ").append(this.toIndentedString(this.codice)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
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



