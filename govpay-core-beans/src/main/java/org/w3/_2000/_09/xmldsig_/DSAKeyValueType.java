//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.10.10 alle 02:21:10 PM CEST 
//


package org.w3._2000._09.xmldsig_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DSAKeyValueType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DSAKeyValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="P" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           &lt;element name="Q" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         &lt;/sequence>
 *         &lt;element name="G" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
 *         &lt;element name="Y" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         &lt;element name="J" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="Seed" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           &lt;element name="PgenCounter" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DSAKeyValueType", propOrder = {
    "p",
    "q",
    "g",
    "y",
    "j",
    "seed",
    "pgenCounter"
})
public class DSAKeyValueType {

    @XmlElement(name = "P")
    protected byte[] p;
    @XmlElement(name = "Q")
    protected byte[] q;
    @XmlElement(name = "G")
    protected byte[] g;
    @XmlElement(name = "Y", required = true)
    protected byte[] y;
    @XmlElement(name = "J")
    protected byte[] j;
    @XmlElement(name = "Seed")
    protected byte[] seed;
    @XmlElement(name = "PgenCounter")
    protected byte[] pgenCounter;

    /**
     * Recupera il valore della proprietà p.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getP() {
        return p;
    }

    /**
     * Imposta il valore della proprietà p.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setP(byte[] value) {
        this.p = value;
    }

    /**
     * Recupera il valore della proprietà q.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getQ() {
        return q;
    }

    /**
     * Imposta il valore della proprietà q.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setQ(byte[] value) {
        this.q = value;
    }

    /**
     * Recupera il valore della proprietà g.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getG() {
        return g;
    }

    /**
     * Imposta il valore della proprietà g.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setG(byte[] value) {
        this.g = value;
    }

    /**
     * Recupera il valore della proprietà y.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getY() {
        return y;
    }

    /**
     * Imposta il valore della proprietà y.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setY(byte[] value) {
        this.y = value;
    }

    /**
     * Recupera il valore della proprietà j.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getJ() {
        return j;
    }

    /**
     * Imposta il valore della proprietà j.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setJ(byte[] value) {
        this.j = value;
    }

    /**
     * Recupera il valore della proprietà seed.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSeed() {
        return seed;
    }

    /**
     * Imposta il valore della proprietà seed.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSeed(byte[] value) {
        this.seed = value;
    }

    /**
     * Recupera il valore della proprietà pgenCounter.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPgenCounter() {
        return pgenCounter;
    }

    /**
     * Imposta il valore della proprietà pgenCounter.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPgenCounter(byte[] value) {
        this.pgenCounter = value;
    }

}
