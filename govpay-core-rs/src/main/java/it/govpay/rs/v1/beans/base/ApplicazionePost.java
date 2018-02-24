package it.govpay.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"principal",
"codificaAvvisi",
"versioneApi",
"servizioVerifica",
"servizioNotifica",
"abilitato",
})
public class ApplicazionePost extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaAvvisi")
  private Object codificaAvvisi = null;
  
    
  /**
   * Versione delle API di integrazione utilizzate
   */
  public enum VersioneApiEnum {
    
    
        
            
    REST_1_0("REST_1.0"),
    
            
    SOAP_2_0("SOAP_2.0"),
    
            
    SOAP_2_1("SOAP_2.1"),
    
            
    SOAP_2_3("SOAP_2.3"),
    
            
    SOAP_2_5("SOAP_2.5");
            
        
    

    private String value;

    VersioneApiEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static VersioneApiEnum fromValue(String text) {
      for (VersioneApiEnum b : VersioneApiEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("versioneApi")
  private VersioneApiEnum versioneApi = null;
  
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
   * Versione delle API di integrazione utilizzate
   **/
  public ApplicazionePost versioneApi(VersioneApiEnum versioneApi) {
    this.versioneApi = versioneApi;
    return this;
  }

  @JsonProperty("versioneApi")
  public VersioneApiEnum getVersioneApi() {
    return versioneApi;
  }
  public void setVersioneApi(VersioneApiEnum versioneApi) {
    this.versioneApi = versioneApi;
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
        Objects.equals(versioneApi, applicazionePost.versioneApi) &&
        Objects.equals(servizioVerifica, applicazionePost.servizioVerifica) &&
        Objects.equals(servizioNotifica, applicazionePost.servizioNotifica) &&
        Objects.equals(abilitato, applicazionePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, codificaAvvisi, versioneApi, servizioVerifica, servizioNotifica, abilitato);
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
    sb.append("    versioneApi: ").append(toIndentedString(versioneApi)).append("\n");
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



