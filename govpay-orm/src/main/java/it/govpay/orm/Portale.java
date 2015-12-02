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
import java.util.ArrayList;
import java.util.List;


/** <p>Java class for Portale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Portale">
 * 		&lt;sequence>
 * 			&lt;element name="codPortale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idStazione" type="{http://www.govpay.it/orm}id-stazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="defaultCallbackURL" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="principal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="PortaleApplicazione" type="{http://www.govpay.it/orm}PortaleApplicazione" minOccurs="1" maxOccurs="unbounded"/>
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
@XmlType(name = "Portale", 
  propOrder = {
  	"codPortale",
  	"idStazione",
  	"defaultCallbackURL",
  	"principal",
  	"abilitato",
  	"portaleApplicazione"
  }
)

@XmlRootElement(name = "Portale")

public class Portale extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Portale() {
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

  public java.lang.String getCodPortale() {
    return this.codPortale;
  }

  public void setCodPortale(java.lang.String codPortale) {
    this.codPortale = codPortale;
  }

  public IdStazione getIdStazione() {
    return this.idStazione;
  }

  public void setIdStazione(IdStazione idStazione) {
    this.idStazione = idStazione;
  }

  public java.lang.String getDefaultCallbackURL() {
    return this.defaultCallbackURL;
  }

  public void setDefaultCallbackURL(java.lang.String defaultCallbackURL) {
    this.defaultCallbackURL = defaultCallbackURL;
  }

  public java.lang.String getPrincipal() {
    return this.principal;
  }

  public void setPrincipal(java.lang.String principal) {
    this.principal = principal;
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

  public void addPortaleApplicazione(PortaleApplicazione portaleApplicazione) {
    this.portaleApplicazione.add(portaleApplicazione);
  }

  public PortaleApplicazione getPortaleApplicazione(int index) {
    return this.portaleApplicazione.get( index );
  }

  public PortaleApplicazione removePortaleApplicazione(int index) {
    return this.portaleApplicazione.remove( index );
  }

  public List<PortaleApplicazione> getPortaleApplicazioneList() {
    return this.portaleApplicazione;
  }

  public void setPortaleApplicazioneList(List<PortaleApplicazione> portaleApplicazione) {
    this.portaleApplicazione=portaleApplicazione;
  }

  public int sizePortaleApplicazioneList() {
    return this.portaleApplicazione.size();
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.PortaleModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Portale.modelStaticInstance==null){
  			it.govpay.orm.Portale.modelStaticInstance = new it.govpay.orm.model.PortaleModel();
	  }
  }
  public static it.govpay.orm.model.PortaleModel model(){
	  if(it.govpay.orm.Portale.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Portale.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPortale",required=true,nillable=false)
  protected java.lang.String codPortale;

  @XmlElement(name="idStazione",required=true,nillable=false)
  protected IdStazione idStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="defaultCallbackURL",required=true,nillable=false)
  protected java.lang.String defaultCallbackURL;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @XmlElement(name="PortaleApplicazione",required=true,nillable=false)
  protected List<PortaleApplicazione> portaleApplicazione = new ArrayList<PortaleApplicazione>();

  /**
   * @deprecated Use method getPortaleApplicazioneList
   * @return List<PortaleApplicazione>
  */
  @Deprecated
  public List<PortaleApplicazione> getPortaleApplicazione() {
  	return this.portaleApplicazione;
  }

  /**
   * @deprecated Use method setPortaleApplicazioneList
   * @param portaleApplicazione List<PortaleApplicazione>
  */
  @Deprecated
  public void setPortaleApplicazione(List<PortaleApplicazione> portaleApplicazione) {
  	this.portaleApplicazione=portaleApplicazione;
  }

  /**
   * @deprecated Use method sizePortaleApplicazioneList
   * @return lunghezza della lista
  */
  @Deprecated
  public int sizePortaleApplicazione() {
  	return this.portaleApplicazione.size();
  }

}
