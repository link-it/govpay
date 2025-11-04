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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idFlusso",
"dataFlusso",
"trn",
"dataRegolamento",
"dataOraPubblicazione",
"dataOraAggiornamento",
"revisione",
"idPsp",
"ragioneSocialePsp",
"bicRiversamento",
"idDominio",
"ragioneSocialeDominio",
"numeroPagamenti",
"importoTotale",
"stato",
"segnalazioni",
})
public class FlussoRendicontazioneIndex extends JSONSerializable {

  @JsonProperty("idFlusso")
  private String idFlusso = null;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
  @JsonProperty("dataFlusso")
  private Date dataFlusso = null;

  @JsonProperty("trn")
  private String trn = null;

  @JsonProperty("dataRegolamento")
  private Date dataRegolamento = null;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
  @JsonProperty("dataOraPubblicazione")
  private Date dataOraPubblicazione = null;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
  @JsonProperty("dataOraAggiornamento")
  private Date dataOraAggiornamento = null;

  @JsonProperty("revisione")
  private Long revisione = null;

  @JsonProperty("idPsp")
  private String idPsp = null;

  @JsonProperty("ragioneSocialePsp")
  private String ragioneSocialePsp = null;

  @JsonProperty("bicRiversamento")
  private String bicRiversamento = null;

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("ragioneSocialeDominio")
  private String ragioneSocialeDominio = null;

  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;

  @JsonProperty("importoTotale")
  private Double importoTotale = null;

  @JsonProperty("stato")
  private StatoFlussoRendicontazione stato = null;

  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;

