package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"principal",
"codificaAvvisi",
"domini",
"entrate",
"acl",
"servizioVerifica",
"servizioNotifica",
"abilitato",
})
public class Applicazione extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaAvvisi")
  private CodificaAvvisi codificaAvvisi = null;
  
  @JsonProperty("domini")
  private List<DominioIndex> domini = null;
  
  @JsonProperty("entrate")
  private List<TipoEntrata> entrate = null;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("servizioVerifica")
  private Connector servizioVerifica = null;
  
  @JsonProperty("servizioNotifica")
  private Connector servizioNotifica = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   * Identificativo dell'applicazione
   **/
  public Applicazione idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo di autenticazione
   **/
  public Applicazione principal(String principal) {
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
  public Applicazione codificaAvvisi(CodificaAvvisi codificaAvvisi) {
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
  public Applicazione domini(List<DominioIndex> domini) {
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
  public Applicazione entrate(List<TipoEntrata> entrate) {
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
   * lista delle acl attive sull'applicazione
   **/
  public Applicazione acl(List<AclPost> acl) {
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
  public Applicazione servizioVerifica(Connector servizioVerifica) {
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
  public Applicazione servizioNotifica(Connector servizioNotifica) {
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
  public Applicazione abilitato(Boolean abilitato) {
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
    Applicazione applicazione = (Applicazione) o;
    return Objects.equals(idA2A, applicazione.idA2A) &&
        Objects.equals(principal, applicazione.principal) &&
        Objects.equals(codificaAvvisi, applicazione.codificaAvvisi) &&
        Objects.equals(domini, applicazione.domini) &&
        Objects.equals(entrate, applicazione.entrate) &&
        Objects.equals(acl, applicazione.acl) &&
        Objects.equals(servizioVerifica, applicazione.servizioVerifica) &&
        Objects.equals(servizioNotifica, applicazione.servizioNotifica) &&
        Objects.equals(abilitato, applicazione.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, principal, codificaAvvisi, domini, entrate, acl, servizioVerifica, servizioNotifica, abilitato);
  }

  public static Applicazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (Applicazione) parse(json, Applicazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "applicazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Applicazione {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
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



