package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"form",
"validazione",
"trasformazione",
"inoltro",
})
public class TipoPendenzaPortalePagamentiCaricamentoPendenze extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("form")
  private TipoPendenzaFormPortalePagamenti form = null;
  
  @JsonProperty("validazione")
  private Object validazione = null;
  
  @JsonProperty("trasformazione")
  private TipoPendenzaTrasformazione trasformazione = null;
  
  @JsonProperty("inoltro")
  private String inoltro = null;
  
  /**
   * Indica se la configurazione e' abilitata
   **/
  public TipoPendenzaPortalePagamentiCaricamentoPendenze abilitato(Boolean abilitato) {
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
  public TipoPendenzaPortalePagamentiCaricamentoPendenze form(TipoPendenzaFormPortalePagamenti form) {
    this.form = form;
    return this;
  }

  @JsonProperty("form")
  public TipoPendenzaFormPortalePagamenti getForm() {
    return form;
  }
  public void setForm(TipoPendenzaFormPortalePagamenti form) {
    this.form = form;
  }

  /**
   * JSON Schema da utilizzare per la validazione dell'input
   **/
  public TipoPendenzaPortalePagamentiCaricamentoPendenze validazione(Object validazione) {
    this.validazione = validazione;
    return this;
  }

  @JsonProperty("validazione")
  public Object getValidazione() {
    return validazione;
  }
  public void setValidazione(Object validazione) {
    this.validazione = validazione;
  }

  /**
   **/
  public TipoPendenzaPortalePagamentiCaricamentoPendenze trasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
    return this;
  }

  @JsonProperty("trasformazione")
  public TipoPendenzaTrasformazione getTrasformazione() {
    return trasformazione;
  }
  public void setTrasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
  }

  /**
   * Identificativo dell'applicazione verso cui fare l'inoltro della pendenza
   **/
  public TipoPendenzaPortalePagamentiCaricamentoPendenze inoltro(String inoltro) {
    this.inoltro = inoltro;
    return this;
  }

  @JsonProperty("inoltro")
  public String getInoltro() {
    return inoltro;
  }
  public void setInoltro(String inoltro) {
    this.inoltro = inoltro;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaPortalePagamentiCaricamentoPendenze tipoPendenzaPortalePagamentiCaricamentoPendenze = (TipoPendenzaPortalePagamentiCaricamentoPendenze) o;
    return Objects.equals(abilitato, tipoPendenzaPortalePagamentiCaricamentoPendenze.abilitato) &&
        Objects.equals(form, tipoPendenzaPortalePagamentiCaricamentoPendenze.form) &&
        Objects.equals(validazione, tipoPendenzaPortalePagamentiCaricamentoPendenze.validazione) &&
        Objects.equals(trasformazione, tipoPendenzaPortalePagamentiCaricamentoPendenze.trasformazione) &&
        Objects.equals(inoltro, tipoPendenzaPortalePagamentiCaricamentoPendenze.inoltro);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, form, validazione, trasformazione, inoltro);
  }

  public static TipoPendenzaPortalePagamentiCaricamentoPendenze parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaPortalePagamentiCaricamentoPendenze) parse(json, TipoPendenzaPortalePagamentiCaricamentoPendenze.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaPortalePagamentiCaricamentoPendenze";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaPortalePagamentiCaricamentoPendenze {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
    sb.append("    validazione: ").append(toIndentedString(validazione)).append("\n");
    sb.append("    trasformazione: ").append(toIndentedString(trasformazione)).append("\n");
    sb.append("    inoltro: ").append(toIndentedString(inoltro)).append("\n");
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
			vf.getValidator("abilitato", this.abilitato).notNull();
		vf.getValidator("form", this.form).validateFields();
		vf.getValidator("trasformazione", this.trasformazione).validateFields();
		
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		
		if(this.inoltro != null)
			validatoreId.validaIdApplicazione("inoltro", this.inoltro);
		
	}
}



