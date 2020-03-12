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
 * Configurazione di sistema per la generazione dei promemoria ricevuta pagamento consegnati via mail
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"oggetto",
"messaggio",
"soloEseguiti",
"allegaPdf",
})
public class TemplateMailPromemoriaRicevuta extends JSONSerializable implements IValidable {
  
  private TipoTemplateTrasformazione tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("soloEseguiti")
  private Boolean soloEseguiti = null;
  
  @JsonProperty("allegaPdf")
  private Boolean allegaPdf = null;
  
  /**
   **/
  public TemplateMailPromemoriaRicevuta tipo(TipoTemplateTrasformazione tipo) {
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
  
  public TemplateMailPromemoriaRicevuta tipo(String tipo) {
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
   * Template di trasformazione da applicare per ottenere l'oggetto da inserire nel promemoria
   **/
  public TemplateMailPromemoriaRicevuta oggetto(Object oggetto) {
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
   * Template di trasformazione da applicare per ottenere il messaggio da inserire nel promemoria
   **/
  public TemplateMailPromemoriaRicevuta messaggio(Object messaggio) {
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
   * Indica se limitare l'invio delle ricevute per i soli pagamenti eseguiti
   **/
  public TemplateMailPromemoriaRicevuta soloEseguiti(Boolean soloEseguiti) {
    this.soloEseguiti = soloEseguiti;
    return this;
  }

  @JsonProperty("soloEseguiti")
  public Boolean SoloEseguiti() {
    return soloEseguiti;
  }
  public void setSoloEseguiti(Boolean soloEseguiti) {
    this.soloEseguiti = soloEseguiti;
  }

  /**
   * Indica se allegare alla email il pdf contenente il promemoria
   **/
  public TemplateMailPromemoriaRicevuta allegaPdf(Boolean allegaPdf) {
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
    TemplateMailPromemoriaRicevuta templateMailPromemoriaRicevuta = (TemplateMailPromemoriaRicevuta) o;
    return Objects.equals(tipo, templateMailPromemoriaRicevuta.tipo) &&
        Objects.equals(oggetto, templateMailPromemoriaRicevuta.oggetto) &&
        Objects.equals(messaggio, templateMailPromemoriaRicevuta.messaggio) &&
        Objects.equals(soloEseguiti, templateMailPromemoriaRicevuta.soloEseguiti) &&
        Objects.equals(allegaPdf, templateMailPromemoriaRicevuta.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, oggetto, messaggio, soloEseguiti, allegaPdf);
  }

  public static TemplateMailPromemoriaRicevuta parse(String json) throws ServiceException, ValidationException {
    return (TemplateMailPromemoriaRicevuta) parse(json, TemplateMailPromemoriaRicevuta.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "templateMailPromemoriaRicevuta";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateMailPromemoriaRicevuta {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    soloEseguiti: ").append(toIndentedString(soloEseguiti)).append("\n");
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
	this.validate("promemoriaRicevuta");
	}

	public void validate(String fieldName) throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		
		int v = 0;
		v = this.oggetto != null ? v+1 : v;
		v = this.messaggio != null ? v+1 : v;
		v = this.tipo != null ? v+1 : v;
		
		if(v != 3) {
		  throw new ValidationException("I campi 'tipo', 'oggetto' e 'messaggio' devono essere tutti valorizzati per definire il field '"+fieldName+"'.");
		}
		
		vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
		vf.getValidator("soloEseguiti", this.soloEseguiti).notNull();
		vf.getValidator("soloEseguiti", this.allegaPdf).notNull();
	}
}



