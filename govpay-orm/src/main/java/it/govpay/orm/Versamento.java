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


/** <p>Java class for Versamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Versamento">
 * 		&lt;sequence>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idUo" type="{http://www.govpay.it/orm}id-uo" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoTotale" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="statoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="aggiornabile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataScadenza" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataOraUltimoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="causaleVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="debitoreAnagrafica" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="debitoreIndirizzo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreCivico" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreCap" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreLocalita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreProvincia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreNazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreEmail" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreTelefono" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreCellulare" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="debitoreFax" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codAnnoTributario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codBundlekey" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
  	"idUo",
  	"idApplicazione",
  	"importoTotale",
  	"statoVersamento",
  	"descrizioneStato",
  	"aggiornabile",
  	"dataCreazione",
  	"dataScadenza",
  	"dataOraUltimoAggiornamento",
  	"causaleVersamento",
  	"debitoreIdentificativo",
  	"debitoreAnagrafica",
  	"debitoreIndirizzo",
  	"debitoreCivico",
  	"debitoreCap",
  	"debitoreLocalita",
  	"debitoreProvincia",
  	"debitoreNazione",
  	"debitoreEmail",
  	"debitoreTelefono",
  	"debitoreCellulare",
  	"debitoreFax",
  	"codLotto",
  	"codVersamentoLotto",
  	"codAnnoTributario",
  	"codBundlekey"
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

  public IdUo getIdUo() {
    return this.idUo;
  }

  public void setIdUo(IdUo idUo) {
    this.idUo = idUo;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
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

  public boolean isAggiornabile() {
    return this.aggiornabile;
  }

  public boolean getAggiornabile() {
    return this.aggiornabile;
  }

  public void setAggiornabile(boolean aggiornabile) {
    this.aggiornabile = aggiornabile;
  }

  public java.util.Date getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(java.util.Date dataCreazione) {
    this.dataCreazione = dataCreazione;
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

  public java.lang.String getCausaleVersamento() {
    return this.causaleVersamento;
  }

  public void setCausaleVersamento(java.lang.String causaleVersamento) {
    this.causaleVersamento = causaleVersamento;
  }

  public java.lang.String getDebitoreIdentificativo() {
    return this.debitoreIdentificativo;
  }

  public void setDebitoreIdentificativo(java.lang.String debitoreIdentificativo) {
    this.debitoreIdentificativo = debitoreIdentificativo;
  }

  public java.lang.String getDebitoreAnagrafica() {
    return this.debitoreAnagrafica;
  }

  public void setDebitoreAnagrafica(java.lang.String debitoreAnagrafica) {
    this.debitoreAnagrafica = debitoreAnagrafica;
  }

  public java.lang.String getDebitoreIndirizzo() {
    return this.debitoreIndirizzo;
  }

  public void setDebitoreIndirizzo(java.lang.String debitoreIndirizzo) {
    this.debitoreIndirizzo = debitoreIndirizzo;
  }

  public java.lang.String getDebitoreCivico() {
    return this.debitoreCivico;
  }

  public void setDebitoreCivico(java.lang.String debitoreCivico) {
    this.debitoreCivico = debitoreCivico;
  }

  public java.lang.String getDebitoreCap() {
    return this.debitoreCap;
  }

  public void setDebitoreCap(java.lang.String debitoreCap) {
    this.debitoreCap = debitoreCap;
  }

  public java.lang.String getDebitoreLocalita() {
    return this.debitoreLocalita;
  }

  public void setDebitoreLocalita(java.lang.String debitoreLocalita) {
    this.debitoreLocalita = debitoreLocalita;
  }

  public java.lang.String getDebitoreProvincia() {
    return this.debitoreProvincia;
  }

  public void setDebitoreProvincia(java.lang.String debitoreProvincia) {
    this.debitoreProvincia = debitoreProvincia;
  }

  public java.lang.String getDebitoreNazione() {
    return this.debitoreNazione;
  }

  public void setDebitoreNazione(java.lang.String debitoreNazione) {
    this.debitoreNazione = debitoreNazione;
  }

  public java.lang.String getDebitoreEmail() {
    return this.debitoreEmail;
  }

  public void setDebitoreEmail(java.lang.String debitoreEmail) {
    this.debitoreEmail = debitoreEmail;
  }

  public java.lang.String getDebitoreTelefono() {
    return this.debitoreTelefono;
  }

  public void setDebitoreTelefono(java.lang.String debitoreTelefono) {
    this.debitoreTelefono = debitoreTelefono;
  }

  public java.lang.String getDebitoreCellulare() {
    return this.debitoreCellulare;
  }

  public void setDebitoreCellulare(java.lang.String debitoreCellulare) {
    this.debitoreCellulare = debitoreCellulare;
  }

  public java.lang.String getDebitoreFax() {
    return this.debitoreFax;
  }

  public void setDebitoreFax(java.lang.String debitoreFax) {
    this.debitoreFax = debitoreFax;
  }

  public java.lang.String getCodLotto() {
    return this.codLotto;
  }

  public void setCodLotto(java.lang.String codLotto) {
    this.codLotto = codLotto;
  }

  public java.lang.String getCodVersamentoLotto() {
    return this.codVersamentoLotto;
  }

  public void setCodVersamentoLotto(java.lang.String codVersamentoLotto) {
    this.codVersamentoLotto = codVersamentoLotto;
  }

  public java.lang.String getCodAnnoTributario() {
    return this.codAnnoTributario;
  }

  public void setCodAnnoTributario(java.lang.String codAnnoTributario) {
    this.codAnnoTributario = codAnnoTributario;
  }

  public java.lang.String getCodBundlekey() {
    return this.codBundlekey;
  }

  public void setCodBundlekey(java.lang.String codBundlekey) {
    this.codBundlekey = codBundlekey;
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

  @XmlElement(name="idUo",required=true,nillable=false)
  protected IdUo idUo;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotale",required=true,nillable=false)
  protected double importoTotale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoVersamento",required=true,nillable=false)
  protected java.lang.String statoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="aggiornabile",required=true,nillable=false)
  protected boolean aggiornabile;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataScadenza",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataScadenza;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraUltimoAggiornamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraUltimoAggiornamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleVersamento",required=false,nillable=false)
  protected java.lang.String causaleVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreIdentificativo",required=true,nillable=false)
  protected java.lang.String debitoreIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreAnagrafica",required=true,nillable=false)
  protected java.lang.String debitoreAnagrafica;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreIndirizzo",required=false,nillable=false)
  protected java.lang.String debitoreIndirizzo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCivico",required=false,nillable=false)
  protected java.lang.String debitoreCivico;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCap",required=false,nillable=false)
  protected java.lang.String debitoreCap;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreLocalita",required=false,nillable=false)
  protected java.lang.String debitoreLocalita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreProvincia",required=false,nillable=false)
  protected java.lang.String debitoreProvincia;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreNazione",required=false,nillable=false)
  protected java.lang.String debitoreNazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreEmail",required=false,nillable=false)
  protected java.lang.String debitoreEmail;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreTelefono",required=false,nillable=false)
  protected java.lang.String debitoreTelefono;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreCellulare",required=false,nillable=false)
  protected java.lang.String debitoreCellulare;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreFax",required=false,nillable=false)
  protected java.lang.String debitoreFax;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codLotto",required=false,nillable=false)
  protected java.lang.String codLotto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoLotto",required=false,nillable=false)
  protected java.lang.String codVersamentoLotto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codAnnoTributario",required=false,nillable=false)
  protected java.lang.String codAnnoTributario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codBundlekey",required=false,nillable=false)
  protected java.lang.String codBundlekey;

}
