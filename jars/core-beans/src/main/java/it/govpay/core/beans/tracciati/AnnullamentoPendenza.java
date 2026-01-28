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
package it.govpay.core.beans.tracciati;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"motivoAnnullamento",
})
public class AnnullamentoPendenza extends JSONSerializable implements IValidable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("motivoAnnullamento")
  private String motivoAnnullamento = null;
  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public AnnullamentoPendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public AnnullamentoPendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return this.idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * Descrizione dell'operazione
   **/
  public AnnullamentoPendenza motivoAnnullamento(String motivoAnnullamento) {
    this.motivoAnnullamento = motivoAnnullamento;
    return this;
  }

  @JsonProperty("motivoAnnullamento")
  public String getMotivoAnnullamento() {
    return this.motivoAnnullamento;
  }
  public void setMotivoAnnullamento(String motivoAnnullamento) {
    this.motivoAnnullamento = motivoAnnullamento;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    AnnullamentoPendenza annullamentoPendenza = (AnnullamentoPendenza) o;
    return Objects.equals(this.idA2A, annullamentoPendenza.idA2A) &&
        Objects.equals(this.idPendenza, annullamentoPendenza.idPendenza) &&
        Objects.equals(this.motivoAnnullamento, annullamentoPendenza.motivoAnnullamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idA2A, this.idPendenza, this.motivoAnnullamento);
  }

  public static AnnullamentoPendenza parse(String json) throws IOException {
    return parse(json, AnnullamentoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "annullamentoPendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnullamentoPendenza {\n");
    
    sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
    sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
    sb.append("    motivoAnnullamento: ").append(this.toIndentedString(this.motivoAnnullamento)).append("\n");
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
	public void validate() throws it.govpay.core.exceptions.ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		
		vf.getValidator("idA2A", this.idA2A).notNull().minLength(1).maxLength(35);
		vf.getValidator("idPendenza", this.idPendenza).notNull().minLength(1).maxLength(35);
		vf.getValidator("motivoAnnullamento", this.motivoAnnullamento).notNull();
	}
}



