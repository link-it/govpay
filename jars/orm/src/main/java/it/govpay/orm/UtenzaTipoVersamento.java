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


/** <p>Java class for UtenzaTipoVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UtenzaTipoVersamento">
 * 		&lt;sequence>
 * 			&lt;element name="idUtenza" type="{http://www.govpay.it/orm}id-utenza" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "UtenzaTipoVersamento", 
  propOrder = {
  	"idUtenza",
  	"idTipoVersamento"
  }
)

@XmlRootElement(name = "UtenzaTipoVersamento")

public class UtenzaTipoVersamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public UtenzaTipoVersamento() {
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

  public IdTipoVersamento getIdTipoVersamento() {
    return this.idTipoVersamento;
  }

  public void setIdTipoVersamento(IdTipoVersamento idTipoVersamento) {
    this.idTipoVersamento = idTipoVersamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.UtenzaTipoVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.UtenzaTipoVersamento.modelStaticInstance==null){
  			it.govpay.orm.UtenzaTipoVersamento.modelStaticInstance = new it.govpay.orm.model.UtenzaTipoVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.UtenzaTipoVersamentoModel model(){
	  if(it.govpay.orm.UtenzaTipoVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.UtenzaTipoVersamento.modelStaticInstance;
  }


  @XmlElement(name="idUtenza",required=true,nillable=false)
  protected IdUtenza idUtenza;

  @XmlElement(name="idTipoVersamento",required=true,nillable=false)
  protected IdTipoVersamento idTipoVersamento;

}
