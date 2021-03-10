package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
"idA2A",
"idPendenza",
"tipoPendenza",
"dominio",
"unitaOperativa",
"stato",
"iuvAvviso",
"dataUltimoAggiornamento",
"dataPagamento",
"importoPagato",
"importoIncassato",
"iuvPagamento",
"anomalo",
"verificato",
"tipo",
"UUID",
"rpp",
"pagamenti",
})
public class PendenzaIndex extends JSONSerializable {
  
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
  private Object proprieta = null;
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("tipoPendenza")
  private TipoPendenzaIndex tipoPendenza = null;
  
  @JsonProperty("dominio")
  private DominioIndex dominio = null;
  
  @JsonProperty("unitaOperativa")
  private UnitaOperativa unitaOperativa = null;
  
  @JsonProperty("stato")
  private StatoPendenza stato = null;
  
  @JsonProperty("iuvAvviso")
  private String iuvAvviso = null;
  
  @JsonProperty("dataUltimoAggiornamento")
  private Date dataUltimoAggiornamento = null;
  
  @JsonProperty("dataPagamento")
  private Date dataPagamento = null;
  
  @JsonProperty("importoPagato")
  private BigDecimal importoPagato = null;
  
  @JsonProperty("importoIncassato")
  private BigDecimal importoIncassato = null;
  
  @JsonProperty("iuvPagamento")
  private String iuvPagamento = null;
  
  @JsonProperty("anomalo")
  private Boolean anomalo = null;
  
  @JsonProperty("verificato")
  private Boolean verificato = null;
  
  @JsonProperty("tipo")
  private TipoPendenzaTipologia tipo = null;
  
  @JsonProperty("UUID")
  private String UUID = null;
  
  @JsonProperty("rpp")
  private String rpp = null;
  
