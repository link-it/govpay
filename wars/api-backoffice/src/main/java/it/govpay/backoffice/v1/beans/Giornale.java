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
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"interfacce",
})
public class Giornale extends JSONSerializable implements IValidable {

  @JsonProperty("interfacce")
  private GdeInterfacce interfacce = null;

  /**
   **/
  public Giornale interfacce(GdeInterfacce interfacce) {
    this.interfacce = interfacce;
    return this;
  }

  @JsonProperty("interfacce")
  public GdeInterfacce getInterfacce() {
    return interfacce;
  }
  public void setInterfacce(GdeInterfacce interfacce) {
    this.interfacce = interfacce;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Giornale giornale = (Giornale) o;
    return Objects.equals(interfacce, giornale.interfacce);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interfacce);
  }

  public static Giornale parse(String json) throws IOException {
	    return parse(json, Giornale.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "giornale";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Giornale {\n");

    sb.append("    interfacce: ").append(toIndentedString(interfacce)).append("\n");
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
		vf.getValidator("interfacce", this.interfacce).notNull().validateFields();
 	}
}



