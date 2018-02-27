package it.govpay.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"principal",
"codificaAvvisi",
"domini",
"entrate",
"servizioVerifica",
"servizioNotifica",
"abilitato",
})
public class ApplicazionePost extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaAvvisi")
  private Object codificaAvvisi = null;
  
  @JsonProperty("domini")
  private List<Object> domini = null;
  
  @JsonProperty("entrate")
  private List<Object> entrate = null;
  
  @JsonProperty("servizioVerifica")
  private Connector servizioVerifica = null;
  
  @JsonProperty("servizioNotifica")
  private Connector servizioNotifica = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
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
   * informazioni sulla codifica e decodifica degli iuv
   **/
  public ApplicazionePost codificaAvvisi(Object codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
    return this;
  }

  @JsonProperty("codificaAvvisi")
  public Object getCodificaAvvisi() {
    return codificaAvvisi;
  }
  public void setCodificaAvvisi(Object codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public ApplicazionePost domini(List<Object> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<Object> getDomini() {
    return domini;
  }
  public void setDomini(List<Object> domini) {
    this.domini = domini;
  }

  /**
   * entrate su cui e' abilitato ad operare
   **/
  public ApplicazionePost entrate(List<Object> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<Object> getEntrate() {
    return entrate;
  }
  public void setEntrate(List<Object> entrate) {
    this.entrate = entrate;
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
        Objects.equals(servizioVerifica, applicazionePost.servizioVerifica) &&
        Objects.equals(servizioNotifica, applicazionePost.servizioNotifica) &&
        Objects.equals(abilitato, applicazionePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, codificaAvvisi, domini, entrate, servizioVerifica, servizioNotifica, abilitato);
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



