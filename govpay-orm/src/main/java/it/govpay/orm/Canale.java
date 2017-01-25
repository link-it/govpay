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


/** <p>Java class for Canale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Canale">
 * 		&lt;sequence>
 * 			&lt;element name="idPsp" type="{http://www.govpay.it/orm}id-psp" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codIntermediario" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="modelloPagamento" type="{http://www.govpay.it/orm}integer" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="disponibilita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="descrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="condizioni" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="urlInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Canale", 
  propOrder = {
  	"idPsp",
  	"codCanale",
  	"codIntermediario",
  	"tipoVersamento",
  	"_decimalWrapper_modelloPagamento",
  	"disponibilita",
  	"descrizione",
  	"condizioni",
  	"urlInfo",
  	"abilitato"
  }
)

@XmlRootElement(name = "Canale")

public class Canale extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Canale() {
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

  public IdPsp getIdPsp() {
    return this.idPsp;
  }

  public void setIdPsp(IdPsp idPsp) {
    this.idPsp = idPsp;
  }

  public java.lang.String getCodCanale() {
    return this.codCanale;
  }

  public void setCodCanale(java.lang.String codCanale) {
    this.codCanale = codCanale;
  }

  public java.lang.String getCodIntermediario() {
    return this.codIntermediario;
  }

  public void setCodIntermediario(java.lang.String codIntermediario) {
    this.codIntermediario = codIntermediario;
  }

  public java.lang.String getTipoVersamento() {
    return this.tipoVersamento;
  }

  public void setTipoVersamento(java.lang.String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  public java.lang.Integer getModelloPagamento() {
    if(this._decimalWrapper_modelloPagamento!=null){
		return (java.lang.Integer) this._decimalWrapper_modelloPagamento.getObject(java.lang.Integer.class);
	}else{
		return this.modelloPagamento;
	}
  }

  public void setModelloPagamento(java.lang.Integer modelloPagamento) {
    if(modelloPagamento!=null){
		this._decimalWrapper_modelloPagamento = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,2,modelloPagamento);
	}
  }

  public java.lang.String getDisponibilita() {
    return this.disponibilita;
  }

  public void setDisponibilita(java.lang.String disponibilita) {
    this.disponibilita = disponibilita;
  }

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public java.lang.String getCondizioni() {
    return this.condizioni;
  }

  public void setCondizioni(java.lang.String condizioni) {
    this.condizioni = condizioni;
  }

  public java.lang.String getUrlInfo() {
    return this.urlInfo;
  }

  public void setUrlInfo(java.lang.String urlInfo) {
    this.urlInfo = urlInfo;
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

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.CanaleModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Canale.modelStaticInstance==null){
  			it.govpay.orm.Canale.modelStaticInstance = new it.govpay.orm.model.CanaleModel();
	  }
  }
  public static it.govpay.orm.model.CanaleModel model(){
	  if(it.govpay.orm.Canale.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Canale.modelStaticInstance;
  }


  @XmlElement(name="idPsp",required=true,nillable=false)
  protected IdPsp idPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCanale",required=true,nillable=false)
  protected java.lang.String codCanale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codIntermediario",required=true,nillable=false)
  protected java.lang.String codIntermediario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoVersamento",required=true,nillable=false)
  protected java.lang.String tipoVersamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="modelloPagamento",required=true,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_modelloPagamento = null;

  @XmlTransient
  protected java.lang.Integer modelloPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="disponibilita",required=false,nillable=false)
  protected java.lang.String disponibilita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=false,nillable=false)
  protected java.lang.String descrizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="condizioni",required=false,nillable=false)
  protected java.lang.String condizioni;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="urlInfo",required=false,nillable=false)
  protected java.lang.String urlInfo;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

}
