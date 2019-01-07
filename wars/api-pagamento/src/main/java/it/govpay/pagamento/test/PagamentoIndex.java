package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.PagamentoBase;
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

public class PagamentoIndex extends PagamentoBase {
  
  @Schema(required = true, description = "URL del pagamento")
 /**
   * URL del pagamento  
  **/
  private String href = null;
  
  @Schema(required = true, description = "URL della lista delle pendenze oggetto del pagamento")
 /**
   * URL della lista delle pendenze oggetto del pagamento  
  **/
  private String pendenze = null;
  
  @Schema(description = "URL della lista delle richieste di pagamento attivate")
 /**
   * URL della lista delle richieste di pagamento attivate  
  **/
  private String rpp = null;
 /**
   * URL del pagamento
   * @return href
  **/
  @JsonProperty("href")
  @NotNull
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public PagamentoIndex href(String href) {
    this.href = href;
    return this;
  }

 /**
   * URL della lista delle pendenze oggetto del pagamento
   * @return pendenze
  **/
  @JsonProperty("pendenze")
  @NotNull
  public String getPendenze() {
    return pendenze;
  }

  public void setPendenze(String pendenze) {
    this.pendenze = pendenze;
  }

  public PagamentoIndex pendenze(String pendenze) {
    this.pendenze = pendenze;
    return this;
  }

 /**
   * URL della lista delle richieste di pagamento attivate
   * @return rpp
  **/
  @JsonProperty("rpp")
  public String getRpp() {
    return rpp;
  }

  public void setRpp(String rpp) {
    this.rpp = rpp;
  }

  public PagamentoIndex rpp(String rpp) {
    this.rpp = rpp;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoIndex {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
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
