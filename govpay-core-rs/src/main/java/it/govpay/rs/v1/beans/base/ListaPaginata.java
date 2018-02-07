package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import java.math.BigDecimal;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"numRisultati",
"numPagine",
"risultatiPerPagina",
"pagina",
"prossimiRisultati",
})
public class ListaPaginata extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("numRisultati")
  private BigDecimal numRisultati = null;
  
  @JsonProperty("numPagine")
  private BigDecimal numPagine = null;
  
  @JsonProperty("risultatiPerPagina")
  private BigDecimal risultatiPerPagina = null;
  
  @JsonProperty("pagina")
  private BigDecimal pagina = null;
  
  @JsonProperty("prossimiRisultati")
  private String prossimiRisultati = null;
  
  /**
   **/
  public ListaPaginata numRisultati(BigDecimal numRisultati) {
    this.numRisultati = numRisultati;
    return this;
  }

  @JsonProperty("numRisultati")
  public BigDecimal getNumRisultati() {
    return numRisultati;
  }
  public void setNumRisultati(BigDecimal numRisultati) {
    this.numRisultati = numRisultati;
  }

  /**
   **/
  public ListaPaginata numPagine(BigDecimal numPagine) {
    this.numPagine = numPagine;
    return this;
  }

  @JsonProperty("numPagine")
  public BigDecimal getNumPagine() {
    return numPagine;
  }
  public void setNumPagine(BigDecimal numPagine) {
    this.numPagine = numPagine;
  }

  /**
   **/
  public ListaPaginata risultatiPerPagina(BigDecimal risultatiPerPagina) {
    this.risultatiPerPagina = risultatiPerPagina;
    return this;
  }

  @JsonProperty("risultatiPerPagina")
  public BigDecimal getRisultatiPerPagina() {
    return risultatiPerPagina;
  }
  public void setRisultatiPerPagina(BigDecimal risultatiPerPagina) {
    this.risultatiPerPagina = risultatiPerPagina;
  }

  /**
   **/
  public ListaPaginata pagina(BigDecimal pagina) {
    this.pagina = pagina;
    return this;
  }

  @JsonProperty("pagina")
  public BigDecimal getPagina() {
    return pagina;
  }
  public void setPagina(BigDecimal pagina) {
    this.pagina = pagina;
  }

  /**
   **/
  public ListaPaginata prossimiRisultati(String prossimiRisultati) {
    this.prossimiRisultati = prossimiRisultati;
    return this;
  }

  @JsonProperty("prossimiRisultati")
  public String getProssimiRisultati() {
    return prossimiRisultati;
  }
  public void setProssimiRisultati(String prossimiRisultati) {
    this.prossimiRisultati = prossimiRisultati;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListaPaginata listaPaginata = (ListaPaginata) o;
    return Objects.equals(numRisultati, listaPaginata.numRisultati) &&
        Objects.equals(numPagine, listaPaginata.numPagine) &&
        Objects.equals(risultatiPerPagina, listaPaginata.risultatiPerPagina) &&
        Objects.equals(pagina, listaPaginata.pagina) &&
        Objects.equals(prossimiRisultati, listaPaginata.prossimiRisultati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numRisultati, numPagine, risultatiPerPagina, pagina, prossimiRisultati);
  }

  public static ListaPaginata parse(String json) {
    return (ListaPaginata) parse(json, ListaPaginata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "listaPaginata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListaPaginata {\n");
    
    sb.append("    numRisultati: ").append(toIndentedString(numRisultati)).append("\n");
    sb.append("    numPagine: ").append(toIndentedString(numPagine)).append("\n");
    sb.append("    risultatiPerPagina: ").append(toIndentedString(risultatiPerPagina)).append("\n");
    sb.append("    pagina: ").append(toIndentedString(pagina)).append("\n");
    sb.append("    prossimiRisultati: ").append(toIndentedString(prossimiRisultati)).append("\n");
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



