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


/** <p>Java class for Documento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Documento">
 * 		&lt;sequence>
 * 			&lt;element name="codDocumento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Documento", 
  propOrder = {
  	"codDocumento",
  	"descrizione",
  	"idDominio",
  	"idApplicazione"
  }
)

@XmlRootElement(name = "Documento")

public class Documento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Documento() {
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

  public java.lang.String getCodDocumento() {
    return this.codDocumento;
  }

  public void setCodDocumento(java.lang.String codDocumento) {
    this.codDocumento = codDocumento;
  }

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.DocumentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Documento.modelStaticInstance==null){
  			it.govpay.orm.Documento.modelStaticInstance = new it.govpay.orm.model.DocumentoModel();
	  }
  }
  public static it.govpay.orm.model.DocumentoModel model(){
	  if(it.govpay.orm.Documento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Documento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDocumento",required=true,nillable=false)
  protected java.lang.String codDocumento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=true,nillable=false)
  protected java.lang.String descrizione;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

}
