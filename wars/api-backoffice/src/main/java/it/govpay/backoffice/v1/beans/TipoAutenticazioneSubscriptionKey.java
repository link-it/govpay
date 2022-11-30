package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"subscriptionKey",
})
public class TipoAutenticazioneSubscriptionKey extends JSONSerializable {
  
  @JsonProperty("subscriptionKey")
  private String subscriptionKey = null;
  
  /**
   **/
  public TipoAutenticazioneSubscriptionKey subscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
    return this;
  }

  @JsonProperty("subscriptionKey")
  public String getSubscriptionKey() {
    return subscriptionKey;
  }
  public void setSubscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneSubscriptionKey tipoAutenticazioneSubscriptionKey = (TipoAutenticazioneSubscriptionKey) o;
    return Objects.equals(subscriptionKey, tipoAutenticazioneSubscriptionKey.subscriptionKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionKey);
  }

  public static TipoAutenticazioneSubscriptionKey parse(String json) throws IOException {
    return (TipoAutenticazioneSubscriptionKey) parse(json, TipoAutenticazioneSubscriptionKey.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneSubscriptionKey";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneSubscriptionKey {\n");
    
    sb.append("    subscriptionKey: ").append(toIndentedString(subscriptionKey)).append("\n");
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



