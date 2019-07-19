package it.govpay.pagamento.v2.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"urlRitorno",
"contoAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"id",
"nome",
"stato",
"importo",
"idSessionePortale",
"idSessionePsp",
"pspRedirectUrl",
"dataRichiestaPagamento",
"rpp",
"pendenze",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagamento extends JSONSerializable {
  
  @JsonProperty("urlRitorno")
  private String urlRitorno = null;
  
  @JsonProperty("contoAddebito")
  private Conto contoAddebito = null;
  
  @JsonProperty("dataEsecuzionePagamento")
  private Date dataEsecuzionePagamento = null;
  
  @JsonProperty("credenzialiPagatore")
  private String credenzialiPagatore = null;
  
  @JsonProperty("soggettoVersante")
  private Soggetto soggettoVersante = null;
  
  @JsonProperty("autenticazioneSoggetto")
  private TipoAutenticazioneSoggetto autenticazioneSoggetto = null;
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("stato")
  private StatoPagamento stato = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("idSessionePortale")
  private String idSessionePortale = null;
  
  @JsonProperty("idSessionePsp")
  private String idSessionePsp = null;
  
  @JsonProperty("pspRedirectUrl")
  private String pspRedirectUrl = null;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  @JsonProperty("dataRichiestaPagamento")
  private Date dataRichiestaPagamento = null;
  
  @JsonProperty("rpp")
  private List<RppIndex> rpp = null;
  
  @JsonProperty("pendenze")
  private List<PendenzaIndex> pendenze = new ArrayList<>();
  
  /**
   * url di ritorno al portale al termine della sessione di pagamento
   **/
  public Pagamento urlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
    return this;
  }

  @JsonProperty("urlRitorno")
  public String getUrlRitorno() {
    return urlRitorno;
  }
  public void setUrlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
  }

  /**
   **/
  public Pagamento contoAddebito(Conto contoAddebito) {
    this.contoAddebito = contoAddebito;
    return this;
  }

  @JsonProperty("contoAddebito")
  public Conto getContoAddebito() {
    return contoAddebito;
  }
  public void setContoAddebito(Conto contoAddebito) {
    this.contoAddebito = contoAddebito;
  }

  /**
   * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
   **/
  public Pagamento dataEsecuzionePagamento(Date dataEsecuzionePagamento) {
    this.dataEsecuzionePagamento = dataEsecuzionePagamento;
    return this;
  }

  @JsonProperty("dataEsecuzionePagamento")
  public Date getDataEsecuzionePagamento() {
    return dataEsecuzionePagamento;
  }
  public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
    this.dataEsecuzionePagamento = dataEsecuzionePagamento;
  }

  /**
   * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
   **/
  public Pagamento credenzialiPagatore(String credenzialiPagatore) {
    this.credenzialiPagatore = credenzialiPagatore;
    return this;
  }

  @JsonProperty("credenzialiPagatore")
  public String getCredenzialiPagatore() {
    return credenzialiPagatore;
  }
  public void setCredenzialiPagatore(String credenzialiPagatore) {
    this.credenzialiPagatore = credenzialiPagatore;
  }

  /**
   **/
  public Pagamento soggettoVersante(Soggetto soggettoVersante) {
    this.soggettoVersante = soggettoVersante;
    return this;
  }

  @JsonProperty("soggettoVersante")
  public Soggetto getSoggettoVersante() {
    return soggettoVersante;
  }
  public void setSoggettoVersante(Soggetto soggettoVersante) {
    this.soggettoVersante = soggettoVersante;
  }

  /**
   **/
  public Pagamento autenticazioneSoggetto(TipoAutenticazioneSoggetto autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
    return this;
  }

  @JsonProperty("autenticazioneSoggetto")
  public TipoAutenticazioneSoggetto getAutenticazioneSoggetto() {
    return autenticazioneSoggetto;
  }
  public void setAutenticazioneSoggetto(TipoAutenticazioneSoggetto autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  /**
   * Identificativo del pagamento assegnato da GovPay
   **/
  public Pagamento id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Identificativo del pagamento assegnato da GovPay
   **/
  public Pagamento nome(String nome) {
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
   **/
  public Pagamento stato(StatoPagamento stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoPagamento getStato() {
    return stato;
  }
  public void setStato(StatoPagamento stato) {
    this.stato = stato;
  }

  /**
   * Importo del pagamento. Corrisponde alla somma degli importi delle pendenze al momento della richiesta
   **/
  public Pagamento importo(BigDecimal importo) {
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
   * Identificativo della sessione di pagamento assegnato dall'EC
   **/
  public Pagamento idSessionePortale(String idSessionePortale) {
    this.idSessionePortale = idSessionePortale;
    return this;
  }

  @JsonProperty("idSessionePortale")
  public String getIdSessionePortale() {
    return idSessionePortale;
  }
  public void setIdSessionePortale(String idSessionePortale) {
    this.idSessionePortale = idSessionePortale;
  }

  /**
   * Identificativo del pagamento assegnato dal psp utilizzato
   **/
  public Pagamento idSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
    return this;
  }

  @JsonProperty("idSessionePsp")
  public String getIdSessionePsp() {
    return idSessionePsp;
  }
  public void setIdSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
  }

  /**
   * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
   **/
  public Pagamento pspRedirectUrl(String pspRedirectUrl) {
    this.pspRedirectUrl = pspRedirectUrl;
    return this;
  }

  @JsonProperty("pspRedirectUrl")
  public String getPspRedirectUrl() {
    return pspRedirectUrl;
  }
  public void setPspRedirectUrl(String pspRedirectUrl) {
    this.pspRedirectUrl = pspRedirectUrl;
  }

  /**
   * Data in cui e' stato inserito il pagamento.
   **/
  public Pagamento dataRichiestaPagamento(Date dataRichiestaPagamento) {
    this.dataRichiestaPagamento = dataRichiestaPagamento;
    return this;
  }

  @JsonProperty("dataRichiestaPagamento")
  public Date getDataRichiestaPagamento() {
    return dataRichiestaPagamento;
  }
  public void setDataRichiestaPagamento(Date dataRichiestaPagamento) {
    this.dataRichiestaPagamento = dataRichiestaPagamento;
  }

  /**
   **/
  public Pagamento rpp(List<RppIndex> rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public List<RppIndex> getRpp() {
    return rpp;
  }
  public void setRpp(List<RppIndex> rpp) {
    this.rpp = rpp;
  }

  /**
   **/
  public Pagamento pendenze(List<PendenzaIndex> pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  @JsonProperty("pendenze")
  public List<PendenzaIndex> getPendenze() {
    return pendenze;
  }
  public void setPendenze(List<PendenzaIndex> pendenze) {
    this.pendenze = pendenze;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pagamento pagamento = (Pagamento) o;
    return Objects.equals(urlRitorno, pagamento.urlRitorno) &&
        Objects.equals(contoAddebito, pagamento.contoAddebito) &&
        Objects.equals(dataEsecuzionePagamento, pagamento.dataEsecuzionePagamento) &&
        Objects.equals(credenzialiPagatore, pagamento.credenzialiPagatore) &&
        Objects.equals(soggettoVersante, pagamento.soggettoVersante) &&
        Objects.equals(autenticazioneSoggetto, pagamento.autenticazioneSoggetto) &&
        Objects.equals(id, pagamento.id) &&
        Objects.equals(nome, pagamento.nome) &&
        Objects.equals(stato, pagamento.stato) &&
        Objects.equals(importo, pagamento.importo) &&
        Objects.equals(idSessionePortale, pagamento.idSessionePortale) &&
        Objects.equals(idSessionePsp, pagamento.idSessionePsp) &&
        Objects.equals(pspRedirectUrl, pagamento.pspRedirectUrl) &&
        Objects.equals(dataRichiestaPagamento, pagamento.dataRichiestaPagamento) &&
        Objects.equals(rpp, pagamento.rpp) &&
        Objects.equals(pendenze, pagamento.pendenze);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urlRitorno, contoAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, id, nome, stato, importo, idSessionePortale, idSessionePsp, pspRedirectUrl, dataRichiestaPagamento, rpp, pendenze);
  }

  public static Pagamento parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Pagamento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pagamento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pagamento {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(toIndentedString(contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    idSessionePortale: ").append(toIndentedString(idSessionePortale)).append("\n");
    sb.append("    idSessionePsp: ").append(toIndentedString(idSessionePsp)).append("\n");
    sb.append("    pspRedirectUrl: ").append(toIndentedString(pspRedirectUrl)).append("\n");
    sb.append("    dataRichiestaPagamento: ").append(toIndentedString(dataRichiestaPagamento)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
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



