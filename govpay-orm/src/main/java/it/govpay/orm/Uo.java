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


/** <p>Java class for Uo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Uo">
 * 		&lt;sequence>
 * 			&lt;element name="codUo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="uoCodiceIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoDenominazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoIndirizzo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoCivico" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoCap" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoLocalita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoProvincia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoNazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoArea" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoUrlSitoWeb" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoEmail" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uoPec" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Uo", 
  propOrder = {
  	"codUo",
  	"idDominio",
  	"abilitato",
  	"uoCodiceIdentificativo",
  	"uoDenominazione",
  	"uoIndirizzo",
  	"uoCivico",
  	"uoCap",
  	"uoLocalita",
  	"uoProvincia",
  	"uoNazione",
  	"uoArea",
  	"uoUrlSitoWeb",
  	"uoEmail",
  	"uoPec"
  }
)

@XmlRootElement(name = "Uo")

public class Uo extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Uo() {
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

  public java.lang.String getCodUo() {
    return this.codUo;
  }

  public void setCodUo(java.lang.String codUo) {
    this.codUo = codUo;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
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

  public java.lang.String getUoCodiceIdentificativo() {
    return this.uoCodiceIdentificativo;
  }

  public void setUoCodiceIdentificativo(java.lang.String uoCodiceIdentificativo) {
    this.uoCodiceIdentificativo = uoCodiceIdentificativo;
  }

  public java.lang.String getUoDenominazione() {
    return this.uoDenominazione;
  }

  public void setUoDenominazione(java.lang.String uoDenominazione) {
    this.uoDenominazione = uoDenominazione;
  }

  public java.lang.String getUoIndirizzo() {
    return this.uoIndirizzo;
  }

  public void setUoIndirizzo(java.lang.String uoIndirizzo) {
    this.uoIndirizzo = uoIndirizzo;
  }

  public java.lang.String getUoCivico() {
    return this.uoCivico;
  }

  public void setUoCivico(java.lang.String uoCivico) {
    this.uoCivico = uoCivico;
  }

  public java.lang.String getUoCap() {
    return this.uoCap;
  }

  public void setUoCap(java.lang.String uoCap) {
    this.uoCap = uoCap;
  }

  public java.lang.String getUoLocalita() {
    return this.uoLocalita;
  }

  public void setUoLocalita(java.lang.String uoLocalita) {
    this.uoLocalita = uoLocalita;
  }

  public java.lang.String getUoProvincia() {
    return this.uoProvincia;
  }

  public void setUoProvincia(java.lang.String uoProvincia) {
    this.uoProvincia = uoProvincia;
  }

  public java.lang.String getUoNazione() {
    return this.uoNazione;
  }

  public void setUoNazione(java.lang.String uoNazione) {
    this.uoNazione = uoNazione;
  }

  public java.lang.String getUoArea() {
    return this.uoArea;
  }

  public void setUoArea(java.lang.String uoArea) {
    this.uoArea = uoArea;
  }

  public java.lang.String getUoUrlSitoWeb() {
    return this.uoUrlSitoWeb;
  }

  public void setUoUrlSitoWeb(java.lang.String uoUrlSitoWeb) {
    this.uoUrlSitoWeb = uoUrlSitoWeb;
  }

  public java.lang.String getUoEmail() {
    return this.uoEmail;
  }

  public void setUoEmail(java.lang.String uoEmail) {
    this.uoEmail = uoEmail;
  }

  public java.lang.String getUoPec() {
    return this.uoPec;
  }

  public void setUoPec(java.lang.String uoPec) {
    this.uoPec = uoPec;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.UoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Uo.modelStaticInstance==null){
  			it.govpay.orm.Uo.modelStaticInstance = new it.govpay.orm.model.UoModel();
	  }
  }
  public static it.govpay.orm.model.UoModel model(){
	  if(it.govpay.orm.Uo.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Uo.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codUo",required=true,nillable=false)
  protected java.lang.String codUo;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoCodiceIdentificativo",required=false,nillable=false)
  protected java.lang.String uoCodiceIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoDenominazione",required=false,nillable=false)
  protected java.lang.String uoDenominazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoIndirizzo",required=false,nillable=false)
  protected java.lang.String uoIndirizzo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoCivico",required=false,nillable=false)
  protected java.lang.String uoCivico;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoCap",required=false,nillable=false)
  protected java.lang.String uoCap;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoLocalita",required=false,nillable=false)
  protected java.lang.String uoLocalita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoProvincia",required=false,nillable=false)
  protected java.lang.String uoProvincia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoNazione",required=false,nillable=false)
  protected java.lang.String uoNazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoArea",required=false,nillable=false)
  protected java.lang.String uoArea;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoUrlSitoWeb",required=false,nillable=false)
  protected java.lang.String uoUrlSitoWeb;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoEmail",required=false,nillable=false)
  protected java.lang.String uoEmail;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uoPec",required=false,nillable=false)
  protected java.lang.String uoPec;

}
