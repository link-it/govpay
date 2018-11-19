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


/** <p>Java class for EsitoAvvisatura complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EsitoAvvisatura">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="identificativoAvvisatura" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoCanale" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codEsito" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneEsito" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idTracciato" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "EsitoAvvisatura", 
  propOrder = {
  	"codDominio",
  	"identificativoAvvisatura",
  	"tipoCanale",
  	"codCanale",
  	"data",
  	"codEsito",
  	"descrizioneEsito",
  	"idTracciato"
  }
)

@XmlRootElement(name = "EsitoAvvisatura")

public class EsitoAvvisatura extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public EsitoAvvisatura() {
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

  public java.lang.String getIdentificativoAvvisatura() {
    return this.identificativoAvvisatura;
  }

  public void setIdentificativoAvvisatura(java.lang.String identificativoAvvisatura) {
    this.identificativoAvvisatura = identificativoAvvisatura;
  }

  public java.lang.Integer getTipoCanale() {
    return this.tipoCanale;
  }

  public void setTipoCanale(java.lang.Integer tipoCanale) {
    this.tipoCanale = tipoCanale;
  }

  public java.lang.String getCodCanale() {
    return this.codCanale;
  }

  public void setCodCanale(java.lang.String codCanale) {
    this.codCanale = codCanale;
  }

  public java.util.Date getData() {
    return this.data;
  }

  public void setData(java.util.Date data) {
    this.data = data;
  }

  public java.lang.Integer getCodEsito() {
    return this.codEsito;
  }

  public void setCodEsito(java.lang.Integer codEsito) {
    this.codEsito = codEsito;
  }

  public java.lang.String getDescrizioneEsito() {
    return this.descrizioneEsito;
  }

  public void setDescrizioneEsito(java.lang.String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
  }

  public IdTracciato getIdTracciato() {
    return this.idTracciato;
  }

  public void setIdTracciato(IdTracciato idTracciato) {
    this.idTracciato = idTracciato;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.EsitoAvvisaturaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.EsitoAvvisatura.modelStaticInstance==null){
  			it.govpay.orm.EsitoAvvisatura.modelStaticInstance = new it.govpay.orm.model.EsitoAvvisaturaModel();
	  }
  }
  public static it.govpay.orm.model.EsitoAvvisaturaModel model(){
	  if(it.govpay.orm.EsitoAvvisatura.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.EsitoAvvisatura.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="identificativoAvvisatura",required=true,nillable=false)
  protected java.lang.String identificativoAvvisatura;

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="tipoCanale",required=true,nillable=false)
  protected java.lang.Integer tipoCanale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCanale",required=false,nillable=false)
  protected java.lang.String codCanale;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="codEsito",required=true,nillable=false)
  protected java.lang.Integer codEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneEsito",required=true,nillable=false)
  protected java.lang.String descrizioneEsito;

  @XmlElement(name="idTracciato",required=true,nillable=false)
  protected IdTracciato idTracciato;

}
