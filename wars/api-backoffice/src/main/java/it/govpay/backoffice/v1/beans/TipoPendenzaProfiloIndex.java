package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTipoPendenza",
"descrizione",
})
public class TipoPendenzaProfiloIndex extends JSONSerializable {
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  /**
   **/
  public TipoPendenzaProfiloIndex idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   **/
  public TipoPendenzaProfiloIndex descrizione(String descrizione) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaProfiloIndex tipoPendenzaProfiloIndex = (TipoPendenzaProfiloIndex) o;
    return Objects.equals(idTipoPendenza, tipoPendenzaProfiloIndex.idTipoPendenza) &&
        Objects.equals(descrizione, tipoPendenzaProfiloIndex.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, descrizione);
  }

  public static TipoPendenzaProfiloIndex parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaProfiloIndex) parse(json, TipoPendenzaProfiloIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaProfiloIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaProfiloIndex {\n");
    
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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



