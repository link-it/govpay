package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Configurazione della generazione dei promemoria spediti via App IO
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"promemoriaAvviso",
"promemoriaRicevuta",
"promemoriaScadenza",
})
public class ConfigurazioneAvvisaturaAppIO extends JSONSerializable implements IValidable {
  
  @JsonProperty("promemoriaAvviso")
  private TemplatePromemoriaAvvisoBase promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TemplatePromemoriaRicevutaBase promemoriaRicevuta = null;
  
  @JsonProperty("promemoriaScadenza")
  private TemplatePromemoriaScadenza promemoriaScadenza = null;
  
  /**
   **/
  public ConfigurazioneAvvisaturaAppIO promemoriaAvviso(TemplatePromemoriaAvvisoBase promemoriaAvviso) {
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
  public ConfigurazioneAvvisaturaAppIO promemoriaRicevuta(TemplatePromemoriaRicevutaBase promemoriaRicevuta) {
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
  public ConfigurazioneAvvisaturaAppIO promemoriaScadenza(TemplatePromemoriaScadenza promemoriaScadenza) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneAvvisaturaAppIO configurazioneAvvisaturaAppIO = (ConfigurazioneAvvisaturaAppIO) o;
    return Objects.equals(promemoriaAvviso, configurazioneAvvisaturaAppIO.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, configurazioneAvvisaturaAppIO.promemoriaRicevuta) &&
        Objects.equals(promemoriaScadenza, configurazioneAvvisaturaAppIO.promemoriaScadenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promemoriaAvviso, promemoriaRicevuta, promemoriaScadenza);
  }

  public static ConfigurazioneAvvisaturaAppIO parse(String json) throws ServiceException, ValidationException {
    return (ConfigurazioneAvvisaturaAppIO) parse(json, ConfigurazioneAvvisaturaAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "configurazioneAvvisaturaAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneAvvisaturaAppIO {\n");
    
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
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("promemoriaAvviso", this.promemoriaAvviso).notNull().validateFields();
	vf.getValidator("promemoriaRicevuta", this.promemoriaRicevuta).notNull().validateFields();
	vf.getValidator("promemoriaScadenza", this.promemoriaScadenza).notNull().validateFields();
}
}



