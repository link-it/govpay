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


/** <p>Java class for Evento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Evento">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="componente" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="categoriaEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="erogatore" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="fruitore" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codStazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametri1" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametri2" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="data1" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="data2" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Evento", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"ccp",
  	"codPsp",
  	"tipoVersamento",
  	"componente",
  	"categoriaEvento",
  	"tipoEvento",
  	"sottotipoEvento",
  	"erogatore",
  	"fruitore",
  	"codStazione",
  	"codCanale",
  	"parametri1",
  	"parametri2",
  	"esito",
  	"data1",
  	"data2",
  	"idVersamento"
  }
)

@XmlRootElement(name = "Evento")

public class Evento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Evento() {
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

  public java.lang.String getCodPsp() {
    return this.codPsp;
  }

  public void setCodPsp(java.lang.String codPsp) {
    this.codPsp = codPsp;
  }

  public java.lang.String getTipoVersamento() {
    return this.tipoVersamento;
  }

  public void setTipoVersamento(java.lang.String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  public java.lang.String getComponente() {
    return this.componente;
  }

  public void setComponente(java.lang.String componente) {
    this.componente = componente;
  }

  public java.lang.String getCategoriaEvento() {
    return this.categoriaEvento;
  }

  public void setCategoriaEvento(java.lang.String categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
  }

  public java.lang.String getTipoEvento() {
    return this.tipoEvento;
  }

  public void setTipoEvento(java.lang.String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  public java.lang.String getSottotipoEvento() {
    return this.sottotipoEvento;
  }

  public void setSottotipoEvento(java.lang.String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
  }

  public java.lang.String getErogatore() {
    return this.erogatore;
  }

  public void setErogatore(java.lang.String erogatore) {
    this.erogatore = erogatore;
  }

  public java.lang.String getFruitore() {
    return this.fruitore;
  }

  public void setFruitore(java.lang.String fruitore) {
    this.fruitore = fruitore;
  }

  public java.lang.String getCodStazione() {
    return this.codStazione;
  }

  public void setCodStazione(java.lang.String codStazione) {
    this.codStazione = codStazione;
  }

  public java.lang.String getCodCanale() {
    return this.codCanale;
  }

  public void setCodCanale(java.lang.String codCanale) {
    this.codCanale = codCanale;
  }

  public java.lang.String getParametri1() {
    return this.parametri1;
  }

  public void setParametri1(java.lang.String parametri1) {
    this.parametri1 = parametri1;
  }

  public java.lang.String getParametri2() {
    return this.parametri2;
  }

  public void setParametri2(java.lang.String parametri2) {
    this.parametri2 = parametri2;
  }

  public java.lang.String getEsito() {
    return this.esito;
  }

  public void setEsito(java.lang.String esito) {
    this.esito = esito;
  }

  public java.util.Date getData1() {
    return this.data1;
  }

  public void setData1(java.util.Date data1) {
    this.data1 = data1;
  }

  public java.util.Date getData2() {
    return this.data2;
  }

  public void setData2(java.util.Date data2) {
    this.data2 = data2;
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.EventoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Evento.modelStaticInstance==null){
  			it.govpay.orm.Evento.modelStaticInstance = new it.govpay.orm.model.EventoModel();
	  }
  }
  public static it.govpay.orm.model.EventoModel model(){
	  if(it.govpay.orm.Evento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Evento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=false,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ccp",required=false,nillable=false)
  protected java.lang.String ccp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=false,nillable=false)
  protected java.lang.String codPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoVersamento",required=false,nillable=false)
  protected java.lang.String tipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="componente",required=false,nillable=false)
  protected java.lang.String componente;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="categoriaEvento",required=false,nillable=false)
  protected java.lang.String categoriaEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoEvento",required=false,nillable=false)
  protected java.lang.String tipoEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sottotipoEvento",required=false,nillable=false)
  protected java.lang.String sottotipoEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="erogatore",required=false,nillable=false)
  protected java.lang.String erogatore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fruitore",required=false,nillable=false)
  protected java.lang.String fruitore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codStazione",required=false,nillable=false)
  protected java.lang.String codStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCanale",required=false,nillable=false)
  protected java.lang.String codCanale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="parametri1",required=false,nillable=false)
  protected java.lang.String parametri1;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="parametri2",required=false,nillable=false)
  protected java.lang.String parametri2;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esito",required=false,nillable=false)
  protected java.lang.String esito;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data1",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date data1;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data2",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date data2;

  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

}
