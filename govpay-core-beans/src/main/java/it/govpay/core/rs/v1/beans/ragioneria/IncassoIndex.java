package it.govpay.core.rs.v1.beans.ragioneria;

import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"causale",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
"idDominio",
"idIncasso",
})
public class IncassoIndex extends JSONSerializable {
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("importo")
  private Double importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idIncasso")
  private String idIncasso = null;
  
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
  public IncassoIndex importo(Double importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public Double getImporto() {
    return this.importo;
  }
  public void setImporto(Double importo) {
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
   * Identificativo ente creditore
   **/
  public IncassoIndex idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
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
    return this.idIncasso;
  }
  public void setIdIncasso(String idIncasso) {
    this.idIncasso = idIncasso;
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
    return Objects.equals(this.causale, incassoIndex.causale) &&
        Objects.equals(this.importo, incassoIndex.importo) &&
        Objects.equals(this.dataValuta, incassoIndex.dataValuta) &&
        Objects.equals(this.dataContabile, incassoIndex.dataContabile) &&
        Objects.equals(this.ibanAccredito, incassoIndex.ibanAccredito) &&
        Objects.equals(this.idDominio, incassoIndex.idDominio) &&
        Objects.equals(this.idIncasso, incassoIndex.idIncasso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.causale, this.importo, this.dataValuta, this.dataContabile, this.ibanAccredito, this.idDominio, this.idIncasso);
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
    
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    dataValuta: ").append(this.toIndentedString(this.dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(this.toIndentedString(this.dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    idIncasso: ").append(this.toIndentedString(this.idIncasso)).append("\n");
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



