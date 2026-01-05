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
package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * PSP che ha gestito la transazione di pagamento ed emesso la ricevuta
 **/
@Schema(description="PSP che ha gestito la transazione di pagamento ed emesso la ricevuta")
public class RicevutaIstitutoAttestante   {
  
  @Schema(description = "Identificativo del PSP assegnato da pagoPA")
 /**
   * Identificativo del PSP assegnato da pagoPA  
  **/
  private String idPSP = null;
  
  @Schema(description = "Ragione sociale del PSP")
 /**
   * Ragione sociale del PSP  
  **/
  private String denominazione = null;
  
  @Schema(description = "Identificativo del canale di pagamento utilizzato")
 /**
   * Identificativo del canale di pagamento utilizzato  
  **/
  private String idCanale = null;
  
  @Schema(description = "Descrizione del canale di pagamento utilizzato")
 /**
   * Descrizione del canale di pagamento utilizzato  
  **/
  private String descrizione = null;
 /**
   * Identificativo del PSP assegnato da pagoPA
   * @return idPSP
  **/
  @JsonProperty("idPSP")
  public String getIdPSP() {
    return idPSP;
  }

  public void setIdPSP(String idPSP) {
    this.idPSP = idPSP;
  }

  public RicevutaIstitutoAttestante idPSP(String idPSP) {
    this.idPSP = idPSP;
    return this;
  }

 /**
   * Ragione sociale del PSP
   * @return denominazione
  **/
  @JsonProperty("denominazione")
  public String getDenominazione() {
    return denominazione;
  }

  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  public RicevutaIstitutoAttestante denominazione(String denominazione) {
    this.denominazione = denominazione;
    return this;
  }

 /**
   * Identificativo del canale di pagamento utilizzato
   * @return idCanale
  **/
  @JsonProperty("idCanale")
  public String getIdCanale() {
    return idCanale;
  }

  public void setIdCanale(String idCanale) {
    this.idCanale = idCanale;
  }

  public RicevutaIstitutoAttestante idCanale(String idCanale) {
    this.idCanale = idCanale;
    return this;
  }

 /**
   * Descrizione del canale di pagamento utilizzato
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public RicevutaIstitutoAttestante descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RicevutaIstitutoAttestante {\n");
    
    sb.append("    idPSP: ").append(toIndentedString(idPSP)).append("\n");
    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
    sb.append("    idCanale: ").append(toIndentedString(idCanale)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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
