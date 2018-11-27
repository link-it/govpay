package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;


@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"appName",
"versione",
"build",
"ambiente",
})
public class InfoGovPay extends JSONSerializable {
  
  @JsonProperty("appName")
  private String appName = null;
  
  @JsonProperty("versione")
  private String versione = null;
  
  @JsonProperty("build")
  private String build = null;
  
  @JsonProperty("ambiente")
  private String ambiente = null;
  
  /**
   * Nome da visualizzare sul browser
   **/
  public InfoGovPay appName(String appName) {
    this.appName = appName;
    return this;
  }

  @JsonProperty("appName")
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }

  /**
   * Versione di Govpay installata
   **/
  public InfoGovPay versione(String versione) {
    this.versione = versione;
    return this;
  }

  @JsonProperty("versione")
  public String getVersione() {
    return versione;
  }
  public void setVersione(String versione) {
    this.versione = versione;
  }

  /**
   * Numero build di Govpay installata
   **/
  public InfoGovPay build(String build) {
    this.build = build;
    return this;
  }

  @JsonProperty("build")
  public String getBuild() {
    return build;
  }
  public void setBuild(String build) {
    this.build = build;
  }

  /**
   * ambiente destinazione del Govpay
   **/
  public InfoGovPay ambiente(String ambiente) {
    this.ambiente = ambiente;
    return this;
  }

  @JsonProperty("ambiente")
  public String getAmbiente() {
    return ambiente;
  }
  public void setAmbiente(String ambiente) {
    this.ambiente = ambiente;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfoGovPay infoGovPay = (InfoGovPay) o;
    return Objects.equals(appName, infoGovPay.appName) &&
        Objects.equals(versione, infoGovPay.versione) &&
        Objects.equals(build, infoGovPay.build) &&
        Objects.equals(ambiente, infoGovPay.ambiente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appName, versione, build, ambiente);
  }

  public static InfoGovPay parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return (InfoGovPay) parse(json, InfoGovPay.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "infoGovPay";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfoGovPay {\n");
    
    sb.append("    appName: ").append(toIndentedString(appName)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
    sb.append("    build: ").append(toIndentedString(build)).append("\n");
    sb.append("    ambiente: ").append(toIndentedString(ambiente)).append("\n");
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



