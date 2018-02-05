package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import java.util.ArrayList;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idRuolo",
"acls",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Ruolo extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idRuolo")
  private String idRuolo = null;
  
  @JsonProperty("acls")
  private List<Object> acls = null;
  
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ruolo ruolo = (Ruolo) o;
    return Objects.equals(idRuolo, ruolo.idRuolo) &&
        Objects.equals(acls, ruolo.acls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idRuolo, acls);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ruolo {\n");
    
    sb.append("    idRuolo: ").append(toIndentedString(idRuolo)).append("\n");
    sb.append("    acls: ").append(toIndentedString(acls)).append("\n");
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



