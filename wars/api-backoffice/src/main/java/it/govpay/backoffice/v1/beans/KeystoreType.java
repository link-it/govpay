package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets keystoreType
 */
public enum KeystoreType {
  
  
  
  
  JKS("JKS");
  
  
  

  private String value;

  KeystoreType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static KeystoreType fromValue(String text) {
    for (KeystoreType b : KeystoreType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



