/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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


/** <p>Java class for id-singola-revoca complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-singola-revoca"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="id-singola-revoca" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "id-singola-revoca", 
  propOrder = {
  	"idSingolaRevoca"
  }
)

@XmlRootElement(name = "id-singola-revoca")

public class IdSingolaRevoca extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public IdSingolaRevoca() {
    super();
  }

  public long getIdSingolaRevoca() {
    return this.idSingolaRevoca;
  }

  public void setIdSingolaRevoca(long idSingolaRevoca) {
    this.idSingolaRevoca = idSingolaRevoca;
  }

  private static final long serialVersionUID = 1L;



  @jakarta.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="id-singola-revoca",required=true,nillable=false)
  protected long idSingolaRevoca;

}
