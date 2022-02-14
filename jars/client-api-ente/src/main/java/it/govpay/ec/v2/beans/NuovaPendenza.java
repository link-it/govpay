package it.govpay.ec.v2.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class NuovaPendenza   {
  
  @Schema(description = "")
  private String idA2A = null;
  
  @Schema(description = "")
  private String idPendenza = null;
  
  @Schema(required = true, description = "")
  private String idTipoPendenza = null;
  
  @Schema(required = true, description = "")
  private String idDominio = null;
  
  @Schema(description = "")
  private String idUnitaOperativa = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Descrizione della pendenza")
 /**
   * Descrizione della pendenza  
  **/
  private String causale = null;
  
  @Schema(required = true, description = "")
  private Soggetto soggettoPagatore = null;
  
  @Schema(example = "10.01", required = true, description = "Importo della pendenza. Deve corrispondere alla somma delle singole voci.")
 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.  
  **/
  private BigDecimal importo = null;
  
  @Schema(description = "")
  private String numeroAvviso = null;
  
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
  
  @Schema(description = "")
  private NuovoDocumento documento = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data in cui inviare il promemoria di pagamento.")
 /**
   * Data in cui inviare il promemoria di pagamento.  
  **/
  private Date dataNotificaAvviso = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data in cui inviare il promemoria di scadenza della pendenza.")
 /**
   * Data in cui inviare il promemoria di scadenza della pendenza.  
  **/
  private Date dataPromemoriaScadenza = null;
  
  @Schema(description = "")
  private ProprietaPendenza proprieta = null;
  
  @Schema(required = true, description = "")
  private List<NuovaVocePendenza> voci = new ArrayList<>();
 /**
   * Get idA2A
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public NuovaPendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Get idPendenza
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public NuovaPendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

 /**
   * Get idTipoPendenza
   * @return idTipoPendenza
  **/
  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }

  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  public NuovaPendenza idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

 /**
   * Get idDominio
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public NuovaPendenza idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Get idUnitaOperativa
   * @return idUnitaOperativa
  **/
  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }

  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  public NuovaPendenza idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

 /**
   * Descrizione della pendenza
   * @return causale
  **/
  @JsonProperty("causale")
  public String getCausale() {
    return causale;
  }

  public void setCausale(String causale) {
    this.causale = causale;
  }

  public NuovaPendenza causale(String causale) {
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

  public NuovaPendenza soggettoPagatore(Soggetto soggettoPagatore) {
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
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public NuovaPendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Get numeroAvviso
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public NuovaPendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
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

  public NuovaPendenza tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

 /**
   * Identificativo della direzione interna all&#x27;ente creditore
   * @return direzione
  **/
  @JsonProperty("direzione")
  public String getDirezione() {
    return direzione;
  }

  public void setDirezione(String direzione) {
    this.direzione = direzione;
  }

  public NuovaPendenza direzione(String direzione) {
    this.direzione = direzione;
    return this;
  }

 /**
   * Identificativo della divisione interna all&#x27;ente creditore
   * @return divisione
  **/
  @JsonProperty("divisione")
  public String getDivisione() {
    return divisione;
  }

  public void setDivisione(String divisione) {
    this.divisione = divisione;
  }

  public NuovaPendenza divisione(String divisione) {
    this.divisione = divisione;
    return this;
  }

 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   * @return dataValidita
  **/
  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return dataValidita;
  }

  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  public NuovaPendenza dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   * @return dataScadenza
  **/
  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return dataScadenza;
  }

  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public NuovaPendenza dataScadenza(Date dataScadenza) {
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

  public NuovaPendenza annoRiferimento(BigDecimal annoRiferimento) {
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

  public NuovaPendenza cartellaPagamento(String cartellaPagamento) {
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

  public NuovaPendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Get documento
   * @return documento
  **/
  @JsonProperty("documento")
  public NuovoDocumento getDocumento() {
    return documento;
  }

  public void setDocumento(NuovoDocumento documento) {
    this.documento = documento;
  }

  public NuovaPendenza documento(NuovoDocumento documento) {
    this.documento = documento;
    return this;
  }

 /**
   * Data in cui inviare il promemoria di pagamento.
   * @return dataNotificaAvviso
  **/
  @JsonProperty("dataNotificaAvviso")
  public Date getDataNotificaAvviso() {
    return dataNotificaAvviso;
  }

  public void setDataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
  }

  public NuovaPendenza dataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
    return this;
  }

 /**
   * Data in cui inviare il promemoria di scadenza della pendenza.
   * @return dataPromemoriaScadenza
  **/
  @JsonProperty("dataPromemoriaScadenza")
  public Date getDataPromemoriaScadenza() {
    return dataPromemoriaScadenza;
  }

  public void setDataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
  }

  public NuovaPendenza dataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
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

  public NuovaPendenza proprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
    return this;
  }

 /**
   * Get voci
   * @return voci
  **/
  @JsonProperty("voci")
  public List<NuovaVocePendenza> getVoci() {
    return voci;
  }

  public void setVoci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
  }

  public NuovaPendenza voci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  public NuovaPendenza addVociItem(NuovaVocePendenza vociItem) {
    this.voci.add(vociItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaPendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
    sb.append("    dataNotificaAvviso: ").append(toIndentedString(dataNotificaAvviso)).append("\n");
    sb.append("    dataPromemoriaScadenza: ").append(toIndentedString(dataPromemoriaScadenza)).append("\n");
    sb.append("    proprieta: ").append(toIndentedString(proprieta)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
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
