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


/** <p>Java class for ContoAccredito complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContoAccredito">
 * 		&lt;sequence>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idFlusso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraPubblicazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraInizioValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "ContoAccredito", 
  propOrder = {
  	"idDominio",
  	"idFlusso",
  	"dataOraPubblicazione",
  	"dataOraInizioValidita",
  	"xml"
  }
)

@XmlRootElement(name = "ContoAccredito")

public class ContoAccredito extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public ContoAccredito() {
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

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public java.lang.String getIdFlusso() {
    return this.idFlusso;
  }

  public void setIdFlusso(java.lang.String idFlusso) {
    this.idFlusso = idFlusso;
  }

  public java.util.Date getDataOraPubblicazione() {
    return this.dataOraPubblicazione;
  }

  public void setDataOraPubblicazione(java.util.Date dataOraPubblicazione) {
    this.dataOraPubblicazione = dataOraPubblicazione;
  }

  public java.util.Date getDataOraInizioValidita() {
    return this.dataOraInizioValidita;
  }

  public void setDataOraInizioValidita(java.util.Date dataOraInizioValidita) {
    this.dataOraInizioValidita = dataOraInizioValidita;
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

  private static it.govpay.orm.model.ContoAccreditoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.ContoAccredito.modelStaticInstance==null){
  			it.govpay.orm.ContoAccredito.modelStaticInstance = new it.govpay.orm.model.ContoAccreditoModel();
	  }
  }
  public static it.govpay.orm.model.ContoAccreditoModel model(){
	  if(it.govpay.orm.ContoAccredito.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.ContoAccredito.modelStaticInstance;
  }


  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idFlusso",required=true,nillable=false)
  protected java.lang.String idFlusso;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraPubblicazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraPubblicazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraInizioValidita",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraInizioValidita;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xml",required=true,nillable=false)
  protected byte[] xml;

}
