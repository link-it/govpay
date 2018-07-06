package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
})
public class RuoloIndex extends JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  /**
   * identificativo del ruolo
   **/
  public RuoloIndex id(String id) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RuoloIndex ruoloIndex = (RuoloIndex) o;
    return Objects.equals(id, ruoloIndex.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public static RuoloIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (RuoloIndex) parse(json, RuoloIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ruoloIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RuoloIndex {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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



