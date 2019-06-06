
package it.govpay.core.beans;

public enum TipoAutenticazione {

    CNS("CNS"),
    USR("USR"),
    OTH("OTH"),
    N_A("N/A");
    private final String value;

    TipoAutenticazione(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static TipoAutenticazione fromValue(String v) {
        for (TipoAutenticazione c: TipoAutenticazione.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
