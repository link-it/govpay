/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"idUnitaOperativa",
"idTipoPendenza",
"nome",
"causale",
"soggettoPagatore",
"importo",
"numeroAvviso",
"dataCaricamento",
"dataValidita",
"dataScadenza",
"annoRiferimento",
"cartellaPagamento",
"datiAllegati",
"tassonomia",
"tassonomiaAvviso",
"direzione",
"divisione",
"documento",
"proprieta",
"voci",
"allegati",
"idA2A",
"idPendenza",
})
public class NuovaPendenzaTracciato extends JSONSerializable implements IValidable {

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;

  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;

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

  @JsonProperty("direzione")
  private String direzione = null;

  @JsonProperty("divisione")
  private String divisione = null;

  @JsonProperty("documento")
  private Documento documento = null;

  @JsonProperty("proprieta")
  private ProprietaPendenza proprieta = null;

  @JsonProperty("voci")
  private List<NuovaVocePendenza> voci = new ArrayList<>();

  @JsonProperty("allegati")
  private List<NuovoAllegatoPendenza> allegati = null;

  @JsonProperty("idA2A")
  private String idA2A = null;

  @JsonProperty("idPendenza")
  private String idPendenza = null;

  /**
   * Identificativo del dominio creditore
   **/
  public NuovaPendenzaTracciato idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Identificativo dell'unita' operativa
   **/
  public NuovaPendenzaTracciato idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }
  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  /**
   * Identificativo della tipologia pendenza
   **/
  public NuovaPendenzaTracciato idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public NuovaPendenzaTracciato nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public NuovaPendenzaTracciato causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   **/
  public NuovaPendenzaTracciato soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return soggettoPagatore;
  }
  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public NuovaPendenzaTracciato importo(BigDecimal importo) {
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
   * Numero avviso, assegnato se pagabile da psp
   **/
  public NuovaPendenzaTracciato numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Data di emissione della pendenza
   **/
  public NuovaPendenzaTracciato dataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
    return this;
  }

  @JsonProperty("dataCaricamento")
  public Date getDataCaricamento() {
    return dataCaricamento;
  }
  public void setDataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public NuovaPendenzaTracciato dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public NuovaPendenzaTracciato dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Anno di riferimento della pendenza
   **/
  public NuovaPendenzaTracciato annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return annoRiferimento;
  }
  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   **/
  public NuovaPendenzaTracciato cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return cartellaPagamento;
  }
  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public NuovaPendenzaTracciato datiAllegati(Object datiAllegati) {
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
  public NuovaPendenzaTracciato tassonomia(String tassonomia) {
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
  public NuovaPendenzaTracciato tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public TassonomiaAvviso getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Identificativo della direzione interna all'ente creditore
   **/
  public NuovaPendenzaTracciato direzione(String direzione) {
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
  public NuovaPendenzaTracciato divisione(String divisione) {
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
   **/
  public NuovaPendenzaTracciato documento(Documento documento) {
    this.documento = documento;
    return this;
  }

  @JsonProperty("documento")
  public Documento getDocumento() {
    return documento;
  }
  public void setDocumento(Documento documento) {
    this.documento = documento;
  }

  /**
   **/
  public NuovaPendenzaTracciato proprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
    return this;
  }

  @JsonProperty("proprieta")
  public ProprietaPendenza getProprieta() {
    return proprieta;
  }
  public void setProprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
  }

  /**
   **/
  public NuovaPendenzaTracciato voci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  @JsonProperty("voci")
  public List<NuovaVocePendenza> getVoci() {
    return voci;
  }
  public void setVoci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
  }

  /**
   **/
  public NuovaPendenzaTracciato allegati(List<NuovoAllegatoPendenza> allegati) {
    this.allegati = allegati;
    return this;
  }

  @JsonProperty("allegati")
  public List<NuovoAllegatoPendenza> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<NuovoAllegatoPendenza> allegati) {
    this.allegati = allegati;
  }

  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public NuovaPendenzaTracciato idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public NuovaPendenzaTracciato idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NuovaPendenzaTracciato nuovaPendenzaTracciato = (NuovaPendenzaTracciato) o;
    return Objects.equals(idDominio, nuovaPendenzaTracciato.idDominio) &&
        Objects.equals(idUnitaOperativa, nuovaPendenzaTracciato.idUnitaOperativa) &&
        Objects.equals(idTipoPendenza, nuovaPendenzaTracciato.idTipoPendenza) &&
        Objects.equals(nome, nuovaPendenzaTracciato.nome) &&
        Objects.equals(causale, nuovaPendenzaTracciato.causale) &&
        Objects.equals(soggettoPagatore, nuovaPendenzaTracciato.soggettoPagatore) &&
        Objects.equals(importo, nuovaPendenzaTracciato.importo) &&
        Objects.equals(numeroAvviso, nuovaPendenzaTracciato.numeroAvviso) &&
        Objects.equals(dataCaricamento, nuovaPendenzaTracciato.dataCaricamento) &&
        Objects.equals(dataValidita, nuovaPendenzaTracciato.dataValidita) &&
        Objects.equals(dataScadenza, nuovaPendenzaTracciato.dataScadenza) &&
        Objects.equals(annoRiferimento, nuovaPendenzaTracciato.annoRiferimento) &&
        Objects.equals(cartellaPagamento, nuovaPendenzaTracciato.cartellaPagamento) &&
        Objects.equals(datiAllegati, nuovaPendenzaTracciato.datiAllegati) &&
        Objects.equals(tassonomia, nuovaPendenzaTracciato.tassonomia) &&
        Objects.equals(tassonomiaAvviso, nuovaPendenzaTracciato.tassonomiaAvviso) &&
        Objects.equals(direzione, nuovaPendenzaTracciato.direzione) &&
        Objects.equals(divisione, nuovaPendenzaTracciato.divisione) &&
        Objects.equals(documento, nuovaPendenzaTracciato.documento) &&
        Objects.equals(proprieta, nuovaPendenzaTracciato.proprieta) &&
        Objects.equals(voci, nuovaPendenzaTracciato.voci) &&
        Objects.equals(allegati, nuovaPendenzaTracciato.allegati) &&
        Objects.equals(idA2A, nuovaPendenzaTracciato.idA2A) &&
        Objects.equals(idPendenza, nuovaPendenzaTracciato.idPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, idUnitaOperativa, idTipoPendenza, nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, documento, proprieta, voci, allegati, idA2A, idPendenza);
  }

  public static NuovaPendenzaTracciato parse(String json) throws IOException {
    return parse(json, NuovaPendenzaTracciato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovaPendenzaTracciato";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaPendenzaTracciato {\n");

    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(toIndentedString(dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
    sb.append("    proprieta: ").append(toIndentedString(proprieta)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
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

  @Override
  public void validate() throws it.govpay.core.exceptions.ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();

		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

		validatoreId.validaIdApplicazione("idA2A", this.idA2A);
		validatoreId.validaIdPendenza("idPendenza", this.idPendenza);

//			validatoreId.validaIdDominio("idDominio", this.idDominio);

		validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa, false);
		validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza, false);

		ValidatoreUtils.validaNomePendenza(vf, "nome", nome);
		ValidatoreUtils.validaCausale(vf, "causale", causale);

		// Il vincolo di obbligatorieta' del soggetto pagatore e' stato eliminato per consentire di acquisire pendenze senza indicare il debitore.
		vf.getValidator("soggettoPagatore", this.soggettoPagatore).validateFields();

		ValidatoreUtils.validaImportoOpzionale(vf, "importo", importo);
		ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
		ValidatoreUtils.validaData(vf, "dataValidita", this.dataValidita);
		ValidatoreUtils.validaData(vf, "dataScadenza", this.dataScadenza);
		ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
		ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
		ValidatoreUtils.validaTassonomia(vf, "tassonomia", tassonomia);

		vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();

		validatoreId.validaIdDirezione("direzione",this.direzione, false);
		validatoreId.validaIdDivisione("divisione",this.divisione, false);

		vf.getValidator("documento", this.documento).validateFields();

		vf.getValidator("proprieta", this.proprieta).validateFields();
		vf.getValidator("allegati", this.allegati).validateObjects();
	}
}



