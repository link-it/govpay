
package it.gov.digitpa.schemas._2011.pagamenti.revoche;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.gov.digitpa.schemas._2011.pagamenti.revoche package. 
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

    private final static QName _RR_QNAME = new QName("http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/", "RR");
    private final static QName _ER_QNAME = new QName("http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/", "ER");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.gov.digitpa.schemas._2011.pagamenti.revoche
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CtRichiestaRevoca }
     * 
     */
    public CtRichiestaRevoca createCtRichiestaRevoca() {
        return new CtRichiestaRevoca();
    }

    /**
     * Create an instance of {@link CtEsitoRevoca }
     * 
     */
    public CtEsitoRevoca createCtEsitoRevoca() {
        return new CtEsitoRevoca();
    }

    /**
     * Create an instance of {@link CtDominio }
     * 
     */
    public CtDominio createCtDominio() {
        return new CtDominio();
    }

    /**
     * Create an instance of {@link CtIdentificativoUnivoco }
     * 
     */
    public CtIdentificativoUnivoco createCtIdentificativoUnivoco() {
        return new CtIdentificativoUnivoco();
    }

    /**
     * Create an instance of {@link CtIdentificativoUnivocoPersonaFG }
     * 
     */
    public CtIdentificativoUnivocoPersonaFG createCtIdentificativoUnivocoPersonaFG() {
        return new CtIdentificativoUnivocoPersonaFG();
    }

    /**
     * Create an instance of {@link CtIdentificativoUnivocoPersonaG }
     * 
     */
    public CtIdentificativoUnivocoPersonaG createCtIdentificativoUnivocoPersonaG() {
        return new CtIdentificativoUnivocoPersonaG();
    }

    /**
     * Create an instance of {@link CtSoggettoVersante }
     * 
     */
    public CtSoggettoVersante createCtSoggettoVersante() {
        return new CtSoggettoVersante();
    }

    /**
     * Create an instance of {@link CtSoggettoPagatore }
     * 
     */
    public CtSoggettoPagatore createCtSoggettoPagatore() {
        return new CtSoggettoPagatore();
    }

    /**
     * Create an instance of {@link CtEnteBeneficiario }
     * 
     */
    public CtEnteBeneficiario createCtEnteBeneficiario() {
        return new CtEnteBeneficiario();
    }

    /**
     * Create an instance of {@link CtIstitutoAttestante }
     * 
     */
    public CtIstitutoAttestante createCtIstitutoAttestante() {
        return new CtIstitutoAttestante();
    }

    /**
     * Create an instance of {@link CtDatiRevoca }
     * 
     */
    public CtDatiRevoca createCtDatiRevoca() {
        return new CtDatiRevoca();
    }

    /**
     * Create an instance of {@link CtDatiEsitoRevoca }
     * 
     */
    public CtDatiEsitoRevoca createCtDatiEsitoRevoca() {
        return new CtDatiEsitoRevoca();
    }

    /**
     * Create an instance of {@link CtDatiSingolaRevoca }
     * 
     */
    public CtDatiSingolaRevoca createCtDatiSingolaRevoca() {
        return new CtDatiSingolaRevoca();
    }

    /**
     * Create an instance of {@link CtDatiSingoloEsitoRevoca }
     * 
     */
    public CtDatiSingoloEsitoRevoca createCtDatiSingoloEsitoRevoca() {
        return new CtDatiSingoloEsitoRevoca();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CtRichiestaRevoca }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/", name = "RR")
    public JAXBElement<CtRichiestaRevoca> createRR(CtRichiestaRevoca value) {
        return new JAXBElement<CtRichiestaRevoca>(_RR_QNAME, CtRichiestaRevoca.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CtEsitoRevoca }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.digitpa.gov.it/schemas/2011/Pagamenti/Revoche/", name = "ER")
    public JAXBElement<CtEsitoRevoca> createER(CtEsitoRevoca value) {
        return new JAXBElement<CtEsitoRevoca>(_ER_QNAME, CtEsitoRevoca.class, null, value);
    }

}
