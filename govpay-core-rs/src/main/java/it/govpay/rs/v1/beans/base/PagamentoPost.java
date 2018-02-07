package it.govpay.rs.v1.beans.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import it.govpay.rs.v1.beans.DatiAddebito;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"urlRitorno",
"pendenze",
"tokenWISP",
"datiAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"lingua",
})
public class PagamentoPost extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("urlRitorno")
  private String urlRitorno = null;
  
  @JsonProperty("pendenze")
  private List<Object> pendenze = new ArrayList<Object>();
  
  @JsonProperty("tokenWISP")
  private TokenWISP tokenWISP = null;
  
  @JsonProperty("datiAddebito")
  private DatiAddebito datiAddebito = null;
  
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
    @JsonValue
    public String toString() {
      return String.valueOf(value);
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
  
  /**
   * url di ritorno al portale al termine della sessione di pagamento
   **/
  public PagamentoPost urlRitorno(String urlRitorno) {
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
   * pendenze o riferimenti alle pendenze oggetto del pagamento
   **/
  public PagamentoPost pendenze(List<Object> pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  @JsonProperty("pendenze")
  public List<Object> getPendenze() {
    return pendenze;
  }
  public void setPendenze(List<Object> pendenze) {
    this.pendenze = pendenze;
  }

  /**
   * Identificativo della scelta effettuata dal WISP se, al momento del pagamento, il versante ha gia' eseguito  la scelta del PSP sul WISP.
   **/
  public PagamentoPost tokenWISP(TokenWISP tokenWISP) {
    this.tokenWISP = tokenWISP;
    return this;
  }

  @JsonProperty("tokenWISP")
  public TokenWISP getTokenWISP() {
    return tokenWISP;
  }
  public void setTokenWISP(TokenWISP tokenWISP) {
    this.tokenWISP = tokenWISP;
  }

  /**
   * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
   **/
  public PagamentoPost datiAddebito(DatiAddebito datiAddebito) {
    this.datiAddebito = datiAddebito;
    return this;
  }

  @JsonProperty("datiAddebito")
  public DatiAddebito getDatiAddebito() {
    return datiAddebito;
  }
  public void setDatiAddebito(DatiAddebito datiAddebito) {
    this.datiAddebito = datiAddebito;
  }

  /**
   * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
   **/
  public PagamentoPost dataEsecuzionePagamento(Date dataEsecuzionePagamento) {
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
  public PagamentoPost credenzialiPagatore(String credenzialiPagatore) {
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
  public PagamentoPost soggettoVersante(Soggetto soggettoVersante) {
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
  public PagamentoPost autenticazioneSoggettoEnum(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
    return this;
  }

  public AutenticazioneSoggettoEnum getAutenticazioneSoggettoEnum() {
    return autenticazioneSoggetto;
  }
  public void setAutenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  /**
   * modalita' di autenticazione del soggetto versante
   **/
  public PagamentoPost autenticazioneSoggetto(String autenticazioneSoggetto) throws Exception{
    this.setAutenticazioneSoggetto(autenticazioneSoggetto);
    return this;
  }

  @JsonProperty("autenticazioneSoggetto")
  public String getAutenticazioneSoggetto() {
	  if(autenticazioneSoggetto != null) {
		  return autenticazioneSoggetto.value;
	  } else {
		  return null;
	  }
    
  }
  public void setAutenticazioneSoggetto(String autenticazioneSoggetto) throws Exception{
	  if(autenticazioneSoggetto != null) {
		  this.autenticazioneSoggetto = AutenticazioneSoggettoEnum.fromValue(autenticazioneSoggetto);
		  if(this.autenticazioneSoggetto == null)
			  throw new Exception("valore ["+autenticazioneSoggetto+"] non ammesso per la property autenticazioneSoggetto");
	  }
  }

  /**
   * Indica il codice della lingua da utilizzare per l’esposizione delle pagine web.
   **/
  public PagamentoPost lingua(LinguaEnum lingua) {
    this.lingua = lingua;
    return this;
  }

  @JsonProperty("lingua")
  public LinguaEnum getLingua() {
    return lingua;
  }
  public void setLingua(LinguaEnum lingua) {
    this.lingua = lingua;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagamentoPost pagamentoPost = (PagamentoPost) o;
    return Objects.equals(urlRitorno, pagamentoPost.urlRitorno) &&
        Objects.equals(pendenze, pagamentoPost.pendenze) &&
        Objects.equals(tokenWISP, pagamentoPost.tokenWISP) &&
        Objects.equals(datiAddebito, pagamentoPost.datiAddebito) &&
        Objects.equals(dataEsecuzionePagamento, pagamentoPost.dataEsecuzionePagamento) &&
        Objects.equals(credenzialiPagatore, pagamentoPost.credenzialiPagatore) &&
        Objects.equals(soggettoVersante, pagamentoPost.soggettoVersante) &&
        Objects.equals(autenticazioneSoggetto, pagamentoPost.autenticazioneSoggetto) &&
        Objects.equals(lingua, pagamentoPost.lingua);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urlRitorno, pendenze, tokenWISP, datiAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, lingua);
  }

  public static PagamentoPost parse(String json) {
    return (PagamentoPost) parse(json, PagamentoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pagamentoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoPost {\n");
    
    sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
    sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
    sb.append("    tokenWISP: ").append(toIndentedString(tokenWISP)).append("\n");
    sb.append("    datiAddebito: ").append(toIndentedString(datiAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
    sb.append("    lingua: ").append(toIndentedString(lingua)).append("\n");
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



