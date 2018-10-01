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


/** <p>Java class for UtenzaDominio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UtenzaDominio">
 * 		&lt;sequence>
 * 			&lt;element name="idUtenza" type="{http://www.govpay.it/orm}id-utenza" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "UtenzaDominio", 
  propOrder = {
  	"idUtenza",
  	"idDominio"
  }
)

@XmlRootElement(name = "UtenzaDominio")

public class UtenzaDominio extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public UtenzaDominio() {
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

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.UtenzaDominioModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.UtenzaDominio.modelStaticInstance==null){
  			it.govpay.orm.UtenzaDominio.modelStaticInstance = new it.govpay.orm.model.UtenzaDominioModel();
	  }
  }
  public static it.govpay.orm.model.UtenzaDominioModel model(){
	  if(it.govpay.orm.UtenzaDominio.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.UtenzaDominio.modelStaticInstance;
  }


  @XmlElement(name="idUtenza",required=true,nillable=false)
  protected IdUtenza idUtenza;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

}
