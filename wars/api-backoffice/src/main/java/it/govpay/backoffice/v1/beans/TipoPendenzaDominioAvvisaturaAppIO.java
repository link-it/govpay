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
  private TemplatePromemoriaAvvisoBase promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TemplatePromemoriaRicevutaBase promemoriaRicevuta = null;
  
  @JsonProperty("promemoriaScadenza")
  private TemplatePromemoriaScadenza promemoriaScadenza = null;
  
  @JsonProperty("apiKey")
  private String apiKey = null;
  
  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaAvviso(TemplatePromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TemplatePromemoriaAvvisoBase getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TemplatePromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaRicevuta(TemplatePromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TemplatePromemoriaRicevutaBase getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TemplatePromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   **/
  public TipoPendenzaDominioAvvisaturaAppIO promemoriaScadenza(TemplatePromemoriaScadenza promemoriaScadenza) {
    this.promemoriaScadenza = promemoriaScadenza;
    return this;
  }

  @JsonProperty("promemoriaScadenza")
  public TemplatePromemoriaScadenza getPromemoriaScadenza() {
    return promemoriaScadenza;
  }
  public void setPromemoriaScadenza(TemplatePromemoriaScadenza promemoriaScadenza) {
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
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("apiKey", this.apiKey).minLength(1).maxLength(35);
	vf.getValidator("promemoriaAvviso", this.promemoriaAvviso).validateFields();
	vf.getValidator("promemoriaRicevuta", this.promemoriaRicevuta).validateFields();
	vf.getValidator("promemoriaScadenza", this.promemoriaScadenza).validateFields();
}
}



