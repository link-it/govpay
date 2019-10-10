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
"captcha",
})
public class Hardening extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("captcha")
  private ConfigurazioneReCaptcha captcha = null;
  
  /**
   * Indica lo stato di abilitazione
   **/
  public Hardening abilitato(Boolean abilitato) {
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
   **/
  public Hardening captcha(ConfigurazioneReCaptcha captcha) {
    this.captcha = captcha;
    return this;
  }

  @JsonProperty("captcha")
  public ConfigurazioneReCaptcha getCaptcha() {
    return captcha;
  }
  public void setCaptcha(ConfigurazioneReCaptcha captcha) {
    this.captcha = captcha;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Hardening hardening = (Hardening) o;
    return Objects.equals(abilitato, hardening.abilitato) &&
        Objects.equals(captcha, hardening.captcha);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, captcha);
  }

  public static Hardening parse(String json) throws ServiceException, ValidationException {
    return (Hardening) parse(json, Hardening.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "hardening";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Hardening {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    captcha: ").append(toIndentedString(captcha)).append("\n");
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
	  vf.getValidator("captcha", captcha).validateFields();
	
  }
}



