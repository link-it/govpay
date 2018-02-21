package it.govpay.rs.v1.beans.base;

import java.util.Date;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"causale",
"importo",
"dataValuta",
"dataContabile",
"id",
"riscossioni",
})
public class Incasso extends it.govpay.rs.v1.beans.JSONSerializable {
  
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
  
  @JsonProperty("riscossioni")
  private String riscossioni = null;
  
  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public Incasso causale(String causale) {
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
  public Incasso importo(Double importo) {
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
  public Incasso dataValuta(Date dataValuta) {
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
  public Incasso dataContabile(Date dataContabile) {
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
  public Incasso id(String id) {
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

  /**
   * Url alle riscossioni incassate
   **/
  public Incasso riscossioni(String riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  @JsonProperty("riscossioni")
  public String getRiscossioni() {
    return riscossioni;
  }
  public void setRiscossioni(String riscossioni) {
    this.riscossioni = riscossioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Incasso incasso = (Incasso) o;
    return Objects.equals(causale, incasso.causale) &&
        Objects.equals(importo, incasso.importo) &&
        Objects.equals(dataValuta, incasso.dataValuta) &&
        Objects.equals(dataContabile, incasso.dataContabile) &&
        Objects.equals(id, incasso.id) &&
        Objects.equals(riscossioni, incasso.riscossioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(causale, importo, dataValuta, dataContabile, id, riscossioni);
  }

  public static Incasso parse(String json) {
    return (Incasso) parse(json, Incasso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incasso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Incasso {\n");
    
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
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



