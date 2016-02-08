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


/** <p>Java class for TracciatoXML complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TracciatoXML">
 * 		&lt;sequence>
 * 			&lt;element name="tipoTracciato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="xml" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "TracciatoXML", 
  propOrder = {
  	"tipoTracciato",
  	"codMessaggio",
  	"dataOraCreazione",
  	"xml"
  }
)

@XmlRootElement(name = "TracciatoXML")

public class TracciatoXML extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public TracciatoXML() {
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

  public java.lang.String getTipoTracciato() {
    return this.tipoTracciato;
  }

  public void setTipoTracciato(java.lang.String tipoTracciato) {
    this.tipoTracciato = tipoTracciato;
  }

  public java.lang.String getCodMessaggio() {
    return this.codMessaggio;
  }

  public void setCodMessaggio(java.lang.String codMessaggio) {
    this.codMessaggio = codMessaggio;
  }

  public java.util.Date getDataOraCreazione() {
    return this.dataOraCreazione;
  }

  public void setDataOraCreazione(java.util.Date dataOraCreazione) {
    this.dataOraCreazione = dataOraCreazione;
  }

  public byte[] getXml() {
    return this.xml;
  }

  public void setXml(byte[] xml) {
    this.xml = xml;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.TracciatoXMLModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.TracciatoXML.modelStaticInstance==null){
  			it.govpay.orm.TracciatoXML.modelStaticInstance = new it.govpay.orm.model.TracciatoXMLModel();
	  }
  }
  public static it.govpay.orm.model.TracciatoXMLModel model(){
	  if(it.govpay.orm.TracciatoXML.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.TracciatoXML.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoTracciato",required=true,nillable=false)
  protected java.lang.String tipoTracciato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMessaggio",required=true,nillable=false)
  protected java.lang.String codMessaggio;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraCreazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xml",required=true,nillable=false)
  protected byte[] xml;

}
