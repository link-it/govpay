package it.govpay.ragioneria.v3.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Riconciliazione extends RiconciliazioneIndex  {
  
  @Schema(description = "")
  private List<Riscossione> riscossioni = null;
 /**
   * Get riscossioni
   * @return riscossioni
  **/
  @JsonProperty("riscossioni")
  public List<Riscossione> getRiscossioni() {
    return riscossioni;
  }

  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  public Riconciliazione riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  public Riconciliazione addRiscossioniItem(Riscossione riscossioniItem) {
    this.riscossioni.add(riscossioniItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riconciliazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
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
