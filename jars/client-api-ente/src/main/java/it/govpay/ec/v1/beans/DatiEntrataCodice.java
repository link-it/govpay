package it.govpay.ec.v1.beans;

import javax.validation.Valid;
//import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
 **/
// @Schema(description="Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.")
public class DatiEntrataCodice  {
  
  // @Schema(required = true, description = "")
  private String codEntrata = null;
 /**
   * Get codEntrata
   * @return codEntrata
  **/
  @JsonProperty("codEntrata")
  @NotNull
  @Valid
  public String getCodEntrata() {
    return codEntrata;
  }

  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public DatiEntrataCodice codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiEntrataCodice {\n");
    
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
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
