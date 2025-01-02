/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.pagamento.v2.beans;


import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"quote",
"proprietaCustom",
})
public class Contabilita extends JSONSerializable implements IValidable {

  @JsonProperty("quote")
  private List<QuotaContabilita> quote = null;

  @JsonProperty("proprietaCustom")
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

  public static Contabilita parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Contabilita.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contabilita";
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

  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();

		vf.getValidator("quote", this.quote).validateObjects();
}
}



