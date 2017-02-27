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


/** <p>Java class for RendicontazionePagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RendicontazionePagamento">
 * 		&lt;sequence>
 * 			&lt;element name="FR" type="{http://www.govpay.it/orm}FR" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="Rendicontazione" type="{http://www.govpay.it/orm}Rendicontazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="Pagamento" type="{http://www.govpay.it/orm}Pagamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="SingoloVersamento" type="{http://www.govpay.it/orm}SingoloVersamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="Versamento" type="{http://www.govpay.it/orm}Versamento" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "RendicontazionePagamento", 
  propOrder = {
  	"fr",
  	"rendicontazione",
  	"pagamento",
  	"singoloVersamento",
  	"versamento"
  }
)

@XmlRootElement(name = "RendicontazionePagamento")

public class RendicontazionePagamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public RendicontazionePagamento() {
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

  public FR getFr() {
    return this.fr;
  }

  public void setFr(FR fr) {
    this.fr = fr;
  }

  public Rendicontazione getRendicontazione() {
    return this.rendicontazione;
  }

  public void setRendicontazione(Rendicontazione rendicontazione) {
    this.rendicontazione = rendicontazione;
  }

  public Pagamento getPagamento() {
    return this.pagamento;
  }

  public void setPagamento(Pagamento pagamento) {
    this.pagamento = pagamento;
  }

  public SingoloVersamento getSingoloVersamento() {
    return this.singoloVersamento;
  }

  public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
    this.singoloVersamento = singoloVersamento;
  }

  public Versamento getVersamento() {
    return this.versamento;
  }

  public void setVersamento(Versamento versamento) {
    this.versamento = versamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.RendicontazionePagamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.RendicontazionePagamento.modelStaticInstance==null){
  			it.govpay.orm.RendicontazionePagamento.modelStaticInstance = new it.govpay.orm.model.RendicontazionePagamentoModel();
	  }
  }
  public static it.govpay.orm.model.RendicontazionePagamentoModel model(){
	  if(it.govpay.orm.RendicontazionePagamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.RendicontazionePagamento.modelStaticInstance;
  }


  @XmlElement(name="FR",required=true,nillable=false)
  protected FR fr;

  @XmlElement(name="Rendicontazione",required=true,nillable=false)
  protected Rendicontazione rendicontazione;

  @XmlElement(name="Pagamento",required=true,nillable=false)
  protected Pagamento pagamento;

  @XmlElement(name="SingoloVersamento",required=true,nillable=false)
  protected SingoloVersamento singoloVersamento;

  @XmlElement(name="Versamento",required=true,nillable=false)
  protected Versamento versamento;

}
