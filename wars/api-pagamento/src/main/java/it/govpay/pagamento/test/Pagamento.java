package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.PagamentoBase;
import it.govpay.pagamento.v2.beans.Pendenze;
import it.govpay.pagamento.v2.beans.Rpps;
import it.govpay.pagamento.v2.beans.StatoPagamento;
import java.math.BigDecimal;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagamento extends PagamentoBase {
  
  @Schema(required = true, description = "")
  private Pendenze pendenze = null;
  
  @Schema(description = "")
  private Rpps rpps = null;
 /**
   * Get pendenze
   * @return pendenze
  **/
  @JsonProperty("pendenze")
  @NotNull
  public Pendenze getPendenze() {
    return pendenze;
  }

  public void setPendenze(Pendenze pendenze) {
    this.pendenze = pendenze;
  }

  public Pagamento pendenze(Pendenze pendenze) {
    this.pendenze = pendenze;
    return this;
  }

 /**
   * Get rpps
   * @return rpps
  **/
  @JsonProperty("rpps")
  public Rpps getRpps() {
    return rpps;
  }

  public void setRpps(Rpps rpps) {
    this.rpps = rpps;
  }

  public Pagamento rpps(Rpps rpps) {
    this.rpps = rpps;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pagamento {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
    sb.append("    rpps: ").append(toIndentedString(rpps)).append("\n");
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
