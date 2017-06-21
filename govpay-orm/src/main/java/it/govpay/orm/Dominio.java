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


/** <p>Java class for Dominio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dominio">
 * 		&lt;sequence>
 * 			&lt;element name="idStazione" type="{http://www.govpay.it/orm}id-stazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="gln" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ragioneSociale" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="xmlContiAccredito" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="xmlTabellaControparti" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="riusoIUV" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="customIUV" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazioneDefault" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="auxDigit" type="{http://www.govpay.it/orm}int" minOccurs="1" maxOccurs="1" default="0"/>
 * 			&lt;element name="iuvPrefix" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuvPrefixStrict" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="segregationCode" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpStato" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpOperazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpDescrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Dominio", 
  propOrder = {
  	"idStazione",
  	"codDominio",
  	"gln",
  	"abilitato",
  	"ragioneSociale",
  	"xmlContiAccredito",
  	"xmlTabellaControparti",
  	"riusoIUV",
  	"customIUV",
  	"idApplicazioneDefault",
  	"_decimalWrapper_auxDigit",
  	"iuvPrefix",
  	"iuvPrefixStrict",
  	"_decimalWrapper_segregationCode",
  	"ndpStato",
  	"ndpOperazione",
  	"ndpDescrizione",
  	"ndpData",
  	"logo"
  }
)

@XmlRootElement(name = "Dominio")

public class Dominio extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Dominio() {
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

  public IdStazione getIdStazione() {
    return this.idStazione;
  }

  public void setIdStazione(IdStazione idStazione) {
    this.idStazione = idStazione;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getGln() {
    return this.gln;
  }

  public void setGln(java.lang.String gln) {
    this.gln = gln;
  }

  public boolean isAbilitato() {
    return this.abilitato;
  }

  public boolean getAbilitato() {
    return this.abilitato;
  }

  public void setAbilitato(boolean abilitato) {
    this.abilitato = abilitato;
  }

  public java.lang.String getRagioneSociale() {
    return this.ragioneSociale;
  }

  public void setRagioneSociale(java.lang.String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public byte[] getXmlContiAccredito() {
    return this.xmlContiAccredito;
  }

  public void setXmlContiAccredito(byte[] xmlContiAccredito) {
    this.xmlContiAccredito = xmlContiAccredito;
  }

  public byte[] getXmlTabellaControparti() {
    return this.xmlTabellaControparti;
  }

  public void setXmlTabellaControparti(byte[] xmlTabellaControparti) {
    this.xmlTabellaControparti = xmlTabellaControparti;
  }

  public boolean isRiusoIUV() {
    return this.riusoIUV;
  }

  public boolean getRiusoIUV() {
    return this.riusoIUV;
  }

  public void setRiusoIUV(boolean riusoIUV) {
    this.riusoIUV = riusoIUV;
  }

  public boolean isCustomIUV() {
    return this.customIUV;
  }

  public boolean getCustomIUV() {
    return this.customIUV;
  }

  public void setCustomIUV(boolean customIUV) {
    this.customIUV = customIUV;
  }

  public IdApplicazione getIdApplicazioneDefault() {
    return this.idApplicazioneDefault;
  }

  public void setIdApplicazioneDefault(IdApplicazione idApplicazioneDefault) {
    this.idApplicazioneDefault = idApplicazioneDefault;
  }

  public int getAuxDigit() {
    return (java.lang.Integer) this._decimalWrapper_auxDigit.getObject(java.lang.Integer.class);
  }

  public void setAuxDigit(int auxDigit) {
    this._decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,auxDigit);
  }

  public java.lang.String getIuvPrefix() {
    return this.iuvPrefix;
  }

  public void setIuvPrefix(java.lang.String iuvPrefix) {
    this.iuvPrefix = iuvPrefix;
  }

  public boolean isIuvPrefixStrict() {
    return this.iuvPrefixStrict;
  }

  public boolean getIuvPrefixStrict() {
    return this.iuvPrefixStrict;
  }

  public void setIuvPrefixStrict(boolean iuvPrefixStrict) {
    this.iuvPrefixStrict = iuvPrefixStrict;
  }

  public java.lang.Integer getSegregationCode() {
    if(this._decimalWrapper_segregationCode!=null){
		return (java.lang.Integer) this._decimalWrapper_segregationCode.getObject(java.lang.Integer.class);
	}else{
		return this.segregationCode;
	}
  }

  public void setSegregationCode(java.lang.Integer segregationCode) {
    if(segregationCode!=null){
		this._decimalWrapper_segregationCode = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,2,segregationCode);
	}
  }

  public java.lang.Integer getNdpStato() {
    return this.ndpStato;
  }

  public void setNdpStato(java.lang.Integer ndpStato) {
    this.ndpStato = ndpStato;
  }

  public java.lang.String getNdpOperazione() {
    return this.ndpOperazione;
  }

  public void setNdpOperazione(java.lang.String ndpOperazione) {
    this.ndpOperazione = ndpOperazione;
  }

  public java.lang.String getNdpDescrizione() {
    return this.ndpDescrizione;
  }

  public void setNdpDescrizione(java.lang.String ndpDescrizione) {
    this.ndpDescrizione = ndpDescrizione;
  }

  public java.util.Date getNdpData() {
    return this.ndpData;
  }

  public void setNdpData(java.util.Date ndpData) {
    this.ndpData = ndpData;
  }

  public byte[] getLogo() {
    return this.logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.DominioModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Dominio.modelStaticInstance==null){
  			it.govpay.orm.Dominio.modelStaticInstance = new it.govpay.orm.model.DominioModel();
	  }
  }
  public static it.govpay.orm.model.DominioModel model(){
	  if(it.govpay.orm.Dominio.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Dominio.modelStaticInstance;
  }


  @XmlElement(name="idStazione",required=true,nillable=false)
  protected IdStazione idStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="gln",required=true,nillable=false)
  protected java.lang.String gln;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSociale",required=true,nillable=false)
  protected java.lang.String ragioneSociale;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlContiAccredito",required=true,nillable=false)
  protected byte[] xmlContiAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlTabellaControparti",required=true,nillable=false)
  protected byte[] xmlTabellaControparti;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="riusoIUV",required=true,nillable=false)
  protected boolean riusoIUV;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="customIUV",required=true,nillable=false)
  protected boolean customIUV;

  @XmlElement(name="idApplicazioneDefault",required=false,nillable=false)
  protected IdApplicazione idApplicazioneDefault;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="auxDigit",required=true,nillable=false,defaultValue="0")
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,  0);

  @XmlTransient
  protected int auxDigit;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuvPrefix",required=false,nillable=false)
  protected java.lang.String iuvPrefix;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="iuvPrefixStrict",required=true,nillable=false,defaultValue="false")
  protected boolean iuvPrefixStrict = false;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="segregationCode",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_segregationCode = null;

  @XmlTransient
  protected java.lang.Integer segregationCode;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="ndpStato",required=false,nillable=false)
  protected java.lang.Integer ndpStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ndpOperazione",required=false,nillable=false)
  protected java.lang.String ndpOperazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ndpDescrizione",required=false,nillable=false)
  protected java.lang.String ndpDescrizione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="ndpData",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date ndpData;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="logo",required=false,nillable=false)
  protected byte[] logo;

}
