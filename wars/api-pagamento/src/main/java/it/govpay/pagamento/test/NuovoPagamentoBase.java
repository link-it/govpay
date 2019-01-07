package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Conto;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.TipoAutenticazioneSoggetto;
import org.joda.time.LocalDate;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NuovoPagamentoBase  {
  
  @Schema(example = "https://www.comune.it/pagamentopagopa/abcd-efgh-ijklm-12345", description = "url di ritorno al portale al termine della sessione di pagamento")
 /**
   * url di ritorno al portale al termine della sessione di pagamento  
  **/
  private String urlRitorno = null;
  
  @Schema(description = "")
  private Conto contoAddebito = null;
  
  @Schema(description = "data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.")
 /**
   * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.  
  **/
  private LocalDate dataEsecuzionePagamento = null;
  
  @Schema(description = "Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).")
 /**
   * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).  
  **/
  private String credenzialiPagatore = null;
  
  @Schema(description = "")
  private Soggetto soggettoVersante = null;
  
  @Schema(description = "")
  private TipoAutenticazioneSoggetto autenticazioneSoggetto = null;
 /**
   * url di ritorno al portale al termine della sessione di pagamento
   * @return urlRitorno
  **/
  @JsonProperty("urlRitorno")
  public String getUrlRitorno() {
    return urlRitorno;
  }

  public void setUrlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
  }

  public NuovoPagamentoBase urlRitorno(String urlRitorno) {
    this.urlRitorno = urlRitorno;
    return this;
  }

 /**
   * Get contoAddebito
   * @return contoAddebito
  **/
  @JsonProperty("contoAddebito")
  public Conto getContoAddebito() {
    return contoAddebito;
  }

  public void setContoAddebito(Conto contoAddebito) {
    this.contoAddebito = contoAddebito;
  }

  public NuovoPagamentoBase contoAddebito(Conto contoAddebito) {
    this.contoAddebito = contoAddebito;
    return this;
  }

 /**
   * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
   * @return dataEsecuzionePagamento
  **/
  @JsonProperty("dataEsecuzionePagamento")
  public LocalDate getDataEsecuzionePagamento() {
    return dataEsecuzionePagamento;
  }

  public void setDataEsecuzionePagamento(LocalDate dataEsecuzionePagamento) {
    this.dataEsecuzionePagamento = dataEsecuzionePagamento;
  }

  public NuovoPagamentoBase dataEsecuzionePagamento(LocalDate dataEsecuzionePagamento) {
    this.dataEsecuzionePagamento = dataEsecuzionePagamento;
    return this;
  }

 /**
   * Eventuali credenziali richieste dal PSP necessarie per completare l&#x27;operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
   * @return credenzialiPagatore
  **/
  @JsonProperty("credenzialiPagatore")
  public String getCredenzialiPagatore() {
    return credenzialiPagatore;
  }

  public void setCredenzialiPagatore(String credenzialiPagatore) {
    this.credenzialiPagatore = credenzialiPagatore;
  }

  public NuovoPagamentoBase credenzialiPagatore(String credenzialiPagatore) {
    this.credenzialiPagatore = credenzialiPagatore;
    return this;
  }

 /**
   * Get soggettoVersante
   * @return soggettoVersante
  **/
  @JsonProperty("soggettoVersante")
  public Soggetto getSoggettoVersante() {
    return soggettoVersante;
  }

  public void setSoggettoVersante(Soggetto soggettoVersante) {
    this.soggettoVersante = soggettoVersante;
  }

  public NuovoPagamentoBase soggettoVersante(Soggetto soggettoVersante) {
    this.soggettoVersante = soggettoVersante;
    return this;
  }

 /**
   * Get autenticazioneSoggetto
   * @return autenticazioneSoggetto
  **/
  @JsonProperty("autenticazioneSoggetto")
  public TipoAutenticazioneSoggetto getAutenticazioneSoggetto() {
    return autenticazioneSoggetto;
  }

  public void setAutenticazioneSoggetto(TipoAutenticazioneSoggetto autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  public NuovoPagamentoBase autenticazioneSoggetto(TipoAutenticazioneSoggetto autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoPagamentoBase {\n");
    
    sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
    sb.append("    contoAddebito: ").append(toIndentedString(contoAddebito)).append("\n");
    sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
    sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
    sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
    sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
