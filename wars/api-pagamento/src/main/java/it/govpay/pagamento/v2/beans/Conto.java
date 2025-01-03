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

import java.util.Objects;

import it.govpay.core.exceptions.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

/**
 * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
 **/

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"iban",
"bic",
})
public class Conto extends JSONSerializable implements IValidable {
  
  @JsonProperty("iban")
  private String iban = null;
  
  @JsonProperty("bic")
  private String bic = null;
  
  /**
   **/
  public Conto iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   **/
  public Conto bic(String bic) {
    this.bic = bic;
    return this;
  }

  @JsonProperty("bic")
  public String getBic() {
    return bic;
  }
  public void setBic(String bic) {
    this.bic = bic;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Conto conto = (Conto) o;
    return Objects.equals(iban, conto.iban) &&
        Objects.equals(bic, conto.bic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, bic);
  }

  public static Conto parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Conto.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "conto";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Conto {\n");
    
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    bic: ").append(toIndentedString(bic)).append("\n");
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
	  
	  ValidatoreIdentificativi vf = new ValidatoreIdentificativi();
	  vf.validaIdIbanAccredito("iban", this.iban);
	  if(this.bic != null)
		  vf.validaBicAccredito("bic", this.bic);
  }
  
}



