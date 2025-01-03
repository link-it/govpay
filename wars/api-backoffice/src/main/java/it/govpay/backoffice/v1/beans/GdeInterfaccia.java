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
"letture",
"scritture",
})
public class GdeInterfaccia extends JSONSerializable implements IValidable{

  @JsonProperty("letture")
  private GdeEvento letture = null;

  @JsonProperty("scritture")
  private GdeEvento scritture = null;

  /**
   **/
  public GdeInterfaccia letture(GdeEvento letture) {
    this.letture = letture;
    return this;
  }

  @JsonProperty("letture")
  public GdeEvento getLetture() {
    return letture;
  }
  public void setLetture(GdeEvento letture) {
    this.letture = letture;
  }

  /**
   **/
  public GdeInterfaccia scritture(GdeEvento scritture) {
    this.scritture = scritture;
    return this;
  }

  @JsonProperty("scritture")
  public GdeEvento getScritture() {
    return scritture;
  }
  public void setScritture(GdeEvento scritture) {
    this.scritture = scritture;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdeInterfaccia gdeInterfaccia = (GdeInterfaccia) o;
    return Objects.equals(letture, gdeInterfaccia.letture) &&
        Objects.equals(scritture, gdeInterfaccia.scritture);
  }

  @Override
  public int hashCode() {
    return Objects.hash(letture, scritture);
  }

  public static GdeInterfaccia parse(String json) throws IOException {
	    return parse(json, GdeInterfaccia.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "gdeInterfaccia";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdeInterfaccia {\n");

    sb.append("    letture: ").append(toIndentedString(letture)).append("\n");
    sb.append("    scritture: ").append(toIndentedString(scritture)).append("\n");
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
		vf.getValidator("letture", this.letture).notNull().validateFields();
		vf.getValidator("scritture", this.scritture).notNull().validateFields();
 	}
}



