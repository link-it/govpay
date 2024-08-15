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


/** <p>Java class for TipoTributo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TipoTributo"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codTributo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tipoContabilita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codContabilita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "TipoTributo", 
  propOrder = {
  	"codTributo",
  	"descrizione",
  	"tipoContabilita",
  	"codContabilita"
  }
)

@XmlRootElement(name = "TipoTributo")

public class TipoTributo extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public TipoTributo() {
    super();
  }

  public java.lang.String getCodTributo() {
    return this.codTributo;
  }

  public void setCodTributo(java.lang.String codTributo) {
    this.codTributo = codTributo;
  }

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public java.lang.String getTipoContabilita() {
    return this.tipoContabilita;
  }

  public void setTipoContabilita(java.lang.String tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public java.lang.String getCodContabilita() {
    return this.codContabilita;
  }

  public void setCodContabilita(java.lang.String codContabilita) {
    this.codContabilita = codContabilita;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.TipoTributoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.TipoTributo.modelStaticInstance==null){
  			it.govpay.orm.TipoTributo.modelStaticInstance = new it.govpay.orm.model.TipoTributoModel();
	  }
  }
  public static it.govpay.orm.model.TipoTributoModel model(){
	  if(it.govpay.orm.TipoTributo.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.TipoTributo.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTributo",required=true,nillable=false)
  protected java.lang.String codTributo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=false,nillable=false)
  protected java.lang.String descrizione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoContabilita",required=false,nillable=false)
  protected java.lang.String tipoContabilita;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codContabilita",required=false,nillable=false)
  protected java.lang.String codContabilita;

}
