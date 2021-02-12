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


/** <p>Java class for VistaPagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaPagamento">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataAcquisizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="commissioniPsp" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoAllegato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="allegato" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataAcquisizioneRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="causaleRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoRevocato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiEsitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdTipoVersamentoDominio" type="{http://www.govpay.it/orm}id-tipo-versamento-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdTipoVersamento" type="{http://www.govpay.it/orm}id-tipo-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdUo" type="{http://www.govpay.it/orm}id-uo" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIdApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsTassonomia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDivisione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDirezione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngCodSingVersEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rptIuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rptCcp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rncTrn" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "VistaPagamento", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"indiceDati",
  	"importoPagato",
  	"dataAcquisizione",
  	"iur",
  	"dataPagamento",
  	"commissioniPsp",
  	"tipoAllegato",
  	"allegato",
  	"dataAcquisizioneRevoca",
  	"causaleRevoca",
  	"datiRevoca",
  	"importoRevocato",
  	"esitoRevoca",
  	"datiEsitoRevoca",
  	"stato",
  	"tipo",
  	"vrsId",
  	"vrsCodVersamentoEnte",
  	"vrsIdTipoVersamentoDominio",
  	"vrsIdTipoVersamento",
  	"vrsIdDominio",
  	"vrsIdUo",
  	"vrsIdApplicazione",
  	"vrsTassonomia",
  	"vrsDivisione",
  	"vrsDirezione",
  	"sngCodSingVersEnte",
  	"rptIuv",
  	"rptCcp",
  	"rncTrn"
  }
)

@XmlRootElement(name = "VistaPagamento")

public class VistaPagamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public VistaPagamento() {
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

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public int getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(int indiceDati) {
    this.indiceDati = indiceDati;
  }

  public double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(double importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.util.Date getDataAcquisizione() {
    return this.dataAcquisizione;
  }

  public void setDataAcquisizione(java.util.Date dataAcquisizione) {
    this.dataAcquisizione = dataAcquisizione;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.util.Date getDataPagamento() {
    return this.dataPagamento;
  }

  public void setDataPagamento(java.util.Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public java.lang.Double getCommissioniPsp() {
    return this.commissioniPsp;
  }

  public void setCommissioniPsp(java.lang.Double commissioniPsp) {
    this.commissioniPsp = commissioniPsp;
  }

  public java.lang.String getTipoAllegato() {
    return this.tipoAllegato;
  }

  public void setTipoAllegato(java.lang.String tipoAllegato) {
    this.tipoAllegato = tipoAllegato;
  }

  public byte[] getAllegato() {
    return this.allegato;
  }

  public void setAllegato(byte[] allegato) {
    this.allegato = allegato;
  }

  public java.util.Date getDataAcquisizioneRevoca() {
    return this.dataAcquisizioneRevoca;
  }

  public void setDataAcquisizioneRevoca(java.util.Date dataAcquisizioneRevoca) {
    this.dataAcquisizioneRevoca = dataAcquisizioneRevoca;
  }

  public java.lang.String getCausaleRevoca() {
    return this.causaleRevoca;
  }

  public void setCausaleRevoca(java.lang.String causaleRevoca) {
    this.causaleRevoca = causaleRevoca;
  }

  public java.lang.String getDatiRevoca() {
    return this.datiRevoca;
  }

  public void setDatiRevoca(java.lang.String datiRevoca) {
    this.datiRevoca = datiRevoca;
  }

  public java.lang.Double getImportoRevocato() {
    return this.importoRevocato;
  }

  public void setImportoRevocato(java.lang.Double importoRevocato) {
    this.importoRevocato = importoRevocato;
  }

  public java.lang.String getEsitoRevoca() {
    return this.esitoRevoca;
  }

  public void setEsitoRevoca(java.lang.String esitoRevoca) {
    this.esitoRevoca = esitoRevoca;
  }

  public java.lang.String getDatiEsitoRevoca() {
    return this.datiEsitoRevoca;
  }

  public void setDatiEsitoRevoca(java.lang.String datiEsitoRevoca) {
    this.datiEsitoRevoca = datiEsitoRevoca;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public long getVrsId() {
    return this.vrsId;
  }

  public void setVrsId(long vrsId) {
    this.vrsId = vrsId;
  }

  public java.lang.String getVrsCodVersamentoEnte() {
    return this.vrsCodVersamentoEnte;
  }

  public void setVrsCodVersamentoEnte(java.lang.String vrsCodVersamentoEnte) {
    this.vrsCodVersamentoEnte = vrsCodVersamentoEnte;
  }

  public IdTipoVersamentoDominio getVrsIdTipoVersamentoDominio() {
    return this.vrsIdTipoVersamentoDominio;
  }

  public void setVrsIdTipoVersamentoDominio(IdTipoVersamentoDominio vrsIdTipoVersamentoDominio) {
    this.vrsIdTipoVersamentoDominio = vrsIdTipoVersamentoDominio;
  }

  public IdTipoVersamento getVrsIdTipoVersamento() {
    return this.vrsIdTipoVersamento;
  }

  public void setVrsIdTipoVersamento(IdTipoVersamento vrsIdTipoVersamento) {
    this.vrsIdTipoVersamento = vrsIdTipoVersamento;
  }

  public IdDominio getVrsIdDominio() {
    return this.vrsIdDominio;
  }

  public void setVrsIdDominio(IdDominio vrsIdDominio) {
    this.vrsIdDominio = vrsIdDominio;
  }

  public IdUo getVrsIdUo() {
    return this.vrsIdUo;
  }

  public void setVrsIdUo(IdUo vrsIdUo) {
    this.vrsIdUo = vrsIdUo;
  }

  public IdApplicazione getVrsIdApplicazione() {
    return this.vrsIdApplicazione;
  }

  public void setVrsIdApplicazione(IdApplicazione vrsIdApplicazione) {
    this.vrsIdApplicazione = vrsIdApplicazione;
  }

  public java.lang.String getVrsTassonomia() {
    return this.vrsTassonomia;
  }

  public void setVrsTassonomia(java.lang.String vrsTassonomia) {
    this.vrsTassonomia = vrsTassonomia;
  }

  public java.lang.String getVrsDivisione() {
    return this.vrsDivisione;
  }

  public void setVrsDivisione(java.lang.String vrsDivisione) {
    this.vrsDivisione = vrsDivisione;
  }

  public java.lang.String getVrsDirezione() {
    return this.vrsDirezione;
  }

  public void setVrsDirezione(java.lang.String vrsDirezione) {
    this.vrsDirezione = vrsDirezione;
  }

  public java.lang.String getSngCodSingVersEnte() {
    return this.sngCodSingVersEnte;
  }

  public void setSngCodSingVersEnte(java.lang.String sngCodSingVersEnte) {
    this.sngCodSingVersEnte = sngCodSingVersEnte;
  }

  public java.lang.String getRptIuv() {
    return this.rptIuv;
  }

  public void setRptIuv(java.lang.String rptIuv) {
    this.rptIuv = rptIuv;
  }

  public java.lang.String getRptCcp() {
    return this.rptCcp;
  }

  public void setRptCcp(java.lang.String rptCcp) {
    this.rptCcp = rptCcp;
  }

  public java.lang.String getRncTrn() {
    return this.rncTrn;
  }

  public void setRncTrn(java.lang.String rncTrn) {
    this.rncTrn = rncTrn;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.VistaPagamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaPagamento.modelStaticInstance==null){
  			it.govpay.orm.VistaPagamento.modelStaticInstance = new it.govpay.orm.model.VistaPagamentoModel();
	  }
  }
  public static it.govpay.orm.model.VistaPagamentoModel model(){
	  if(it.govpay.orm.VistaPagamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaPagamento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected int indiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoPagato",required=true,nillable=false)
  protected double importoPagato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAcquisizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAcquisizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataPagamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="commissioniPsp",required=false,nillable=false)
  protected java.lang.Double commissioniPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoAllegato",required=false,nillable=false)
  protected java.lang.String tipoAllegato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="allegato",required=false,nillable=false)
  protected byte[] allegato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAcquisizioneRevoca",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAcquisizioneRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleRevoca",required=false,nillable=false)
  protected java.lang.String causaleRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiRevoca",required=false,nillable=false)
  protected java.lang.String datiRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoRevocato",required=false,nillable=false)
  protected java.lang.Double importoRevocato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esitoRevoca",required=false,nillable=false)
  protected java.lang.String esitoRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiEsitoRevoca",required=false,nillable=false)
  protected java.lang.String datiEsitoRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=false,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="vrsId",required=false,nillable=false)
  protected long vrsId;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodVersamentoEnte",required=true,nillable=false)
  protected java.lang.String vrsCodVersamentoEnte;

  @XmlElement(name="vrsIdTipoVersamentoDominio",required=true,nillable=false)
  protected IdTipoVersamentoDominio vrsIdTipoVersamentoDominio;

  @XmlElement(name="vrsIdTipoVersamento",required=true,nillable=false)
  protected IdTipoVersamento vrsIdTipoVersamento;

  @XmlElement(name="vrsIdDominio",required=true,nillable=false)
  protected IdDominio vrsIdDominio;

  @XmlElement(name="vrsIdUo",required=false,nillable=false)
  protected IdUo vrsIdUo;

  @XmlElement(name="vrsIdApplicazione",required=true,nillable=false)
  protected IdApplicazione vrsIdApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsTassonomia",required=false,nillable=false)
  protected java.lang.String vrsTassonomia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDivisione",required=false,nillable=false)
  protected java.lang.String vrsDivisione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDirezione",required=false,nillable=false)
  protected java.lang.String vrsDirezione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngCodSingVersEnte",required=true,nillable=false)
  protected java.lang.String sngCodSingVersEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rptIuv",required=true,nillable=false)
  protected java.lang.String rptIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rptCcp",required=true,nillable=false)
  protected java.lang.String rptCcp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rncTrn",required=true,nillable=false)
  protected java.lang.String rncTrn;

}
