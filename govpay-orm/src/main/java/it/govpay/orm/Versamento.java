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


/** <p>Java class for Versamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Versamento">
 * 		&lt;sequence>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idEnte" type="{http://www.govpay.it/orm}id-ente" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idAnagraficaDebitore" type="{http://www.govpay.it/orm}id-anagrafica" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoTotale" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="statoVersamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="statoRendicontazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraUltimoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Versamento", 
  propOrder = {
  	"codVersamentoEnte",
  	"codDominio",
  	"iuv",
  	"idEnte",
  	"idApplicazione",
  	"idAnagraficaDebitore",
  	"importoTotale",
  	"statoVersamento",
  	"descrizioneStato",
  	"statoRendicontazione",
  	"importoPagato",
  	"dataScadenza",
  	"dataOraUltimoAggiornamento"
  }
)

@XmlRootElement(name = "Versamento")

public class Versamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Versamento() {
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

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
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

  public IdEnte getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(IdEnte idEnte) {
    this.idEnte = idEnte;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdAnagrafica getIdAnagraficaDebitore() {
    return this.idAnagraficaDebitore;
  }

  public void setIdAnagraficaDebitore(IdAnagrafica idAnagraficaDebitore) {
    this.idAnagraficaDebitore = idAnagraficaDebitore;
  }

  public double getImportoTotale() {
    return this.importoTotale;
  }

  public void setImportoTotale(double importoTotale) {
    this.importoTotale = importoTotale;
  }

  public java.lang.String getStatoVersamento() {
    return this.statoVersamento;
  }

  public void setStatoVersamento(java.lang.String statoVersamento) {
    this.statoVersamento = statoVersamento;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public java.lang.String getStatoRendicontazione() {
    return this.statoRendicontazione;
  }

  public void setStatoRendicontazione(java.lang.String statoRendicontazione) {
    this.statoRendicontazione = statoRendicontazione;
  }

  public java.lang.Double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(java.lang.Double importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.util.Date getDataScadenza() {
    return this.dataScadenza;
  }

  public void setDataScadenza(java.util.Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public java.util.Date getDataOraUltimoAggiornamento() {
    return this.dataOraUltimoAggiornamento;
  }

  public void setDataOraUltimoAggiornamento(java.util.Date dataOraUltimoAggiornamento) {
    this.dataOraUltimoAggiornamento = dataOraUltimoAggiornamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.VersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Versamento.modelStaticInstance==null){
  			it.govpay.orm.Versamento.modelStaticInstance = new it.govpay.orm.model.VersamentoModel();
	  }
  }
  public static it.govpay.orm.model.VersamentoModel model(){
	  if(it.govpay.orm.Versamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Versamento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @XmlElement(name="idEnte",required=true,nillable=false)
  protected IdEnte idEnte;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @XmlElement(name="idAnagraficaDebitore",required=true,nillable=false)
  protected IdAnagrafica idAnagraficaDebitore;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotale",required=true,nillable=false)
  protected double importoTotale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoVersamento",required=true,nillable=false)
  protected java.lang.String statoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoRendicontazione",required=false,nillable=false)
  protected java.lang.String statoRendicontazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoPagato",required=false,nillable=false)
  protected java.lang.Double importoPagato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataScadenza",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataScadenza;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraUltimoAggiornamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraUltimoAggiornamento;

}
