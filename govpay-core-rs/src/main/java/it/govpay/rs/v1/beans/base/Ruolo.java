package it.govpay.rs.v1.beans.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"acls",
"idRuolo",
})
public class Ruolo extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("acls")
  private List<Object> acls = new ArrayList<Object>();
  
  @JsonProperty("idRuolo")
  private String idRuolo = null;
  
  /**
   **/
  public Ruolo acls(List<Object> acls) {
    this.acls = acls;
    return this;
  }

  @JsonProperty("acls")
  public List<Object> getAcls() {
    return acls;
  }
  public void setAcls(List<Object> acls) {
    this.acls = acls;
  }

  /**
   * Nome identificativo del ruolo
   **/
  public Ruolo idRuolo(String idRuolo) {
    this.idRuolo = idRuolo;
    return this;
  }

  @JsonProperty("idRuolo")
  public String getIdRuolo() {
    return idRuolo;
  }
  public void setIdRuolo(String idRuolo) {
    this.idRuolo = idRuolo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ruolo ruolo = (Ruolo) o;
    return Objects.equals(acls, ruolo.acls) &&
        Objects.equals(idRuolo, ruolo.idRuolo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acls, idRuolo);
  }

  public static Ruolo parse(String json) {
    return (Ruolo) parse(json, Ruolo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ruolo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ruolo {\n");
    
    sb.append("    acls: ").append(toIndentedString(acls)).append("\n");
    sb.append("    idRuolo: ").append(toIndentedString(idRuolo)).append("\n");
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



