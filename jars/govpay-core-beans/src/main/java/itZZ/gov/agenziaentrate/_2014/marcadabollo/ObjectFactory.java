//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.10.10 alle 02:21:10 PM CEST 
//


package itZZ.gov.agenziaentrate._2014.marcadabollo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.gov.agenziaentrate._2014.marcadabollo package. 
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

    private final static QName _MarcaDaBollo_QNAME = new QName("http://www.agenziaentrate.gov.it/2014/MarcaDaBollo", "marcaDaBollo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.gov.agenziaentrate._2014.marcadabollo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TipoMarcaDaBollo }
     * 
     */
    public TipoMarcaDaBollo createTipoMarcaDaBollo() {
        return new TipoMarcaDaBollo();
    }

    /**
     * Create an instance of {@link DDigestMethodType }
     * 
     */
    public DDigestMethodType createDDigestMethodType() {
        return new DDigestMethodType();
    }

    /**
     * Create an instance of {@link TipoPSP }
     * 
     */
    public TipoPSP createTipoPSP() {
        return new TipoPSP();
    }

    /**
     * Create an instance of {@link TipoImpronta }
     * 
     */
    public TipoImpronta createTipoImpronta() {
        return new TipoImpronta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMarcaDaBollo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.agenziaentrate.gov.it/2014/MarcaDaBollo", name = "marcaDaBollo")
    public JAXBElement<TipoMarcaDaBollo> createMarcaDaBollo(TipoMarcaDaBollo value) {
        return new JAXBElement<TipoMarcaDaBollo>(_MarcaDaBollo_QNAME, TipoMarcaDaBollo.class, null, value);
    }

}
