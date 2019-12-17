package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"subject",
"body",
})
public class ConfigurazioneGenerazioneMessageAppIO extends JSONSerializable implements IValidable {
  
    
  /**
   * Indica il tipo di template da applicare per attualizzare la richiesta
   */
  public enum TipoEnum {
    
    
        
            
    FREEMARKER("freemarker");
            
        
    

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  private TipoEnum tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("subject")
  private Object subject = null;
  
  @JsonProperty("body")
  private Object body = null;
  
  /**
   * Indica il tipo di template da applicare per attualizzare la richiesta
   **/
  public ConfigurazioneGenerazioneMessageAppIO tipo(TipoEnum tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  @JsonIgnore
  public TipoEnum getTipoEnum() {
    return tipoEnum;
  }
  public void setTipo(TipoEnum tipoEnum) {
    this.tipoEnum = tipoEnum;
  }
  
  public ConfigurazioneGenerazioneMessageAppIO tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
  


  /**
   * Template di trasformazione da applicare per ottenere l'oggetto da inserire nella richiesta
   **/
  public ConfigurazioneGenerazioneMessageAppIO subject(Object subject) {
    this.subject = subject;
    return this;
  }

  @JsonProperty("subject")
  public Object getSubject() {
    return subject;
  }
  public void setSubject(Object subject) {
    this.subject = subject;
  }

  /**
   * Template di trasformazione da applicare per ottenere il messaggio da inserire nella richiesta
   **/
  public ConfigurazioneGenerazioneMessageAppIO body(Object body) {
    this.body = body;
    return this;
  }

  @JsonProperty("body")
  public Object getBody() {
    return body;
  }
  public void setBody(Object body) {
    this.body = body;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneGenerazioneMessageAppIO configurazioneGenerazioneMessageAppIO = (ConfigurazioneGenerazioneMessageAppIO) o;
    return Objects.equals(tipo, configurazioneGenerazioneMessageAppIO.tipo) &&
        Objects.equals(subject, configurazioneGenerazioneMessageAppIO.subject) &&
        Objects.equals(body, configurazioneGenerazioneMessageAppIO.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, subject, body);
  }

  public static ConfigurazioneGenerazioneMessageAppIO parse(String json) throws ServiceException, ValidationException {
    return (ConfigurazioneGenerazioneMessageAppIO) parse(json, ConfigurazioneGenerazioneMessageAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "configurazioneGenerazioneMessageAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneGenerazioneMessageAppIO {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
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
		
	  vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
	  
	  if((this.tipo != null && this.subject == null) || (this.tipo == null && this.body != null)) {
		  throw new ValidationException("I campi 'subject' e 'body' devono essere entrambi valorizzati per definire il field 'message'.");
	  }
	}
}



