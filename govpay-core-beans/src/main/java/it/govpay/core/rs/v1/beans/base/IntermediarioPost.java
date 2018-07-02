package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;

@org.codehaus.jackson.annotate.JsonPropertyOrder({
"denominazione",
"principalPagoPa",
"servizioPagoPa",
"abilitato",
})
public class IntermediarioPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("denominazione")
  private String denominazione = null;
  
  @JsonProperty("principalPagoPa")
  private String principalPagoPa = null;
  
  @JsonProperty("servizioPagoPa")
  private ConnettorePagopa servizioPagoPa = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public IntermediarioPost denominazione(String denominazione) {
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
   * Principal autenticato le richieste ricevute da PagoPA
   **/
  public IntermediarioPost principalPagoPa(String principalPagoPa) {
    this.principalPagoPa = principalPagoPa;
    return this;
  }

  @JsonProperty("principalPagoPa")
  public String getPrincipalPagoPa() {
    return principalPagoPa;
  }
  public void setPrincipalPagoPa(String principalPagoPa) {
    this.principalPagoPa = principalPagoPa;
  }

  /**
   **/
  public IntermediarioPost servizioPagoPa(ConnettorePagopa servizioPagoPa) {
    this.servizioPagoPa = servizioPagoPa;
    return this;
  }

  @JsonProperty("servizioPagoPa")
  public ConnettorePagopa getServizioPagoPa() {
    return servizioPagoPa;
  }
  public void setServizioPagoPa(ConnettorePagopa servizioPagoPa) {
    this.servizioPagoPa = servizioPagoPa;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public IntermediarioPost abilitato(Boolean abilitato) {
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
    IntermediarioPost intermediarioPost = (IntermediarioPost) o;
    return Objects.equals(denominazione, intermediarioPost.denominazione) &&
        Objects.equals(principalPagoPa, intermediarioPost.principalPagoPa) &&
        Objects.equals(servizioPagoPa, intermediarioPost.servizioPagoPa) &&
        Objects.equals(abilitato, intermediarioPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(denominazione, principalPagoPa, servizioPagoPa, abilitato);
  }

  public static IntermediarioPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (IntermediarioPost) parse(json, IntermediarioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "intermediarioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntermediarioPost {\n");
    
    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
    sb.append("    principalPagoPa: ").append(toIndentedString(principalPagoPa)).append("\n");
    sb.append("    servizioPagoPa: ").append(toIndentedString(servizioPagoPa)).append("\n");
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



