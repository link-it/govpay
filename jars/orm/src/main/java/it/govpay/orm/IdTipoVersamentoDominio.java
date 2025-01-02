/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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


/** <p>Java class for id-tipo-versamento-dominio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-tipo-versamento-dominio">
 * 		&lt;sequence>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idTipoVersamento" type="{http://www.govpay.it/orm}id-tipo-versamento" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "id-tipo-versamento-dominio", 
  propOrder = {
  	"idDominio",
  	"idTipoVersamento"
  }
)

@XmlRootElement(name = "id-tipo-versamento-dominio")

public class IdTipoVersamentoDominio extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public IdTipoVersamentoDominio() {
	  //donothing
  }

  public Long getId() {
    if(this.id!=null) {
		return this.id;
    } else {
		return Long.valueOf(-1);
    }
  }

  public void setId(Long id) {
    if(id!=null) {
		this.id=id;
    } else {
		this.id=Long.valueOf(-1);
    }
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public IdTipoVersamento getIdTipoVersamento() {
    return this.idTipoVersamento;
  }

  public void setIdTipoVersamento(IdTipoVersamento idTipoVersamento) {
    this.idTipoVersamento = idTipoVersamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @XmlElement(name="idTipoVersamento",required=true,nillable=false)
  protected IdTipoVersamento idTipoVersamento;

}
