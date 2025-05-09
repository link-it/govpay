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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"denominazione",
"principalPagoPa",
"servizioPagoPa",
"servizioFtp",
"servizioPagoPaRecuperoRT",
"abilitato",
})
public class IntermediarioPost extends it.govpay.core.beans.JSONSerializable implements IValidable{

  @JsonProperty("denominazione")
  private String denominazione = null;

  @JsonProperty("principalPagoPa")
  private String principalPagoPa = null;

  @JsonProperty("servizioPagoPa")
  private ConnettorePagopa servizioPagoPa = null;

  @JsonProperty("servizioFtp")
  private ServizioFtp servizioFtp = null;

  @JsonProperty("servizioPagoPaRecuperoRT")
  private ConnettorePagopaRecuperoRT servizioPagoPaRecuperoRT = null;

  @JsonProperty("abilitato")
  private Boolean abilitato = null;

  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public IntermediarioPost denominazione(String denominazione) {
    this.denominazione = denominazione;
    return this;
  }

  @JsonProperty("denominazione")
  public String getDenominazione() {
    return this.denominazione;
  }
  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  /**
   * Principal autenticato le richieste ricevute da PagoPA
   **/
  public IntermediarioPost principalPagoPa(String principalPagoPa) {
    this.principalPagoPa = principalPagoPa;
    return this;
  }

  @JsonProperty("principalPagoPa")
  public String getPrincipalPagoPa() {
    return this.principalPagoPa;
  }
  public void setPrincipalPagoPa(String principalPagoPa) {
    this.principalPagoPa = principalPagoPa;
  }

  /**
   **/
  public IntermediarioPost servizioPagoPa(ConnettorePagopa servizioPagoPa) {
    this.servizioPagoPa = servizioPagoPa;
    return this;
  }

  @JsonProperty("servizioPagoPa")
  public ConnettorePagopa getServizioPagoPa() {
    return this.servizioPagoPa;
  }
  public void setServizioPagoPa(ConnettorePagopa servizioPagoPa) {
    this.servizioPagoPa = servizioPagoPa;
  }

  /**
   **/
  public IntermediarioPost servizioFtp(ServizioFtp servizioFtp) {
    this.servizioFtp = servizioFtp;
    return this;
  }

  @JsonProperty("servizioFtp")
  public ServizioFtp getServizioFtp() {
    return this.servizioFtp;
  }
  public void setServizioFtp(ServizioFtp servizioFtp) {
    this.servizioFtp = servizioFtp;
  }

  /**
   **/
  public IntermediarioPost servizioPagoPaRecuperoRT(ConnettorePagopaRecuperoRT servizioPagoPaRecuperoRT) {
    this.servizioPagoPaRecuperoRT = servizioPagoPaRecuperoRT;
    return this;
  }

  @JsonProperty("servizioPagoPaRecuperoRT")
  public ConnettorePagopaRecuperoRT getServizioPagoPaRecuperoRT() {
    return servizioPagoPaRecuperoRT;
  }
  public void setServizioPagoPaRecuperoRT(ConnettorePagopaRecuperoRT servizioPagoPaRecuperoRT) {
    this.servizioPagoPaRecuperoRT = servizioPagoPaRecuperoRT;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public IntermediarioPost abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    IntermediarioPost intermediarioPost = (IntermediarioPost) o;
    return Objects.equals(this.denominazione, intermediarioPost.denominazione) &&
        Objects.equals(this.principalPagoPa, intermediarioPost.principalPagoPa) &&
        Objects.equals(this.servizioPagoPa, intermediarioPost.servizioPagoPa) &&
        Objects.equals(this.servizioFtp, intermediarioPost.servizioFtp) &&
        Objects.equals(servizioPagoPaRecuperoRT, intermediarioPost.servizioPagoPaRecuperoRT) &&
        Objects.equals(this.abilitato, intermediarioPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(denominazione, principalPagoPa, servizioPagoPa, servizioFtp, servizioPagoPaRecuperoRT, abilitato);
  }

  public static IntermediarioPost parse(String json) throws IOException {
    return parse(json, IntermediarioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "intermediarioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntermediarioPost {\n");

    sb.append("    denominazione: ").append(this.toIndentedString(this.denominazione)).append("\n");
    sb.append("    principalPagoPa: ").append(this.toIndentedString(this.principalPagoPa)).append("\n");
    sb.append("    servizioPagoPa: ").append(this.toIndentedString(this.servizioPagoPa)).append("\n");
    sb.append("    servizioFtp: ").append(this.toIndentedString(this.servizioFtp)).append("\n");
    sb.append("    servizioPagoPaRecuperoRT: ").append(toIndentedString(servizioPagoPaRecuperoRT)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
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
		vf.getValidator("denominazione", this.denominazione).notNull().minLength(1).maxLength(255);
		vf.getValidator("principalPagoPa", this.principalPagoPa).notNull().minLength(1).maxLength(4000);
		vf.getValidator("servizioPagoPa", this.servizioPagoPa).notNull().validateFields();
		vf.getValidator("servizioFtp", this.servizioFtp).validateFields();
		vf.getValidator("servizioPagoPaRecuperoRT", this.servizioPagoPaRecuperoRT).validateFields();
		vf.getValidator("abilitato", this.abilitato).notNull();
	}
}



