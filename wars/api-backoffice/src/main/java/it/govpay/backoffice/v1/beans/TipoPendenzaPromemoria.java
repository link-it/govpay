package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"oggetto",
"messaggio",
"allegaAvviso",
})
public class TipoPendenzaPromemoria extends JSONSerializable implements IValidable{
  
  @JsonProperty("oggetto")
  private String oggetto = null;
  
  @JsonProperty("messaggio")
  private String messaggio = null;
  
  @JsonProperty("allegaAvviso")
  private Boolean allegaAvviso = true;
  
  /**
   * Oggetto da inserire nella email
   **/
  public TipoPendenzaPromemoria oggetto(String oggetto) {
    this.oggetto = oggetto;
    return this;
  }

  @JsonProperty("oggetto")
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   * Definisce il template del body da inserire nella email
   **/
  public TipoPendenzaPromemoria messaggio(String messaggio) {
    this.messaggio = messaggio;
    return this;
  }

  @JsonProperty("messaggio")
  public String getMessaggio() {
    return messaggio;
  }
  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
  }

  /**
   * Indica se allegare alla email il pdf contenente l'avviso di pagamento
   **/
  public TipoPendenzaPromemoria allegaAvviso(Boolean allegaAvviso) {
    this.allegaAvviso = allegaAvviso;
    return this;
  }

  @JsonProperty("allegaAvviso")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = it.govpay.rs.v1.beans.deserializer.BooleanDeserializer.class)
  public Boolean AllegaAvviso() {
    return allegaAvviso;
  }
  public void setAllegaAvviso(Boolean allegaAvviso) {
    this.allegaAvviso = allegaAvviso;
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
        Objects.equals(allegaAvviso, tipoPendenzaPromemoria.allegaAvviso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oggetto, messaggio, allegaAvviso);
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
    sb.append("    allegaAvviso: ").append(toIndentedString(allegaAvviso)).append("\n");
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
	   
	   vf.getValidator("oggetto", this.oggetto).minLength(1).maxLength(512);
		
	   if((this.oggetto != null && this.messaggio == null) || (this.oggetto == null && this.messaggio != null)) {
			  throw new ValidationException("I campi 'oggetto' e 'definizione' devono essere entrambi valorizzati per definire il field 'promemoria'.");
	  }
	   
	}
}



