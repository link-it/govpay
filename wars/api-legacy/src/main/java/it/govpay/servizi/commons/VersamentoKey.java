
package it.govpay.servizi.commons;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for versamentoKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versamentoKey"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="codApplicazione" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;choice&gt;
 *             &lt;element name="codVersamentoEnte" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *             &lt;sequence&gt;
 *               &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *               &lt;element name="codUnivocoDebitore" type="{http://www.govpay.it/servizi/commons/}string35" minOccurs="0"/&gt;
 *               &lt;element name="bundlekey" type="{http://www.govpay.it/servizi/commons/}string256"/&gt;
 *             &lt;/sequence&gt;
 *           &lt;/choice&gt;
 *           &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="codDominio" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *           &lt;element name="iuv" type="{http://www.govpay.it/servizi/commons/}cod35"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "versamentoKey", propOrder = {
    "content"
})
public class VersamentoKey {

    @XmlElementRefs({
        @XmlElementRef(name = "bundlekey", type = JAXBElement.class),
        @XmlElementRef(name = "codApplicazione", type = JAXBElement.class),
        @XmlElementRef(name = "iuv", type = JAXBElement.class),
        @XmlElementRef(name = "codUnivocoDebitore", type = JAXBElement.class),
        @XmlElementRef(name = "codVersamentoEnte", type = JAXBElement.class),
        @XmlElementRef(name = "codDominio", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "CodDominio" is used by two different parts of a schema. See: 
     * line 198 of file:/home/pintori/NO_BACKUP/git/govpay_tmp/govpay/govpay-core-beans/src/main/resources/wsdl/GpRnd_2.5.wsdl
     * line 190 of file:/home/pintori/NO_BACKUP/git/govpay_tmp/govpay/govpay-core-beans/src/main/resources/wsdl/GpRnd_2.5.wsdl
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
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
        if (content == null) {
            content = new ArrayList<JAXBElement<String>>();
        }
        return this.content;
    }

}
