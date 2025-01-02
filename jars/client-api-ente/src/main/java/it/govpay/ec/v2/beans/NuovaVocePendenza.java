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
package it.govpay.ec.v2.beans;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class NuovaVocePendenza extends TipoRiferimentoVocePendenza  {
  
  @Schema(description = "")
  private String idDominio = null;
  
  @Schema(example = "abcdef12345_1", requiredMode = RequiredMode.REQUIRED, description = "Identificativo della voce di pedenza nel gestionale proprietario")
 /**
   * Identificativo della voce di pedenza nel gestionale proprietario  
  **/
  private String idVocePendenza = null;
  
  @Schema(example = "10.01", requiredMode = RequiredMode.REQUIRED, description = "Importo della voce")
 /**
   * Importo della voce  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", requiredMode = RequiredMode.REQUIRED, description = "descrizione della voce di pagamento")
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
 /**
   * Get idDominio
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public NuovaVocePendenza idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   * @return idVocePendenza
  **/
  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }

  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  public NuovaVocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

 /**
   * Importo della voce
   * minimum: 0
   * maximum: 10000000000000000
   * @return importo
  **/
  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public NuovaVocePendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * descrizione della voce di pagamento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public NuovaVocePendenza descrizione(String descrizione) {
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

  public NuovaVocePendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Testo libero per la causale versamento
   * @return descrizioneCausaleRPT
  **/
  @JsonProperty("descrizioneCausaleRPT")
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }

  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  public NuovaVocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
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

  public NuovaVocePendenza contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaVocePendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
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
