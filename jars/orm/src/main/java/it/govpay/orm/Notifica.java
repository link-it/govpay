/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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


/** <p>Java class for Notifica complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Notifica">
 * 		&lt;sequence>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idRpt" type="{http://www.govpay.it/orm}id-rpt" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoEsito" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Notifica",
  propOrder = {
  	"idApplicazione",
  	"idRpt",
  	"tipoEsito",
  	"dataCreazione",
  	"stato",
  	"descrizioneStato",
  	"dataAggiornamentoStato",
  	"dataProssimaSpedizione",
  	"tentativiSpedizione"
  }
)

@XmlRootElement(name = "Notifica")

public class Notifica extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Notifica() {
    super();
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdRpt getIdRpt() {
    return this.idRpt;
  }

  public void setIdRpt(IdRpt idRpt) {
    this.idRpt = idRpt;
  }

  public java.lang.String getTipoEsito() {
    return this.tipoEsito;
  }

  public void setTipoEsito(java.lang.String tipoEsito) {
    this.tipoEsito = tipoEsito;
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

  private static it.govpay.orm.model.NotificaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Notifica.modelStaticInstance==null){
  			it.govpay.orm.Notifica.modelStaticInstance = new it.govpay.orm.model.NotificaModel();
	  }
  }
  public static it.govpay.orm.model.NotificaModel model(){
	  if(it.govpay.orm.Notifica.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Notifica.modelStaticInstance;
  }


  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @XmlElement(name="idRpt",required=false,nillable=false)
  protected IdRpt idRpt;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoEsito",required=true,nillable=false)
  protected java.lang.String tipoEsito;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAggiornamentoStato",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAggiornamentoStato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataProssimaSpedizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataProssimaSpedizione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="unsignedLong")
  @XmlElement(name="tentativiSpedizione",required=false,nillable=false)
  protected java.lang.Long tentativiSpedizione;

}
