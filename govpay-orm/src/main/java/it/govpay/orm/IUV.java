/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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


/** <p>Java class for IUV complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IUV">
 * 		&lt;sequence>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="prg" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataGenerazione" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="applicationCode" type="{http://www.govpay.it/orm}integer" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="auxDigit" type="{http://www.govpay.it/orm}integer" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "IUV", 
  propOrder = {
  	"idApplicazione",
  	"codDominio",
  	"prg",
  	"iuv",
  	"dataGenerazione",
  	"_decimalWrapper_applicationCode",
  	"_decimalWrapper_auxDigit"
  }
)

@XmlRootElement(name = "IUV")

public class IUV extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IUV() {
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

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public long getPrg() {
    return this.prg;
  }

  public void setPrg(long prg) {
    this.prg = prg;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.util.Date getDataGenerazione() {
    return this.dataGenerazione;
  }

  public void setDataGenerazione(java.util.Date dataGenerazione) {
    this.dataGenerazione = dataGenerazione;
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

  public java.lang.Integer getAuxDigit() {
    if(this._decimalWrapper_auxDigit!=null){
		return (java.lang.Integer) this._decimalWrapper_auxDigit.getObject(java.lang.Integer.class);
	}else{
		return this.auxDigit;
	}
  }

  public void setAuxDigit(java.lang.Integer auxDigit) {
    if(auxDigit!=null){
		this._decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,auxDigit);
	}
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.IUVModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.IUV.modelStaticInstance==null){
  			it.govpay.orm.IUV.modelStaticInstance = new it.govpay.orm.model.IUVModel();
	  }
  }
  public static it.govpay.orm.model.IUVModel model(){
	  if(it.govpay.orm.IUV.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.IUV.modelStaticInstance;
  }


  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="prg",required=true,nillable=false)
  protected long prg;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataGenerazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataGenerazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="applicationCode",required=true,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_applicationCode = null;

  @XmlTransient
  protected java.lang.Integer applicationCode;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="auxDigit",required=true,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_auxDigit = null;

  @XmlTransient
  protected java.lang.Integer auxDigit;

}
