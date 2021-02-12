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
 * 			&lt;element name="gln" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ragioneSociale" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazioneDefault" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="auxDigit" type="{http://www.govpay.it/orm}int" minOccurs="1" maxOccurs="1" default="0"/>
 * 			&lt;element name="iuvPrefix" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="segregationCode" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="cbill" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="autStampaPoste" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codConnettoreMyPivot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codConnettoreSecim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
  	"idApplicazioneDefault",
  	"_decimalWrapper_auxDigit",
  	"iuvPrefix",
  	"_decimalWrapper_segregationCode",
  	"logo",
  	"cbill",
  	"autStampaPoste",
  	"codConnettoreMyPivot",
  	"codConnettoreSecim"
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

  public byte[] getLogo() {
    return this.logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public java.lang.String getCbill() {
    return this.cbill;
  }

  public void setCbill(java.lang.String cbill) {
    this.cbill = cbill;
  }

  public java.lang.String getAutStampaPoste() {
    return this.autStampaPoste;
  }

  public void setAutStampaPoste(java.lang.String autStampaPoste) {
    this.autStampaPoste = autStampaPoste;
  }

  public java.lang.String getCodConnettoreMyPivot() {
    return this.codConnettoreMyPivot;
  }

  public void setCodConnettoreMyPivot(java.lang.String codConnettoreMyPivot) {
    this.codConnettoreMyPivot = codConnettoreMyPivot;
  }

  public java.lang.String getCodConnettoreSecim() {
    return this.codConnettoreSecim;
  }

  public void setCodConnettoreSecim(java.lang.String codConnettoreSecim) {
    this.codConnettoreSecim = codConnettoreSecim;
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
  @XmlElement(name="gln",required=false,nillable=false)
  protected java.lang.String gln;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSociale",required=true,nillable=false)
  protected java.lang.String ragioneSociale;

  @XmlElement(name="idApplicazioneDefault",required=false,nillable=false)
  protected IdApplicazione idApplicazioneDefault;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="auxDigit",required=true,nillable=false,defaultValue="0")
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,  0);

  @javax.xml.bind.annotation.XmlTransient
  protected int auxDigit;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuvPrefix",required=false,nillable=false)
  protected java.lang.String iuvPrefix;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="segregationCode",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_segregationCode = null;

  @javax.xml.bind.annotation.XmlTransient
  protected java.lang.Integer segregationCode;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="logo",required=false,nillable=false)
  protected byte[] logo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cbill",required=false,nillable=false)
  protected java.lang.String cbill;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="autStampaPoste",required=false,nillable=false)
  protected java.lang.String autStampaPoste;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreMyPivot",required=false,nillable=false)
  protected java.lang.String codConnettoreMyPivot;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreSecim",required=false,nillable=false)
  protected java.lang.String codConnettoreSecim;

}
