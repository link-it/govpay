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


/** <p>Java class for id-versamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id-versamento"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="srcDebitoreIdentificativo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="debitoreAnagrafica" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codVersamentoLotto" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codAnnoTributario" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoTotale" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="causaleVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="statoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idUo" type="{http://www.govpay.it/orm}id-uo" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idTipoVersamento" type="{http://www.govpay.it/orm}id-tipo-versamento" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="divisione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="direzione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tassonomia" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "id-versamento", 
  propOrder = {
  	"codVersamentoEnte",
  	"idApplicazione",
  	"srcDebitoreIdentificativo",
  	"debitoreAnagrafica",
  	"codVersamentoLotto",
  	"codAnnoTributario",
  	"importoTotale",
  	"causaleVersamento",
  	"statoVersamento",
  	"idUo",
  	"idTipoVersamento",
  	"divisione",
  	"direzione",
  	"tassonomia",
  	"tipo"
  }
)

@XmlRootElement(name = "id-versamento")

public class IdVersamento extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public IdVersamento() {
    super();
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getSrcDebitoreIdentificativo() {
    return this.srcDebitoreIdentificativo;
  }

  public void setSrcDebitoreIdentificativo(java.lang.String srcDebitoreIdentificativo) {
    this.srcDebitoreIdentificativo = srcDebitoreIdentificativo;
  }

  public java.lang.String getDebitoreAnagrafica() {
    return this.debitoreAnagrafica;
  }

  public void setDebitoreAnagrafica(java.lang.String debitoreAnagrafica) {
    this.debitoreAnagrafica = debitoreAnagrafica;
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

  public double getImportoTotale() {
    return this.importoTotale;
  }

  public void setImportoTotale(double importoTotale) {
    this.importoTotale = importoTotale;
  }

  public java.lang.String getCausaleVersamento() {
    return this.causaleVersamento;
  }

  public void setCausaleVersamento(java.lang.String causaleVersamento) {
    this.causaleVersamento = causaleVersamento;
  }

  public java.lang.String getStatoVersamento() {
    return this.statoVersamento;
  }

  public void setStatoVersamento(java.lang.String statoVersamento) {
    this.statoVersamento = statoVersamento;
  }

  public IdUo getIdUo() {
    return this.idUo;
  }

  public void setIdUo(IdUo idUo) {
    this.idUo = idUo;
  }

  public IdTipoVersamento getIdTipoVersamento() {
    return this.idTipoVersamento;
  }

  public void setIdTipoVersamento(IdTipoVersamento idTipoVersamento) {
    this.idTipoVersamento = idTipoVersamento;
  }

  public java.lang.String getDivisione() {
    return this.divisione;
  }

  public void setDivisione(java.lang.String divisione) {
    this.divisione = divisione;
  }

  public java.lang.String getDirezione() {
    return this.direzione;
  }

  public void setDirezione(java.lang.String direzione) {
    this.direzione = direzione;
  }

  public java.lang.String getTassonomia() {
    return this.tassonomia;
  }

  public void setTassonomia(java.lang.String tassonomia) {
    this.tassonomia = tassonomia;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  private static final long serialVersionUID = 1L;



  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="srcDebitoreIdentificativo",required=false,nillable=false)
  protected java.lang.String srcDebitoreIdentificativo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="debitoreAnagrafica",required=false,nillable=false)
  protected java.lang.String debitoreAnagrafica;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoLotto",required=false,nillable=false)
  protected java.lang.String codVersamentoLotto;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codAnnoTributario",required=false,nillable=false)
  protected java.lang.String codAnnoTributario;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotale",required=false,nillable=false)
  protected double importoTotale;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleVersamento",required=false,nillable=false)
  protected java.lang.String causaleVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoVersamento",required=false,nillable=false)
  protected java.lang.String statoVersamento;

  @XmlElement(name="idUo",required=true,nillable=false)
  protected IdUo idUo;

  @XmlElement(name="idTipoVersamento",required=true,nillable=false)
  protected IdTipoVersamento idTipoVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="divisione",required=false,nillable=false)
  protected java.lang.String divisione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="direzione",required=false,nillable=false)
  protected java.lang.String direzione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tassonomia",required=false,nillable=false)
  protected java.lang.String tassonomia;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=false,nillable=false)
  protected java.lang.String tipo;

}
