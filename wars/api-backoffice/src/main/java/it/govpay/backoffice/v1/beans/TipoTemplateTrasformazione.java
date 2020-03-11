package io.swagger.servlets.swagger-inflector-server.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Indica il tipo di template da applicare per effettuare la trasformazione
 **/
import com.fasterxml.jackson.annotation.JsonCreator;


/**
 * Indica il tipo di template da applicare per effettuare la trasformazione
 */
public enum TipoTemplateTrasformazione {
  
  
  
  
  FREEMARKER("freemarker");
  
  
  

  private String value;

  TipoTemplateTrasformazione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoTemplateTrasformazione fromValue(String text) {
    for (TipoTemplateTrasformazione b : TipoTemplateTrasformazione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



