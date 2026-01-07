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


/** <p>Java class for ACL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ACL"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="ruolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idUtenza" type="{http://www.govpay.it/orm}id-utenza" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="servizio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="diritti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "ACL", 
  propOrder = {
  	"ruolo",
  	"idUtenza",
  	"servizio",
  	"diritti"
  }
)

@XmlRootElement(name = "ACL")

public class ACL extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public ACL() {
    super();
  }

  public java.lang.String getRuolo() {
    return this.ruolo;
  }

  public void setRuolo(java.lang.String ruolo) {
    this.ruolo = ruolo;
  }

  public IdUtenza getIdUtenza() {
    return this.idUtenza;
  }

  public void setIdUtenza(IdUtenza idUtenza) {
    this.idUtenza = idUtenza;
  }

  public java.lang.String getServizio() {
    return this.servizio;
  }

  public void setServizio(java.lang.String servizio) {
    this.servizio = servizio;
  }

  public java.lang.String getDiritti() {
    return this.diritti;
  }

  public void setDiritti(java.lang.String diritti) {
    this.diritti = diritti;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.ACLModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.ACL.modelStaticInstance==null){
  			it.govpay.orm.ACL.modelStaticInstance = new it.govpay.orm.model.ACLModel();
	  }
  }
  public static it.govpay.orm.model.ACLModel model(){
	  if(it.govpay.orm.ACL.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.ACL.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ruolo",required=false,nillable=false)
  protected java.lang.String ruolo;

  @XmlElement(name="idUtenza",required=false,nillable=false)
  protected IdUtenza idUtenza;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="servizio",required=true,nillable=false)
  protected java.lang.String servizio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="diritti",required=true,nillable=false)
  protected java.lang.String diritti;

}
