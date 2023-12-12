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
package it.govpay.ec.v2.beans;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class NuovoAllegatoPendenza   {
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "nome del file")
 /**
   * nome del file  
  **/
  private String nome = null;
  
  @Schema(description = "mime type del file")
 /**
   * mime type del file  
  **/
  private String tipo = "application/octet-stream";
  
  @Schema(description = "descrizione del file")
 /**
   * descrizione del file  
  **/
  private String descrizione = null;
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "contenuto del file")
 /**
   * contenuto del file  
  **/
  private byte[] contenuto = null;
 /**
   * nome del file
   * @return nome
  **/
  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public NuovoAllegatoPendenza nome(String nome) {
    this.nome = nome;
    return this;
  }

 /**
   * mime type del file
   * @return tipo
  **/
  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public NuovoAllegatoPendenza tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * descrizione del file
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public NuovoAllegatoPendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * contenuto del file
   * @return contenuto
  **/
  @JsonProperty("contenuto")
  public byte[] getContenuto() {
    return contenuto;
  }

  public void setContenuto(byte[] contenuto) {
    this.contenuto = contenuto;
  }

  public NuovoAllegatoPendenza contenuto(byte[] contenuto) {
    this.contenuto = contenuto;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoAllegatoPendenza {\n");
    
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
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
