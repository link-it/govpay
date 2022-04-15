package it.govpay.ec.v2.beans;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Modello di pagamento
 */
public enum ModelloPagamento {
  ENTE("Pagamento ad iniziativa Ente"),
  
  PSP("Pagamento ad iniziativa PSP");

  private String value;

  ModelloPagamento(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ModelloPagamento fromValue(String text) {
    for (ModelloPagamento b : ModelloPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
