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


/** <p>Java class for Audit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Audit"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idOggetto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="tipoOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="oggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
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

public class Audit extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Audit() {
    super();
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


  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @XmlElement(name="idOperatore",required=true,nillable=false)
  protected IdOperatore idOperatore;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idOggetto",required=true,nillable=false)
  protected long idOggetto;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoOggetto",required=true,nillable=false)
  protected java.lang.String tipoOggetto;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="oggetto",required=true,nillable=false)
  protected java.lang.String oggetto;

}
