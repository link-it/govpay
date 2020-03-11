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
"definizione",
})
public class TipoPendenzaTrasformazione extends JSONSerializable implements IValidable {

  private TipoTemplateTrasformazione tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("definizione")
  private Object definizione = null;
  
  /**
   **/
  public TipoPendenzaTrasformazione tipo(TipoTemplateTrasformazione tipo) {
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
  
  public TipoPendenzaTrasformazione tipo(String tipo) {
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
   * Template di trasformazione da applicare per trasformare l'input in una pendenza nel formato GovPay
   **/
  public TipoPendenzaTrasformazione definizione(Object definizione) {
    this.definizione = definizione;
    return this;
  }

  @JsonProperty("definizione")
  public Object getDefinizione() {
    return definizione;
  }
  public void setDefinizione(Object definizione) {
    this.definizione = definizione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaTrasformazione tipoPendenzaTrasformazione = (TipoPendenzaTrasformazione) o;
    return Objects.equals(tipo, tipoPendenzaTrasformazione.tipo) &&
        Objects.equals(definizione, tipoPendenzaTrasformazione.definizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, definizione);
  }

  public static TipoPendenzaTrasformazione parse(String json) throws ServiceException, ValidationException { 
    return parse(json, TipoPendenzaTrasformazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaTrasformazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaTrasformazione {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    definizione: ").append(toIndentedString(definizione)).append("\n");
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
	  
	  if((this.tipo != null && this.definizione == null) || (this.tipo == null && this.definizione != null)) {
		  throw new ValidationException("I campi 'tipo' e 'definizione' devono essere entrambi valorizzati per definire il field 'trasformazione'.");
	  }
	}
}



