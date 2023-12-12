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
package it.govpay.pagamento.v1.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"causale",
"soggettoPagatore",
"importo",
"numeroAvviso",
"dataCaricamento",
"dataValidita",
"dataScadenza",
"dataPagamento",
"annoRiferimento",
"cartellaPagamento",
"datiAllegati",
"tassonomia",
"tassonomiaAvviso",
"idA2A",
"idPendenza",
"dominio",
"unitaOperativa",
"stato",
"segnalazioni",
"voci",
"rpp",
"pagamenti",
})
public class Pendenza extends JSONSerializable {

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("causale")
  private String causale = null;

  @JsonProperty("soggettoPagatore")
  private Soggetto soggettoPagatore = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;

  @JsonProperty("dataCaricamento")
  private Date dataCaricamento = null;

  @JsonProperty("dataValidita")
  private Date dataValidita = null;

  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;

  @JsonProperty("dataPagamento")
  private Date dataPagamento = null;

  @JsonProperty("annoRiferimento")
  private BigDecimal annoRiferimento = null;

  @JsonProperty("cartellaPagamento")
  private String cartellaPagamento = null;

  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;

  @JsonProperty("tassonomia")
  private String tassonomia = null;

  @JsonProperty("tassonomiaAvviso")
  private TassonomiaAvviso tassonomiaAvviso = null;

  @JsonProperty("idA2A")
  private String idA2A = null;

  @JsonProperty("idPendenza")
  private String idPendenza = null;

  @JsonProperty("dominio")
  private DominioIndex dominio = null;

  @JsonProperty("unitaOperativa")
  private UnitaOperativa unitaOperativa = null;

  @JsonProperty("stato")
  private StatoPendenza stato = null;

  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;

  @JsonProperty("voci")
  private List<VocePendenza> voci = new ArrayList<>();

  @JsonProperty("rpp")
  private List<Rpp> rpp = new ArrayList<>();

  @JsonProperty("pagamenti")
  private List<Pagamento> pagamenti = new ArrayList<>();

