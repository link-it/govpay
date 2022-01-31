
package it.govpay.servizi.commons;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.govpay.servizi.commons package. 
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

    private final static QName _MetaInfo_QNAME = new QName("http://www.govpay.it/servizi/commons/", "metaInfo");
    private final static QName _VersamentoKeyCodApplicazione_QNAME = new QName("", "codApplicazione");
    private final static QName _VersamentoKeyCodVersamentoEnte_QNAME = new QName("", "codVersamentoEnte");
    private final static QName _VersamentoKeyCodDominio_QNAME = new QName("", "codDominio");
    private final static QName _VersamentoKeyCodUnivocoDebitore_QNAME = new QName("", "codUnivocoDebitore");
    private final static QName _VersamentoKeyBundlekey_QNAME = new QName("", "bundlekey");
    private final static QName _VersamentoKeyIuv_QNAME = new QName("", "iuv");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.govpay.servizi.commons
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Versamento }
     * 
     */
    public Versamento createVersamento() {
        return new Versamento();
    }

    /**
     * Create an instance of {@link Versamento.SingoloVersamento }
     * 
     */
    public Versamento.SingoloVersamento createVersamentoSingoloVersamento() {
        return new Versamento.SingoloVersamento();
    }

    /**
     * Create an instance of {@link it.govpay.servizi.commons.Pagamento }
     * 
     */
    public it.govpay.servizi.commons.Pagamento createPagamento() {
        return new it.govpay.servizi.commons.Pagamento();
    }

    /**
     * Create an instance of {@link FlussoRendicontazione }
     * 
     */
    public FlussoRendicontazione createFlussoRendicontazione() {
        return new FlussoRendicontazione();
    }

    /**
     * Create an instance of {@link MetaInfo }
     * 
     */
    public MetaInfo createMetaInfo() {
        return new MetaInfo();
    }

    /**
     * Create an instance of {@link GpResponse }
     * 
     */
    public GpResponse createGpResponse() {
        return new GpResponse();
    }

    /**
     * Create an instance of {@link Canale }
     * 
     */
    public Canale createCanale() {
        return new Canale();
    }

    /**
     * Create an instance of {@link EstremiFlussoRendicontazione }
     * 
     */
    public EstremiFlussoRendicontazione createEstremiFlussoRendicontazione() {
        return new EstremiFlussoRendicontazione();
    }

    /**
     * Create an instance of {@link IuvGenerato }
     * 
     */
    public IuvGenerato createIuvGenerato() {
        return new IuvGenerato();
    }

    /**
     * Create an instance of {@link Transazione }
     * 
     */
    public Transazione createTransazione() {
        return new Transazione();
    }

    /**
     * Create an instance of {@link RichiestaStorno }
     * 
     */
    public RichiestaStorno createRichiestaStorno() {
        return new RichiestaStorno();
    }

    /**
     * Create an instance of {@link VersamentoKey }
     * 
     */
    public VersamentoKey createVersamentoKey() {
        return new VersamentoKey();
    }

    /**
     * Create an instance of {@link Anomalia }
     * 
     */
    public Anomalia createAnomalia() {
        return new Anomalia();
    }

    /**
     * Create an instance of {@link Anagrafica }
     * 
     */
    public Anagrafica createAnagrafica() {
        return new Anagrafica();
    }

    /**
     * Create an instance of {@link Versamento.SpezzoneCausaleStrutturata }
     * 
     */
    public Versamento.SpezzoneCausaleStrutturata createVersamentoSpezzoneCausaleStrutturata() {
        return new Versamento.SpezzoneCausaleStrutturata();
    }

    /**
     * Create an instance of {@link Versamento.SingoloVersamento.BolloTelematico }
     * 
     */
    public Versamento.SingoloVersamento.BolloTelematico createVersamentoSingoloVersamentoBolloTelematico() {
        return new Versamento.SingoloVersamento.BolloTelematico();
    }

    /**
     * Create an instance of {@link Versamento.SingoloVersamento.Tributo }
     * 
     */
    public Versamento.SingoloVersamento.Tributo createVersamentoSingoloVersamentoTributo() {
        return new Versamento.SingoloVersamento.Tributo();
    }

    /**
     * Create an instance of {@link it.govpay.servizi.commons.Pagamento.Allegato }
     * 
     */
    public it.govpay.servizi.commons.Pagamento.Allegato createPagamentoAllegato() {
        return new it.govpay.servizi.commons.Pagamento.Allegato();
    }

    /**
     * Create an instance of {@link FlussoRendicontazione.Pagamento }
     * 
     */
    public FlussoRendicontazione.Pagamento createFlussoRendicontazionePagamento() {
        return new FlussoRendicontazione.Pagamento();
    }

    /**
     * Create an instance of {@link MetaInfo.IuvProp }
     * 
     */
    public MetaInfo.IuvProp createMetaInfoIuvProp() {
        return new MetaInfo.IuvProp();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/servizi/commons/", name = "metaInfo")
    public JAXBElement<MetaInfo> createMetaInfo(MetaInfo value) {
        return new JAXBElement<MetaInfo>(_MetaInfo_QNAME, MetaInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codApplicazione", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyCodApplicazione(String value) {
        return new JAXBElement<String>(_VersamentoKeyCodApplicazione_QNAME, String.class, VersamentoKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codVersamentoEnte", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyCodVersamentoEnte(String value) {
        return new JAXBElement<String>(_VersamentoKeyCodVersamentoEnte_QNAME, String.class, VersamentoKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codDominio", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyCodDominio(String value) {
        return new JAXBElement<String>(_VersamentoKeyCodDominio_QNAME, String.class, VersamentoKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "codUnivocoDebitore", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyCodUnivocoDebitore(String value) {
        return new JAXBElement<String>(_VersamentoKeyCodUnivocoDebitore_QNAME, String.class, VersamentoKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "bundlekey", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyBundlekey(String value) {
        return new JAXBElement<String>(_VersamentoKeyBundlekey_QNAME, String.class, VersamentoKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "iuv", scope = VersamentoKey.class)
    public JAXBElement<String> createVersamentoKeyIuv(String value) {
        return new JAXBElement<String>(_VersamentoKeyIuv_QNAME, String.class, VersamentoKey.class, value);
    }

}
