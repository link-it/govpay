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


/** <p>Java class for Incasso complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Incasso">
 * 		&lt;sequence>
 * 			&lt;element name="trn" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="causale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importo" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataValuta" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataContabile" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraIncasso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nomeDispositivo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ibanAccredito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sct" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="identificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codFlussoRendicontazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Incasso", 
  propOrder = {
  	"trn",
  	"codDominio",
  	"causale",
  	"importo",
  	"dataValuta",
  	"dataContabile",
  	"dataOraIncasso",
  	"nomeDispositivo",
  	"idApplicazione",
  	"idOperatore",
  	"ibanAccredito",
  	"sct",
  	"identificativo",
  	"iuv",
  	"codFlussoRendicontazione",
  	"stato",
  	"descrizioneStato"
  }
)

@XmlRootElement(name = "Incasso")

public class Incasso extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Incasso() {
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

  public java.lang.String getTrn() {
    return this.trn;
  }

  public void setTrn(java.lang.String trn) {
    this.trn = trn;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getCausale() {
    return this.causale;
  }

  public void setCausale(java.lang.String causale) {
    this.causale = causale;
  }

  public java.lang.Double getImporto() {
    return this.importo;
  }

  public void setImporto(java.lang.Double importo) {
    this.importo = importo;
  }

  public java.util.Date getDataValuta() {
    return this.dataValuta;
  }

  public void setDataValuta(java.util.Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  public java.util.Date getDataContabile() {
    return this.dataContabile;
  }

  public void setDataContabile(java.util.Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  public java.util.Date getDataOraIncasso() {
    return this.dataOraIncasso;
  }

  public void setDataOraIncasso(java.util.Date dataOraIncasso) {
    this.dataOraIncasso = dataOraIncasso;
  }

  public java.lang.String getNomeDispositivo() {
    return this.nomeDispositivo;
  }

  public void setNomeDispositivo(java.lang.String nomeDispositivo) {
    this.nomeDispositivo = nomeDispositivo;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdOperatore getIdOperatore() {
    return this.idOperatore;
  }

  public void setIdOperatore(IdOperatore idOperatore) {
    this.idOperatore = idOperatore;
  }

  public java.lang.String getIbanAccredito() {
    return this.ibanAccredito;
  }

  public void setIbanAccredito(java.lang.String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public java.lang.String getSct() {
    return this.sct;
  }

  public void setSct(java.lang.String sct) {
    this.sct = sct;
  }

  public java.lang.String getIdentificativo() {
    return this.identificativo;
  }

  public void setIdentificativo(java.lang.String identificativo) {
    this.identificativo = identificativo;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getCodFlussoRendicontazione() {
    return this.codFlussoRendicontazione;
  }

  public void setCodFlussoRendicontazione(java.lang.String codFlussoRendicontazione) {
    this.codFlussoRendicontazione = codFlussoRendicontazione;
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

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.IncassoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Incasso.modelStaticInstance==null){
  			it.govpay.orm.Incasso.modelStaticInstance = new it.govpay.orm.model.IncassoModel();
	  }
  }
  public static it.govpay.orm.model.IncassoModel model(){
	  if(it.govpay.orm.Incasso.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Incasso.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="trn",required=true,nillable=false)
  protected java.lang.String trn;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causale",required=false,nillable=false)
  protected java.lang.String causale;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importo",required=true,nillable=false)
  protected java.lang.Double importo;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataValuta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataValuta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataContabile",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataContabile;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraIncasso",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraIncasso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nomeDispositivo",required=false,nillable=false)
  protected java.lang.String nomeDispositivo;

  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @XmlElement(name="idOperatore",required=false,nillable=false)
  protected IdOperatore idOperatore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ibanAccredito",required=false,nillable=false)
  protected java.lang.String ibanAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sct",required=false,nillable=false)
  protected java.lang.String sct;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="identificativo",required=true,nillable=false)
  protected java.lang.String identificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codFlussoRendicontazione",required=false,nillable=false)
  protected java.lang.String codFlussoRendicontazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

}
