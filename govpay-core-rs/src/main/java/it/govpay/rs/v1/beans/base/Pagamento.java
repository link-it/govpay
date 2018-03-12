package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import it.govpay.rs.v1.beans.base.Soggetto;
import it.govpay.rs.v1.beans.base.StatoPagamento;
import it.govpay.rs.v1.serializer.CustomBigDecimalSerializer;

import java.math.BigDecimal;
import java.util.Date;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"id",
"idSessionePortale",
"idSessionePsp",
"nome",
"importo",
"stato",
"pspRedirectUrl",
"dataRichiestaPagamento",
"datiAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"canale",
"pendenze",
"rpp",
})
public class Pagamento extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("idSessionePortale")
  private String idSessionePortale = null;
  
  @JsonProperty("idSessionePsp")
  private String idSessionePsp = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("stato")
  private StatoPagamento stato = null;
  
  @JsonProperty("pspRedirectUrl")
  private String pspRedirectUrl = null;
  
  @JsonProperty("dataRichiestaPagamento")
  private Date dataRichiestaPagamento = null;
  
  @JsonProperty("datiAddebito")
  private Object datiAddebito = null;
  
  @JsonProperty("dataEsecuzionePagamento")
  private Date dataEsecuzionePagamento = null;
  
  @JsonProperty("credenzialiPagatore")
  private String credenzialiPagatore = null;
  
  @JsonProperty("soggettoVersante")
  private Soggetto soggettoVersante = null;
  
    
  /**
   * modalita' di autenticazione del soggetto versante
   */
  public enum AutenticazioneSoggettoEnum {
    
    
        
            
    CNS("CNS"),
    
            
    USR("USR"),
    
            
    OTH("OTH"),
    
            
    N_A("N/A");
            
        
    

    private String value;

    AutenticazioneSoggettoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static AutenticazioneSoggettoEnum fromValue(String text) {
      for (AutenticazioneSoggettoEnum b : AutenticazioneSoggettoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("autenticazioneSoggetto")
  private AutenticazioneSoggettoEnum autenticazioneSoggetto = null;
  
  @JsonProperty("canale")
  private String canale = null;
  
  @JsonProperty("pendenze")
  private String pendenze = null;
  
  @JsonProperty("rpp")
  private String rpp = null;
  
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
   * Identificativo del pagamento assegnato dal portale chiamante
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
   * Nome del pagamento da visualizzare all'utente.
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
   * Importo del pagamento. Corrisponde alla somma degli importi delle pendenze al momento della richiesta
   **/
  public Pagamento importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  @JsonSerialize(using = CustomBigDecimalSerializer.class)
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
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
   * Data e ora di avvio del pagamento
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
   * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
   **/
  public Pagamento datiAddebito(Object datiAddebito) {
    this.datiAddebito = datiAddebito;
    return this;
  }

  @JsonProperty("datiAddebito")
  public Object getDatiAddebito() {
    return datiAddebito;
  }
  public void setDatiAddebito(Object datiAddebito) {
    this.datiAddebito = datiAddebito;
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
   * modalita' di autenticazione del soggetto versante
   **/
  public Pagamento autenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
    return this;
  }

  @JsonProperty("autenticazioneSoggetto")
  public AutenticazioneSoggettoEnum getAutenticazioneSoggetto() {
    return autenticazioneSoggetto;
  }
  public void setAutenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  /**
   * Url per il canale di pagamento utilizzato
   **/
  public Pagamento canale(String canale) {
    this.canale = canale;
    return this;
  }

  @JsonProperty("canale")
  public String getCanale() {
    return canale;
  }
  public void setCanale(String canale) {
    this.canale = canale;
  }

  /**
   * Url per le pendenze oggetto del Pagamento
   **/
  public Pagamento pendenze(String pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  @JsonProperty("pendenze")
  public String getPendenze() {
    return pendenze;
  }
  public void setPendenze(String pendenze) {
    this.pendenze = pendenze;
  }

  /**
   * Url per le richieste di pagamento oggetto del Pagamento
   **/
  public Pagamento rpp(String rpp) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pagamento pagamento = (Pagamento) o;
    return Objects.equals(id, pagamento.id) &&
        Objects.equals(idSessionePortale, pagamento.idSessionePortale) &&
        Objects.equals(idSessionePsp, pagamento.idSessionePsp) &&
        Objects.equals(nome, pagamento.nome) &&
        Objects.equals(importo, pagamento.importo) &&
        Objects.equals(stato, pagamento.stato) &&
        Objects.equals(pspRedirectUrl, pagamento.pspRedirectUrl) &&
        Objects.equals(dataRichiestaPagamento, pagamento.dataRichiestaPagamento) &&
        Objects.equals(datiAddebito, pagamento.datiAddebito) &&
        Objects.equals(dataEsecuzionePagamento, pagamento.dataEsecuzionePagamento) &&
        Objects.equals(credenzialiPagatore, pagamento.credenzialiPagatore) &&
        Objects.equals(soggettoVersante, pagamento.soggettoVersante) &&
        Objects.equals(autenticazioneSoggetto, pagamento.autenticazioneSoggetto) &&
        Objects.equals(canale, pagamento.canale) &&
        Objects.equals(pendenze, pagamento.pendenze) &&
        Objects.equals(rpp, pagamento.rpp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idSessionePortale, idSessionePsp, nome, importo, stato, pspRedirectUrl, dataRichiestaPagamento, datiAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, canale, pendenze, rpp);
  }

  public static Pagamento parse(String json) {
    return (Pagamento) parse(json, Pagamento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pagamento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pagamento {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idSessionePortale: ").append(toIndentedString(idSessionePortale)).append("\n");
    sb.append("    idSessionePsp: ").append(toIndentedString(idSessionePsp)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    pspRedirectUrl: ").append(toIndentedString(pspRedirectUrl)).append("\n");
    sb.append("    dataRichiestaPagamento: ").append(toIndentedString(dataRichiestaPagamento)).append("\n");
    sb.append("    datiAddebito: ").append(toIndentedString(datiAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
    sb.append("    canale: ").append(toIndentedString(canale)).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
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



