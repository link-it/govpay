package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"denominazione",
"principalPagoPa",
"servizioPagoPa",
"servizioFtp",
"abilitato",
})
public class IntermediarioPost extends it.govpay.core.beans.JSONSerializable {
  
  @JsonProperty("denominazione")
  private String denominazione = null;
  
  @JsonProperty("principalPagoPa")
  private String principalPagoPa = null;
  
  @JsonProperty("servizioPagoPa")
  private ConnettorePagopa servizioPagoPa = null;
  
  @JsonProperty("servizioFtp")
  private ServizioFtp servizioFtp = null;
  
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
    return this.denominazione;
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
    return this.principalPagoPa;
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
    return this.servizioPagoPa;
  }
  public void setServizioPagoPa(ConnettorePagopa servizioPagoPa) {
    this.servizioPagoPa = servizioPagoPa;
  }

  /**
   **/
  public IntermediarioPost servizioFtp(ServizioFtp servizioFtp) {
    this.servizioFtp = servizioFtp;
    return this;
  }

  @JsonProperty("servizioFtp")
  public ServizioFtp getServizioFtp() {
    return this.servizioFtp;
  }
  public void setServizioFtp(ServizioFtp servizioFtp) {
    this.servizioFtp = servizioFtp;
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
    IntermediarioPost intermediarioPost = (IntermediarioPost) o;
    return Objects.equals(this.denominazione, intermediarioPost.denominazione) &&
        Objects.equals(this.principalPagoPa, intermediarioPost.principalPagoPa) &&
        Objects.equals(this.servizioPagoPa, intermediarioPost.servizioPagoPa) &&
        Objects.equals(this.servizioFtp, intermediarioPost.servizioFtp) &&
        Objects.equals(this.abilitato, intermediarioPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.denominazione, this.principalPagoPa, this.servizioPagoPa, this.servizioFtp, this.abilitato);
  }

  public static IntermediarioPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, IntermediarioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "intermediarioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntermediarioPost {\n");
    
    sb.append("    denominazione: ").append(this.toIndentedString(this.denominazione)).append("\n");
    sb.append("    principalPagoPa: ").append(this.toIndentedString(this.principalPagoPa)).append("\n");
    sb.append("    servizioPagoPa: ").append(this.toIndentedString(this.servizioPagoPa)).append("\n");
    sb.append("    servizioFtp: ").append(this.toIndentedString(this.servizioFtp)).append("\n");
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



