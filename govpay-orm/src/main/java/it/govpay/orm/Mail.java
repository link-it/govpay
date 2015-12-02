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


/** <p>Java class for Mail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mail">
 * 		&lt;sequence>
 * 			&lt;element name="tipoMail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="bundleKey" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idVersamento" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="mittente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="destinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cc" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="oggetto" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="messaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="statoSpedizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dettaglioErroreSpedizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraUltimaSpedizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tentativiRispedizione" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTracciatoRPT" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTracciatoRT" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Mail", 
  propOrder = {
  	"tipoMail",
  	"bundleKey",
  	"idVersamento",
  	"mittente",
  	"destinatario",
  	"cc",
  	"oggetto",
  	"messaggio",
  	"statoSpedizione",
  	"dettaglioErroreSpedizione",
  	"dataOraUltimaSpedizione",
  	"tentativiRispedizione",
  	"idTracciatoRPT",
  	"idTracciatoRT"
  }
)

@XmlRootElement(name = "Mail")

public class Mail extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Mail() {
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

  public java.lang.String getTipoMail() {
    return this.tipoMail;
  }

  public void setTipoMail(java.lang.String tipoMail) {
    this.tipoMail = tipoMail;
  }

  public long getBundleKey() {
    return this.bundleKey;
  }

  public void setBundleKey(long bundleKey) {
    this.bundleKey = bundleKey;
  }

  public java.lang.Long getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(java.lang.Long idVersamento) {
    this.idVersamento = idVersamento;
  }

  public java.lang.String getMittente() {
    return this.mittente;
  }

  public void setMittente(java.lang.String mittente) {
    this.mittente = mittente;
  }

  public java.lang.String getDestinatario() {
    return this.destinatario;
  }

  public void setDestinatario(java.lang.String destinatario) {
    this.destinatario = destinatario;
  }

  public java.lang.String getCc() {
    return this.cc;
  }

  public void setCc(java.lang.String cc) {
    this.cc = cc;
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

  public java.lang.String getStatoSpedizione() {
    return this.statoSpedizione;
  }

  public void setStatoSpedizione(java.lang.String statoSpedizione) {
    this.statoSpedizione = statoSpedizione;
  }

  public java.lang.String getDettaglioErroreSpedizione() {
    return this.dettaglioErroreSpedizione;
  }

  public void setDettaglioErroreSpedizione(java.lang.String dettaglioErroreSpedizione) {
    this.dettaglioErroreSpedizione = dettaglioErroreSpedizione;
  }

  public java.util.Date getDataOraUltimaSpedizione() {
    return this.dataOraUltimaSpedizione;
  }

  public void setDataOraUltimaSpedizione(java.util.Date dataOraUltimaSpedizione) {
    this.dataOraUltimaSpedizione = dataOraUltimaSpedizione;
  }

  public java.lang.Long getTentativiRispedizione() {
    return this.tentativiRispedizione;
  }

  public void setTentativiRispedizione(java.lang.Long tentativiRispedizione) {
    this.tentativiRispedizione = tentativiRispedizione;
  }

  public IdTracciato getIdTracciatoRPT() {
    return this.idTracciatoRPT;
  }

  public void setIdTracciatoRPT(IdTracciato idTracciatoRPT) {
    this.idTracciatoRPT = idTracciatoRPT;
  }

  public IdTracciato getIdTracciatoRT() {
    return this.idTracciatoRT;
  }

  public void setIdTracciatoRT(IdTracciato idTracciatoRT) {
    this.idTracciatoRT = idTracciatoRT;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.MailModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Mail.modelStaticInstance==null){
  			it.govpay.orm.Mail.modelStaticInstance = new it.govpay.orm.model.MailModel();
	  }
  }
  public static it.govpay.orm.model.MailModel model(){
	  if(it.govpay.orm.Mail.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Mail.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoMail",required=true,nillable=false)
  protected java.lang.String tipoMail;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="bundleKey",required=true,nillable=false)
  protected long bundleKey;

  @javax.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="idVersamento",required=false,nillable=false)
  protected java.lang.Long idVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="mittente",required=true,nillable=false)
  protected java.lang.String mittente;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="destinatario",required=true,nillable=false)
  protected java.lang.String destinatario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cc",required=false,nillable=false)
  protected java.lang.String cc;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="oggetto",required=true,nillable=false)
  protected java.lang.String oggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="messaggio",required=true,nillable=false)
  protected java.lang.String messaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoSpedizione",required=true,nillable=false)
  protected java.lang.String statoSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioErroreSpedizione",required=false,nillable=false)
  protected java.lang.String dettaglioErroreSpedizione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraUltimaSpedizione",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraUltimaSpedizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="tentativiRispedizione",required=false,nillable=false)
  protected java.lang.Long tentativiRispedizione;

  @XmlElement(name="idTracciatoRPT",required=false,nillable=false)
  protected IdTracciato idTracciatoRPT;

  @XmlElement(name="idTracciatoRT",required=false,nillable=false)
  protected IdTracciato idTracciatoRT;

}
