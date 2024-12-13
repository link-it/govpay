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


/** <p>Java class for PagamentoPortale complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PagamentoPortale"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="nome" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="importo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="versanteIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idSessione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idSessionePortale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idSessionePsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codiceStato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="pspRedirectURL" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="pspEsito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="jsonRequest" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataRichiesta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="urlRitorno" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="multiBeneficiario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="ack" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="principal" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo_utenza" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="srcVersanteIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="severita" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "PagamentoPortale",
  propOrder = {
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
  	"dataRichiesta",
  	"urlRitorno",
  	"codPsp",
  	"tipoVersamento",
  	"multiBeneficiario",
  	"ack",
  	"tipo",
  	"principal",
  	"tipoUtenza",
  	"idApplicazione",
  	"srcVersanteIdentificativo",
  	"severita"
  }
)

@XmlRootElement(name = "PagamentoPortale")

public class PagamentoPortale extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public PagamentoPortale() {
    super();
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

  public boolean isAck() {
    return this.ack;
  }

  public boolean getAck() {
    return this.ack;
  }

  public void setAck(boolean ack) {
    this.ack = ack;
  }

  public int getTipo() {
    return this.tipo;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }

  public java.lang.String getPrincipal() {
    return this.principal;
  }

  public void setPrincipal(java.lang.String principal) {
    this.principal = principal;
  }

  public java.lang.String getTipoUtenza() {
    return this.tipoUtenza;
  }

  public void setTipoUtenza(java.lang.String tipoUtenza) {
    this.tipoUtenza = tipoUtenza;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getSrcVersanteIdentificativo() {
    return this.srcVersanteIdentificativo;
  }

  public void setSrcVersanteIdentificativo(java.lang.String srcVersanteIdentificativo) {
    this.srcVersanteIdentificativo = srcVersanteIdentificativo;
  }

  public java.math.BigInteger getSeverita() {
    return this.severita;
  }

  public void setSeverita(java.math.BigInteger severita) {
    this.severita = severita;
  }

  private static final long serialVersionUID = 1L;

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


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codCanale",required=false,nillable=false)
  protected java.lang.String codCanale;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome",required=true,nillable=false)
  protected java.lang.String nome;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importo",required=true,nillable=false)
  protected double importo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="versanteIdentificativo",required=false,nillable=false)
  protected java.lang.String versanteIdentificativo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessione",required=true,nillable=false)
  protected java.lang.String idSessione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessionePortale",required=false,nillable=false)
  protected java.lang.String idSessionePortale;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessionePsp",required=false,nillable=false)
  protected java.lang.String idSessionePsp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codiceStato",required=true,nillable=false)
  protected java.lang.String codiceStato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspRedirectURL",required=false,nillable=false)
  protected java.lang.String pspRedirectURL;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pspEsito",required=false,nillable=false)
  protected java.lang.String pspEsito;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="jsonRequest",required=false,nillable=false)
  protected java.lang.String jsonRequest;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRichiesta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRichiesta;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="urlRitorno",required=false,nillable=false)
  protected java.lang.String urlRitorno;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=false,nillable=false)
  protected java.lang.String codPsp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoVersamento",required=false,nillable=false)
  protected java.lang.String tipoVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="multiBeneficiario",required=false,nillable=false)
  protected java.lang.String multiBeneficiario;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="ack",required=true,nillable=false)
  protected boolean ack;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected int tipo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="principal",required=true,nillable=false)
  protected java.lang.String principal;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo_utenza",required=true,nillable=false)
  protected java.lang.String tipoUtenza;

  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="srcVersanteIdentificativo",required=false,nillable=false)
  protected java.lang.String srcVersanteIdentificativo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="severita",required=false,nillable=false)
  protected java.math.BigInteger severita;

}