  /**
   * identificativo del flusso di rendicontazione
   **/
  public FlussoRendicontazioneIndex idFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
    return this;
  }

  @JsonProperty("idFlusso")
  public String getIdFlusso() {
    return this.idFlusso;
  }
  public void setIdFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
  }

  /**
   * Data di emissione del flusso
   **/
  public FlussoRendicontazioneIndex dataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
    return this;
  }

  @JsonProperty("dataFlusso")
  public Date getDataFlusso() {
    return this.dataFlusso;
  }
  public void setDataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
  }

  /**
   * Identificativo dell'operazione di riversamento assegnato dal psp debitore
   **/
  public FlussoRendicontazioneIndex trn(String trn) {
    this.trn = trn;
    return this;
  }

  @JsonProperty("trn")
  public String getTrn() {
    return this.trn;
  }
  public void setTrn(String trn) {
    this.trn = trn;
  }

  /**
   * Data dell'operazione di riversamento fondi
   **/
  public FlussoRendicontazioneIndex dataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
    return this;
  }

  @JsonProperty("dataRegolamento")
  public Date getDataRegolamento() {
    return this.dataRegolamento;
  }
  public void setDataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  /**
   * Data di pubblicazione del flusso di rendicontazione
   **/
  public FlussoRendicontazioneIndex dataOraPubblicazione(Date dataOraPubblicazione) {
    this.dataOraPubblicazione = dataOraPubblicazione;
    return this;
  }

  @JsonProperty("dataOraPubblicazione")
  public Date getDataOraPubblicazione() {
    return this.dataOraPubblicazione;
  }
  public void setDataOraPubblicazione(Date dataOraPubblicazione) {
    this.dataOraPubblicazione = dataOraPubblicazione;
  }

  /**
   * Data di aggiornamento del flusso di rendicontazione
   **/
  public FlussoRendicontazioneIndex dataOraAggiornamento(Date dataOraAggiornamento) {
    this.dataOraAggiornamento = dataOraAggiornamento;
    return this;
  }

  @JsonProperty("dataOraAggiornamento")
  public Date getDataOraAggiornamento() {
    return this.dataOraAggiornamento;
  }
  public void setDataOraAggiornamento(Date dataOraAggiornamento) {
    this.dataOraAggiornamento = dataOraAggiornamento;
  }

  /**
   * Numero di revisione del flusso di rendicontazione
   **/
  public FlussoRendicontazioneIndex revisione(Long revisione) {
    this.revisione = revisione;
    return this;
  }

  @JsonProperty("revisione")
  public Long getRevisione() {
    return this.revisione;
  }
  public void setRevisione(Long revisione) {
    this.revisione = revisione;
  }

  /**
   * Identificativo del psp che ha emesso la rendicontazione
   **/
  public FlussoRendicontazioneIndex idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return this.idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  /**
   * Ragione sociale del psp che ha emesso la rendicontazione
   **/
  public FlussoRendicontazioneIndex ragioneSocialePsp(String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
    return this;
  }

  @JsonProperty("ragioneSocialePsp")
  public String getRagioneSocialePsp() {
    return this.ragioneSocialePsp;
  }
  public void setRagioneSocialePsp(String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
  }

  /**
   * Codice Bic della banca che ha generato il riversamento
   **/
  public FlussoRendicontazioneIndex bicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
    return this;
  }

  @JsonProperty("bicRiversamento")
  public String getBicRiversamento() {
    return this.bicRiversamento;
  }
  public void setBicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
  }

  /**
   * Identificativo del dominio creditore del riversamento
   **/
  public FlussoRendicontazioneIndex idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Ragione sociale del domino creditore del riversamento
   **/
  public FlussoRendicontazioneIndex ragioneSocialeDominio(String ragioneSocialeDominio) {
    this.ragioneSocialeDominio = ragioneSocialeDominio;
    return this;
  }

  @JsonProperty("ragioneSocialeDominio")
  public String getRagioneSocialeDominio() {
    return this.ragioneSocialeDominio;
  }
  public void setRagioneSocialeDominio(String ragioneSocialeDominio) {
    this.ragioneSocialeDominio = ragioneSocialeDominio;
  }

  /**
   * numero di pagamenti oggetto della rendicontazione
   **/
  public FlussoRendicontazioneIndex numeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
    return this;
  }

  @JsonProperty("numeroPagamenti")
  public BigDecimal getNumeroPagamenti() {
    return this.numeroPagamenti;
  }
  public void setNumeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  /**
   * somma degli importi rendicontati
   **/
  public FlussoRendicontazioneIndex importoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
    return this;
  }

  @JsonProperty("importoTotale")
  public Double getImportoTotale() {
    return this.importoTotale;
  }
  public void setImportoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
  }

  /**
   **/
  public FlussoRendicontazioneIndex stato(StatoFlussoRendicontazione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoFlussoRendicontazione getStato() {
    return stato;
  }
  public void setStato(StatoFlussoRendicontazione stato) {
    this.stato = stato;
  }

  /**
   **/
  public FlussoRendicontazioneIndex segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return this.segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FlussoRendicontazioneIndex flussoRendicontazioneIndex = (FlussoRendicontazioneIndex) o;
    return Objects.equals(this.idFlusso, flussoRendicontazioneIndex.idFlusso) &&
        Objects.equals(this.dataFlusso, flussoRendicontazioneIndex.dataFlusso) &&
        Objects.equals(this.trn, flussoRendicontazioneIndex.trn) &&
        Objects.equals(this.dataRegolamento, flussoRendicontazioneIndex.dataRegolamento) &&
        Objects.equals(this.dataOraPubblicazione, flussoRendicontazioneIndex.dataOraPubblicazione) &&
        Objects.equals(this.dataOraAggiornamento, flussoRendicontazioneIndex.dataOraAggiornamento) &&
        Objects.equals(this.revisione, flussoRendicontazioneIndex.revisione) &&
        Objects.equals(this.idPsp, flussoRendicontazioneIndex.idPsp) &&
        Objects.equals(this.ragioneSocialePsp, flussoRendicontazioneIndex.ragioneSocialePsp) &&
        Objects.equals(this.bicRiversamento, flussoRendicontazioneIndex.bicRiversamento) &&
        Objects.equals(this.idDominio, flussoRendicontazioneIndex.idDominio) &&
        Objects.equals(this.ragioneSocialeDominio, flussoRendicontazioneIndex.ragioneSocialeDominio) &&
        Objects.equals(this.numeroPagamenti, flussoRendicontazioneIndex.numeroPagamenti) &&
        Objects.equals(this.importoTotale, flussoRendicontazioneIndex.importoTotale) &&
        Objects.equals(stato, flussoRendicontazioneIndex.stato) &&
        Objects.equals(this.segnalazioni, flussoRendicontazioneIndex.segnalazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idFlusso, this.dataFlusso, this.trn, this.dataRegolamento, this.dataOraPubblicazione, this.dataOraAggiornamento, this.revisione, this.idPsp, this.ragioneSocialePsp, this.bicRiversamento, this.idDominio, this.ragioneSocialeDominio, this.numeroPagamenti, this.importoTotale, stato, this.segnalazioni);
  }

  public static FlussoRendicontazioneIndex parse(String json) throws IOException {
    return parse(json, FlussoRendicontazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "flussoRendicontazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlussoRendicontazioneIndex {\n");

    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
    sb.append("    dataFlusso: ").append(this.toIndentedString(this.dataFlusso)).append("\n");
    sb.append("    trn: ").append(this.toIndentedString(this.trn)).append("\n");
    sb.append("    dataRegolamento: ").append(this.toIndentedString(this.dataRegolamento)).append("\n");
    sb.append("    dataOraPubblicazione: ").append(this.toIndentedString(this.dataOraPubblicazione)).append("\n");
    sb.append("    dataOraAggiornamento: ").append(this.toIndentedString(this.dataOraAggiornamento)).append("\n");
    sb.append("    revisione: ").append(this.toIndentedString(this.revisione)).append("\n");
    sb.append("    idPsp: ").append(this.toIndentedString(this.idPsp)).append("\n");
    sb.append("    ragioneSocialePsp: ").append(this.toIndentedString(this.ragioneSocialePsp)).append("\n");
    sb.append("    bicRiversamento: ").append(this.toIndentedString(this.bicRiversamento)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    ragioneSocialeDominio: ").append(this.toIndentedString(this.ragioneSocialeDominio)).append("\n");
    sb.append("    numeroPagamenti: ").append(this.toIndentedString(this.numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(this.toIndentedString(this.importoTotale)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(this.toIndentedString(this.segnalazioni)).append("\n");
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



