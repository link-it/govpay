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


/** <p>Java class for Stazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Stazione">
 * 		&lt;sequence>
 * 			&lt;element name="idIntermediario" type="{http://www.govpay.it/orm}id-intermediario" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codStazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="password" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="applicationCode" type="{http://www.govpay.it/orm}integer" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ndpStato" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpOperazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpDescrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ndpData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Stazione", 
  propOrder = {
  	"idIntermediario",
  	"codStazione",
  	"password",
  	"abilitato",
  	"_decimalWrapper_applicationCode",
  	"ndpStato",
  	"ndpOperazione",
  	"ndpDescrizione",
  	"ndpData"
  }
)

@XmlRootElement(name = "Stazione")

public class Stazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Stazione() {
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

  public IdIntermediario getIdIntermediario() {
    return this.idIntermediario;
  }

  public void setIdIntermediario(IdIntermediario idIntermediario) {
    this.idIntermediario = idIntermediario;
  }

  public java.lang.String getCodStazione() {
    return this.codStazione;
  }

  public void setCodStazione(java.lang.String codStazione) {
    this.codStazione = codStazione;
  }

  public java.lang.String getPassword() {
    return this.password;
  }

  public void setPassword(java.lang.String password) {
    this.password = password;
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

  public java.lang.Integer getApplicationCode() {
    if(this._decimalWrapper_applicationCode!=null){
		return (java.lang.Integer) this._decimalWrapper_applicationCode.getObject(java.lang.Integer.class);
	}else{
		return this.applicationCode;
	}
  }

  public void setApplicationCode(java.lang.Integer applicationCode) {
    if(applicationCode!=null){
		this._decimalWrapper_applicationCode = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,2,applicationCode);
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

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.StazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Stazione.modelStaticInstance==null){
  			it.govpay.orm.Stazione.modelStaticInstance = new it.govpay.orm.model.StazioneModel();
	  }
  }
  public static it.govpay.orm.model.StazioneModel model(){
	  if(it.govpay.orm.Stazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Stazione.modelStaticInstance;
  }


  @XmlElement(name="idIntermediario",required=true,nillable=false)
  protected IdIntermediario idIntermediario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codStazione",required=true,nillable=false)
  protected java.lang.String codStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="password",required=true,nillable=false)
  protected java.lang.String password;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="applicationCode",required=true,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_applicationCode = null;

  @XmlTransient
  protected java.lang.Integer applicationCode;

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

}
