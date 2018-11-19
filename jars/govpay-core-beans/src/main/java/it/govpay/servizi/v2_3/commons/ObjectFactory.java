
package it.govpay.servizi.v2_3.commons;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.govpay.servizi.v2_3.commons package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.govpay.servizi.v2_3.commons
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Incasso }
     * 
     */
    public Incasso createIncasso() {
        return new Incasso();
    }

    /**
     * Create an instance of {@link FlussoRendicontazione }
     * 
     */
    public FlussoRendicontazione createFlussoRendicontazione() {
        return new FlussoRendicontazione();
    }

    /**
     * Create an instance of {@link FlussoRendicontazione.Rendicontazione }
     * 
     */
    public FlussoRendicontazione.Rendicontazione createFlussoRendicontazioneRendicontazione() {
        return new FlussoRendicontazione.Rendicontazione();
    }

    /**
     * Create an instance of {@link GpResponse }
     * 
     */
    public GpResponse createGpResponse() {
        return new GpResponse();
    }

    /**
     * Create an instance of {@link EstremiFlussoRendicontazione }
     * 
     */
    public EstremiFlussoRendicontazione createEstremiFlussoRendicontazione() {
        return new EstremiFlussoRendicontazione();
    }

    /**
     * Create an instance of {@link Incasso.Pagamento }
     * 
     */
    public Incasso.Pagamento createIncassoPagamento() {
        return new Incasso.Pagamento();
    }

    /**
     * Create an instance of {@link FlussoRendicontazione.Rendicontazione.Pagamento }
     * 
     */
    public FlussoRendicontazione.Rendicontazione.Pagamento createFlussoRendicontazioneRendicontazionePagamento() {
        return new FlussoRendicontazione.Rendicontazione.Pagamento();
    }

}
