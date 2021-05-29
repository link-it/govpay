package it.govpay.ec.v1.beans;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contabilita {

	private List<QuotaContabilita> quote = null;

	private Object proprietaCustom = null;

  /**
   **/
  public Contabilita quote(List<QuotaContabilita> quote) {
    this.quote = quote;
    return this;
  }

  @JsonProperty("quote")
  public List<QuotaContabilita> getQuote() {
    return quote;
  }
  public void setQuote(List<QuotaContabilita> quote) {
    this.quote = quote;
  }

  /**
   * Dati specifici del gestionale di contabilità
   **/
  public Contabilita proprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
    return this;
  }

  @JsonProperty("proprietaCustom")
  public Object getProprietaCustom() {
    return proprietaCustom;
  }
  public void setProprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Contabilita contabilita = (Contabilita) o;
    return Objects.equals(quote, contabilita.quote) &&
        Objects.equals(proprietaCustom, contabilita.proprietaCustom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quote, proprietaCustom);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Contabilita {\n");
    
    sb.append("    quote: ").append(toIndentedString(quote)).append("\n");
    sb.append("    proprietaCustom: ").append(toIndentedString(proprietaCustom)).append("\n");
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
