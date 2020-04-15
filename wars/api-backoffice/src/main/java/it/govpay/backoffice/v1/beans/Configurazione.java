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
"mailBatch",
"avvisaturaMail",
"avvisaturaAppIO",
"appIOBatch",
})
public class Configurazione extends JSONSerializable implements IValidable{
  
  @JsonProperty("giornaleEventi")
  private Giornale giornaleEventi = null;
  
  @JsonProperty("tracciatoCsv")
  private TracciatoCsv tracciatoCsv = null;
  
  @JsonProperty("hardening")
  private Hardening hardening = null;
  
  @JsonProperty("mailBatch")
  private MailBatch mailBatch = null;
  
  @JsonProperty("avvisaturaMail")
  private ConfigurazioneAvvisaturaMail avvisaturaMail = null;
  
  @JsonProperty("avvisaturaAppIO")
  private ConfigurazioneAvvisaturaAppIO avvisaturaAppIO = null;
  
  @JsonProperty("appIOBatch")
  private AppIOBatch appIOBatch = null;
  
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

  /**
   **/
  public Configurazione mailBatch(MailBatch mailBatch) {
    this.mailBatch = mailBatch;
    return this;
  }

  @JsonProperty("mailBatch")
  public MailBatch getMailBatch() {
    return mailBatch;
  }
  public void setMailBatch(MailBatch mailBatch) {
    this.mailBatch = mailBatch;
  }

  /**
   **/
  public Configurazione avvisaturaMail(ConfigurazioneAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
    return this;
  }

  @JsonProperty("avvisaturaMail")
  public ConfigurazioneAvvisaturaMail getAvvisaturaMail() {
    return avvisaturaMail;
  }
  public void setAvvisaturaMail(ConfigurazioneAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
  }

  /**
   **/
  public Configurazione avvisaturaAppIO(ConfigurazioneAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
    return this;
  }

  @JsonProperty("avvisaturaAppIO")
  public ConfigurazioneAvvisaturaAppIO getAvvisaturaAppIO() {
    return avvisaturaAppIO;
  }
  public void setAvvisaturaAppIO(ConfigurazioneAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
  }

  /**
   **/
  public Configurazione appIOBatch(AppIOBatch appIOBatch) {
    this.appIOBatch = appIOBatch;
    return this;
  }

  @JsonProperty("appIOBatch")
  public AppIOBatch getAppIOBatch() {
    return appIOBatch;
  }
  public void setAppIOBatch(AppIOBatch appIOBatch) {
    this.appIOBatch = appIOBatch;
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
        Objects.equals(hardening, configurazione.hardening) &&
        Objects.equals(mailBatch, configurazione.mailBatch) &&
        Objects.equals(avvisaturaMail, configurazione.avvisaturaMail) &&
        Objects.equals(avvisaturaAppIO, configurazione.avvisaturaAppIO) &&
        Objects.equals(appIOBatch, configurazione.appIOBatch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(giornaleEventi, tracciatoCsv, hardening, mailBatch, avvisaturaMail, avvisaturaAppIO, appIOBatch);
  }

  public static Configurazione parse(String json) throws ServiceException, ValidationException {
    return parse(json, Configurazione.class);
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
    sb.append("    mailBatch: ").append(toIndentedString(mailBatch)).append("\n");
    sb.append("    avvisaturaMail: ").append(toIndentedString(avvisaturaMail)).append("\n");
    sb.append("    avvisaturaAppIO: ").append(toIndentedString(avvisaturaAppIO)).append("\n");
    sb.append("    appIOBatch: ").append(toIndentedString(appIOBatch)).append("\n");
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
	vf.getValidator("mailBatch", this.mailBatch).notNull().validateFields();
	vf.getValidator("avvisaturaMail", this.avvisaturaMail).notNull().validateFields();
	vf.getValidator("avvisaturaAppIO", this.avvisaturaAppIO).notNull().validateFields();	
	vf.getValidator("appIOBatch", this.appIOBatch).notNull().validateFields();	
  }
}



