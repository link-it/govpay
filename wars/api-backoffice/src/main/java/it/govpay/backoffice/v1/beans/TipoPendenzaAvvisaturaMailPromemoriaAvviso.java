package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;

/**
 * Configurazione della generazione dei promemoria avviso pagamento consegnati via mail
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"oggetto",
"messaggio",
"allegaPdf",
})
public class TipoPendenzaAvvisaturaMailPromemoriaAvviso extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("allegaPdf")
  private Boolean allegaPdf = null;
  
  /**
   * Indicazione la gestione del promemoria e' abilitata
   **/
  public TipoPendenzaAvvisaturaMailPromemoriaAvviso abilitato(Boolean abilitato) {
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
   * Template di trasformazione da applicare per ottenere l'oggetto da inserire nella email
   **/
  public TipoPendenzaAvvisaturaMailPromemoriaAvviso oggetto(Object oggetto) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaAvviso messaggio(Object messaggio) {
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
  public TipoPendenzaAvvisaturaMailPromemoriaAvviso allegaPdf(Boolean allegaPdf) {
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
    TipoPendenzaAvvisaturaMailPromemoriaAvviso tipoPendenzaAvvisaturaMailPromemoriaAvviso = (TipoPendenzaAvvisaturaMailPromemoriaAvviso) o;
    return Objects.equals(abilitato, tipoPendenzaAvvisaturaMailPromemoriaAvviso.abilitato) &&
        Objects.equals(oggetto, tipoPendenzaAvvisaturaMailPromemoriaAvviso.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaAvvisaturaMailPromemoriaAvviso.messaggio) &&
        Objects.equals(allegaPdf, tipoPendenzaAvvisaturaMailPromemoriaAvviso.allegaPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, oggetto, messaggio, allegaPdf);
  }

  public static TipoPendenzaAvvisaturaMailPromemoriaAvviso parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAvvisaturaMailPromemoriaAvviso) parse(json, TipoPendenzaAvvisaturaMailPromemoriaAvviso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaMailPromemoriaAvviso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaMailPromemoriaAvviso {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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
	// TODO Auto-generated method stub
	
}
}



