package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idPsp",
"ragioneSociale",
"bollo",
"storno",
"canali",
"abilitato",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Psp extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("bollo")
  private Boolean bollo = null;
  
  @JsonProperty("storno")
  private Boolean storno = null;
  
  @JsonProperty("canali")
  private String canali = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * identificativo del PSP
   **/
  public Psp idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  /**
   * identificativo del PSP
   **/
  public Psp ragioneSociale(String ragioneSociale) {
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
   **/
  public Psp bollo(Boolean bollo) {
    this.bollo = bollo;
    return this;
  }

  @JsonProperty("bollo")
  public Boolean isBollo() {
    return bollo;
  }
  public void setBollo(Boolean bollo) {
    this.bollo = bollo;
  }

  /**
   **/
  public Psp storno(Boolean storno) {
    this.storno = storno;
    return this;
  }

  @JsonProperty("storno")
  public Boolean isStorno() {
    return storno;
  }
  public void setStorno(Boolean storno) {
    this.storno = storno;
  }

  /**
   * URL per acquisire la lista dei canali
   **/
  public Psp canali(String canali) {
    this.canali = canali;
    return this;
  }

  @JsonProperty("canali")
  public String getCanali() {
    return canali;
  }
  public void setCanali(String canali) {
    this.canali = canali;
  }

  /**
   **/
  public Psp abilitato(Boolean abilitato) {
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
    Psp psp = (Psp) o;
    return Objects.equals(idPsp, psp.idPsp) &&
        Objects.equals(ragioneSociale, psp.ragioneSociale) &&
        Objects.equals(bollo, psp.bollo) &&
        Objects.equals(storno, psp.storno) &&
        Objects.equals(canali, psp.canali) &&
        Objects.equals(abilitato, psp.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPsp, ragioneSociale, bollo, storno, canali, abilitato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Psp {\n");
    
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    bollo: ").append(toIndentedString(bollo)).append("\n");
    sb.append("    storno: ").append(toIndentedString(storno)).append("\n");
    sb.append("    canali: ").append(toIndentedString(canali)).append("\n");
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



