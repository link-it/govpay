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


/** <p>Java class for Pagamento complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Pagamento">
 * 		&lt;sequence>
 * 			&lt;element name="idRPT" type="{http://www.govpay.it/orm}id-rpt" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="0" maxOccurs="1"/>
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
 * 			&lt;element name="idIncasso" type="{http://www.govpay.it/orm}id-incasso" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
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
  	"idIncasso",
  	"tipo"
  }
)

@XmlRootElement(name = "Pagamento")

public class Pagamento extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Pagamento() {
    super();
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

  public java.math.BigDecimal getCommissioniPsp() {
    return this.commissioniPsp;
  }

  public void setCommissioniPsp(java.math.BigDecimal commissioniPsp) {
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

  public java.math.BigDecimal getImportoRevocato() {
    return this.importoRevocato;
  }

  public void setImportoRevocato(java.math.BigDecimal importoRevocato) {
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

  public IdIncasso getIdIncasso() {
    return this.idIncasso;
  }

  public void setIdIncasso(IdIncasso idIncasso) {
    this.idIncasso = idIncasso;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  private static final long serialVersionUID = 1L;

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


  @XmlElement(name="idRPT",required=false,nillable=false)
  protected IdRpt idRPT;

  @XmlElement(name="idSingoloVersamento",required=false,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected int indiceDati;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoPagato",required=true,nillable=false)
  protected double importoPagato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAcquisizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAcquisizione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataPagamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataPagamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="commissioniPsp",required=false,nillable=false)
  protected java.math.BigDecimal commissioniPsp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoAllegato",required=false,nillable=false)
  protected java.lang.String tipoAllegato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="allegato",required=false,nillable=false)
  protected byte[] allegato;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAcquisizioneRevoca",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAcquisizioneRevoca;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleRevoca",required=false,nillable=false)
  protected java.lang.String causaleRevoca;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiRevoca",required=false,nillable=false)
  protected java.lang.String datiRevoca;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoRevocato",required=false,nillable=false)
  protected java.math.BigDecimal importoRevocato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esitoRevoca",required=false,nillable=false)
  protected java.lang.String esitoRevoca;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiEsitoRevoca",required=false,nillable=false)
  protected java.lang.String datiEsitoRevoca;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=false,nillable=false)
  protected java.lang.String stato;

  @XmlElement(name="idIncasso",required=false,nillable=false)
  protected IdIncasso idIncasso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

}
