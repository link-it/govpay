package it.govpay.ragioneria.v2.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"importo",
"dataValuta",
"dataContabile",
"contoAccredito",
"sct",
"idDominio",
"idRiconciliazione",
"riscossioni",
"causale",
"iuv",
"idFlusso",
})
public class Riconciliazione extends JSONSerializable {
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("contoAccredito")
  private String contoAccredito = null;
  
  @JsonProperty("sct")
  private String sct = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idRiconciliazione")
  private String idRiconciliazione = null;
  
  @JsonProperty("riscossioni")
  private List<RiscossioneIndex> riscossioni = new ArrayList<>();
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("idFlusso")
  private String idFlusso = null;

  /**
   **/
  public Riconciliazione importo(BigDecimal importo) {
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
   * Data di valuta dell'incasso
   **/
  public Riconciliazione dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public Riconciliazione dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public Riconciliazione contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }
  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  /**
   * Identificativo Sepa Credit Transfer
   **/
  public Riconciliazione sct(String sct) {
    this.sct = sct;
    return this;
  }

  @JsonProperty("sct")
  public String getSct() {
    return sct;
  }
  public void setSct(String sct) {
    this.sct = sct;
  }

  /**
   * Identificativo ente creditore
   **/
  public Riconciliazione idDominio(String idDominio) {
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
   * Identificativo della riconciliazione assegnato da GovPay
   **/
  public Riconciliazione idRiconciliazione(String idRiconciliazione) {
    this.idRiconciliazione = idRiconciliazione;
    return this;
  }

  @JsonProperty("idRiconciliazione")
  public String getIdRiconciliazione() {
    return idRiconciliazione;
  }
  public void setIdRiconciliazione(String idRiconciliazione) {
    this.idRiconciliazione = idRiconciliazione;
  }

  /**
   **/
  public Riconciliazione riscossioni(List<RiscossioneIndex> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  @JsonProperty("riscossioni")
  public List<RiscossioneIndex> getRiscossioni() {
    return riscossioni;
  }
  public void setRiscossioni(List<RiscossioneIndex> riscossioni) {
    this.riscossioni = riscossioni;
  }

  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public Riconciliazione causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }
  
  /**
   * Identificativo univoco di riscossione.
   **/
  public Riconciliazione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return this.iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo del flusso di rendicontazione.
   **/
  public Riconciliazione idFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
    return this;
  }

  @JsonProperty("idFlusso")
  public String getIdFlusso() {
    return this.idFlusso;
  }
  public void setIdFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Riconciliazione riconciliazione = (Riconciliazione) o;
    return Objects.equals(importo, riconciliazione.importo) &&
        Objects.equals(dataValuta, riconciliazione.dataValuta) &&
        Objects.equals(dataContabile, riconciliazione.dataContabile) &&
        Objects.equals(contoAccredito, riconciliazione.contoAccredito) &&
        Objects.equals(sct, riconciliazione.sct) &&
        Objects.equals(idDominio, riconciliazione.idDominio) &&
        Objects.equals(idRiconciliazione, riconciliazione.idRiconciliazione) &&
        Objects.equals(riscossioni, riconciliazione.riscossioni) &&
        Objects.equals(this.causale, riconciliazione.causale) &&
        Objects.equals(this.iuv, riconciliazione.iuv) &&
        Objects.equals(this.idFlusso, riconciliazione.idFlusso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(importo, dataValuta, dataContabile, contoAccredito, sct, idDominio, idRiconciliazione, riscossioni, causale, iuv, idFlusso);
  }

  public static Riconciliazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Riconciliazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "riconciliazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riconciliazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    contoAccredito: ").append(toIndentedString(contoAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idRiconciliazione: ").append(toIndentedString(idRiconciliazione)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
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



