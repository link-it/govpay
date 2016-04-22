/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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


/** <p>Java class for Connettore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Connettore">
 * 		&lt;sequence>
 * 			&lt;element name="codConnettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codProprieta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="valore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Connettore", 
  propOrder = {
  	"codConnettore",
  	"codProprieta",
  	"valore"
  }
)

@XmlRootElement(name = "Connettore")

public class Connettore extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Connettore() {
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

  public java.lang.String getCodConnettore() {
    return this.codConnettore;
  }

  public void setCodConnettore(java.lang.String codConnettore) {
    this.codConnettore = codConnettore;
  }

  public java.lang.String getCodProprieta() {
    return this.codProprieta;
  }

  public void setCodProprieta(java.lang.String codProprieta) {
    this.codProprieta = codProprieta;
  }

  public java.lang.String getValore() {
    return this.valore;
  }

  public void setValore(java.lang.String valore) {
    this.valore = valore;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.ConnettoreModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Connettore.modelStaticInstance==null){
  			it.govpay.orm.Connettore.modelStaticInstance = new it.govpay.orm.model.ConnettoreModel();
	  }
  }
  public static it.govpay.orm.model.ConnettoreModel model(){
	  if(it.govpay.orm.Connettore.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Connettore.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettore",required=true,nillable=false)
  protected java.lang.String codConnettore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codProprieta",required=true,nillable=false)
  protected java.lang.String codProprieta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="valore",required=true,nillable=false)
  protected java.lang.String valore;

}
