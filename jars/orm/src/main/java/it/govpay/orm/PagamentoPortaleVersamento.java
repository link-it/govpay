/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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


/** <p>Java class for PagamentoPortaleVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PagamentoPortaleVersamento"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="idPagamentoPortale" type="{http://www.govpay.it/orm}id-pagamento-portale" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "PagamentoPortaleVersamento", 
  propOrder = {
  	"idPagamentoPortale",
  	"idVersamento"
  }
)

@XmlRootElement(name = "PagamentoPortaleVersamento")

public class PagamentoPortaleVersamento extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public PagamentoPortaleVersamento() {
    super();
  }

  public IdPagamentoPortale getIdPagamentoPortale() {
    return this.idPagamentoPortale;
  }

  public void setIdPagamentoPortale(IdPagamentoPortale idPagamentoPortale) {
    this.idPagamentoPortale = idPagamentoPortale;
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.PagamentoPortaleVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.PagamentoPortaleVersamento.modelStaticInstance==null){
  			it.govpay.orm.PagamentoPortaleVersamento.modelStaticInstance = new it.govpay.orm.model.PagamentoPortaleVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.PagamentoPortaleVersamentoModel model(){
	  if(it.govpay.orm.PagamentoPortaleVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.PagamentoPortaleVersamento.modelStaticInstance;
  }


  @XmlElement(name="idPagamentoPortale",required=true,nillable=false)
  protected IdPagamentoPortale idPagamentoPortale;

  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

}
