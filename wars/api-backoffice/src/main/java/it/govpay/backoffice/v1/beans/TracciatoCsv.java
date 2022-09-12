package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import it.govpay.core.exceptions.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"intestazione",
"richiesta",
"risposta",
})
public class TracciatoCsv extends JSONSerializable implements IValidable {

  private TipoTemplateTrasformazione tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("intestazione")
  private String intestazione = null;
  
  @JsonProperty("richiesta")
  private Object richiesta = null;
  
  @JsonProperty("risposta")
  private Object risposta = null;
  
  /**
   **/
  public TracciatoCsv tipo(TipoTemplateTrasformazione tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  @JsonIgnore
  public TipoTemplateTrasformazione getTipoEnum() {
    return tipoEnum;
  }
  public void setTipo(TipoTemplateTrasformazione tipoEnum) {
    this.tipoEnum = tipoEnum;
  }
  
  public TracciatoCsv tipo(String tipo) {
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
   * Intestazione del file CSV di risposta
   **/
  public TracciatoCsv intestazione(String intestazione) {
    this.intestazione = intestazione;
    return this;
  }

  @JsonProperty("intestazione")
  public String getIntestazione() {
    return intestazione;
  }
  public void setIntestazione(String intestazione) {
    this.intestazione = intestazione;
  }

  /**
   * Template per la trasformazione di un record da CSV a JSON
   **/
  public TracciatoCsv richiesta(Object richiesta) {
    this.richiesta = richiesta;
    return this;
  }

  @JsonProperty("richiesta")
  public Object getRichiesta() {
    return richiesta;
  }
  public void setRichiesta(Object richiesta) {
    this.richiesta = richiesta;
  }

  /**
   * Template per la trasformazione di un record da JSON a CSV
   **/
  public TracciatoCsv risposta(Object risposta) {
    this.risposta = risposta;
    return this;
  }

  @JsonProperty("risposta")
  public Object getRisposta() {
    return risposta;
  }
  public void setRisposta(Object risposta) {
    this.risposta = risposta;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TracciatoCsv tracciatoCsv = (TracciatoCsv) o;
    return Objects.equals(tipo, tracciatoCsv.tipo) &&
        Objects.equals(intestazione, tracciatoCsv.intestazione) &&
        Objects.equals(richiesta, tracciatoCsv.richiesta) &&
        Objects.equals(risposta, tracciatoCsv.risposta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, intestazione, richiesta, risposta);
  }

  public static TracciatoCsv parse(String json) throws ServiceException, ValidationException {
    return parse(json, TracciatoCsv.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoCsv";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoCsv {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    intestazione: ").append(toIndentedString(intestazione)).append("\n");
    sb.append("    richiesta: ").append(toIndentedString(richiesta)).append("\n");
    sb.append("    risposta: ").append(toIndentedString(risposta)).append("\n");
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
	
	if(this.richiesta != null || this.risposta != null || this.intestazione != null || this.tipo != null) {
		if(!(this.richiesta != null && this.risposta != null && this.intestazione != null && this.tipo != null)) {
			  throw new ValidationException("I campi 'tipo', 'intestazione', 'richiesta' e 'risposta' devono essere tutti valorizzati per definire il field 'tracciatoCsv'.");
		}
	}
	vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
  }
}



