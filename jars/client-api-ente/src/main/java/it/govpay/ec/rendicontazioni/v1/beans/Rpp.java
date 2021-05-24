package it.govpay.ec.rendicontazioni.v1.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Rpp  {
  
  @Schema(description = "Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico")
 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico  
  **/
  private byte[] rpt = null;
  
  @Schema(required = true, description = "Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica")
 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica  
  **/
  private byte[] rt = null;
 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico
   * @return rpt
  **/
  @JsonProperty("rpt")
  @Valid
  public byte[] getRpt() {
    return rpt;
  }

  public void setRpt(byte[] rpt) {
    this.rpt = rpt;
  }

  public Rpp rpt(byte[] rpt) {
    this.rpt = rpt;
    return this;
  }

 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   * @return rt
  **/
  @JsonProperty("rt")
  @NotNull
  @Valid
  public byte[] getRt() {
    return rt;
  }

  public void setRt(byte[] rt) {
    this.rt = rt;
  }

  public Rpp rt(byte[] rt) {
    this.rt = rt;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rpp {\n");
    
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
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
