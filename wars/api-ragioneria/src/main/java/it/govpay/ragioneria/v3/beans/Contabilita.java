package it.govpay.ragioneria.v3.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Contabilita   {
  
  @Schema(description = "")
  private List<QuotaContabilita> quote = null;
  
  @Schema(description = "Dati specifici del gestionale di contabilità")
 /**
   * Dati specifici del gestionale di contabilità  
  **/
  private Object proprietaCustom = null;
 /**
   * Get quote
   * @return quote
  **/
  @JsonProperty("quote")
  public List<QuotaContabilita> getQuote() {
    return quote;
  }

  public void setQuote(List<QuotaContabilita> quote) {
    this.quote = quote;
  }

  public Contabilita quote(List<QuotaContabilita> quote) {
    this.quote = quote;
    return this;
  }

  public Contabilita addQuoteItem(QuotaContabilita quoteItem) {
    this.quote.add(quoteItem);
    return this;
  }

 /**
   * Dati specifici del gestionale di contabilità
   * @return proprietaCustom
  **/
  @JsonProperty("proprietaCustom")
  public Object getProprietaCustom() {
    return proprietaCustom;
  }

  public void setProprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
  }

  public Contabilita proprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
    return this;
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
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
