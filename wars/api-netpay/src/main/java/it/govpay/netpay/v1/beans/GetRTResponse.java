package it.govpay.netpay.v1.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetRTResponse   {
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
  
  @Schema(description = "")
  private GetRTReceipt receipt = null;
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

  public GetRTResponse result(ResultEnum result) {
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

  public GetRTResponse errorReason(ErrorReason errorReason) {
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

  public GetRTResponse errorReasonAgID(ErrorReasonAgID errorReasonAgID) {
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

  public GetRTResponse errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

 /**
   * Get receipt
   * @return receipt
  **/
  @JsonProperty("receipt")
  public GetRTReceipt getReceipt() {
    return receipt;
  }

  public void setReceipt(GetRTReceipt receipt) {
    this.receipt = receipt;
  }

  public GetRTResponse receipt(GetRTReceipt receipt) {
    this.receipt = receipt;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRTResponse {\n");
    
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    errorReason: ").append(toIndentedString(errorReason)).append("\n");
    sb.append("    errorReasonAgID: ").append(toIndentedString(errorReasonAgID)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    receipt: ").append(toIndentedString(receipt)).append("\n");
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
