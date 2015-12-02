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


/** <p>Java class for Evento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Evento">
 * 		&lt;sequence>
 * 			&lt;element name="dataOraEvento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codPsp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="componente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="categoriaEvento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoEvento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEvento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codFruitore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codErogatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codStazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="canalePagamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="altriParametri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
  	"dataOraEvento",
  	"codDominio",
  	"iuv",
  	"idApplicazione",
  	"ccp",
  	"codPsp",
  	"tipoVersamento",
  	"componente",
  	"categoriaEvento",
  	"tipoEvento",
  	"sottotipoEvento",
  	"codFruitore",
  	"codErogatore",
  	"codStazione",
  	"canalePagamento",
  	"altriParametri",
  	"esito"
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

  public java.util.Date getDataOraEvento() {
    return this.dataOraEvento;
  }

  public void setDataOraEvento(java.util.Date dataOraEvento) {
    this.dataOraEvento = dataOraEvento;
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

  public java.lang.Long getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(java.lang.Long idApplicazione) {
    this.idApplicazione = idApplicazione;
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

  public java.lang.String getCodFruitore() {
    return this.codFruitore;
  }

  public void setCodFruitore(java.lang.String codFruitore) {
    this.codFruitore = codFruitore;
  }

  public java.lang.String getCodErogatore() {
    return this.codErogatore;
  }

  public void setCodErogatore(java.lang.String codErogatore) {
    this.codErogatore = codErogatore;
  }

  public java.lang.String getCodStazione() {
    return this.codStazione;
  }

  public void setCodStazione(java.lang.String codStazione) {
    this.codStazione = codStazione;
  }

  public java.lang.String getCanalePagamento() {
    return this.canalePagamento;
  }

  public void setCanalePagamento(java.lang.String canalePagamento) {
    this.canalePagamento = canalePagamento;
  }

  public java.lang.String getAltriParametri() {
    return this.altriParametri;
  }

  public void setAltriParametri(java.lang.String altriParametri) {
    this.altriParametri = altriParametri;
  }

  public java.lang.String getEsito() {
    return this.esito;
  }

  public void setEsito(java.lang.String esito) {
    this.esito = esito;
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


  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraEvento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=false,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected java.lang.Long idApplicazione;

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
  @XmlElement(name="codFruitore",required=false,nillable=false)
  protected java.lang.String codFruitore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codErogatore",required=false,nillable=false)
  protected java.lang.String codErogatore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codStazione",required=false,nillable=false)
  protected java.lang.String codStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="canalePagamento",required=false,nillable=false)
  protected java.lang.String canalePagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="altriParametri",required=false,nillable=false)
  protected java.lang.String altriParametri;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esito",required=false,nillable=false)
  protected java.lang.String esito;

}
