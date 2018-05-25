package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
 **/@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ibanAddebito",
"bicAddebito",
})
public class ContoAddebito extends JSONSerializable {
  
  @JsonProperty("ibanAddebito")
  private String ibanAddebito = null;
  
  @JsonProperty("bicAddebito")
  private String bicAddebito = null;
  
  /**
   * Iban di addebito del pagatore.
   **/
  public ContoAddebito ibanAddebito(String ibanAddebito) {
    this.ibanAddebito = ibanAddebito;
    return this;
  }

  @JsonProperty("ibanAddebito")
  public String getIbanAddebito() {
    return ibanAddebito;
  }
  public void setIbanAddebito(String ibanAddebito) {
    this.ibanAddebito = ibanAddebito;
  }

  /**
   * Bic della banca di addebito del pagatore.
   **/
  public ContoAddebito bicAddebito(String bicAddebito) {
    this.bicAddebito = bicAddebito;
    return this;
  }

  @JsonProperty("bicAddebito")
  public String getBicAddebito() {
    return bicAddebito;
  }
  public void setBicAddebito(String bicAddebito) {
    this.bicAddebito = bicAddebito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContoAddebito contoAddebito = (ContoAddebito) o;
    return Objects.equals(ibanAddebito, contoAddebito.ibanAddebito) &&
        Objects.equals(bicAddebito, contoAddebito.bicAddebito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanAddebito, bicAddebito);
  }

  public static ContoAddebito parse(String json) {
    return (ContoAddebito) parse(json, ContoAddebito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contoAddebito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContoAddebito {\n");
    
    sb.append("    ibanAddebito: ").append(toIndentedString(ibanAddebito)).append("\n");
    sb.append("    bicAddebito: ").append(toIndentedString(bicAddebito)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}



