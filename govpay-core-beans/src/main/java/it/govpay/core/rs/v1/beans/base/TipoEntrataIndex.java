package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idEntrata",
"descrizione",
"location",
})
public class TipoEntrataIndex extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idEntrata")
  private String idEntrata = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("location")
  private String location = null;
  
  /**
   **/
  public TipoEntrataIndex idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  @JsonProperty("idEntrata")
  public String getIdEntrata() {
    return idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  /**
   **/
  public TipoEntrataIndex descrizione(String descrizione) {
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
  public TipoEntrataIndex location(String location) {
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
    TipoEntrataIndex tipoEntrataIndex = (TipoEntrataIndex) o;
    return Objects.equals(idEntrata, tipoEntrataIndex.idEntrata) &&
        Objects.equals(descrizione, tipoEntrataIndex.descrizione) &&
        Objects.equals(location, tipoEntrataIndex.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEntrata, descrizione, location);
  }

  public static TipoEntrataIndex parse(String json) {
    return (TipoEntrataIndex) parse(json, TipoEntrataIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoEntrataIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrataIndex {\n");
    
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
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



