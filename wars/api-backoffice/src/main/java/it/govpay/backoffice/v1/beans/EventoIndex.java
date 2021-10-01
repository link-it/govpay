package it.govpay.backoffice.v1.beans;


import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"componente",
"categoriaEvento",
"ruolo",
"tipoEvento",
"esito",
"dataEvento",
"durataEvento",
"sottotipoEvento",
"sottotipoEsito",
"dettaglioEsito",
"idDominio",
"iuv",
"ccp",
"idA2A",
"idPendenza",
"idPagamento",
"datiPagoPA",
"severita",
})
public class EventoIndex extends JSONSerializable {
  
  @JsonProperty("id")
  private Long id = null;
  
  @JsonProperty("componente")
  private ComponenteEvento componente = null;
  
  @JsonProperty("categoriaEvento")
  private CategoriaEvento categoriaEvento = null;
  
  @JsonProperty("ruolo")
  private RuoloEvento ruolo = null;
  
  @JsonProperty("tipoEvento")
  private String tipoEvento = null;
  
  @JsonProperty("esito")
  private EsitoEvento esito = null;
  
  @JsonProperty("dataEvento")
  private Date dataEvento = null;
  
  @JsonProperty("durataEvento")
  private Long durataEvento = null;
  
  @JsonProperty("sottotipoEvento")
  private String sottotipoEvento = null;
  
  @JsonProperty("sottotipoEsito")
  private String sottotipoEsito = null;
  
  @JsonProperty("dettaglioEsito")
  private String dettaglioEsito = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("ccp")
  private String ccp = null;
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("idPagamento")
  private String idPagamento = null;
  
  @JsonProperty("datiPagoPA")
  private DatiPagoPA datiPagoPA = null;
  
  @JsonProperty("severita")
  private Integer severita = null;
  
  /**
   * Identificativo evento
   **/
  public EventoIndex id(Long id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  public EventoIndex componente(ComponenteEvento componente) {
    this.componente = componente;
    return this;
  }

  @JsonProperty("componente")
  public ComponenteEvento getComponente() {
    return componente;
  }
  public void setComponente(ComponenteEvento componente) {
    this.componente = componente;
  }

  /**
   **/
  public EventoIndex categoriaEvento(CategoriaEvento categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
    return this;
  }

  @JsonProperty("categoriaEvento")
  public CategoriaEvento getCategoriaEvento() {
    return categoriaEvento;
  }
  public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
  }

  /**
   **/
  public EventoIndex ruolo(RuoloEvento ruolo) {
    this.ruolo = ruolo;
    return this;
  }

  @JsonProperty("ruolo")
  public RuoloEvento getRuolo() {
    return ruolo;
  }
  public void setRuolo(RuoloEvento ruolo) {
    this.ruolo = ruolo;
  }

