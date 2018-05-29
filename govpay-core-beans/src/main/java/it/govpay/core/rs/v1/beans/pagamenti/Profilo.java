package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"domini",
"entrate",
"acl",
})
public class Profilo extends JSONSerializable {
  
  @JsonProperty("domini")
  private List<DominioIndex> domini = new ArrayList<DominioIndex>();
  
  @JsonProperty("entrate")
  private List<TipoEntrata> entrate = new ArrayList<TipoEntrata>();
  
  @JsonProperty("acl")
  private List<Acl> acl = new ArrayList<Acl>();
  
  /**
   * domini su cui e' abilitato ad operare
   **/
  public Profilo domini(List<DominioIndex> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<DominioIndex> getDomini() {
    return domini;
  }
  public void setDomini(List<DominioIndex> domini) {
    this.domini = domini;
  }

  /**
   * entrate su cui e' abilitato ad operare
   **/
  public Profilo entrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<TipoEntrata> getEntrate() {
    return entrate;
  }
  public void setEntrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
  }

  /**
   **/
  public Profilo acl(List<Acl> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<Acl> getAcl() {
    return acl;
  }
  public void setAcl(List<Acl> acl) {
    this.acl = acl;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(domini, profilo.domini) &&
        Objects.equals(entrate, profilo.entrate) &&
        Objects.equals(acl, profilo.acl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(domini, entrate, acl);
  }

  public static Profilo parse(String json) {
    return (Profilo) parse(json, Profilo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "profilo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");
    
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
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



