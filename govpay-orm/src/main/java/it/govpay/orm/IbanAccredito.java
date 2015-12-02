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


/** <p>Java class for IbanAccredito complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IbanAccredito">
 * 		&lt;sequence>
 * 			&lt;element name="codIban" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idSellerBank" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idNegozio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="bicAccredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ibanAppoggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="bicAppoggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="postale" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="attivato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "IbanAccredito", 
  propOrder = {
  	"codIban",
  	"idSellerBank",
  	"idNegozio",
  	"bicAccredito",
  	"ibanAppoggio",
  	"bicAppoggio",
  	"postale",
  	"attivato",
  	"abilitato",
  	"idDominio"
  }
)

@XmlRootElement(name = "IbanAccredito")

public class IbanAccredito extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IbanAccredito() {
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

  public java.lang.String getCodIban() {
    return this.codIban;
  }

  public void setCodIban(java.lang.String codIban) {
    this.codIban = codIban;
  }

  public java.lang.String getIdSellerBank() {
    return this.idSellerBank;
  }

  public void setIdSellerBank(java.lang.String idSellerBank) {
    this.idSellerBank = idSellerBank;
  }

  public java.lang.String getIdNegozio() {
    return this.idNegozio;
  }

  public void setIdNegozio(java.lang.String idNegozio) {
    this.idNegozio = idNegozio;
  }

  public java.lang.String getBicAccredito() {
    return this.bicAccredito;
  }

  public void setBicAccredito(java.lang.String bicAccredito) {
    this.bicAccredito = bicAccredito;
  }

  public java.lang.String getIbanAppoggio() {
    return this.ibanAppoggio;
  }

  public void setIbanAppoggio(java.lang.String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  public java.lang.String getBicAppoggio() {
    return this.bicAppoggio;
  }

  public void setBicAppoggio(java.lang.String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
  }

  public boolean isPostale() {
    return this.postale;
  }

  public boolean getPostale() {
    return this.postale;
  }

  public void setPostale(boolean postale) {
    this.postale = postale;
  }

  public boolean isAttivato() {
    return this.attivato;
  }

  public boolean getAttivato() {
    return this.attivato;
  }

  public void setAttivato(boolean attivato) {
    this.attivato = attivato;
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

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.IbanAccreditoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.IbanAccredito.modelStaticInstance==null){
  			it.govpay.orm.IbanAccredito.modelStaticInstance = new it.govpay.orm.model.IbanAccreditoModel();
	  }
  }
  public static it.govpay.orm.model.IbanAccreditoModel model(){
	  if(it.govpay.orm.IbanAccredito.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.IbanAccredito.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codIban",required=true,nillable=false)
  protected java.lang.String codIban;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSellerBank",required=false,nillable=false)
  protected java.lang.String idSellerBank;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idNegozio",required=false,nillable=false)
  protected java.lang.String idNegozio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="bicAccredito",required=false,nillable=false)
  protected java.lang.String bicAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ibanAppoggio",required=false,nillable=false)
  protected java.lang.String ibanAppoggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="bicAppoggio",required=false,nillable=false)
  protected java.lang.String bicAppoggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="postale",required=true,nillable=false)
  protected boolean postale;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="attivato",required=true,nillable=false)
  protected boolean attivato;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

}
