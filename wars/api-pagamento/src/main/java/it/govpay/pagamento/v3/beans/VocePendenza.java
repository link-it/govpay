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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VocePendenza extends TipoRiferimentoVocePendenza  {

  @Schema(description = "")
  private Dominio dominio = null;

  @Schema(example = "abcdef12345_1", required = true, description = "Identificativo della voce di pedenza nel gestionale proprietario")
 /**
   * Identificativo della voce di pedenza nel gestionale proprietario
  **/
  private String idVocePendenza = null;

  @Schema(example = "Sanzione CdS n. abc00000", description = "descrizione della voce di pagamento")
 /**
   * descrizione della voce di pagamento
  **/
  private String descrizione = null;

  @Schema(description = "Dati applicativi allegati dal gestionale secondo un formato proprietario.")
 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
  **/
  private Object datiAllegati = null;

  @Schema(example = "Sanzione CdS n. abc00000", description = "Testo libero per la causale versamento")
 /**
   * Testo libero per la causale versamento
  **/
  private String descrizioneCausaleRPT = null;

  @Schema(description = "")
  private Contabilita contabilita = null;

  @Schema(description = "")
  private Metadata metadata = null;

  @Schema(required = true, description = "")
  private Pendenza pendenza = null;
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

  public VocePendenza dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   * @return idVocePendenza
  **/
  @JsonProperty("idVocePendenza")
  @NotNull
  public String getIdVocePendenza() {
    return idVocePendenza;
  }

  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  public VocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

 /**
   * descrizione della voce di pagamento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
 @Size(min=1,max=140)  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public VocePendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   * @return datiAllegati
  **/
  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }

  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public VocePendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Testo libero per la causale versamento
   * @return descrizioneCausaleRPT
  **/
  @JsonProperty("descrizioneCausaleRPT")
 @Size(min=1,max=140)  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }

  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  public VocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }

 /**
   * Get contabilita
   * @return contabilita
  **/
  @JsonProperty("contabilita")
  public Contabilita getContabilita() {
    return contabilita;
  }

  public void setContabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
  }

  public VocePendenza contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }

 /**
   * Get metadata
   * @return metadata
  **/
  @JsonProperty("metadata")
  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public VocePendenza metadata(Metadata metadata) {
    this.metadata = metadata;
    return this;
  }

 /**
   * Get pendenza
   * @return pendenza
  **/
  @JsonProperty("pendenza")
  @NotNull
  public Pendenza getPendenza() {
    return pendenza;
  }

  public void setPendenza(Pendenza pendenza) {
    this.pendenza = pendenza;
  }

  public VocePendenza pendenza(Pendenza pendenza) {
    this.pendenza = pendenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
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
