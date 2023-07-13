package it.govpay.backoffice.v1.beans;


/**
 * Classificazione ente della tassonomia pagoPA  * 01: COMUNE / UNIONE DI COMUNI / CONSORZI  * 02: PROVINCIA / CITTA&#x27; METROPOLITANA  * 03: REGIONE  * 04: ORDINI PROFESSIONALI  * 05: SERVIZIO SANITARIO NAZIONALE  * 06: UNIVERSITÀ / SCUOLA STATALE  * 07: PUBBLICHE AMMINISTRAZIONI CENTRALI  * 08: ALTRE AMMINISTRAZIONI  * 09: GESTORI PUBBLICI SERVIZI  * 11: ENTI DI NATURA PRIVATISTICA  * 12: AGENZIE FISCALI
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Classificazione ente della tassonomia pagoPA  * 01: COMUNE / UNIONE DI COMUNI / CONSORZI  * 02: PROVINCIA / CITTA' METROPOLITANA  * 03: REGIONE  * 04: ORDINI PROFESSIONALI  * 05: SERVIZIO SANITARIO NAZIONALE  * 06: UNIVERSITÀ / SCUOLA STATALE  * 07: PUBBLICHE AMMINISTRAZIONI CENTRALI  * 08: ALTRE AMMINISTRAZIONI  * 09: GESTORI PUBBLICI SERVIZI  * 11: ENTI DI NATURA PRIVATISTICA  * 12: AGENZIE FISCALI
 */
public enum TassonomiaPagoPADominio {




  _01("01"),


  _02("02"),


  _03("03"),


  _04("04"),


  _05("05"),


  _06("06"),


  _07("07"),


  _08("08"),


  _09("09"),


  _11("11"),


  _12("12");




  private String value;

  TassonomiaPagoPADominio(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TassonomiaPagoPADominio fromValue(String text) {
    for (TassonomiaPagoPADominio b : TassonomiaPagoPADominio.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



