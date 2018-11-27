/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for RR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RR">
 * 		&lt;sequence>
 * 			&lt;element name="idRpt" type="{http://www.govpay.it/orm}id-rpt" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgRevoca" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataMsgRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataMsgEsito" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotaleRichiesto" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgEsito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotaleRevocato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="xmlRR" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="xmlER" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneRR" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneER" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 		&lt;/sequence>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$, $Date$
 * 
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RR", 
  propOrder = {
  	"idRpt",
  	"codDominio",
  	"iuv",
  	"ccp",
  	"codMsgRevoca",
  	"dataMsgRevoca",
  	"dataMsgEsito",
  	"stato",
  	"descrizioneStato",
  	"importoTotaleRichiesto",
  	"codMsgEsito",
  	"importoTotaleRevocato",
  	"xmlRR",
  	"xmlER",
  	"codTransazioneRR",
  	"codTransazioneER"
  }
)

@XmlRootElement(name = "RR")

public class RR extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public RR() {
  }

  public Long getId() {
    if(this.id!=null)
		return this.id;
	else
		return new Long(-1);
  }

  public void setId(Long id) {
    if(id!=null)
		this.id=id;
	else
		this.id=new Long(-1);
  }

  public IdRpt getIdRpt() {
    return this.idRpt;
  }

  public void setIdRpt(IdRpt idRpt) {
    this.idRpt = idRpt;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getCcp() {
    return this.ccp;
  }

  public void setCcp(java.lang.String ccp) {
    this.ccp = ccp;
  }

  public java.lang.String getCodMsgRevoca() {
    return this.codMsgRevoca;
  }

  public void setCodMsgRevoca(java.lang.String codMsgRevoca) {
    this.codMsgRevoca = codMsgRevoca;
  }

  public java.util.Date getDataMsgRevoca() {
    return this.dataMsgRevoca;
  }

  public void setDataMsgRevoca(java.util.Date dataMsgRevoca) {
    this.dataMsgRevoca = dataMsgRevoca;
  }

  public java.util.Date getDataMsgEsito() {
    return this.dataMsgEsito;
  }

  public void setDataMsgEsito(java.util.Date dataMsgEsito) {
    this.dataMsgEsito = dataMsgEsito;
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

  public double getImportoTotaleRichiesto() {
    return this.importoTotaleRichiesto;
  }

  public void setImportoTotaleRichiesto(double importoTotaleRichiesto) {
    this.importoTotaleRichiesto = importoTotaleRichiesto;
  }

  public java.lang.String getCodMsgEsito() {
    return this.codMsgEsito;
  }

  public void setCodMsgEsito(java.lang.String codMsgEsito) {
    this.codMsgEsito = codMsgEsito;
  }

  public java.lang.Double getImportoTotaleRevocato() {
    return this.importoTotaleRevocato;
  }

  public void setImportoTotaleRevocato(java.lang.Double importoTotaleRevocato) {
    this.importoTotaleRevocato = importoTotaleRevocato;
  }

  public byte[] getXmlRR() {
    return this.xmlRR;
  }

  public void setXmlRR(byte[] xmlRR) {
    this.xmlRR = xmlRR;
  }

  public byte[] getXmlER() {
    return this.xmlER;
  }

  public void setXmlER(byte[] xmlER) {
    this.xmlER = xmlER;
  }

  public java.lang.String getCodTransazioneRR() {
    return this.codTransazioneRR;
  }

  public void setCodTransazioneRR(java.lang.String codTransazioneRR) {
    this.codTransazioneRR = codTransazioneRR;
  }

  public java.lang.String getCodTransazioneER() {
    return this.codTransazioneER;
  }

  public void setCodTransazioneER(java.lang.String codTransazioneER) {
    this.codTransazioneER = codTransazioneER;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.RRModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.RR.modelStaticInstance==null){
  			it.govpay.orm.RR.modelStaticInstance = new it.govpay.orm.model.RRModel();
	  }
  }
  public static it.govpay.orm.model.RRModel model(){
	  if(it.govpay.orm.RR.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.RR.modelStaticInstance;
  }


  @XmlElement(name="idRpt",required=true,nillable=false)
  protected IdRpt idRpt;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ccp",required=true,nillable=false)
  protected java.lang.String ccp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRevoca",required=true,nillable=false)
  protected java.lang.String codMsgRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataMsgRevoca",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataMsgRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataMsgEsito",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataMsgEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotaleRichiesto",required=true,nillable=false)
  protected double importoTotaleRichiesto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgEsito",required=false,nillable=false)
  protected java.lang.String codMsgEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoTotaleRevocato",required=false,nillable=false)
  protected java.lang.Double importoTotaleRevocato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlRR",required=true,nillable=false)
  protected byte[] xmlRR;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlER",required=false,nillable=false)
  protected byte[] xmlER;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneRR",required=false,nillable=false)
  protected java.lang.String codTransazioneRR;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneER",required=false,nillable=false)
  protected java.lang.String codTransazioneER;

}
