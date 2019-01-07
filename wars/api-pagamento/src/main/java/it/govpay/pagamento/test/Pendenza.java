package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.Pagamenti;
import it.govpay.pagamento.v2.beans.PendenzaBase;
import it.govpay.pagamento.v2.beans.Rpps;
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

public class Pendenza extends PendenzaBase {
  
  @Schema(required = true, description = "")
  private Rpps rpps = null;
  
  @Schema(required = true, description = "")
  private Pagamenti pagamenti = null;
 /**
   * Get rpps
   * @return rpps
  **/
  @JsonProperty("rpps")
  @NotNull
  public Rpps getRpps() {
    return rpps;
  }

  public void setRpps(Rpps rpps) {
    this.rpps = rpps;
  }

  public Pendenza rpps(Rpps rpps) {
    this.rpps = rpps;
    return this;
  }

 /**
   * Get pagamenti
   * @return pagamenti
  **/
  @JsonProperty("pagamenti")
  @NotNull
  public Pagamenti getPagamenti() {
    return pagamenti;
  }

  public void setPagamenti(Pagamenti pagamenti) {
    this.pagamenti = pagamenti;
  }

  public Pendenza pagamenti(Pagamenti pagamenti) {
    this.pagamenti = pagamenti;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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
