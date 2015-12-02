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


/** <p>Java class for Anagrafica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Anagrafica">
 * 		&lt;sequence>
 * 			&lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codUnivoco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="civico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="cap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="localita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="nazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="cellulare" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Anagrafica", 
  propOrder = {
  	"ragioneSociale",
  	"codUnivoco",
  	"indirizzo",
  	"civico",
  	"cap",
  	"localita",
  	"provincia",
  	"nazione",
  	"email",
  	"telefono",
  	"cellulare",
  	"fax"
  }
)

@XmlRootElement(name = "Anagrafica")

public class Anagrafica extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Anagrafica() {
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

  public java.lang.String getRagioneSociale() {
    return this.ragioneSociale;
  }

  public void setRagioneSociale(java.lang.String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public java.lang.String getCodUnivoco() {
    return this.codUnivoco;
  }

  public void setCodUnivoco(java.lang.String codUnivoco) {
    this.codUnivoco = codUnivoco;
  }

  public java.lang.String getIndirizzo() {
    return this.indirizzo;
  }

  public void setIndirizzo(java.lang.String indirizzo) {
    this.indirizzo = indirizzo;
  }

  public java.lang.String getCivico() {
    return this.civico;
  }

  public void setCivico(java.lang.String civico) {
    this.civico = civico;
  }

  public java.lang.String getCap() {
    return this.cap;
  }

  public void setCap(java.lang.String cap) {
    this.cap = cap;
  }

  public java.lang.String getLocalita() {
    return this.localita;
  }

  public void setLocalita(java.lang.String localita) {
    this.localita = localita;
  }

  public java.lang.String getProvincia() {
    return this.provincia;
  }

  public void setProvincia(java.lang.String provincia) {
    this.provincia = provincia;
  }

  public java.lang.String getNazione() {
    return this.nazione;
  }

  public void setNazione(java.lang.String nazione) {
    this.nazione = nazione;
  }

  public java.lang.String getEmail() {
    return this.email;
  }

  public void setEmail(java.lang.String email) {
    this.email = email;
  }

  public java.lang.String getTelefono() {
    return this.telefono;
  }

  public void setTelefono(java.lang.String telefono) {
    this.telefono = telefono;
  }

  public java.lang.String getCellulare() {
    return this.cellulare;
  }

  public void setCellulare(java.lang.String cellulare) {
    this.cellulare = cellulare;
  }

  public java.lang.String getFax() {
    return this.fax;
  }

  public void setFax(java.lang.String fax) {
    this.fax = fax;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.AnagraficaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Anagrafica.modelStaticInstance==null){
  			it.govpay.orm.Anagrafica.modelStaticInstance = new it.govpay.orm.model.AnagraficaModel();
	  }
  }
  public static it.govpay.orm.model.AnagraficaModel model(){
	  if(it.govpay.orm.Anagrafica.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Anagrafica.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSociale",required=true,nillable=false)
  protected java.lang.String ragioneSociale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codUnivoco",required=true,nillable=false)
  protected java.lang.String codUnivoco;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="indirizzo",required=false,nillable=false)
  protected java.lang.String indirizzo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="civico",required=false,nillable=false)
  protected java.lang.String civico;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cap",required=false,nillable=false)
  protected java.lang.String cap;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="localita",required=false,nillable=false)
  protected java.lang.String localita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="provincia",required=false,nillable=false)
  protected java.lang.String provincia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nazione",required=false,nillable=false)
  protected java.lang.String nazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="email",required=false,nillable=false)
  protected java.lang.String email;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="telefono",required=false,nillable=false)
  protected java.lang.String telefono;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cellulare",required=false,nillable=false)
  protected java.lang.String cellulare;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fax",required=false,nillable=false)
  protected java.lang.String fax;

}
