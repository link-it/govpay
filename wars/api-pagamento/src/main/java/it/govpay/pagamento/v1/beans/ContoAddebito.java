/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.pagamento.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
 **/

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"iban",
"bic",
})
public class ContoAddebito extends JSONSerializable implements IValidable {

  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("bic")
  private String bic = null;

  /**
   * Iban di addebito del pagatore.
   **/
  public ContoAddebito iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return this.iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   * Bic della banca di addebito del pagatore.
   **/
  public ContoAddebito bic(String bic) {
    this.bic = bic;
    return this;
  }

  @JsonProperty("bic")
  public String getBic() {
    return this.bic;
  }
  public void setBic(String bic) {
    this.bic = bic;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ContoAddebito contoAddebito = (ContoAddebito) o;
    return Objects.equals(this.iban, contoAddebito.iban) &&
        Objects.equals(this.bic, contoAddebito.bic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.iban, this.bic);
  }

  public static ContoAddebito parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, ContoAddebito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contoAddebito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContoAddebito {\n");

    sb.append("    iban: ").append(this.toIndentedString(this.iban)).append("\n");
    sb.append("    bic: ").append(this.toIndentedString(this.bic)).append("\n");
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
	  vf.getValidator("iban", this.iban).notNull().pattern("[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}");
	  vf.getValidator("bic", this.bic).pattern("[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}");
  }

}



