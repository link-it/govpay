package it.govpay.pagamento.v2.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"urlRitorno",
"contoAddebito",
"dataEsecuzionePagamento",
"credenzialiPagatore",
"soggettoVersante",
"autenticazioneSoggetto",
"pendenze",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class NuovoPagamento extends JSONSerializable implements IValidable {
  
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
  private TipoAutenticazioneSoggetto autenticazioneSoggetto = TipoAutenticazioneSoggetto.N_A;
  
  @JsonProperty("pendenze")
  private List<NuovaPendenza> pendenze = new ArrayList<>();
  
  /**
   * url di ritorno al portale al termine della sessione di pagamento
   **/
  public NuovoPagamento urlRitorno(String urlRitorno) {
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
  public NuovoPagamento contoAddebito(Conto contoAddebito) {
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
  public NuovoPagamento dataEsecuzionePagamento(Date dataEsecuzionePagamento) {
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
  public NuovoPagamento credenzialiPagatore(String credenzialiPagatore) {
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
  public NuovoPagamento soggettoVersante(Soggetto soggettoVersante) {
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
  public NuovoPagamento autenticazioneSoggetto(TipoAutenticazioneSoggetto autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
    return this;
  }

  @JsonProperty("autenticazioneSoggetto")
  public String getAutenticazioneSoggetto() {
    if(this.autenticazioneSoggetto != null) {
      return this.autenticazioneSoggetto.toString();
    } else {
      return null;
    }
  }
  
  public void setAutenticazioneSoggetto(String autenticazioneSoggetto) throws ValidationException{
    if(autenticazioneSoggetto != null) {
      this.autenticazioneSoggetto = TipoAutenticazioneSoggetto.fromValue(autenticazioneSoggetto);
      if(this.autenticazioneSoggetto == null)
        throw new ValidationException("valore ["+autenticazioneSoggetto+"] non ammesso per la property autenticazioneSoggetto");
    }
  }
  
  /**
   * pendenze o riferimenti alle pendenze oggetto del pagamento
   **/
  public NuovoPagamento pendenze(List<NuovaPendenza> pendenze) {
    this.pendenze = pendenze;
    return this;
  }

  @JsonProperty("pendenze")
  public List<NuovaPendenza> getPendenze() {
    return pendenze;
  }
  public void setPendenze(List<NuovaPendenza> pendenze) {
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
    NuovoPagamento nuovoPagamento = (NuovoPagamento) o;
    return Objects.equals(urlRitorno, nuovoPagamento.urlRitorno) &&
        Objects.equals(contoAddebito, nuovoPagamento.contoAddebito) &&
        Objects.equals(dataEsecuzionePagamento, nuovoPagamento.dataEsecuzionePagamento) &&
        Objects.equals(credenzialiPagatore, nuovoPagamento.credenzialiPagatore) &&
        Objects.equals(soggettoVersante, nuovoPagamento.soggettoVersante) &&
        Objects.equals(autenticazioneSoggetto, nuovoPagamento.autenticazioneSoggetto) &&
        Objects.equals(pendenze, nuovoPagamento.pendenze);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urlRitorno, contoAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, pendenze);
  }

  public static NuovoPagamento parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
    return parse(json, NuovoPagamento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovoPagamento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoPagamento {\n");
    
    sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(toIndentedString(contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
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

	@Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("pendenze", this.pendenze).notNull().minItems(1).maxItems(5).validateObjects();
		vf.getValidator("urlRitorno", this.urlRitorno).pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		vf.getValidator("contoAddebito", this.contoAddebito).validateFields();
		//vf.getValidator("dataEsecuzionePagamento", this.dataEsecuzionePagamento).after(LocalDate.now()).insideDays(30);
		vf.getValidator("credenzialiPagatore", this.credenzialiPagatore).minLength(1).maxLength(35);
		vf.getValidator("soggettoVersante", this.soggettoVersante).validateFields();
		vf.getValidator("autenticazioneSoggetto", this.autenticazioneSoggetto).notNull();
	}
}



