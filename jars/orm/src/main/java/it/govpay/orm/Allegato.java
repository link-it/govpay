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


/** <p>Java class for Allegato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Allegato"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="nome" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="rawContenuto" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "Allegato", 
  propOrder = {
  	"idVersamento",
  	"nome",
  	"tipo",
  	"descrizione",
  	"dataCreazione",
  	"rawContenuto"
  }
)

@XmlRootElement(name = "Allegato")

public class Allegato extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Allegato() {
    super();
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  public java.lang.String getNome() {
    return this.nome;
  }

  public void setNome(java.lang.String nome) {
    this.nome = nome;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public java.util.Date getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(java.util.Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public byte[] getRawContenuto() {
    return this.rawContenuto;
  }

  public void setRawContenuto(byte[] rawContenuto) {
    this.rawContenuto = rawContenuto;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.AllegatoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Allegato.modelStaticInstance==null){
  			it.govpay.orm.Allegato.modelStaticInstance = new it.govpay.orm.model.AllegatoModel();
	  }
  }
  public static it.govpay.orm.model.AllegatoModel model(){
	  if(it.govpay.orm.Allegato.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Allegato.modelStaticInstance;
  }


  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=true,nillable=false)
  protected java.lang.String nome;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=false,nillable=false)
  protected java.lang.String tipo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=false,nillable=false)
  protected java.lang.String descrizione;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawContenuto",required=false,nillable=false)
  protected byte[] rawContenuto;

}
