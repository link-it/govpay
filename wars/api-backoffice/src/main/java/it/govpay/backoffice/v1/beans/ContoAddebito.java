package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;

/**
 * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"iban",
"bic",
})
public class ContoAddebito extends JSONSerializable {

  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("bic")
  private String bic = null;

  /**
   * Iban di addebito del pagatore.
   **/
  public ContoAddebito iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return this.iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   * Bic della banca di addebito del pagatore.
   **/
  public ContoAddebito bic(String bic) {
    this.bic = bic;
    return this;
  }

  @JsonProperty("bic")
  public String getBic() {
    return this.bic;
  }
  public void setBic(String bic) {
    this.bic = bic;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ContoAddebito contoAddebito = (ContoAddebito) o;
    return Objects.equals(this.iban, contoAddebito.iban) &&
        Objects.equals(this.bic, contoAddebito.bic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.iban, this.bic);
  }

  public static ContoAddebito parse(String json) throws IOException {
    return parse(json, ContoAddebito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contoAddebito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContoAddebito {\n");

    sb.append("    iban: ").append(this.toIndentedString(this.iban)).append("\n");
    sb.append("    bic: ").append(this.toIndentedString(this.bic)).append("\n");
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



