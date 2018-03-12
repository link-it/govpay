package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.EsitoRpt;
import it.govpay.rs.v1.beans.base.ModelloPagamento;
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
})
public class RppIndex extends it.govpay.rs.v1.beans.JSONSerializable {
  
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
  
  /**
   * Identificativo ente creditore
   **/
  public RppIndex idDominio(String idDominio) {
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
  public RppIndex iuv(String iuv) {
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
  public RppIndex ccp(String ccp) {
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
  public RppIndex pendenza(String pendenza) {
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
  public RppIndex canale(String canale) {
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
  public RppIndex modelloPagamento(ModelloPagamento modelloPagamento) {
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
  public RppIndex stato(String stato) {
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
  public RppIndex dettaglioStato(String dettaglioStato) {
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
  public RppIndex dataRichiesta(Date dataRichiesta) {
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
  public RppIndex dataRicevuta(Date dataRicevuta) {
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
  public RppIndex esito(EsitoRpt esito) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RppIndex rppIndex = (RppIndex) o;
    return Objects.equals(idDominio, rppIndex.idDominio) &&
        Objects.equals(iuv, rppIndex.iuv) &&
        Objects.equals(ccp, rppIndex.ccp) &&
        Objects.equals(pendenza, rppIndex.pendenza) &&
        Objects.equals(canale, rppIndex.canale) &&
        Objects.equals(modelloPagamento, rppIndex.modelloPagamento) &&
        Objects.equals(stato, rppIndex.stato) &&
        Objects.equals(dettaglioStato, rppIndex.dettaglioStato) &&
        Objects.equals(dataRichiesta, rppIndex.dataRichiesta) &&
        Objects.equals(dataRicevuta, rppIndex.dataRicevuta) &&
        Objects.equals(esito, rppIndex.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, ccp, pendenza, canale, modelloPagamento, stato, dettaglioStato, dataRichiesta, dataRicevuta, esito);
  }

  public static RppIndex parse(String json) {
    return (RppIndex) parse(json, RppIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "rppIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RppIndex {\n");
    
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



