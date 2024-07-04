/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package it.govpay.orm;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for Tracciato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tracciato"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codTipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="formato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataCaricamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataCompletamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="beanDati" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="fileNameRichiesta" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="rawRichiesta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="fileNameEsito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="rawEsito" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="zipStampe" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/&gt;
 * 		&lt;/sequence&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Rev$, $Date$
 * 
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tracciato", 
  propOrder = {
  	"codDominio",
  	"codTipoVersamento",
  	"formato",
  	"tipo",
  	"stato",
  	"descrizioneStato",
  	"dataCaricamento",
  	"dataCompletamento",
  	"beanDati",
  	"fileNameRichiesta",
  	"rawRichiesta",
  	"fileNameEsito",
  	"rawEsito",
  	"idOperatore",
  	"zipStampe"
  }
)

@XmlRootElement(name = "Tracciato")

public class Tracciato extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Tracciato() {
    super();
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getCodTipoVersamento() {
    return this.codTipoVersamento;
  }

  public void setCodTipoVersamento(java.lang.String codTipoVersamento) {
    this.codTipoVersamento = codTipoVersamento;
  }

  public java.lang.String getFormato() {
    return this.formato;
  }

  public void setFormato(java.lang.String formato) {
    this.formato = formato;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public java.util.Date getDataCaricamento() {
    return this.dataCaricamento;
  }

  public void setDataCaricamento(java.util.Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  public java.util.Date getDataCompletamento() {
    return this.dataCompletamento;
  }

  public void setDataCompletamento(java.util.Date dataCompletamento) {
    this.dataCompletamento = dataCompletamento;
  }

  public java.lang.String getBeanDati() {
    return this.beanDati;
  }

  public void setBeanDati(java.lang.String beanDati) {
    this.beanDati = beanDati;
  }

  public java.lang.String getFileNameRichiesta() {
    return this.fileNameRichiesta;
  }

  public void setFileNameRichiesta(java.lang.String fileNameRichiesta) {
    this.fileNameRichiesta = fileNameRichiesta;
  }

  public byte[] getRawRichiesta() {
    return this.rawRichiesta;
  }

  public void setRawRichiesta(byte[] rawRichiesta) {
    this.rawRichiesta = rawRichiesta;
  }

  public java.lang.String getFileNameEsito() {
    return this.fileNameEsito;
  }

  public void setFileNameEsito(java.lang.String fileNameEsito) {
    this.fileNameEsito = fileNameEsito;
  }

  public byte[] getRawEsito() {
    return this.rawEsito;
  }

  public void setRawEsito(byte[] rawEsito) {
    this.rawEsito = rawEsito;
  }

  public IdOperatore getIdOperatore() {
    return this.idOperatore;
  }

  public void setIdOperatore(IdOperatore idOperatore) {
    this.idOperatore = idOperatore;
  }

  public byte[] getZipStampe() {
    return this.zipStampe;
  }

  public void setZipStampe(byte[] zipStampe) {
    this.zipStampe = zipStampe;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.TracciatoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Tracciato.modelStaticInstance==null){
  			it.govpay.orm.Tracciato.modelStaticInstance = new it.govpay.orm.model.TracciatoModel();
	  }
  }
  public static it.govpay.orm.model.TracciatoModel model(){
	  if(it.govpay.orm.Tracciato.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Tracciato.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTipoVersamento",required=false,nillable=false)
  protected java.lang.String codTipoVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="formato",required=true,nillable=false)
  protected java.lang.String formato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCaricamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCaricamento;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCompletamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCompletamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="beanDati",required=false,nillable=false)
  protected java.lang.String beanDati;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fileNameRichiesta",required=false,nillable=false)
  protected java.lang.String fileNameRichiesta;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawRichiesta",required=false,nillable=false)
  protected byte[] rawRichiesta;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fileNameEsito",required=false,nillable=false)
  protected java.lang.String fileNameEsito;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawEsito",required=false,nillable=false)
  protected byte[] rawEsito;

  @XmlElement(name="idOperatore",required=false,nillable=false)
  protected IdOperatore idOperatore;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="zipStampe",required=false,nillable=false)
  protected byte[] zipStampe;

}
