package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
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
    return this.ragioneSociale;
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
    return this.domini;
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
    return this.entrate;
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
    return this.acl;
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
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    OperatorePost operatorePost = (OperatorePost) o;
    return Objects.equals(this.ragioneSociale, operatorePost.ragioneSociale) &&
        Objects.equals(this.domini, operatorePost.domini) &&
        Objects.equals(this.entrate, operatorePost.entrate) &&
        Objects.equals(this.acl, operatorePost.acl) &&
        Objects.equals(this.abilitato, operatorePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ragioneSociale, this.domini, this.entrate, this.acl, this.abilitato);
  }

  public static OperatorePost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, OperatorePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatorePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorePost {\n");
    
    sb.append("    ragioneSociale: ").append(this.toIndentedString(this.ragioneSociale)).append("\n");
    sb.append("    domini: ").append(this.toIndentedString(this.domini)).append("\n");
    sb.append("    entrate: ").append(this.toIndentedString(this.entrate)).append("\n");
    sb.append("    acl: ").append(this.toIndentedString(this.acl)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
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



