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
package it.govpay.ragioneria.v3.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class RicevuteRisultati   {

  @Schema(description = "")
  private Dominio dominio = null;

  @Schema(example = "RF23567483937849450550875", description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento
  **/
  private String iuv = null;

  @Schema(example = "ab12345", description = "Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento")
 /**
   * Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento
  **/
  private String idRicevuta = null;

  @Schema(description = "Data di acquisizione della ricevuta")
 /**
   * Data di acquisizione della ricevuta
  **/
  private Date data = null;

  @Schema(description = "")
  private EsitoRpp esito = null;
 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public RicevuteRisultati dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Identificativo univoco di versamento
   * @return iuv
  **/
  @JsonProperty("iuv")
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public RicevuteRisultati iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Corrisponde al &#x60;receiptId&#x60; oppure al &#x60;ccp&#x60; a seconda del modello di pagamento
   * @return idRicevuta
  **/
  @JsonProperty("idRicevuta")
  public String getIdRicevuta() {
    return idRicevuta;
  }

  public void setIdRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
  }

  public RicevuteRisultati idRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
    return this;
  }

 /**
   * Data di acquisizione della ricevuta
   * @return data
  **/
  @JsonProperty("data")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public RicevuteRisultati data(Date data) {
    this.data = data;
    return this;
  }

  /**
   * Get esito
   * @return esito
  **/
  @JsonProperty("esito")
  public EsitoRpp getEsito() {
    return esito;
  }

  public void setEsito(EsitoRpp esito) {
    this.esito = esito;
  }

  public RicevuteRisultati esito(EsitoRpp esito) {
    this.esito = esito;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RicevuteRisultati {\n");

    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    idRicevuta: ").append(toIndentedString(idRicevuta)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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
