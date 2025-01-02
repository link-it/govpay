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
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"tipo",
"descrizione",
"contenuto",
})
public class NuovoAllegatoPendenza extends JSONSerializable  implements IValidable{

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("tipo")
  private String tipo = "application/octet-stream";

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("contenuto")
  private byte[] contenuto = null;

  /**
   * nome del file
   **/
  public NuovoAllegatoPendenza nome(String nome) {
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
  public NuovoAllegatoPendenza tipo(String tipo) {
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
  public NuovoAllegatoPendenza descrizione(String descrizione) {
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
   * contenuto del file
   **/
  public NuovoAllegatoPendenza contenuto(byte[] contenuto) {
    this.contenuto = contenuto;
    return this;
  }

  @JsonProperty("contenuto")
  public byte[] getContenuto() {
    return contenuto;
  }
  public void setContenuto(byte[] contenuto) {
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
    NuovoAllegatoPendenza nuovoAllegatoPendenza = (NuovoAllegatoPendenza) o;
    return Objects.equals(nome, nuovoAllegatoPendenza.nome) &&
        Objects.equals(tipo, nuovoAllegatoPendenza.tipo) &&
        Objects.equals(descrizione, nuovoAllegatoPendenza.descrizione) &&
        java.util.Arrays.equals(contenuto, nuovoAllegatoPendenza.contenuto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, tipo, descrizione, contenuto);
  }

  public static NuovoAllegatoPendenza parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, NuovoAllegatoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovoAllegatoPendenza";
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("nome", this.nome).notNull().minLength(1).maxLength(255);
		vf.getValidator("tipo", this.tipo).minLength(1).maxLength(255);
		vf.getValidator("descrizione", this.descrizione).minLength(1).maxLength(255);

		if(this.contenuto == null)
			throw new ValidationException("Il campo " + "contenuto" + " non deve essere vuoto.");
	}
}



