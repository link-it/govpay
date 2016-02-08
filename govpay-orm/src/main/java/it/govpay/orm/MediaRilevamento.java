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


/** <p>Java class for MediaRilevamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MediaRilevamento">
 * 		&lt;sequence>
 * 			&lt;element name="idSLA" type="{http://www.govpay.it/orm}id-sla" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOsservazione" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numRilevamentiA" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="percentualeA" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numRilevamentiB" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="percentualeB" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numRilevamentiOver" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "MediaRilevamento", 
  propOrder = {
  	"idSLA",
  	"idApplicazione",
  	"dataOsservazione",
  	"numRilevamentiA",
  	"percentualeA",
  	"numRilevamentiB",
  	"percentualeB",
  	"numRilevamentiOver"
  }
)

@XmlRootElement(name = "MediaRilevamento")

public class MediaRilevamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public MediaRilevamento() {
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

  public long getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(long idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.util.Date getDataOsservazione() {
    return this.dataOsservazione;
  }

  public void setDataOsservazione(java.util.Date dataOsservazione) {
    this.dataOsservazione = dataOsservazione;
  }

  public long getNumRilevamentiA() {
    return this.numRilevamentiA;
  }

  public void setNumRilevamentiA(long numRilevamentiA) {
    this.numRilevamentiA = numRilevamentiA;
  }

  public double getPercentualeA() {
    return this.percentualeA;
  }

  public void setPercentualeA(double percentualeA) {
    this.percentualeA = percentualeA;
  }

  public long getNumRilevamentiB() {
    return this.numRilevamentiB;
  }

  public void setNumRilevamentiB(long numRilevamentiB) {
    this.numRilevamentiB = numRilevamentiB;
  }

  public double getPercentualeB() {
    return this.percentualeB;
  }

  public void setPercentualeB(double percentualeB) {
    this.percentualeB = percentualeB;
  }

  public long getNumRilevamentiOver() {
    return this.numRilevamentiOver;
  }

  public void setNumRilevamentiOver(long numRilevamentiOver) {
    this.numRilevamentiOver = numRilevamentiOver;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.MediaRilevamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.MediaRilevamento.modelStaticInstance==null){
  			it.govpay.orm.MediaRilevamento.modelStaticInstance = new it.govpay.orm.model.MediaRilevamentoModel();
	  }
  }
  public static it.govpay.orm.model.MediaRilevamentoModel model(){
	  if(it.govpay.orm.MediaRilevamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.MediaRilevamento.modelStaticInstance;
  }


  @XmlElement(name="idSLA",required=true,nillable=false)
  protected IdSla idSLA;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected long idApplicazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataOsservazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOsservazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numRilevamentiA",required=true,nillable=false)
  protected long numRilevamentiA;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="percentualeA",required=true,nillable=false)
  protected double percentualeA;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numRilevamentiB",required=true,nillable=false)
  protected long numRilevamentiB;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="percentualeB",required=true,nillable=false)
  protected double percentualeB;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numRilevamentiOver",required=true,nillable=false)
  protected long numRilevamentiOver;

}
