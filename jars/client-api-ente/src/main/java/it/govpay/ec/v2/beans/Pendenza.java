package it.govpay.ec.v2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Dati della pendenza
 **/
@Schema(description="Dati della pendenza")
public class Pendenza   {
  
  @Schema(example = "A2A-12345", required = true, description = "Identificativo del gestionale responsabile della pendenza")
 /**
   * Identificativo del gestionale responsabile della pendenza  
  **/
  private String idA2A = null;
  
  @Schema(example = "abcdef12345", required = true, description = "Identificativo della pendenza nel gestionale responsabile")
 /**
   * Identificativo della pendenza nel gestionale responsabile  
  **/
  private String idPendenza = null;
  
  @Schema(example = "IMU", description = "Identificativo della tipologia pendenza")
 /**
   * Identificativo della tipologia pendenza  
  **/
  private String idTipoPendenza = null;
  
  @Schema(description = "")
  private Dominio dominio = null;
  
  @Schema(description = "")
  private UnitaOperativa unitaOperativa = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", description = "Descrizione da inserire nell'avviso di pagamento")
 /**
   * Descrizione da inserire nell'avviso di pagamento  
  **/
  private String causale = null;
  
  @Schema(description = "")
  private Soggetto soggettoPagatore = null;
  
  @Schema(example = "10.01", description = "Importo della pendenza. Deve corrispondere alla somma delle singole voci.")
 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "123456789012345678", description = "Identificativo univoco versamento, assegnato se pagabile da psp")
 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp  
  **/
  private String numeroAvviso = null;
  
  @Schema(example = "Tue Dec 31 01:00:00 CET 2019", description = "Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.")
 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.  
  **/
  private Date dataValidita = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data di scadenza della pendenza, decorsa la quale non è più pagabile.")
 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.  
  **/
  private Date dataScadenza = null;
  
  @Schema(example = "2020", description = "Anno di riferimento della pendenza")
 /**
   * Anno di riferimento della pendenza  
  **/
  private BigDecimal annoRiferimento = null;
  
  @Schema(example = "ABC00000001", description = "Identificativo della cartella di pagamento a cui afferisce la pendenza")
 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza  
  **/
  private String cartellaPagamento = null;
  
  @Schema(description = "Dati applicativi allegati dal gestionale secondo un formato proprietario.")
 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.  
  **/
  private Object datiAllegati = null;
  
  @Schema(example = "Sanzioni", description = "Macro categoria della pendenza secondo la classificazione del creditore")
 /**
   * Macro categoria della pendenza secondo la classificazione del creditore  
  **/
  private String tassonomia = null;
  
  @Schema(example = "Direzione", description = "Identificativo della direzione interna all'ente creditore")
 /**
   * Identificativo della direzione interna all'ente creditore  
  **/
  private String direzione = null;
  
  @Schema(example = "Divisione", description = "Identificativo della divisione interna all'ente creditore")
 /**
   * Identificativo della divisione interna all'ente creditore  
  **/
  private String divisione = null;
  
  @Schema(description = "")
  private Documento documento = null;
  
  @Schema(description = "Parametro di randomizzazione delle URL di pagamento statiche")
 /**
   * Parametro di randomizzazione delle URL di pagamento statiche  
  **/
  private String UUID = null;
  
  @Schema(description = "")
  private ProprietaPendenza proprieta = null;
  
  @Schema(description = "")
  private List<AllegatoPendenza> allegati = null;
 /**
   * Identificativo del gestionale responsabile della pendenza
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  @NotNull
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public Pendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Identificativo della pendenza nel gestionale responsabile
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  @NotNull
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public Pendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

 /**
   * Identificativo della tipologia pendenza
   * @return idTipoPendenza
  **/
  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }

  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  public Pendenza idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public Pendenza dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Get unitaOperativa
   * @return unitaOperativa
  **/
  @JsonProperty("unitaOperativa")
  public UnitaOperativa getUnitaOperativa() {
    return unitaOperativa;
  }

  public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
  }

  public Pendenza unitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
    return this;
  }

 /**
   * Descrizione da inserire nell&#x27;avviso di pagamento
   * @return causale
  **/
  @JsonProperty("causale")
 @Size(max=140)  public String getCausale() {
    return causale;
  }

  public void setCausale(String causale) {
    this.causale = causale;
  }

  public Pendenza causale(String causale) {
    this.causale = causale;
    return this;
  }

 /**
   * Get soggettoPagatore
   * @return soggettoPagatore
  **/
  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return soggettoPagatore;
  }

  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  public Pendenza soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   * minimum: 0
   * maximum: 10000000000000000
   * @return importo
  **/
  @JsonProperty("importo")
 @DecimalMin("0") @DecimalMax("10000000000000000")  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public Pendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
 @Pattern(regexp="[0-9]18")  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public Pendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   * @return dataValidita
  **/
  @JsonProperty("dataValidita")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  public Date getDataValidita() {
    return dataValidita;
  }

  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  public Pendenza dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   * @return dataScadenza
  **/
  @JsonProperty("dataScadenza")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  public Date getDataScadenza() {
    return dataScadenza;
  }

  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public Pendenza dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

 /**
   * Anno di riferimento della pendenza
   * @return annoRiferimento
  **/
  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return annoRiferimento;
  }

  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public Pendenza annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   * @return cartellaPagamento
  **/
  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return cartellaPagamento;
  }

  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  public Pendenza cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   * @return datiAllegati
  **/
  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }

  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public Pendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   * @return tassonomia
  **/
  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return tassonomia;
  }

  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  public Pendenza tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

 /**
   * Identificativo della direzione interna all&#x27;ente creditore
   * @return direzione
  **/
  @JsonProperty("direzione")
 @Size(min=1,max=35)  public String getDirezione() {
    return direzione;
  }

  public void setDirezione(String direzione) {
    this.direzione = direzione;
  }

  public Pendenza direzione(String direzione) {
    this.direzione = direzione;
    return this;
  }

 /**
   * Identificativo della divisione interna all&#x27;ente creditore
   * @return divisione
  **/
  @JsonProperty("divisione")
 @Size(min=1,max=35)  public String getDivisione() {
    return divisione;
  }

  public void setDivisione(String divisione) {
    this.divisione = divisione;
  }

  public Pendenza divisione(String divisione) {
    this.divisione = divisione;
    return this;
  }

 /**
   * Get documento
   * @return documento
  **/
  @JsonProperty("documento")
  public Documento getDocumento() {
    return documento;
  }

  public void setDocumento(Documento documento) {
    this.documento = documento;
  }

  public Pendenza documento(Documento documento) {
    this.documento = documento;
    return this;
  }

 /**
   * Parametro di randomizzazione delle URL di pagamento statiche
   * @return UUID
  **/
  @JsonProperty("UUID")
  public String getUUID() {
    return UUID;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  public Pendenza UUID(String UUID) {
    this.UUID = UUID;
    return this;
  }

 /**
   * Get proprieta
   * @return proprieta
  **/
  @JsonProperty("proprieta")
  public ProprietaPendenza getProprieta() {
    return proprieta;
  }

  public void setProprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
  }

  public Pendenza proprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
    return this;
  }

 /**
   * Get allegati
   * @return allegati
  **/
  @JsonProperty("allegati")
  public List<AllegatoPendenza> getAllegati() {
    return allegati;
  }

  public void setAllegati(List<AllegatoPendenza> allegati) {
    this.allegati = allegati;
  }

  public Pendenza allegati(List<AllegatoPendenza> allegati) {
    this.allegati = allegati;
    return this;
  }

  public Pendenza addAllegatiItem(AllegatoPendenza allegatiItem) {
    this.allegati.add(allegatiItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
    sb.append("    UUID: ").append(toIndentedString(UUID)).append("\n");
    sb.append("    proprieta: ").append(toIndentedString(proprieta)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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
