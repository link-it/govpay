package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
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
    return principal;
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
    return ragioneSociale;
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
    OperatoreIndex operatoreIndex = (OperatoreIndex) o;
    return Objects.equals(principal, operatoreIndex.principal) &&
        Objects.equals(ragioneSociale, operatoreIndex.ragioneSociale) &&
        Objects.equals(abilitato, operatoreIndex.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, ragioneSociale, abilitato);
  }

  public static OperatoreIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (OperatoreIndex) parse(json, OperatoreIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatoreIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatoreIndex {\n");
    
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
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



