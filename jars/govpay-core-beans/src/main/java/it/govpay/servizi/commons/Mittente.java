
package it.govpay.servizi.commons;

public enum Mittente {

    NODO_DEI_PAGAMENTI_SPC("NodoDeiPagamentiSPC"),
    PSP("PSP"),
    GOV_PAY("GovPay");
    private final String value;

    Mittente(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static Mittente fromValue(String v) {
        for (Mittente c: Mittente.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
