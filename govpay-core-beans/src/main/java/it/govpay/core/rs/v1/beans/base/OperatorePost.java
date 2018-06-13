package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ragioneSociale",
"domini",
"entrate",
"acl",
"abilitato",
})
public class OperatorePost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("domini")
  private List<String> domini = null;
  
  @JsonProperty("entrate")
  private List<String> entrate = null;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Nome e cognome dell'operatore
   **/
  public OperatorePost ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  @JsonProperty("ragioneSociale")
  public String getRagioneSociale() {
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  /**
   * domini su cui e' abilitato ad operare. Se la lista e' vuota, l'abilitazione e' per tutti i domini
   **/
  public OperatorePost domini(List<String> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<String> getDomini() {
    return domini;
  }
  public void setDomini(List<String> domini) {
    this.domini = domini;
  }

  /**
   * entrate su cui e' abilitato ad operare. Se la lista e' vuota, l'abilitazione e' per tutte le entrate
   **/
  public OperatorePost entrate(List<String> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<String> getEntrate() {
    return entrate;
  }
  public void setEntrate(List<String> entrate) {
    this.entrate = entrate;
  }

  /**
   * lista delle acl attive sull'operatore
   **/
  public OperatorePost acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  /**
   * Indicazione se l'operatore è abilitato ad operare sulla piattaforma
   **/
  public OperatorePost abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperatorePost operatorePost = (OperatorePost) o;
    return Objects.equals(ragioneSociale, operatorePost.ragioneSociale) &&
        Objects.equals(domini, operatorePost.domini) &&
        Objects.equals(entrate, operatorePost.entrate) &&
        Objects.equals(acl, operatorePost.acl) &&
        Objects.equals(abilitato, operatorePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, domini, entrate, acl, abilitato);
  }

  public static OperatorePost parse(String json) {
    return (OperatorePost) parse(json, OperatorePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatorePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorePost {\n");
    
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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



