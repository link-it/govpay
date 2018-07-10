//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.07.10 alle 10:18:53 AM CEST 
//


package gov.telematici.pagamenti.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.telematici.pagamenti.ws package. 
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

    private final static QName _ListaEsitoAvvisiDigitali_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "listaEsitoAvvisiDigitali");
    private final static QName _CtAvvisoDigitale_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "avvisoDigitale");
    private final static QName _EsitoPresaInCarico_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "esitoPresaInCarico");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.telematici.pagamenti.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListaAvvisiDigitali }
     * 
     */
    public ListaAvvisiDigitali createListaAvvisiDigitali() {
        return new ListaAvvisiDigitali();
    }

    /**
     * Create an instance of {@link CtAvvisoDigitale }
     * 
     */
    public CtAvvisoDigitale createCtAvvisoDigitale() {
        return new CtAvvisoDigitale();
    }

    /**
     * Create an instance of {@link ListaEsitoAvvisiDigitali }
     * 
     */
    public ListaEsitoAvvisiDigitali createListaEsitoAvvisiDigitali() {
        return new ListaEsitoAvvisiDigitali();
    }

    /**
     * Create an instance of {@link CtEsitoPresaInCarico }
     * 
     */
    public CtEsitoPresaInCarico createCtEsitoPresaInCarico() {
        return new CtEsitoPresaInCarico();
    }

    /**
     * Create an instance of {@link CtFaultBean }
     * 
     */
    public CtFaultBean createCtFaultBean() {
        return new CtFaultBean();
    }

    /**
     * Create an instance of {@link CtIdentificativoUnivocoPersonaFG }
     * 
     */
    public CtIdentificativoUnivocoPersonaFG createCtIdentificativoUnivocoPersonaFG() {
        return new CtIdentificativoUnivocoPersonaFG();
    }

    /**
     * Create an instance of {@link CtSoggettoPagatore }
     * 
     */
    public CtSoggettoPagatore createCtSoggettoPagatore() {
        return new CtSoggettoPagatore();
    }

    /**
     * Create an instance of {@link CtDatiSingoloVersamento }
     * 
     */
    public CtDatiSingoloVersamento createCtDatiSingoloVersamento() {
        return new CtDatiSingoloVersamento();
    }

    /**
     * Create an instance of {@link CtEsitoAvvisatura }
     * 
     */
    public CtEsitoAvvisatura createCtEsitoAvvisatura() {
        return new CtEsitoAvvisatura();
    }

    /**
     * Create an instance of {@link CtRisposta }
     * 
     */
    public CtRisposta createCtRisposta() {
        return new CtRisposta();
    }

    /**
     * Create an instance of {@link CtEsitoAvvisoDigitale }
     * 
     */
    public CtEsitoAvvisoDigitale createCtEsitoAvvisoDigitale() {
        return new CtEsitoAvvisoDigitale();
    }

    /**
     * Create an instance of {@link CtPeriodoRiferimento }
     * 
     */
    public CtPeriodoRiferimento createCtPeriodoRiferimento() {
        return new CtPeriodoRiferimento();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaEsitoAvvisiDigitali }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "listaEsitoAvvisiDigitali")
    public JAXBElement<ListaEsitoAvvisiDigitali> createListaEsitoAvvisiDigitali(ListaEsitoAvvisiDigitali value) {
        return new JAXBElement<ListaEsitoAvvisiDigitali>(_ListaEsitoAvvisiDigitali_QNAME, ListaEsitoAvvisiDigitali.class, null, value);
    }
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaEsitoAvvisiDigitali }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "avvisoDigitale")
    public JAXBElement<CtAvvisoDigitale> createCtEsitoAvvisoDigitale(CtAvvisoDigitale value) {
        return new JAXBElement<CtAvvisoDigitale>(_CtAvvisoDigitale_QNAME, CtAvvisoDigitale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CtEsitoPresaInCarico }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.pagamenti.telematici.gov/", name = "esitoPresaInCarico")
    public JAXBElement<CtEsitoPresaInCarico> createEsitoPresaInCarico(CtEsitoPresaInCarico value) {
        return new JAXBElement<CtEsitoPresaInCarico>(_EsitoPresaInCarico_QNAME, CtEsitoPresaInCarico.class, null, value);
    }

}
