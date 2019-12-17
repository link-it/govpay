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
"abilitato",
"url",
"message",
})
public class ConfigurazioneAppIO extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("message")
  private ConfigurazioneMessageAppIO message = null;
  
  /**
   * Indica lo stato di abilitazione
   **/
  public ConfigurazioneAppIO abilitato(Boolean abilitato) {
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
   * URL del servizio di IO
   **/
  public ConfigurazioneAppIO url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  public ConfigurazioneAppIO message(ConfigurazioneMessageAppIO message) {
    this.message = message;
    return this;
  }

  @JsonProperty("message")
  public ConfigurazioneMessageAppIO getMessage() {
    return message;
  }
  public void setMessage(ConfigurazioneMessageAppIO message) {
    this.message = message;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneAppIO configurazioneAppIO = (ConfigurazioneAppIO) o;
    return Objects.equals(abilitato, configurazioneAppIO.abilitato) &&
        Objects.equals(url, configurazioneAppIO.url) &&
        Objects.equals(message, configurazioneAppIO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, url, message);
  }

  public static ConfigurazioneAppIO parse(String json) throws ServiceException, ValidationException {
    return (ConfigurazioneAppIO) parse(json, ConfigurazioneAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "configurazioneAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneAppIO {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
	  if(this.abilitato.booleanValue()) {
		  vf.getValidator("url", this.url).notNull().minLength(1).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES);
		  vf.getValidator("message", this.message).notNull().validateFields();
	  }
	}
}



