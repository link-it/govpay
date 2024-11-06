/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"denominazione",
"principalPagoPa",
"servizioPagoPa",
"servizioFtp",
"servizioPagoPaRecuperoRT",
"abilitato",
"idIntermediario",
"stazioni",
})
public class Intermediario extends it.govpay.core.beans.JSONSerializable {

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

  @JsonProperty("idIntermediario")
  private String idIntermediario = null;

  @JsonProperty("stazioni")
  private List<StazioneIndex> stazioni = new ArrayList<>();

  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public Intermediario denominazione(String denominazione) {
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
  public Intermediario principalPagoPa(String principalPagoPa) {
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
  public Intermediario servizioPagoPa(ConnettorePagopa servizioPagoPa) {
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
  public Intermediario servizioFtp(ServizioFtp servizioFtp) {
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
  public Intermediario servizioPagoPaRecuperoRT(ConnettorePagopaRecuperoRT servizioPagoPaRecuperoRT) {
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
  public Intermediario abilitato(Boolean abilitato) {
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

  /**
   * Identificativo dell'intermediario
   **/
  public Intermediario idIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
    return this;
  }

  @JsonProperty("idIntermediario")
  public String getIdIntermediario() {
    return this.idIntermediario;
  }
  public void setIdIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
  }

  /**
   **/
  public Intermediario stazioni(List<StazioneIndex> stazioni) {
    this.stazioni = stazioni;
    return this;
  }

  @JsonProperty("stazioni")
  public List<StazioneIndex> getStazioni() {
    return this.stazioni;
  }
  public void setStazioni(List<StazioneIndex> stazioni) {
    this.stazioni = stazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Intermediario intermediario = (Intermediario) o;
    return Objects.equals(this.denominazione, intermediario.denominazione) &&
        Objects.equals(this.principalPagoPa, intermediario.principalPagoPa) &&
        Objects.equals(this.servizioPagoPa, intermediario.servizioPagoPa) &&
        Objects.equals(this.servizioFtp, intermediario.servizioFtp) &&
        Objects.equals(servizioPagoPaRecuperoRT, intermediario.servizioPagoPaRecuperoRT) &&
        Objects.equals(this.abilitato, intermediario.abilitato) &&
        Objects.equals(this.idIntermediario, intermediario.idIntermediario) &&
        Objects.equals(this.stazioni, intermediario.stazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(denominazione, principalPagoPa, servizioPagoPa, servizioFtp, servizioPagoPaRecuperoRT, abilitato, idIntermediario, stazioni);
  }

  public static Intermediario parse(String json) throws IOException {
    return parse(json, Intermediario.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "intermediario";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Intermediario {\n");
    
    sb.append("    denominazione: ").append(this.toIndentedString(this.denominazione)).append("\n");
    sb.append("    principalPagoPa: ").append(this.toIndentedString(this.principalPagoPa)).append("\n");
    sb.append("    servizioPagoPa: ").append(this.toIndentedString(this.servizioPagoPa)).append("\n");
    sb.append("    servizioFtp: ").append(this.toIndentedString(this.servizioFtp)).append("\n");
    sb.append("    servizioPagoPaRecuperoRT: ").append(toIndentedString(servizioPagoPaRecuperoRT)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    idIntermediario: ").append(this.toIndentedString(this.idIntermediario)).append("\n");
    sb.append("    stazioni: ").append(this.toIndentedString(this.stazioni)).append("\n");
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



