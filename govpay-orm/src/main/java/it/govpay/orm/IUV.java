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


/** <p>Java class for IUV complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IUV">
 * 		&lt;sequence>
 * 			&lt;element name="prg" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="applicationCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataGenerazione" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoIuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="auxDigit" type="{http://www.govpay.it/orm}int" minOccurs="1" maxOccurs="1" default="0"/>
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
  	"prg",
  	"iuv",
  	"applicationCode",
  	"dataGenerazione",
  	"idApplicazione",
  	"tipoIuv",
  	"idDominio",
  	"codVersamentoEnte",
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

  public int getApplicationCode() {
    return this.applicationCode;
  }

  public void setApplicationCode(int applicationCode) {
    this.applicationCode = applicationCode;
  }

  public java.util.Date getDataGenerazione() {
    return this.dataGenerazione;
  }

  public void setDataGenerazione(java.util.Date dataGenerazione) {
    this.dataGenerazione = dataGenerazione;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getTipoIuv() {
    return this.tipoIuv;
  }

  public void setTipoIuv(java.lang.String tipoIuv) {
    this.tipoIuv = tipoIuv;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  public int getAuxDigit() {
    return (java.lang.Integer) this._decimalWrapper_auxDigit.getObject(java.lang.Integer.class);
  }

  public void setAuxDigit(int auxDigit) {
    this._decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,auxDigit);
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


  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="prg",required=true,nillable=false)
  protected long prg;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="applicationCode",required=true,nillable=false)
  protected int applicationCode;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataGenerazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataGenerazione;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoIuv",required=true,nillable=false)
  protected java.lang.String tipoIuv;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=false,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="auxDigit",required=true,nillable=false,defaultValue="0")
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_auxDigit = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,  0);

  @javax.xml.bind.annotation.XmlTransient
  protected int auxDigit;

}
