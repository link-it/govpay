package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

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
"annoRiferimento",
"cartellaPagamento",
"datiAllegati",
"tassonomia",
"tassonomiaAvviso",
"direzione",
"divisione",
"documento",
"dataNotificaAvviso",
"dataPromemoriaScadenza",
"proprieta",
})
public class PendenzaBase extends JSONSerializable {
  
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
  
  @JsonProperty("dataNotificaAvviso")
  private Date dataNotificaAvviso = null;
  
  @JsonProperty("dataPromemoriaScadenza")
  private Date dataPromemoriaScadenza = null;
  
  @JsonProperty("proprieta")
  private ProprietaPendenza proprieta = null;
  
  /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public PendenzaBase nome(String nome) {
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
  public PendenzaBase causale(String causale) {
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
  public PendenzaBase soggettoPagatore(Soggetto soggettoPagatore) {
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
  public PendenzaBase importo(BigDecimal importo) {
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
  public PendenzaBase numeroAvviso(String numeroAvviso) {
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
  public PendenzaBase dataCaricamento(Date dataCaricamento) {
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
  public PendenzaBase dataValidita(Date dataValidita) {
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
  public PendenzaBase dataScadenza(Date dataScadenza) {
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
  public PendenzaBase annoRiferimento(BigDecimal annoRiferimento) {
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
  public PendenzaBase cartellaPagamento(String cartellaPagamento) {
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
  public PendenzaBase datiAllegati(Object datiAllegati) {
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
  public PendenzaBase tassonomia(String tassonomia) {
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
  public PendenzaBase tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
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
  public PendenzaBase direzione(String direzione) {
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
  public PendenzaBase divisione(String divisione) {
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
  public PendenzaBase documento(Documento documento) {
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
   * Data in cui inviare il promemoria di pagamento.
   **/
  public PendenzaBase dataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
    return this;
  }

  @JsonProperty("dataNotificaAvviso")
  public Date getDataNotificaAvviso() {
    return dataNotificaAvviso;
  }
  public void setDataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
  }

  /**
   * Data in cui inviare il promemoria di scadenza della pendenza.
   **/
  public PendenzaBase dataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
    return this;
  }

  @JsonProperty("dataPromemoriaScadenza")
  public Date getDataPromemoriaScadenza() {
    return dataPromemoriaScadenza;
  }
  public void setDataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
  }

  /**
   **/
  public PendenzaBase proprieta(ProprietaPendenza proprieta) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PendenzaBase pendenzaBase = (PendenzaBase) o;
    return Objects.equals(nome, pendenzaBase.nome) &&
        Objects.equals(causale, pendenzaBase.causale) &&
        Objects.equals(soggettoPagatore, pendenzaBase.soggettoPagatore) &&
        Objects.equals(importo, pendenzaBase.importo) &&
        Objects.equals(numeroAvviso, pendenzaBase.numeroAvviso) &&
        Objects.equals(dataCaricamento, pendenzaBase.dataCaricamento) &&
        Objects.equals(dataValidita, pendenzaBase.dataValidita) &&
        Objects.equals(dataScadenza, pendenzaBase.dataScadenza) &&
        Objects.equals(annoRiferimento, pendenzaBase.annoRiferimento) &&
        Objects.equals(cartellaPagamento, pendenzaBase.cartellaPagamento) &&
        Objects.equals(datiAllegati, pendenzaBase.datiAllegati) &&
        Objects.equals(tassonomia, pendenzaBase.tassonomia) &&
        Objects.equals(tassonomiaAvviso, pendenzaBase.tassonomiaAvviso) &&
        Objects.equals(direzione, pendenzaBase.direzione) &&
        Objects.equals(divisione, pendenzaBase.divisione) &&
        Objects.equals(documento, pendenzaBase.documento) &&
        Objects.equals(dataNotificaAvviso, pendenzaBase.dataNotificaAvviso) &&
        Objects.equals(dataPromemoriaScadenza, pendenzaBase.dataPromemoriaScadenza) &&
        Objects.equals(proprieta, pendenzaBase.proprieta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, documento, dataNotificaAvviso, dataPromemoriaScadenza, proprieta);
  }

  public static PendenzaBase parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaBase.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaBase";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaBase {\n");
    
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
    sb.append("    dataNotificaAvviso: ").append(toIndentedString(dataNotificaAvviso)).append("\n");
    sb.append("    dataPromemoriaScadenza: ").append(toIndentedString(dataPromemoriaScadenza)).append("\n");
    sb.append("    proprieta: ").append(toIndentedString(proprieta)).append("\n");
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



