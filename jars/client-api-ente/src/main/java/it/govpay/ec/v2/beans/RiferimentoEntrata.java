package it.govpay.ec.v2.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
  * Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
 **/
@Schema(description="Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.")
public class RiferimentoEntrata  implements OneOfTipoRiferimentoVocePendenza  {
  
  @Schema(example = "SRV-12345", requiredMode = RequiredMode.REQUIRED, description = "")
  private String codEntrata = null;
 /**
   * Get codEntrata
   * @return codEntrata
  **/
  @JsonProperty("codEntrata")
  @NotNull
 @Pattern(regexp="(^[a-zA-Z0-9\\-_\\.]{1,35}$)")  public String getCodEntrata() {
    return codEntrata;
  }

  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public RiferimentoEntrata codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoEntrata {\n");
    
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
