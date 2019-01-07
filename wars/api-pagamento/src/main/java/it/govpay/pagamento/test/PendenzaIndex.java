package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.PendenzaBase;
import it.govpay.pagamento.v2.beans.Segnalazione;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import it.govpay.pagamento.v2.beans.UnitaOperativa;
import it.govpay.pagamento.v2.beans.VocePendenza;
import java.math.BigDecimal;
import java.util.List;
import org.joda.time.LocalDate;
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

public class PendenzaIndex extends PendenzaBase {
  
  @Schema(required = true, description = "Url della pendenza")
 /**
   * Url della pendenza  
  **/
  private String href = null;
  
  @Schema(description = "Url per l'elenco delle rpp emesse per la pendenza")
 /**
   * Url per l'elenco delle rpp emesse per la pendenza  
  **/
  private String rpps = null;
  
  @Schema(required = true, description = "Url per l'elenco dei pagamenti da portale comprensivi della pendenza")
 /**
   * Url per l'elenco dei pagamenti da portale comprensivi della pendenza  
  **/
  private String pagamenti = null;
 /**
   * Url della pendenza
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

  public PendenzaIndex href(String href) {
    this.href = href;
    return this;
  }

 /**
   * Url per l&#x27;elenco delle rpp emesse per la pendenza
   * @return rpps
  **/
  @JsonProperty("rpps")
  public String getRpps() {
    return rpps;
  }

  public void setRpps(String rpps) {
    this.rpps = rpps;
  }

  public PendenzaIndex rpps(String rpps) {
    this.rpps = rpps;
    return this;
  }

 /**
   * Url per l&#x27;elenco dei pagamenti da portale comprensivi della pendenza
   * @return pagamenti
  **/
  @JsonProperty("pagamenti")
  @NotNull
  public String getPagamenti() {
    return pagamenti;
  }

  public void setPagamenti(String pagamenti) {
    this.pagamenti = pagamenti;
  }

  public PendenzaIndex pagamenti(String pagamenti) {
    this.pagamenti = pagamenti;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaIndex {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    rpps: ").append(toIndentedString(rpps)).append("\n");
    sb.append("    pagamenti: ").append(toIndentedString(pagamenti)).append("\n");
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
