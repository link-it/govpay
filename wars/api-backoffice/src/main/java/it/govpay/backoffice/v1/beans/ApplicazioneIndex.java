package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"principal",
"abilitato",
})
public class ApplicazioneIndex extends JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   * Identificativo dell'applicazione
   **/
  public ApplicazioneIndex idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo di autenticazione
   **/
  public ApplicazioneIndex principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return this.principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public ApplicazioneIndex abilitato(Boolean abilitato) {
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
    ApplicazioneIndex applicazioneIndex = (ApplicazioneIndex) o;
    return Objects.equals(this.idA2A, applicazioneIndex.idA2A) &&
        Objects.equals(this.principal, applicazioneIndex.principal) &&
        Objects.equals(this.abilitato, applicazioneIndex.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idA2A, this.principal, this.abilitato);
  }

  public static ApplicazioneIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, ApplicazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "applicazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicazioneIndex {\n");
    
    sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
    sb.append("    principal: ").append(this.toIndentedString(this.principal)).append("\n");
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



