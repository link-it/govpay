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


/** <p>Java class for Intermediario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Intermediario">
 * 		&lt;sequence>
 * 			&lt;element name="codIntermediario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codConnettorePdd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Intermediario", 
  propOrder = {
  	"codIntermediario",
  	"codConnettorePdd",
  	"denominazione",
  	"abilitato"
  }
)

@XmlRootElement(name = "Intermediario")

public class Intermediario extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Intermediario() {
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

  public java.lang.String getCodIntermediario() {
    return this.codIntermediario;
  }

  public void setCodIntermediario(java.lang.String codIntermediario) {
    this.codIntermediario = codIntermediario;
  }

  public java.lang.String getCodConnettorePdd() {
    return this.codConnettorePdd;
  }

  public void setCodConnettorePdd(java.lang.String codConnettorePdd) {
    this.codConnettorePdd = codConnettorePdd;
  }

  public java.lang.String getDenominazione() {
    return this.denominazione;
  }

  public void setDenominazione(java.lang.String denominazione) {
    this.denominazione = denominazione;
  }

  public boolean isAbilitato() {
    return this.abilitato;
  }

  public boolean getAbilitato() {
    return this.abilitato;
  }

  public void setAbilitato(boolean abilitato) {
    this.abilitato = abilitato;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.IntermediarioModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Intermediario.modelStaticInstance==null){
  			it.govpay.orm.Intermediario.modelStaticInstance = new it.govpay.orm.model.IntermediarioModel();
	  }
  }
  public static it.govpay.orm.model.IntermediarioModel model(){
	  if(it.govpay.orm.Intermediario.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Intermediario.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codIntermediario",required=true,nillable=false)
  protected java.lang.String codIntermediario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettorePdd",required=true,nillable=false)
  protected java.lang.String codConnettorePdd;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="denominazione",required=true,nillable=false)
  protected java.lang.String denominazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

}
