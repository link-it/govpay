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


/** <p>Java class for Batch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Batch">
 * 		&lt;sequence>
 * 			&lt;element name="codBatch" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nodo" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="inizio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="aggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Batch", 
  propOrder = {
  	"codBatch",
  	"nodo",
  	"inizio",
  	"aggiornamento"
  }
)

@XmlRootElement(name = "Batch")

public class Batch extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Batch() {
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

  public java.lang.String getCodBatch() {
    return this.codBatch;
  }

  public void setCodBatch(java.lang.String codBatch) {
    this.codBatch = codBatch;
  }

  public java.lang.String getNodo() {
    return this.nodo;
  }

  public void setNodo(java.lang.String nodo) {
    this.nodo = nodo;
  }

  public java.util.Date getInizio() {
    return this.inizio;
  }

  public void setInizio(java.util.Date inizio) {
    this.inizio = inizio;
  }

  public java.util.Date getAggiornamento() {
    return this.aggiornamento;
  }

  public void setAggiornamento(java.util.Date aggiornamento) {
    this.aggiornamento = aggiornamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.BatchModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Batch.modelStaticInstance==null){
  			it.govpay.orm.Batch.modelStaticInstance = new it.govpay.orm.model.BatchModel();
	  }
  }
  public static it.govpay.orm.model.BatchModel model(){
	  if(it.govpay.orm.Batch.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Batch.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codBatch",required=true,nillable=false)
  protected java.lang.String codBatch;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nodo",required=false,nillable=false)
  protected java.lang.String nodo;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="inizio",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date inizio;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="aggiornamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date aggiornamento;

}
