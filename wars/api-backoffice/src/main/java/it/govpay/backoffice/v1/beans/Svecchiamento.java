package it.govpay.backoffice.v1.beans;

import java.math.BigInteger;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Per ogni categoria di oggetti presenti nel db di Govpay e indicati in questo oggetto si puo&#x27; indicare la soglia (in numero di giorni) oltre la quale gli oggetti della categoria verranno eliminati. Impostando il valore a &#x27;null&#x27; gli oggetti della categoria non verranno eliminati.
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"stampeAvvisi",
"stampeRicevute",
"tracciatiPendenzeScartati",
"tracciatiPendenzeCompletati",
"pendenzeScadute",
"pendenzePagate",
"pendenzeAnnullate",
"pendenzeDaPagareSenzaScadenza",
"pagamentiEseguiti",
"pagamentiNonEseguiti",
"pagamentiFalliti",
"rendicontazioni",
"eventi",
"notificheConsegnate",
"notificheNonConsegnate",
})
public class Svecchiamento extends JSONSerializable implements IValidable{
  
  @JsonProperty("stampeAvvisi")
  private Integer stampeAvvisi = null;
  
  @JsonProperty("stampeRicevute")
  private Integer stampeRicevute = null;
  
  @JsonProperty("tracciatiPendenzeScartati")
  private Integer tracciatiPendenzeScartati = null;
  
  @JsonProperty("tracciatiPendenzeCompletati")
  private Integer tracciatiPendenzeCompletati = null;
  
  @JsonProperty("pendenzeScadute")
  private Integer pendenzeScadute = null;
  
  @JsonProperty("pendenzePagate")
  private Integer pendenzePagate = null;
  
  @JsonProperty("pendenzeAnnullate")
  private Integer pendenzeAnnullate = null;
  
  @JsonProperty("pendenzeDaPagareSenzaScadenza")
  private Integer pendenzeDaPagareSenzaScadenza = null;
  
  @JsonProperty("pagamentiEseguiti")
  private Integer pagamentiEseguiti = null;
  
  @JsonProperty("pagamentiNonEseguiti")
  private Integer pagamentiNonEseguiti = null;
  
  @JsonProperty("pagamentiFalliti")
  private Integer pagamentiFalliti = null;
  
  @JsonProperty("rendicontazioni")
  private Integer rendicontazioni = null;
  
  @JsonProperty("eventi")
  private Integer eventi = null;
  
  @JsonProperty("notificheConsegnate")
  private Integer notificheConsegnate = null;
  
  @JsonProperty("notificheNonConsegnate")
  private Integer notificheNonConsegnate = null;
  
  /**
   * numero di giorni di conservazione delle stampe avviso di pagamento
   * minimum: 1
   **/
  public Svecchiamento stampeAvvisi(Integer stampeAvvisi) {
    this.stampeAvvisi = stampeAvvisi;
    return this;
  }

  @JsonProperty("stampeAvvisi")
  public Integer getStampeAvvisi() {
    return stampeAvvisi;
  }
  public void setStampeAvvisi(Integer stampeAvvisi) {
    this.stampeAvvisi = stampeAvvisi;
  }

  /**
   * numero di giorni di conservazione delle stampe ricevuta di pagamento
   * minimum: 1
   **/
  public Svecchiamento stampeRicevute(Integer stampeRicevute) {
    this.stampeRicevute = stampeRicevute;
    return this;
  }

  @JsonProperty("stampeRicevute")
  public Integer getStampeRicevute() {
    return stampeRicevute;
  }
  public void setStampeRicevute(Integer stampeRicevute) {
    this.stampeRicevute = stampeRicevute;
  }

  /**
   * numero di giorni di conservazione dei tracciati scartati
   * minimum: 1
   **/
  public Svecchiamento tracciatiPendenzeScartati(Integer tracciatiPendenzeScartati) {
    this.tracciatiPendenzeScartati = tracciatiPendenzeScartati;
    return this;
  }

  @JsonProperty("tracciatiPendenzeScartati")
  public Integer getTracciatiPendenzeScartati() {
    return tracciatiPendenzeScartati;
  }
  public void setTracciatiPendenzeScartati(Integer tracciatiPendenzeScartati) {
    this.tracciatiPendenzeScartati = tracciatiPendenzeScartati;
  }

