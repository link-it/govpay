/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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


/** <p>Java class for Utenza complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Utenza">
 * 		&lt;sequence>
 * 			&lt;element name="principal" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="principalOriginale" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="true"/>
 * 			&lt;element name="autorizzazione_domini_star" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="autorizzazione_tipi_vers_star" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="ruoli" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="password" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Utenza", 
  propOrder = {
  	"principal",
  	"principalOriginale",
  	"abilitato",
  	"autorizzazioneDominiStar",
  	"autorizzazioneTipiVersStar",
  	"ruoli",
  	"password"
  }
)

@XmlRootElement(name = "Utenza")

public class Utenza extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Utenza() {
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

  public boolean isAutorizzazioneDominiStar() {
    return this.autorizzazioneDominiStar;
  }

  public boolean getAutorizzazioneDominiStar() {
    return this.autorizzazioneDominiStar;
  }

  public void setAutorizzazioneDominiStar(boolean autorizzazioneDominiStar) {
    this.autorizzazioneDominiStar = autorizzazioneDominiStar;
  }

  public boolean isAutorizzazioneTipiVersStar() {
    return this.autorizzazioneTipiVersStar;
  }

  public boolean getAutorizzazioneTipiVersStar() {
    return this.autorizzazioneTipiVersStar;
  }

  public void setAutorizzazioneTipiVersStar(boolean autorizzazioneTipiVersStar) {
    this.autorizzazioneTipiVersStar = autorizzazioneTipiVersStar;
  }

  public java.lang.String getRuoli() {
    return this.ruoli;
  }

  public void setRuoli(java.lang.String ruoli) {
    this.ruoli = ruoli;
  }

  public java.lang.String getPassword() {
    return this.password;
  }

  public void setPassword(java.lang.String password) {
    this.password = password;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.UtenzaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Utenza.modelStaticInstance==null){
  			it.govpay.orm.Utenza.modelStaticInstance = new it.govpay.orm.model.UtenzaModel();
	  }
  }
  public static it.govpay.orm.model.UtenzaModel model(){
	  if(it.govpay.orm.Utenza.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Utenza.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principalOriginale",required=true,nillable=false)
  protected java.lang.String principalOriginale;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false,defaultValue="true")
  protected boolean abilitato = true;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="autorizzazione_domini_star",required=true,nillable=false,defaultValue="false")
  protected boolean autorizzazioneDominiStar = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="autorizzazione_tipi_vers_star",required=true,nillable=false,defaultValue="false")
  protected boolean autorizzazioneTipiVersStar = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ruoli",required=false,nillable=false)
  protected java.lang.String ruoli;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="password",required=false,nillable=false)
  protected java.lang.String password;

}
