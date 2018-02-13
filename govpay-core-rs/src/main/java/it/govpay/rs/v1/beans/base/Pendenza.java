package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.Soggetto;
import it.govpay.rs.v1.beans.base.StatoPendenza;
import it.govpay.rs.v1.beans.base.VocePendenza;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idA2A",
"idPendenza",
"dominio",
"unitaOperativa",
"soggettoPagatore",
"stato",
"importo",
"numeroAvviso",
"dataCaricamento",
"dataValidita",
"dataScadenza",
"annoRiferimento",
"nome",
"causale",
"tassonomiaAvviso",
"tassonomia",
"voci",
"rpts",
"pagamenti",
})
public class Pendenza extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("dominio")
  private String dominio = null;
  
  @JsonProperty("unitaOperativa")
  private String unitaOperativa = null;
  
  @JsonProperty("soggettoPagatore")
  private Soggetto soggettoPagatore = null;
  
  @JsonProperty("stato")
  private StatoPendenza stato = null;
  
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
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
  @JsonProperty("tassonomia")
  private String tassonomia = null;
  
  @JsonProperty("voci")
  private List<VocePendenza> voci = new ArrayList<VocePendenza>();
  
  @JsonProperty("rpts")
  private String rpts = null;
  
  @JsonProperty("pagamenti")
  private String pagamenti = null;
  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public Pendenza idA2A(String idA2A) {
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
  public Pendenza idPendenza(String idPendenza) {
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
   * Url per il dominio creditore
   **/
  public Pendenza dominio(String dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public String getDominio() {
    return dominio;
  }
  public void setDominio(String dominio) {
    this.dominio = dominio;
  }

  /**
   * Url per l'unita operativa creditrice
   **/
  public Pendenza unitaOperativa(String unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
    return this;
  }

  @JsonProperty("unitaOperativa")
  public String getUnitaOperativa() {
    return unitaOperativa;
  }
  public void setUnitaOperativa(String unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
  }

  /**
   **/
  public Pendenza soggettoPagatore(Soggetto soggettoPagatore) {
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
   **/
  public Pendenza stato(StatoPendenza stato) {
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
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public Pendenza importo(BigDecimal importo) {
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
  public Pendenza numeroAvviso(String numeroAvviso) {
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
  public Pendenza dataCaricamento(Date dataCaricamento) {
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
  public Pendenza dataValidita(Date dataValidita) {
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
  public Pendenza dataScadenza(Date dataScadenza) {
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
  public Pendenza annoRiferimento(BigDecimal annoRiferimento) {
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
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public Pendenza nome(String nome) {
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
  public Pendenza causale(String causale) {
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
   * Macro categoria della pendenza secondo la classificazione AgID
   **/
  public Pendenza tassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
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
    return tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   **/
  public Pendenza voci(List<VocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  @JsonProperty("voci")
  public List<VocePendenza> getVoci() {
    return voci;
  }
  public void setVoci(List<VocePendenza> voci) {
    this.voci = voci;
  }

  /**
   * Url per l'elenco delle rpt emesse per la pendenza
   **/
  public Pendenza rpts(String rpts) {
    this.rpts = rpts;
    return this;
  }

  @JsonProperty("rpts")
  public String getRpts() {
    return rpts;
  }
  public void setRpts(String rpts) {
    this.rpts = rpts;
  }

  /**
   * Url per l'elenco dei pagamenti da portale comprensivi della pendenza
   **/
  public Pendenza pagamenti(String pagamenti) {
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
    Pendenza pendenza = (Pendenza) o;
    return Objects.equals(idA2A, pendenza.idA2A) &&
        Objects.equals(idPendenza, pendenza.idPendenza) &&
        Objects.equals(dominio, pendenza.dominio) &&
        Objects.equals(unitaOperativa, pendenza.unitaOperativa) &&
        Objects.equals(soggettoPagatore, pendenza.soggettoPagatore) &&
        Objects.equals(stato, pendenza.stato) &&
        Objects.equals(importo, pendenza.importo) &&
        Objects.equals(numeroAvviso, pendenza.numeroAvviso) &&
        Objects.equals(dataCaricamento, pendenza.dataCaricamento) &&
        Objects.equals(dataValidita, pendenza.dataValidita) &&
        Objects.equals(dataScadenza, pendenza.dataScadenza) &&
        Objects.equals(annoRiferimento, pendenza.annoRiferimento) &&
        Objects.equals(nome, pendenza.nome) &&
        Objects.equals(causale, pendenza.causale) &&
        Objects.equals(tassonomiaAvviso, pendenza.tassonomiaAvviso) &&
        Objects.equals(tassonomia, pendenza.tassonomia) &&
        Objects.equals(voci, pendenza.voci) &&
        Objects.equals(rpts, pendenza.rpts) &&
        Objects.equals(pagamenti, pendenza.pagamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, dominio, unitaOperativa, soggettoPagatore, stato, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, nome, causale, tassonomiaAvviso, tassonomia, voci, rpts, pagamenti);
  }

  public static Pendenza parse(String json) {
    return (Pendenza) parse(json, Pendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(toIndentedString(dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("    rpts: ").append(toIndentedString(rpts)).append("\n");
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



