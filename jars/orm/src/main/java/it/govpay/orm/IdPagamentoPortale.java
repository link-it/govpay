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


/** <p>Java class for id-pagamento-portale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-pagamento-portale">
 * 		&lt;sequence>
 * 			&lt;element name="idSessione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="srcVersanteIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "id-pagamento-portale", 
  propOrder = {
  	"idSessione",
  	"idApplicazione",
  	"srcVersanteIdentificativo",
  	"dataRichiesta"
  }
)

@XmlRootElement(name = "id-pagamento-portale")

public class IdPagamentoPortale extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdPagamentoPortale() {
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

  public java.lang.String getIdSessione() {
    return this.idSessione;
  }

  public void setIdSessione(java.lang.String idSessione) {
    this.idSessione = idSessione;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getSrcVersanteIdentificativo() {
    return this.srcVersanteIdentificativo;
  }

  public void setSrcVersanteIdentificativo(java.lang.String srcVersanteIdentificativo) {
    this.srcVersanteIdentificativo = srcVersanteIdentificativo;
  }

  public java.util.Date getDataRichiesta() {
    return this.dataRichiesta;
  }

  public void setDataRichiesta(java.util.Date dataRichiesta) {
    this.dataRichiesta = dataRichiesta;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessione",required=true,nillable=false)
  protected java.lang.String idSessione;

  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="srcVersanteIdentificativo",required=false,nillable=false)
  protected java.lang.String srcVersanteIdentificativo;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRichiesta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRichiesta;

}
