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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"type",
"location",
"password",
"managementAlgorithm",
})
public class Keystore extends JSONSerializable implements IValidable {

  private KeystoreType typeEnum = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("managementAlgorithm")
  private String managementAlgorithm = null;

  /**
   **/
  public Keystore typeEnum(KeystoreType type) {
    this.typeEnum = type;
    return this;
  }

  @JsonIgnore
  public KeystoreType getTypeEnum() {
    return typeEnum;
  }
  public void setTypeEnum(KeystoreType type) {
    this.typeEnum = type;
  }

  /**
   **/
  public Keystore type(String type) {
    this.type = type;
    return this;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public Keystore location(String location) {
    this.location = location;
    return this;
  }

  @JsonProperty("location")
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   **/
  public Keystore password(String password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  public Keystore managementAlgorithm(String managementAlgorithm) {
    this.managementAlgorithm = managementAlgorithm;
    return this;
  }

  @JsonProperty("managementAlgorithm")
  public String getManagementAlgorithm() {
    return managementAlgorithm;
  }
  public void setManagementAlgorithm(String managementAlgorithm) {
    this.managementAlgorithm = managementAlgorithm;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Keystore keystore = (Keystore) o;
    return Objects.equals(type, keystore.type) &&
        Objects.equals(location, keystore.location) &&
        Objects.equals(password, keystore.password) &&
        Objects.equals(managementAlgorithm, keystore.managementAlgorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, location, password, managementAlgorithm);
  }

  public static Keystore parse(String json) throws IOException {
    return parse(json, Keystore.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "keystore";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Keystore {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    managementAlgorithm: ").append(toIndentedString(managementAlgorithm)).append("\n");
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

	// se e' stato compilato almeno un campo valido tutta la form
	if(StringUtils.isNotEmpty(this.type) || StringUtils.isNotEmpty(this.location) || StringUtils.isNotEmpty(this.password) || StringUtils.isNotEmpty(this.managementAlgorithm)) {
		vf.getValidator("type", this.type).notNull();

		if(KeystoreType.fromValue(this.type) == null){
			throw new ValidationException("Codifica inesistente per type. Valore fornito [" + this.type + "] valori possibili " + ArrayUtils.toString(KeystoreType.values()));
		}

		vf.getValidator("location", this.location).notNull().minLength(1).maxLength(1024);
		vf.getValidator("password", this.password).minLength(1).maxLength(255);
		vf.getValidator("managementAlgorithm", this.managementAlgorithm).minLength(1).maxLength(255);
	}
  }
}