  /**
   * numero di giorni di conservazione dei tracciati completati
   * minimum: 1
   **/
  public Svecchiamento tracciatiPendenzeCompletati(Integer tracciatiPendenzeCompletati) {
    this.tracciatiPendenzeCompletati = tracciatiPendenzeCompletati;
    return this;
  }

  @JsonProperty("tracciatiPendenzeCompletati")
  public Integer getTracciatiPendenzeCompletati() {
    return tracciatiPendenzeCompletati;
  }
  public void setTracciatiPendenzeCompletati(Integer tracciatiPendenzeCompletati) {
    this.tracciatiPendenzeCompletati = tracciatiPendenzeCompletati;
  }

  /**
   * numero di giorni di conservazione delle pendenze scadute
   * minimum: 30
   **/
  public Svecchiamento pendenzeScadute(Integer pendenzeScadute) {
    this.pendenzeScadute = pendenzeScadute;
    return this;
  }

  @JsonProperty("pendenzeScadute")
  public Integer getPendenzeScadute() {
    return pendenzeScadute;
  }
  public void setPendenzeScadute(Integer pendenzeScadute) {
    this.pendenzeScadute = pendenzeScadute;
  }

  /**
   * numero di giorni di conservazione delle pendenze pagate
   * minimum: 365
   **/
  public Svecchiamento pendenzePagate(Integer pendenzePagate) {
    this.pendenzePagate = pendenzePagate;
    return this;
  }

  @JsonProperty("pendenzePagate")
  public Integer getPendenzePagate() {
    return pendenzePagate;
  }
  public void setPendenzePagate(Integer pendenzePagate) {
    this.pendenzePagate = pendenzePagate;
  }

  /**
   * numero di giorni di conservazione delle pendenze annullate
   * minimum: 30
   **/
  public Svecchiamento pendenzeAnnullate(Integer pendenzeAnnullate) {
    this.pendenzeAnnullate = pendenzeAnnullate;
    return this;
  }

  @JsonProperty("pendenzeAnnullate")
  public Integer getPendenzeAnnullate() {
    return pendenzeAnnullate;
  }
  public void setPendenzeAnnullate(Integer pendenzeAnnullate) {
    this.pendenzeAnnullate = pendenzeAnnullate;
  }

  /**
   * numero di giorni di conservazione delle pendenze da pagare che non hanno una scadenza
   * minimum: 1825
   **/
  public Svecchiamento pendenzeDaPagareSenzaScadenza(Integer pendenzeDaPagareSenzaScadenza) {
    this.pendenzeDaPagareSenzaScadenza = pendenzeDaPagareSenzaScadenza;
    return this;
  }

  @JsonProperty("pendenzeDaPagareSenzaScadenza")
  public Integer getPendenzeDaPagareSenzaScadenza() {
    return pendenzeDaPagareSenzaScadenza;
  }
  public void setPendenzeDaPagareSenzaScadenza(Integer pendenzeDaPagareSenzaScadenza) {
    this.pendenzeDaPagareSenzaScadenza = pendenzeDaPagareSenzaScadenza;
  }

  /**
   * numero di giorni di conservazione dei pagamenti eseguiti
   * minimum: 365
   **/
  public Svecchiamento pagamentiEseguiti(Integer pagamentiEseguiti) {
    this.pagamentiEseguiti = pagamentiEseguiti;
    return this;
  }

  @JsonProperty("pagamentiEseguiti")
  public Integer getPagamentiEseguiti() {
    return pagamentiEseguiti;
  }
  public void setPagamentiEseguiti(Integer pagamentiEseguiti) {
    this.pagamentiEseguiti = pagamentiEseguiti;
  }

  /**
   * numero di giorni di conservazione dei pagamenti non eseguiti
   * minimum: 30
   **/
  public Svecchiamento pagamentiNonEseguiti(Integer pagamentiNonEseguiti) {
    this.pagamentiNonEseguiti = pagamentiNonEseguiti;
    return this;
  }

  @JsonProperty("pagamentiNonEseguiti")
  public Integer getPagamentiNonEseguiti() {
    return pagamentiNonEseguiti;
  }
  public void setPagamentiNonEseguiti(Integer pagamentiNonEseguiti) {
    this.pagamentiNonEseguiti = pagamentiNonEseguiti;
  }

