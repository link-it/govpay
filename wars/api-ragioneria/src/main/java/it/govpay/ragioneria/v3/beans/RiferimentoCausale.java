package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera.
 **/
@Schema(description="Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera.")
public class RiferimentoCausale  implements OneOfTipoRiferimentoNuovaRiconciliazione  {
  
  @Schema(example = "/PUR/LGPE-RIVERSAMENTO/URI/2017-01-01ABI00000011234", required = true, description = "")
  private String causale = null;
 /**
   * Get causale
   * @return causale
  **/
  @JsonProperty("causale")
  @NotNull
  public String getCausale() {
    return causale;
  }

  public void setCausale(String causale) {
    this.causale = causale;
  }

  public RiferimentoCausale causale(String causale) {
    this.causale = causale;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoCausale {\n");
    
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
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
