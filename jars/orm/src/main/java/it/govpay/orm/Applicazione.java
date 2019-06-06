/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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


/** <p>Java class for Applicazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Applicazione">
 * 		&lt;sequence>
 * 			&lt;element name="idUtenza" type="{http://www.govpay.it/orm}id-utenza" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="autoIUV" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="firmaRicevuta" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codConnettoreIntegrazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="trusted" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codApplicazioneIuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="regExp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
  	"idUtenza",
  	"codApplicazione",
  	"autoIUV",
  	"firmaRicevuta",
  	"codConnettoreIntegrazione",
  	"trusted",
  	"codApplicazioneIuv",
  	"regExp"
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

  public IdUtenza getIdUtenza() {
    return this.idUtenza;
  }

  public void setIdUtenza(IdUtenza idUtenza) {
    this.idUtenza = idUtenza;
  }

  public java.lang.String getCodApplicazione() {
    return this.codApplicazione;
  }

  public void setCodApplicazione(java.lang.String codApplicazione) {
    this.codApplicazione = codApplicazione;
  }

  public boolean isAutoIUV() {
    return this.autoIUV;
  }

  public boolean getAutoIUV() {
    return this.autoIUV;
  }

  public void setAutoIUV(boolean autoIUV) {
    this.autoIUV = autoIUV;
  }

  public java.lang.String getFirmaRicevuta() {
    return this.firmaRicevuta;
  }

  public void setFirmaRicevuta(java.lang.String firmaRicevuta) {
    this.firmaRicevuta = firmaRicevuta;
  }

  public java.lang.String getCodConnettoreIntegrazione() {
    return this.codConnettoreIntegrazione;
  }

  public void setCodConnettoreIntegrazione(java.lang.String codConnettoreIntegrazione) {
    this.codConnettoreIntegrazione = codConnettoreIntegrazione;
  }

  public boolean isTrusted() {
    return this.trusted;
  }

  public boolean getTrusted() {
    return this.trusted;
  }

  public void setTrusted(boolean trusted) {
    this.trusted = trusted;
  }

  public java.lang.String getCodApplicazioneIuv() {
    return this.codApplicazioneIuv;
  }

  public void setCodApplicazioneIuv(java.lang.String codApplicazioneIuv) {
    this.codApplicazioneIuv = codApplicazioneIuv;
  }

  public java.lang.String getRegExp() {
    return this.regExp;
  }

  public void setRegExp(java.lang.String regExp) {
    this.regExp = regExp;
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


  @XmlElement(name="idUtenza",required=true,nillable=false)
  protected IdUtenza idUtenza;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=true,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="autoIUV",required=true,nillable=false)
  protected boolean autoIUV;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="firmaRicevuta",required=true,nillable=false)
  protected java.lang.String firmaRicevuta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettoreIntegrazione",required=false,nillable=false)
  protected java.lang.String codConnettoreIntegrazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="trusted",required=true,nillable=false)
  protected boolean trusted;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazioneIuv",required=false,nillable=false)
  protected java.lang.String codApplicazioneIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="regExp",required=false,nillable=false)
  protected java.lang.String regExp;

}
