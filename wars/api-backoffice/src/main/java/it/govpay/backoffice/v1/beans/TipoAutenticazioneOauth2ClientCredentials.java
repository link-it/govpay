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
"clientId",
"clientSecret",
"urlTokenEndpoint",
"scope",
})
public class TipoAutenticazioneOauth2ClientCredentials extends JSONSerializable {
  
  @JsonProperty("clientId")
  private String clientId = null;
  
  @JsonProperty("clientSecret")
  private String clientSecret = null;
  
  @JsonProperty("urlTokenEndpoint")
  private String urlTokenEndpoint = null;
  
  @JsonProperty("scope")
  private String scope = null;
  
  /**
   * Identificativo dell'applicazione da inviare all'authorization server
   **/
  public TipoAutenticazioneOauth2ClientCredentials clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  @JsonProperty("clientId")
  public String getClientId() {
    return clientId;
  }
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Password assegnata all'applicazione da inviare all'authorization server
   **/
  public TipoAutenticazioneOauth2ClientCredentials clientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

  @JsonProperty("clientSecret")
  public String getClientSecret() {
    return clientSecret;
  }
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  /**
   * URL del server dove fare la chiamata di richiesta del token
   **/
  public TipoAutenticazioneOauth2ClientCredentials urlTokenEndpoint(String urlTokenEndpoint) {
    this.urlTokenEndpoint = urlTokenEndpoint;
    return this;
  }

  @JsonProperty("urlTokenEndpoint")
  public String getUrlTokenEndpoint() {
    return urlTokenEndpoint;
  }
  public void setUrlTokenEndpoint(String urlTokenEndpoint) {
    this.urlTokenEndpoint = urlTokenEndpoint;
  }

  /**
   * Livello di accesso richiesto per l'operazione da eseguire
   **/
  public TipoAutenticazioneOauth2ClientCredentials scope(String scope) {
    this.scope = scope;
    return this;
  }

  @JsonProperty("scope")
  public String getScope() {
    return scope;
  }
  public void setScope(String scope) {
    this.scope = scope;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneOauth2ClientCredentials tipoAutenticazioneOauth2ClientCredentials = (TipoAutenticazioneOauth2ClientCredentials) o;
    return Objects.equals(clientId, tipoAutenticazioneOauth2ClientCredentials.clientId) &&
        Objects.equals(clientSecret, tipoAutenticazioneOauth2ClientCredentials.clientSecret) &&
        Objects.equals(urlTokenEndpoint, tipoAutenticazioneOauth2ClientCredentials.urlTokenEndpoint) &&
        Objects.equals(scope, tipoAutenticazioneOauth2ClientCredentials.scope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, clientSecret, urlTokenEndpoint, scope);
  }

  public static TipoAutenticazioneOauth2ClientCredentials parse(String json) throws IOException {
    return (TipoAutenticazioneOauth2ClientCredentials) parse(json, TipoAutenticazioneOauth2ClientCredentials.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneOauth2ClientCredentials";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneOauth2ClientCredentials {\n");
    
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
    sb.append("    urlTokenEndpoint: ").append(toIndentedString(urlTokenEndpoint)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
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



