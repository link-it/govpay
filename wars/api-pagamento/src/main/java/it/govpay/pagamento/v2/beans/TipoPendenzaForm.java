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
package it.govpay.pagamento.v2.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import it.govpay.core.exceptions.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"definizione",
"impaginazione",
})
public class TipoPendenzaForm extends JSONSerializable {
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("definizione")
  private Object definizione = null;
  
  @JsonProperty("impaginazione")
  private Object impaginazione = null;
  
  /**
   * Indica il linguaggio da utilizzare per il disegno della form di inserimento della pendenza
   **/
  public TipoPendenzaForm tipo(String tipo) {
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
   * Definizione della form nel linguaggio indicato nel field tipo
   **/
  public TipoPendenzaForm definizione(Object definizione) {
    this.definizione = definizione;
    return this;
  }

  @JsonProperty("definizione")
  public Object getDefinizione() {
    return definizione;
  }
  public void setDefinizione(Object definizione) {
    this.definizione = definizione;
  }

  /**
   * Definizione dell'impaginazione della pagina di pagamento del portale
   **/
  public TipoPendenzaForm impaginazione(Object impaginazione) {
    this.impaginazione = impaginazione;
    return this;
  }

  @JsonProperty("impaginazione")
  public Object getImpaginazione() {
    return impaginazione;
  }
  public void setImpaginazione(Object impaginazione) {
    this.impaginazione = impaginazione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaForm tipoPendenzaForm = (TipoPendenzaForm) o;
    return Objects.equals(tipo, tipoPendenzaForm.tipo) &&
        Objects.equals(definizione, tipoPendenzaForm.definizione) &&
        Objects.equals(impaginazione, tipoPendenzaForm.impaginazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, definizione, impaginazione);
  }

  public static TipoPendenzaForm parse(String json) throws it.govpay.core.exceptions.IOException { 
    return parse(json, TipoPendenzaForm.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaForm";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaForm {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    definizione: ").append(toIndentedString(definizione)).append("\n");
    sb.append("    impaginazione: ").append(toIndentedString(impaginazione)).append("\n");
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



