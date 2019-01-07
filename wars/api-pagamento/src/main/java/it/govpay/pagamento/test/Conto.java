package it.govpay.pagamento.test;

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

public class Conto  {
  
  @Schema(example = "IT60X0542811101000000123456", required = true, description = "")
  private String iban = null;
  
  @Schema(example = "DABAIE2D", description = "")
  private String bic = null;
 /**
   * Get iban
   * @return iban
  **/
  @JsonProperty("iban")
  @NotNull
 @Pattern(regexp="[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public Conto iban(String iban) {
    this.iban = iban;
    return this;
  }

 /**
   * Get bic
   * @return bic
  **/
  @JsonProperty("bic")
 @Pattern(regexp="[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}")  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public Conto bic(String bic) {
    this.bic = bic;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Conto {\n");
    
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    bic: ").append(toIndentedString(bic)).append("\n");
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
