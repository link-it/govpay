/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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


/** <p>Java class for Pagamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Pagamento">
 * 		&lt;sequence>
 * 			&lt;element name="idRPT" type="{http://www.govpay.it/orm}id-rpt" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="commissioniPsp" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoAllegato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="allegato" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idFrApplicazione" type="{http://www.govpay.it/orm}id-fr-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rendicontazioneEsito" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rendicontazioneData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codflussoRendicontazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="annoRiferimento" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="indiceSingoloPagamento" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idRr" type="{http://www.govpay.it/orm}id-rr" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="causaleRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoRevocato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiEsitoRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idFrApplicazioneRevoca" type="{http://www.govpay.it/orm}id-fr-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rendicontazioneEsitoRevoca" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rendicontazioneDataRevoca" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codFlussoRendicontazioneRevoca" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="annoRiferimentoRevoca" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="indiceSingoloPagamentoRevoca" type="{http://www.govpay.it/orm}integer" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Pagamento", 
  propOrder = {
  	"idRPT",
  	"idSingoloVersamento",
  	"codSingoloVersamentoEnte",
  	"importoPagato",
  	"iur",
  	"dataPagamento",
  	"commissioniPsp",
  	"tipoAllegato",
  	"allegato",
  	"idFrApplicazione",
  	"rendicontazioneEsito",
  	"rendicontazioneData",
  	"codflussoRendicontazione",
  	"_decimalWrapper_annoRiferimento",
  	"_decimalWrapper_indiceSingoloPagamento",
  	"idRr",
  	"causaleRevoca",
  	"datiRevoca",
  	"importoRevocato",
  	"esitoRevoca",
  	"datiEsitoRevoca",
  	"idFrApplicazioneRevoca",
  	"_decimalWrapper_rendicontazioneEsitoRevoca",
  	"rendicontazioneDataRevoca",
  	"codFlussoRendicontazioneRevoca",
  	"_decimalWrapper_annoRiferimentoRevoca",
  	"_decimalWrapper_indiceSingoloPagamentoRevoca"
  }
)

@XmlRootElement(name = "Pagamento")

public class Pagamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Pagamento() {
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

  public IdRpt getIdRPT() {
    return this.idRPT;
  }

  public void setIdRPT(IdRpt idRPT) {
    this.idRPT = idRPT;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(double importoPagato) {
    this.importoPagato = importoPagato;
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

  public IdFrApplicazione getIdFrApplicazione() {
    return this.idFrApplicazione;
  }

  public void setIdFrApplicazione(IdFrApplicazione idFrApplicazione) {
    this.idFrApplicazione = idFrApplicazione;
  }

  public java.lang.Integer getRendicontazioneEsito() {
    return this.rendicontazioneEsito;
  }

  public void setRendicontazioneEsito(java.lang.Integer rendicontazioneEsito) {
    this.rendicontazioneEsito = rendicontazioneEsito;
  }

  public java.util.Date getRendicontazioneData() {
    return this.rendicontazioneData;
  }

  public void setRendicontazioneData(java.util.Date rendicontazioneData) {
    this.rendicontazioneData = rendicontazioneData;
  }

  public java.lang.String getCodflussoRendicontazione() {
    return this.codflussoRendicontazione;
  }

  public void setCodflussoRendicontazione(java.lang.String codflussoRendicontazione) {
    this.codflussoRendicontazione = codflussoRendicontazione;
  }

  public java.lang.Integer getAnnoRiferimento() {
    if(this._decimalWrapper_annoRiferimento!=null){
		return (java.lang.Integer) this._decimalWrapper_annoRiferimento.getObject(java.lang.Integer.class);
	}else{
		return this.annoRiferimento;
	}
  }

  public void setAnnoRiferimento(java.lang.Integer annoRiferimento) {
    if(annoRiferimento!=null){
		this._decimalWrapper_annoRiferimento = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,4,annoRiferimento);
	}
  }

  public java.lang.Integer getIndiceSingoloPagamento() {
    if(this._decimalWrapper_indiceSingoloPagamento!=null){
		return (java.lang.Integer) this._decimalWrapper_indiceSingoloPagamento.getObject(java.lang.Integer.class);
	}else{
		return this.indiceSingoloPagamento;
	}
  }

  public void setIndiceSingoloPagamento(java.lang.Integer indiceSingoloPagamento) {
    if(indiceSingoloPagamento!=null){
		this._decimalWrapper_indiceSingoloPagamento = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,indiceSingoloPagamento);
	}
  }

  public IdRr getIdRr() {
    return this.idRr;
  }

  public void setIdRr(IdRr idRr) {
    this.idRr = idRr;
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

  public IdFrApplicazione getIdFrApplicazioneRevoca() {
    return this.idFrApplicazioneRevoca;
  }

  public void setIdFrApplicazioneRevoca(IdFrApplicazione idFrApplicazioneRevoca) {
    this.idFrApplicazioneRevoca = idFrApplicazioneRevoca;
  }

  public java.lang.Integer getRendicontazioneEsitoRevoca() {
    if(this._decimalWrapper_rendicontazioneEsitoRevoca!=null){
		return (java.lang.Integer) this._decimalWrapper_rendicontazioneEsitoRevoca.getObject(java.lang.Integer.class);
	}else{
		return this.rendicontazioneEsitoRevoca;
	}
  }

  public void setRendicontazioneEsitoRevoca(java.lang.Integer rendicontazioneEsitoRevoca) {
    if(rendicontazioneEsitoRevoca!=null){
		this._decimalWrapper_rendicontazioneEsitoRevoca = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,rendicontazioneEsitoRevoca);
	}
  }

  public java.util.Date getRendicontazioneDataRevoca() {
    return this.rendicontazioneDataRevoca;
  }

  public void setRendicontazioneDataRevoca(java.util.Date rendicontazioneDataRevoca) {
    this.rendicontazioneDataRevoca = rendicontazioneDataRevoca;
  }

  public java.lang.String getCodFlussoRendicontazioneRevoca() {
    return this.codFlussoRendicontazioneRevoca;
  }

  public void setCodFlussoRendicontazioneRevoca(java.lang.String codFlussoRendicontazioneRevoca) {
    this.codFlussoRendicontazioneRevoca = codFlussoRendicontazioneRevoca;
  }

  public java.lang.Integer getAnnoRiferimentoRevoca() {
    if(this._decimalWrapper_annoRiferimentoRevoca!=null){
		return (java.lang.Integer) this._decimalWrapper_annoRiferimentoRevoca.getObject(java.lang.Integer.class);
	}else{
		return this.annoRiferimentoRevoca;
	}
  }

  public void setAnnoRiferimentoRevoca(java.lang.Integer annoRiferimentoRevoca) {
    if(annoRiferimentoRevoca!=null){
		this._decimalWrapper_annoRiferimentoRevoca = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,4,annoRiferimentoRevoca);
	}
  }

  public java.lang.Integer getIndiceSingoloPagamentoRevoca() {
    if(this._decimalWrapper_indiceSingoloPagamentoRevoca!=null){
		return (java.lang.Integer) this._decimalWrapper_indiceSingoloPagamentoRevoca.getObject(java.lang.Integer.class);
	}else{
		return this.indiceSingoloPagamentoRevoca;
	}
  }

  public void setIndiceSingoloPagamentoRevoca(java.lang.Integer indiceSingoloPagamentoRevoca) {
    if(indiceSingoloPagamentoRevoca!=null){
		this._decimalWrapper_indiceSingoloPagamentoRevoca = new org.openspcoop2.utils.jaxb.DecimalWrapper(1,1,indiceSingoloPagamentoRevoca);
	}
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.PagamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Pagamento.modelStaticInstance==null){
  			it.govpay.orm.Pagamento.modelStaticInstance = new it.govpay.orm.model.PagamentoModel();
	  }
  }
  public static it.govpay.orm.model.PagamentoModel model(){
	  if(it.govpay.orm.Pagamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Pagamento.modelStaticInstance;
  }


  @XmlElement(name="idRPT",required=true,nillable=false)
  protected IdRpt idRPT;

  @XmlElement(name="idSingoloVersamento",required=true,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoPagato",required=true,nillable=false)
  protected double importoPagato;

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

  @XmlElement(name="idFrApplicazione",required=false,nillable=false)
  protected IdFrApplicazione idFrApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="rendicontazioneEsito",required=false,nillable=false)
  protected java.lang.Integer rendicontazioneEsito;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="rendicontazioneData",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date rendicontazioneData;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codflussoRendicontazione",required=false,nillable=false)
  protected java.lang.String codflussoRendicontazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="annoRiferimento",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_annoRiferimento = null;

  @XmlTransient
  protected java.lang.Integer annoRiferimento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="indiceSingoloPagamento",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_indiceSingoloPagamento = null;

  @XmlTransient
  protected java.lang.Integer indiceSingoloPagamento;

  @XmlElement(name="idRr",required=false,nillable=false)
  protected IdRr idRr;

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

  @XmlElement(name="idFrApplicazioneRevoca",required=false,nillable=false)
  protected IdFrApplicazione idFrApplicazioneRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="rendicontazioneEsitoRevoca",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_rendicontazioneEsitoRevoca = null;

  @XmlTransient
  protected java.lang.Integer rendicontazioneEsitoRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="rendicontazioneDataRevoca",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date rendicontazioneDataRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codFlussoRendicontazioneRevoca",required=false,nillable=false)
  protected java.lang.String codFlussoRendicontazioneRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="annoRiferimentoRevoca",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_annoRiferimentoRevoca = null;

  @XmlTransient
  protected java.lang.Integer annoRiferimentoRevoca;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Decimal2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="indiceSingoloPagamentoRevoca",required=false,nillable=false)
  org.openspcoop2.utils.jaxb.DecimalWrapper _decimalWrapper_indiceSingoloPagamentoRevoca = null;

  @XmlTransient
  protected java.lang.Integer indiceSingoloPagamentoRevoca;

}
