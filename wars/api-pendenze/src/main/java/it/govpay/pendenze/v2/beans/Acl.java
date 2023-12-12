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
package it.govpay.pendenze.v2.beans;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"ruolo",
"principal",
"servizio",
"autorizzazioni",
})
public class Acl extends JSONSerializable {

  @JsonProperty("ruolo")
  private String ruolo = null;

  @JsonProperty("principal")
  private String principal = null;

  @JsonProperty("servizio")
  private TipoServizio servizio = null;

  @JsonProperty("autorizzazioni")
  private List<String> autorizzazioni = null;

  /**
   * ruolo a cui si applica l'acl
   **/
  public Acl ruolo(String ruolo) {
    this.ruolo = ruolo;
    return this;
  }

  @JsonProperty("ruolo")
  public String getRuolo() {
    return ruolo;
  }
  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  /**
   * principal a cui si applica l'acl
   **/
  public Acl principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   **/
  public Acl servizi(TipoServizio servizio) {
    this.servizio = servizio;
    return this;
  }

  @JsonProperty("servizio")
  public TipoServizio getServizio() {
    return servizio;
  }
  public void setServizio(TipoServizio servizio) {
    this.servizio = servizio;
  }

  /**
   **/
  public Acl autorizzazioni(List<String> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
    return this;
  }

  @JsonProperty("autorizzazioni")
  public List<String> getAutorizzazioni() {
    return autorizzazioni;
  }
  public void setAutorizzazioni(List<String> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Acl acl = (Acl) o;
    return Objects.equals(ruolo, acl.ruolo) &&
        Objects.equals(principal, acl.principal) &&
        Objects.equals(servizio, acl.servizio) &&
        Objects.equals(autorizzazioni, acl.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ruolo, principal, servizio, autorizzazioni);
  }

  public static Acl parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Acl.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "acl";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Acl {\n");

    sb.append("    ruolo: ").append(toIndentedString(ruolo)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    servizi: ").append(toIndentedString(servizio)).append("\n");
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
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



