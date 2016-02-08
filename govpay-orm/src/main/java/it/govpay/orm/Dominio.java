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


/** <p>Java class for Dominio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dominio">
 * 		&lt;sequence>
 * 			&lt;element name="idStazione" type="{http://www.govpay.it/orm}id-stazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="gln" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pluginClass" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="Disponibilita" type="{http://www.govpay.it/orm}Disponibilita" minOccurs="0" maxOccurs="unbounded"/>
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
@XmlType(name = "Dominio", 
  propOrder = {
  	"idStazione",
  	"codDominio",
  	"ragioneSociale",
  	"gln",
  	"pluginClass",
  	"abilitato",
  	"disponibilita"
  }
)

@XmlRootElement(name = "Dominio")

public class Dominio extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Dominio() {
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

  public IdStazione getIdStazione() {
    return this.idStazione;
  }

  public void setIdStazione(IdStazione idStazione) {
    this.idStazione = idStazione;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getRagioneSociale() {
    return this.ragioneSociale;
  }

  public void setRagioneSociale(java.lang.String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public java.lang.String getGln() {
    return this.gln;
  }

  public void setGln(java.lang.String gln) {
    this.gln = gln;
  }

  public java.lang.String getPluginClass() {
    return this.pluginClass;
  }

  public void setPluginClass(java.lang.String pluginClass) {
    this.pluginClass = pluginClass;
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

  public void addDisponibilita(Disponibilita disponibilita) {
    this.disponibilita.add(disponibilita);
  }

  public Disponibilita getDisponibilita(int index) {
    return this.disponibilita.get( index );
  }

  public Disponibilita removeDisponibilita(int index) {
    return this.disponibilita.remove( index );
  }

  public List<Disponibilita> getDisponibilitaList() {
    return this.disponibilita;
  }

  public void setDisponibilitaList(List<Disponibilita> disponibilita) {
    this.disponibilita=disponibilita;
  }

  public int sizeDisponibilitaList() {
    return this.disponibilita.size();
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.DominioModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Dominio.modelStaticInstance==null){
  			it.govpay.orm.Dominio.modelStaticInstance = new it.govpay.orm.model.DominioModel();
	  }
  }
  public static it.govpay.orm.model.DominioModel model(){
	  if(it.govpay.orm.Dominio.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Dominio.modelStaticInstance;
  }


  @XmlElement(name="idStazione",required=true,nillable=false)
  protected IdStazione idStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSociale",required=true,nillable=false)
  protected java.lang.String ragioneSociale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="gln",required=true,nillable=false)
  protected java.lang.String gln;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pluginClass",required=false,nillable=false)
  protected java.lang.String pluginClass;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @XmlElement(name="Disponibilita",required=true,nillable=false)
  protected List<Disponibilita> disponibilita = new ArrayList<Disponibilita>();

  /**
   * @deprecated Use method getDisponibilitaList
   * @return List<Disponibilita>
  */
  @Deprecated
  public List<Disponibilita> getDisponibilita() {
  	return this.disponibilita;
  }

  /**
   * @deprecated Use method setDisponibilitaList
   * @param disponibilita List<Disponibilita>
  */
  @Deprecated
  public void setDisponibilita(List<Disponibilita> disponibilita) {
  	this.disponibilita=disponibilita;
  }

  /**
   * @deprecated Use method sizeDisponibilitaList
   * @return lunghezza della lista
  */
  @Deprecated
  public int sizeDisponibilita() {
  	return this.disponibilita.size();
  }

}
