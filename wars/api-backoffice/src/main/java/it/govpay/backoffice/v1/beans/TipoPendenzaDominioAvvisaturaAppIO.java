package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Configurazione dell&#x27;avvisatura via app io per la tipologia pendenza dominio
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"promemoriaAvviso",
"promemoriaRicevuta",
"promemoriaScadenza",
"apiKey",
})
public class TipoPendenzaDominioAvvisaturaAppIO extends JSONSerializable implements IValidable {
  
  @JsonProperty("promemoriaAvviso")
  private TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta = null;
  
  @JsonProperty("promemoriaScadenza")
  private TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza = null;
  
  @JsonProperty("apiKey")
  private String apiKey = null;
  
  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaAvviso(TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaRicevuta(TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaScadenza(TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza) {
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

  /**
   * La API Key per le invocazioni.
   **/
  public TipoPendenzaDominioAvvisaturaAppIO apiKey(String apiKey) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominioAvvisaturaAppIO tipoPendenzaDominioAvvisaturaAppIO = (TipoPendenzaDominioAvvisaturaAppIO) o;
    return Objects.equals(promemoriaAvviso, tipoPendenzaDominioAvvisaturaAppIO.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaDominioAvvisaturaAppIO.promemoriaRicevuta) &&
        Objects.equals(promemoriaScadenza, tipoPendenzaDominioAvvisaturaAppIO.promemoriaScadenza) &&
        Objects.equals(apiKey, tipoPendenzaDominioAvvisaturaAppIO.apiKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promemoriaAvviso, promemoriaRicevuta, promemoriaScadenza, apiKey);
  }

  public static TipoPendenzaDominioAvvisaturaAppIO parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaDominioAvvisaturaAppIO) parse(json, TipoPendenzaDominioAvvisaturaAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominioAvvisaturaAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominioAvvisaturaAppIO {\n");
    
    sb.append("    promemoriaAvviso: ").append(toIndentedString(promemoriaAvviso)).append("\n");
    sb.append("    promemoriaRicevuta: ").append(toIndentedString(promemoriaRicevuta)).append("\n");
    sb.append("    promemoriaScadenza: ").append(toIndentedString(promemoriaScadenza)).append("\n");
    sb.append("    apiKey: ").append(toIndentedString(apiKey)).append("\n");
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
	vf.getValidator("apiKey", this.apiKey).minLength(1).maxLength(35);
	int v = 0;
	try {
		if(this.promemoriaAvviso != null) {
			this.promemoriaAvviso.validate(abilitatoObbligatorio);
			
			if(this.promemoriaAvviso.Abilitato()) {
				v++;
			}
		}
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaAvviso' non valido: " + e.getMessage());
	}
	try {
		if(this.promemoriaRicevuta != null) {
			this.promemoriaRicevuta.validate(abilitatoObbligatorio);
			
			if(this.promemoriaRicevuta.Abilitato()) {
				v++;
			}
		}
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaRicevuta' non valido: " + e.getMessage());
	}
	try {
		if(this.promemoriaScadenza != null) {
			this.promemoriaScadenza.validate(abilitatoObbligatorio);
			
			if(this.promemoriaScadenza.Abilitato()) {
				v++;
			}
		}
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaScadenza' non valido: " + e.getMessage());
	}
	
	if(v > 0) { //se ho abilitato almento uno dei tre promemoria l'API-Key e' obbligatoria
		try {
			vf.getValidator("apiKey", this.apiKey).notNull();
		}catch(ValidationException e) {
			throw new ValidationException("Il campo 'apiKey' e' obbligatorio quando si abilita uno tra promemoriaAvviso, promemoriaRicevuta o promemoriaScadenza.");
		}
	}
}
}



