package it.govpay.ec.v2.beans;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VoceDescrizioneImporto   {
  
  @Schema(example = "canone annuo", description = "voce importo")
 /**
   * voce importo  
  **/
  private String voce = null;
  
  @Schema(example = "10.01", description = "importo")
 /**
   * importo  
  **/
  private BigDecimal importo = null;
 /**
   * voce importo
   * @return voce
  **/
  @JsonProperty("voce")
  public String getVoce() {
    return voce;
  }

  public void setVoce(String voce) {
    this.voce = voce;
  }

  public VoceDescrizioneImporto voce(String voce) {
    this.voce = voce;
    return this;
  }

 /**
   * importo
   * @return importo
  **/
  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public VoceDescrizioneImporto importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoceDescrizioneImporto {\n");
    
    sb.append("    voce: ").append(toIndentedString(voce)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
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
