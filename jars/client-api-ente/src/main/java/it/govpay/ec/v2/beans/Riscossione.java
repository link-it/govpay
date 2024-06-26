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
package it.govpay.ec.v2.beans;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class Riscossione   {
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "")
  private Dominio dominio = null;
  
  @Schema(example = "RF23567483937849450550875", requiredMode = RequiredMode.REQUIRED, description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento  
  **/
  private String iuv = null;
  
  @Schema(example = "ab12345", requiredMode = RequiredMode.REQUIRED, description = "Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento")
 /**
   * Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento  
  **/
  private String idRicevuta = null;
  
  @Schema(example = "1234acdc", requiredMode = RequiredMode.REQUIRED, description = "Identificativo univoco di riscossione.")
 /**
   * Identificativo univoco di riscossione.  
  **/
  private String iur = null;
  
  @Schema(example = "1", requiredMode = RequiredMode.REQUIRED, description = "indice posizionale della voce pendenza riscossa")
 /**
   * indice posizionale della voce pendenza riscossa  
  **/
  private BigDecimal indice = null;
  
  @Schema(description = "")
  private StatoRiscossione stato = null;
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "")
  private TipoRiscossione tipo = null;
  
  @Schema(example = "10.01", requiredMode = RequiredMode.REQUIRED, description = "Importo riscosso.")
 /**
   * Importo riscosso.  
  **/
  private BigDecimal importo = null;
  
  @Schema(requiredMode = RequiredMode.REQUIRED, description = "Data di esecuzione della riscossione")
 /**
   * Data di esecuzione della riscossione  
  **/
  private Date data = null;
  
  @Schema(description = "")
  private Allegato allegato = null;
  
  @Schema(description = "")
  private VocePendenza vocePendenza = null;
 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  @NotNull
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public Riscossione dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Identificativo univoco di versamento
   * @return iuv
  **/
  @JsonProperty("iuv")
  @NotNull
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public Riscossione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Corrisponde al &#x60;receiptId&#x60; oppure al &#x60;ccp&#x60; a seconda del modello di pagamento
   * @return idRicevuta
  **/
  @JsonProperty("idRicevuta")
  @NotNull
  public String getIdRicevuta() {
    return idRicevuta;
  }

  public void setIdRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
  }

  public Riscossione idRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
    return this;
  }

 /**
   * Identificativo univoco di riscossione.
   * @return iur
  **/
  @JsonProperty("iur")
  @NotNull
  public String getIur() {
    return iur;
  }

  public void setIur(String iur) {
    this.iur = iur;
  }

  public Riscossione iur(String iur) {
    this.iur = iur;
    return this;
  }

 /**
   * indice posizionale della voce pendenza riscossa
   * @return indice
  **/
  @JsonProperty("indice")
  @NotNull
  public BigDecimal getIndice() {
    return indice;
  }

  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  public Riscossione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  public StatoRiscossione getStato() {
    return stato;
  }

  public void setStato(StatoRiscossione stato) {
    this.stato = stato;
  }

  public Riscossione stato(StatoRiscossione stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public TipoRiscossione getTipo() {
    return tipo;
  }

  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
  }

  public Riscossione tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * Importo riscosso.
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public Riscossione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Data di esecuzione della riscossione
   * @return data
  **/
  @JsonProperty("data")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  @NotNull
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Riscossione data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Get allegato
   * @return allegato
  **/
  @JsonProperty("allegato")
  public Allegato getAllegato() {
    return allegato;
  }

  public void setAllegato(Allegato allegato) {
    this.allegato = allegato;
  }

  public Riscossione allegato(Allegato allegato) {
    this.allegato = allegato;
    return this;
  }

 /**
   * Get vocePendenza
   * @return vocePendenza
  **/
  @JsonProperty("vocePendenza")
  public VocePendenza getVocePendenza() {
    return vocePendenza;
  }

  public void setVocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
  }

  public Riscossione vocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riscossione {\n");
    
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    idRicevuta: ").append(toIndentedString(idRicevuta)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
    sb.append("    vocePendenza: ").append(toIndentedString(vocePendenza)).append("\n");
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
