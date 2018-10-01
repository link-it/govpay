package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"principal",
"ragioneSociale",
"abilitato",
})
public class OperatoreIndex extends JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Username dell'operatore
   **/
  public OperatoreIndex principal(String principal) {
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
   * Nome e cognome dell'operatore
   **/
  public OperatoreIndex ragioneSociale(String ragioneSociale) {
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
   * Indicazione se l'operatore è abilitato ad operare sulla piattaforma
   **/
  public OperatoreIndex abilitato(Boolean abilitato) {
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
    OperatoreIndex operatoreIndex = (OperatoreIndex) o;
    return Objects.equals(this.principal, operatoreIndex.principal) &&
        Objects.equals(this.ragioneSociale, operatoreIndex.ragioneSociale) &&
        Objects.equals(this.abilitato, operatoreIndex.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.principal, this.ragioneSociale, this.abilitato);
  }

  public static OperatoreIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, OperatoreIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatoreIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatoreIndex {\n");
    
    sb.append("    principal: ").append(this.toIndentedString(this.principal)).append("\n");
    sb.append("    ragioneSociale: ").append(this.toIndentedString(this.ragioneSociale)).append("\n");
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



