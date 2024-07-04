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


/** <p>Java class for Configurazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Configurazione"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="nome" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="valore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "Configurazione", 
  propOrder = {
  	"nome",
  	"valore"
  }
)

@XmlRootElement(name = "Configurazione")

public class Configurazione extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Configurazione() {
    super();
  }

  public java.lang.String getNome() {
    return this.nome;
  }

  public void setNome(java.lang.String nome) {
    this.nome = nome;
  }

  public java.lang.String getValore() {
    return this.valore;
  }

  public void setValore(java.lang.String valore) {
    this.valore = valore;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.ConfigurazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Configurazione.modelStaticInstance==null){
  			it.govpay.orm.Configurazione.modelStaticInstance = new it.govpay.orm.model.ConfigurazioneModel();
	  }
  }
  public static it.govpay.orm.model.ConfigurazioneModel model(){
	  if(it.govpay.orm.Configurazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Configurazione.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=true,nillable=false)
  protected java.lang.String nome;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="valore",required=false,nillable=false)
  protected java.lang.String valore;

}
