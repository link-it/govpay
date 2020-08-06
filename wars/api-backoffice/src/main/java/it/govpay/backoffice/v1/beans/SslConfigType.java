package it.govpay.backoffice.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets sslConfigType
 */
public enum SslConfigType {
  
  
  
  
  SSL("SSL"),
  
  
  SSLV3("SSLv3"),
  
  
  TLS("TLS"),
  
  
  TLSV1("TLSv1"),
  
  
  TLSV1_1("TLSv1.1"),
  
  
  TLSV1_2("TLSv1.2");
  
  
  

  private String value;

  SslConfigType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SslConfigType fromValue(String text) {
    for (SslConfigType b : SslConfigType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



