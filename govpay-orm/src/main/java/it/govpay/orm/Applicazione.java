/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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


/** <p>Java class for Applicazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Applicazione">
 * 		&lt;sequence>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="principal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="firmaRicevuta" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codConnettoreEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codConnettoreVerifica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ApplicazioneTributo" type="{http://www.govpay.it/orm}ApplicazioneTributo" minOccurs="0" maxOccurs="unbounded"/>
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
@XmlType(name = "Applicazione", 
  propOrder = {
  	"codApplicazione",
  	"abilitato",
  	"principal",
  	"firmaRicevuta",
  	"codConnettoreEsito",
  	"codConnettoreVerifica",
  	"applicazioneTributo"
  }
)

@XmlRootElement(name = "Applicazione")

public class Applicazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Applicazione() {
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

  public java.lang.String getCodApplicazione() {
    return this.codApplicazione;
  }

  public void setCodApplicazione(java.lang.String codApplicazione) {
    this.codApplicazione = codApplicazione;
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

  public java.lang.String getPrincipal() {
    return this.principal;
  }

  public void setPrincipal(java.lang.String principal) {
    this.principal = principal;
  }

  public java.lang.String getFirmaRicevuta() {
    return this.firmaRicevuta;
  }

  public void setFirmaRicevuta(java.lang.String firmaRicevuta) {
    this.firmaRicevuta = firmaRicevuta;
  }

  public java.lang.String getCodConnettoreEsito() {
    return this.codConnettoreEsito;
  }

  public void setCodConnettoreEsito(java.lang.String codConnettoreEsito) {
    this.codConnettoreEsito = codConnettoreEsito;
  }

  public java.lang.String getCodConnettoreVerifica() {
    return this.codConnettoreVerifica;
  }

  public void setCodConnettoreVerifica(java.lang.String codConnettoreVerifica) {
    this.codConnettoreVerifica = codConnettoreVerifica;
  }

  public void addApplicazioneTributo(ApplicazioneTributo applicazioneTributo) {
    this.applicazioneTributo.add(applicazioneTributo);
  }

  public ApplicazioneTributo getApplicazioneTributo(int index) {
    return this.applicazioneTributo.get( index );
  }

  public ApplicazioneTributo removeApplicazioneTributo(int index) {
    return this.applicazioneTributo.remove( index );
  }

  public List<ApplicazioneTributo> getApplicazioneTributoList() {
    return this.applicazioneTributo;
  }

  public void setApplicazioneTributoList(List<ApplicazioneTributo> applicazioneTributo) {
    this.applicazioneTributo=applicazioneTributo;
  }

  public int sizeApplicazioneTributoList() {
    return this.applicazioneTributo.size();
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.ApplicazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Applicazione.modelStaticInstance==null){
  			it.govpay.orm.Applicazione.modelStaticInstance = new it.govpay.orm.model.ApplicazioneModel();
	  }
  }
  public static it.govpay.orm.model.ApplicazioneModel model(){
	  if(it.govpay.orm.Applicazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Applicazione.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=true,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="firmaRicevuta",required=true,nillable=false)
  protected java.lang.String firmaRicevuta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreEsito",required=false,nillable=false)
  protected java.lang.String codConnettoreEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreVerifica",required=false,nillable=false)
  protected java.lang.String codConnettoreVerifica;

  @XmlElement(name="ApplicazioneTributo",required=true,nillable=false)
  protected List<ApplicazioneTributo> applicazioneTributo = new ArrayList<ApplicazioneTributo>();

  /**
   * @deprecated Use method getApplicazioneTributoList
   * @return List<ApplicazioneTributo>
  */
  @Deprecated
  public List<ApplicazioneTributo> getApplicazioneTributo() {
  	return this.applicazioneTributo;
  }

  /**
   * @deprecated Use method setApplicazioneTributoList
   * @param applicazioneTributo List<ApplicazioneTributo>
  */
  @Deprecated
  public void setApplicazioneTributo(List<ApplicazioneTributo> applicazioneTributo) {
  	this.applicazioneTributo=applicazioneTributo;
  }

  /**
   * @deprecated Use method sizeApplicazioneTributoList
   * @return lunghezza della lista
  */
  @Deprecated
  public int sizeApplicazioneTributo() {
  	return this.applicazioneTributo.size();
  }

}
