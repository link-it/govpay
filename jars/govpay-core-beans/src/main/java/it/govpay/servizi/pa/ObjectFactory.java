
package it.govpay.servizi.pa;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.govpay.servizi.pa package. 
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

    private final static QName _PaNotificaTransazioneResponse_QNAME = new QName("http://www.govpay.it/servizi/pa/", "paNotificaTransazioneResponse");
    private final static QName _PaNotificaStornoResponse_QNAME = new QName("http://www.govpay.it/servizi/pa/", "paNotificaStornoResponse");
    private final static QName _PaVerificaVersamentoCodApplicazione_QNAME = new QName("", "codApplicazione");
    private final static QName _PaVerificaVersamentoCodVersamentoEnte_QNAME = new QName("", "codVersamentoEnte");
    private final static QName _PaVerificaVersamentoCodDominio_QNAME = new QName("", "codDominio");
    private final static QName _PaVerificaVersamentoIuv_QNAME = new QName("", "iuv");
    private final static QName _PaVerificaVersamentoBundlekey_QNAME = new QName("", "bundlekey");
    private final static QName _PaVerificaVersamentoCodUnivocoDebitore_QNAME = new QName("", "codUnivocoDebitore");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.govpay.servizi.pa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaNotificaStorno }
     * 
     */
    public PaNotificaStorno createPaNotificaStorno() {
        return new PaNotificaStorno();
    }

    /**
     * Create an instance of {@link PaNotificaTransazione }
     * 
     */
    public PaNotificaTransazione createPaNotificaTransazione() {
        return new PaNotificaTransazione();
    }

    /**
     * Create an instance of {@link PaNotificaStorno.RichiestaStorno }
     * 
     */
    public PaNotificaStorno.RichiestaStorno createPaNotificaStornoRichiestaStorno() {
        return new PaNotificaStorno.RichiestaStorno();
    }

    /**
     * Create an instance of {@link PaVerificaVersamento }
     * 
     */
    public PaVerificaVersamento createPaVerificaVersamento() {
        return new PaVerificaVersamento();
    }

    /**
     * Create an instance of {@link PaVerificaVersamentoResponse }
     * 
     */
    public PaVerificaVersamentoResponse createPaVerificaVersamentoResponse() {
        return new PaVerificaVersamentoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/servizi/pa/", name = "paNotificaTransazioneResponse")
    public JAXBElement<String> createPaNotificaTransazioneResponse(String value) {
        return new JAXBElement<>(_PaNotificaTransazioneResponse_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/servizi/pa/", name = "paNotificaStornoResponse")
    public JAXBElement<String> createPaNotificaStornoResponse(String value) {
        return new JAXBElement<>(_PaNotificaStornoResponse_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codApplicazione", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoCodApplicazione(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoCodApplicazione_QNAME, String.class, PaVerificaVersamento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codVersamentoEnte", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoCodVersamentoEnte(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoCodVersamentoEnte_QNAME, String.class, PaVerificaVersamento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codDominio", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoCodDominio(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoCodDominio_QNAME, String.class, PaVerificaVersamento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "iuv", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoIuv(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoIuv_QNAME, String.class, PaVerificaVersamento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "bundlekey", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoBundlekey(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoBundlekey_QNAME, String.class, PaVerificaVersamento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codUnivocoDebitore", scope = PaVerificaVersamento.class)
    public JAXBElement<String> createPaVerificaVersamentoCodUnivocoDebitore(String value) {
        return new JAXBElement<>(_PaVerificaVersamentoCodUnivocoDebitore_QNAME, String.class, PaVerificaVersamento.class, value);
    }

}