  /**
   **/
  public EventoIndex tipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
    return this;
  }

  @JsonProperty("tipoEvento")
  public String getTipoEvento() {
    return tipoEvento;
  }
  public void setTipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  /**
   **/
  public EventoIndex esito(EsitoEvento esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public EsitoEvento getEsito() {
    return esito;
  }
  public void setEsito(EsitoEvento esito) {
    this.esito = esito;
  }

  /**
   * Data emissione evento
   **/
  public EventoIndex dataEvento(Date dataEvento) {
    this.dataEvento = dataEvento;
    return this;
  }

  @JsonProperty("dataEvento")
  public Date getDataEvento() {
    return dataEvento;
  }
  public void setDataEvento(Date dataEvento) {
    this.dataEvento = dataEvento;
  }

  /**
   * Durata evento (in millisecondi)
   **/
  public EventoIndex durataEvento(Long durataEvento) {
    this.durataEvento = durataEvento;
    return this;
  }

  @JsonProperty("durataEvento")
  public Long getDurataEvento() {
    return durataEvento;
  }
  public void setDurataEvento(Long durataEvento) {
    this.durataEvento = durataEvento;
  }

  /**
   **/
  public EventoIndex sottotipoEvento(String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
    return this;
  }

  @JsonProperty("sottotipoEvento")
  public String getSottotipoEvento() {
    return sottotipoEvento;
  }
  public void setSottotipoEvento(String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
  }

  /**
   * Descrizione dell'esito
   **/
  public EventoIndex sottotipoEsito(String sottotipoEsito) {
    this.sottotipoEsito = sottotipoEsito;
    return this;
  }

  @JsonProperty("sottotipoEsito")
  public String getSottotipoEsito() {
    return sottotipoEsito;
  }
  public void setSottotipoEsito(String sottotipoEsito) {
    this.sottotipoEsito = sottotipoEsito;
  }

  /**
   **/
  public EventoIndex dettaglioEsito(String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
    return this;
  }

  @JsonProperty("dettaglioEsito")
  public String getDettaglioEsito() {
    return dettaglioEsito;
  }
  public void setDettaglioEsito(String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
  }

  /**
   * Identificativo ente creditore
   **/
  public EventoIndex idDominio(String idDominio) {
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
  public EventoIndex iuv(String iuv) {
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
  public EventoIndex ccp(String ccp) {
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
   * Identificativo del gestionale responsabile della pendenza
   **/
  public EventoIndex idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public EventoIndex idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * Identificativo del pagamento assegnato da GovPay
   **/
  public EventoIndex idPagamento(String idPagamento) {
    this.idPagamento = idPagamento;
    return this;
  }

  @JsonProperty("idPagamento")
  public String getIdPagamento() {
    return idPagamento;
  }
  public void setIdPagamento(String idPagamento) {
    this.idPagamento = idPagamento;
  }

  /**
   **/
  public EventoIndex datiPagoPA(DatiPagoPA datiPagoPA) {
    this.datiPagoPA = datiPagoPA;
    return this;
  }

  @JsonProperty("datiPagoPA")
  public DatiPagoPA getDatiPagoPA() {
    return datiPagoPA;
  }
  public void setDatiPagoPA(DatiPagoPA datiPagoPA) {
    this.datiPagoPA = datiPagoPA;
  }

  /**
   * indica il livello di severita nel caso di evento con esito KO/FAIL
   **/
  public EventoIndex severita(Integer severita) {
    this.severita = severita;
    return this;
  }

  @JsonProperty("severita")
  public Integer getSeverita() {
    return severita;
  }
  public void setSeverita(Integer severita) {
    this.severita = severita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventoIndex eventoIndex = (EventoIndex) o;
    return Objects.equals(id, eventoIndex.id) &&
        Objects.equals(componente, eventoIndex.componente) &&
        Objects.equals(categoriaEvento, eventoIndex.categoriaEvento) &&
        Objects.equals(ruolo, eventoIndex.ruolo) &&
        Objects.equals(tipoEvento, eventoIndex.tipoEvento) &&
        Objects.equals(esito, eventoIndex.esito) &&
        Objects.equals(dataEvento, eventoIndex.dataEvento) &&
        Objects.equals(durataEvento, eventoIndex.durataEvento) &&
        Objects.equals(sottotipoEvento, eventoIndex.sottotipoEvento) &&
        Objects.equals(sottotipoEsito, eventoIndex.sottotipoEsito) &&
        Objects.equals(dettaglioEsito, eventoIndex.dettaglioEsito) &&
        Objects.equals(idDominio, eventoIndex.idDominio) &&
        Objects.equals(iuv, eventoIndex.iuv) &&
        Objects.equals(ccp, eventoIndex.ccp) &&
        Objects.equals(idA2A, eventoIndex.idA2A) &&
        Objects.equals(idPendenza, eventoIndex.idPendenza) &&
        Objects.equals(idPagamento, eventoIndex.idPagamento) &&
        Objects.equals(datiPagoPA, eventoIndex.datiPagoPA) &&
        Objects.equals(severita, eventoIndex.severita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, componente, categoriaEvento, ruolo, tipoEvento, esito, dataEvento, durataEvento, sottotipoEvento, sottotipoEsito, dettaglioEsito, idDominio, iuv, ccp, idA2A, idPendenza, idPagamento, datiPagoPA, severita);
  }

  public static EventoIndex parse(String json) throws ServiceException, ValidationException { 
    return parse(json, EventoIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "eventoIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventoIndex {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    componente: ").append(toIndentedString(componente)).append("\n");
    sb.append("    categoriaEvento: ").append(toIndentedString(categoriaEvento)).append("\n");
    sb.append("    ruolo: ").append(toIndentedString(ruolo)).append("\n");
    sb.append("    tipoEvento: ").append(toIndentedString(tipoEvento)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    dataEvento: ").append(toIndentedString(dataEvento)).append("\n");
    sb.append("    durataEvento: ").append(toIndentedString(durataEvento)).append("\n");
    sb.append("    sottotipoEvento: ").append(toIndentedString(sottotipoEvento)).append("\n");
    sb.append("    sottotipoEsito: ").append(toIndentedString(sottotipoEsito)).append("\n");
    sb.append("    dettaglioEsito: ").append(toIndentedString(dettaglioEsito)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    ccp: ").append(toIndentedString(ccp)).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idPagamento: ").append(toIndentedString(idPagamento)).append("\n");
    sb.append("    datiPagoPA: ").append(toIndentedString(datiPagoPA)).append("\n");
    sb.append("    severita: ").append(toIndentedString(severita)).append("\n");
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