  @JsonProperty("pagamenti")
  private String pagamenti = null;
  
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
   * Numero avviso, assegnato se pagabile da psp
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
  public String getTassonomiaAvviso() {
      if(this.tassonomiaAvviso != null)
          return this.tassonomiaAvviso.toString();
    return null;
  }	
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
	  if(tassonomiaAvviso != null)
		 this.tassonomiaAvviso = TassonomiaAvviso.fromValue(tassonomiaAvviso);
  }

  @JsonIgnore
  public TassonomiaAvviso getTassonomiaAvvisoEnum() {
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
   **/
  public PendenzaIndex documento(Documento documento) {
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
  public PendenzaIndex dataNotificaAvviso(Date dataNotificaAvviso) {
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
  public PendenzaIndex dataPromemoriaScadenza(Date dataPromemoriaScadenza) {
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
   * Dati supporto per la gestione del ciclo di vita della pendenza.
   **/
  public PendenzaIndex proprieta(Object proprieta) {
    this.proprieta = proprieta;
    return this;
  }

  @JsonProperty("proprieta")
  public Object getProprieta() {
    return proprieta;
  }
  public void setProprieta(Object proprieta) {
    this.proprieta = proprieta;
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
  public PendenzaIndex tipoPendenza(TipoPendenzaIndex tipoPendenza) {
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
   * Data di ultimo aggiornamento della pendenza
   **/
  public PendenzaIndex dataUltimoAggiornamento(Date dataUltimoAggiornamento) {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    return this;
  }

  @JsonProperty("dataUltimoAggiornamento")
  public Date getDataUltimoAggiornamento() {
    return this.dataUltimoAggiornamento;
  }
  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
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
    return this.dataPagamento;
  }
  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  /**
   * Importo Pagato.
   **/
  public PendenzaIndex importoPagato(BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
    return this;
  }

  @JsonProperty("importoPagato")
  public BigDecimal getImportoPagato() {
    return this.importoPagato;
  }
  public void setImportoPagato(BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
  }

  /**
   * Importo Incassato.
   **/
  public PendenzaIndex importoIncassato(BigDecimal importoIncassato) {
    this.importoIncassato = importoIncassato;
    return this;
  }

  @JsonProperty("importoIncassato")
  public BigDecimal getImportoIncassato() {
    return this.importoIncassato;
  }
  public void setImportoIncassato(BigDecimal importoIncassato) {
    this.importoIncassato = importoIncassato;
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
   * indicazione se sono presenti eventuali anomalie
   **/
  public PendenzaIndex anomalo(Boolean anomalo) {
    this.anomalo = anomalo;
    return this;
  }

  @JsonProperty("anomalo")
  public Boolean Anomalo() {
    return anomalo;
  }
  public void setAnomalo(Boolean anomalo) {
    this.anomalo = anomalo;
  }

  /**
   * indicazione se eventuali anomalie sono state verificate da un operatore
   **/
  public PendenzaIndex verificato(Boolean verificato) {
    this.verificato = verificato;
    return this;
  }

  @JsonProperty("verificato")
  public Boolean Verificato() {
    return verificato;
  }
  public void setVerificato(Boolean verificato) {
    this.verificato = verificato;
  }

  /**
   **/
  public PendenzaIndex tipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoPendenzaTipologia getTipo() {
    return tipo;
  }
  public void setTipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
  }

  /**
   * Parametro di randomizzazione delle URL di pagamento statiche
   **/
  public PendenzaIndex UUID(String UUID) {
    this.UUID = UUID;
    return this;
  }

  @JsonProperty("UUID")
  public String getUUID() {
    return UUID;
  }
  public void setUUID(String UUID) {
    this.UUID = UUID;
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
    return Objects.equals(nome, pendenzaIndex.nome) &&
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
        Objects.equals(documento, pendenzaIndex.documento) &&
        Objects.equals(dataNotificaAvviso, pendenzaIndex.dataNotificaAvviso) &&
        Objects.equals(dataPromemoriaScadenza, pendenzaIndex.dataPromemoriaScadenza) &&
        Objects.equals(proprieta, pendenzaIndex.proprieta) &&
        Objects.equals(idA2A, pendenzaIndex.idA2A) &&
        Objects.equals(idPendenza, pendenzaIndex.idPendenza) &&
        Objects.equals(tipoPendenza, pendenzaIndex.tipoPendenza) &&
        Objects.equals(dominio, pendenzaIndex.dominio) &&
        Objects.equals(unitaOperativa, pendenzaIndex.unitaOperativa) &&
        Objects.equals(stato, pendenzaIndex.stato) &&
        Objects.equals(iuvAvviso, pendenzaIndex.iuvAvviso) &&
        Objects.equals(dataUltimoAggiornamento, pendenzaIndex.dataUltimoAggiornamento) &&
        Objects.equals(dataPagamento, pendenzaIndex.dataPagamento) &&
        Objects.equals(importoPagato, pendenzaIndex.importoPagato) &&
        Objects.equals(importoIncassato, pendenzaIndex.importoIncassato) &&
        Objects.equals(iuvPagamento, pendenzaIndex.iuvPagamento) &&
        Objects.equals(anomalo, pendenzaIndex.anomalo) &&
        Objects.equals(verificato, pendenzaIndex.verificato) &&
        Objects.equals(tipo, pendenzaIndex.tipo) &&
        Objects.equals(UUID, pendenzaIndex.UUID) &&
        Objects.equals(rpp, pendenzaIndex.rpp) &&
        Objects.equals(pagamenti, pendenzaIndex.pagamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, documento, dataNotificaAvviso, dataPromemoriaScadenza, proprieta, idA2A, idPendenza, tipoPendenza, dominio, unitaOperativa, stato, iuvAvviso, dataUltimoAggiornamento, dataPagamento, importoPagato, importoIncassato, iuvPagamento, anomalo, verificato, tipo, UUID, rpp, pagamenti);
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
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    tipoPendenza: ").append(toIndentedString(tipoPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    iuvAvviso: ").append(toIndentedString(iuvAvviso)).append("\n");
    sb.append("    dataUltimoAggiornamento: ").append(toIndentedString(dataUltimoAggiornamento)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    importoPagato: ").append(toIndentedString(importoPagato)).append("\n");
    sb.append("    importoIncassato: ").append(toIndentedString(importoIncassato)).append("\n");
    sb.append("    iuvPagamento: ").append(toIndentedString(iuvPagamento)).append("\n");
    sb.append("    anomalo: ").append(toIndentedString(anomalo)).append("\n");
    sb.append("    verificato: ").append(toIndentedString(verificato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    UUID: ").append(toIndentedString(UUID)).append("\n");
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



