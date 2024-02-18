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


import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"numeroPagamenti",
"importo",
"dettaglio",
"direzione",
"divisione",
"tassonomia",
"tipoPendenza",
"dominio",
"unitaOperativa",
"applicazione",
})
public class StatisticaQuadratura extends JSONSerializable {

  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("dettaglio")
  private String dettaglio = null;

  @JsonProperty("direzione")
  private String direzione = null;

  @JsonProperty("divisione")
  private String divisione = null;

  @JsonProperty("tassonomia")
  private String tassonomia = null;

  @JsonProperty("tipoPendenza")
  private TipoPendenzaIndex tipoPendenza = null;

  @JsonProperty("dominio")
  private DominioIndex dominio = null;

  @JsonProperty("unitaOperativa")
  private UnitaOperativaIndex unitaOperativa = null;

  @JsonProperty("applicazione")
  private ApplicazioneIndex applicazione = null;

  /**
   * Numero di pagamenti corrispondenti ai parametri impostati
   **/
  public StatisticaQuadratura numeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
    return this;
  }

  @JsonProperty("numeroPagamenti")
  public BigDecimal getNumeroPagamenti() {
    return numeroPagamenti;
  }
  public void setNumeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  /**
   * Importo Totale corrispondente ai parametri impostati
   **/
  public StatisticaQuadratura importo(BigDecimal importo) {
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
   * Url al dettaglio della statistica
   **/
  public StatisticaQuadratura dettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
    return this;
  }

  @JsonProperty("dettaglio")
  public String getDettaglio() {
    return dettaglio;
  }
  public void setDettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
  }

  /**
   * Identificativo della direzione interna all'ente creditore
   **/
  public StatisticaQuadratura direzione(String direzione) {
    this.direzione = direzione;
    return this;
  }

  @JsonProperty("direzione")
  public String getDirezione() {
    return direzione;
  }
  public void setDirezione(String direzione) {
    this.direzione = direzione;
  }

  /**
   * Identificativo della divisione interna all'ente creditore
   **/
  public StatisticaQuadratura divisione(String divisione) {
    this.divisione = divisione;
    return this;
  }

  @JsonProperty("divisione")
  public String getDivisione() {
    return divisione;
  }
  public void setDivisione(String divisione) {
    this.divisione = divisione;
  }

  /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   **/
  public StatisticaQuadratura tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   **/
  public StatisticaQuadratura tipoPendenza(TipoPendenzaIndex tipoPendenza) {
    this.tipoPendenza = tipoPendenza;
    return this;
  }

  @JsonProperty("tipoPendenza")
  public TipoPendenzaIndex getTipoPendenza() {
    return tipoPendenza;
  }
  public void setTipoPendenza(TipoPendenzaIndex tipoPendenza) {
    this.tipoPendenza = tipoPendenza;
  }

  /**
   **/
  public StatisticaQuadratura dominio(DominioIndex dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public DominioIndex getDominio() {
    return dominio;
  }
  public void setDominio(DominioIndex dominio) {
    this.dominio = dominio;
  }

  /**
   **/
  public StatisticaQuadratura unitaOperativa(UnitaOperativaIndex unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
    return this;
  }

  @JsonProperty("unitaOperativa")
  public UnitaOperativaIndex getUnitaOperativa() {
    return unitaOperativa;
  }
  public void setUnitaOperativa(UnitaOperativaIndex unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
  }

  /**
   **/
  public StatisticaQuadratura applicazione(ApplicazioneIndex applicazione) {
    this.applicazione = applicazione;
    return this;
  }

  @JsonProperty("applicazione")
  public ApplicazioneIndex getApplicazione() {
    return applicazione;
  }
  public void setApplicazione(ApplicazioneIndex applicazione) {
    this.applicazione = applicazione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticaQuadratura statisticaQuadratura = (StatisticaQuadratura) o;
    return Objects.equals(numeroPagamenti, statisticaQuadratura.numeroPagamenti) &&
        Objects.equals(importo, statisticaQuadratura.importo) &&
        Objects.equals(dettaglio, statisticaQuadratura.dettaglio) &&
        Objects.equals(direzione, statisticaQuadratura.direzione) &&
        Objects.equals(divisione, statisticaQuadratura.divisione) &&
        Objects.equals(tassonomia, statisticaQuadratura.tassonomia) &&
        Objects.equals(tipoPendenza, statisticaQuadratura.tipoPendenza) &&
        Objects.equals(dominio, statisticaQuadratura.dominio) &&
        Objects.equals(unitaOperativa, statisticaQuadratura.unitaOperativa) &&
        Objects.equals(applicazione, statisticaQuadratura.applicazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numeroPagamenti, importo, dettaglio, direzione, divisione, tassonomia, tipoPendenza, dominio, unitaOperativa, applicazione);
  }

  public static StatisticaQuadratura parse(String json) throws IOException {
    return parse(json, StatisticaQuadratura.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "statisticaQuadratura";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticaQuadratura {\n");

    sb.append("    numeroPagamenti: ").append(toIndentedString(numeroPagamenti)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    tipoPendenza: ").append(toIndentedString(tipoPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    applicazione: ").append(toIndentedString(applicazione)).append("\n");
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



