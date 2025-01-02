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
package it.govpay.ragioneria.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"iuv",
"iur",
"indice",
"pendenza",
"vocePendenza",
"rpp",
"stato",
"tipo",
"importo",
"data",
"commissioni",
"allegato",
"incasso",
})
public class Riscossione extends JSONSerializable {

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("iuv")
  private String iuv = null;

  @JsonProperty("iur")
  private String iur = null;

  @JsonProperty("indice")
  private BigDecimal indice = null;

  @JsonProperty("pendenza")
  private PendenzaIndex pendenza = null;

  @JsonProperty("vocePendenza")
  private VocePendenza vocePendenza = null;

  @JsonProperty("rpp")
  private RppIndex rpp = null;

  @JsonProperty("stato")
  private StatoRiscossione stato = null;

  @JsonProperty("tipo")
  private TipoRiscossione tipo = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("data")
  private Date data = null;

  @JsonProperty("commissioni")
  private BigDecimal commissioni = null;

  @JsonProperty("allegato")
  private Allegato allegato = null;

  @JsonProperty("incasso")
  private IncassoIndex incasso = null;

  /**
   **/
  public Riscossione idDominio(String idDominio) {
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
   * Identificativo univoco di versamento
   **/
  public Riscossione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return this.iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo univoco di riscossione.
   **/
  public Riscossione iur(String iur) {
    this.iur = iur;
    return this;
  }

  @JsonProperty("iur")
  public String getIur() {
    return this.iur;
  }
  public void setIur(String iur) {
    this.iur = iur;
  }

  /**
   * indice posizionale della voce pendenza riscossa
   **/
  public Riscossione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return this.indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   **/
  public Riscossione pendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public PendenzaIndex getPendenza() {
    return this.pendenza;
  }
  public void setPendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
  }

  /**
   **/
  public Riscossione vocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
    return this;
  }

  @JsonProperty("vocePendenza")
  public VocePendenza getVocePendenza() {
    return this.vocePendenza;
  }
  public void setVocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
  }

  /**
   **/
  public Riscossione rpp(RppIndex rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public RppIndex getRpp() {
    return this.rpp;
  }
  public void setRpp(RppIndex rpp) {
    this.rpp = rpp;
  }

  /**
   **/
  public Riscossione stato(StatoRiscossione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoRiscossione getStato() {
    return this.stato;
  }
  public void setStato(StatoRiscossione stato) {
    this.stato = stato;
  }

  /**
   **/
  public Riscossione tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoRiscossione getTipo() {
    return this.tipo;
  }
  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
  }

  /**
   * Importo riscosso.
   **/
  public Riscossione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Data di esecuzione della riscossione
   **/
  public Riscossione data(Date data) {
    this.data = data;
    return this;
  }

  @JsonProperty("data")
  public Date getData() {
    return this.data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   * Importo delle commissioni applicate al pagamento dal PSP
   **/
  public Riscossione commissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
    return this;
  }

  @JsonProperty("commissioni")
  public BigDecimal getCommissioni() {
    return this.commissioni;
  }
  public void setCommissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
  }

  /**
   **/
  public Riscossione allegato(Allegato allegato) {
    this.allegato = allegato;
    return this;
  }

  @JsonProperty("allegato")
  public Allegato getAllegato() {
    return this.allegato;
  }
  public void setAllegato(Allegato allegato) {
    this.allegato = allegato;
  }

  /**
   **/
  public Riscossione incasso(IncassoIndex incasso) {
    this.incasso = incasso;
    return this;
  }

  @JsonProperty("incasso")
  public IncassoIndex getIncasso() {
    return this.incasso;
  }
  public void setIncasso(IncassoIndex incasso) {
    this.incasso = incasso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Riscossione riscossione = (Riscossione) o;
    return Objects.equals(this.idDominio, riscossione.idDominio) &&
        Objects.equals(this.iuv, riscossione.iuv) &&
        Objects.equals(this.iur, riscossione.iur) &&
        Objects.equals(this.indice, riscossione.indice) &&
        Objects.equals(this.pendenza, riscossione.pendenza) &&
        Objects.equals(this.vocePendenza, riscossione.vocePendenza) &&
        Objects.equals(this.rpp, riscossione.rpp) &&
        Objects.equals(this.stato, riscossione.stato) &&
        Objects.equals(this.tipo, riscossione.tipo) &&
        Objects.equals(this.importo, riscossione.importo) &&
        Objects.equals(this.data, riscossione.data) &&
        Objects.equals(this.commissioni, riscossione.commissioni) &&
        Objects.equals(this.allegato, riscossione.allegato) &&
        Objects.equals(this.incasso, riscossione.incasso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.iuv, this.iur, this.indice, this.pendenza, this.vocePendenza, this.rpp, this.stato, this.tipo, this.importo, this.data, this.commissioni, this.allegato, this.incasso);
  }

  public static Riscossione parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Riscossione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "riscossione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riscossione {\n");

    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    iur: ").append(this.toIndentedString(this.iur)).append("\n");
    sb.append("    indice: ").append(this.toIndentedString(this.indice)).append("\n");
    sb.append("    pendenza: ").append(this.toIndentedString(this.pendenza)).append("\n");
    sb.append("    vocePendenza: ").append(this.toIndentedString(this.vocePendenza)).append("\n");
    sb.append("    rpp: ").append(this.toIndentedString(this.rpp)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    data: ").append(this.toIndentedString(this.data)).append("\n");
    sb.append("    commissioni: ").append(this.toIndentedString(this.commissioni)).append("\n");
    sb.append("    allegato: ").append(this.toIndentedString(this.allegato)).append("\n");
    sb.append("    incasso: ").append(this.toIndentedString(this.incasso)).append("\n");
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



