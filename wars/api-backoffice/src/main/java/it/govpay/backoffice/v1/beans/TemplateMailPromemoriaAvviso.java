package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Configurazione di sistema per la generazione dei promemoria avviso pagamento consegnati via mail
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"oggetto",
"messaggio",
"allegaPdf",
})
public class TemplateMailPromemoriaAvviso extends JSONSerializable implements IValidable {
  
  private TipoTemplateTrasformazione tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("allegaPdf")
  private Boolean allegaPdf = null;
  
  /**
   **/
  public TemplateMailPromemoriaAvviso tipo(TipoTemplateTrasformazione tipo) {
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
  
  public TemplateMailPromemoriaAvviso tipo(String tipo) {
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
  public TemplateMailPromemoriaAvviso oggetto(Object oggetto) {
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
  public TemplateMailPromemoriaAvviso messaggio(Object messaggio) {
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
  public TemplateMailPromemoriaAvviso allegaPdf(Boolean allegaPdf) {
    this.allegaPdf = allegaPdf;
    return this;
  }

  @JsonProperty("allegaPdf")
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
    TemplateMailPromemoriaAvviso templateMailPromemoriaAvviso = (TemplateMailPromemoriaAvviso) o;
    return Objects.equals(tipo, templateMailPromemoriaAvviso.tipo) &&
        Objects.equals(oggetto, templateMailPromemoriaAvviso.oggetto) &&
        Objects.equals(messaggio, templateMailPromemoriaAvviso.messaggio) &&
        Objects.equals(allegaPdf, templateMailPromemoriaAvviso.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, oggetto, messaggio, allegaPdf);
  }

  public static TemplateMailPromemoriaAvviso parse(String json) throws ServiceException, ValidationException {
    return (TemplateMailPromemoriaAvviso) parse(json, TemplateMailPromemoriaAvviso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "templateMailPromemoriaAvviso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateMailPromemoriaAvviso {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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
	 this.validate(true);
  }

  public void validate(boolean abilitatoObbligatorio) throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
//		if(abilitatoObbligatorio)
//			vf.getValidator("abilitato", abilitato).notNull();
		vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
	  
  }
}



