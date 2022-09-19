package it.govpay.netpay.v1.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.ec.v1.validator.SoggettoPagatoreValidator;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;

public class ActivePaymentItem   implements IValidable {
  
  @Schema(required = true, description = "Progressivo da 1 a 5 del singolo pagamento afferente allo IUV.")
 /**
   * Progressivo da 1 a 5 del singolo pagamento afferente allo IUV.  
  **/
  private BigDecimal paymentId = null;
  
  @Schema(example = "1000", required = true, description = "Importo da pagare espresso in centesimi di Euro")
 /**
   * Importo da pagare espresso in centesimi di Euro  
  **/
  private BigDecimal totAmount = null;
  
  @Schema(required = true, description = "Data di scadenza del pagamento")
 /**
   * Data di scadenza del pagamento  
  **/
  private Date dueDate = null;
  
  @Schema(example = "abcdef12345", required = true, description = "Identificativo unico della posizione debitoria nel sistema Net@Pay")
 /**
   * Identificativo unico della posizione debitoria nel sistema Net@Pay  
  **/
  private String invoiceID = null;
  
  @Schema(example = "Bolletta GAS", required = true, description = "Descrizione della causale, si tratta della parte descrittiva da inserire alla fine della causale al momento della creazione della RPT.")
 /**
   * Descrizione della causale, si tratta della parte descrittiva da inserire alla fine della causale al momento della creazione della RPT.  
  **/
  private String invoiceType = null;
  
  @Schema(example = "ALTRO/3321", required = true, description = "Indicazione dell'imputazione della specifica entrata nella forma\\: <tipo contabilità>”/”<codice contabilità>")
 /**
   * Indicazione dell'imputazione della specifica entrata nella forma\\: <tipo contabilità>”/”<codice contabilità>  
  **/
  private String additionalRemittanceInfo = null;
  
  @Schema(example = "IT60X0542811101000000123456", required = true, description = "IBAN di accredito dell'Ente Creditore")
 /**
   * IBAN di accredito dell'Ente Creditore  
  **/
  private String creditorIBAN = null;
  public enum ClientTypeEnum {
    G("G"),
    F("F");

    private String value;

    ClientTypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static ClientTypeEnum fromValue(String text) {
      for (ClientTypeEnum b : ClientTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }  
  @Schema(example = "F", required = true, description = "Tipologia Soggetto Debitore, se persona fisica (F) o giuridica (G).")
 /**
   * Tipologia Soggetto Debitore, se persona fisica (F) o giuridica (G).  
  **/
  private ClientTypeEnum clientType = null;
  
  @Schema(example = "Mario Rossi", required = true, description = "Anagrafica del Soggetto Debitore (Indica il nominativo o la ragione sociale del pagatore).")
 /**
   * Anagrafica del Soggetto Debitore (Indica il nominativo o la ragione sociale del pagatore).  
  **/
  private String clientDescription = null;
  
  @Schema(example = "RSSMRA30A01H501I", required = true, description = "Codice fiscale o Partita IVA del Soggetto Debitore.")
 /**
   * Codice fiscale o Partita IVA del Soggetto Debitore.  
  **/
  private String clientFiscalID = null;
 /**
   * Progressivo da 1 a 5 del singolo pagamento afferente allo IUV.
   * minimum: 1
   * maximum: 5
   * @return paymentId
  **/
  @JsonProperty("paymentId")
  @NotNull
 @DecimalMin("1") @DecimalMax("5")  public BigDecimal getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(BigDecimal paymentId) {
    this.paymentId = paymentId;
  }

  public ActivePaymentItem paymentId(BigDecimal paymentId) {
    this.paymentId = paymentId;
    return this;
  }

 /**
   * Importo da pagare espresso in centesimi di Euro
   * @return totAmount
  **/
  @JsonProperty("totAmount")
  @NotNull
  public BigDecimal getTotAmount() {
    return totAmount;
  }

  public void setTotAmount(BigDecimal totAmount) {
    this.totAmount = totAmount;
  }

  public ActivePaymentItem totAmount(BigDecimal totAmount) {
    this.totAmount = totAmount;
    return this;
  }

 /**
   * Data di scadenza del pagamento
   * @return dueDate
  **/
  @JsonProperty("dueDate")
  @NotNull
  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public ActivePaymentItem dueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

 /**
   * Identificativo unico della posizione debitoria nel sistema Net@Pay
   * @return invoiceID
  **/
  @JsonProperty("invoiceID")
  @NotNull
 @Size(max=50)  public String getInvoiceID() {
    return invoiceID;
  }

  public void setInvoiceID(String invoiceID) {
    this.invoiceID = invoiceID;
  }

  public ActivePaymentItem invoiceID(String invoiceID) {
    this.invoiceID = invoiceID;
    return this;
  }

 /**
   * Descrizione della causale, si tratta della parte descrittiva da inserire alla fine della causale al momento della creazione della RPT.
   * @return invoiceType
  **/
  @JsonProperty("invoiceType")
  @NotNull
 @Size(max=50)  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public ActivePaymentItem invoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
    return this;
  }

