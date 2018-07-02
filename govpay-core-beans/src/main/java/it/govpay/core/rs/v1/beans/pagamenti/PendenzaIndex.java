package it.govpay.core.rs.v1.beans.pagamenti;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.utils.SimpleDateFormatUtils;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
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
"idA2A",
"idPendenza",
"dominio",
"unitaOperativa",
"stato",
"segnalazioni",
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
  private String datiAllegati = null;
  
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
    return nome;
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
    return causale;
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
    return soggettoPagatore;
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
    return importo;
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
    return numeroAvviso;
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
    return dataCaricamento;
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
    return dataValidita;
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
    return dataScadenza;
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
    return annoRiferimento;
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
    return cartellaPagamento;
  }
  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public PendenzaIndex datiAllegati(String datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

  @JsonProperty("datiAllegati")
  public String getDatiAllegati() {
    return datiAllegati;
  }
  public void setDatiAllegati(String datiAllegati) {
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
    return tassonomia;
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
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
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
    return idA2A;
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
    return idPendenza;
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
    return dominio;
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
    return unitaOperativa;
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
    return stato;
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
    return segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
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
    return rpp;
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
    return pagamenti;
  }
  public void setPagamenti(String pagamenti) {
    this.pagamenti = pagamenti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
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
        Objects.equals(idA2A, pendenzaIndex.idA2A) &&
        Objects.equals(idPendenza, pendenzaIndex.idPendenza) &&
        Objects.equals(dominio, pendenzaIndex.dominio) &&
        Objects.equals(unitaOperativa, pendenzaIndex.unitaOperativa) &&
        Objects.equals(stato, pendenzaIndex.stato) &&
        Objects.equals(segnalazioni, pendenzaIndex.segnalazioni) &&
        Objects.equals(rpp, pendenzaIndex.rpp) &&
        Objects.equals(pagamenti, pendenzaIndex.pagamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, idA2A, idPendenza, dominio, unitaOperativa, stato, segnalazioni, rpp, pagamenti);
  }

  public static PendenzaIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (PendenzaIndex) parse(json, PendenzaIndex.class);
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
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
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
  
	@Override
	public String toJSON(String fields) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		return super.toJSON(fields,mapper);
	}

}



