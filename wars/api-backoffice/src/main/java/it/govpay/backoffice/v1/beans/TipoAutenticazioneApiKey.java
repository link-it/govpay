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
package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"apiId",
"apiKey",
})
public class TipoAutenticazioneApiKey extends JSONSerializable {
  
  @JsonProperty("apiId")
  private String apiId = null;
  
  @JsonProperty("apiKey")
  private String apiKey = null;
  
  /**
   * valore da inserire all'interno dell'header previsto per l'API-ID
   **/
  public TipoAutenticazioneApiKey apiId(String apiId) {
    this.apiId = apiId;
    return this;
  }

  @JsonProperty("apiId")
  public String getApiId() {
    return apiId;
  }
  public void setApiId(String apiId) {
    this.apiId = apiId;
  }

  /**
   * valore da inserire all'interno dell'header previsto per l'API-KEY
   **/
  public TipoAutenticazioneApiKey apiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  @JsonProperty("apiKey")
  public String getApiKey() {
    return apiKey;
  }
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneApiKey tipoAutenticazioneApiKey = (TipoAutenticazioneApiKey) o;
    return Objects.equals(apiId, tipoAutenticazioneApiKey.apiId) &&
        Objects.equals(apiKey, tipoAutenticazioneApiKey.apiKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiId, apiKey);
  }

  public static TipoAutenticazioneApiKey parse(String json) throws IOException {
    return (TipoAutenticazioneApiKey) parse(json, TipoAutenticazioneApiKey.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneApiKey";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneApiKey {\n");
    
    sb.append("    apiId: ").append(toIndentedString(apiId)).append("\n");
    sb.append("    apiKey: ").append(toIndentedString(apiKey)).append("\n");
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



