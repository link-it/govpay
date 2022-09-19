package it.govpay.netpay.v1.beans;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

public class ActivePaymentRequest  implements IValidable {
  
  @Schema(required = true, description = "Url di Net@PayWeb a cui reindirizzare la risposta al termine del pagamento della posizione debitoria sul portale del PSP")
 /**
   * Url di Net@PayWeb a cui reindirizzare la risposta al termine del pagamento della posizione debitoria sul portale del PSP  
  **/
  private String callbackURL = null;
  
  @Schema(required = true, description = "Id della sessione interna definito da Net@PAYWeb")
 /**
   * Id della sessione interna definito da Net@PAYWeb  
  **/
  private String sessionID = null;
  
  @Schema(example = "12345678901", required = true, description = "Identificativo dell'Ente Creditore per il quale si fa il pagamento")
 /**
   * Identificativo dell'Ente Creditore per il quale si fa il pagamento  
  **/
  private String domainId = null;
  
  @Schema(example = "01000000000012345", required = true, description = "IUV della richiesta di pagamento")
 /**
   * IUV della richiesta di pagamento  
  **/
  private String creditorTxId = null;
  
  @Schema(required = true, description = "")
  private List<ActivePaymentItem> activePaymentList = new ArrayList<>();
 /**
   * Url di Net@PayWeb a cui reindirizzare la risposta al termine del pagamento della posizione debitoria sul portale del PSP
   * @return callbackURL
  **/
  @JsonProperty("callbackURL")
  @NotNull
  public String getCallbackURL() {
    return callbackURL;
  }

  public void setCallbackURL(String callbackURL) {
    this.callbackURL = callbackURL;
  }

  public ActivePaymentRequest callbackURL(String callbackURL) {
    this.callbackURL = callbackURL;
    return this;
  }

 /**
   * Id della sessione interna definito da Net@PAYWeb
   * @return sessionID
  **/
  @JsonProperty("sessionID")
  @NotNull
 @Size(max=36)  public String getSessionID() {
    return sessionID;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public ActivePaymentRequest sessionID(String sessionID) {
    this.sessionID = sessionID;
    return this;
  }

 /**
   * Identificativo dell&#x27;Ente Creditore per il quale si fa il pagamento
   * @return domainId
  **/
  @JsonProperty("domainId")
  @NotNull
 @Pattern(regexp="^([0-9]){35}$") @Size(max=35)  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  public ActivePaymentRequest domainId(String domainId) {
    this.domainId = domainId;
    return this;
  }

 /**
   * IUV della richiesta di pagamento
   * @return creditorTxId
  **/
  @JsonProperty("creditorTxId")
  @NotNull
 @Pattern(regexp="^([\\w]){1,35}$") @Size(max=35)  public String getCreditorTxId() {
    return creditorTxId;
  }

  public void setCreditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
  }

  public ActivePaymentRequest creditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
    return this;
  }

 /**
   * Get activePaymentList
   * @return activePaymentList
  **/
  @JsonProperty("activePaymentList")
  @NotNull
 @Size(min=1,max=5)  public List<ActivePaymentItem> getActivePaymentList() {
    return activePaymentList;
  }

  public void setActivePaymentList(List<ActivePaymentItem> activePaymentList) {
    this.activePaymentList = activePaymentList;
  }

  public ActivePaymentRequest activePaymentList(List<ActivePaymentItem> activePaymentList) {
    this.activePaymentList = activePaymentList;
    return this;
  }

  public ActivePaymentRequest addActivePaymentListItem(ActivePaymentItem activePaymentListItem) {
    this.activePaymentList.add(activePaymentListItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivePaymentRequest {\n");
    
    sb.append("    callbackURL: ").append(toIndentedString(callbackURL)).append("\n");
    sb.append("    sessionID: ").append(toIndentedString(sessionID)).append("\n");
    sb.append("    domainId: ").append(toIndentedString(domainId)).append("\n");
    sb.append("    creditorTxId: ").append(toIndentedString(creditorTxId)).append("\n");
    sb.append("    activePaymentList: ").append(toIndentedString(activePaymentList)).append("\n");
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

	vf.getValidator("callbackURL", this.callbackURL).notNull();
	vf.getValidator("sessionID", this.sessionID).notNull().minLength(1).maxLength(36);
	vf.getValidator("domainId", this.domainId).notNull();
	validatoreId.validaIdDominio("domainId", domainId);
	vf.getValidator("creditorTxId", this.creditorTxId).notNull().minLength(1).maxLength(35);
	vf.getValidator("activePaymentList", this.activePaymentList).notNull().minItems(1).maxItems(5).validateObjects();
  }
}
