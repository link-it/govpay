package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;

/**
 * Configurazione della generazione dei promemoria avviso pagamento
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"tipo",
"oggetto",
"messaggio",
})
public class TipoPendenzaAvvisaturaPromemoriaAvvisoBase extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  private TipoTemplateTrasformazione tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  /**
   * Indicazione la gestione del promemoria e' abilitata
   **/
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase abilitato(Boolean abilitato) {
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
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase tipo(TipoTemplateTrasformazione tipo) {
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
  
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase tipo(String tipo) {
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
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase oggetto(Object oggetto) {
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
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase messaggio(Object messaggio) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaAvvisaturaPromemoriaAvvisoBase tipoPendenzaAvvisaturaPromemoriaAvvisoBase = (TipoPendenzaAvvisaturaPromemoriaAvvisoBase) o;
    return Objects.equals(abilitato, tipoPendenzaAvvisaturaPromemoriaAvvisoBase.abilitato) &&
        Objects.equals(tipo, tipoPendenzaAvvisaturaPromemoriaAvvisoBase.tipo) &&
        Objects.equals(oggetto, tipoPendenzaAvvisaturaPromemoriaAvvisoBase.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaAvvisaturaPromemoriaAvvisoBase.messaggio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, tipo, oggetto, messaggio);
  }

  public static TipoPendenzaAvvisaturaPromemoriaAvvisoBase parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAvvisaturaPromemoriaAvvisoBase) parse(json, TipoPendenzaAvvisaturaPromemoriaAvvisoBase.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaPromemoriaAvvisoBase";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaPromemoriaAvvisoBase {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
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



