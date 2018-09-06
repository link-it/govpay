package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonFormat;
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
"modello",
"pspRedirectUrl",
"urlRitorno",
"contoAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"lingua",
"rpp",
"verificato",
"note",
})
public class PagamentoIndex extends JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
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
  
    
  /**
   * Modello di pagamento
   */
  public enum ModelloEnum {
    
    
        
            
    ENTE("Pagamento ad iniziativa Ente"),
    
            
    PSP("Pagamento ad iniziativa PSP");
            
        
    

    private String value;

    ModelloEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static ModelloEnum fromValue(String text) {
      for (ModelloEnum b : ModelloEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("modello")
  private ModelloEnum modello = null;
  
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
  
  @JsonProperty("rpp")
  private String rpp = null;
  
  @JsonProperty("verificato")
  private Boolean verificato = null;
  
  @JsonProperty("note")
  private List<Nota> note = null;
  
  /**
   * Identificativo del pagamento assegnato da GovPay
   **/
  public PagamentoIndex id(String id) {
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
  public PagamentoIndex nome(String nome) {
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
  public PagamentoIndex dataRichiestaPagamento(Date dataRichiestaPagamento) {
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
  public PagamentoIndex idSessionePortale(String idSessionePortale) {
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
  public PagamentoIndex idSessionePsp(String idSessionePsp) {
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
  public PagamentoIndex importo(BigDecimal importo) {
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
  public PagamentoIndex stato(StatoPagamento stato) {
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
   * Modello di pagamento
   **/
  public PagamentoIndex modello(ModelloEnum modello) {
    this.modello = modello;
    return this;
  }

  @JsonProperty("modello")
  public ModelloEnum getModello() {
    return this.modello;
  }
  public void setModello(ModelloEnum modello) {
    this.modello = modello;
  }

  /**
   * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
   **/
  public PagamentoIndex pspRedirectUrl(String pspRedirectUrl) {
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
  public PagamentoIndex urlRitorno(String urlRitorno) {
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
  public PagamentoIndex contoAddebito(ContoAddebito contoAddebito) {
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
  public PagamentoIndex dataEsecuzionePagamento(Date dataEsecuzionePagamento) {
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
  public PagamentoIndex credenzialiPagatore(String credenzialiPagatore) {
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
  public PagamentoIndex soggettoVersante(Soggetto soggettoVersante) {
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
  public PagamentoIndex autenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
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
  public PagamentoIndex lingua(LinguaEnum lingua) {
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
   * Url per le richieste di pagamento oggetto del Pagamento
   **/
  public PagamentoIndex rpp(String rpp) {
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
   * indicazione se eventuali anomalie sono state verificate da un operatore
   **/
  public PagamentoIndex verificato(Boolean verificato) {
    this.verificato = verificato;
    return this;
  }

  @JsonProperty("verificato")
  public Boolean Verificato() {
    return this.verificato;
  }
  public void setVerificato(Boolean verificato) {
    this.verificato = verificato;
  }

  /**
   **/
  public PagamentoIndex note(List<Nota> note) {
    this.note = note;
    return this;
  }

  @JsonProperty("note")
  public List<Nota> getNote() {
    return this.note;
  }
  public void setNote(List<Nota> note) {
    this.note = note;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PagamentoIndex pagamentoIndex = (PagamentoIndex) o;
    return Objects.equals(this.id, pagamentoIndex.id) &&
        Objects.equals(this.nome, pagamentoIndex.nome) &&
        Objects.equals(this.dataRichiestaPagamento, pagamentoIndex.dataRichiestaPagamento) &&
        Objects.equals(this.idSessionePortale, pagamentoIndex.idSessionePortale) &&
        Objects.equals(this.idSessionePsp, pagamentoIndex.idSessionePsp) &&
        Objects.equals(this.importo, pagamentoIndex.importo) &&
        Objects.equals(this.stato, pagamentoIndex.stato) &&
        Objects.equals(this.modello, pagamentoIndex.modello) &&
        Objects.equals(this.pspRedirectUrl, pagamentoIndex.pspRedirectUrl) &&
        Objects.equals(this.urlRitorno, pagamentoIndex.urlRitorno) &&
        Objects.equals(this.contoAddebito, pagamentoIndex.contoAddebito) &&
        Objects.equals(this.dataEsecuzionePagamento, pagamentoIndex.dataEsecuzionePagamento) &&
        Objects.equals(this.credenzialiPagatore, pagamentoIndex.credenzialiPagatore) &&
        Objects.equals(this.soggettoVersante, pagamentoIndex.soggettoVersante) &&
        Objects.equals(this.autenticazioneSoggetto, pagamentoIndex.autenticazioneSoggetto) &&
        Objects.equals(this.lingua, pagamentoIndex.lingua) &&
        Objects.equals(this.rpp, pagamentoIndex.rpp) &&
        Objects.equals(this.verificato, pagamentoIndex.verificato) &&
        Objects.equals(this.note, pagamentoIndex.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.nome, this.dataRichiestaPagamento, this.idSessionePortale, this.idSessionePsp, this.importo, this.stato, this.modello, this.pspRedirectUrl, this.urlRitorno, this.contoAddebito, this.dataEsecuzionePagamento, this.credenzialiPagatore, this.soggettoVersante, this.autenticazioneSoggetto, this.lingua, this.rpp, this.verificato, this.note);
  }

  public static PagamentoIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PagamentoIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pagamentoIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoIndex {\n");
    
    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
    sb.append("    dataRichiestaPagamento: ").append(this.toIndentedString(this.dataRichiestaPagamento)).append("\n");
    sb.append("    idSessionePortale: ").append(this.toIndentedString(this.idSessionePortale)).append("\n");
    sb.append("    idSessionePsp: ").append(this.toIndentedString(this.idSessionePsp)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    modello: ").append(this.toIndentedString(this.modello)).append("\n");
    sb.append("    pspRedirectUrl: ").append(this.toIndentedString(this.pspRedirectUrl)).append("\n");
    sb.append("    urlRitorno: ").append(this.toIndentedString(this.urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(this.toIndentedString(this.contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(this.toIndentedString(this.dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(this.toIndentedString(this.credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(this.toIndentedString(this.soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(this.toIndentedString(this.autenticazioneSoggetto)).append("\n");
    sb.append("    lingua: ").append(this.toIndentedString(this.lingua)).append("\n");
    sb.append("    rpp: ").append(this.toIndentedString(this.rpp)).append("\n");
    sb.append("    verificato: ").append(this.toIndentedString(this.verificato)).append("\n");
    sb.append("    note: ").append(this.toIndentedString(this.note)).append("\n");
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



