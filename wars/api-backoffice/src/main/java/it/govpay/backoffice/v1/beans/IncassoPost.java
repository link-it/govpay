package it.govpay.backoffice.v1.beans;

import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"causale",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
})
public class IncassoPost extends it.govpay.core.beans.JSONSerializable {
  
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
  
  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public IncassoPost causale(String causale) {
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
  public IncassoPost importo(Double importo) {
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
  public IncassoPost dataValuta(Date dataValuta) {
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
  public IncassoPost dataContabile(Date dataContabile) {
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
  public IncassoPost ibanAccredito(String ibanAccredito) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    IncassoPost incassoPost = (IncassoPost) o;
    return Objects.equals(this.causale, incassoPost.causale) &&
        Objects.equals(this.importo, incassoPost.importo) &&
        Objects.equals(this.dataValuta, incassoPost.dataValuta) &&
        Objects.equals(this.dataContabile, incassoPost.dataContabile) &&
        Objects.equals(this.ibanAccredito, incassoPost.ibanAccredito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.causale, this.importo, this.dataValuta, this.dataContabile, this.ibanAccredito);
  }

  public static IncassoPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, IncassoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incassoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncassoPost {\n");
    
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    dataValuta: ").append(this.toIndentedString(this.dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(this.toIndentedString(this.dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
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



