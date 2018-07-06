
package it.gov.digitpa.schemas._2011.ws.nodo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.gov.digitpa.schemas._2011.ws.nodo package. 
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

    private final static QName _PaaInviaRT_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaInviaRT");
    private final static QName _PaaInviaRTRisposta_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaInviaRTRisposta");
    private final static QName _PaaInviaEsitoStorno_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaInviaEsitoStorno");
    private final static QName _PaaInviaEsitoStornoRisposta_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaInviaEsitoStornoRisposta");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.gov.digitpa.schemas._2011.ws.nodo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaaInviaRT }
     * 
     */
    public PaaInviaRT createPaaInviaRT() {
        return new PaaInviaRT();
    }

    /**
     * Create an instance of {@link PaaInviaRTRisposta }
     * 
     */
    public PaaInviaRTRisposta createPaaInviaRTRisposta() {
        return new PaaInviaRTRisposta();
    }

    /**
     * Create an instance of {@link PaaInviaEsitoStorno }
     * 
     */
    public PaaInviaEsitoStorno createPaaInviaEsitoStorno() {
        return new PaaInviaEsitoStorno();
    }

    /**
     * Create an instance of {@link PaaInviaEsitoStornoRisposta }
     * 
     */
    public PaaInviaEsitoStornoRisposta createPaaInviaEsitoStornoRisposta() {
        return new PaaInviaEsitoStornoRisposta();
    }

    /**
     * Create an instance of {@link EsitoPaaInviaRT }
     * 
     */
    public EsitoPaaInviaRT createEsitoPaaInviaRT() {
        return new EsitoPaaInviaRT();
    }

    /**
     * Create an instance of {@link Risposta }
     * 
     */
    public Risposta createRisposta() {
        return new Risposta();
    }

    /**
     * Create an instance of {@link FaultBean }
     * 
     */
    public FaultBean createFaultBean() {
        return new FaultBean();
    }

    /**
     * Create an instance of {@link TipoInviaEsitoStornoRisposta }
     * 
     */
    public TipoInviaEsitoStornoRisposta createTipoInviaEsitoStornoRisposta() {
        return new TipoInviaEsitoStornoRisposta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaInviaRT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaInviaRT")
    public JAXBElement<PaaInviaRT> createPaaInviaRT(PaaInviaRT value) {
        return new JAXBElement<PaaInviaRT>(_PaaInviaRT_QNAME, PaaInviaRT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaInviaRTRisposta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaInviaRTRisposta")
    public JAXBElement<PaaInviaRTRisposta> createPaaInviaRTRisposta(PaaInviaRTRisposta value) {
        return new JAXBElement<PaaInviaRTRisposta>(_PaaInviaRTRisposta_QNAME, PaaInviaRTRisposta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaInviaEsitoStorno }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaInviaEsitoStorno")
    public JAXBElement<PaaInviaEsitoStorno> createPaaInviaEsitoStorno(PaaInviaEsitoStorno value) {
        return new JAXBElement<PaaInviaEsitoStorno>(_PaaInviaEsitoStorno_QNAME, PaaInviaEsitoStorno.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaInviaEsitoStornoRisposta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaInviaEsitoStornoRisposta")
    public JAXBElement<PaaInviaEsitoStornoRisposta> createPaaInviaEsitoStornoRisposta(PaaInviaEsitoStornoRisposta value) {
        return new JAXBElement<PaaInviaEsitoStornoRisposta>(_PaaInviaEsitoStornoRisposta_QNAME, PaaInviaEsitoStornoRisposta.class, null, value);
    }

}
