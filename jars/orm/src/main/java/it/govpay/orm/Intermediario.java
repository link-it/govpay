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


/** <p>Java class for Intermediario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Intermediario"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codIntermediario" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codConnettorePdd" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codConnettoreFtp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="principal" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="principalOriginale" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "Intermediario", 
  propOrder = {
  	"codIntermediario",
  	"codConnettorePdd",
  	"codConnettoreFtp",
  	"denominazione",
  	"principal",
  	"principalOriginale",
  	"abilitato"
  }
)

@XmlRootElement(name = "Intermediario")

public class Intermediario extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Intermediario() {
    super();
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

  public java.lang.String getCodConnettoreFtp() {
    return this.codConnettoreFtp;
  }

  public void setCodConnettoreFtp(java.lang.String codConnettoreFtp) {
    this.codConnettoreFtp = codConnettoreFtp;
  }

  public java.lang.String getDenominazione() {
    return this.denominazione;
  }

  public void setDenominazione(java.lang.String denominazione) {
    this.denominazione = denominazione;
  }

  public java.lang.String getPrincipal() {
    return this.principal;
  }

  public void setPrincipal(java.lang.String principal) {
    this.principal = principal;
  }

  public java.lang.String getPrincipalOriginale() {
    return this.principalOriginale;
  }

  public void setPrincipalOriginale(java.lang.String principalOriginale) {
    this.principalOriginale = principalOriginale;
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


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codIntermediario",required=true,nillable=false)
  protected java.lang.String codIntermediario;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettorePdd",required=true,nillable=false)
  protected java.lang.String codConnettorePdd;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreFtp",required=false,nillable=false)
  protected java.lang.String codConnettoreFtp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="denominazione",required=true,nillable=false)
  protected java.lang.String denominazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principalOriginale",required=true,nillable=false)
  protected java.lang.String principalOriginale;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

}
