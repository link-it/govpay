package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"giornaleEventi",
"tracciatoCsv",
"hardening",
})
public class Configurazione extends JSONSerializable implements IValidable{
  
  @JsonProperty("giornaleEventi")
  private Giornale giornaleEventi = null;
  
  @JsonProperty("tracciatoCsv")
  private TracciatoCsv tracciatoCsv = null;
  
  @JsonProperty("hardening")
  private Hardening hardening = null;
  
  /**
   **/
  public Configurazione giornaleEventi(Giornale giornaleEventi) {
    this.giornaleEventi = giornaleEventi;
    return this;
  }

  @JsonProperty("giornaleEventi")
  public Giornale getGiornaleEventi() {
    return giornaleEventi;
  }
  public void setGiornaleEventi(Giornale giornaleEventi) {
    this.giornaleEventi = giornaleEventi;
  }

  /**
   **/
  public Configurazione tracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
    return this;
  }

  @JsonProperty("tracciatoCsv")
  public TracciatoCsv getTracciatoCsv() {
    return tracciatoCsv;
  }
  public void setTracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
  }

  /**
   **/
  public Configurazione hardening(Hardening hardening) {
    this.hardening = hardening;
    return this;
  }

  @JsonProperty("hardening")
  public Hardening getHardening() {
    return hardening;
  }
  public void setHardening(Hardening hardening) {
    this.hardening = hardening;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Configurazione configurazione = (Configurazione) o;
    return Objects.equals(giornaleEventi, configurazione.giornaleEventi) &&
        Objects.equals(tracciatoCsv, configurazione.tracciatoCsv) &&
        Objects.equals(hardening, configurazione.hardening);
  }

  @Override
  public int hashCode() {
    return Objects.hash(giornaleEventi, tracciatoCsv, hardening);
  }

  public static Configurazione parse(String json) throws ServiceException, ValidationException {
    return (Configurazione) parse(json, Configurazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "configurazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Configurazione {\n");
    
    sb.append("    giornaleEventi: ").append(toIndentedString(giornaleEventi)).append("\n");
    sb.append("    tracciatoCsv: ").append(toIndentedString(tracciatoCsv)).append("\n");
    sb.append("    hardening: ").append(toIndentedString(hardening)).append("\n");
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

  @Override
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("giornaleEventi", this.giornaleEventi).notNull().validateFields();
	vf.getValidator("tracciatoCsv", this.tracciatoCsv).notNull().validateFields();
	vf.getValidator("hardening", this.hardening).notNull().validateFields();
  }
}



