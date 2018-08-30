package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nomePendenza",
"tipoOperazione",
"stato",
"descrizioneStato",
"enteCreditore",
"debitore",
"applicazione",
"identificativoPendenza",
"numeroAvviso",
})
public class OperazionePendenza extends JSONSerializable {
  
  @JsonProperty("nomePendenza")
  private String nomePendenza = null;
  
    
  /**
   * Tipo Operazione
   */
  public enum TipoOperazioneEnum {
    
    
        
            
    ADD("ADD"),
    
            
    DEL("DEL"),
    
            
    NON_VALIDA("NON VALIDA");
            
        
    

    private String value;

    TipoOperazioneEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoOperazioneEnum fromValue(String text) {
      for (TipoOperazioneEnum b : TipoOperazioneEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipoOperazione")
  private TipoOperazioneEnum tipoOperazione = null;
  
    
  /**
   * Stato elaborazione dell'operazione
   */
  public enum StatoEnum {
    
    
        
            
    ESEGUITO("ESEGUITO"),
    
            
    SCARTATO("SCARTATO"),
    
            
    NON_VALIDO("NON VALIDO");
            
        
    

    private String value;

    StatoEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static StatoEnum fromValue(String text) {
      for (StatoEnum b : StatoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("stato")
  private StatoEnum stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("enteCreditore")
  private String enteCreditore = null;
  
  @JsonProperty("debitore")
  private String debitore = null;
  
  @JsonProperty("applicazione")
  private String applicazione = null;
  
  @JsonProperty("identificativoPendenza")
  private String identificativoPendenza = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  /**
   * Nome della pendenza associata all'operazione
   **/
  public OperazionePendenza nomePendenza(String nomePendenza) {
    this.nomePendenza = nomePendenza;
    return this;
  }

  @JsonProperty("nomePendenza")
  public String getNomePendenza() {
    return nomePendenza;
  }
  public void setNomePendenza(String nomePendenza) {
    this.nomePendenza = nomePendenza;
  }

  /**
   * Tipo Operazione
   **/
  public OperazionePendenza tipoOperazione(TipoOperazioneEnum tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
    return this;
  }

  @JsonProperty("tipoOperazione")
  public TipoOperazioneEnum getTipoOperazione() {
    return tipoOperazione;
  }
  public void setTipoOperazione(TipoOperazioneEnum tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
  }

  /**
   * Stato elaborazione dell'operazione
   **/
  public OperazionePendenza stato(StatoEnum stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoEnum getStato() {
    return stato;
  }
  public void setStato(StatoEnum stato) {
    this.stato = stato;
  }

  /**
   * Descrizione dello stato operazione
   **/
  public OperazionePendenza descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return descrizioneStato;
  }
  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  /**
   * Ente a cui si riferisce l'operazione
   **/
  public OperazionePendenza enteCreditore(String enteCreditore) {
    this.enteCreditore = enteCreditore;
    return this;
  }

  @JsonProperty("enteCreditore")
  public String getEnteCreditore() {
    return enteCreditore;
  }
  public void setEnteCreditore(String enteCreditore) {
    this.enteCreditore = enteCreditore;
  }

  /**
   * Debitore
   **/
  public OperazionePendenza debitore(String debitore) {
    this.debitore = debitore;
    return this;
  }

  @JsonProperty("debitore")
  public String getDebitore() {
    return debitore;
  }
  public void setDebitore(String debitore) {
    this.debitore = debitore;
  }

  /**
   * Applicazione che ha effettuato l'operazione
   **/
  public OperazionePendenza applicazione(String applicazione) {
    this.applicazione = applicazione;
    return this;
  }

  @JsonProperty("applicazione")
  public String getApplicazione() {
    return applicazione;
  }
  public void setApplicazione(String applicazione) {
    this.applicazione = applicazione;
  }

  /**
   * Identificativo della pendenza associata all'operazione
   **/
  public OperazionePendenza identificativoPendenza(String identificativoPendenza) {
    this.identificativoPendenza = identificativoPendenza;
    return this;
  }

  @JsonProperty("identificativoPendenza")
  public String getIdentificativoPendenza() {
    return identificativoPendenza;
  }
  public void setIdentificativoPendenza(String identificativoPendenza) {
    this.identificativoPendenza = identificativoPendenza;
  }

  /**
   * Numero Avviso generato
   **/
  public OperazionePendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperazionePendenza operazionePendenza = (OperazionePendenza) o;
    return Objects.equals(nomePendenza, operazionePendenza.nomePendenza) &&
        Objects.equals(tipoOperazione, operazionePendenza.tipoOperazione) &&
        Objects.equals(stato, operazionePendenza.stato) &&
        Objects.equals(descrizioneStato, operazionePendenza.descrizioneStato) &&
        Objects.equals(enteCreditore, operazionePendenza.enteCreditore) &&
        Objects.equals(debitore, operazionePendenza.debitore) &&
        Objects.equals(applicazione, operazionePendenza.applicazione) &&
        Objects.equals(identificativoPendenza, operazionePendenza.identificativoPendenza) &&
        Objects.equals(numeroAvviso, operazionePendenza.numeroAvviso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomePendenza, tipoOperazione, stato, descrizioneStato, enteCreditore, debitore, applicazione, identificativoPendenza, numeroAvviso);
  }

  public static OperazionePendenza parse(String json) throws ServiceException, ValidationException {
    return (OperazionePendenza) parse(json, OperazionePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazionePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazionePendenza {\n");
    
    sb.append("    nomePendenza: ").append(toIndentedString(nomePendenza)).append("\n");
    sb.append("    tipoOperazione: ").append(toIndentedString(tipoOperazione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    enteCreditore: ").append(toIndentedString(enteCreditore)).append("\n");
    sb.append("    debitore: ").append(toIndentedString(debitore)).append("\n");
    sb.append("    applicazione: ").append(toIndentedString(applicazione)).append("\n");
    sb.append("    identificativoPendenza: ").append(toIndentedString(identificativoPendenza)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
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
}



