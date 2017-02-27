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


/** <p>Java class for RPT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RPT">
 * 		&lt;sequence>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idCanale" type="{http://www.govpay.it/orm}id-canale" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idPortale" type="{http://www.govpay.it/orm}id-portale" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codCarrello" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgRichiesta" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataMsgRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSessione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSessionePortale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pspRedirectURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="xmlRPT" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataAggiornamentoStato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="callbackURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="modelloPagamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgRicevuta" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataMsgRicevuta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="firmaRicevuta" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codEsitoPagamento" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="xmlRT" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codStazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneRPT" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneRT" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "RPT", 
  propOrder = {
  	"idVersamento",
  	"idCanale",
  	"idPortale",
  	"codCarrello",
  	"iuv",
  	"ccp",
  	"codDominio",
  	"codMsgRichiesta",
  	"dataMsgRichiesta",
  	"stato",
  	"descrizioneStato",
  	"codSessione",
  	"codSessionePortale",
  	"pspRedirectURL",
  	"xmlRPT",
  	"dataAggiornamentoStato",
  	"callbackURL",
  	"modelloPagamento",
  	"codMsgRicevuta",
  	"dataMsgRicevuta",
  	"firmaRicevuta",
  	"_decimalWrapper_codEsitoPagamento",
  	"importoTotalePagato",
  	"xmlRT",
  	"codStazione",
  	"codTransazioneRPT",
  	"codTransazioneRT"
  }
)

@XmlRootElement(name = "RPT")

public class RPT extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public RPT() {
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

  public IdCanale getIdCanale() {
    return this.idCanale;
  }

  public void setIdCanale(IdCanale idCanale) {
    this.idCanale = idCanale;
  }

  public IdPortale getIdPortale() {
    return this.idPortale;
  }

  public void setIdPortale(IdPortale idPortale) {
    this.idPortale = idPortale;
  }

  public java.lang.String getCodCarrello() {
    return this.codCarrello;
  }

  public void setCodCarrello(java.lang.String codCarrello) {
    this.codCarrello = codCarrello;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getCcp() {
    return this.ccp;
  }

  public void setCcp(java.lang.String ccp) {
    this.ccp = ccp;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getCodMsgRichiesta() {
    return this.codMsgRichiesta;
  }

  public void setCodMsgRichiesta(java.lang.String codMsgRichiesta) {
    this.codMsgRichiesta = codMsgRichiesta;
  }

  public java.util.Date getDataMsgRichiesta() {
    return this.dataMsgRichiesta;
  }

  public void setDataMsgRichiesta(java.util.Date dataMsgRichiesta) {
    this.dataMsgRichiesta = dataMsgRichiesta;
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

  public java.lang.String getCodSessione() {
    return this.codSessione;
  }

  public void setCodSessione(java.lang.String codSessione) {
    this.codSessione = codSessione;
  }

  public java.lang.String getCodSessionePortale() {
    return this.codSessionePortale;
  }

  public void setCodSessionePortale(java.lang.String codSessionePortale) {
    this.codSessionePortale = codSessionePortale;
  }

  public java.lang.String getPspRedirectURL() {
    return this.pspRedirectURL;
  }

  public void setPspRedirectURL(java.lang.String pspRedirectURL) {
    this.pspRedirectURL = pspRedirectURL;
  }

  public byte[] getXmlRPT() {
    return this.xmlRPT;
  }

  public void setXmlRPT(byte[] xmlRPT) {
    this.xmlRPT = xmlRPT;
  }

  public java.util.Date getDataAggiornamentoStato() {
    return this.dataAggiornamentoStato;
  }

  public void setDataAggiornamentoStato(java.util.Date dataAggiornamentoStato) {
    this.dataAggiornamentoStato = dataAggiornamentoStato;
  }

  public java.lang.String getCallbackURL() {
    return this.callbackURL;
  }

  public void setCallbackURL(java.lang.String callbackURL) {
    this.callbackURL = callbackURL;
  }

  public java.lang.String getModelloPagamento() {
    return this.modelloPagamento;
  }

  public void setModelloPagamento(java.lang.String modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
  }

  public java.lang.String getCodMsgRicevuta() {
    return this.codMsgRicevuta;
  }

  public void setCodMsgRicevuta(java.lang.String codMsgRicevuta) {
    this.codMsgRicevuta = codMsgRicevuta;
  }

  public java.util.Date getDataMsgRicevuta() {
    return this.dataMsgRicevuta;
  }

  public void setDataMsgRicevuta(java.util.Date dataMsgRicevuta) {
    this.dataMsgRicevuta = dataMsgRicevuta;
  }

  public java.lang.String getFirmaRicevuta() {
    return this.firmaRicevuta;
  }

  public void setFirmaRicevuta(java.lang.String firmaRicevuta) {
    this.firmaRicevuta = firmaRicevuta;
  }

  public java.lang.Integer getCodEsitoPagamento() {
    if(this._decimalWrapper_codEsitoPagamento!=null){
		return (java.lang.Integer) this._decimalWrapper_codEsitoPagamento.getObject(java.lang.Integer.class);
	}else{
		return this.codEsitoPagamento;
	}
  }

  public void setCodEsitoPagamento(java.lang.Integer codEsitoPagamento) {
    if(codEsitoPagamento!=null){
		this._decimalWrapper_codEsitoPagamento = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,codEsitoPagamento);
	}
  }

  public java.lang.Double getImportoTotalePagato() {
    return this.importoTotalePagato;
  }

  public void setImportoTotalePagato(java.lang.Double importoTotalePagato) {
    this.importoTotalePagato = importoTotalePagato;
  }

  public byte[] getXmlRT() {
    return this.xmlRT;
  }

  public void setXmlRT(byte[] xmlRT) {
    this.xmlRT = xmlRT;
  }

  public java.lang.String getCodStazione() {
    return this.codStazione;
  }

  public void setCodStazione(java.lang.String codStazione) {
    this.codStazione = codStazione;
  }

  public java.lang.String getCodTransazioneRPT() {
    return this.codTransazioneRPT;
  }

  public void setCodTransazioneRPT(java.lang.String codTransazioneRPT) {
    this.codTransazioneRPT = codTransazioneRPT;
  }

  public java.lang.String getCodTransazioneRT() {
    return this.codTransazioneRT;
  }

  public void setCodTransazioneRT(java.lang.String codTransazioneRT) {
    this.codTransazioneRT = codTransazioneRT;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.RPTModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.RPT.modelStaticInstance==null){
  			it.govpay.orm.RPT.modelStaticInstance = new it.govpay.orm.model.RPTModel();
	  }
  }
  public static it.govpay.orm.model.RPTModel model(){
	  if(it.govpay.orm.RPT.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.RPT.modelStaticInstance;
  }


  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @XmlElement(name="idCanale",required=true,nillable=false)
  protected IdCanale idCanale;

  @XmlElement(name="idPortale",required=false,nillable=false)
  protected IdPortale idPortale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCarrello",required=false,nillable=false)
  protected java.lang.String codCarrello;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ccp",required=true,nillable=false)
  protected java.lang.String ccp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRichiesta",required=true,nillable=false)
  protected java.lang.String codMsgRichiesta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataMsgRichiesta",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataMsgRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSessione",required=false,nillable=false)
  protected java.lang.String codSessione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSessionePortale",required=false,nillable=false)
  protected java.lang.String codSessionePortale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspRedirectURL",required=false,nillable=false)
  protected java.lang.String pspRedirectURL;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlRPT",required=true,nillable=false)
  protected byte[] xmlRPT;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAggiornamentoStato",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAggiornamentoStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="callbackURL",required=false,nillable=false)
  protected java.lang.String callbackURL;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="modelloPagamento",required=true,nillable=false)
  protected java.lang.String modelloPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRicevuta",required=false,nillable=false)
  protected java.lang.String codMsgRicevuta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataMsgRicevuta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataMsgRicevuta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="firmaRicevuta",required=true,nillable=false)
  protected java.lang.String firmaRicevuta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="codEsitoPagamento",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_codEsitoPagamento = null;

  @XmlTransient
  protected java.lang.Integer codEsitoPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoTotalePagato",required=false,nillable=false)
  protected java.lang.Double importoTotalePagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xmlRT",required=false,nillable=false)
  protected byte[] xmlRT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codStazione",required=true,nillable=false)
  protected java.lang.String codStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneRPT",required=false,nillable=false)
  protected java.lang.String codTransazioneRPT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneRT",required=false,nillable=false)
  protected java.lang.String codTransazioneRT;

}
