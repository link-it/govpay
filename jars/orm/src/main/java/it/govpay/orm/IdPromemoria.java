/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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


/** <p>Java class for id-promemoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-promemoria">
 * 		&lt;sequence>
 * 			&lt;element name="idPromemoria" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "id-promemoria", 
  propOrder = {
  	"idPromemoria"
  }
)

@XmlRootElement(name = "id-promemoria")

public class IdPromemoria extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdPromemoria() {
	  //donothing
  }

  public Long getId() {
    if(this.id!=null) {
		return this.id;
    } else {
		return Long.valueOf(-1);
    }
  }

  public void setId(Long id) {
    if(id!=null) {
		this.id=id;
    } else {
		this.id=Long.valueOf(-1);
    }
  }

  public long getIdPromemoria() {
    return this.idPromemoria;
  }

  public void setIdPromemoria(long idPromemoria) {
    this.idPromemoria = idPromemoria;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idPromemoria",required=true,nillable=false)
  protected long idPromemoria;

}
