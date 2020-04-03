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


/** <p>Java class for VistaRptVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaRptVersamento">
 * 		&lt;sequence>
 * 			&lt;element name="idPagamentoPortale" type="{http://www.govpay.it/orm}id-pagamento-portale" minOccurs="0" maxOccurs="1"/>
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
 * 			&lt;element name="modelloPagamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codMsgRicevuta" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataMsgRicevuta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codEsitoPagamento" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="xmlRT" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codCanale" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codIntermediarioPsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoIdentificativoAttestante" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="identificativoAttestante" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="denominazioneAttestante" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codStazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneRPT" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTransazioneRT" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="statoConservazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStatoCons" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataConservazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="bloccante" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="true"/>
 * 			&lt;element name="vrsId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsNome" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIdTipoVersamentoDominio" type="{http://www.govpay.it/orm}id-tipo-versamento-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdTipoVersamento" type="{http://www.govpay.it/orm}id-tipo-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIdUo" type="{http://www.govpay.it/orm}id-uo" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIdApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsImportoTotale" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsStatoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDescrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsAggiornabile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDataValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDataOraUltimoAgg" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsCausaleVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreAnagrafica" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreIndirizzo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreCivico" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreCap" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreLocalita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreProvincia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreNazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreEmail" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreTelefono" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreCellulare" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDebitoreFax" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsTassonomiaAvviso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsTassonomia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodVersamentoLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodAnnoTributario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsCodBundlekey" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDatiAllegati" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIncasso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsAnomalie" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIuvVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsNumeroAvviso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsAck" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsAnomalo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsDivisione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDirezione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIdSessione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsDataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsImportoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsImportoIncassato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsStatoPagamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsIuvPagamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsSrcDebitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "VistaRptVersamento", 
  propOrder = {
  	"idPagamentoPortale",
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
  	"_decimalWrapper_codEsitoPagamento",
  	"importoTotalePagato",
  	"xmlRT",
  	"codCanale",
  	"codPsp",
  	"codIntermediarioPsp",
  	"tipoVersamento",
  	"tipoIdentificativoAttestante",
  	"identificativoAttestante",
  	"denominazioneAttestante",
  	"codStazione",
  	"codTransazioneRPT",
  	"codTransazioneRT",
  	"statoConservazione",
  	"descrizioneStatoCons",
  	"dataConservazione",
  	"bloccante",
  	"vrsId",
  	"vrsCodVersamentoEnte",
  	"vrsNome",
  	"vrsIdTipoVersamentoDominio",
  	"vrsIdTipoVersamento",
  	"vrsIdDominio",
  	"vrsIdUo",
  	"vrsIdApplicazione",
  	"vrsImportoTotale",
  	"vrsStatoVersamento",
  	"vrsDescrizioneStato",
  	"vrsAggiornabile",
  	"vrsDataCreazione",
  	"vrsDataValidita",
  	"vrsDataScadenza",
  	"vrsDataOraUltimoAgg",
  	"vrsCausaleVersamento",
  	"vrsDebitoreTipo",
  	"vrsDebitoreIdentificativo",
  	"vrsDebitoreAnagrafica",
  	"vrsDebitoreIndirizzo",
  	"vrsDebitoreCivico",
  	"vrsDebitoreCap",
  	"vrsDebitoreLocalita",
  	"vrsDebitoreProvincia",
  	"vrsDebitoreNazione",
  	"vrsDebitoreEmail",
  	"vrsDebitoreTelefono",
  	"vrsDebitoreCellulare",
  	"vrsDebitoreFax",
  	"vrsTassonomiaAvviso",
  	"vrsTassonomia",
  	"vrsCodLotto",
  	"vrsCodVersamentoLotto",
  	"vrsCodAnnoTributario",
  	"vrsCodBundlekey",
  	"vrsDatiAllegati",
  	"vrsIncasso",
  	"vrsAnomalie",
  	"vrsIuvVersamento",
  	"vrsNumeroAvviso",
  	"vrsAck",
  	"vrsAnomalo",
  	"vrsDivisione",
  	"vrsDirezione",
  	"vrsIdSessione",
  	"vrsDataPagamento",
  	"vrsImportoPagato",
  	"vrsImportoIncassato",
  	"vrsStatoPagamento",
  	"vrsIuvPagamento"
  }
)

@XmlRootElement(name = "VistaRptVersamento")

public class VistaRptVersamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public VistaRptVersamento() {
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

  public IdPagamentoPortale getIdPagamentoPortale() {
    return this.idPagamentoPortale;
  }

  public void setIdPagamentoPortale(IdPagamentoPortale idPagamentoPortale) {
    this.idPagamentoPortale = idPagamentoPortale;
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

  public java.lang.String getCodCanale() {
    return this.codCanale;
  }

  public void setCodCanale(java.lang.String codCanale) {
    this.codCanale = codCanale;
  }

  public java.lang.String getCodPsp() {
    return this.codPsp;
  }

  public void setCodPsp(java.lang.String codPsp) {
    this.codPsp = codPsp;
  }

  public java.lang.String getCodIntermediarioPsp() {
    return this.codIntermediarioPsp;
  }

  public void setCodIntermediarioPsp(java.lang.String codIntermediarioPsp) {
    this.codIntermediarioPsp = codIntermediarioPsp;
  }

  public java.lang.String getTipoVersamento() {
    return this.tipoVersamento;
  }

  public void setTipoVersamento(java.lang.String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  public java.lang.String getTipoIdentificativoAttestante() {
    return this.tipoIdentificativoAttestante;
  }

  public void setTipoIdentificativoAttestante(java.lang.String tipoIdentificativoAttestante) {
    this.tipoIdentificativoAttestante = tipoIdentificativoAttestante;
  }

  public java.lang.String getIdentificativoAttestante() {
    return this.identificativoAttestante;
  }

  public void setIdentificativoAttestante(java.lang.String identificativoAttestante) {
    this.identificativoAttestante = identificativoAttestante;
  }

  public java.lang.String getDenominazioneAttestante() {
    return this.denominazioneAttestante;
  }

  public void setDenominazioneAttestante(java.lang.String denominazioneAttestante) {
    this.denominazioneAttestante = denominazioneAttestante;
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

  public java.lang.String getStatoConservazione() {
    return this.statoConservazione;
  }

  public void setStatoConservazione(java.lang.String statoConservazione) {
    this.statoConservazione = statoConservazione;
  }

  public java.lang.String getDescrizioneStatoCons() {
    return this.descrizioneStatoCons;
  }

  public void setDescrizioneStatoCons(java.lang.String descrizioneStatoCons) {
    this.descrizioneStatoCons = descrizioneStatoCons;
  }

  public java.util.Date getDataConservazione() {
    return this.dataConservazione;
  }

  public void setDataConservazione(java.util.Date dataConservazione) {
    this.dataConservazione = dataConservazione;
  }

  public boolean isBloccante() {
    return this.bloccante;
  }

  public boolean getBloccante() {
    return this.bloccante;
  }

  public void setBloccante(boolean bloccante) {
    this.bloccante = bloccante;
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

  public java.lang.String getVrsNome() {
    return this.vrsNome;
  }

  public void setVrsNome(java.lang.String vrsNome) {
    this.vrsNome = vrsNome;
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

  public double getVrsImportoTotale() {
    return this.vrsImportoTotale;
  }

  public void setVrsImportoTotale(double vrsImportoTotale) {
    this.vrsImportoTotale = vrsImportoTotale;
  }

  public java.lang.String getVrsStatoVersamento() {
    return this.vrsStatoVersamento;
  }

  public void setVrsStatoVersamento(java.lang.String vrsStatoVersamento) {
    this.vrsStatoVersamento = vrsStatoVersamento;
  }

  public java.lang.String getVrsDescrizioneStato() {
    return this.vrsDescrizioneStato;
  }

  public void setVrsDescrizioneStato(java.lang.String vrsDescrizioneStato) {
    this.vrsDescrizioneStato = vrsDescrizioneStato;
  }

  public boolean isVrsAggiornabile() {
    return this.vrsAggiornabile;
  }

  public boolean getVrsAggiornabile() {
    return this.vrsAggiornabile;
  }

  public void setVrsAggiornabile(boolean vrsAggiornabile) {
    this.vrsAggiornabile = vrsAggiornabile;
  }

  public java.util.Date getVrsDataCreazione() {
    return this.vrsDataCreazione;
  }

  public void setVrsDataCreazione(java.util.Date vrsDataCreazione) {
    this.vrsDataCreazione = vrsDataCreazione;
  }

  public java.util.Date getVrsDataValidita() {
    return this.vrsDataValidita;
  }

  public void setVrsDataValidita(java.util.Date vrsDataValidita) {
    this.vrsDataValidita = vrsDataValidita;
  }

  public java.util.Date getVrsDataScadenza() {
    return this.vrsDataScadenza;
  }

  public void setVrsDataScadenza(java.util.Date vrsDataScadenza) {
    this.vrsDataScadenza = vrsDataScadenza;
  }

  public java.util.Date getVrsDataOraUltimoAgg() {
    return this.vrsDataOraUltimoAgg;
  }

  public void setVrsDataOraUltimoAgg(java.util.Date vrsDataOraUltimoAgg) {
    this.vrsDataOraUltimoAgg = vrsDataOraUltimoAgg;
  }

  public java.lang.String getVrsCausaleVersamento() {
    return this.vrsCausaleVersamento;
  }

  public void setVrsCausaleVersamento(java.lang.String vrsCausaleVersamento) {
    this.vrsCausaleVersamento = vrsCausaleVersamento;
  }

  public java.lang.String getVrsDebitoreTipo() {
    return this.vrsDebitoreTipo;
  }

  public void setVrsDebitoreTipo(java.lang.String vrsDebitoreTipo) {
    this.vrsDebitoreTipo = vrsDebitoreTipo;
  }

  public java.lang.String getVrsDebitoreIdentificativo() {
    return this.vrsDebitoreIdentificativo;
  }

  public void setVrsDebitoreIdentificativo(java.lang.String vrsDebitoreIdentificativo) {
    this.vrsDebitoreIdentificativo = vrsDebitoreIdentificativo;
  }

  public java.lang.String getVrsDebitoreAnagrafica() {
    return this.vrsDebitoreAnagrafica;
  }

  public void setVrsDebitoreAnagrafica(java.lang.String vrsDebitoreAnagrafica) {
    this.vrsDebitoreAnagrafica = vrsDebitoreAnagrafica;
  }

  public java.lang.String getVrsDebitoreIndirizzo() {
    return this.vrsDebitoreIndirizzo;
  }

  public void setVrsDebitoreIndirizzo(java.lang.String vrsDebitoreIndirizzo) {
    this.vrsDebitoreIndirizzo = vrsDebitoreIndirizzo;
  }

  public java.lang.String getVrsDebitoreCivico() {
    return this.vrsDebitoreCivico;
  }

  public void setVrsDebitoreCivico(java.lang.String vrsDebitoreCivico) {
    this.vrsDebitoreCivico = vrsDebitoreCivico;
  }

  public java.lang.String getVrsDebitoreCap() {
    return this.vrsDebitoreCap;
  }

  public void setVrsDebitoreCap(java.lang.String vrsDebitoreCap) {
    this.vrsDebitoreCap = vrsDebitoreCap;
  }

  public java.lang.String getVrsDebitoreLocalita() {
    return this.vrsDebitoreLocalita;
  }

  public void setVrsDebitoreLocalita(java.lang.String vrsDebitoreLocalita) {
    this.vrsDebitoreLocalita = vrsDebitoreLocalita;
  }

  public java.lang.String getVrsDebitoreProvincia() {
    return this.vrsDebitoreProvincia;
  }

  public void setVrsDebitoreProvincia(java.lang.String vrsDebitoreProvincia) {
    this.vrsDebitoreProvincia = vrsDebitoreProvincia;
  }

  public java.lang.String getVrsDebitoreNazione() {
    return this.vrsDebitoreNazione;
  }

  public void setVrsDebitoreNazione(java.lang.String vrsDebitoreNazione) {
    this.vrsDebitoreNazione = vrsDebitoreNazione;
  }

  public java.lang.String getVrsDebitoreEmail() {
    return this.vrsDebitoreEmail;
  }

  public void setVrsDebitoreEmail(java.lang.String vrsDebitoreEmail) {
    this.vrsDebitoreEmail = vrsDebitoreEmail;
  }

  public java.lang.String getVrsDebitoreTelefono() {
    return this.vrsDebitoreTelefono;
  }

  public void setVrsDebitoreTelefono(java.lang.String vrsDebitoreTelefono) {
    this.vrsDebitoreTelefono = vrsDebitoreTelefono;
  }

  public java.lang.String getVrsDebitoreCellulare() {
    return this.vrsDebitoreCellulare;
  }

  public void setVrsDebitoreCellulare(java.lang.String vrsDebitoreCellulare) {
    this.vrsDebitoreCellulare = vrsDebitoreCellulare;
  }

  public java.lang.String getVrsDebitoreFax() {
    return this.vrsDebitoreFax;
  }

  public void setVrsDebitoreFax(java.lang.String vrsDebitoreFax) {
    this.vrsDebitoreFax = vrsDebitoreFax;
  }

  public java.lang.String getVrsTassonomiaAvviso() {
    return this.vrsTassonomiaAvviso;
  }

  public void setVrsTassonomiaAvviso(java.lang.String vrsTassonomiaAvviso) {
    this.vrsTassonomiaAvviso = vrsTassonomiaAvviso;
  }

  public java.lang.String getVrsTassonomia() {
    return this.vrsTassonomia;
  }

  public void setVrsTassonomia(java.lang.String vrsTassonomia) {
    this.vrsTassonomia = vrsTassonomia;
  }

  public java.lang.String getVrsCodLotto() {
    return this.vrsCodLotto;
  }

  public void setVrsCodLotto(java.lang.String vrsCodLotto) {
    this.vrsCodLotto = vrsCodLotto;
  }

  public java.lang.String getVrsCodVersamentoLotto() {
    return this.vrsCodVersamentoLotto;
  }

  public void setVrsCodVersamentoLotto(java.lang.String vrsCodVersamentoLotto) {
    this.vrsCodVersamentoLotto = vrsCodVersamentoLotto;
  }

  public java.lang.String getVrsCodAnnoTributario() {
    return this.vrsCodAnnoTributario;
  }

  public void setVrsCodAnnoTributario(java.lang.String vrsCodAnnoTributario) {
    this.vrsCodAnnoTributario = vrsCodAnnoTributario;
  }

  public java.lang.String getVrsCodBundlekey() {
    return this.vrsCodBundlekey;
  }

  public void setVrsCodBundlekey(java.lang.String vrsCodBundlekey) {
    this.vrsCodBundlekey = vrsCodBundlekey;
  }

  public java.lang.String getVrsDatiAllegati() {
    return this.vrsDatiAllegati;
  }

  public void setVrsDatiAllegati(java.lang.String vrsDatiAllegati) {
    this.vrsDatiAllegati = vrsDatiAllegati;
  }

  public java.lang.String getVrsIncasso() {
    return this.vrsIncasso;
  }

  public void setVrsIncasso(java.lang.String vrsIncasso) {
    this.vrsIncasso = vrsIncasso;
  }

  public java.lang.String getVrsAnomalie() {
    return this.vrsAnomalie;
  }

  public void setVrsAnomalie(java.lang.String vrsAnomalie) {
    this.vrsAnomalie = vrsAnomalie;
  }

  public java.lang.String getVrsIuvVersamento() {
    return this.vrsIuvVersamento;
  }

  public void setVrsIuvVersamento(java.lang.String vrsIuvVersamento) {
    this.vrsIuvVersamento = vrsIuvVersamento;
  }

  public java.lang.String getVrsNumeroAvviso() {
    return this.vrsNumeroAvviso;
  }

  public void setVrsNumeroAvviso(java.lang.String vrsNumeroAvviso) {
    this.vrsNumeroAvviso = vrsNumeroAvviso;
  }

  public boolean isVrsAck() {
    return this.vrsAck;
  }

  public boolean getVrsAck() {
    return this.vrsAck;
  }

  public void setVrsAck(boolean vrsAck) {
    this.vrsAck = vrsAck;
  }

  public boolean isVrsAnomalo() {
    return this.vrsAnomalo;
  }

  public boolean getVrsAnomalo() {
    return this.vrsAnomalo;
  }

  public void setVrsAnomalo(boolean vrsAnomalo) {
    this.vrsAnomalo = vrsAnomalo;
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

  public java.lang.String getVrsIdSessione() {
    return this.vrsIdSessione;
  }

  public void setVrsIdSessione(java.lang.String vrsIdSessione) {
    this.vrsIdSessione = vrsIdSessione;
  }

  public java.util.Date getVrsDataPagamento() {
    return this.vrsDataPagamento;
  }

  public void setVrsDataPagamento(java.util.Date vrsDataPagamento) {
    this.vrsDataPagamento = vrsDataPagamento;
  }

  public java.lang.Double getVrsImportoPagato() {
    return this.vrsImportoPagato;
  }

  public void setVrsImportoPagato(java.lang.Double vrsImportoPagato) {
    this.vrsImportoPagato = vrsImportoPagato;
  }

  public java.lang.Double getVrsImportoIncassato() {
    return this.vrsImportoIncassato;
  }

  public void setVrsImportoIncassato(java.lang.Double vrsImportoIncassato) {
    this.vrsImportoIncassato = vrsImportoIncassato;
  }

  public java.lang.String getVrsStatoPagamento() {
    return this.vrsStatoPagamento;
  }

  public void setVrsStatoPagamento(java.lang.String vrsStatoPagamento) {
    this.vrsStatoPagamento = vrsStatoPagamento;
  }

  public java.lang.String getVrsIuvPagamento() {
    return this.vrsIuvPagamento;
  }

  public void setVrsIuvPagamento(java.lang.String vrsIuvPagamento) {
    this.vrsIuvPagamento = vrsIuvPagamento;
  }

  public java.lang.String getVrsSrcDebitoreIdentificativo() {
    return this.vrsSrcDebitoreIdentificativo;
  }

  public void setVrsSrcDebitoreIdentificativo(java.lang.String vrsSrcDebitoreIdentificativo) {
    this.vrsSrcDebitoreIdentificativo = vrsSrcDebitoreIdentificativo;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.VistaRptVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaRptVersamento.modelStaticInstance==null){
  			it.govpay.orm.VistaRptVersamento.modelStaticInstance = new it.govpay.orm.model.VistaRptVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.VistaRptVersamentoModel model(){
	  if(it.govpay.orm.VistaRptVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaRptVersamento.modelStaticInstance;
  }


  @XmlElement(name="idPagamentoPortale",required=false,nillable=false)
  protected IdPagamentoPortale idPagamentoPortale;

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
  @XmlElement(name="modelloPagamento",required=false,nillable=false)
  protected java.lang.String modelloPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRicevuta",required=false,nillable=false)
  protected java.lang.String codMsgRicevuta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataMsgRicevuta",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataMsgRicevuta;

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
  @XmlElement(name="codCanale",required=false,nillable=false)
  protected java.lang.String codCanale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=false,nillable=false)
  protected java.lang.String codPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codIntermediarioPsp",required=false,nillable=false)
  protected java.lang.String codIntermediarioPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoVersamento",required=false,nillable=false)
  protected java.lang.String tipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoIdentificativoAttestante",required=false,nillable=false)
  protected java.lang.String tipoIdentificativoAttestante;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="identificativoAttestante",required=false,nillable=false)
  protected java.lang.String identificativoAttestante;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="denominazioneAttestante",required=false,nillable=false)
  protected java.lang.String denominazioneAttestante;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codStazione",required=true,nillable=false)
  protected java.lang.String codStazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneRPT",required=false,nillable=false)
  protected java.lang.String codTransazioneRPT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTransazioneRT",required=false,nillable=false)
  protected java.lang.String codTransazioneRT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoConservazione",required=false,nillable=false)
  protected java.lang.String statoConservazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStatoCons",required=false,nillable=false)
  protected java.lang.String descrizioneStatoCons;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataConservazione",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataConservazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="bloccante",required=true,nillable=false,defaultValue="true")
  protected boolean bloccante = true;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="vrsId",required=false,nillable=false)
  protected long vrsId;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodVersamentoEnte",required=true,nillable=false)
  protected java.lang.String vrsCodVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsNome",required=false,nillable=false)
  protected java.lang.String vrsNome;

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

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="vrsImportoTotale",required=true,nillable=false)
  protected double vrsImportoTotale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsStatoVersamento",required=true,nillable=false)
  protected java.lang.String vrsStatoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDescrizioneStato",required=false,nillable=false)
  protected java.lang.String vrsDescrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="vrsAggiornabile",required=true,nillable=false)
  protected boolean vrsAggiornabile;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="vrsDataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date vrsDataCreazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="vrsDataValidita",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date vrsDataValidita;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="vrsDataScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date vrsDataScadenza;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="vrsDataOraUltimoAgg",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date vrsDataOraUltimoAgg;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCausaleVersamento",required=false,nillable=false)
  protected java.lang.String vrsCausaleVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreTipo",required=false,nillable=false)
  protected java.lang.String vrsDebitoreTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreIdentificativo",required=true,nillable=false)
  protected java.lang.String vrsDebitoreIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreAnagrafica",required=true,nillable=false)
  protected java.lang.String vrsDebitoreAnagrafica;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreIndirizzo",required=false,nillable=false)
  protected java.lang.String vrsDebitoreIndirizzo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreCivico",required=false,nillable=false)
  protected java.lang.String vrsDebitoreCivico;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreCap",required=false,nillable=false)
  protected java.lang.String vrsDebitoreCap;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreLocalita",required=false,nillable=false)
  protected java.lang.String vrsDebitoreLocalita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreProvincia",required=false,nillable=false)
  protected java.lang.String vrsDebitoreProvincia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreNazione",required=false,nillable=false)
  protected java.lang.String vrsDebitoreNazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreEmail",required=false,nillable=false)
  protected java.lang.String vrsDebitoreEmail;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreTelefono",required=false,nillable=false)
  protected java.lang.String vrsDebitoreTelefono;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreCellulare",required=false,nillable=false)
  protected java.lang.String vrsDebitoreCellulare;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDebitoreFax",required=false,nillable=false)
  protected java.lang.String vrsDebitoreFax;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsTassonomiaAvviso",required=false,nillable=false)
  protected java.lang.String vrsTassonomiaAvviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsTassonomia",required=false,nillable=false)
  protected java.lang.String vrsTassonomia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodLotto",required=false,nillable=false)
  protected java.lang.String vrsCodLotto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodVersamentoLotto",required=false,nillable=false)
  protected java.lang.String vrsCodVersamentoLotto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodAnnoTributario",required=false,nillable=false)
  protected java.lang.String vrsCodAnnoTributario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsCodBundlekey",required=false,nillable=false)
  protected java.lang.String vrsCodBundlekey;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDatiAllegati",required=false,nillable=false)
  protected java.lang.String vrsDatiAllegati;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsIncasso",required=false,nillable=false)
  protected java.lang.String vrsIncasso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsAnomalie",required=false,nillable=false)
  protected java.lang.String vrsAnomalie;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsIuvVersamento",required=false,nillable=false)
  protected java.lang.String vrsIuvVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsNumeroAvviso",required=false,nillable=false)
  protected java.lang.String vrsNumeroAvviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="vrsAck",required=true,nillable=false)
  protected boolean vrsAck;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="vrsAnomalo",required=true,nillable=false)
  protected boolean vrsAnomalo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDivisione",required=false,nillable=false)
  protected java.lang.String vrsDivisione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsDirezione",required=false,nillable=false)
  protected java.lang.String vrsDirezione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsIdSessione",required=false,nillable=false)
  protected java.lang.String vrsIdSessione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="vrsDataPagamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date vrsDataPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="vrsImportoPagato",required=true,nillable=false)
  protected java.lang.Double vrsImportoPagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="vrsImportoIncassato",required=true,nillable=false)
  protected java.lang.Double vrsImportoIncassato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsStatoPagamento",required=true,nillable=false)
  protected java.lang.String vrsStatoPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsIuvPagamento",required=false,nillable=false)
  protected java.lang.String vrsIuvPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsSrcDebitoreIdentificativo",required=true,nillable=false)
  protected java.lang.String vrsSrcDebitoreIdentificativo;

}
