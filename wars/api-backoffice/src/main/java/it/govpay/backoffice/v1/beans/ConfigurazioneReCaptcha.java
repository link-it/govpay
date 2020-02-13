package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"serverURL",
"siteKey",
"secretKey",
"soglia",
"parametro",
"denyOnFail",
"connectionTimeout",
"readTimeout",
})
public class ConfigurazioneReCaptcha extends JSONSerializable implements IValidable {
  
  @JsonProperty("serverURL")
  private String serverURL = null;
  
  @JsonProperty("siteKey")
  private String siteKey = null;
  
  @JsonProperty("secretKey")
  private String secretKey = null;
  
  @JsonProperty("soglia")
  private BigDecimal soglia = null;
  
  @JsonProperty("parametro")
  private String parametro = null;
  
  @JsonProperty("denyOnFail")
  private Boolean denyOnFail = null;
  
  @JsonProperty("connectionTimeout")
  private BigDecimal connectionTimeout = null;
  
  @JsonProperty("readTimeout")
  private BigDecimal readTimeout = null;
  
  /**
   * URL del servizio di verifica
   **/
  public ConfigurazioneReCaptcha serverURL(String serverURL) {
    this.serverURL = serverURL;
    return this;
  }

  @JsonProperty("serverURL")
  public String getServerURL() {
    return serverURL;
  }
  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  /**
   * chiave sito
   **/
  public ConfigurazioneReCaptcha siteKey(String siteKey) {
    this.siteKey = siteKey;
    return this;
  }

  @JsonProperty("siteKey")
  public String getSiteKey() {
    return siteKey;
  }
  public void setSiteKey(String siteKey) {
    this.siteKey = siteKey;
  }

  /**
   * chiave segreta di configurazione per l'applicazione
   **/
  public ConfigurazioneReCaptcha secretKey(String secretKey) {
    this.secretKey = secretKey;
    return this;
  }

  @JsonProperty("secretKey")
  public String getSecretKey() {
    return secretKey;
  }
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  /**
   * Soglia di accettazione della risposta di validazione
   **/
  public ConfigurazioneReCaptcha soglia(BigDecimal soglia) {
    this.soglia = soglia;
    return this;
  }

  @JsonProperty("soglia")
  public BigDecimal getSoglia() {
    return soglia;
  }
  public void setSoglia(BigDecimal soglia) {
    this.soglia = soglia;
  }

  /**
   * nome del parametro da utilizzare per leggere la response da inviare al server
   **/
  public ConfigurazioneReCaptcha parametro(String parametro) {
    this.parametro = parametro;
    return this;
  }

  @JsonProperty("parametro")
  public String getParametro() {
    return parametro;
  }
  public void setParametro(String parametro) {
    this.parametro = parametro;
  }

  /**
   * Indica se autorizzare la richiesta in caso di errore di raggiungibilita' del server
   **/
  public ConfigurazioneReCaptcha denyOnFail(Boolean denyOnFail) {
    this.denyOnFail = denyOnFail;
    return this;
  }

  @JsonProperty("denyOnFail")
  public Boolean DenyOnFail() {
    return denyOnFail;
  }
  public void setDenyOnFail(Boolean denyOnFail) {
    this.denyOnFail = denyOnFail;
  }

  /**
   * Valore di ConnectionTimeout da impostare nella connessione con il server
   **/
  public ConfigurazioneReCaptcha connectionTimeout(BigDecimal connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
    return this;
  }

  @JsonProperty("connectionTimeout")
  public BigDecimal getConnectionTimeout() {
    return connectionTimeout;
  }
  public void setConnectionTimeout(BigDecimal connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  /**
   * Valore di ReadTimeout da impostare nella connessione con il server
   **/
  public ConfigurazioneReCaptcha readTimeout(BigDecimal readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  @JsonProperty("readTimeout")
  public BigDecimal getReadTimeout() {
    return readTimeout;
  }
  public void setReadTimeout(BigDecimal readTimeout) {
    this.readTimeout = readTimeout;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneReCaptcha configurazioneReCaptcha = (ConfigurazioneReCaptcha) o;
    return Objects.equals(serverURL, configurazioneReCaptcha.serverURL) &&
        Objects.equals(siteKey, configurazioneReCaptcha.siteKey) &&
        Objects.equals(secretKey, configurazioneReCaptcha.secretKey) &&
        Objects.equals(soglia, configurazioneReCaptcha.soglia) &&
        Objects.equals(parametro, configurazioneReCaptcha.parametro) &&
        Objects.equals(denyOnFail, configurazioneReCaptcha.denyOnFail) &&
        Objects.equals(connectionTimeout, configurazioneReCaptcha.connectionTimeout) &&
        Objects.equals(readTimeout, configurazioneReCaptcha.readTimeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverURL, siteKey, secretKey, soglia, parametro, denyOnFail, connectionTimeout, readTimeout);
  }

  public static ConfigurazioneReCaptcha parse(String json) throws ServiceException, ValidationException {
    return (ConfigurazioneReCaptcha) parse(json, ConfigurazioneReCaptcha.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "configurazioneReCaptcha";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneReCaptcha {\n");
    
    sb.append("    serverURL: ").append(toIndentedString(serverURL)).append("\n");
    sb.append("    siteKey: ").append(toIndentedString(siteKey)).append("\n");
    sb.append("    secretKey: ").append(toIndentedString(secretKey)).append("\n");
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
    sb.append("    parametro: ").append(toIndentedString(parametro)).append("\n");
    sb.append("    denyOnFail: ").append(toIndentedString(denyOnFail)).append("\n");
    sb.append("    connectionTimeout: ").append(toIndentedString(connectionTimeout)).append("\n");
    sb.append("    readTimeout: ").append(toIndentedString(readTimeout)).append("\n");
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
	  
	  vf.getValidator("serverURL", this.serverURL).notNull().minLength(1).isUrl();
	  vf.getValidator("siteKey", this.siteKey).notNull().minLength(1);
	  vf.getValidator("secretKey", this.secretKey).notNull().minLength(1);
	  vf.getValidator("soglia", this.soglia).notNull().min(BigDecimal.ZERO).max(BigDecimal.ONE);
	  vf.getValidator("parametro", this.parametro).notNull().minLength(1).pattern(CostantiValidazione.PATTERN_G_RECAPTCHA_RESPONSE);
	  vf.getValidator("denyOnFail", denyOnFail).notNull();
	  vf.getValidator("connectionTimeout", connectionTimeout).notNull().min(BigDecimal.ZERO);
	  vf.getValidator("readTimeout", readTimeout).notNull().min(BigDecimal.ZERO);
  }
}



