package it.govpay.ragioneria.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"idIncasso",
"causale",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
"sct",
})
public class IncassoIndex extends JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idIncasso")
  private String idIncasso = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("sct")
  private String sct = null;
  
  /**
   * Identificativo ente creditore
   **/
  public IncassoIndex idDominio(String idDominio) {
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
   * Identificativo dell'incasso
   **/
  public IncassoIndex idIncasso(String idIncasso) {
    this.idIncasso = idIncasso;
    return this;
  }

  @JsonProperty("idIncasso")
  public String getIdIncasso() {
    return idIncasso;
  }
  public void setIdIncasso(String idIncasso) {
    this.idIncasso = idIncasso;
  }

  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public IncassoIndex causale(String causale) {
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
   **/
  public IncassoIndex importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Data di valuta dell'incasso
   **/
  public IncassoIndex dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return this.dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public IncassoIndex dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return this.dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public IncassoIndex ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   * Identificativo Sepa Credit Transfer
   **/
  public IncassoIndex sct(String sct) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    IncassoIndex incassoIndex = (IncassoIndex) o;
    return Objects.equals(idDominio, incassoIndex.idDominio) &&
        Objects.equals(idIncasso, incassoIndex.idIncasso) &&
        Objects.equals(causale, incassoIndex.causale) &&
        Objects.equals(importo, incassoIndex.importo) &&
        Objects.equals(dataValuta, incassoIndex.dataValuta) &&
        Objects.equals(dataContabile, incassoIndex.dataContabile) &&
        Objects.equals(ibanAccredito, incassoIndex.ibanAccredito) &&
        Objects.equals(sct, incassoIndex.sct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, idIncasso, causale, importo, dataValuta, dataContabile, ibanAccredito, sct);
  }

  public static IncassoIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, IncassoIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incassoIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncassoIndex {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idIncasso: ").append(toIndentedString(idIncasso)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
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



