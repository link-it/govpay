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


/** <p>Java class for Tributo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tributo">
 * 		&lt;sequence>
 * 			&lt;element name="idEnte" type="{http://www.govpay.it/orm}id-ente" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codTributo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ibanAccredito" type="{http://www.govpay.it/orm}id-iban-accredito" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoContabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codiceContabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
  	"idEnte",
  	"codTributo",
  	"abilitato",
  	"descrizione",
  	"ibanAccredito",
  	"tipoContabilita",
  	"codiceContabilita"
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

  public IdEnte getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(IdEnte idEnte) {
    this.idEnte = idEnte;
  }

  public java.lang.String getCodTributo() {
    return this.codTributo;
  }

  public void setCodTributo(java.lang.String codTributo) {
    this.codTributo = codTributo;
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

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public IdIbanAccredito getIbanAccredito() {
    return this.ibanAccredito;
  }

  public void setIbanAccredito(IdIbanAccredito ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
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


  @XmlElement(name="idEnte",required=true,nillable=false)
  protected IdEnte idEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTributo",required=true,nillable=false)
  protected java.lang.String codTributo;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=false,nillable=false)
  protected java.lang.String descrizione;

  @XmlElement(name="ibanAccredito",required=false,nillable=false)
  protected IdIbanAccredito ibanAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoContabilita",required=true,nillable=false)
  protected java.lang.String tipoContabilita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codiceContabilita",required=true,nillable=false)
  protected java.lang.String codiceContabilita;

}
