package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;

/**
 * Configurazione della generazione dei promemoria ricevuta pagamento
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"tipo",
"oggetto",
"messaggio",
"soloEseguiti",
})
public class TipoPendenzaAvvisaturaPromemoriaRicevutaBase extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
    
  /**
   * Indica il tipo di template da applicare per attualizzare il promemoria
   */
  public enum TipoEnum {
    
    
        
            
    FREEMARKER("freemarker");
            
        
    

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  private TipoEnum tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("oggetto")
  private Object oggetto = null;
  
  @JsonProperty("messaggio")
  private Object messaggio = null;
  
  @JsonProperty("soloEseguiti")
  private Boolean soloEseguiti = null;
  
  /**
   * Indicazione la gestione del promemoria e' abilitata
   **/
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase abilitato(Boolean abilitato) {
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
   * Indica il tipo di template da applicare per attualizzare il promemoria
   **/
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase tipo(TipoEnum tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  @JsonIgnore
  public TipoEnum getTipoEnum() {
    return tipoEnum;
  }
  public void setTipo(TipoEnum tipoEnum) {
    this.tipoEnum = tipoEnum;
  }
  
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase tipo(String tipo) {
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
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase oggetto(Object oggetto) {
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
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase messaggio(Object messaggio) {
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
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase soloEseguiti(Boolean soloEseguiti) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaAvvisaturaPromemoriaRicevutaBase tipoPendenzaAvvisaturaPromemoriaRicevutaBase = (TipoPendenzaAvvisaturaPromemoriaRicevutaBase) o;
    return Objects.equals(abilitato, tipoPendenzaAvvisaturaPromemoriaRicevutaBase.abilitato) &&
        Objects.equals(tipo, tipoPendenzaAvvisaturaPromemoriaRicevutaBase.tipo) &&
        Objects.equals(oggetto, tipoPendenzaAvvisaturaPromemoriaRicevutaBase.oggetto) &&
        Objects.equals(messaggio, tipoPendenzaAvvisaturaPromemoriaRicevutaBase.messaggio) &&
        Objects.equals(soloEseguiti, tipoPendenzaAvvisaturaPromemoriaRicevutaBase.soloEseguiti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, tipo, oggetto, messaggio, soloEseguiti);
  }

  public static TipoPendenzaAvvisaturaPromemoriaRicevutaBase parse(String json) throws ServiceException, ValidationException {
    return (TipoPendenzaAvvisaturaPromemoriaRicevutaBase) parse(json, TipoPendenzaAvvisaturaPromemoriaRicevutaBase.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaPromemoriaRicevutaBase";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaPromemoriaRicevutaBase {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    soloEseguiti: ").append(toIndentedString(soloEseguiti)).append("\n");
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



