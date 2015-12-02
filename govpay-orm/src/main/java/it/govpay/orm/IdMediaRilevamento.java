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


/** <p>Java class for id-media-rilevamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-media-rilevamento">
 * 		&lt;sequence>
 * 			&lt;element name="idSLA" type="{http://www.govpay.it/orm}id-sla" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOsservazione" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "id-media-rilevamento", 
  propOrder = {
  	"idSLA",
  	"dataOsservazione"
  }
)

@XmlRootElement(name = "id-media-rilevamento")

public class IdMediaRilevamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdMediaRilevamento() {
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

  public IdSla getIdSLA() {
    return this.idSLA;
  }

  public void setIdSLA(IdSla idSLA) {
    this.idSLA = idSLA;
  }

  public java.util.Date getDataOsservazione() {
    return this.dataOsservazione;
  }

  public void setDataOsservazione(java.util.Date dataOsservazione) {
    this.dataOsservazione = dataOsservazione;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @XmlElement(name="idSLA",required=true,nillable=false)
  protected IdSla idSLA;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataOsservazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOsservazione;

}
