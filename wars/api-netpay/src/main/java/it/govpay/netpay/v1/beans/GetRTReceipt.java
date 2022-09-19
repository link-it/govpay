package it.govpay.netpay.v1.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetRTReceipt   {
  
  @Schema(required = true, description = "Ricevuta telematica in formato XML")
 /**
   * Ricevuta telematica in formato XML  
  **/
  private String receiptXML = null;
  
  @Schema(description = "Ricevuta telematica in formato PDF")
 /**
   * Ricevuta telematica in formato PDF  
  **/
  private String receiptPDF = null;
  
  @Schema(description = "Identificativo del PSP")
 /**
   * Identificativo del PSP  
  **/
  private String pspId = null;
  
  @Schema(description = "Identificativo del canale del PSP")
 /**
   * Identificativo del canale del PSP  
  **/
  private String channelId = null;
  
  @Schema(description = "")
  private PaymentType paymentType = null;
  
  @Schema(description = "Descrizione della pendenza, che viene visualizzata nella sezione causale dell'avviso di pagamento")
 /**
   * Descrizione della pendenza, che viene visualizzata nella sezione causale dell'avviso di pagamento  
  **/
  private String paymentCausal = null;
  
  @Schema(description = "Identificativo unico della RT")
 /**
   * Identificativo unico della RT  
  **/
  private String idRT = null;
  
  @Schema(description = "")
  private PaymentResultCode paymentResultCode = null;
  
  @Schema(example = "1000", description = "Valore effettivamente pagato espresso in centesimi di Euro")
 /**
   * Valore effettivamente pagato espresso in centesimi di Euro  
  **/
  private BigDecimal amountPaid = null;
  
  @Schema(description = "Codice identificativo del contesto di pagamento")
 /**
   * Codice identificativo del contesto di pagamento  
  **/
  private String paymentContextCode = null;
  
  @Schema(description = "Data di creazione dell'RT da parte del PSP")
 /**
   * Data di creazione dell'RT da parte del PSP  
  **/
  private Date dateRT = null;
 /**
   * Ricevuta telematica in formato XML
   * @return receiptXML
  **/
  @JsonProperty("receiptXML")
  @NotNull
  public String getReceiptXML() {
    return receiptXML;
  }

  public void setReceiptXML(String receiptXML) {
    this.receiptXML = receiptXML;
  }

  public GetRTReceipt receiptXML(String receiptXML) {
    this.receiptXML = receiptXML;
    return this;
  }

 /**
   * Ricevuta telematica in formato PDF
   * @return receiptPDF
  **/
  @JsonProperty("receiptPDF")
  public String getReceiptPDF() {
    return receiptPDF;
  }

  public void setReceiptPDF(String receiptPDF) {
    this.receiptPDF = receiptPDF;
  }

  public GetRTReceipt receiptPDF(String receiptPDF) {
    this.receiptPDF = receiptPDF;
    return this;
  }

 /**
   * Identificativo del PSP
   * @return pspId
  **/
  @JsonProperty("pspId")
 @Size(min=1,max=35)  public String getPspId() {
    return pspId;
  }

  public void setPspId(String pspId) {
    this.pspId = pspId;
  }

  public GetRTReceipt pspId(String pspId) {
    this.pspId = pspId;
    return this;
  }

 /**
   * Identificativo del canale del PSP
   * @return channelId
  **/
  @JsonProperty("channelId")
 @Size(min=1,max=35)  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public GetRTReceipt channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

 /**
   * Get paymentType
   * @return paymentType
  **/
  @JsonProperty("paymentType")
  public PaymentType getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(PaymentType paymentType) {
    this.paymentType = paymentType;
  }

  public GetRTReceipt paymentType(PaymentType paymentType) {
    this.paymentType = paymentType;
    return this;
  }

 /**
   * Descrizione della pendenza, che viene visualizzata nella sezione causale dell&#x27;avviso di pagamento
   * @return paymentCausal
  **/
  @JsonProperty("paymentCausal")
 @Size(min=1,max=140)  public String getPaymentCausal() {
    return paymentCausal;
  }

  public void setPaymentCausal(String paymentCausal) {
    this.paymentCausal = paymentCausal;
  }

  public GetRTReceipt paymentCausal(String paymentCausal) {
    this.paymentCausal = paymentCausal;
    return this;
  }

 /**
   * Identificativo unico della RT
   * @return idRT
  **/
  @JsonProperty("idRT")
 @Size(min=1,max=35)  public String getIdRT() {
    return idRT;
  }

  public void setIdRT(String idRT) {
    this.idRT = idRT;
  }

  public GetRTReceipt idRT(String idRT) {
    this.idRT = idRT;
    return this;
  }

 /**
   * Get paymentResultCode
   * @return paymentResultCode
  **/
  @JsonProperty("paymentResultCode")
  public PaymentResultCode getPaymentResultCode() {
    return paymentResultCode;
  }

  public void setPaymentResultCode(PaymentResultCode paymentResultCode) {
    this.paymentResultCode = paymentResultCode;
  }

  public GetRTReceipt paymentResultCode(PaymentResultCode paymentResultCode) {
    this.paymentResultCode = paymentResultCode;
    return this;
  }

 /**
   * Valore effettivamente pagato espresso in centesimi di Euro
   * @return amountPaid
  **/
  @JsonProperty("amountPaid")
  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }

  public GetRTReceipt amountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
    return this;
  }

 /**
   * Codice identificativo del contesto di pagamento
   * @return paymentContextCode
  **/
  @JsonProperty("paymentContextCode")
 @Size(min=1,max=35)  public String getPaymentContextCode() {
    return paymentContextCode;
  }

  public void setPaymentContextCode(String paymentContextCode) {
    this.paymentContextCode = paymentContextCode;
  }

  public GetRTReceipt paymentContextCode(String paymentContextCode) {
    this.paymentContextCode = paymentContextCode;
    return this;
  }

 /**
   * Data di creazione dell&#x27;RT da parte del PSP
   * @return dateRT
  **/
  @JsonProperty("dateRT")
  public Date getDateRT() {
    return dateRT;
  }

  public void setDateRT(Date dateRT) {
    this.dateRT = dateRT;
  }

  public GetRTReceipt dateRT(Date dateRT) {
    this.dateRT = dateRT;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRTReceipt {\n");
    
    sb.append("    receiptXML: ").append(toIndentedString(receiptXML)).append("\n");
    sb.append("    receiptPDF: ").append(toIndentedString(receiptPDF)).append("\n");
    sb.append("    pspId: ").append(toIndentedString(pspId)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    paymentType: ").append(toIndentedString(paymentType)).append("\n");
    sb.append("    paymentCausal: ").append(toIndentedString(paymentCausal)).append("\n");
    sb.append("    idRT: ").append(toIndentedString(idRT)).append("\n");
    sb.append("    paymentResultCode: ").append(toIndentedString(paymentResultCode)).append("\n");
    sb.append("    amountPaid: ").append(toIndentedString(amountPaid)).append("\n");
    sb.append("    paymentContextCode: ").append(toIndentedString(paymentContextCode)).append("\n");
    sb.append("    dateRT: ").append(toIndentedString(dateRT)).append("\n");
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
