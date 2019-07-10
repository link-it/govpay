package it.govpay.pagamento.v2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
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
"idA2A",
"idPendenza",
"dominio",
"unitaOperativa",
"stato",
"segnalazioni",
"iuvAvviso",
"iuvPagamento",
"dataPagamento",
"rpp",
"pagamenti",
})
public class PendenzaIndex extends JSONSerializable {
  
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
  
  @JsonProperty("iuvAvviso")
  private String iuvAvviso = null;
  
  @JsonProperty("iuvPagamento")
  private String iuvPagamento = null;
  
  @JsonProperty("dataPagamento")
  private Date dataPagamento = null;
  
  @JsonProperty("rpp")
  private String rpp = null;
  
  @JsonProperty("pagamenti")
  private String pagamenti = null;
  
  /**
   * Identificativo della tipologia pendenza
   **/
  public PendenzaIndex idTipoPendenza(String idTipoPendenza) {
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
  public PendenzaIndex nome(String nome) {
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
  public PendenzaIndex causale(String causale) {
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
  public PendenzaIndex soggettoPagatore(Soggetto soggettoPagatore) {
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
  public PendenzaIndex importo(BigDecimal importo) {
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
  public PendenzaIndex numeroAvviso(String numeroAvviso) {
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
  public PendenzaIndex dataCaricamento(Date dataCaricamento) {
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
  public PendenzaIndex dataValidita(Date dataValidita) {
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
  public PendenzaIndex dataScadenza(Date dataScadenza) {
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
   * Anno di riferimento della pendenza
   **/
  public PendenzaIndex annoRiferimento(BigDecimal annoRiferimento) {
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
  public PendenzaIndex cartellaPagamento(String cartellaPagamento) {
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
  public PendenzaIndex datiAllegati(Object datiAllegati) {
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
  public PendenzaIndex tassonomia(String tassonomia) {
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
  public PendenzaIndex tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
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
   * Identificativo della direzione interna all'ente creditore
   **/
  public PendenzaIndex direzione(String direzione) {
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
  public PendenzaIndex divisione(String divisione) {
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
   * Identificativo del gestionale responsabile della pendenza
   **/
  public PendenzaIndex idA2A(String idA2A) {
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
  public PendenzaIndex idPendenza(String idPendenza) {
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
  public PendenzaIndex dominio(DominioIndex dominio) {
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
  public PendenzaIndex unitaOperativa(UnitaOperativa unitaOperativa) {
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
  public PendenzaIndex stato(StatoPendenza stato) {
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
  public PendenzaIndex segnalazioni(List<Segnalazione> segnalazioni) {
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
   * Iuv avviso, assegnato se pagabile da psp
   **/
  public PendenzaIndex iuvAvviso(String iuvAvviso) {
    this.iuvAvviso = iuvAvviso;
    return this;
  }

  @JsonProperty("iuvAvviso")
  public String getIuvAvviso() {
    return iuvAvviso;
  }
  public void setIuvAvviso(String iuvAvviso) {
    this.iuvAvviso = iuvAvviso;
  }

  /**
   * Iuv dell'ultimo pagamento eseguito con successo
   **/
  public PendenzaIndex iuvPagamento(String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
    return this;
  }

  @JsonProperty("iuvPagamento")
  public String getIuvPagamento() {
    return iuvPagamento;
  }
  public void setIuvPagamento(String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
  }

  /**
   * Data di pagamento della pendenza
   **/
  public PendenzaIndex dataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

  @JsonProperty("dataPagamento")
  public Date getDataPagamento() {
    return dataPagamento;
  }
  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  /**
   * Url per l'elenco delle rpp emesse per la pendenza
   **/
  public PendenzaIndex rpp(String rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public String getRpp() {
    return this.rpp;
  }
  public void setRpp(String rpp) {
    this.rpp = rpp;
  }

  /**
   * Url per l'elenco dei pagamenti da portale comprensivi della pendenza
   **/
  public PendenzaIndex pagamenti(String pagamenti) {
    this.pagamenti = pagamenti;
    return this;
  }

  @JsonProperty("pagamenti")
  public String getPagamenti() {
    return this.pagamenti;
  }
  public void setPagamenti(String pagamenti) {
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
    PendenzaIndex pendenzaIndex = (PendenzaIndex) o;
    return Objects.equals(idTipoPendenza, pendenzaIndex.idTipoPendenza) &&
        Objects.equals(nome, pendenzaIndex.nome) &&
        Objects.equals(causale, pendenzaIndex.causale) &&
        Objects.equals(soggettoPagatore, pendenzaIndex.soggettoPagatore) &&
        Objects.equals(importo, pendenzaIndex.importo) &&
        Objects.equals(numeroAvviso, pendenzaIndex.numeroAvviso) &&
        Objects.equals(dataCaricamento, pendenzaIndex.dataCaricamento) &&
        Objects.equals(dataValidita, pendenzaIndex.dataValidita) &&
        Objects.equals(dataScadenza, pendenzaIndex.dataScadenza) &&
        Objects.equals(annoRiferimento, pendenzaIndex.annoRiferimento) &&
        Objects.equals(cartellaPagamento, pendenzaIndex.cartellaPagamento) &&
        Objects.equals(datiAllegati, pendenzaIndex.datiAllegati) &&
        Objects.equals(tassonomia, pendenzaIndex.tassonomia) &&
        Objects.equals(tassonomiaAvviso, pendenzaIndex.tassonomiaAvviso) &&
        Objects.equals(direzione, pendenzaIndex.direzione) &&
        Objects.equals(divisione, pendenzaIndex.divisione) &&
        Objects.equals(idA2A, pendenzaIndex.idA2A) &&
        Objects.equals(idPendenza, pendenzaIndex.idPendenza) &&
        Objects.equals(dominio, pendenzaIndex.dominio) &&
        Objects.equals(unitaOperativa, pendenzaIndex.unitaOperativa) &&
        Objects.equals(stato, pendenzaIndex.stato) &&
        Objects.equals(segnalazioni, pendenzaIndex.segnalazioni) &&
        Objects.equals(iuvAvviso, pendenzaIndex.iuvAvviso) &&
        Objects.equals(iuvPagamento, pendenzaIndex.iuvPagamento) &&
        Objects.equals(dataPagamento, pendenzaIndex.dataPagamento) &&
        Objects.equals(rpp, pendenzaIndex.rpp) &&
        Objects.equals(pagamenti, pendenzaIndex.pagamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, idA2A, idPendenza, dominio, unitaOperativa, stato, segnalazioni, iuvAvviso, iuvPagamento, dataPagamento, rpp, pagamenti);
  }

  public static PendenzaIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaIndex {\n");
    
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
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    iuvAvviso: ").append(toIndentedString(iuvAvviso)).append("\n");
    sb.append("    iuvPagamento: ").append(toIndentedString(iuvPagamento)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
    sb.append("    pagamenti: ").append(toIndentedString(pagamenti)).append("\n");
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



