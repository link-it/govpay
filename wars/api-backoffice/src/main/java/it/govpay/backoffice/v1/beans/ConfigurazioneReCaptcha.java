package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"serverURL",
"siteKey",
"secretKey",
"soglia",
"parametro",
})
public class ConfigurazioneReCaptcha extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
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
  
  /**
   * Indica lo stato di abilitazione
   **/
  public ConfigurazioneReCaptcha abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneReCaptcha configurazioneReCaptcha = (ConfigurazioneReCaptcha) o;
    return Objects.equals(abilitato, configurazioneReCaptcha.abilitato) &&
        Objects.equals(serverURL, configurazioneReCaptcha.serverURL) &&
        Objects.equals(siteKey, configurazioneReCaptcha.siteKey) &&
        Objects.equals(secretKey, configurazioneReCaptcha.secretKey) &&
        Objects.equals(soglia, configurazioneReCaptcha.soglia) &&
        Objects.equals(parametro, configurazioneReCaptcha.parametro);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, serverURL, siteKey, secretKey, soglia, parametro);
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
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    serverURL: ").append(toIndentedString(serverURL)).append("\n");
    sb.append("    siteKey: ").append(toIndentedString(siteKey)).append("\n");
    sb.append("    secretKey: ").append(toIndentedString(secretKey)).append("\n");
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
    sb.append("    parametro: ").append(toIndentedString(parametro)).append("\n");
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
	  
	  vf.getValidator("abilitato", abilitato).notNull();
	  if(this.abilitato) {
		  vf.getValidator("serverURL", this.serverURL).notNull().minLength(1).isUrl();
		  vf.getValidator("siteKey", this.siteKey).notNull().minLength(1);
		  vf.getValidator("secretKey", this.secretKey).notNull().minLength(1);
		  vf.getValidator("soglia", this.soglia).notNull().min(new BigDecimal(0.1)).max(new BigDecimal(1.0));
		  vf.getValidator("parametro", this.parametro).notNull().minLength(1);
	  }
	
  }
}



