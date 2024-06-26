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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for id-anagrafica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-anagrafica">
 * 		&lt;sequence>
 * 			&lt;element name="idAnagrafica" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codUnivoco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "id-anagrafica", 
  propOrder = {
  	"idAnagrafica",
  	"codUnivoco"
  }
)

@XmlRootElement(name = "id-anagrafica")

public class IdAnagrafica extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdAnagrafica() {
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

  public long getIdAnagrafica() {
    return this.idAnagrafica;
  }

  public void setIdAnagrafica(long idAnagrafica) {
    this.idAnagrafica = idAnagrafica;
  }

  public java.lang.String getCodUnivoco() {
    return this.codUnivoco;
  }

  public void setCodUnivoco(java.lang.String codUnivoco) {
    this.codUnivoco = codUnivoco;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @jakarta.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idAnagrafica",required=true,nillable=false)
  protected long idAnagrafica;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codUnivoco",required=false,nillable=false)
  protected java.lang.String codUnivoco;

}
