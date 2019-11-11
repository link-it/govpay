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


/** <p>Java class for Promemoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Promemoria">
 * 		&lt;sequence>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idRPT" type="{http://www.govpay.it/orm}id-rpt" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="destinatarioTo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="destinatarioCc" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="messaggioContentType" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="oggetto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="messaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="allegaPdf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="dataAggiornamentoStato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataProssimaSpedizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tentativiSpedizione" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Promemoria", 
  propOrder = {
  	"idVersamento",
  	"idRPT",
  	"tipo",
  	"dataCreazione",
  	"stato",
  	"descrizioneStato",
  	"destinatarioTo",
  	"destinatarioCc",
  	"messaggioContentType",
  	"oggetto",
  	"messaggio",
  	"allegaPdf",
  	"dataAggiornamentoStato",
  	"dataProssimaSpedizione",
  	"tentativiSpedizione"
  }
)

@XmlRootElement(name = "Promemoria")

public class Promemoria extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Promemoria() {
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

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  public IdRpt getIdRPT() {
    return this.idRPT;
  }

  public void setIdRPT(IdRpt idRPT) {
    this.idRPT = idRPT;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public java.util.Date getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(java.util.Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public java.lang.String getDestinatarioTo() {
    return this.destinatarioTo;
  }

  public void setDestinatarioTo(java.lang.String destinatarioTo) {
    this.destinatarioTo = destinatarioTo;
  }

  public java.lang.String getDestinatarioCc() {
    return this.destinatarioCc;
  }

  public void setDestinatarioCc(java.lang.String destinatarioCc) {
    this.destinatarioCc = destinatarioCc;
  }

  public java.lang.String getMessaggioContentType() {
    return this.messaggioContentType;
  }

  public void setMessaggioContentType(java.lang.String messaggioContentType) {
    this.messaggioContentType = messaggioContentType;
  }

  public java.lang.String getOggetto() {
    return this.oggetto;
  }

  public void setOggetto(java.lang.String oggetto) {
    this.oggetto = oggetto;
  }

  public java.lang.String getMessaggio() {
    return this.messaggio;
  }

  public void setMessaggio(java.lang.String messaggio) {
    this.messaggio = messaggio;
  }

  public boolean isAllegaPdf() {
    return this.allegaPdf;
  }

  public boolean getAllegaPdf() {
    return this.allegaPdf;
  }

  public void setAllegaPdf(boolean allegaPdf) {
    this.allegaPdf = allegaPdf;
  }

  public java.util.Date getDataAggiornamentoStato() {
    return this.dataAggiornamentoStato;
  }

  public void setDataAggiornamentoStato(java.util.Date dataAggiornamentoStato) {
    this.dataAggiornamentoStato = dataAggiornamentoStato;
  }

  public java.util.Date getDataProssimaSpedizione() {
    return this.dataProssimaSpedizione;
  }

  public void setDataProssimaSpedizione(java.util.Date dataProssimaSpedizione) {
    this.dataProssimaSpedizione = dataProssimaSpedizione;
  }

  public java.lang.Long getTentativiSpedizione() {
    return this.tentativiSpedizione;
  }

  public void setTentativiSpedizione(java.lang.Long tentativiSpedizione) {
    this.tentativiSpedizione = tentativiSpedizione;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.PromemoriaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Promemoria.modelStaticInstance==null){
  			it.govpay.orm.Promemoria.modelStaticInstance = new it.govpay.orm.model.PromemoriaModel();
	  }
  }
  public static it.govpay.orm.model.PromemoriaModel model(){
	  if(it.govpay.orm.Promemoria.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Promemoria.modelStaticInstance;
  }


  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @XmlElement(name="idRPT",required=false,nillable=false)
  protected IdRpt idRPT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="destinatarioTo",required=true,nillable=false)
  protected java.lang.String destinatarioTo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="destinatarioCc",required=false,nillable=false)
  protected java.lang.String destinatarioCc;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="messaggioContentType",required=false,nillable=false)
  protected java.lang.String messaggioContentType;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="oggetto",required=false,nillable=false)
  protected java.lang.String oggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="messaggio",required=false,nillable=false)
  protected java.lang.String messaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="allegaPdf",required=true,nillable=false,defaultValue="false")
  protected boolean allegaPdf = false;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAggiornamentoStato",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAggiornamentoStato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataProssimaSpedizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataProssimaSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="tentativiSpedizione",required=false,nillable=false)
  protected java.lang.Long tentativiSpedizione;

}
