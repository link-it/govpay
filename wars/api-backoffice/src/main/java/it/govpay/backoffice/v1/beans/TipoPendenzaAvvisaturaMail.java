package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;

/**
 * Configurazione dell&#x27;avvisatura via mail
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"promemoriaAvviso",
"promemoriaRicevuta",
"promemoriaScadenza",
})
public class TipoPendenzaAvvisaturaMail extends JSONSerializable implements IValidable {
  
  @JsonProperty("promemoriaAvviso")
  private TipoPendenzaAvvisaturaMailPromemoriaAvviso promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TipoPendenzaAvvisaturaMailPromemoriaRicevuta promemoriaRicevuta = null;
  
  @JsonProperty("promemoriaScadenza")
  private TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza = null;
  
  /**
   **/
  public TipoPendenzaAvvisaturaMail promemoriaAvviso(TipoPendenzaAvvisaturaMailPromemoriaAvviso promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TipoPendenzaAvvisaturaMailPromemoriaAvviso getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TipoPendenzaAvvisaturaMailPromemoriaAvviso promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaAvvisaturaMail promemoriaRicevuta(TipoPendenzaAvvisaturaMailPromemoriaRicevuta promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TipoPendenzaAvvisaturaMailPromemoriaRicevuta promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   **/
  public TipoPendenzaAvvisaturaMail promemoriaScadenza(TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza) {
    this.promemoriaScadenza = promemoriaScadenza;
    return this;
  }

  @JsonProperty("promemoriaScadenza")
  public TipoPendenzaAvvisaturaPromemoriaScadenza getPromemoriaScadenza() {
    return promemoriaScadenza;
  }
  public void setPromemoriaScadenza(TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza) {
    this.promemoriaScadenza = promemoriaScadenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaAvvisaturaMail tipoPendenzaAvvisaturaMail = (TipoPendenzaAvvisaturaMail) o;
    return Objects.equals(promemoriaAvviso, tipoPendenzaAvvisaturaMail.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaAvvisaturaMail.promemoriaRicevuta) &&
        Objects.equals(promemoriaScadenza, tipoPendenzaAvvisaturaMail.promemoriaScadenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promemoriaAvviso, promemoriaRicevuta, promemoriaScadenza);
  }

  public static TipoPendenzaAvvisaturaMail parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAvvisaturaMail) parse(json, TipoPendenzaAvvisaturaMail.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaMail";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaMail {\n");
    
    sb.append("    promemoriaAvviso: ").append(toIndentedString(promemoriaAvviso)).append("\n");
    sb.append("    promemoriaRicevuta: ").append(toIndentedString(promemoriaRicevuta)).append("\n");
    sb.append("    promemoriaScadenza: ").append(toIndentedString(promemoriaScadenza)).append("\n");
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
	try {
		if(this.promemoriaAvviso != null)
			this.promemoriaAvviso.validate(abilitatoObbligatorio);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaAvviso' non valido: " + e.getMessage());
	}
	try {
		if(this.promemoriaRicevuta != null)
			this.promemoriaRicevuta.validate(abilitatoObbligatorio);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaRicevuta' non valido: " + e.getMessage());
	}
	try {
		if(this.promemoriaScadenza != null)
			this.promemoriaScadenza.validate(abilitatoObbligatorio);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaScadenza' non valido: " + e.getMessage());
	}
}
}



