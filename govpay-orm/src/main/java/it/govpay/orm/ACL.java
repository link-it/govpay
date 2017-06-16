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


/** <p>Java class for ACL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ACL">
 * 		&lt;sequence>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idPortale" type="{http://www.govpay.it/orm}id-portale" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="diritti" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codServizio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTipoTributo" type="{http://www.govpay.it/orm}id-tipo-tributo" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "ACL", 
  propOrder = {
  	"idApplicazione",
  	"idPortale",
  	"idOperatore",
  	"codTipo",
  	"diritti",
  	"codServizio",
  	"idDominio",
  	"idTipoTributo"
  }
)

@XmlRootElement(name = "ACL")

public class ACL extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public ACL() {
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

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdPortale getIdPortale() {
    return this.idPortale;
  }

  public void setIdPortale(IdPortale idPortale) {
    this.idPortale = idPortale;
  }

  public IdOperatore getIdOperatore() {
    return this.idOperatore;
  }

  public void setIdOperatore(IdOperatore idOperatore) {
    this.idOperatore = idOperatore;
  }

  public java.lang.String getCodTipo() {
    return this.codTipo;
  }

  public void setCodTipo(java.lang.String codTipo) {
    this.codTipo = codTipo;
  }

  public int getDiritti() {
    return this.diritti;
  }

  public void setDiritti(int diritti) {
    this.diritti = diritti;
  }

  public java.lang.String getCodServizio() {
    return this.codServizio;
  }

  public void setCodServizio(java.lang.String codServizio) {
    this.codServizio = codServizio;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public IdTipoTributo getIdTipoTributo() {
    return this.idTipoTributo;
  }

  public void setIdTipoTributo(IdTipoTributo idTipoTributo) {
    this.idTipoTributo = idTipoTributo;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.ACLModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.ACL.modelStaticInstance==null){
  			it.govpay.orm.ACL.modelStaticInstance = new it.govpay.orm.model.ACLModel();
	  }
  }
  public static it.govpay.orm.model.ACLModel model(){
	  if(it.govpay.orm.ACL.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.ACL.modelStaticInstance;
  }


  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @XmlElement(name="idPortale",required=false,nillable=false)
  protected IdPortale idPortale;

  @XmlElement(name="idOperatore",required=false,nillable=false)
  protected IdOperatore idOperatore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTipo",required=true,nillable=false)
  protected java.lang.String codTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="diritti",required=false,nillable=false)
  protected int diritti;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codServizio",required=true,nillable=false)
  protected java.lang.String codServizio;

  @XmlElement(name="idDominio",required=false,nillable=false)
  protected IdDominio idDominio;

  @XmlElement(name="idTipoTributo",required=false,nillable=false)
  protected IdTipoTributo idTipoTributo;

}
