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


/** <p>Java class for SLA complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLA">
 * 		&lt;sequence>
 * 			&lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoEventoIniziale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEventoIniziale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoEventoFinale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEventoFinale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tempoA" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tempoB" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tolleranzaA" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tolleranzaB" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "SLA", 
  propOrder = {
  	"descrizione",
  	"tipoEventoIniziale",
  	"sottotipoEventoIniziale",
  	"tipoEventoFinale",
  	"sottotipoEventoFinale",
  	"tempoA",
  	"tempoB",
  	"tolleranzaA",
  	"tolleranzaB"
  }
)

@XmlRootElement(name = "SLA")

public class SLA extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public SLA() {
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

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public java.lang.String getTipoEventoIniziale() {
    return this.tipoEventoIniziale;
  }

  public void setTipoEventoIniziale(java.lang.String tipoEventoIniziale) {
    this.tipoEventoIniziale = tipoEventoIniziale;
  }

  public java.lang.String getSottotipoEventoIniziale() {
    return this.sottotipoEventoIniziale;
  }

  public void setSottotipoEventoIniziale(java.lang.String sottotipoEventoIniziale) {
    this.sottotipoEventoIniziale = sottotipoEventoIniziale;
  }

  public java.lang.String getTipoEventoFinale() {
    return this.tipoEventoFinale;
  }

  public void setTipoEventoFinale(java.lang.String tipoEventoFinale) {
    this.tipoEventoFinale = tipoEventoFinale;
  }

  public java.lang.String getSottotipoEventoFinale() {
    return this.sottotipoEventoFinale;
  }

  public void setSottotipoEventoFinale(java.lang.String sottotipoEventoFinale) {
    this.sottotipoEventoFinale = sottotipoEventoFinale;
  }

  public long getTempoA() {
    return this.tempoA;
  }

  public void setTempoA(long tempoA) {
    this.tempoA = tempoA;
  }

  public long getTempoB() {
    return this.tempoB;
  }

  public void setTempoB(long tempoB) {
    this.tempoB = tempoB;
  }

  public double getTolleranzaA() {
    return this.tolleranzaA;
  }

  public void setTolleranzaA(double tolleranzaA) {
    this.tolleranzaA = tolleranzaA;
  }

  public double getTolleranzaB() {
    return this.tolleranzaB;
  }

  public void setTolleranzaB(double tolleranzaB) {
    this.tolleranzaB = tolleranzaB;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.SLAModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.SLA.modelStaticInstance==null){
  			it.govpay.orm.SLA.modelStaticInstance = new it.govpay.orm.model.SLAModel();
	  }
  }
  public static it.govpay.orm.model.SLAModel model(){
	  if(it.govpay.orm.SLA.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.SLA.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=true,nillable=false)
  protected java.lang.String descrizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoEventoIniziale",required=true,nillable=false)
  protected java.lang.String tipoEventoIniziale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sottotipoEventoIniziale",required=true,nillable=false)
  protected java.lang.String sottotipoEventoIniziale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoEventoFinale",required=true,nillable=false)
  protected java.lang.String tipoEventoFinale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sottotipoEventoFinale",required=true,nillable=false)
  protected java.lang.String sottotipoEventoFinale;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="tempoA",required=true,nillable=false)
  protected long tempoA;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="tempoB",required=true,nillable=false)
  protected long tempoB;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="tolleranzaA",required=true,nillable=false)
  protected double tolleranzaA;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="tolleranzaB",required=true,nillable=false)
  protected double tolleranzaB;

}
