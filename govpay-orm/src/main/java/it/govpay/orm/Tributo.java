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


/** <p>Java class for Tributo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tributo">
 * 		&lt;sequence>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idIbanAccredito" type="{http://www.govpay.it/orm}id-iban-accredito" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoContabilita" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codiceContabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoTributo" type="{http://www.govpay.it/orm}TipoTributo" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codTributoIuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Tributo", 
  propOrder = {
  	"idDominio",
  	"abilitato",
  	"idIbanAccredito",
  	"tipoContabilita",
  	"codiceContabilita",
  	"tipoTributo",
  	"codTributoIuv"
  }
)

@XmlRootElement(name = "Tributo")

public class Tributo extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Tributo() {
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

  public IdIbanAccredito getIdIbanAccredito() {
    return this.idIbanAccredito;
  }

  public void setIdIbanAccredito(IdIbanAccredito idIbanAccredito) {
    this.idIbanAccredito = idIbanAccredito;
  }

  public java.lang.String getTipoContabilita() {
    return this.tipoContabilita;
  }

  public void setTipoContabilita(java.lang.String tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public java.lang.String getCodiceContabilita() {
    return this.codiceContabilita;
  }

  public void setCodiceContabilita(java.lang.String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public TipoTributo getTipoTributo() {
    return this.tipoTributo;
  }

  public void setTipoTributo(TipoTributo tipoTributo) {
    this.tipoTributo = tipoTributo;
  }

  public java.lang.String getCodTributoIuv() {
    return this.codTributoIuv;
  }

  public void setCodTributoIuv(java.lang.String codTributoIuv) {
    this.codTributoIuv = codTributoIuv;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.TributoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Tributo.modelStaticInstance==null){
  			it.govpay.orm.Tributo.modelStaticInstance = new it.govpay.orm.model.TributoModel();
	  }
  }
  public static it.govpay.orm.model.TributoModel model(){
	  if(it.govpay.orm.Tributo.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Tributo.modelStaticInstance;
  }


  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @XmlElement(name="idIbanAccredito",required=false,nillable=false)
  protected IdIbanAccredito idIbanAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoContabilita",required=true,nillable=false)
  protected java.lang.String tipoContabilita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codiceContabilita",required=true,nillable=false)
  protected java.lang.String codiceContabilita;

  @XmlElement(name="tipoTributo",required=true,nillable=false)
  protected TipoTributo tipoTributo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTributoIuv",required=false,nillable=false)
  protected java.lang.String codTributoIuv;

}
