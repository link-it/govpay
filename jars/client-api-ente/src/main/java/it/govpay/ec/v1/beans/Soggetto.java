package it.govpay.ec.v1.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

/**
  * dati anagrafici di un versante o pagatore.
 **/
// @Schema(description="dati anagrafici di un versante o pagatore.")
public class Soggetto  {
  
  // @Schema(required = true, description = "")
  private TipoSoggetto tipo = null;
  
  // @Schema(example = "RSSMRA30A01H501I", required = true, description = "codice fiscale o partita iva del soggetto")
 /**
   * codice fiscale o partita iva del soggetto  
  **/
  private String identificativo = null;
  
  // @Schema(example = "Mario Rossi", description = "nome e cognome o altra ragione sociale del soggetto")
 /**
   * nome e cognome o altra ragione sociale del soggetto  
  **/
  private String anagrafica = null;
  
  // @Schema(example = "Piazza della Vittoria", description = "")
  private String indirizzo = null;
  
  // @Schema(example = "10/A", description = "")
  private String civico = null;
  
  // @Schema(example = "0", description = "")
  private String cap = null;
  
  // @Schema(example = "Roma", description = "")
  private String localita = null;
  
  // @Schema(example = "Roma", description = "")
  private String provincia = null;
  
  // @Schema(example = "IT", description = "")
  private String nazione = null;
  
  // @Schema(example = "mario.rossi@host.eu", description = "")
  private String email = null;
  
  // @Schema(example = "+39 000-1234567", description = "")
  private String cellulare = null;
 /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  @Valid
  public TipoSoggetto getTipo() {
    return tipo;
  }

  public void setTipo(TipoSoggetto tipo) {
    this.tipo = tipo;
  }

  public Soggetto tipo(TipoSoggetto tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * codice fiscale o partita iva del soggetto
   * @return identificativo
  **/
  @JsonProperty("identificativo")
  @NotNull
  @Valid
  public String getIdentificativo() {
    return identificativo;
  }

  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  public Soggetto identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

 /**
   * nome e cognome o altra ragione sociale del soggetto
   * @return anagrafica
  **/
  @JsonProperty("anagrafica")
  @Valid
  public String getAnagrafica() {
    return anagrafica;
  }

  public void setAnagrafica(String anagrafica) {
    this.anagrafica = anagrafica;
  }

  public Soggetto anagrafica(String anagrafica) {
    this.anagrafica = anagrafica;
    return this;
  }

 /**
   * Get indirizzo
   * @return indirizzo
  **/
  @JsonProperty("indirizzo")
  @Valid
  public String getIndirizzo() {
    return indirizzo;
  }

  public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
  }

  public Soggetto indirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
    return this;
  }

 /**
   * Get civico
   * @return civico
  **/
  @JsonProperty("civico")
  @Valid
  public String getCivico() {
    return civico;
  }

  public void setCivico(String civico) {
    this.civico = civico;
  }

  public Soggetto civico(String civico) {
    this.civico = civico;
    return this;
  }

 /**
   * Get cap
   * @return cap
  **/
  @JsonProperty("cap")
  @Valid
  public String getCap() {
    return cap;
  }

  public void setCap(String cap) {
    this.cap = cap;
  }

  public Soggetto cap(String cap) {
    this.cap = cap;
    return this;
  }

 /**
   * Get localita
   * @return localita
  **/
  @JsonProperty("localita")
  @Valid
  public String getLocalita() {
    return localita;
  }

  public void setLocalita(String localita) {
    this.localita = localita;
  }

  public Soggetto localita(String localita) {
    this.localita = localita;
    return this;
  }

 /**
   * Get provincia
   * @return provincia
  **/
  @JsonProperty("provincia")
  @Valid
  public String getProvincia() {
    return provincia;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  public Soggetto provincia(String provincia) {
    this.provincia = provincia;
    return this;
  }

 /**
   * Get nazione
   * @return nazione
  **/
  @JsonProperty("nazione")
  @Valid
  public String getNazione() {
    return nazione;
  }

  public void setNazione(String nazione) {
    this.nazione = nazione;
  }

  public Soggetto nazione(String nazione) {
    this.nazione = nazione;
    return this;
  }

 /**
   * Get email
   * @return email
  **/
  @JsonProperty("email")
  @Valid
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Soggetto email(String email) {
    this.email = email;
    return this;
  }

 /**
   * Get cellulare
   * @return cellulare
  **/
  @JsonProperty("cellulare")
  @Valid
  public String getCellulare() {
    return cellulare;
  }

  public void setCellulare(String cellulare) {
    this.cellulare = cellulare;
  }

  public Soggetto cellulare(String cellulare) {
    this.cellulare = cellulare;
    return this;
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
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
