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


/** <p>Java class for VistaRendicontazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaRendicontazione">
 * 		&lt;sequence>
 * 			&lt;element name="frCodPsp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frCodDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frCodFlusso" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frStato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frDescrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frIur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frDataOraFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frDataRegolamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frDataAcquisizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="frNumeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frImportoTotalePagamenti" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frCodBicRiversamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frIdIncasso" type="{http://www.govpay.it/orm}id-incasso" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frRagioneSocialePsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frRagioneSocialeDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="frObsoleto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rndIuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rndIur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rndIndiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rndImportoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rndEsito" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rndData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rndStato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rndAnomalie" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rndIdPagamento" type="{http://www.govpay.it/orm}id-pagamento" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngIdTributo" type="{http://www.govpay.it/orm}id-tributo" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngCodSingVersEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sngStatoSingoloVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sngImportoSingoloVersamento" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sngDescrizione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngDatiAllegati" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngIndiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="sngDescrizioneCausaleRPT" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sngContabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
 * 			&lt;element name="vrsCodRata" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsIdDocumento" type="{http://www.govpay.it/orm}id-documento" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="vrsTipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="vrsProprieta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagCodDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagIuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagIndiceDati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagImportoPagato" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagDataAcquisizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagIur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagDataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagCommissioniPsp" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagTipoAllegato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagAllegato" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagDataAcquisizioneRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagCausaleRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagDatiRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagImportoRevocato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagEsitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagDatiEsitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagTipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "VistaRendicontazione", 
  propOrder = {
  	"frCodPsp",
  	"frCodDominio",
  	"frCodFlusso",
  	"frStato",
  	"frDescrizioneStato",
  	"frIur",
  	"frDataOraFlusso",
  	"frDataRegolamento",
  	"frDataAcquisizione",
  	"frNumeroPagamenti",
  	"frImportoTotalePagamenti",
  	"frCodBicRiversamento",
  	"frId",
  	"frIdIncasso",
  	"frRagioneSocialePsp",
  	"frRagioneSocialeDominio",
  	"frObsoleto",
  	"rndIuv",
  	"rndIur",
  	"rndIndiceDati",
  	"rndImportoPagato",
  	"rndEsito",
  	"rndData",
  	"rndStato",
  	"rndAnomalie",
  	"rndIdPagamento",
  	"sngIdTributo",
  	"sngCodSingVersEnte",
  	"sngStatoSingoloVersamento",
  	"sngImportoSingoloVersamento",
  	"sngDescrizione",
  	"sngDatiAllegati",
  	"sngIndiceDati",
  	"sngDescrizioneCausaleRPT",
  	"sngContabilita",
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
  	"vrsIuvPagamento",
  	"vrsCodRata",
  	"vrsIdDocumento",
  	"vrsTipo",
  	"vrsProprieta",
  	"pagCodDominio",
  	"pagIuv",
  	"pagIndiceDati",
  	"pagImportoPagato",
  	"pagDataAcquisizione",
  	"pagIur",
  	"pagDataPagamento",
  	"pagCommissioniPsp",
  	"pagTipoAllegato",
  	"pagAllegato",
  	"pagDataAcquisizioneRevoca",
  	"pagCausaleRevoca",
  	"pagDatiRevoca",
  	"pagImportoRevocato",
  	"pagEsitoRevoca",
  	"pagDatiEsitoRevoca",
  	"pagStato",
  	"pagTipo",
  	"rptIuv",
  	"rptCcp",
  	"rncTrn"
  }
)

@XmlRootElement(name = "VistaRendicontazione")

public class VistaRendicontazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public VistaRendicontazione() {
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

  public java.lang.String getFrCodPsp() {
    return this.frCodPsp;
  }

  public void setFrCodPsp(java.lang.String frCodPsp) {
    this.frCodPsp = frCodPsp;
  }

  public java.lang.String getFrCodDominio() {
    return this.frCodDominio;
  }

  public void setFrCodDominio(java.lang.String frCodDominio) {
    this.frCodDominio = frCodDominio;
  }

  public java.lang.String getFrCodFlusso() {
    return this.frCodFlusso;
  }

  public void setFrCodFlusso(java.lang.String frCodFlusso) {
    this.frCodFlusso = frCodFlusso;
  }

  public java.lang.String getFrStato() {
    return this.frStato;
  }

  public void setFrStato(java.lang.String frStato) {
    this.frStato = frStato;
  }

  public java.lang.String getFrDescrizioneStato() {
    return this.frDescrizioneStato;
  }

  public void setFrDescrizioneStato(java.lang.String frDescrizioneStato) {
    this.frDescrizioneStato = frDescrizioneStato;
  }

  public java.lang.String getFrIur() {
    return this.frIur;
  }

  public void setFrIur(java.lang.String frIur) {
    this.frIur = frIur;
  }

  public java.util.Date getFrDataOraFlusso() {
    return this.frDataOraFlusso;
  }

  public void setFrDataOraFlusso(java.util.Date frDataOraFlusso) {
    this.frDataOraFlusso = frDataOraFlusso;
  }

  public java.util.Date getFrDataRegolamento() {
    return this.frDataRegolamento;
  }

  public void setFrDataRegolamento(java.util.Date frDataRegolamento) {
    this.frDataRegolamento = frDataRegolamento;
  }

  public java.util.Date getFrDataAcquisizione() {
    return this.frDataAcquisizione;
  }

  public void setFrDataAcquisizione(java.util.Date frDataAcquisizione) {
    this.frDataAcquisizione = frDataAcquisizione;
  }

  public long getFrNumeroPagamenti() {
    return this.frNumeroPagamenti;
  }

  public void setFrNumeroPagamenti(long frNumeroPagamenti) {
    this.frNumeroPagamenti = frNumeroPagamenti;
  }

  public java.lang.Double getFrImportoTotalePagamenti() {
    return this.frImportoTotalePagamenti;
  }

  public void setFrImportoTotalePagamenti(java.lang.Double frImportoTotalePagamenti) {
    this.frImportoTotalePagamenti = frImportoTotalePagamenti;
  }

  public java.lang.String getFrCodBicRiversamento() {
    return this.frCodBicRiversamento;
  }

  public void setFrCodBicRiversamento(java.lang.String frCodBicRiversamento) {
    this.frCodBicRiversamento = frCodBicRiversamento;
  }

  public long getFrId() {
    return this.frId;
  }

  public void setFrId(long frId) {
    this.frId = frId;
  }

  public IdIncasso getFrIdIncasso() {
    return this.frIdIncasso;
  }

  public void setFrIdIncasso(IdIncasso frIdIncasso) {
    this.frIdIncasso = frIdIncasso;
  }

  public java.lang.String getFrRagioneSocialePsp() {
    return this.frRagioneSocialePsp;
  }

  public void setFrRagioneSocialePsp(java.lang.String frRagioneSocialePsp) {
    this.frRagioneSocialePsp = frRagioneSocialePsp;
  }

  public java.lang.String getFrRagioneSocialeDominio() {
    return this.frRagioneSocialeDominio;
  }

  public void setFrRagioneSocialeDominio(java.lang.String frRagioneSocialeDominio) {
    this.frRagioneSocialeDominio = frRagioneSocialeDominio;
  }

  public Boolean getFrObsoleto() {
    return this.frObsoleto;
  }

  public void setFrObsoleto(Boolean frObsoleto) {
    this.frObsoleto = frObsoleto;
  }

  public java.lang.String getRndIuv() {
    return this.rndIuv;
  }

  public void setRndIuv(java.lang.String rndIuv) {
    this.rndIuv = rndIuv;
  }

  public java.lang.String getRndIur() {
    return this.rndIur;
  }

  public void setRndIur(java.lang.String rndIur) {
    this.rndIur = rndIur;
  }

  public java.lang.Integer getRndIndiceDati() {
    return this.rndIndiceDati;
  }

  public void setRndIndiceDati(java.lang.Integer rndIndiceDati) {
    this.rndIndiceDati = rndIndiceDati;
  }

  public java.lang.Double getRndImportoPagato() {
    return this.rndImportoPagato;
  }

  public void setRndImportoPagato(java.lang.Double rndImportoPagato) {
    this.rndImportoPagato = rndImportoPagato;
  }

  public java.lang.Integer getRndEsito() {
    return this.rndEsito;
  }

  public void setRndEsito(java.lang.Integer rndEsito) {
    this.rndEsito = rndEsito;
  }

  public java.util.Date getRndData() {
    return this.rndData;
  }

  public void setRndData(java.util.Date rndData) {
    this.rndData = rndData;
  }

  public java.lang.String getRndStato() {
    return this.rndStato;
  }

  public void setRndStato(java.lang.String rndStato) {
    this.rndStato = rndStato;
  }

  public java.lang.String getRndAnomalie() {
    return this.rndAnomalie;
  }

  public void setRndAnomalie(java.lang.String rndAnomalie) {
    this.rndAnomalie = rndAnomalie;
  }

  public IdPagamento getRndIdPagamento() {
    return this.rndIdPagamento;
  }

  public void setRndIdPagamento(IdPagamento rndIdPagamento) {
    this.rndIdPagamento = rndIdPagamento;
  }

  public IdTributo getSngIdTributo() {
    return this.sngIdTributo;
  }

  public void setSngIdTributo(IdTributo sngIdTributo) {
    this.sngIdTributo = sngIdTributo;
  }

  public java.lang.String getSngCodSingVersEnte() {
    return this.sngCodSingVersEnte;
  }

  public void setSngCodSingVersEnte(java.lang.String sngCodSingVersEnte) {
    this.sngCodSingVersEnte = sngCodSingVersEnte;
  }

  public java.lang.String getSngStatoSingoloVersamento() {
    return this.sngStatoSingoloVersamento;
  }

  public void setSngStatoSingoloVersamento(java.lang.String sngStatoSingoloVersamento) {
    this.sngStatoSingoloVersamento = sngStatoSingoloVersamento;
  }

  public double getSngImportoSingoloVersamento() {
    return this.sngImportoSingoloVersamento;
  }

  public void setSngImportoSingoloVersamento(double sngImportoSingoloVersamento) {
    this.sngImportoSingoloVersamento = sngImportoSingoloVersamento;
  }

  public java.lang.String getSngDescrizione() {
    return this.sngDescrizione;
  }

  public void setSngDescrizione(java.lang.String sngDescrizione) {
    this.sngDescrizione = sngDescrizione;
  }

  public java.lang.String getSngDatiAllegati() {
    return this.sngDatiAllegati;
  }

  public void setSngDatiAllegati(java.lang.String sngDatiAllegati) {
    this.sngDatiAllegati = sngDatiAllegati;
  }

  public java.lang.Integer getSngIndiceDati() {
    return this.sngIndiceDati;
  }

  public void setSngIndiceDati(java.lang.Integer sngIndiceDati) {
    this.sngIndiceDati = sngIndiceDati;
  }

  public java.lang.String getSngDescrizioneCausaleRPT() {
    return this.sngDescrizioneCausaleRPT;
  }

  public void setSngDescrizioneCausaleRPT(java.lang.String sngDescrizioneCausaleRPT) {
    this.sngDescrizioneCausaleRPT = sngDescrizioneCausaleRPT;
  }

  public java.lang.String getSngContabilita() {
    return this.sngContabilita;
  }

  public void setSngContabilita(java.lang.String sngContabilita) {
    this.sngContabilita = sngContabilita;
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

  public java.lang.String getVrsCodRata() {
    return this.vrsCodRata;
  }

  public void setVrsCodRata(java.lang.String vrsCodRata) {
    this.vrsCodRata = vrsCodRata;
  }

  public IdDocumento getVrsIdDocumento() {
    return this.vrsIdDocumento;
  }

  public void setVrsIdDocumento(IdDocumento vrsIdDocumento) {
    this.vrsIdDocumento = vrsIdDocumento;
  }

  public java.lang.String getVrsTipo() {
    return this.vrsTipo;
  }

  public void setVrsTipo(java.lang.String vrsTipo) {
    this.vrsTipo = vrsTipo;
  }

  public java.lang.String getVrsProprieta() {
    return this.vrsProprieta;
  }

  public void setVrsProprieta(java.lang.String vrsProprieta) {
    this.vrsProprieta = vrsProprieta;
  }

  public java.lang.String getPagCodDominio() {
    return this.pagCodDominio;
  }

  public void setPagCodDominio(java.lang.String pagCodDominio) {
    this.pagCodDominio = pagCodDominio;
  }

  public java.lang.String getPagIuv() {
    return this.pagIuv;
  }

  public void setPagIuv(java.lang.String pagIuv) {
    this.pagIuv = pagIuv;
  }

  public int getPagIndiceDati() {
    return this.pagIndiceDati;
  }

  public void setPagIndiceDati(int pagIndiceDati) {
    this.pagIndiceDati = pagIndiceDati;
  }

  public double getPagImportoPagato() {
    return this.pagImportoPagato;
  }

  public void setPagImportoPagato(double pagImportoPagato) {
    this.pagImportoPagato = pagImportoPagato;
  }

  public java.util.Date getPagDataAcquisizione() {
    return this.pagDataAcquisizione;
  }

  public void setPagDataAcquisizione(java.util.Date pagDataAcquisizione) {
    this.pagDataAcquisizione = pagDataAcquisizione;
  }

  public java.lang.String getPagIur() {
    return this.pagIur;
  }

  public void setPagIur(java.lang.String pagIur) {
    this.pagIur = pagIur;
  }

  public java.util.Date getPagDataPagamento() {
    return this.pagDataPagamento;
  }

  public void setPagDataPagamento(java.util.Date pagDataPagamento) {
    this.pagDataPagamento = pagDataPagamento;
  }

  public java.lang.Double getPagCommissioniPsp() {
    return this.pagCommissioniPsp;
  }

  public void setPagCommissioniPsp(java.lang.Double pagCommissioniPsp) {
    this.pagCommissioniPsp = pagCommissioniPsp;
  }

  public java.lang.String getPagTipoAllegato() {
    return this.pagTipoAllegato;
  }

  public void setPagTipoAllegato(java.lang.String pagTipoAllegato) {
    this.pagTipoAllegato = pagTipoAllegato;
  }

  public byte[] getPagAllegato() {
    return this.pagAllegato;
  }

  public void setPagAllegato(byte[] pagAllegato) {
    this.pagAllegato = pagAllegato;
  }

  public java.util.Date getPagDataAcquisizioneRevoca() {
    return this.pagDataAcquisizioneRevoca;
  }

  public void setPagDataAcquisizioneRevoca(java.util.Date pagDataAcquisizioneRevoca) {
    this.pagDataAcquisizioneRevoca = pagDataAcquisizioneRevoca;
  }

  public java.lang.String getPagCausaleRevoca() {
    return this.pagCausaleRevoca;
  }

  public void setPagCausaleRevoca(java.lang.String pagCausaleRevoca) {
    this.pagCausaleRevoca = pagCausaleRevoca;
  }

  public java.lang.String getPagDatiRevoca() {
    return this.pagDatiRevoca;
  }

  public void setPagDatiRevoca(java.lang.String pagDatiRevoca) {
    this.pagDatiRevoca = pagDatiRevoca;
  }

  public java.lang.Double getPagImportoRevocato() {
    return this.pagImportoRevocato;
  }

  public void setPagImportoRevocato(java.lang.Double pagImportoRevocato) {
    this.pagImportoRevocato = pagImportoRevocato;
  }

  public java.lang.String getPagEsitoRevoca() {
    return this.pagEsitoRevoca;
  }

  public void setPagEsitoRevoca(java.lang.String pagEsitoRevoca) {
    this.pagEsitoRevoca = pagEsitoRevoca;
  }

  public java.lang.String getPagDatiEsitoRevoca() {
    return this.pagDatiEsitoRevoca;
  }

  public void setPagDatiEsitoRevoca(java.lang.String pagDatiEsitoRevoca) {
    this.pagDatiEsitoRevoca = pagDatiEsitoRevoca;
  }

  public java.lang.String getPagStato() {
    return this.pagStato;
  }

  public void setPagStato(java.lang.String pagStato) {
    this.pagStato = pagStato;
  }

  public java.lang.String getPagTipo() {
    return this.pagTipo;
  }

  public void setPagTipo(java.lang.String pagTipo) {
    this.pagTipo = pagTipo;
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

  private static it.govpay.orm.model.VistaRendicontazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaRendicontazione.modelStaticInstance==null){
  			it.govpay.orm.VistaRendicontazione.modelStaticInstance = new it.govpay.orm.model.VistaRendicontazioneModel();
	  }
  }
  public static it.govpay.orm.model.VistaRendicontazioneModel model(){
	  if(it.govpay.orm.VistaRendicontazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaRendicontazione.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frCodPsp",required=true,nillable=false)
  protected java.lang.String frCodPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frCodDominio",required=true,nillable=false)
  protected java.lang.String frCodDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frCodFlusso",required=true,nillable=false)
  protected java.lang.String frCodFlusso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frStato",required=true,nillable=false)
  protected java.lang.String frStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frDescrizioneStato",required=false,nillable=false)
  protected java.lang.String frDescrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frIur",required=true,nillable=false)
  protected java.lang.String frIur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="frDataOraFlusso",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date frDataOraFlusso;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="frDataRegolamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date frDataRegolamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="frDataAcquisizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date frDataAcquisizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="frNumeroPagamenti",required=false,nillable=false)
  protected long frNumeroPagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="frImportoTotalePagamenti",required=false,nillable=false)
  protected java.lang.Double frImportoTotalePagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frCodBicRiversamento",required=false,nillable=false)
  protected java.lang.String frCodBicRiversamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="frId",required=false,nillable=false)
  protected long frId;

  @XmlElement(name="frIdIncasso",required=false,nillable=false)
  protected IdIncasso frIdIncasso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frRagioneSocialePsp",required=false,nillable=false)
  protected java.lang.String frRagioneSocialePsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frRagioneSocialeDominio",required=false,nillable=false)
  protected java.lang.String frRagioneSocialeDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="frObsoleto",required=true,nillable=false)
  protected Boolean frObsoleto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rndIuv",required=true,nillable=false)
  protected java.lang.String rndIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rndIur",required=true,nillable=false)
  protected java.lang.String rndIur;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="rndIndiceDati",required=false,nillable=false)
  protected java.lang.Integer rndIndiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="rndImportoPagato",required=false,nillable=false)
  protected java.lang.Double rndImportoPagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="rndEsito",required=false,nillable=false)
  protected java.lang.Integer rndEsito;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="rndData",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date rndData;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rndStato",required=true,nillable=false)
  protected java.lang.String rndStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="rndAnomalie",required=false,nillable=false)
  protected java.lang.String rndAnomalie;

  @XmlElement(name="rndIdPagamento",required=false,nillable=false)
  protected IdPagamento rndIdPagamento;

  @XmlElement(name="sngIdTributo",required=false,nillable=false)
  protected IdTributo sngIdTributo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngCodSingVersEnte",required=true,nillable=false)
  protected java.lang.String sngCodSingVersEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngStatoSingoloVersamento",required=true,nillable=false)
  protected java.lang.String sngStatoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="sngImportoSingoloVersamento",required=true,nillable=false)
  protected double sngImportoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngDescrizione",required=false,nillable=false)
  protected java.lang.String sngDescrizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngDatiAllegati",required=false,nillable=false)
  protected java.lang.String sngDatiAllegati;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="sngIndiceDati",required=true,nillable=false)
  protected java.lang.Integer sngIndiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngDescrizioneCausaleRPT",required=false,nillable=false)
  protected java.lang.String sngDescrizioneCausaleRPT;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sngContabilita",required=false,nillable=false)
  protected java.lang.String sngContabilita;

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
  @XmlElement(name="vrsCodRata",required=false,nillable=false)
  protected java.lang.String vrsCodRata;

  @XmlElement(name="vrsIdDocumento",required=false,nillable=false)
  protected IdDocumento vrsIdDocumento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsTipo",required=true,nillable=false)
  protected java.lang.String vrsTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="vrsProprieta",required=false,nillable=false)
  protected java.lang.String vrsProprieta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagCodDominio",required=true,nillable=false)
  protected java.lang.String pagCodDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagIuv",required=true,nillable=false)
  protected java.lang.String pagIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="pagIndiceDati",required=true,nillable=false)
  protected int pagIndiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="pagImportoPagato",required=true,nillable=false)
  protected double pagImportoPagato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="pagDataAcquisizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date pagDataAcquisizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagIur",required=true,nillable=false)
  protected java.lang.String pagIur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="pagDataPagamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date pagDataPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="pagCommissioniPsp",required=false,nillable=false)
  protected java.lang.Double pagCommissioniPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagTipoAllegato",required=false,nillable=false)
  protected java.lang.String pagTipoAllegato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="pagAllegato",required=false,nillable=false)
  protected byte[] pagAllegato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="pagDataAcquisizioneRevoca",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date pagDataAcquisizioneRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagCausaleRevoca",required=false,nillable=false)
  protected java.lang.String pagCausaleRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagDatiRevoca",required=false,nillable=false)
  protected java.lang.String pagDatiRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="pagImportoRevocato",required=false,nillable=false)
  protected java.lang.Double pagImportoRevocato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagEsitoRevoca",required=false,nillable=false)
  protected java.lang.String pagEsitoRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagDatiEsitoRevoca",required=false,nillable=false)
  protected java.lang.String pagDatiEsitoRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagStato",required=false,nillable=false)
  protected java.lang.String pagStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagTipo",required=true,nillable=false)
  protected java.lang.String pagTipo;

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
