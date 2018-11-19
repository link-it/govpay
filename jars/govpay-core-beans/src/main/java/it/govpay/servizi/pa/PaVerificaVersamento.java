
package it.govpay.servizi.pa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *             &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="bundlekey" type="{http://www.govpay.it/servizi/commons/}string256"/&gt;
 *             &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *             &lt;element name="codUnivocoDebitore" type="{http://www.govpay.it/servizi/commons/}string35" minOccurs="0"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "paVerificaVersamento")
public class PaVerificaVersamento {

    @XmlElementRefs({
        @XmlElementRef(name = "codVersamentoEnte", type = JAXBElement.class),
        @XmlElementRef(name = "iuv", type = JAXBElement.class),
        @XmlElementRef(name = "bundlekey", type = JAXBElement.class),
        @XmlElementRef(name = "codApplicazione", type = JAXBElement.class),
        @XmlElementRef(name = "codDominio", type = JAXBElement.class),
        @XmlElementRef(name = "codUnivocoDebitore", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> content;

    /**
     * Recupera il resto del modello di contenuto. 
     * 
     * <p>
     * Questa proprietà "catch-all" viene recuperata per il seguente motivo: 
     * Il nome di campo "CodDominio" è usato da due diverse parti di uno schema. Vedere: 
     * riga 704 di file:/home/bussu/NO_BACKUP/git/linkit-git/GovPay/govpay-core-beans/src/main/resources/wsdl/GpPa.wsdl
     * riga 699 di file:/home/bussu/NO_BACKUP/git/linkit-git/GovPay/govpay-core-beans/src/main/resources/wsdl/GpPa.wsdl
     * <p>
     * Per eliminare questa proprietà, applicare una personalizzazione della proprietà a una 
     * delle seguenti due dichiarazioni per modificarne il nome: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getContent() {
        if (this.content == null) {
            this.content = new ArrayList<>();
        }
        return this.content;
    }

}
