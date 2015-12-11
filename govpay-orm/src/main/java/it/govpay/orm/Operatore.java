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


/** <p>Java class for Operatore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Operatore">
 * 		&lt;sequence>
 * 			&lt;element name="principal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="profilo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="true"/>
 * 			&lt;element name="OperatoreEnte" type="{http://www.govpay.it/orm}OperatoreEnte" minOccurs="1" maxOccurs="unbounded"/>
 * 			&lt;element name="OperatoreApplicazione" type="{http://www.govpay.it/orm}OperatoreApplicazione" minOccurs="1" maxOccurs="unbounded"/>
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
@XmlType(name = "Operatore", 
  propOrder = {
  	"principal",
  	"nome",
  	"profilo",
  	"abilitato",
  	"operatoreEnte",
  	"operatoreApplicazione"
  }
)

@XmlRootElement(name = "Operatore")

public class Operatore extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Operatore() {
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

  public java.lang.String getNome() {
    return this.nome;
  }

  public void setNome(java.lang.String nome) {
    this.nome = nome;
  }

  public java.lang.String getProfilo() {
    return this.profilo;
  }

  public void setProfilo(java.lang.String profilo) {
    this.profilo = profilo;
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

  public void addOperatoreEnte(OperatoreEnte operatoreEnte) {
    this.operatoreEnte.add(operatoreEnte);
  }

  public OperatoreEnte getOperatoreEnte(int index) {
    return this.operatoreEnte.get( index );
  }

  public OperatoreEnte removeOperatoreEnte(int index) {
    return this.operatoreEnte.remove( index );
  }

  public List<OperatoreEnte> getOperatoreEnteList() {
    return this.operatoreEnte;
  }

  public void setOperatoreEnteList(List<OperatoreEnte> operatoreEnte) {
    this.operatoreEnte=operatoreEnte;
  }

  public int sizeOperatoreEnteList() {
    return this.operatoreEnte.size();
  }

  public void addOperatoreApplicazione(OperatoreApplicazione operatoreApplicazione) {
    this.operatoreApplicazione.add(operatoreApplicazione);
  }

  public OperatoreApplicazione getOperatoreApplicazione(int index) {
    return this.operatoreApplicazione.get( index );
  }

  public OperatoreApplicazione removeOperatoreApplicazione(int index) {
    return this.operatoreApplicazione.remove( index );
  }

  public List<OperatoreApplicazione> getOperatoreApplicazioneList() {
    return this.operatoreApplicazione;
  }

  public void setOperatoreApplicazioneList(List<OperatoreApplicazione> operatoreApplicazione) {
    this.operatoreApplicazione=operatoreApplicazione;
  }

  public int sizeOperatoreApplicazioneList() {
    return this.operatoreApplicazione.size();
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.OperatoreModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Operatore.modelStaticInstance==null){
  			it.govpay.orm.Operatore.modelStaticInstance = new it.govpay.orm.model.OperatoreModel();
	  }
  }
  public static it.govpay.orm.model.OperatoreModel model(){
	  if(it.govpay.orm.Operatore.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Operatore.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=true,nillable=false)
  protected java.lang.String nome;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="profilo",required=true,nillable=false)
  protected java.lang.String profilo;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false,defaultValue="true")
  protected boolean abilitato = true;

  @XmlElement(name="OperatoreEnte",required=true,nillable=false)
  protected List<OperatoreEnte> operatoreEnte = new ArrayList<OperatoreEnte>();

  /**
   * @deprecated Use method getOperatoreEnteList
   * @return List<OperatoreEnte>
  */
  @Deprecated
  public List<OperatoreEnte> getOperatoreEnte() {
  	return this.operatoreEnte;
  }

  /**
   * @deprecated Use method setOperatoreEnteList
   * @param operatoreEnte List<OperatoreEnte>
  */
  @Deprecated
  public void setOperatoreEnte(List<OperatoreEnte> operatoreEnte) {
  	this.operatoreEnte=operatoreEnte;
  }

  /**
   * @deprecated Use method sizeOperatoreEnteList
   * @return lunghezza della lista
  */
  @Deprecated
  public int sizeOperatoreEnte() {
  	return this.operatoreEnte.size();
  }

  @XmlElement(name="OperatoreApplicazione",required=true,nillable=false)
  protected List<OperatoreApplicazione> operatoreApplicazione = new ArrayList<OperatoreApplicazione>();

  /**
   * @deprecated Use method getOperatoreApplicazioneList
   * @return List<OperatoreApplicazione>
  */
  @Deprecated
  public List<OperatoreApplicazione> getOperatoreApplicazione() {
  	return this.operatoreApplicazione;
  }

  /**
   * @deprecated Use method setOperatoreApplicazioneList
   * @param operatoreApplicazione List<OperatoreApplicazione>
  */
  @Deprecated
  public void setOperatoreApplicazione(List<OperatoreApplicazione> operatoreApplicazione) {
  	this.operatoreApplicazione=operatoreApplicazione;
  }

  /**
   * @deprecated Use method sizeOperatoreApplicazioneList
   * @return lunghezza della lista
  */
  @Deprecated
  public int sizeOperatoreApplicazione() {
  	return this.operatoreApplicazione.size();
  }

}
