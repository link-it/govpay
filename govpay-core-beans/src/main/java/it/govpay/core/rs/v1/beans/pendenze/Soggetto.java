package it.govpay.core.rs.v1.beans.pendenze;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;

/**
 * dati anagrafici di un versante o pagatore.
 **/@org.codehaus.jackson.annotate.JsonPropertyOrder({
"tipo",
"identificativo",
"anagrafica",
"indirizzo",
"civico",
"cap",
"localita",
"provincia",
"nazione",
"email",
"cellulare",
})
public class Soggetto extends JSONSerializable {
  
    
  /**
   * tipologia di soggetto, se persona fisica (F) o giuridica (G)
   */
  public enum TipoEnum {
    
    
        
            
    G("G"),
    
            
    F("F");
            
        
    

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @org.codehaus.jackson.annotate.JsonValue
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

    
    
  @JsonProperty("tipo")
  private TipoEnum tipo = null;
  
  @JsonProperty("identificativo")
  private String identificativo = null;
  
  @JsonProperty("anagrafica")
  private String anagrafica = null;
  
  @JsonProperty("indirizzo")
  private String indirizzo = null;
  
  @JsonProperty("civico")
  private String civico = null;
  
  @JsonProperty("cap")
  private String cap = null;
  
  @JsonProperty("localita")
  private String localita = null;
  
  @JsonProperty("provincia")
  private String provincia = null;
  
  @JsonProperty("nazione")
  private String nazione = null;
  
  @JsonProperty("email")
  private String email = null;
  
  @JsonProperty("cellulare")
  private String cellulare = null;
  
  /**
   * tipologia di soggetto, se persona fisica (F) o giuridica (G)
   **/
  public Soggetto tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoEnum getTipo() {
    return tipo;
  }
  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  /**
   * codice fiscale o partita iva del soggetto
   **/
  public Soggetto identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

  @JsonProperty("identificativo")
  public String getIdentificativo() {
    return identificativo;
  }
  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  /**
   * nome e cognome o altra ragione sociale del soggetto
   **/
  public Soggetto anagrafica(String anagrafica) {
    this.anagrafica = anagrafica;
    return this;
  }

  @JsonProperty("anagrafica")
  public String getAnagrafica() {
    return anagrafica;
  }
  public void setAnagrafica(String anagrafica) {
    this.anagrafica = anagrafica;
  }

  /**
   **/
  public Soggetto indirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
    return this;
  }

  @JsonProperty("indirizzo")
  public String getIndirizzo() {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
  }

  /**
   **/
  public Soggetto civico(String civico) {
    this.civico = civico;
    return this;
  }

  @JsonProperty("civico")
  public String getCivico() {
    return civico;
  }
  public void setCivico(String civico) {
    this.civico = civico;
  }

  /**
   **/
  public Soggetto cap(String cap) {
    this.cap = cap;
    return this;
  }

  @JsonProperty("cap")
  public String getCap() {
    return cap;
  }
  public void setCap(String cap) {
    this.cap = cap;
  }

  /**
   **/
  public Soggetto localita(String localita) {
    this.localita = localita;
    return this;
  }

  @JsonProperty("localita")
  public String getLocalita() {
    return localita;
  }
  public void setLocalita(String localita) {
    this.localita = localita;
  }

  /**
   **/
  public Soggetto provincia(String provincia) {
    this.provincia = provincia;
    return this;
  }

  @JsonProperty("provincia")
  public String getProvincia() {
    return provincia;
  }
  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  /**
   **/
  public Soggetto nazione(String nazione) {
    this.nazione = nazione;
    return this;
  }

  @JsonProperty("nazione")
  public String getNazione() {
    return nazione;
  }
  public void setNazione(String nazione) {
    this.nazione = nazione;
  }

  /**
   **/
  public Soggetto email(String email) {
    this.email = email;
    return this;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  public Soggetto cellulare(String cellulare) {
    this.cellulare = cellulare;
    return this;
  }

  @JsonProperty("cellulare")
  public String getCellulare() {
    return cellulare;
  }
  public void setCellulare(String cellulare) {
    this.cellulare = cellulare;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Soggetto soggetto = (Soggetto) o;
    return Objects.equals(tipo, soggetto.tipo) &&
        Objects.equals(identificativo, soggetto.identificativo) &&
        Objects.equals(anagrafica, soggetto.anagrafica) &&
        Objects.equals(indirizzo, soggetto.indirizzo) &&
        Objects.equals(civico, soggetto.civico) &&
        Objects.equals(cap, soggetto.cap) &&
        Objects.equals(localita, soggetto.localita) &&
        Objects.equals(provincia, soggetto.provincia) &&
        Objects.equals(nazione, soggetto.nazione) &&
        Objects.equals(email, soggetto.email) &&
        Objects.equals(cellulare, soggetto.cellulare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, identificativo, anagrafica, indirizzo, civico, cap, localita, provincia, nazione, email, cellulare);
  }

  public static Soggetto parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (Soggetto) parse(json, Soggetto.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "soggetto";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Soggetto {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    anagrafica: ").append(toIndentedString(anagrafica)).append("\n");
    sb.append("    indirizzo: ").append(toIndentedString(indirizzo)).append("\n");
    sb.append("    civico: ").append(toIndentedString(civico)).append("\n");
    sb.append("    cap: ").append(toIndentedString(cap)).append("\n");
    sb.append("    localita: ").append(toIndentedString(localita)).append("\n");
    sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
    sb.append("    nazione: ").append(toIndentedString(nazione)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    cellulare: ").append(toIndentedString(cellulare)).append("\n");
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



