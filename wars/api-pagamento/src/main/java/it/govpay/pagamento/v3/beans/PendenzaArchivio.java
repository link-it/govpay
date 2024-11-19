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
package it.govpay.pagamento.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Rappresentazione di una posizione debitoria
 **/
@Schema(description="Rappresentazione di una posizione debitoria")
public class PendenzaArchivio extends PendenzaPagata  {

  @Schema(required = true, description = "")
  private StatoPendenza stato = null;

  @Schema(description = "Descrizione in dettaglio dello stato della pendenza.")
 /**
   * Descrizione in dettaglio dello stato della pendenza.
  **/
  private String descrizioneStato = null;
 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoPendenza getStato() {
    return stato;
  }

  public void setStato(StatoPendenza stato) {
    this.stato = stato;
  }

  public PendenzaArchivio stato(StatoPendenza stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Descrizione in dettaglio dello stato della pendenza.
   * @return descrizioneStato
  **/
  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return descrizioneStato;
  }

  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public PendenzaArchivio descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaArchivio {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
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
