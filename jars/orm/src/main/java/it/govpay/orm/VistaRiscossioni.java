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


/** <p>Java class for VistaRiscossioni complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaRiscossioni">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cod_flusso" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="fr_iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagamenti" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataPagamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codTipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codEntrata" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="identificativoDebitore" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreAnagrafica" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="anno" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="descrTipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ragioneSocialePsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codRata" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idDocumento" type="{http://www.govpay.it/orm}id-documento" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="causaleVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoVersamento" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numeroAvviso" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuvPagamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="contabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "VistaRiscossioni", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"iur",
  	"codFlusso",
  	"frIur",
  	"dataRegolamento",
  	"numeroPagamenti",
  	"importoTotalePagamenti",
  	"importoPagato",
  	"codSingoloVersamentoEnte",
  	"indiceDati",
  	"codVersamentoEnte",
  	"codApplicazione",
  	"dataPagamento",
  	"codTipoVersamento",
  	"codEntrata",
  	"identificativoDebitore",
  	"debitoreAnagrafica",
  	"anno",
  	"descrTipoVersamento",
  	"codPsp",
  	"ragioneSocialePsp",
  	"codRata",
  	"idDocumento",
  	"causaleVersamento",
  	"importoVersamento",
  	"numeroAvviso",
  	"iuvPagamento",
  	"dataScadenza",
  	"contabilita"
  }
)

@XmlRootElement(name = "VistaRiscossioni")

public class VistaRiscossioni extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public VistaRiscossioni() {
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

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.lang.String getCodFlusso() {
    return this.codFlusso;
  }

  public void setCodFlusso(java.lang.String codFlusso) {
    this.codFlusso = codFlusso;
  }

  public java.lang.String getFrIur() {
    return this.frIur;
  }

  public void setFrIur(java.lang.String frIur) {
    this.frIur = frIur;
  }

  public java.util.Date getDataRegolamento() {
    return this.dataRegolamento;
  }

  public void setDataRegolamento(java.util.Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  public long getNumeroPagamenti() {
    return this.numeroPagamenti;
  }

  public void setNumeroPagamenti(long numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  public java.lang.Double getImportoTotalePagamenti() {
    return this.importoTotalePagamenti;
  }

  public void setImportoTotalePagamenti(java.lang.Double importoTotalePagamenti) {
    this.importoTotalePagamenti = importoTotalePagamenti;
  }

  public java.lang.Double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(java.lang.Double importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public java.lang.Integer getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(java.lang.Integer indiceDati) {
    this.indiceDati = indiceDati;
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  public java.lang.String getCodApplicazione() {
    return this.codApplicazione;
  }

  public void setCodApplicazione(java.lang.String codApplicazione) {
    this.codApplicazione = codApplicazione;
  }

  public java.util.Date getDataPagamento() {
    return this.dataPagamento;
  }

  public void setDataPagamento(java.util.Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public java.lang.String getCodTipoVersamento() {
    return this.codTipoVersamento;
  }

  public void setCodTipoVersamento(java.lang.String codTipoVersamento) {
    this.codTipoVersamento = codTipoVersamento;
  }

  public java.lang.String getCodEntrata() {
    return this.codEntrata;
  }

  public void setCodEntrata(java.lang.String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public java.lang.String getIdentificativoDebitore() {
    return this.identificativoDebitore;
  }

  public void setIdentificativoDebitore(java.lang.String identificativoDebitore) {
    this.identificativoDebitore = identificativoDebitore;
  }

  public java.lang.String getDebitoreAnagrafica() {
    return this.debitoreAnagrafica;
  }

  public void setDebitoreAnagrafica(java.lang.String debitoreAnagrafica) {
    this.debitoreAnagrafica = debitoreAnagrafica;
  }

  public java.lang.String getAnno() {
    return this.anno;
  }

  public void setAnno(java.lang.String anno) {
    this.anno = anno;
  }

  public java.lang.String getDescrTipoVersamento() {
    return this.descrTipoVersamento;
  }

  public void setDescrTipoVersamento(java.lang.String descrTipoVersamento) {
    this.descrTipoVersamento = descrTipoVersamento;
  }

  public java.lang.String getCodPsp() {
    return this.codPsp;
  }

  public void setCodPsp(java.lang.String codPsp) {
    this.codPsp = codPsp;
  }

  public java.lang.String getRagioneSocialePsp() {
    return this.ragioneSocialePsp;
  }

  public void setRagioneSocialePsp(java.lang.String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
  }

  public java.lang.String getCodRata() {
    return this.codRata;
  }

  public void setCodRata(java.lang.String codRata) {
    this.codRata = codRata;
  }

  public IdDocumento getIdDocumento() {
    return this.idDocumento;
  }

  public void setIdDocumento(IdDocumento idDocumento) {
    this.idDocumento = idDocumento;
  }

  public java.lang.String getCausaleVersamento() {
    return this.causaleVersamento;
  }

  public void setCausaleVersamento(java.lang.String causaleVersamento) {
    this.causaleVersamento = causaleVersamento;
  }

  public double getImportoVersamento() {
    return this.importoVersamento;
  }

  public void setImportoVersamento(double importoVersamento) {
    this.importoVersamento = importoVersamento;
  }

  public java.lang.String getNumeroAvviso() {
    return this.numeroAvviso;
  }

  public void setNumeroAvviso(java.lang.String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public java.lang.String getIuvPagamento() {
    return this.iuvPagamento;
  }

  public void setIuvPagamento(java.lang.String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
  }

  public java.util.Date getDataScadenza() {
    return this.dataScadenza;
  }

  public void setDataScadenza(java.util.Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public java.lang.String getContabilita() {
    return this.contabilita;
  }

  public void setContabilita(java.lang.String contabilita) {
    this.contabilita = contabilita;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.VistaRiscossioniModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaRiscossioni.modelStaticInstance==null){
  			it.govpay.orm.VistaRiscossioni.modelStaticInstance = new it.govpay.orm.model.VistaRiscossioniModel();
	  }
  }
  public static it.govpay.orm.model.VistaRiscossioniModel model(){
	  if(it.govpay.orm.VistaRiscossioni.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaRiscossioni.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cod_flusso",required=true,nillable=false)
  protected java.lang.String codFlusso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fr_iur",required=true,nillable=false)
  protected java.lang.String frIur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRegolamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRegolamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numeroPagamenti",required=false,nillable=false)
  protected long numeroPagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoTotalePagamenti",required=false,nillable=false)
  protected java.lang.Double importoTotalePagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoPagato",required=false,nillable=false)
  protected java.lang.Double importoPagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected java.lang.Integer indiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=true,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataPagamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTipoVersamento",required=false,nillable=false)
  protected java.lang.String codTipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codEntrata",required=false,nillable=false)
  protected java.lang.String codEntrata;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="identificativoDebitore",required=false,nillable=false)
  protected java.lang.String identificativoDebitore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreAnagrafica",required=true,nillable=false)
  protected java.lang.String debitoreAnagrafica;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="anno",required=false,nillable=false)
  protected java.lang.String anno;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrTipoVersamento",required=true,nillable=false)
  protected java.lang.String descrTipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=true,nillable=false)
  protected java.lang.String codPsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSocialePsp",required=false,nillable=false)
  protected java.lang.String ragioneSocialePsp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codRata",required=false,nillable=false)
  protected java.lang.String codRata;

  @XmlElement(name="idDocumento",required=false,nillable=false)
  protected IdDocumento idDocumento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleVersamento",required=false,nillable=false)
  protected java.lang.String causaleVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoVersamento",required=true,nillable=false)
  protected double importoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="numeroAvviso",required=false,nillable=false)
  protected java.lang.String numeroAvviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuvPagamento",required=false,nillable=false)
  protected java.lang.String iuvPagamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataScadenza;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="contabilita",required=false,nillable=false)
  protected java.lang.String contabilita;

}
