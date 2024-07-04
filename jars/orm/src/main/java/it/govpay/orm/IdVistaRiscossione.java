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


/** <p>Java class for id-vista-riscossione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-vista-riscossione"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "id-vista-riscossione", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"indiceDati"
  }
)

@XmlRootElement(name = "id-vista-riscossione")

public class IdVistaRiscossione extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public IdVistaRiscossione() {
    super();
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.math.BigInteger getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(java.math.BigInteger indiceDati) {
    this.indiceDati = indiceDati;
  }

  private static final long serialVersionUID = 1L;



  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected java.math.BigInteger indiceDati;

}
