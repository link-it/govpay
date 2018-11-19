
package it.gov.digitpa.schemas._2011.ws.psp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.gov.digitpa.schemas._2011.ws.psp package. 
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

    private final static QName _PaaVerificaRPT_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaVerificaRPT");
    private final static QName _PaaVerificaRPTRisposta_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaVerificaRPTRisposta");
    private final static QName _PaaAttivaRPT_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaAttivaRPT");
    private final static QName _PaaAttivaRPTRisposta_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "paaAttivaRPTRisposta");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.gov.digitpa.schemas._2011.ws.psp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaaVerificaRPT }
     * 
     */
    public PaaVerificaRPT createPaaVerificaRPT() {
        return new PaaVerificaRPT();
    }

    /**
     * Create an instance of {@link PaaVerificaRPTRisposta }
     * 
     */
    public PaaVerificaRPTRisposta createPaaVerificaRPTRisposta() {
        return new PaaVerificaRPTRisposta();
    }

    /**
     * Create an instance of {@link PaaAttivaRPT }
     * 
     */
    public PaaAttivaRPT createPaaAttivaRPT() {
        return new PaaAttivaRPT();
    }

    /**
     * Create an instance of {@link PaaAttivaRPTRisposta }
     * 
     */
    public PaaAttivaRPTRisposta createPaaAttivaRPTRisposta() {
        return new PaaAttivaRPTRisposta();
    }

    /**
     * Create an instance of {@link CtSpezzoniCausaleVersamento }
     * 
     */
    public CtSpezzoniCausaleVersamento createCtSpezzoniCausaleVersamento() {
        return new CtSpezzoniCausaleVersamento();
    }

    /**
     * Create an instance of {@link CtSpezzoneStrutturatoCausaleVersamento }
     * 
     */
    public CtSpezzoneStrutturatoCausaleVersamento createCtSpezzoneStrutturatoCausaleVersamento() {
        return new CtSpezzoneStrutturatoCausaleVersamento();
    }

    /**
     * Create an instance of {@link PaaTipoDatiPagamentoPA }
     * 
     */
    public PaaTipoDatiPagamentoPA createPaaTipoDatiPagamentoPA() {
        return new PaaTipoDatiPagamentoPA();
    }

    /**
     * Create an instance of {@link PaaTipoDatiPagamentoPSP }
     * 
     */
    public PaaTipoDatiPagamentoPSP createPaaTipoDatiPagamentoPSP() {
        return new PaaTipoDatiPagamentoPSP();
    }

    /**
     * Create an instance of {@link EsitoVerificaRPT }
     * 
     */
    public EsitoVerificaRPT createEsitoVerificaRPT() {
        return new EsitoVerificaRPT();
    }

    /**
     * Create an instance of {@link EsitoAttivaRPT }
     * 
     */
    public EsitoAttivaRPT createEsitoAttivaRPT() {
        return new EsitoAttivaRPT();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaVerificaRPT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaVerificaRPT")
    public JAXBElement<PaaVerificaRPT> createPaaVerificaRPT(PaaVerificaRPT value) {
        return new JAXBElement<>(_PaaVerificaRPT_QNAME, PaaVerificaRPT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaVerificaRPTRisposta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaVerificaRPTRisposta")
    public JAXBElement<PaaVerificaRPTRisposta> createPaaVerificaRPTRisposta(PaaVerificaRPTRisposta value) {
        return new JAXBElement<>(_PaaVerificaRPTRisposta_QNAME, PaaVerificaRPTRisposta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaAttivaRPT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaAttivaRPT")
    public JAXBElement<PaaAttivaRPT> createPaaAttivaRPT(PaaAttivaRPT value) {
        return new JAXBElement<>(_PaaAttivaRPT_QNAME, PaaAttivaRPT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaaAttivaRPTRisposta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "paaAttivaRPTRisposta")
    public JAXBElement<PaaAttivaRPTRisposta> createPaaAttivaRPTRisposta(PaaAttivaRPTRisposta value) {
        return new JAXBElement<>(_PaaAttivaRPTRisposta_QNAME, PaaAttivaRPTRisposta.class, null, value);
    }

}
