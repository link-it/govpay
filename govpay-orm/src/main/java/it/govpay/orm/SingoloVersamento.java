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


/** <p>Java class for SingoloVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingoloVersamento">
 * 		&lt;sequence>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indice" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idTributo" type="{http://www.govpay.it/orm}id-tributo" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ibanAccredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoSingoloVersamento" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoCommissioniPA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="singoloImportoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="causaleVersamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiSpecificiRiscossione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="statoSingoloVersamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esitoSingoloPagamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataEsitoSingoloPagamento" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "SingoloVersamento", 
  propOrder = {
  	"idVersamento",
  	"indice",
  	"codSingoloVersamentoEnte",
  	"idTributo",
  	"annoRiferimento",
  	"ibanAccredito",
  	"importoSingoloVersamento",
  	"importoCommissioniPA",
  	"singoloImportoPagato",
  	"causaleVersamento",
  	"datiSpecificiRiscossione",
  	"statoSingoloVersamento",
  	"esitoSingoloPagamento",
  	"dataEsitoSingoloPagamento",
  	"iur"
  }
)

@XmlRootElement(name = "SingoloVersamento")

public class SingoloVersamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public SingoloVersamento() {
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

  public int getIndice() {
    return this.indice;
  }

  public void setIndice(int indice) {
    this.indice = indice;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public IdTributo getIdTributo() {
    return this.idTributo;
  }

  public void setIdTributo(IdTributo idTributo) {
    this.idTributo = idTributo;
  }

  public int getAnnoRiferimento() {
    return this.annoRiferimento;
  }

  public void setAnnoRiferimento(int annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public java.lang.String getIbanAccredito() {
    return this.ibanAccredito;
  }

  public void setIbanAccredito(java.lang.String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public double getImportoSingoloVersamento() {
    return this.importoSingoloVersamento;
  }

  public void setImportoSingoloVersamento(double importoSingoloVersamento) {
    this.importoSingoloVersamento = importoSingoloVersamento;
  }

  public java.lang.Double getImportoCommissioniPA() {
    return this.importoCommissioniPA;
  }

  public void setImportoCommissioniPA(java.lang.Double importoCommissioniPA) {
    this.importoCommissioniPA = importoCommissioniPA;
  }

  public java.lang.Double getSingoloImportoPagato() {
    return this.singoloImportoPagato;
  }

  public void setSingoloImportoPagato(java.lang.Double singoloImportoPagato) {
    this.singoloImportoPagato = singoloImportoPagato;
  }

  public java.lang.String getCausaleVersamento() {
    return this.causaleVersamento;
  }

  public void setCausaleVersamento(java.lang.String causaleVersamento) {
    this.causaleVersamento = causaleVersamento;
  }

  public java.lang.String getDatiSpecificiRiscossione() {
    return this.datiSpecificiRiscossione;
  }

  public void setDatiSpecificiRiscossione(java.lang.String datiSpecificiRiscossione) {
    this.datiSpecificiRiscossione = datiSpecificiRiscossione;
  }

  public java.lang.String getStatoSingoloVersamento() {
    return this.statoSingoloVersamento;
  }

  public void setStatoSingoloVersamento(java.lang.String statoSingoloVersamento) {
    this.statoSingoloVersamento = statoSingoloVersamento;
  }

  public java.lang.String getEsitoSingoloPagamento() {
    return this.esitoSingoloPagamento;
  }

  public void setEsitoSingoloPagamento(java.lang.String esitoSingoloPagamento) {
    this.esitoSingoloPagamento = esitoSingoloPagamento;
  }

  public java.util.Date getDataEsitoSingoloPagamento() {
    return this.dataEsitoSingoloPagamento;
  }

  public void setDataEsitoSingoloPagamento(java.util.Date dataEsitoSingoloPagamento) {
    this.dataEsitoSingoloPagamento = dataEsitoSingoloPagamento;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.SingoloVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.SingoloVersamento.modelStaticInstance==null){
  			it.govpay.orm.SingoloVersamento.modelStaticInstance = new it.govpay.orm.model.SingoloVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.SingoloVersamentoModel model(){
	  if(it.govpay.orm.SingoloVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.SingoloVersamento.modelStaticInstance;
  }


  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="indice",required=true,nillable=false)
  protected int indice;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=false,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @XmlElement(name="idTributo",required=true,nillable=false)
  protected IdTributo idTributo;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="annoRiferimento",required=false,nillable=false)
  protected int annoRiferimento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ibanAccredito",required=false,nillable=false)
  protected java.lang.String ibanAccredito;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoSingoloVersamento",required=true,nillable=false)
  protected double importoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoCommissioniPA",required=false,nillable=false)
  protected java.lang.Double importoCommissioniPA;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="singoloImportoPagato",required=false,nillable=false)
  protected java.lang.Double singoloImportoPagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleVersamento",required=false,nillable=false)
  protected java.lang.String causaleVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiSpecificiRiscossione",required=false,nillable=false)
  protected java.lang.String datiSpecificiRiscossione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoSingoloVersamento",required=false,nillable=false)
  protected java.lang.String statoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esitoSingoloPagamento",required=false,nillable=false)
  protected java.lang.String esitoSingoloPagamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataEsitoSingoloPagamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataEsitoSingoloPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=false,nillable=false)
  protected java.lang.String iur;

}
