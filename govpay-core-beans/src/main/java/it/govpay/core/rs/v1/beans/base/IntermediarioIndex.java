package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idIntermediario",
"denominazione",
"abilitato",
})
public class IntermediarioIndex extends JSONSerializable {
  
  @JsonProperty("idIntermediario")
  private String idIntermediario = null;
  
  @JsonProperty("denominazione")
  private String denominazione = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Identificativo dell'intermediario
   **/
  public IntermediarioIndex idIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
    return this;
  }

  @JsonProperty("idIntermediario")
  public String getIdIntermediario() {
    return idIntermediario;
  }
  public void setIdIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
  }

  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public IntermediarioIndex denominazione(String denominazione) {
    this.denominazione = denominazione;
    return this;
  }

  @JsonProperty("denominazione")
  public String getDenominazione() {
    return denominazione;
  }
  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public IntermediarioIndex abilitato(Boolean abilitato) {
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
    IntermediarioIndex intermediarioIndex = (IntermediarioIndex) o;
    return Objects.equals(idIntermediario, intermediarioIndex.idIntermediario) &&
        Objects.equals(denominazione, intermediarioIndex.denominazione) &&
        Objects.equals(abilitato, intermediarioIndex.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idIntermediario, denominazione, abilitato);
  }

  public static IntermediarioIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (IntermediarioIndex) parse(json, IntermediarioIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "intermediarioIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntermediarioIndex {\n");
    
    sb.append("    idIntermediario: ").append(toIndentedString(idIntermediario)).append("\n");
    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
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



