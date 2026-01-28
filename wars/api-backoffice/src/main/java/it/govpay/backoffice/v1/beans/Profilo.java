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
package it.govpay.backoffice.v1.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"domini",
"tipiPendenza",
"acl",
"autenticazione",
})
public class Profilo extends JSONSerializable {

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("domini")
  private List<DominioProfiloIndex> domini = new ArrayList<>();

  @JsonProperty("tipiPendenza")
  private List<TipoPendenza> tipiPendenza = new ArrayList<>();

  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<>();

  @JsonProperty("autenticazione")
  private String autenticazione = null;

  /**
   * Nome dell'utenza
   **/
  public Profilo nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public Profilo domini(List<DominioProfiloIndex> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<DominioProfiloIndex> getDomini() {
    return domini;
  }
  public void setDomini(List<DominioProfiloIndex> domini) {
    this.domini = domini;
  }

  /**
   * tipologie di pendenza su cui e' abilitato ad operare
   **/
  public Profilo tipiPendenza(List<TipoPendenza> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenza> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenza> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  /**
   **/
  public Profilo acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  /**
   * tipo autenticazione utilizzata per loggarsi nel sistema
   **/
  public Profilo autenticazione(String autenticazione) {
    this.autenticazione = autenticazione;
    return this;
  }

  @JsonProperty("autenticazione")
  public String getAutenticazione() {
    return autenticazione;
  }
  public void setAutenticazione(String autenticazione) {
    this.autenticazione = autenticazione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(nome, profilo.nome) &&
        Objects.equals(domini, profilo.domini) &&
        Objects.equals(tipiPendenza, profilo.tipiPendenza) &&
        Objects.equals(acl, profilo.acl) &&
        Objects.equals(autenticazione, profilo.autenticazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, domini, tipiPendenza, acl, autenticazione);
  }

  public static Profilo parse(String json) throws IOException {
    return parse(json, Profilo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "profilo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");

    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    autenticazione: ").append(toIndentedString(autenticazione)).append("\n");
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



