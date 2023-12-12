/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.pagamento.v3.beans;

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
