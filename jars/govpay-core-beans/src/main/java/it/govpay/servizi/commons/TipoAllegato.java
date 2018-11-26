
package it.govpay.servizi.commons;

public enum TipoAllegato {


    /**
     * Esito del pagamento prodotto dal PSP
     * 
     */
    ES,

    /**
     * Marca da Bollo Digitale
     * 
     */
    BD;

    public String value() {
        return this.name();
    }

    public static TipoAllegato fromValue(String v) {
        return valueOf(v);
    }

}
