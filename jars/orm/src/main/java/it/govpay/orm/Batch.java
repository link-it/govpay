/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for Batch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Batch"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codBatch" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="nodo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="inizio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="aggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 		&lt;/sequence&gt;
 * &lt;/complexType&gt;
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

public class Batch extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Batch() {
    super();
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


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codBatch",required=true,nillable=false)
  protected java.lang.String codBatch;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nodo",required=false,nillable=false)
  protected java.lang.String nodo;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="inizio",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date inizio;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="aggiornamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date aggiornamento;

}
