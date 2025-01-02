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
package it.govpay.pendenze.v2.beans;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"tipo",
"descrizione",
"contenuto",
})
public class AllegatoPendenza extends JSONSerializable {

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("tipo")
  private String tipo = "application/octet-stream";

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("contenuto")
  private String contenuto = null;

  /**
   * nome del file
   **/
  public AllegatoPendenza nome(String nome) {
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
   * mime type del file
   **/
  public AllegatoPendenza tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * descrizione del file
   **/
  public AllegatoPendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * path per accedere al file, nella forma /allegati/{id}
   **/
  public AllegatoPendenza contenuto(String contenuto) {
    this.contenuto = contenuto;
    return this;
  }

  @JsonProperty("contenuto")
  public String getContenuto() {
    return contenuto;
  }
  public void setContenuto(String contenuto) {
    this.contenuto = contenuto;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllegatoPendenza allegatoPendenza = (AllegatoPendenza) o;
    return Objects.equals(nome, allegatoPendenza.nome) &&
        Objects.equals(tipo, allegatoPendenza.tipo) &&
        Objects.equals(descrizione, allegatoPendenza.descrizione) &&
        Objects.equals(contenuto, allegatoPendenza.contenuto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, tipo, descrizione, contenuto);
  }

  public static AllegatoPendenza parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, AllegatoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "allegatoPendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllegatoPendenza {\n");

    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    contenuto: ").append(toIndentedString(contenuto)).append("\n");
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



