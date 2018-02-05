package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.EsitoRpt;
import it.govpay.rs.v1.beans.base.ModelloPagamento;
import java.math.BigDecimal;
import java.util.Date;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idDominio",
"iuv",
"ccp",
"pendenza",
"canale",
"modelloPagamento",
"stato",
"dettaglioStato",
"dataRichiesta",
"dataRicevuta",
"esito",
"importo",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Rpt extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("ccp")
  private String ccp = null;
  
  @JsonProperty("pendenza")
  private String pendenza = null;
  
  @JsonProperty("canale")
  private String canale = null;
  
  @JsonProperty("modelloPagamento")
  private ModelloPagamento modelloPagamento = null;
  
  @JsonProperty("stato")
  private String stato = null;
  
  @JsonProperty("dettaglioStato")
  private String dettaglioStato = null;
  
  @JsonProperty("dataRichiesta")
  private Date dataRichiesta = null;
  
  @JsonProperty("dataRicevuta")
  private Date dataRicevuta = null;
  
  @JsonProperty("esito")
  private EsitoRpt esito = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  /**
   * Identificativo ente creditore
   **/
  public Rpt idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Identificativo univoco di versamento
   **/
  public Rpt iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Codice contesto di pagamento
   **/
  public Rpt ccp(String ccp) {
    this.ccp = ccp;
    return this;
  }

  @JsonProperty("ccp")
  public String getCcp() {
    return ccp;
  }
  public void setCcp(String ccp) {
    this.ccp = ccp;
  }

  /**
   * Url al dettaglio della pendenza
   **/
  public Rpt pendenza(String pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public String getPendenza() {
    return pendenza;
  }
  public void setPendenza(String pendenza) {
    this.pendenza = pendenza;
  }

  /**
   * Url al dettaglio del canale utilizzato
   **/
  public Rpt canale(String canale) {
    this.canale = canale;
    return this;
  }

  @JsonProperty("canale")
  public String getCanale() {
    return canale;
  }
  public void setCanale(String canale) {
    this.canale = canale;
  }

  /**
   **/
  public Rpt modelloPagamento(ModelloPagamento modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
    return this;
  }

  @JsonProperty("modelloPagamento")
  public ModelloPagamento getModelloPagamento() {
    return modelloPagamento;
  }
  public void setModelloPagamento(ModelloPagamento modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
  }

  /**
   * Stato della richiesta di pagamento sulla piattaforma PagoPA.
   **/
  public Rpt stato(String stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   * Dettaglio fornito dal Nodo dei Pagamenti sullo stato della richiesta.
   **/
  public Rpt dettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
    return this;
  }

  @JsonProperty("dettaglioStato")
  public String getDettaglioStato() {
    return dettaglioStato;
  }
  public void setDettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
  }

  /**
   * Data di emissione della richiesta di pagamento.
   **/
  public Rpt dataRichiesta(Date dataRichiesta) {
    this.dataRichiesta = dataRichiesta;
    return this;
  }

  @JsonProperty("dataRichiesta")
  public Date getDataRichiesta() {
    return dataRichiesta;
  }
  public void setDataRichiesta(Date dataRichiesta) {
    this.dataRichiesta = dataRichiesta;
  }

  /**
   * Data di ricezione della ricevuta telematica
   **/
  public Rpt dataRicevuta(Date dataRicevuta) {
    this.dataRicevuta = dataRicevuta;
    return this;
  }

  @JsonProperty("dataRicevuta")
  public Date getDataRicevuta() {
    return dataRicevuta;
  }
  public void setDataRicevuta(Date dataRicevuta) {
    this.dataRicevuta = dataRicevuta;
  }

  /**
   **/
  public Rpt esito(EsitoRpt esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public EsitoRpt getEsito() {
    return esito;
  }
  public void setEsito(EsitoRpt esito) {
    this.esito = esito;
  }

  /**
   * Importo totale riscosso, corrispondente alla somma delle singole riscossioni.
   **/
  public Rpt importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rpt rpt = (Rpt) o;
    return Objects.equals(idDominio, rpt.idDominio) &&
        Objects.equals(iuv, rpt.iuv) &&
        Objects.equals(ccp, rpt.ccp) &&
        Objects.equals(pendenza, rpt.pendenza) &&
        Objects.equals(canale, rpt.canale) &&
        Objects.equals(modelloPagamento, rpt.modelloPagamento) &&
        Objects.equals(stato, rpt.stato) &&
        Objects.equals(dettaglioStato, rpt.dettaglioStato) &&
        Objects.equals(dataRichiesta, rpt.dataRichiesta) &&
        Objects.equals(dataRicevuta, rpt.dataRicevuta) &&
        Objects.equals(esito, rpt.esito) &&
        Objects.equals(importo, rpt.importo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, ccp, pendenza, canale, modelloPagamento, stato, dettaglioStato, dataRichiesta, dataRicevuta, esito, importo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rpt {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    ccp: ").append(toIndentedString(ccp)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    canale: ").append(toIndentedString(canale)).append("\n");
    sb.append("    modelloPagamento: ").append(toIndentedString(modelloPagamento)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dettaglioStato: ").append(toIndentedString(dettaglioStato)).append("\n");
    sb.append("    dataRichiesta: ").append(toIndentedString(dataRichiesta)).append("\n");
    sb.append("    dataRicevuta: ").append(toIndentedString(dataRicevuta)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
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



