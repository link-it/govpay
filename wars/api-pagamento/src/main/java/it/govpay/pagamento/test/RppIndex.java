package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.RppBase;
import it.govpay.pagamento.v2.beans.Segnalazione;
import java.util.List;
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

public class RppIndex extends RppBase {
  
  @Schema(required = true, description = "URL della rpp")
 /**
   * URL della rpp  
  **/
  private String href = null;
  
  @Schema(required = true, description = "Url alla pendenza oggetto della richiesta di pagamento")
 /**
   * Url alla pendenza oggetto della richiesta di pagamento  
  **/
  private String pendenza = null;
 /**
   * URL della rpp
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

  public RppIndex href(String href) {
    this.href = href;
    return this;
  }

 /**
   * Url alla pendenza oggetto della richiesta di pagamento
   * @return pendenza
  **/
  @JsonProperty("pendenza")
  @NotNull
  public String getPendenza() {
    return pendenza;
  }

  public void setPendenza(String pendenza) {
    this.pendenza = pendenza;
  }

  public RppIndex pendenza(String pendenza) {
    this.pendenza = pendenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RppIndex {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
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