  /**
   * numero di giorni di conservazione dei pagamenti falliti
   * minimum: 7
   **/
  public Svecchiamento pagamentiFalliti(Integer pagamentiFalliti) {
    this.pagamentiFalliti = pagamentiFalliti;
    return this;
  }

  @JsonProperty("pagamentiFalliti")
  public Integer getPagamentiFalliti() {
    return pagamentiFalliti;
  }
  public void setPagamentiFalliti(Integer pagamentiFalliti) {
    this.pagamentiFalliti = pagamentiFalliti;
  }

  /**
   * numero di giorni di conservazione dei flussi di rendicontazione
   * minimum: 730
   **/
  public Svecchiamento rendicontazioni(Integer rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
    return this;
  }

  @JsonProperty("rendicontazioni")
  public Integer getRendicontazioni() {
    return rendicontazioni;
  }
  public void setRendicontazioni(Integer rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
  }

  /**
   * numero di giorni di conservazione degli eventi
   * minimum: 365
   **/
  public Svecchiamento eventi(Integer eventi) {
    this.eventi = eventi;
    return this;
  }

  @JsonProperty("eventi")
  public Integer getEventi() {
    return eventi;
  }
  public void setEventi(Integer eventi) {
    this.eventi = eventi;
  }

  /**
   * numero di giorni di conservazione delle notifiche consegnate
   * minimum: 7
   **/
  public Svecchiamento notificheConsegnate(Integer notificheConsegnate) {
    this.notificheConsegnate = notificheConsegnate;
    return this;
  }

  @JsonProperty("notificheConsegnate")
  public Integer getNotificheConsegnate() {
    return notificheConsegnate;
  }
  public void setNotificheConsegnate(Integer notificheConsegnate) {
    this.notificheConsegnate = notificheConsegnate;
  }

  /**
   * numero di giorni di conservazione delle notifiche non consegnate
   * minimum: 180
   **/
  public Svecchiamento notificheNonConsegnate(Integer notificheNonConsegnate) {
    this.notificheNonConsegnate = notificheNonConsegnate;
    return this;
  }

  @JsonProperty("notificheNonConsegnate")
  public Integer getNotificheNonConsegnate() {
    return notificheNonConsegnate;
  }
  public void setNotificheNonConsegnate(Integer notificheNonConsegnate) {
    this.notificheNonConsegnate = notificheNonConsegnate;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Svecchiamento svecchiamento = (Svecchiamento) o;
    return Objects.equals(stampeAvvisi, svecchiamento.stampeAvvisi) &&
        Objects.equals(stampeRicevute, svecchiamento.stampeRicevute) &&
        Objects.equals(tracciatiPendenzeScartati, svecchiamento.tracciatiPendenzeScartati) &&
        Objects.equals(tracciatiPendenzeCompletati, svecchiamento.tracciatiPendenzeCompletati) &&
        Objects.equals(pendenzeScadute, svecchiamento.pendenzeScadute) &&
        Objects.equals(pendenzePagate, svecchiamento.pendenzePagate) &&
        Objects.equals(pendenzeAnnullate, svecchiamento.pendenzeAnnullate) &&
        Objects.equals(pendenzeDaPagareSenzaScadenza, svecchiamento.pendenzeDaPagareSenzaScadenza) &&
        Objects.equals(pagamentiEseguiti, svecchiamento.pagamentiEseguiti) &&
        Objects.equals(pagamentiNonEseguiti, svecchiamento.pagamentiNonEseguiti) &&
        Objects.equals(pagamentiFalliti, svecchiamento.pagamentiFalliti) &&
        Objects.equals(rendicontazioni, svecchiamento.rendicontazioni) &&
        Objects.equals(eventi, svecchiamento.eventi) &&
        Objects.equals(notificheConsegnate, svecchiamento.notificheConsegnate) &&
        Objects.equals(notificheNonConsegnate, svecchiamento.notificheNonConsegnate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stampeAvvisi, stampeRicevute, tracciatiPendenzeScartati, tracciatiPendenzeCompletati, pendenzeScadute, pendenzePagate, pendenzeAnnullate, pendenzeDaPagareSenzaScadenza, pagamentiEseguiti, pagamentiNonEseguiti, pagamentiFalliti, rendicontazioni, eventi, notificheConsegnate, notificheNonConsegnate);
  }

