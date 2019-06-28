package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"oggetto",
"messaggio",
"allegaPdf",
})
public class TipoPendenzaPromemoria extends JSONSerializable implements IValidable{
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("allegaPdf")
  private Boolean allegaPdf = true;
  
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
    return Objects.equals(oggetto, tipoPendenzaPromemoria.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaPromemoria.messaggio) &&
        Objects.equals(allegaPdf, tipoPendenzaPromemoria.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oggetto, messaggio, allegaPdf);
  }

  public static TipoPendenzaPromemoria parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaPromemoria) parse(json, TipoPendenzaPromemoria.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaPromemoria";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaPromemoria {\n");
    
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
	   if((this.oggetto != null && this.messaggio == null) || (this.oggetto == null && this.messaggio != null)) {
			  throw new ValidationException("I campi 'oggetto' e 'definizione' devono essere entrambi valorizzati per definire il field 'promemoria'.");
	  }
	   
	}
}