 /**
   * Indicazione dell&#x27;imputazione della specifica entrata nella forma\\: &lt;tipo contabilità&gt;”/”&lt;codice contabilità&gt;
   * @return additionalRemittanceInfo
  **/
  @JsonProperty("additionalRemittanceInfo")
  @NotNull
 @Size(max=140)  public String getAdditionalRemittanceInfo() {
    return additionalRemittanceInfo;
  }

  public void setAdditionalRemittanceInfo(String additionalRemittanceInfo) {
    this.additionalRemittanceInfo = additionalRemittanceInfo;
  }

  public ActivePaymentItem additionalRemittanceInfo(String additionalRemittanceInfo) {
    this.additionalRemittanceInfo = additionalRemittanceInfo;
    return this;
  }

 /**
   * IBAN di accredito dell&#x27;Ente Creditore
   * @return creditorIBAN
  **/
  @JsonProperty("creditorIBAN")
  @NotNull
 @Size(max=35)  public String getCreditorIBAN() {
    return creditorIBAN;
  }

  public void setCreditorIBAN(String creditorIBAN) {
    this.creditorIBAN = creditorIBAN;
  }

  public ActivePaymentItem creditorIBAN(String creditorIBAN) {
    this.creditorIBAN = creditorIBAN;
    return this;
  }

 /**
   * Tipologia Soggetto Debitore, se persona fisica (F) o giuridica (G).
   * @return clientType
  **/
  @JsonProperty("clientType")
  @NotNull
 @Size(max=1)  public String getClientType() {
    if (clientType == null) {
      return null;
    }
    return clientType.getValue();
  }

  public void setClientType(ClientTypeEnum clientType) {
    this.clientType = clientType;
  }

  public ActivePaymentItem clientType(ClientTypeEnum clientType) {
    this.clientType = clientType;
    return this;
  }

 /**
   * Anagrafica del Soggetto Debitore (Indica il nominativo o la ragione sociale del pagatore).
   * @return clientDescription
  **/
  @JsonProperty("clientDescription")
  @NotNull
 @Size(max=70)  public String getClientDescription() {
    return clientDescription;
  }

  public void setClientDescription(String clientDescription) {
    this.clientDescription = clientDescription;
  }

  public ActivePaymentItem clientDescription(String clientDescription) {
    this.clientDescription = clientDescription;
    return this;
  }

 /**
   * Codice fiscale o Partita IVA del Soggetto Debitore.
   * @return clientFiscalID
  **/
  @JsonProperty("clientFiscalID")
  @NotNull
 @Size(max=35)  public String getClientFiscalID() {
    return clientFiscalID;
  }

  public void setClientFiscalID(String clientFiscalID) {
    this.clientFiscalID = clientFiscalID;
  }

  public ActivePaymentItem clientFiscalID(String clientFiscalID) {
    this.clientFiscalID = clientFiscalID;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivePaymentItem {\n");
    
    sb.append("    paymentId: ").append(toIndentedString(paymentId)).append("\n");
    sb.append("    totAmount: ").append(toIndentedString(totAmount)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    invoiceID: ").append(toIndentedString(invoiceID)).append("\n");
    sb.append("    invoiceType: ").append(toIndentedString(invoiceType)).append("\n");
    sb.append("    additionalRemittanceInfo: ").append(toIndentedString(additionalRemittanceInfo)).append("\n");
    sb.append("    creditorIBAN: ").append(toIndentedString(creditorIBAN)).append("\n");
    sb.append("    clientType: ").append(toIndentedString(clientType)).append("\n");
    sb.append("    clientDescription: ").append(toIndentedString(clientDescription)).append("\n");
    sb.append("    clientFiscalID: ").append(toIndentedString(clientFiscalID)).append("\n");
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
  
  @Override
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
	
	vf.getValidator("paymentId", this.paymentId).notNull();
	vf.getValidator("totAmount", this.totAmount).notNull();
	vf.getValidator("dueDate", this.dueDate).notNull();
	vf.getValidator("invoiceID", this.invoiceID).notNull();
	validatoreId.validaIdPendenza("invoiceID", this.invoiceID);
	vf.getValidator("invoiceType", this.invoiceType).notNull();
	ValidatoreUtils.validaCausale(vf, "invoiceType", invoiceType);
	vf.getValidator("additionalRemittanceInfo", this.additionalRemittanceInfo).notNull();
	String[] split = this.additionalRemittanceInfo.split("/");
	
	ValidatoreUtils.validaTipoContabilita(vf, "tipoContabilita", split[0]);
	ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", split[1]);
	
	vf.getValidator("creditorIBAN", this.creditorIBAN).notNull();
	validatoreId.validaIdIbanAccredito("creditorIBAN", this.creditorIBAN);
	
	SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
	
	soggettoPagatoreValidator.validaTipo("clientType", this.clientType != null ? this.clientType.toString() : null);
	soggettoPagatoreValidator.validaIdentificativo("clientFiscalID", this.clientFiscalID);
	soggettoPagatoreValidator.validaAnagrafica("clientDescription", this.clientDescription);
  }
}
