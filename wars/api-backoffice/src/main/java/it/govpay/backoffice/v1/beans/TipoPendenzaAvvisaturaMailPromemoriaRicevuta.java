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
 * Configurazione della generazione dei promemoria ricevuta pagamento consegnati via mail
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"tipo",
"oggetto",
"messaggio",
"soloEseguiti",
"allegaPdf",
})
public class TipoPendenzaAvvisaturaMailPromemoriaRicevuta extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
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
   * Indicazione la gestione del promemoria e' abilitata
   **/
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta abilitato(Boolean abilitato) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta tipo(TipoTemplateTrasformazione tipo) {
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
  
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta tipo(String tipo) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta oggetto(Object oggetto) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta messaggio(Object messaggio) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta soloEseguiti(Boolean soloEseguiti) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaRicevuta allegaPdf(Boolean allegaPdf) {
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
    TipoPendenzaAvvisaturaMailPromemoriaRicevuta tipoPendenzaAvvisaturaMailPromemoriaRicevuta = (TipoPendenzaAvvisaturaMailPromemoriaRicevuta) o;
    return Objects.equals(abilitato, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.abilitato) &&
        Objects.equals(tipo, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.tipo) &&
        Objects.equals(oggetto, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.messaggio) &&
        Objects.equals(soloEseguiti, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.soloEseguiti) &&
        Objects.equals(allegaPdf, tipoPendenzaAvvisaturaMailPromemoriaRicevuta.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, tipo, oggetto, messaggio, soloEseguiti, allegaPdf);
  }

  public static TipoPendenzaAvvisaturaMailPromemoriaRicevuta parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAvvisaturaMailPromemoriaRicevuta) parse(json, TipoPendenzaAvvisaturaMailPromemoriaRicevuta.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaMailPromemoriaRicevuta";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaMailPromemoriaRicevuta {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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
	 this.validate(true);
}

public void validate(boolean abilitatoObbligatorio) throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	if(abilitatoObbligatorio)
		vf.getValidator("abilitato", abilitato).notNull();
	
//	int v = 0;
//	v = this.oggetto != null ? v+1 : v;
//	v = this.messaggio != null ? v+1 : v;
//	v = this.tipo != null ? v+1 : v;
//	
//	if(v != 3) {
//	  throw new ValidationException("I campi 'tipo', 'oggetto' e 'messaggio' devono essere tutti valorizzati per definire il field 'promemoriaRicevuta'.");
//	}
	
	vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
}
}