  public static Svecchiamento parse(String json) throws ServiceException, ValidationException {
    return (Svecchiamento) parse(json, Svecchiamento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "svecchiamento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Svecchiamento {\n");
    
    sb.append("    stampeAvvisi: ").append(toIndentedString(stampeAvvisi)).append("\n");
    sb.append("    stampeRicevute: ").append(toIndentedString(stampeRicevute)).append("\n");
    sb.append("    tracciatiPendenzeScartati: ").append(toIndentedString(tracciatiPendenzeScartati)).append("\n");
    sb.append("    tracciatiPendenzeCompletati: ").append(toIndentedString(tracciatiPendenzeCompletati)).append("\n");
    sb.append("    pendenzeScadute: ").append(toIndentedString(pendenzeScadute)).append("\n");
    sb.append("    pendenzePagate: ").append(toIndentedString(pendenzePagate)).append("\n");
    sb.append("    pendenzeAnnullate: ").append(toIndentedString(pendenzeAnnullate)).append("\n");
    sb.append("    pendenzeDaPagareSenzaScadenza: ").append(toIndentedString(pendenzeDaPagareSenzaScadenza)).append("\n");
    sb.append("    pagamentiEseguiti: ").append(toIndentedString(pagamentiEseguiti)).append("\n");
    sb.append("    pagamentiNonEseguiti: ").append(toIndentedString(pagamentiNonEseguiti)).append("\n");
    sb.append("    pagamentiFalliti: ").append(toIndentedString(pagamentiFalliti)).append("\n");
    sb.append("    rendicontazioni: ").append(toIndentedString(rendicontazioni)).append("\n");
    sb.append("    eventi: ").append(toIndentedString(eventi)).append("\n");
    sb.append("    notificheConsegnate: ").append(toIndentedString(notificheConsegnate)).append("\n");
    sb.append("    notificheNonConsegnate: ").append(toIndentedString(notificheNonConsegnate)).append("\n");
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
	
	vf.getValidator("stampeAvvisi", this.stampeAvvisi).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoAvvisiPagamentoValoreMinimo()));
	vf.getValidator("stampeRicevute", this.stampeRicevute).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoRicevutePagamentoValoreMinimo()));
	vf.getValidator("tracciatiPendenzeScartati", this.tracciatiPendenzeScartati).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoTracciatiPendenzeScartatiValoreMinimo()));
	vf.getValidator("tracciatiPendenzeCompletati", this.tracciatiPendenzeCompletati).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoTracciatiPendenzeCompletatiValoreMinimo()));
	vf.getValidator("pendenzeScadute", this.pendenzeScadute).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPendenzeScaduteValoreMinimo()));
	vf.getValidator("pendenzePagate", this.pendenzePagate).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPendenzePagateValoreMinimo()));
	vf.getValidator("pendenzeAnnullate", this.pendenzeAnnullate).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPendenzeAnnullateValoreMinimo()));
	vf.getValidator("pendenzeDaPagareSenzaScadenza", this.pendenzeDaPagareSenzaScadenza).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPendenzeDaPagareValoreMinimo()));
	vf.getValidator("pagamentiEseguiti", this.pagamentiEseguiti).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPagamentiEseguitiValoreMinimo()));
	vf.getValidator("pagamentiNonEseguiti", this.pagamentiNonEseguiti).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPagamentiNonEseguitiValoreMinimo()));
	vf.getValidator("pagamentiFalliti", this.pagamentiFalliti).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoPagamentiFallitiValoreMinimo()));
	vf.getValidator("rendicontazioni", this.rendicontazioni).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoRendicontazioniValoreMinimo()));
	vf.getValidator("eventi", this.eventi).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoEventiValoreMinimo()));
	vf.getValidator("notificheConsegnate", this.notificheConsegnate).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoNotificheConsegnateValoreMinimo()));
	vf.getValidator("notificheNonConsegnate", this.notificheNonConsegnate).min(BigInteger.valueOf(GovpayConfig.getInstance().getBatchSvecchiamentoNotificheNonConsegnateValoreMinimo()));
	
  }
}



