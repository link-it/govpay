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


/** <p>Java class for RPT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RPT">
 * 		&lt;sequence>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idPsp" type="{http://www.govpay.it/orm}id-psp" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idCanale" type="{http://www.govpay.it/orm}id-canale" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idPortale" type="{http://www.govpay.it/orm}id-portale" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTracciatoXML" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idStazione" type="{http://www.govpay.it/orm}id-stazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codCarrello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idAnagraficaVersante" type="{http://www.govpay.it/orm}id-anagrafica" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraMsgRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgRichiesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ibanAddebito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="autenticazioneSoggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="firmaRT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codFault" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="callbackURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSessione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pspRedirectURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
  	"idPsp",
  	"idCanale",
  	"idPortale",
  	"idTracciatoXML",
  	"idStazione",
  	"codCarrello",
  	"iuv",
  	"ccp",
  	"codDominio",
  	"tipoVersamento",
  	"idAnagraficaVersante",
  	"dataOraMsgRichiesta",
  	"dataOraCreazione",
  	"codMsgRichiesta",
  	"ibanAddebito",
  	"autenticazioneSoggetto",
  	"firmaRT",
  	"stato",
  	"descrizioneStato",
  	"codFault",
  	"callbackURL",
  	"codSessione",
  	"pspRedirectURL"
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

  public IdPsp getIdPsp() {
    return this.idPsp;
  }

  public void setIdPsp(IdPsp idPsp) {
    this.idPsp = idPsp;
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

  public IdTracciato getIdTracciatoXML() {
    return this.idTracciatoXML;
  }

  public void setIdTracciatoXML(IdTracciato idTracciatoXML) {
    this.idTracciatoXML = idTracciatoXML;
  }

  public IdStazione getIdStazione() {
    return this.idStazione;
  }

  public void setIdStazione(IdStazione idStazione) {
    this.idStazione = idStazione;
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

  public java.lang.String getTipoVersamento() {
    return this.tipoVersamento;
  }

  public void setTipoVersamento(java.lang.String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  public IdAnagrafica getIdAnagraficaVersante() {
    return this.idAnagraficaVersante;
  }

  public void setIdAnagraficaVersante(IdAnagrafica idAnagraficaVersante) {
    this.idAnagraficaVersante = idAnagraficaVersante;
  }

  public java.util.Date getDataOraMsgRichiesta() {
    return this.dataOraMsgRichiesta;
  }

  public void setDataOraMsgRichiesta(java.util.Date dataOraMsgRichiesta) {
    this.dataOraMsgRichiesta = dataOraMsgRichiesta;
  }

  public java.util.Date getDataOraCreazione() {
    return this.dataOraCreazione;
  }

  public void setDataOraCreazione(java.util.Date dataOraCreazione) {
    this.dataOraCreazione = dataOraCreazione;
  }

  public java.lang.String getCodMsgRichiesta() {
    return this.codMsgRichiesta;
  }

  public void setCodMsgRichiesta(java.lang.String codMsgRichiesta) {
    this.codMsgRichiesta = codMsgRichiesta;
  }

  public java.lang.String getIbanAddebito() {
    return this.ibanAddebito;
  }

  public void setIbanAddebito(java.lang.String ibanAddebito) {
    this.ibanAddebito = ibanAddebito;
  }

  public java.lang.String getAutenticazioneSoggetto() {
    return this.autenticazioneSoggetto;
  }

  public void setAutenticazioneSoggetto(java.lang.String autenticazioneSoggetto) {
    this.autenticazioneSoggetto = autenticazioneSoggetto;
  }

  public java.lang.String getFirmaRT() {
    return this.firmaRT;
  }

  public void setFirmaRT(java.lang.String firmaRT) {
    this.firmaRT = firmaRT;
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

  public java.lang.String getCodFault() {
    return this.codFault;
  }

  public void setCodFault(java.lang.String codFault) {
    this.codFault = codFault;
  }

  public java.lang.String getCallbackURL() {
    return this.callbackURL;
  }

  public void setCallbackURL(java.lang.String callbackURL) {
    this.callbackURL = callbackURL;
  }

  public java.lang.String getCodSessione() {
    return this.codSessione;
  }

  public void setCodSessione(java.lang.String codSessione) {
    this.codSessione = codSessione;
  }

  public java.lang.String getPspRedirectURL() {
    return this.pspRedirectURL;
  }

  public void setPspRedirectURL(java.lang.String pspRedirectURL) {
    this.pspRedirectURL = pspRedirectURL;
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

  @XmlElement(name="idPsp",required=false,nillable=false)
  protected IdPsp idPsp;

  @XmlElement(name="idCanale",required=false,nillable=false)
  protected IdCanale idCanale;

  @XmlElement(name="idPortale",required=false,nillable=false)
  protected IdPortale idPortale;

  @XmlElement(name="idTracciatoXML",required=false,nillable=false)
  protected IdTracciato idTracciatoXML;

  @XmlElement(name="idStazione",required=false,nillable=false)
  protected IdStazione idStazione;

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
  @XmlElement(name="tipoVersamento",required=false,nillable=false)
  protected java.lang.String tipoVersamento;

  @XmlElement(name="idAnagraficaVersante",required=false,nillable=false)
  protected IdAnagrafica idAnagraficaVersante;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraMsgRichiesta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraMsgRichiesta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraCreazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRichiesta",required=true,nillable=false)
  protected java.lang.String codMsgRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ibanAddebito",required=false,nillable=false)
  protected java.lang.String ibanAddebito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="autenticazioneSoggetto",required=false,nillable=false)
  protected java.lang.String autenticazioneSoggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="firmaRT",required=true,nillable=false)
  protected java.lang.String firmaRT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codFault",required=false,nillable=false)
  protected java.lang.String codFault;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="callbackURL",required=false,nillable=false)
  protected java.lang.String callbackURL;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSessione",required=false,nillable=false)
  protected java.lang.String codSessione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspRedirectURL",required=false,nillable=false)
  protected java.lang.String pspRedirectURL;

}
