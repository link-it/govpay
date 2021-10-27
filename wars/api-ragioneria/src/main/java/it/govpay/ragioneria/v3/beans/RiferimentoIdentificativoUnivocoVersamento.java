package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Identificativo univoco di versamento.
 **/
@Schema(description="Identificativo univoco di versamento.")
public class RiferimentoIdentificativoUnivocoVersamento  implements OneOfTipoRiferimentoNuovaRiconciliazione, OneOfTipoRiferimentoRiconciliazione  {
  
  @Schema(example = "RF23567483937849450550875", required = true, description = "")
  private String iuv = null;
 /**
   * Get iuv
   * @return iuv
  **/
  @JsonProperty("iuv")
  @NotNull
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public RiferimentoIdentificativoUnivocoVersamento iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoIdentificativoUnivocoVersamento {\n");
    
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
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
