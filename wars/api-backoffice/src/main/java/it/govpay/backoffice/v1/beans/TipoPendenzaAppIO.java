package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"apiKey",
"message",
})
public class TipoPendenzaAppIO extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = false;
  
  @JsonProperty("apiKey")
  private String apiKey = null;
  
  @JsonProperty("message")
  private ConfigurazioneGenerazioneMessageAppIO message = null;
  
  /**
   * indica se la spedizione del messaggio e' prevista per questo tipo pendenza.
   **/
  public TipoPendenzaAppIO abilitato(Boolean abilitato) {
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
   * La API Key per le invocazioni.
   **/
  public TipoPendenzaAppIO apiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  @JsonProperty("apiKey")
  public String getApiKey() {
    return apiKey;
  }
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   **/
  public TipoPendenzaAppIO message(ConfigurazioneGenerazioneMessageAppIO message) {
    this.message = message;
    return this;
  }

  @JsonProperty("message")
  public ConfigurazioneGenerazioneMessageAppIO getMessage() {
    return message;
  }
  public void setMessage(ConfigurazioneGenerazioneMessageAppIO message) {
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
    TipoPendenzaAppIO tipoPendenzaAppIO = (TipoPendenzaAppIO) o;
    return Objects.equals(abilitato, tipoPendenzaAppIO.abilitato) &&
        Objects.equals(apiKey, tipoPendenzaAppIO.apiKey) &&
        Objects.equals(message, tipoPendenzaAppIO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, apiKey, message);
  }

  public static TipoPendenzaAppIO parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAppIO) parse(json, TipoPendenzaAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAppIO {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    apiKey: ").append(toIndentedString(apiKey)).append("\n");
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
	 this.validate(true);
  }

  public void validate(boolean abilitatoObbligatorio) throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		if(abilitatoObbligatorio)
			vf.getValidator("abilitato", abilitato).notNull();
		vf.getValidator("apiKey", this.apiKey).minLength(1).maxLength(35);
		vf.getValidator("message", this.message).validateFields();
  }
}



