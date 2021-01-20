package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"nome",
"dataRichiestaPagamento",
"idSessionePortale",
"idSessionePsp",
"importo",
"modello",
"stato",
"descrizioneStato",
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
"severita",
})
public class Pagamento extends it.govpay.core.beans.JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  @JsonProperty("dataRichiestaPagamento")
  private Date dataRichiestaPagamento = null;
  
  @JsonProperty("idSessionePortale")
  private String idSessionePortale = null;
  
  @JsonProperty("idSessionePsp")
  private String idSessionePsp = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
    
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
  
  @JsonProperty("stato")
  private StatoPagamento stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
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
    @JsonValue
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
  private List<Rpp> rpp = null;
  
  @JsonProperty("verificato")
  private Boolean verificato = null;
  
  @JsonProperty("severita")
  private Integer severita = null;
  
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
   * Modello di pagamento
   **/
  public Pagamento modello(ModelloEnum modello) {
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
   * Descrizione estesa dello stato del pagamento
   **/
  public Pagamento descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return this.descrizioneStato;
  }
  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
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
  public Pagamento rpp(List<Rpp> rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public List<Rpp> getRpp() {
    return this.rpp;
  }
  public void setRpp(List<Rpp> rpp) {
    this.rpp = rpp;
  }

  /**
   * indicazione se eventuali anomalie sono state verificate da un operatore
   **/
  public Pagamento verificato(Boolean verificato) {
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
   * indica il livello di severita dell'errore che ha portato il pagamento in stato FALLITO
   **/
  public Pagamento severita(Integer severita) {
    this.severita = severita;
    return this;
  }

  @JsonProperty("severita")
  public Integer getSeverita() {
    return severita;
  }
  public void setSeverita(Integer severita) {
    this.severita = severita;
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
    return Objects.equals(id, pagamento.id) &&
        Objects.equals(nome, pagamento.nome) &&
        Objects.equals(dataRichiestaPagamento, pagamento.dataRichiestaPagamento) &&
        Objects.equals(idSessionePortale, pagamento.idSessionePortale) &&
        Objects.equals(idSessionePsp, pagamento.idSessionePsp) &&
        Objects.equals(importo, pagamento.importo) &&
        Objects.equals(modello, pagamento.modello) &&
        Objects.equals(stato, pagamento.stato) &&
        Objects.equals(descrizioneStato, pagamento.descrizioneStato) &&
        Objects.equals(pspRedirectUrl, pagamento.pspRedirectUrl) &&
        Objects.equals(urlRitorno, pagamento.urlRitorno) &&
        Objects.equals(contoAddebito, pagamento.contoAddebito) &&
        Objects.equals(dataEsecuzionePagamento, pagamento.dataEsecuzionePagamento) &&
        Objects.equals(credenzialiPagatore, pagamento.credenzialiPagatore) &&
        Objects.equals(soggettoVersante, pagamento.soggettoVersante) &&
        Objects.equals(autenticazioneSoggetto, pagamento.autenticazioneSoggetto) &&
        Objects.equals(lingua, pagamento.lingua) &&
        Objects.equals(rpp, pagamento.rpp) &&
        Objects.equals(verificato, pagamento.verificato) &&
        Objects.equals(severita, pagamento.severita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, dataRichiestaPagamento, idSessionePortale, idSessionePsp, importo, modello, stato, descrizioneStato, pspRedirectUrl, urlRitorno, contoAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, lingua, rpp, verificato, severita);
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
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    dataRichiestaPagamento: ").append(toIndentedString(dataRichiestaPagamento)).append("\n");
    sb.append("    idSessionePortale: ").append(toIndentedString(idSessionePortale)).append("\n");
    sb.append("    idSessionePsp: ").append(toIndentedString(idSessionePsp)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    modello: ").append(toIndentedString(modello)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    pspRedirectUrl: ").append(toIndentedString(pspRedirectUrl)).append("\n");
    sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(toIndentedString(contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
    sb.append("    lingua: ").append(toIndentedString(lingua)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
    sb.append("    verificato: ").append(toIndentedString(verificato)).append("\n");
    sb.append("    severita: ").append(toIndentedString(severita)).append("\n");
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



