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
"oggetto",
"messaggio",
"allegaPdf",
})
public class TipoPendenzaPromemoria extends JSONSerializable implements IValidable{
  
    
  /**
   * Indica il tipo di template da applicare per attualizzare il promemoria
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
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("allegaPdf")
  private Boolean allegaPdf = true;
  
  /**
   * Indica il tipo di template da applicare per attualizzare il promemoria
   **/
  public TipoPendenzaPromemoria tipo(TipoEnum tipo) {
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
  
  public TipoPendenzaPromemoria tipo(String tipo) {
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
   * Template di trasformazione da applicare per ottenere l'oggetto da inserire nella email
   **/
  public TipoPendenzaPromemoria oggetto(Object oggetto) {
    this.oggetto = oggetto;
    return this;
  }

  @JsonProperty("oggetto")
  public Object getOggetto() {
    return oggetto;
  }
  public void setOggetto(Object oggetto) {
    this.oggetto = oggetto;
  }

  /**
   * Template di trasformazione da applicare per ottenere il messaggio da inserire nella email
   **/
  public TipoPendenzaPromemoria messaggio(Object messaggio) {
    this.messaggio = messaggio;
    return this;
  }

  @JsonProperty("messaggio")
  public Object getMessaggio() {
    return messaggio;
  }
  public void setMessaggio(Object messaggio) {
    this.messaggio = messaggio;
  }

  /**
   * Indica se allegare alla email il pdf contenente il promemoria
   **/
  public TipoPendenzaPromemoria allegaPdf(Boolean allegaPdf) {
    this.allegaPdf = allegaPdf;
    return this;
  }

  @JsonProperty("allegaPdf")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = it.govpay.rs.v1.beans.deserializer.BooleanDeserializer.class)
  public Boolean AllegaPdf() {
    return allegaPdf;
  }
  public void setAllegaPdf(Boolean allegaPdf) {
    this.allegaPdf = allegaPdf;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaPromemoria tipoPendenzaPromemoria = (TipoPendenzaPromemoria) o;
    return Objects.equals(tipo, tipoPendenzaPromemoria.tipo) &&
        Objects.equals(oggetto, tipoPendenzaPromemoria.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaPromemoria.messaggio) &&
        Objects.equals(allegaPdf, tipoPendenzaPromemoria.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, oggetto, messaggio, allegaPdf);
  }

  public static TipoPendenzaPromemoria parse(String json) throws ServiceException, ValidationException {
    return parse(json, TipoPendenzaPromemoria.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaPromemoria";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaPromemoria {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    allegaPdf: ").append(toIndentedString(allegaPdf)).append("\n");
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
	  int v = 0;
		v = this.oggetto != null ? v+1 : v;
		v = this.messaggio != null ? v+1 : v;
		v = this.tipo != null ? v+1 : v;
		
	  if(v != 3) {
		  throw new ValidationException("I campi 'tipo', 'oggetto' e 'messaggio' devono essere tutti valorizzati per definire il field 'promemoria'.");
	  }
		
	  ValidatorFactory vf = ValidatorFactory.newInstance();
	  vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
	}
  
	public void validate(String fieldName) throws ValidationException {
		int v = 0;
		v = this.oggetto != null ? v+1 : v;
		v = this.messaggio != null ? v+1 : v;
		v = this.tipo != null ? v+1 : v;
		
	  if(v != 3) {
		  throw new ValidationException("I campi 'tipo', 'oggetto' e 'messaggio' devono essere tutti valorizzati per definire il field '"+fieldName+"'.");
	  }
	  
	  
	  ValidatorFactory vf = ValidatorFactory.newInstance();
	  vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
		
	  
	}
}



