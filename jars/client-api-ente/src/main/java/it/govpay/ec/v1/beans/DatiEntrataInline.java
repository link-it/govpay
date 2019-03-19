package it.govpay.ec.v1.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Definisce i dettagli di incasso.
 **/
// @Schema(description="Definisce i dettagli di incasso.")
public class DatiEntrataInline  {
  
  // @Schema(example = "IT60X0542811101000000123456", required = true, description = "")
  private String ibanAccredito = null;
  
  // @Schema(example = "DABAIE2D", description = "")
  private String bicAccredito = null;
  
  // @Schema(example = "IT60X0542811101000000123456", description = "")
  private String ibanAppoggio = null;
  
  // @Schema(example = "DABAIE2D", description = "")
  private String bicAppoggio = null;
  
  // @Schema(required = true, description = "")
  private TipoContabilita tipoContabilita = null;
  
  // @Schema(example = "3321", required = true, description = "Codifica del capitolo di bilancio")
 /**
   * Codifica del capitolo di bilancio  
  **/
  private String codiceContabilita = null;
 /**
   * Get ibanAccredito
   * @return ibanAccredito
  **/
  @JsonProperty("ibanAccredito")
  @NotNull
  @Valid
  public String getIbanAccredito() {
    return ibanAccredito;
  }

  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public DatiEntrataInline ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

 /**
   * Get bicAccredito
   * @return bicAccredito
  **/
  @JsonProperty("bicAccredito")
  @Valid
  public String getBicAccredito() {
    return bicAccredito;
  }

  public void setBicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
  }

  public DatiEntrataInline bicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
    return this;
  }

 /**
   * Get ibanAppoggio
   * @return ibanAppoggio
  **/
  @JsonProperty("ibanAppoggio")
  @Valid
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }

  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  public DatiEntrataInline ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

 /**
   * Get bicAppoggio
   * @return bicAppoggio
  **/
  @JsonProperty("bicAppoggio")
  @Valid
  public String getBicAppoggio() {
    return bicAppoggio;
  }

  public void setBicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
  }

  public DatiEntrataInline bicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
    return this;
  }

 /**
   * Get tipoContabilita
   * @return tipoContabilita
  **/
  @JsonProperty("tipoContabilita")
  @NotNull
  @Valid
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }

  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public DatiEntrataInline tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

 /**
   * Codifica del capitolo di bilancio
   * @return codiceContabilita
  **/
  @JsonProperty("codiceContabilita")
  @NotNull
  @Valid
  public String getCodiceContabilita() {
    return codiceContabilita;
  }

  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public DatiEntrataInline codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiEntrataInline {\n");
    
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    bicAccredito: ").append(toIndentedString(bicAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    bicAppoggio: ").append(toIndentedString(bicAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
