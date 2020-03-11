package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;

@JsonPropertyOrder({
"idTipoPendenza",
"descrizione",
"tipo",
"codificaIUV",
"pagaTerzi",
"abilitato",
"portaleBackoffice",
"portalePagamento",
"avvisaturaMail",
"avvisaturaAppIO",
"visualizzazione",
"tracciatoCsv",
"valori",
})
public class TipoPendenzaDominio extends JSONSerializable {
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tipo")
  private TipoPendenzaTipologia tipo = null;
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("portaleBackoffice")
  private TipoPendenzaPortaleCaricamentoPendenze portaleBackoffice = null;
  
  @JsonProperty("portalePagamento")
  private TipoPendenzaPortaleCaricamentoPendenze portalePagamento = null;
  
  @JsonProperty("avvisaturaMail")
  private TipoPendenzaAvvisaturaMail avvisaturaMail = null;
  
  @JsonProperty("avvisaturaAppIO")
  private TipoPendenzaAvvisaturaAppIO avvisaturaAppIO = null;
  
  @JsonProperty("visualizzazione")
  private Object visualizzazione = null;
  
  @JsonProperty("tracciatoCsv")
  private TracciatoCsv tracciatoCsv = null;
  
  @JsonProperty("valori")
  private TipoPendenzaDominioPost valori = null;
  
  /**
   **/
  public TipoPendenzaDominio idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   **/
  public TipoPendenzaDominio descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  public TipoPendenzaDominio tipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoPendenzaTipologia getTipo() {
    return tipo;
  }
  public void setTipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenzaDominio codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Indica se la pendenza e' pagabile da soggetti terzi
   **/
  public TipoPendenzaDominio pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenzaDominio abilitato(Boolean abilitato) {
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
  public TipoPendenzaDominio portaleBackoffice(TipoPendenzaPortaleCaricamentoPendenze portaleBackoffice) {
    this.portaleBackoffice = portaleBackoffice;
    return this;
  }

  @JsonProperty("portaleBackoffice")
  public TipoPendenzaPortaleCaricamentoPendenze getPortaleBackoffice() {
    return portaleBackoffice;
  }
  public void setPortaleBackoffice(TipoPendenzaPortaleCaricamentoPendenze portaleBackoffice) {
    this.portaleBackoffice = portaleBackoffice;
  }

  /**
   **/
  public TipoPendenzaDominio portalePagamento(TipoPendenzaPortaleCaricamentoPendenze portalePagamento) {
    this.portalePagamento = portalePagamento;
    return this;
  }

  @JsonProperty("portalePagamento")
  public TipoPendenzaPortaleCaricamentoPendenze getPortalePagamento() {
    return portalePagamento;
  }
  public void setPortalePagamento(TipoPendenzaPortaleCaricamentoPendenze portalePagamento) {
    this.portalePagamento = portalePagamento;
  }

  /**
   **/
  public TipoPendenzaDominio avvisaturaMail(TipoPendenzaAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
    return this;
  }

  @JsonProperty("avvisaturaMail")
  public TipoPendenzaAvvisaturaMail getAvvisaturaMail() {
    return avvisaturaMail;
  }
  public void setAvvisaturaMail(TipoPendenzaAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
  }

  /**
   **/
  public TipoPendenzaDominio avvisaturaAppIO(TipoPendenzaAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
    return this;
  }

  @JsonProperty("avvisaturaAppIO")
  public TipoPendenzaAvvisaturaAppIO getAvvisaturaAppIO() {
    return avvisaturaAppIO;
  }
  public void setAvvisaturaAppIO(TipoPendenzaAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
  }

  /**
   * Definisce come visualizzare la pendenza
   **/
  public TipoPendenzaDominio visualizzazione(Object visualizzazione) {
    this.visualizzazione = visualizzazione;
    return this;
  }

  @JsonProperty("visualizzazione")
  public Object getVisualizzazione() {
    return visualizzazione;
  }
  public void setVisualizzazione(Object visualizzazione) {
    this.visualizzazione = visualizzazione;
  }

  /**
   **/
  public TipoPendenzaDominio tracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
    return this;
  }

  @JsonProperty("tracciatoCsv")
  public TracciatoCsv getTracciatoCsv() {
    return tracciatoCsv;
  }
  public void setTracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
  }

  /**
   **/
  public TipoPendenzaDominio valori(TipoPendenzaDominioPost valori) {
    this.valori = valori;
    return this;
  }

  @JsonProperty("valori")
  public TipoPendenzaDominioPost getValori() {
    return valori;
  }
  public void setValori(TipoPendenzaDominioPost valori) {
    this.valori = valori;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominio tipoPendenzaDominio = (TipoPendenzaDominio) o;
    return Objects.equals(idTipoPendenza, tipoPendenzaDominio.idTipoPendenza) &&
        Objects.equals(descrizione, tipoPendenzaDominio.descrizione) &&
        Objects.equals(tipo, tipoPendenzaDominio.tipo) &&
        Objects.equals(codificaIUV, tipoPendenzaDominio.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaDominio.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenzaDominio.abilitato) &&
        Objects.equals(portaleBackoffice, tipoPendenzaDominio.portaleBackoffice) &&
        Objects.equals(portalePagamento, tipoPendenzaDominio.portalePagamento) &&
        Objects.equals(avvisaturaMail, tipoPendenzaDominio.avvisaturaMail) &&
        Objects.equals(avvisaturaAppIO, tipoPendenzaDominio.avvisaturaAppIO) &&
        Objects.equals(visualizzazione, tipoPendenzaDominio.visualizzazione) &&
        Objects.equals(tracciatoCsv, tipoPendenzaDominio.tracciatoCsv) &&
        Objects.equals(valori, tipoPendenzaDominio.valori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, descrizione, tipo, codificaIUV, pagaTerzi, abilitato, portaleBackoffice, portalePagamento, avvisaturaMail, avvisaturaAppIO, visualizzazione, tracciatoCsv, valori);
  }

  public static TipoPendenzaDominio parse(String json) throws ServiceException, ValidationException{
    return parse(json, TipoPendenzaDominio.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominio";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominio {\n");
    
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    portaleBackoffice: ").append(toIndentedString(portaleBackoffice)).append("\n");
    sb.append("    portalePagamento: ").append(toIndentedString(portalePagamento)).append("\n");
    sb.append("    avvisaturaMail: ").append(toIndentedString(avvisaturaMail)).append("\n");
    sb.append("    avvisaturaAppIO: ").append(toIndentedString(avvisaturaAppIO)).append("\n");
    sb.append("    visualizzazione: ").append(toIndentedString(visualizzazione)).append("\n");
    sb.append("    tracciatoCsv: ").append(toIndentedString(tracciatoCsv)).append("\n");
    sb.append("    valori: ").append(toIndentedString(valori)).append("\n");
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



