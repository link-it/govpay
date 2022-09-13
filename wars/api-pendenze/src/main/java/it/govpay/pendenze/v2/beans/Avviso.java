package it.govpay.pendenze.v2.beans;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"stato",
"importo",
"idDominio",
"numeroAvviso",
"dataValidita",
"dataScadenza",
"dataPagamento",
"descrizione",
"tassonomiaAvviso",
"qrcode",
"barcode",
})
public class Avviso extends JSONSerializable {

  @JsonProperty("stato")
  private StatoAvviso stato = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;

  @JsonProperty("dataValidita")
  private Date dataValidita = null;

  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;

  @JsonProperty("dataPagamento")
  private Date dataPagamento = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("tassonomiaAvviso")
  private TassonomiaAvviso tassonomiaAvviso = null;

  @JsonProperty("qrcode")
  private String qrcode = null;

  @JsonProperty("barcode")
  private String barcode = null;

  /**
   **/
  public Avviso stato(StatoAvviso stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoAvviso getStato() {
    return stato;
  }
  public void setStato(StatoAvviso stato) {
    this.stato = stato;
  }

  /**
   * Importo della pendenza associata all'avviso.
   **/
  public Avviso importo(BigDecimal importo) {
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

  /**
   * Identificativo del creditore dell'avviso
   **/
  public Avviso idDominio(String idDominio) {
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
   * Numero identificativo dell'avviso di pagamento
   **/
  public Avviso numeroAvviso(String numeroAvviso) {
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

  /**
   * Data di validita dei dati dell'avviso, decorsa la quale l'importo può subire variazioni.
   **/
  public Avviso dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza dell'avviso, decorsa la quale non è più pagabile.
   **/
  public Avviso dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Data di pagamento dell'avviso.
   **/
  public Avviso dataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

  @JsonProperty("dataPagamento")
  public Date getDataPagamento() {
    return dataPagamento;
  }
  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public Avviso descrizione(String descrizione) {
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
  public Avviso tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public TassonomiaAvviso getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Testo da codificare nel qr-code dell'avviso
   **/
  public Avviso qrcode(String qrcode) {
    this.qrcode = qrcode;
    return this;
  }

  @JsonProperty("qrcode")
  public String getQrcode() {
    return qrcode;
  }
  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  /**
   * Testo da codificare nel bar-code dell'avviso
   **/
  public Avviso barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  @JsonProperty("barcode")
  public String getBarcode() {
    return barcode;
  }
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Avviso avviso = (Avviso) o;
    return Objects.equals(stato, avviso.stato) &&
        Objects.equals(importo, avviso.importo) &&
        Objects.equals(idDominio, avviso.idDominio) &&
        Objects.equals(numeroAvviso, avviso.numeroAvviso) &&
        Objects.equals(dataValidita, avviso.dataValidita) &&
        Objects.equals(dataScadenza, avviso.dataScadenza) &&
        Objects.equals(dataPagamento, avviso.dataPagamento) &&
        Objects.equals(descrizione, avviso.descrizione) &&
        Objects.equals(tassonomiaAvviso, avviso.tassonomiaAvviso) &&
        Objects.equals(qrcode, avviso.qrcode) &&
        Objects.equals(barcode, avviso.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stato, importo, idDominio, numeroAvviso, dataValidita, dataScadenza, dataPagamento, descrizione, tassonomiaAvviso, qrcode, barcode);
  }

  public static Avviso parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Avviso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "avviso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Avviso {\n");

    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    qrcode: ").append(toIndentedString(qrcode)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
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



