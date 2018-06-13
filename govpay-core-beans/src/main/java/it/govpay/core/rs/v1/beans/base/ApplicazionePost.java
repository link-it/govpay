package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"principal",
"codificaAvvisi",
"domini",
"entrate",
"acl",
"servizioVerifica",
"servizioNotifica",
"abilitato",
})
public class ApplicazionePost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaAvvisi")
  private CodificaAvvisi codificaAvvisi = null;
  
  @JsonProperty("domini")
  private List<String> domini = null;
  
  @JsonProperty("entrate")
  private List<String> entrate = null;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("servizioVerifica")
  private Connector servizioVerifica = null;
  
  @JsonProperty("servizioNotifica")
  private Connector servizioNotifica = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   * Identificativo di autenticazione
   **/
  public ApplicazionePost principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   **/
  public ApplicazionePost codificaAvvisi(CodificaAvvisi codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
    return this;
  }

  @JsonProperty("codificaAvvisi")
  public CodificaAvvisi getCodificaAvvisi() {
    return codificaAvvisi;
  }
  public void setCodificaAvvisi(CodificaAvvisi codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public ApplicazionePost domini(List<String> domini) {
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
   * entrate su cui e' abilitato ad operare
   **/
  public ApplicazionePost entrate(List<String> entrate) {
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
  public ApplicazionePost acl(List<AclPost> acl) {
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
   **/
  public ApplicazionePost servizioVerifica(Connector servizioVerifica) {
    this.servizioVerifica = servizioVerifica;
    return this;
  }

  @JsonProperty("servizioVerifica")
  public Connector getServizioVerifica() {
    return servizioVerifica;
  }
  public void setServizioVerifica(Connector servizioVerifica) {
    this.servizioVerifica = servizioVerifica;
  }

  /**
   **/
  public ApplicazionePost servizioNotifica(Connector servizioNotifica) {
    this.servizioNotifica = servizioNotifica;
    return this;
  }

  @JsonProperty("servizioNotifica")
  public Connector getServizioNotifica() {
    return servizioNotifica;
  }
  public void setServizioNotifica(Connector servizioNotifica) {
    this.servizioNotifica = servizioNotifica;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public ApplicazionePost abilitato(Boolean abilitato) {
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
    ApplicazionePost applicazionePost = (ApplicazionePost) o;
    return Objects.equals(principal, applicazionePost.principal) &&
        Objects.equals(codificaAvvisi, applicazionePost.codificaAvvisi) &&
        Objects.equals(domini, applicazionePost.domini) &&
        Objects.equals(entrate, applicazionePost.entrate) &&
        Objects.equals(acl, applicazionePost.acl) &&
        Objects.equals(servizioVerifica, applicazionePost.servizioVerifica) &&
        Objects.equals(servizioNotifica, applicazionePost.servizioNotifica) &&
        Objects.equals(abilitato, applicazionePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, codificaAvvisi, domini, entrate, acl, servizioVerifica, servizioNotifica, abilitato);
  }

  public static ApplicazionePost parse(String json) {
    return (ApplicazionePost) parse(json, ApplicazionePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "applicazionePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicazionePost {\n");
    
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    codificaAvvisi: ").append(toIndentedString(codificaAvvisi)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    servizioVerifica: ").append(toIndentedString(servizioVerifica)).append("\n");
    sb.append("    servizioNotifica: ").append(toIndentedString(servizioNotifica)).append("\n");
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



