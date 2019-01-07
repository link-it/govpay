package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Conto;
import it.govpay.pagamento.v2.beans.NuovoPagamentoBase;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.TipoAutenticazioneSoggetto;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;

import javax.validation.Valid;
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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class NuovoPagamento extends NuovoPagamentoBase {
  
  @Schema(required = true, description = "pendenze o riferimenti alle pendenze oggetto del pagamento")
 /**
   * pendenze o riferimenti alle pendenze oggetto del pagamento  
  **/
  @Valid
  @JsonDeserialize(using = CustomPendenzeDeserializer.class)
  private List<Object> pendenze = new ArrayList<Object>();
 /**
   * pendenze o riferimenti alle pendenze oggetto del pagamento
   * @return pendenze
  **/
  @JsonProperty("pendenze")
  @NotNull
 @Size(min=1)  public List<Object> getPendenze() {
    return pendenze;
  }

  public void setPendenze(List<Object> pendenze) {
    this.pendenze = pendenze;
  }

  public NuovoPagamento pendenze(List<Object> pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  public NuovoPagamento addPendenzeItem(Object pendenzeItem) {
    this.pendenze.add(pendenzeItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoPagamento {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
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
