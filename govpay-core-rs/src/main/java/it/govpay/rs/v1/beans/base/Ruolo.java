package it.govpay.rs.v1.beans.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

@org.codehaus.jackson.annotate.JsonPropertyOrder({
"descrizione",
"acls",
"idRuolo",
})
public class Ruolo extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("acls")
  private List<ACL> acls = new ArrayList<ACL>();
  
  @JsonProperty("idRuolo")
  private String idRuolo = null;
  
  /**
   * descrizione del ruolo
   **/
  public Ruolo descrizione(String descrizione) {
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
  public Ruolo acls(List<ACL> acls) {
    this.acls = acls;
    return this;
  }

  @JsonProperty("acls")
  public List<ACL> getAcls() {
    return acls;
  }
  public void setAcls(List<ACL> acls) {
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
    return Objects.equals(descrizione, ruolo.descrizione) &&
        Objects.equals(acls, ruolo.acls) &&
        Objects.equals(idRuolo, ruolo.idRuolo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, acls, idRuolo);
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
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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



