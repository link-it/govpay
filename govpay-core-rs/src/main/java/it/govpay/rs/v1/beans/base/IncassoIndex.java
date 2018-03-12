package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.IncassoPost;
import java.util.Date;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"causale",
"importo",
"dataValuta",
"dataContabile",
"id",
})
public class IncassoIndex extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("importo")
  private Double importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("id")
  private String id = null;
  
  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public IncassoIndex causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return causale;
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
    return importo;
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
    return dataValuta;
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
    return dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo dell'incasso
   **/
  public IncassoIndex id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncassoIndex incassoIndex = (IncassoIndex) o;
    return Objects.equals(causale, incassoIndex.causale) &&
        Objects.equals(importo, incassoIndex.importo) &&
        Objects.equals(dataValuta, incassoIndex.dataValuta) &&
        Objects.equals(dataContabile, incassoIndex.dataContabile) &&
        Objects.equals(id, incassoIndex.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(causale, importo, dataValuta, dataContabile, id);
  }

  public static IncassoIndex parse(String json) {
    return (IncassoIndex) parse(json, IncassoIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incassoIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncassoIndex {\n");
    
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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



