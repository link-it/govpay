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


/** <p>Java class for id-pagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-pagamento">
 * 		&lt;sequence>
 * 			&lt;element name="idPagamento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "id-pagamento", 
  propOrder = {
  	"idPagamento",
  	"indiceDati",
  	"iuv"
  }
)

@XmlRootElement(name = "id-pagamento")

public class IdPagamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdPagamento() {
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

  public long getIdPagamento() {
    return this.idPagamento;
  }

  public void setIdPagamento(long idPagamento) {
    this.idPagamento = idPagamento;
  }

  public int getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(int indiceDati) {
    this.indiceDati = indiceDati;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idPagamento",required=true,nillable=false)
  protected long idPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected int indiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

}
