package it.govpay.core.rs.v1.beans.ragioneria;

import java.util.Objects;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.utils.SimpleDateFormatUtils;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"causale",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
})
public class IncassoPost extends JSONSerializable {
  
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
    return causale;
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
    return importo;
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
    return dataValuta;
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
    return dataContabile;
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
    return ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncassoPost incassoPost = (IncassoPost) o;
    return Objects.equals(causale, incassoPost.causale) &&
        Objects.equals(importo, incassoPost.importo) &&
        Objects.equals(dataValuta, incassoPost.dataValuta) &&
        Objects.equals(dataContabile, incassoPost.dataContabile) &&
        Objects.equals(ibanAccredito, incassoPost.ibanAccredito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(causale, importo, dataValuta, dataContabile, ibanAccredito);
  }

  public static IncassoPost parse(String json) {
    return (IncassoPost) parse(json, IncassoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incassoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncassoPost {\n");
    
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
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
  
  
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}

}



