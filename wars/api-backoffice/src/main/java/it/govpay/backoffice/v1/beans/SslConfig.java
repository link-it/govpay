/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"type",
"hostnameVerifier",
"truststore",
"keystore",
})
public class SslConfig extends JSONSerializable implements IValidable{

  @JsonProperty("abilitato")
  private Boolean abilitato = false;

  private SslConfigType typeEnum = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("hostnameVerifier")
  private Boolean hostnameVerifier = false;

  @JsonProperty("truststore")
  private Keystore truststore = null;

  @JsonProperty("keystore")
  private Keystore keystore = null;

  /**
   **/
  public SslConfig abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean getAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   **/
  public SslConfig typeEnum(SslConfigType type) {
    this.typeEnum = type;
    return this;
  }

  @JsonIgnore
  public SslConfigType getTypeEnum() {
    return typeEnum;
  }
  public void setTypeEnum(SslConfigType type) {
    this.typeEnum = type;
  }

  /**
   **/
  public SslConfig type(String type) {
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
  public SslConfig hostnameVerifier(Boolean hostnameVerifier) {
    this.hostnameVerifier = hostnameVerifier;
    return this;
  }

  @JsonProperty("hostnameVerifier")
  public Boolean getHostnameVerifier() {
    return hostnameVerifier;
  }
  public void setHostnameVerifier(Boolean hostnameVerifier) {
    this.hostnameVerifier = hostnameVerifier;
  }

  /**
   **/
  public SslConfig truststore(Keystore truststore) {
    this.truststore = truststore;
    return this;
  }

  @JsonProperty("truststore")
  public Keystore getTruststore() {
    return truststore;
  }
  public void setTruststore(Keystore truststore) {
    this.truststore = truststore;
  }

  /**
   **/
  public SslConfig keystore(Keystore keystore) {
    this.keystore = keystore;
    return this;
  }

  @JsonProperty("keystore")
  public Keystore getKeystore() {
    return keystore;
  }
  public void setKeystore(Keystore keystore) {
    this.keystore = keystore;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SslConfig sslConfig = (SslConfig) o;
    return Objects.equals(abilitato, sslConfig.abilitato) &&
        Objects.equals(type, sslConfig.type) &&
        Objects.equals(hostnameVerifier, sslConfig.hostnameVerifier) &&
        Objects.equals(truststore, sslConfig.truststore) &&
        Objects.equals(keystore, sslConfig.keystore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, type, hostnameVerifier, truststore, keystore);
  }

  public static SslConfig parse(String json) throws IOException {
    return parse(json, SslConfig.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "sslConfig";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SslConfig {\n");

    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    hostnameVerifier: ").append(toIndentedString(hostnameVerifier)).append("\n");
    sb.append("    truststore: ").append(toIndentedString(truststore)).append("\n");
    sb.append("    keystore: ").append(toIndentedString(keystore)).append("\n");
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

	vf.getValidator("abilitato", this.abilitato).notNull();

	if(this.abilitato) {
		vf.getValidator("type", this.type).notNull();

		if(SslConfigType.fromValue(this.type) == null)
			throw new ValidationException("Codifica inesistente per type. Valore fornito [" + this.type + "] valori possibili " + ArrayUtils.toString(SslConfigType.values()));

		vf.getValidator("hostnameVerifier", this.hostnameVerifier).notNull();
		vf.getValidator("truststore", this.truststore).validateFields();
		vf.getValidator("keystore", this.keystore).validateFields();
	}
  }
}



