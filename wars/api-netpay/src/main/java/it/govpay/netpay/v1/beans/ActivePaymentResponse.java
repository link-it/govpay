package it.govpay.netpay.v1.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

public class ActivePaymentResponse   {
  public enum ResultEnum {
    OK("OK"),
    KO("KO");

    private String value;

    ResultEnum(String value) {
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
    public static ResultEnum fromValue(String text) {
      for (ResultEnum b : ResultEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }  
  @Schema(example = "OK", required = true, description = "Esito della presa in carico della richiesta")
 /**
   * Esito della presa in carico della richiesta  
  **/
  private ResultEnum result = null;
  
  @Schema(description = "")
  private ErrorReason errorReason = null;
  
  @Schema(description = "")
  private ErrorReasonAgID errorReasonAgID = null;
  
  @Schema(description = "Eventuale ulteriore dettaglio testuale sul problema riscontrato")
 /**
   * Eventuale ulteriore dettaglio testuale sul problema riscontrato  
  **/
  private String errorMessage = null;
  
  @Schema(description = "URL a cui Net@PAYWeb deve fare la redirect per far effettuare il pagamento su WISP 2.0 all’utente")
 /**
   * URL a cui Net@PAYWeb deve fare la redirect per far effettuare il pagamento su WISP 2.0 all’utente  
  **/
  private String redirectURL = null;
  
  @Schema(description = "Id della sessione interna definito da Net@PAYWeb")
 /**
   * Id della sessione interna definito da Net@PAYWeb  
  **/
  private String sessionID = null;
  
  @Schema(description = "Identificativo Univoco della transazione di pagamento.")
 /**
   * Identificativo Univoco della transazione di pagamento.  
  **/
  private String creditorTxId = null;
 /**
   * Esito della presa in carico della richiesta
   * @return result
  **/
  @JsonProperty("result")
  @NotNull
  public String getResult() {
    if (result == null) {
      return null;
    }
    return result.getValue();
  }

  public void setResult(ResultEnum result) {
    this.result = result;
  }

  public ActivePaymentResponse result(ResultEnum result) {
    this.result = result;
    return this;
  }

 /**
   * Get errorReason
   * @return errorReason
  **/
  @JsonProperty("errorReason")
  public ErrorReason getErrorReason() {
    return errorReason;
  }

  public void setErrorReason(ErrorReason errorReason) {
    this.errorReason = errorReason;
  }

  public ActivePaymentResponse errorReason(ErrorReason errorReason) {
    this.errorReason = errorReason;
    return this;
  }

 /**
   * Get errorReasonAgID
   * @return errorReasonAgID
  **/
  @JsonProperty("errorReasonAgID")
  public ErrorReasonAgID getErrorReasonAgID() {
    return errorReasonAgID;
  }

  public void setErrorReasonAgID(ErrorReasonAgID errorReasonAgID) {
    this.errorReasonAgID = errorReasonAgID;
  }

  public ActivePaymentResponse errorReasonAgID(ErrorReasonAgID errorReasonAgID) {
    this.errorReasonAgID = errorReasonAgID;
    return this;
  }

 /**
   * Eventuale ulteriore dettaglio testuale sul problema riscontrato
   * @return errorMessage
  **/
  @JsonProperty("errorMessage")
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ActivePaymentResponse errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

 /**
   * URL a cui Net@PAYWeb deve fare la redirect per far effettuare il pagamento su WISP 2.0 all’utente
   * @return redirectURL
  **/
  @JsonProperty("redirectURL")
  public String getRedirectURL() {
    return redirectURL;
  }

  public void setRedirectURL(String redirectURL) {
    this.redirectURL = redirectURL;
  }

  public ActivePaymentResponse redirectURL(String redirectURL) {
    this.redirectURL = redirectURL;
    return this;
  }

 /**
   * Id della sessione interna definito da Net@PAYWeb
   * @return sessionID
  **/
  @JsonProperty("sessionID")
  public String getSessionID() {
    return sessionID;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public ActivePaymentResponse sessionID(String sessionID) {
    this.sessionID = sessionID;
    return this;
  }

 /**
   * Identificativo Univoco della transazione di pagamento.
   * @return creditorTxId
  **/
  @JsonProperty("creditorTxId")
  public String getCreditorTxId() {
    return creditorTxId;
  }

  public void setCreditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
  }

  public ActivePaymentResponse creditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivePaymentResponse {\n");
    
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    errorReason: ").append(toIndentedString(errorReason)).append("\n");
    sb.append("    errorReasonAgID: ").append(toIndentedString(errorReasonAgID)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    redirectURL: ").append(toIndentedString(redirectURL)).append("\n");
    sb.append("    sessionID: ").append(toIndentedString(sessionID)).append("\n");
    sb.append("    creditorTxId: ").append(toIndentedString(creditorTxId)).append("\n");
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
