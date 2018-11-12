package it.govpay.core.rs.v2.beans.pagamenti;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"nome",
"dataRichiestaPagamento",
"idSessionePortale",
"idSessionePsp",
"importo",
"stato",
"pspRedirectUrl",
"urlRitorno",
"contoAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"lingua",
"pendenze",
"rpp",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagamento extends JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("dataRichiestaPagamento")
  private Date dataRichiestaPagamento = null;
  
  @JsonProperty("idSessionePortale")
  private String idSessionePortale = null;
  
  @JsonProperty("idSessionePsp")
  private String idSessionePsp = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("stato")
  private StatoPagamento stato = null;
  
  @JsonProperty("pspRedirectUrl")
  private String pspRedirectUrl = null;
  
  @JsonProperty("urlRitorno")
  private String urlRitorno = null;
  
  @JsonProperty("contoAddebito")
  private ContoAddebito contoAddebito = null;
  
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
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
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
  
    
  /**
   * Indica il codice della lingua da utilizzare per l’esposizione delle pagine web.
   */
  public enum LinguaEnum {
    
    
        
            
    IT("IT"),
    
            
    EN("EN"),
    
            
    FR("FR"),
    
            
    DE("DE"),
    
            
    SL("SL");
            
        
    

    private String value;

    LinguaEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static LinguaEnum fromValue(String text) {
      for (LinguaEnum b : LinguaEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("lingua")
  private LinguaEnum lingua = LinguaEnum.IT;
  
  @JsonProperty("pendenze")
  private List<PendenzaIndex> pendenze = null;
  
  @JsonProperty("rpp")
  private List<RppIndex> rpp = null;
  
  /**
   * Identificativo del pagamento assegnato da GovPay
   **/
  public Pagamento id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return this.id;
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
    return this.nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Data di richiesta del pagamento
   **/
  public Pagamento dataRichiestaPagamento(Date dataRichiestaPagamento) {
    this.dataRichiestaPagamento = dataRichiestaPagamento;
    return this;
  }

  @JsonProperty("dataRichiestaPagamento")
  public Date getDataRichiestaPagamento() {
    return this.dataRichiestaPagamento;
  }
  public void setDataRichiestaPagamento(Date dataRichiestaPagamento) {
    this.dataRichiestaPagamento = dataRichiestaPagamento;
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
    return this.idSessionePortale;
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
    return this.idSessionePsp;
  }
  public void setIdSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
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
    return this.importo;
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
    return this.stato;
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
    return this.pspRedirectUrl;
  }
  public void setPspRedirectUrl(String pspRedirectUrl) {
    this.pspRedirectUrl = pspRedirectUrl;
  }

  /**
   * url di ritorno al portale al termine della sessione di pagamento
   **/
  public Pagamento urlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
    return this;
  }

  @JsonProperty("urlRitorno")
  public String getUrlRitorno() {
    return this.urlRitorno;
  }
  public void setUrlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
  }

  /**
   **/
  public Pagamento contoAddebito(ContoAddebito contoAddebito) {
    this.contoAddebito = contoAddebito;
    return this;
  }

  @JsonProperty("contoAddebito")
  public ContoAddebito getContoAddebito() {
    return this.contoAddebito;
  }
  public void setContoAddebito(ContoAddebito contoAddebito) {
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
    return this.dataEsecuzionePagamento;
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
    return this.credenzialiPagatore;
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
    return this.soggettoVersante;
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
    return this.autenticazioneSoggetto;
  }
  public void setAutenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  /**
   * Indica il codice della lingua da utilizzare per l’esposizione delle pagine web.
   **/
  public Pagamento lingua(LinguaEnum lingua) {
    this.lingua = lingua;
    return this;
  }

  @JsonProperty("lingua")
  public LinguaEnum getLingua() {
    return this.lingua;
  }
  public void setLingua(LinguaEnum lingua) {
    this.lingua = lingua;
  }

  /**
   **/
  public Pagamento pendenze(List<PendenzaIndex> pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  @JsonProperty("pendenze")
  public List<PendenzaIndex> getPendenze() {
    return this.pendenze;
  }
  public void setPendenze(List<PendenzaIndex> pendenze) {
    this.pendenze = pendenze;
  }

  /**
   **/
  public Pagamento rpp(List<RppIndex> rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public List<RppIndex> getRpp() {
    return this.rpp;
  }
  public void setRpp(List<RppIndex> rpp) {
    this.rpp = rpp;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Pagamento pagamento = (Pagamento) o;
    return Objects.equals(this.id, pagamento.id) &&
        Objects.equals(this.nome, pagamento.nome) &&
        Objects.equals(this.dataRichiestaPagamento, pagamento.dataRichiestaPagamento) &&
        Objects.equals(this.idSessionePortale, pagamento.idSessionePortale) &&
        Objects.equals(this.idSessionePsp, pagamento.idSessionePsp) &&
        Objects.equals(this.importo, pagamento.importo) &&
        Objects.equals(this.stato, pagamento.stato) &&
        Objects.equals(this.pspRedirectUrl, pagamento.pspRedirectUrl) &&
        Objects.equals(this.urlRitorno, pagamento.urlRitorno) &&
        Objects.equals(this.contoAddebito, pagamento.contoAddebito) &&
        Objects.equals(this.dataEsecuzionePagamento, pagamento.dataEsecuzionePagamento) &&
        Objects.equals(this.credenzialiPagatore, pagamento.credenzialiPagatore) &&
        Objects.equals(this.soggettoVersante, pagamento.soggettoVersante) &&
        Objects.equals(this.autenticazioneSoggetto, pagamento.autenticazioneSoggetto) &&
        Objects.equals(this.lingua, pagamento.lingua) &&
        Objects.equals(this.pendenze, pagamento.pendenze) &&
        Objects.equals(this.rpp, pagamento.rpp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.nome, this.dataRichiestaPagamento, this.idSessionePortale, this.idSessionePsp, this.importo, this.stato, this.pspRedirectUrl, this.urlRitorno, this.contoAddebito, this.dataEsecuzionePagamento, this.credenzialiPagatore, this.soggettoVersante, this.autenticazioneSoggetto, this.lingua, this.pendenze, this.rpp);
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
    
    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
    sb.append("    dataRichiestaPagamento: ").append(this.toIndentedString(this.dataRichiestaPagamento)).append("\n");
    sb.append("    idSessionePortale: ").append(this.toIndentedString(this.idSessionePortale)).append("\n");
    sb.append("    idSessionePsp: ").append(this.toIndentedString(this.idSessionePsp)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    pspRedirectUrl: ").append(this.toIndentedString(this.pspRedirectUrl)).append("\n");
    sb.append("    urlRitorno: ").append(this.toIndentedString(this.urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(this.toIndentedString(this.contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(this.toIndentedString(this.dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(this.toIndentedString(this.credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(this.toIndentedString(this.soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(this.toIndentedString(this.autenticazioneSoggetto)).append("\n");
    sb.append("    lingua: ").append(this.toIndentedString(this.lingua)).append("\n");
    sb.append("    pendenze: ").append(this.toIndentedString(this.pendenze)).append("\n");
    sb.append("    rpp: ").append(this.toIndentedString(this.rpp)).append("\n");
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



