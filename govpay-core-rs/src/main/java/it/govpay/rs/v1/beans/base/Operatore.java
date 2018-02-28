package it.govpay.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ragioneSociale",
"domini",
"entrate",
"abilitato",
"principal",
})
public class Operatore extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("domini")
  private List<Object> domini = null;
  
  @JsonProperty("entrate")
  private List<Object> entrate = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  /**
   * Nome e cognome dell'operatore
   **/
  public Operatore ragioneSociale(String ragioneSociale) {
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
   * domini su cui e' abilitato ad operare
   **/
  public Operatore domini(List<Object> domini) {
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
  public Operatore entrate(List<Object> entrate) {
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
   * Indicazione se l'operatore è abilitato ad operare sulla piattaforma
   **/
  public Operatore abilitato(Boolean abilitato) {
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

  /**
   * Username dell'operatore
   **/
  public Operatore principal(String principal) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Operatore operatore = (Operatore) o;
    return Objects.equals(ragioneSociale, operatore.ragioneSociale) &&
        Objects.equals(domini, operatore.domini) &&
        Objects.equals(entrate, operatore.entrate) &&
        Objects.equals(abilitato, operatore.abilitato) &&
        Objects.equals(principal, operatore.principal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, domini, entrate, abilitato, principal);
  }

  public static Operatore parse(String json) {
    return (Operatore) parse(json, Operatore.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatore";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operatore {\n");
    
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
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



