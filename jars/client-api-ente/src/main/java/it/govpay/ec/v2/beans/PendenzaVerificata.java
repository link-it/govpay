package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class PendenzaVerificata extends NuovaPendenza  {
  
  @Schema(description = "")
  private StatoPendenzaVerificata stato = null;
 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  public StatoPendenzaVerificata getStato() {
    return stato;
  }

  public void setStato(StatoPendenzaVerificata stato) {
    this.stato = stato;
  }

  public PendenzaVerificata stato(StatoPendenzaVerificata stato) {
    this.stato = stato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaVerificata {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
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
