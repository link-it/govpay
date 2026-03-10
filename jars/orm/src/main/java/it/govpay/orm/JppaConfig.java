/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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


/** <p>Java class for JppaConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JppaConfig"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codConnettore" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataUltimaRt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "JppaConfig", 
  propOrder = {
  	"idDominio",
  	"codDominio",
  	"codConnettore",
  	"abilitato",
  	"dataUltimaRt"
  }
)

@XmlRootElement(name = "JppaConfig")

public class JppaConfig extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public JppaConfig() {
    super();
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getCodConnettore() {
    return this.codConnettore;
  }

  public void setCodConnettore(java.lang.String codConnettore) {
    this.codConnettore = codConnettore;
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

  public java.util.Date getDataUltimaRt() {
    return this.dataUltimaRt;
  }

  public void setDataUltimaRt(java.util.Date dataUltimaRt) {
    this.dataUltimaRt = dataUltimaRt;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.JppaConfigModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.JppaConfig.modelStaticInstance==null){
  			it.govpay.orm.JppaConfig.modelStaticInstance = new it.govpay.orm.model.JppaConfigModel();
	  }
  }
  public static it.govpay.orm.model.JppaConfigModel model(){
	  if(it.govpay.orm.JppaConfig.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.JppaConfig.modelStaticInstance;
  }


  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codConnettore",required=true,nillable=false)
  protected java.lang.String codConnettore;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataUltimaRt",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataUltimaRt;

}
