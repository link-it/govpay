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


/** <p>Java class for Carrello complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Carrello">
 * 		&lt;sequence>
 * 			&lt;element name="idRpt" type="{http://www.govpay.it/orm}id-rpt" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codCarrello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Carrello", 
  propOrder = {
  	"idRpt",
  	"codCarrello"
  }
)

@XmlRootElement(name = "Carrello")

public class Carrello extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Carrello() {
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

  public IdRpt getIdRpt() {
    return this.idRpt;
  }

  public void setIdRpt(IdRpt idRpt) {
    this.idRpt = idRpt;
  }

  public java.lang.String getCodCarrello() {
    return this.codCarrello;
  }

  public void setCodCarrello(java.lang.String codCarrello) {
    this.codCarrello = codCarrello;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.CarrelloModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Carrello.modelStaticInstance==null){
  			it.govpay.orm.Carrello.modelStaticInstance = new it.govpay.orm.model.CarrelloModel();
	  }
  }
  public static it.govpay.orm.model.CarrelloModel model(){
	  if(it.govpay.orm.Carrello.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Carrello.modelStaticInstance;
  }


  @XmlElement(name="idRpt",required=true,nillable=false)
  protected IdRpt idRpt;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCarrello",required=true,nillable=false)
  protected java.lang.String codCarrello;

}
