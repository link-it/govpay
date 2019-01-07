package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoAvviso;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import java.math.BigDecimal;
import org.joda.time.LocalDate;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Avviso  {
  
  @Schema(required = true, description = "")
  private StatoAvviso stato = null;
  
  @Schema(example = "10.01", required = true, description = "Importo della pendenza associata all'avviso.")
 /**
   * Importo della pendenza associata all'avviso.  
  **/
  private BigDecimal importo = null;
  
  @Schema(description = "")
  private Soggetto debitore = null;
  
  @Schema(required = true, description = "")
  private Dominio dominio = null;
  
  @Schema(example = "100000000000012", required = true, description = "Numero identificativo dell'avviso di pagamento")
 /**
   * Numero identificativo dell'avviso di pagamento  
  **/
  private String numeroAvviso = null;
  
  @Schema(required = true, description = "Data di validita dei dati dell'avviso, decorsa la quale l'importo può subire variazioni.")
 /**
   * Data di validita dei dati dell'avviso, decorsa la quale l'importo può subire variazioni.  
  **/
  private LocalDate dataValidita = null;
  
  @Schema(description = "Data di scadenza dell'avviso, decorsa la quale non è più pagabile.")
 /**
   * Data di scadenza dell'avviso, decorsa la quale non è più pagabile.  
  **/
  private LocalDate dataScadenza = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Descrizione dell'avviso di pagamento")
 /**
   * Descrizione dell'avviso di pagamento  
  **/
  private String descrizione = null;
  
  @Schema(required = true, description = "")
  private TassonomiaAvviso tassonomia = null;
  
  @Schema(required = true, description = "Testo da codificare nel qr-code dell'avviso")
 /**
   * Testo da codificare nel qr-code dell'avviso  
  **/
  private String qrcode = null;
  
  @Schema(required = true, description = "Testo da codificare nel bar-code dell'avviso")
 /**
   * Testo da codificare nel bar-code dell'avviso  
  **/
  private String barcode = null;
 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoAvviso getStato() {
    return stato;
  }

  public void setStato(StatoAvviso stato) {
    this.stato = stato;
  }

  public Avviso stato(StatoAvviso stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Importo della pendenza associata all&#x27;avviso.
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public Avviso importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Get debitore
   * @return debitore
  **/
  @JsonProperty("debitore")
  public Soggetto getDebitore() {
    return debitore;
  }

  public void setDebitore(Soggetto debitore) {
    this.debitore = debitore;
  }

  public Avviso debitore(Soggetto debitore) {
    this.debitore = debitore;
    return this;
  }

 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  @NotNull
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public Avviso dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Numero identificativo dell&#x27;avviso di pagamento
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
  @NotNull
  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public Avviso numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Data di validita dei dati dell&#x27;avviso, decorsa la quale l&#x27;importo può subire variazioni.
   * @return dataValidita
  **/
  @JsonProperty("dataValidita")
  @NotNull
  public LocalDate getDataValidita() {
    return dataValidita;
  }

  public void setDataValidita(LocalDate dataValidita) {
    this.dataValidita = dataValidita;
  }

  public Avviso dataValidita(LocalDate dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

 /**
   * Data di scadenza dell&#x27;avviso, decorsa la quale non è più pagabile.
   * @return dataScadenza
  **/
  @JsonProperty("dataScadenza")
  public LocalDate getDataScadenza() {
    return dataScadenza;
  }

  public void setDataScadenza(LocalDate dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public Avviso dataScadenza(LocalDate dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

 /**
   * Descrizione dell&#x27;avviso di pagamento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Avviso descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Get tassonomia
   * @return tassonomia
  **/
  @JsonProperty("tassonomia")
  @NotNull
  public TassonomiaAvviso getTassonomia() {
    return tassonomia;
  }

  public void setTassonomia(TassonomiaAvviso tassonomia) {
    this.tassonomia = tassonomia;
  }

  public Avviso tassonomia(TassonomiaAvviso tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

 /**
   * Testo da codificare nel qr-code dell&#x27;avviso
   * @return qrcode
  **/
  @JsonProperty("qrcode")
  @NotNull
  public String getQrcode() {
    return qrcode;
  }

  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  public Avviso qrcode(String qrcode) {
    this.qrcode = qrcode;
    return this;
  }

 /**
   * Testo da codificare nel bar-code dell&#x27;avviso
   * @return barcode
  **/
  @JsonProperty("barcode")
  @NotNull
  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Avviso barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Avviso {\n");
    
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    debitore: ").append(toIndentedString(debitore)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    qrcode: ").append(toIndentedString(qrcode)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
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
