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


/** <p>Java class for Audit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Audit">
 * 		&lt;sequence>
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idOggetto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="oggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Audit", 
  propOrder = {
  	"data",
  	"idOperatore",
  	"idOggetto",
  	"tipoOggetto",
  	"oggetto"
  }
)

@XmlRootElement(name = "Audit")

public class Audit extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Audit() {
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

  public java.util.Date getData() {
    return this.data;
  }

  public void setData(java.util.Date data) {
    this.data = data;
  }

  public IdOperatore getIdOperatore() {
    return this.idOperatore;
  }

  public void setIdOperatore(IdOperatore idOperatore) {
    this.idOperatore = idOperatore;
  }

  public long getIdOggetto() {
    return this.idOggetto;
  }

  public void setIdOggetto(long idOggetto) {
    this.idOggetto = idOggetto;
  }

  public java.lang.String getTipoOggetto() {
    return this.tipoOggetto;
  }

  public void setTipoOggetto(java.lang.String tipoOggetto) {
    this.tipoOggetto = tipoOggetto;
  }

  public java.lang.String getOggetto() {
    return this.oggetto;
  }

  public void setOggetto(java.lang.String oggetto) {
    this.oggetto = oggetto;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.AuditModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Audit.modelStaticInstance==null){
  			it.govpay.orm.Audit.modelStaticInstance = new it.govpay.orm.model.AuditModel();
	  }
  }
  public static it.govpay.orm.model.AuditModel model(){
	  if(it.govpay.orm.Audit.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Audit.modelStaticInstance;
  }


  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @XmlElement(name="idOperatore",required=true,nillable=false)
  protected IdOperatore idOperatore;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idOggetto",required=true,nillable=false)
  protected long idOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoOggetto",required=true,nillable=false)
  protected java.lang.String tipoOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="oggetto",required=true,nillable=false)
  protected java.lang.String oggetto;

}
