
package it.govpay.core.beans;

public enum ModelloPagamento {

    IMMEDIATO,
    IMMEDIATO_MULTIBENEFICIARIO,
    DIFFERITO,
    ATTIVATO_PRESSO_PSP;

    public String value() {
        return this.name();
    }

    public static ModelloPagamento fromValue(String v) {
        return valueOf(v);
    }

}
