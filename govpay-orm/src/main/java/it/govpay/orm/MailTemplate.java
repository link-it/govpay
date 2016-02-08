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


/** <p>Java class for MailTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MailTemplate">
 * 		&lt;sequence>
 * 			&lt;element name="mittente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="templateOggetto" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="templateMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="allegati" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "MailTemplate", 
  propOrder = {
  	"mittente",
  	"templateOggetto",
  	"templateMessaggio",
  	"allegati"
  }
)

@XmlRootElement(name = "MailTemplate")

public class MailTemplate extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public MailTemplate() {
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

  public java.lang.String getMittente() {
    return this.mittente;
  }

  public void setMittente(java.lang.String mittente) {
    this.mittente = mittente;
  }

  public java.lang.String getTemplateOggetto() {
    return this.templateOggetto;
  }

  public void setTemplateOggetto(java.lang.String templateOggetto) {
    this.templateOggetto = templateOggetto;
  }

  public java.lang.String getTemplateMessaggio() {
    return this.templateMessaggio;
  }

  public void setTemplateMessaggio(java.lang.String templateMessaggio) {
    this.templateMessaggio = templateMessaggio;
  }

  public java.lang.String getAllegati() {
    return this.allegati;
  }

  public void setAllegati(java.lang.String allegati) {
    this.allegati = allegati;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.MailTemplateModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.MailTemplate.modelStaticInstance==null){
  			it.govpay.orm.MailTemplate.modelStaticInstance = new it.govpay.orm.model.MailTemplateModel();
	  }
  }
  public static it.govpay.orm.model.MailTemplateModel model(){
	  if(it.govpay.orm.MailTemplate.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.MailTemplate.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="mittente",required=true,nillable=false)
  protected java.lang.String mittente;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="templateOggetto",required=true,nillable=false)
  protected java.lang.String templateOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="templateMessaggio",required=true,nillable=false)
  protected java.lang.String templateMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="allegati",required=false,nillable=false)
  protected java.lang.String allegati;

}
