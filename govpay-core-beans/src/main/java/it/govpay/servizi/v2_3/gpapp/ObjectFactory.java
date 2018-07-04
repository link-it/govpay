
package it.govpay.servizi.v2_3.gpapp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import it.govpay.servizi.v2_3.commons.GpResponse;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.govpay.servizi.v2_3.gpapp package. 
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

    private final static QName _GpAnnullaVersamentoResponse_QNAME = new QName("http://www.govpay.it/servizi/v2_3/gpApp/", "gpAnnullaVersamentoResponse");
    private final static QName _GpNotificaPagamentoResponse_QNAME = new QName("http://www.govpay.it/servizi/v2_3/gpApp/", "gpNotificaPagamentoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.govpay.servizi.v2_3.gpapp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GpGeneraIuv }
     * 
     */
    public GpGeneraIuv createGpGeneraIuv() {
        return new GpGeneraIuv();
    }

    /**
     * Create an instance of {@link GpCaricaIuv }
     * 
     */
    public GpCaricaIuv createGpCaricaIuv() {
        return new GpCaricaIuv();
    }

    /**
     * Create an instance of {@link GpCaricaVersamento }
     * 
     */
    public GpCaricaVersamento createGpCaricaVersamento() {
        return new GpCaricaVersamento();
    }

    /**
     * Create an instance of {@link GpCaricaVersamentoResponse }
     * 
     */
    public GpCaricaVersamentoResponse createGpCaricaVersamentoResponse() {
        return new GpCaricaVersamentoResponse();
    }

    /**
     * Create an instance of {@link GpAnnullaVersamento }
     * 
     */
    public GpAnnullaVersamento createGpAnnullaVersamento() {
        return new GpAnnullaVersamento();
    }

    /**
     * Create an instance of {@link GpNotificaPagamento }
     * 
     */
    public GpNotificaPagamento createGpNotificaPagamento() {
        return new GpNotificaPagamento();
    }

    /**
     * Create an instance of {@link GpGeneraIuv.IuvRichiesto }
     * 
     */
    public GpGeneraIuv.IuvRichiesto createGpGeneraIuvIuvRichiesto() {
        return new GpGeneraIuv.IuvRichiesto();
    }

    /**
     * Create an instance of {@link GpGeneraIuvResponse }
     * 
     */
    public GpGeneraIuvResponse createGpGeneraIuvResponse() {
        return new GpGeneraIuvResponse();
    }

    /**
     * Create an instance of {@link GpCaricaIuv.IuvGenerato }
     * 
     */
    public GpCaricaIuv.IuvGenerato createGpCaricaIuvIuvGenerato() {
        return new GpCaricaIuv.IuvGenerato();
    }

    /**
     * Create an instance of {@link GpCaricaIuvResponse }
     * 
     */
    public GpCaricaIuvResponse createGpCaricaIuvResponse() {
        return new GpCaricaIuvResponse();
    }

    /**
     * Create an instance of {@link GpChiediStatoVersamento }
     * 
     */
    public GpChiediStatoVersamento createGpChiediStatoVersamento() {
        return new GpChiediStatoVersamento();
    }

    /**
     * Create an instance of {@link GpChiediStatoVersamentoResponse }
     * 
     */
    public GpChiediStatoVersamentoResponse createGpChiediStatoVersamentoResponse() {
        return new GpChiediStatoVersamentoResponse();
    }

    /**
     * Create an instance of {@link GpChiediListaFlussiRendicontazione }
     * 
     */
    public GpChiediListaFlussiRendicontazione createGpChiediListaFlussiRendicontazione() {
        return new GpChiediListaFlussiRendicontazione();
    }

    /**
     * Create an instance of {@link GpChiediListaFlussiRendicontazioneResponse }
     * 
     */
    public GpChiediListaFlussiRendicontazioneResponse createGpChiediListaFlussiRendicontazioneResponse() {
        return new GpChiediListaFlussiRendicontazioneResponse();
    }

    /**
     * Create an instance of {@link GpChiediFlussoRendicontazione }
     * 
     */
    public GpChiediFlussoRendicontazione createGpChiediFlussoRendicontazione() {
        return new GpChiediFlussoRendicontazione();
    }

    /**
     * Create an instance of {@link GpChiediFlussoRendicontazioneResponse }
     * 
     */
    public GpChiediFlussoRendicontazioneResponse createGpChiediFlussoRendicontazioneResponse() {
        return new GpChiediFlussoRendicontazioneResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/servizi/v2_3/gpApp/", name = "gpAnnullaVersamentoResponse")
    public JAXBElement<GpResponse> createGpAnnullaVersamentoResponse(GpResponse value) {
        return new JAXBElement<GpResponse>(_GpAnnullaVersamentoResponse_QNAME, GpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/servizi/v2_3/gpApp/", name = "gpNotificaPagamentoResponse")
    public JAXBElement<GpResponse> createGpNotificaPagamentoResponse(GpResponse value) {
        return new JAXBElement<GpResponse>(_GpNotificaPagamentoResponse_QNAME, GpResponse.class, null, value);
    }

}
