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


/** <p>Java class for Ente complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ente">
 * 		&lt;sequence>
 * 			&lt;element name="idAnagraficaEnte" type="{http://www.govpay.it/orm}id-anagrafica" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idTemplateRPT" type="{http://www.govpay.it/orm}id-mail-template" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTemplateRT" type="{http://www.govpay.it/orm}id-mail-template" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="invioMailRPTAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="invioMailRTAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Ente", 
  propOrder = {
  	"idAnagraficaEnte",
  	"codEnte",
  	"idDominio",
  	"abilitato",
  	"idTemplateRPT",
  	"idTemplateRT",
  	"invioMailRPTAbilitato",
  	"invioMailRTAbilitato"
  }
)

@XmlRootElement(name = "Ente")

public class Ente extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Ente() {
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

  public IdAnagrafica getIdAnagraficaEnte() {
    return this.idAnagraficaEnte;
  }

  public void setIdAnagraficaEnte(IdAnagrafica idAnagraficaEnte) {
    this.idAnagraficaEnte = idAnagraficaEnte;
  }

  public java.lang.String getCodEnte() {
    return this.codEnte;
  }

  public void setCodEnte(java.lang.String codEnte) {
    this.codEnte = codEnte;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
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

  public IdMailTemplate getIdTemplateRPT() {
    return this.idTemplateRPT;
  }

  public void setIdTemplateRPT(IdMailTemplate idTemplateRPT) {
    this.idTemplateRPT = idTemplateRPT;
  }

  public IdMailTemplate getIdTemplateRT() {
    return this.idTemplateRT;
  }

  public void setIdTemplateRT(IdMailTemplate idTemplateRT) {
    this.idTemplateRT = idTemplateRT;
  }

  public boolean isInvioMailRPTAbilitato() {
    return this.invioMailRPTAbilitato;
  }

  public boolean getInvioMailRPTAbilitato() {
    return this.invioMailRPTAbilitato;
  }

  public void setInvioMailRPTAbilitato(boolean invioMailRPTAbilitato) {
    this.invioMailRPTAbilitato = invioMailRPTAbilitato;
  }

  public boolean isInvioMailRTAbilitato() {
    return this.invioMailRTAbilitato;
  }

  public boolean getInvioMailRTAbilitato() {
    return this.invioMailRTAbilitato;
  }

  public void setInvioMailRTAbilitato(boolean invioMailRTAbilitato) {
    this.invioMailRTAbilitato = invioMailRTAbilitato;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.EnteModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Ente.modelStaticInstance==null){
  			it.govpay.orm.Ente.modelStaticInstance = new it.govpay.orm.model.EnteModel();
	  }
  }
  public static it.govpay.orm.model.EnteModel model(){
	  if(it.govpay.orm.Ente.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Ente.modelStaticInstance;
  }


  @XmlElement(name="idAnagraficaEnte",required=true,nillable=false)
  protected IdAnagrafica idAnagraficaEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codEnte",required=true,nillable=false)
  protected java.lang.String codEnte;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @XmlElement(name="idTemplateRPT",required=false,nillable=false)
  protected IdMailTemplate idTemplateRPT;

  @XmlElement(name="idTemplateRT",required=false,nillable=false)
  protected IdMailTemplate idTemplateRT;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="invioMailRPTAbilitato",required=true,nillable=false)
  protected boolean invioMailRPTAbilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="invioMailRTAbilitato",required=true,nillable=false)
  protected boolean invioMailRTAbilitato;

}
