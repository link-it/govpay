
package it.govpay.core.beans;

public enum TipoVersamento {


    /**
     * Bonifico Bancario di Tesoreria
     * 
     */
    BBT,

    /**
     * Bollettino Postale
     * 
     */
    BP,

    /**
     * Addebito diretto
     * 
     */
    AD,

    /**
     * Carta di Pagamento
     * 
     */
    CP,

    /**
     * On-line Banking e-payment
     * 
     */
    OBEP,

    /**
     * Pagamento attivato presso PSP
     * 
     */
    PO;

    public String value() {
        return this.name();
    }

    public static TipoVersamento fromValue(String v) {
        return valueOf(v);
    }

}
