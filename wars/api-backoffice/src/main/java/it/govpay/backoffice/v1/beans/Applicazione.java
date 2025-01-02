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

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"principal",
"password",
"codificaAvvisi",
"domini",
"tipiPendenza",
"apiPagamenti",
"apiPendenze",
"apiRagioneria",
"acl",
"ruoli",
"servizioIntegrazione",
"abilitato",
})
public class Applicazione extends it.govpay.core.beans.JSONSerializable {

  @JsonProperty("idA2A")
  private String idA2A = null;

  @JsonProperty("principal")
  private String principal = null;

  @JsonProperty("password")
  private Boolean password = null;

  @JsonProperty("codificaAvvisi")
  private CodificaAvvisi codificaAvvisi = null;

  @JsonProperty("domini")
  private List<DominioProfiloIndex> domini = null;

  @JsonProperty("tipiPendenza")
  private List<TipoPendenza> tipiPendenza = null;

  @JsonProperty("apiPagamenti")
  private Boolean apiPagamenti = false;

  @JsonProperty("apiPendenze")
  private Boolean apiPendenze = false;

  @JsonProperty("apiRagioneria")
  private Boolean apiRagioneria = false;

  @JsonProperty("acl")
  private List<AclPost> acl = null;

  @JsonProperty("ruoli")
  private List<Ruolo> ruoli = null;

  @JsonProperty("servizioIntegrazione")
  private Connector servizioIntegrazione = null;

  @JsonProperty("abilitato")
  private Boolean abilitato = true;

  /**
   * Identificativo dell'applicazione
   **/
  public Applicazione idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo di autenticazione
   **/
  public Applicazione principal(String principal) {
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
   * Indica se e' stata configurata una password per l'accesso con HTTP-Basic.
   **/
  public Applicazione password(Boolean password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public Boolean getPassword() {
    return password;
  }
  public void setPassword(Boolean password) {
    this.password = password;
  }

  /**
   **/
  public Applicazione codificaAvvisi(CodificaAvvisi codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
    return this;
  }

  @JsonProperty("codificaAvvisi")
  public CodificaAvvisi getCodificaAvvisi() {
    return codificaAvvisi;
  }
  public void setCodificaAvvisi(CodificaAvvisi codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public Applicazione domini(List<DominioProfiloIndex> domini) {
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
  public Applicazione tipiPendenza(List<TipoPendenza> tipiPendenza) {
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
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Pagamento
   **/
  public Applicazione apiPagamenti(Boolean apiPagamenti) {
    this.apiPagamenti = apiPagamenti;
    return this;
  }

  @JsonProperty("apiPagamenti")
  public Boolean getApiPagamenti() {
    return apiPagamenti;
  }
  public void setApiPagamenti(Boolean apiPagamenti) {
    this.apiPagamenti = apiPagamenti;
  }

  /**
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Pendenze
   **/
  public Applicazione apiPendenze(Boolean apiPendenze) {
    this.apiPendenze = apiPendenze;
    return this;
  }

  @JsonProperty("apiPendenze")
  public Boolean getApiPendenze() {
    return apiPendenze;
  }
  public void setApiPendenze(Boolean apiPendenze) {
    this.apiPendenze = apiPendenze;
  }

  /**
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Ragioneria
   **/
  public Applicazione apiRagioneria(Boolean apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
    return this;
  }

  @JsonProperty("apiRagioneria")
  public Boolean getApiRagioneria() {
    return apiRagioneria;
  }
  public void setApiRagioneria(Boolean apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
  }

  /**
   * lista delle acl attive sull'applicazione
   **/
  public Applicazione acl(List<AclPost> acl) {
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
   * lista dei ruoli attivi sull'applicazione
   **/
  public Applicazione ruoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
    return this;
  }

  @JsonProperty("ruoli")
  public List<Ruolo> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
  }

  /**
   **/
  public Applicazione servizioIntegrazione(Connector servizioIntegrazione) {
    this.servizioIntegrazione = servizioIntegrazione;
    return this;
  }

  @JsonProperty("servizioIntegrazione")
  public Connector getServizioIntegrazione() {
    return servizioIntegrazione;
  }
  public void setServizioIntegrazione(Connector servizioIntegrazione) {
    this.servizioIntegrazione = servizioIntegrazione;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public Applicazione abilitato(Boolean abilitato) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Applicazione applicazione = (Applicazione) o;
    return Objects.equals(idA2A, applicazione.idA2A) &&
        Objects.equals(principal, applicazione.principal) &&
        Objects.equals(password, applicazione.password) &&
        Objects.equals(codificaAvvisi, applicazione.codificaAvvisi) &&
        Objects.equals(domini, applicazione.domini) &&
        Objects.equals(tipiPendenza, applicazione.tipiPendenza) &&
        Objects.equals(apiPagamenti, applicazione.apiPagamenti) &&
        Objects.equals(apiPendenze, applicazione.apiPendenze) &&
        Objects.equals(apiRagioneria, applicazione.apiRagioneria) &&
        Objects.equals(acl, applicazione.acl) &&
        Objects.equals(ruoli, applicazione.ruoli) &&
        Objects.equals(servizioIntegrazione, applicazione.servizioIntegrazione) &&
        Objects.equals(abilitato, applicazione.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, principal, password, codificaAvvisi, domini, tipiPendenza, apiPagamenti, apiPendenze, apiRagioneria, acl, ruoli, servizioIntegrazione, abilitato);
  }

  public static Applicazione parse(String json) throws IOException {
    return parse(json, Applicazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "applicazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Applicazione {\n");

    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    codificaAvvisi: ").append(toIndentedString(codificaAvvisi)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    apiPagamenti: ").append(toIndentedString(apiPagamenti)).append("\n");
    sb.append("    apiPendenze: ").append(toIndentedString(apiPendenze)).append("\n");
    sb.append("    apiRagioneria: ").append(toIndentedString(apiRagioneria)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
    sb.append("    servizioIntegrazione: ").append(toIndentedString(servizioIntegrazione)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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



