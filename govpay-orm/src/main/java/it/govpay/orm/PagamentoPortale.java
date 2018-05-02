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


/** <p>Java class for PagamentoPortale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PagamentoPortale">
 * 		&lt;sequence>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="nome" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="versanteIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idSessione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idSessionePortale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idSessionePsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codiceStato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pspRedirectURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pspEsito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="jsonRequest" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="wispIdDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="wispKeyPA" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="wispKeyWisp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="wispHtml" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="urlRitorno" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="multiBeneficiario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "PagamentoPortale", 
  propOrder = {
  	"codApplicazione",
  	"codCanale",
  	"nome",
  	"importo",
  	"versanteIdentificativo",
  	"idSessione",
  	"idSessionePortale",
  	"idSessionePsp",
  	"stato",
  	"codiceStato",
  	"descrizioneStato",
  	"pspRedirectURL",
  	"pspEsito",
  	"jsonRequest",
  	"wispIdDominio",
  	"wispKeyPA",
  	"wispKeyWisp",
  	"wispHtml",
  	"dataRichiesta",
  	"urlRitorno",
  	"codPsp",
  	"tipoVersamento",
  	"multiBeneficiario"
  }
)

@XmlRootElement(name = "PagamentoPortale")

public class PagamentoPortale extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public PagamentoPortale() {
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

  public java.lang.String getCodApplicazione() {
    return this.codApplicazione;
  }

  public void setCodApplicazione(java.lang.String codApplicazione) {
    this.codApplicazione = codApplicazione;
  }

  public java.lang.String getCodCanale() {
    return this.codCanale;
  }

  public void setCodCanale(java.lang.String codCanale) {
    this.codCanale = codCanale;
  }

  public java.lang.String getNome() {
    return this.nome;
  }

  public void setNome(java.lang.String nome) {
    this.nome = nome;
  }

  public double getImporto() {
    return this.importo;
  }

  public void setImporto(double importo) {
    this.importo = importo;
  }

  public java.lang.String getVersanteIdentificativo() {
    return this.versanteIdentificativo;
  }

  public void setVersanteIdentificativo(java.lang.String versanteIdentificativo) {
    this.versanteIdentificativo = versanteIdentificativo;
  }

  public java.lang.String getIdSessione() {
    return this.idSessione;
  }

  public void setIdSessione(java.lang.String idSessione) {
    this.idSessione = idSessione;
  }

  public java.lang.String getIdSessionePortale() {
    return this.idSessionePortale;
  }

  public void setIdSessionePortale(java.lang.String idSessionePortale) {
    this.idSessionePortale = idSessionePortale;
  }

  public java.lang.String getIdSessionePsp() {
    return this.idSessionePsp;
  }

  public void setIdSessionePsp(java.lang.String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getCodiceStato() {
    return this.codiceStato;
  }

  public void setCodiceStato(java.lang.String codiceStato) {
    this.codiceStato = codiceStato;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public java.lang.String getPspRedirectURL() {
    return this.pspRedirectURL;
  }

  public void setPspRedirectURL(java.lang.String pspRedirectURL) {
    this.pspRedirectURL = pspRedirectURL;
  }

  public java.lang.String getPspEsito() {
    return this.pspEsito;
  }

  public void setPspEsito(java.lang.String pspEsito) {
    this.pspEsito = pspEsito;
  }

  public java.lang.String getJsonRequest() {
    return this.jsonRequest;
  }

  public void setJsonRequest(java.lang.String jsonRequest) {
    this.jsonRequest = jsonRequest;
  }

  public java.lang.String getWispIdDominio() {
    return this.wispIdDominio;
  }

  public void setWispIdDominio(java.lang.String wispIdDominio) {
    this.wispIdDominio = wispIdDominio;
  }

  public java.lang.String getWispKeyPA() {
    return this.wispKeyPA;
  }

  public void setWispKeyPA(java.lang.String wispKeyPA) {
    this.wispKeyPA = wispKeyPA;
  }

  public java.lang.String getWispKeyWisp() {
    return this.wispKeyWisp;
  }

  public void setWispKeyWisp(java.lang.String wispKeyWisp) {
    this.wispKeyWisp = wispKeyWisp;
  }

  public java.lang.String getWispHtml() {
    return this.wispHtml;
  }

  public void setWispHtml(java.lang.String wispHtml) {
    this.wispHtml = wispHtml;
  }

  public java.util.Date getDataRichiesta() {
    return this.dataRichiesta;
  }

  public void setDataRichiesta(java.util.Date dataRichiesta) {
    this.dataRichiesta = dataRichiesta;
  }

  public java.lang.String getUrlRitorno() {
    return this.urlRitorno;
  }

  public void setUrlRitorno(java.lang.String urlRitorno) {
    this.urlRitorno = urlRitorno;
  }

  public java.lang.String getCodPsp() {
    return this.codPsp;
  }

  public void setCodPsp(java.lang.String codPsp) {
    this.codPsp = codPsp;
  }

  public java.lang.String getTipoVersamento() {
    return this.tipoVersamento;
  }

  public void setTipoVersamento(java.lang.String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  public java.lang.String getMultiBeneficiario() {
    return this.multiBeneficiario;
  }

  public void setMultiBeneficiario(java.lang.String multiBeneficiario) {
    this.multiBeneficiario = multiBeneficiario;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.PagamentoPortaleModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.PagamentoPortale.modelStaticInstance==null){
  			it.govpay.orm.PagamentoPortale.modelStaticInstance = new it.govpay.orm.model.PagamentoPortaleModel();
	  }
  }
  public static it.govpay.orm.model.PagamentoPortaleModel model(){
	  if(it.govpay.orm.PagamentoPortale.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.PagamentoPortale.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=true,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCanale",required=false,nillable=false)
  protected java.lang.String codCanale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=true,nillable=false)
  protected java.lang.String nome;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importo",required=true,nillable=false)
  protected double importo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="versanteIdentificativo",required=false,nillable=false)
  protected java.lang.String versanteIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessione",required=true,nillable=false)
  protected java.lang.String idSessione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessionePortale",required=false,nillable=false)
  protected java.lang.String idSessionePortale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessionePsp",required=false,nillable=false)
  protected java.lang.String idSessionePsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codiceStato",required=true,nillable=false)
  protected java.lang.String codiceStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspRedirectURL",required=false,nillable=false)
  protected java.lang.String pspRedirectURL;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspEsito",required=false,nillable=false)
  protected java.lang.String pspEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="jsonRequest",required=false,nillable=false)
  protected java.lang.String jsonRequest;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="wispIdDominio",required=false,nillable=false)
  protected java.lang.String wispIdDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="wispKeyPA",required=false,nillable=false)
  protected java.lang.String wispKeyPA;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="wispKeyWisp",required=false,nillable=false)
  protected java.lang.String wispKeyWisp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="wispHtml",required=false,nillable=false)
  protected java.lang.String wispHtml;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRichiesta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="urlRitorno",required=true,nillable=false)
  protected java.lang.String urlRitorno;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=false,nillable=false)
  protected java.lang.String codPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoVersamento",required=false,nillable=false)
  protected java.lang.String tipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="multiBeneficiario",required=false,nillable=false)
  protected java.lang.String multiBeneficiario;

}