  /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public Pendenza nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return this.nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public Pendenza causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   **/
  public Pendenza soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return this.soggettoPagatore;
  }
  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public Pendenza importo(BigDecimal importo) {
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
   * Identificativo univoco versamento, assegnato se pagabile da psp
   **/
  public Pendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Data di emissione della pendenza
   **/
  public Pendenza dataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
    return this;
  }

  @JsonProperty("dataCaricamento")
  public Date getDataCaricamento() {
    return this.dataCaricamento;
  }
  public void setDataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public Pendenza dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return this.dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public Pendenza dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return this.dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Data di pagamento della pendenza
   **/
  public Pendenza dataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

  @JsonProperty("dataPagamento")
  public Date getDataPagamento() {
    return this.dataPagamento;
  }
  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  /**
   * Anno di riferimento della pendenza
   **/
  public Pendenza annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return this.annoRiferimento;
  }
  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   **/
  public Pendenza cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return this.cartellaPagamento;
  }
  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public Pendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }
  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   **/
  public Pendenza tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return this.tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   **/
  public Pendenza tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public TassonomiaAvviso getTassonomiaAvviso() {
    return this.tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public Pendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public Pendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return this.idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   **/
  public Pendenza dominio(DominioIndex dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public DominioIndex getDominio() {
    return this.dominio;
  }
  public void setDominio(DominioIndex dominio) {
    this.dominio = dominio;
  }

  /**
   **/
  public Pendenza unitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
    return this;
  }

  @JsonProperty("unitaOperativa")
  public UnitaOperativa getUnitaOperativa() {
    return this.unitaOperativa;
  }
  public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
  }

  /**
   **/
  public Pendenza stato(StatoPendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoPendenza getStato() {
    return this.stato;
  }
  public void setStato(StatoPendenza stato) {
    this.stato = stato;
  }

  /**
   **/
  public Pendenza segnalazioni(List<Segnalazione> segnalazioni) {
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

  /**
   **/
  public Pendenza voci(List<VocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  @JsonProperty("voci")
  public List<VocePendenza> getVoci() {
    return this.voci;
  }
  public void setVoci(List<VocePendenza> voci) {
    this.voci = voci;
  }

  /**
   **/
  public Pendenza rpp(List<Rpp> rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public List<Rpp> getRpp() {
    return this.rpp;
  }
  public void setRpp(List<Rpp> rpp) {
    this.rpp = rpp;
  }

  /**
   **/
  public Pendenza pagamenti(List<Pagamento> pagamenti) {
    this.pagamenti = pagamenti;
    return this;
  }

  @JsonProperty("pagamenti")
  public List<Pagamento> getPagamenti() {
    return this.pagamenti;
  }
  public void setPagamenti(List<Pagamento> pagamenti) {
    this.pagamenti = pagamenti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Pendenza pendenza = (Pendenza) o;
    return  Objects.equals(this.nome, pendenza.nome) &&
        Objects.equals(this.causale, pendenza.causale) &&
        Objects.equals(this.soggettoPagatore, pendenza.soggettoPagatore) &&
        Objects.equals(this.importo, pendenza.importo) &&
        Objects.equals(this.numeroAvviso, pendenza.numeroAvviso) &&
        Objects.equals(this.dataCaricamento, pendenza.dataCaricamento) &&
        Objects.equals(this.dataValidita, pendenza.dataValidita) &&
        Objects.equals(this.dataScadenza, pendenza.dataScadenza) &&
        Objects.equals(this.dataPagamento, pendenza.dataPagamento) &&
        Objects.equals(this.annoRiferimento, pendenza.annoRiferimento) &&
        Objects.equals(this.cartellaPagamento, pendenza.cartellaPagamento) &&
        Objects.equals(this.datiAllegati, pendenza.datiAllegati) &&
        Objects.equals(this.tassonomia, pendenza.tassonomia) &&
        Objects.equals(this.tassonomiaAvviso, pendenza.tassonomiaAvviso) &&
        Objects.equals(this.idA2A, pendenza.idA2A) &&
        Objects.equals(this.idPendenza, pendenza.idPendenza) &&
        Objects.equals(this.dominio, pendenza.dominio) &&
        Objects.equals(this.unitaOperativa, pendenza.unitaOperativa) &&
        Objects.equals(this.stato, pendenza.stato) &&
        Objects.equals(this.segnalazioni, pendenza.segnalazioni) &&
        Objects.equals(this.voci, pendenza.voci) &&
        Objects.equals(this.rpp, pendenza.rpp) &&
        Objects.equals(this.pagamenti, pendenza.pagamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, dataPagamento, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, idA2A, idPendenza, dominio, unitaOperativa, stato, segnalazioni, voci, rpp, pagamenti);
  }

  public static Pendenza parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Pendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pendenza {\n");

    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(this.toIndentedString(this.soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(this.toIndentedString(this.dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(this.toIndentedString(this.dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(this.toIndentedString(this.dataScadenza)).append("\n");
    sb.append("    dataPagamento: ").append(this.toIndentedString(this.dataPagamento)).append("\n");
    sb.append("    annoRiferimento: ").append(this.toIndentedString(this.annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(this.toIndentedString(this.cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(this.toIndentedString(this.datiAllegati)).append("\n");
    sb.append("    tassonomia: ").append(this.toIndentedString(this.tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(this.toIndentedString(this.tassonomiaAvviso)).append("\n");
    sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
    sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
    sb.append("    dominio: ").append(this.toIndentedString(this.dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(this.toIndentedString(this.unitaOperativa)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    segnalazioni: ").append(this.toIndentedString(this.segnalazioni)).append("\n");
    sb.append("    voci: ").append(this.toIndentedString(this.voci)).append("\n");
    sb.append("    rpp: ").append(this.toIndentedString(this.rpp)).append("\n");
    sb.append("    pagamenti: ").append(this.toIndentedString(this.pagamenti)).append("\n");
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



