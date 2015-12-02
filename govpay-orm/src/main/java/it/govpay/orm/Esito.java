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


/** <p>Java class for Esito complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Esito">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="statoSpedizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dettaglioSpedizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tentativiSpedizione" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraUltimaSpedizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraProssimaSpedizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Esito", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"idApplicazione",
  	"statoSpedizione",
  	"dettaglioSpedizione",
  	"tentativiSpedizione",
  	"dataOraCreazione",
  	"dataOraUltimaSpedizione",
  	"dataOraProssimaSpedizione",
  	"xml"
  }
)

@XmlRootElement(name = "Esito")

public class Esito extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Esito() {
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

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getStatoSpedizione() {
    return this.statoSpedizione;
  }

  public void setStatoSpedizione(java.lang.String statoSpedizione) {
    this.statoSpedizione = statoSpedizione;
  }

  public java.lang.String getDettaglioSpedizione() {
    return this.dettaglioSpedizione;
  }

  public void setDettaglioSpedizione(java.lang.String dettaglioSpedizione) {
    this.dettaglioSpedizione = dettaglioSpedizione;
  }

  public java.lang.Long getTentativiSpedizione() {
    return this.tentativiSpedizione;
  }

  public void setTentativiSpedizione(java.lang.Long tentativiSpedizione) {
    this.tentativiSpedizione = tentativiSpedizione;
  }

  public java.util.Date getDataOraCreazione() {
    return this.dataOraCreazione;
  }

  public void setDataOraCreazione(java.util.Date dataOraCreazione) {
    this.dataOraCreazione = dataOraCreazione;
  }

  public java.util.Date getDataOraUltimaSpedizione() {
    return this.dataOraUltimaSpedizione;
  }

  public void setDataOraUltimaSpedizione(java.util.Date dataOraUltimaSpedizione) {
    this.dataOraUltimaSpedizione = dataOraUltimaSpedizione;
  }

  public java.util.Date getDataOraProssimaSpedizione() {
    return this.dataOraProssimaSpedizione;
  }

  public void setDataOraProssimaSpedizione(java.util.Date dataOraProssimaSpedizione) {
    this.dataOraProssimaSpedizione = dataOraProssimaSpedizione;
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

  private static it.govpay.orm.model.EsitoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Esito.modelStaticInstance==null){
  			it.govpay.orm.Esito.modelStaticInstance = new it.govpay.orm.model.EsitoModel();
	  }
  }
  public static it.govpay.orm.model.EsitoModel model(){
	  if(it.govpay.orm.Esito.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Esito.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoSpedizione",required=true,nillable=false)
  protected java.lang.String statoSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioSpedizione",required=false,nillable=false)
  protected java.lang.String dettaglioSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="tentativiSpedizione",required=false,nillable=false)
  protected java.lang.Long tentativiSpedizione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraCreazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraUltimaSpedizione",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraUltimaSpedizione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraProssimaSpedizione",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraProssimaSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xml",required=true,nillable=false)
  protected byte[] xml;

}
