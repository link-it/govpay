
package it.govpay.servizi.commons;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
public class VersamentoKey {

    protected List<JAXBElement<String>> content;

    /**
     * Recupera il resto del modello di contenuto. 
     * 
     * <p>
     * Questa proprietà "catch-all" viene recuperata per il seguente motivo: 
     * Il nome di campo "CodDominio" è usato da due diverse parti di uno schema. Vedere: 
     * riga 187 di file:/home/bussu/NO_BACKUP/git/linkit-git/GovPay/govpay-core-beans/src/main/resources/wsdl/GpRnd_2.5.wsdl
     * riga 179 di file:/home/bussu/NO_BACKUP/git/linkit-git/GovPay/govpay-core-beans/src/main/resources/wsdl/GpRnd_2.5.wsdl
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
