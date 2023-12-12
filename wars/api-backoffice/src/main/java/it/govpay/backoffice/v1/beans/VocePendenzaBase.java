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
package it.govpay.backoffice.v1.beans;



import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"stato",
"descrizioneCausaleRPT",
"contabilita",
})
public class VocePendenzaBase extends JSONSerializable {

  @JsonProperty("indice")
  private BigDecimal indice = null;

  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("stato")
  private StatoVocePendenza stato = null;

  @JsonProperty("descrizioneCausaleRPT")
  private String descrizioneCausaleRPT = null;

  @JsonProperty("contabilita")
  private Contabilita contabilita = null;

  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenzaBase indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   **/
  public VocePendenzaBase idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * Importo della voce
   **/
  public VocePendenzaBase importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * descrizione della voce di pagamento
   **/
  public VocePendenzaBase descrizione(String descrizione) {
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
   **/
  public VocePendenzaBase stato(StatoVocePendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoVocePendenza getStato() {
    return stato;
  }
  public void setStato(StatoVocePendenza stato) {
    this.stato = stato;
  }

  /**
   * Testo libero per la causale versamento
   **/
  public VocePendenzaBase descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }

  @JsonProperty("descrizioneCausaleRPT")
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }
  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  /**
   **/
  public VocePendenzaBase contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }

  @JsonProperty("contabilita")
  public Contabilita getContabilita() {
    return contabilita;
  }
  public void setContabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VocePendenzaBase vocePendenzaBase = (VocePendenzaBase) o;
    return Objects.equals(indice, vocePendenzaBase.indice) &&
        Objects.equals(idVocePendenza, vocePendenzaBase.idVocePendenza) &&
        Objects.equals(importo, vocePendenzaBase.importo) &&
        Objects.equals(descrizione, vocePendenzaBase.descrizione) &&
        Objects.equals(stato, vocePendenzaBase.stato) &&
        Objects.equals(descrizioneCausaleRPT, vocePendenzaBase.descrizioneCausaleRPT) &&
        Objects.equals(contabilita, vocePendenzaBase.contabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indice, idVocePendenza, importo, descrizione, stato, descrizioneCausaleRPT, contabilita);
  }

  public static VocePendenzaBase parse(String json) throws IOException {
    return parse(json, VocePendenzaBase.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vocePendenzaBase";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenzaBase {\n");

    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
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



