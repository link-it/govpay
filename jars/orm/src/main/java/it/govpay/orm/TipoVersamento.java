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


/** <p>Java class for TipoVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TipoVersamento">
 * 		&lt;sequence>
 * 			&lt;element name="codTipoVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codificaIuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipo" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagaTerzi" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="abilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="boFormTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boFormDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boValidazioneDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boTrasformazioneTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boTrasformazioneDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boCodApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="boAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="pagFormTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagFormDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagValidazioneDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagTrasformazioneTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagTrasformazioneDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagCodApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="pagAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvMailPromAvvAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvMailPromAvvPdf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromAvvTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromAvvOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromAvvMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromRicAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvMailPromRicPdf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromRicTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromRicOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromRicMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromRicEseguiti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromScadAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvMailPromScadPreavviso" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromScadTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromScadOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvMailPromScadMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="visualizzazioneDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tracCsvTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tracCsvHeaderRisposta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tracCsvTemplateRichiesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tracCsvTemplateRisposta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromAvvAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvAppIoPromAvvTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromAvvOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromAvvMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromRicAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvAppIoPromRicTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromRicOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromRicMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromRicEseguiti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromScadAbilitato" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="avvAppIoPromScadPreavviso" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromScadTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromScadOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="avvAppIoPromScadMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "TipoVersamento", 
  propOrder = {
  	"codTipoVersamento",
  	"descrizione",
  	"codificaIuv",
  	"tipo",
  	"pagaTerzi",
  	"abilitato",
  	"boFormTipo",
  	"boFormDefinizione",
  	"boValidazioneDef",
  	"boTrasformazioneTipo",
  	"boTrasformazioneDef",
  	"boCodApplicazione",
  	"boAbilitato",
  	"pagFormTipo",
  	"pagFormDefinizione",
  	"pagValidazioneDef",
  	"pagTrasformazioneTipo",
  	"pagTrasformazioneDef",
  	"pagCodApplicazione",
  	"pagAbilitato",
  	"avvMailPromAvvAbilitato",
  	"avvMailPromAvvPdf",
  	"avvMailPromAvvTipo",
  	"avvMailPromAvvOggetto",
  	"avvMailPromAvvMessaggio",
  	"avvMailPromRicAbilitato",
  	"avvMailPromRicPdf",
  	"avvMailPromRicTipo",
  	"avvMailPromRicOggetto",
  	"avvMailPromRicMessaggio",
  	"avvMailPromRicEseguiti",
  	"avvMailPromScadAbilitato",
  	"avvMailPromScadPreavviso",
  	"avvMailPromScadTipo",
  	"avvMailPromScadOggetto",
  	"avvMailPromScadMessaggio",
  	"visualizzazioneDefinizione",
  	"tracCsvTipo",
  	"tracCsvHeaderRisposta",
  	"tracCsvTemplateRichiesta",
  	"tracCsvTemplateRisposta",
  	"avvAppIoPromAvvAbilitato",
  	"avvAppIoPromAvvTipo",
  	"avvAppIoPromAvvOggetto",
  	"avvAppIoPromAvvMessaggio",
  	"avvAppIoPromRicAbilitato",
  	"avvAppIoPromRicTipo",
  	"avvAppIoPromRicOggetto",
  	"avvAppIoPromRicMessaggio",
  	"avvAppIoPromRicEseguiti",
  	"avvAppIoPromScadAbilitato",
  	"avvAppIoPromScadPreavviso",
  	"avvAppIoPromScadTipo",
  	"avvAppIoPromScadOggetto",
  	"avvAppIoPromScadMessaggio"
  }
)

@XmlRootElement(name = "TipoVersamento")

public class TipoVersamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public TipoVersamento() {
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

  public java.lang.String getCodTipoVersamento() {
    return this.codTipoVersamento;
  }

  public void setCodTipoVersamento(java.lang.String codTipoVersamento) {
    this.codTipoVersamento = codTipoVersamento;
  }

  public java.lang.String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(java.lang.String descrizione) {
    this.descrizione = descrizione;
  }

  public java.lang.String getCodificaIuv() {
    return this.codificaIuv;
  }

  public void setCodificaIuv(java.lang.String codificaIuv) {
    this.codificaIuv = codificaIuv;
  }

  public java.lang.String getTipo() {
    return this.tipo;
  }

  public void setTipo(java.lang.String tipo) {
    this.tipo = tipo;
  }

  public boolean isPagaTerzi() {
    return this.pagaTerzi;
  }

  public boolean getPagaTerzi() {
    return this.pagaTerzi;
  }

  public void setPagaTerzi(boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  public boolean isAbilitato() {
    return this.abilitato;
  }

  public boolean getAbilitato() {
    return this.abilitato;
  }

  public void setAbilitato(boolean abilitato) {
    this.abilitato = abilitato;
  }

  public java.lang.String getBoFormTipo() {
    return this.boFormTipo;
  }

  public void setBoFormTipo(java.lang.String boFormTipo) {
    this.boFormTipo = boFormTipo;
  }

  public java.lang.String getBoFormDefinizione() {
    return this.boFormDefinizione;
  }

  public void setBoFormDefinizione(java.lang.String boFormDefinizione) {
    this.boFormDefinizione = boFormDefinizione;
  }

  public java.lang.String getBoValidazioneDef() {
    return this.boValidazioneDef;
  }

  public void setBoValidazioneDef(java.lang.String boValidazioneDef) {
    this.boValidazioneDef = boValidazioneDef;
  }

  public java.lang.String getBoTrasformazioneTipo() {
    return this.boTrasformazioneTipo;
  }

  public void setBoTrasformazioneTipo(java.lang.String boTrasformazioneTipo) {
    this.boTrasformazioneTipo = boTrasformazioneTipo;
  }

  public java.lang.String getBoTrasformazioneDef() {
    return this.boTrasformazioneDef;
  }

  public void setBoTrasformazioneDef(java.lang.String boTrasformazioneDef) {
    this.boTrasformazioneDef = boTrasformazioneDef;
  }

  public java.lang.String getBoCodApplicazione() {
    return this.boCodApplicazione;
  }

  public void setBoCodApplicazione(java.lang.String boCodApplicazione) {
    this.boCodApplicazione = boCodApplicazione;
  }

  public boolean isBoAbilitato() {
    return this.boAbilitato;
  }

  public boolean getBoAbilitato() {
    return this.boAbilitato;
  }

  public void setBoAbilitato(boolean boAbilitato) {
    this.boAbilitato = boAbilitato;
  }

  public java.lang.String getPagFormTipo() {
    return this.pagFormTipo;
  }

  public void setPagFormTipo(java.lang.String pagFormTipo) {
    this.pagFormTipo = pagFormTipo;
  }

  public java.lang.String getPagFormDefinizione() {
    return this.pagFormDefinizione;
  }

  public void setPagFormDefinizione(java.lang.String pagFormDefinizione) {
    this.pagFormDefinizione = pagFormDefinizione;
  }

  public java.lang.String getPagValidazioneDef() {
    return this.pagValidazioneDef;
  }

  public void setPagValidazioneDef(java.lang.String pagValidazioneDef) {
    this.pagValidazioneDef = pagValidazioneDef;
  }

  public java.lang.String getPagTrasformazioneTipo() {
    return this.pagTrasformazioneTipo;
  }

  public void setPagTrasformazioneTipo(java.lang.String pagTrasformazioneTipo) {
    this.pagTrasformazioneTipo = pagTrasformazioneTipo;
  }

  public java.lang.String getPagTrasformazioneDef() {
    return this.pagTrasformazioneDef;
  }

  public void setPagTrasformazioneDef(java.lang.String pagTrasformazioneDef) {
    this.pagTrasformazioneDef = pagTrasformazioneDef;
  }

  public java.lang.String getPagCodApplicazione() {
    return this.pagCodApplicazione;
  }

  public void setPagCodApplicazione(java.lang.String pagCodApplicazione) {
    this.pagCodApplicazione = pagCodApplicazione;
  }

  public boolean isPagAbilitato() {
    return this.pagAbilitato;
  }

  public boolean getPagAbilitato() {
    return this.pagAbilitato;
  }

  public void setPagAbilitato(boolean pagAbilitato) {
    this.pagAbilitato = pagAbilitato;
  }

  public boolean isAvvMailPromAvvAbilitato() {
    return this.avvMailPromAvvAbilitato;
  }

  public boolean getAvvMailPromAvvAbilitato() {
    return this.avvMailPromAvvAbilitato;
  }

  public void setAvvMailPromAvvAbilitato(boolean avvMailPromAvvAbilitato) {
    this.avvMailPromAvvAbilitato = avvMailPromAvvAbilitato;
  }

  public Boolean getAvvMailPromAvvPdf() {
    return this.avvMailPromAvvPdf;
  }

  public void setAvvMailPromAvvPdf(Boolean avvMailPromAvvPdf) {
    this.avvMailPromAvvPdf = avvMailPromAvvPdf;
  }

  public java.lang.String getAvvMailPromAvvTipo() {
    return this.avvMailPromAvvTipo;
  }

  public void setAvvMailPromAvvTipo(java.lang.String avvMailPromAvvTipo) {
    this.avvMailPromAvvTipo = avvMailPromAvvTipo;
  }

  public java.lang.String getAvvMailPromAvvOggetto() {
    return this.avvMailPromAvvOggetto;
  }

  public void setAvvMailPromAvvOggetto(java.lang.String avvMailPromAvvOggetto) {
    this.avvMailPromAvvOggetto = avvMailPromAvvOggetto;
  }

  public java.lang.String getAvvMailPromAvvMessaggio() {
    return this.avvMailPromAvvMessaggio;
  }

  public void setAvvMailPromAvvMessaggio(java.lang.String avvMailPromAvvMessaggio) {
    this.avvMailPromAvvMessaggio = avvMailPromAvvMessaggio;
  }

  public boolean isAvvMailPromRicAbilitato() {
    return this.avvMailPromRicAbilitato;
  }

  public boolean getAvvMailPromRicAbilitato() {
    return this.avvMailPromRicAbilitato;
  }

  public void setAvvMailPromRicAbilitato(boolean avvMailPromRicAbilitato) {
    this.avvMailPromRicAbilitato = avvMailPromRicAbilitato;
  }

  public Boolean getAvvMailPromRicPdf() {
    return this.avvMailPromRicPdf;
  }

  public void setAvvMailPromRicPdf(Boolean avvMailPromRicPdf) {
    this.avvMailPromRicPdf = avvMailPromRicPdf;
  }

  public java.lang.String getAvvMailPromRicTipo() {
    return this.avvMailPromRicTipo;
  }

  public void setAvvMailPromRicTipo(java.lang.String avvMailPromRicTipo) {
    this.avvMailPromRicTipo = avvMailPromRicTipo;
  }

  public java.lang.String getAvvMailPromRicOggetto() {
    return this.avvMailPromRicOggetto;
  }

  public void setAvvMailPromRicOggetto(java.lang.String avvMailPromRicOggetto) {
    this.avvMailPromRicOggetto = avvMailPromRicOggetto;
  }

  public java.lang.String getAvvMailPromRicMessaggio() {
    return this.avvMailPromRicMessaggio;
  }

  public void setAvvMailPromRicMessaggio(java.lang.String avvMailPromRicMessaggio) {
    this.avvMailPromRicMessaggio = avvMailPromRicMessaggio;
  }

  public Boolean getAvvMailPromRicEseguiti() {
    return this.avvMailPromRicEseguiti;
  }

  public void setAvvMailPromRicEseguiti(Boolean avvMailPromRicEseguiti) {
    this.avvMailPromRicEseguiti = avvMailPromRicEseguiti;
  }

  public boolean isAvvMailPromScadAbilitato() {
    return this.avvMailPromScadAbilitato;
  }

  public boolean getAvvMailPromScadAbilitato() {
    return this.avvMailPromScadAbilitato;
  }

  public void setAvvMailPromScadAbilitato(boolean avvMailPromScadAbilitato) {
    this.avvMailPromScadAbilitato = avvMailPromScadAbilitato;
  }

  public java.lang.Integer getAvvMailPromScadPreavviso() {
    return this.avvMailPromScadPreavviso;
  }

  public void setAvvMailPromScadPreavviso(java.lang.Integer avvMailPromScadPreavviso) {
    this.avvMailPromScadPreavviso = avvMailPromScadPreavviso;
  }

  public java.lang.String getAvvMailPromScadTipo() {
    return this.avvMailPromScadTipo;
  }

  public void setAvvMailPromScadTipo(java.lang.String avvMailPromScadTipo) {
    this.avvMailPromScadTipo = avvMailPromScadTipo;
  }

  public java.lang.String getAvvMailPromScadOggetto() {
    return this.avvMailPromScadOggetto;
  }

  public void setAvvMailPromScadOggetto(java.lang.String avvMailPromScadOggetto) {
    this.avvMailPromScadOggetto = avvMailPromScadOggetto;
  }

  public java.lang.String getAvvMailPromScadMessaggio() {
    return this.avvMailPromScadMessaggio;
  }

  public void setAvvMailPromScadMessaggio(java.lang.String avvMailPromScadMessaggio) {
    this.avvMailPromScadMessaggio = avvMailPromScadMessaggio;
  }

  public java.lang.String getVisualizzazioneDefinizione() {
    return this.visualizzazioneDefinizione;
  }

  public void setVisualizzazioneDefinizione(java.lang.String visualizzazioneDefinizione) {
    this.visualizzazioneDefinizione = visualizzazioneDefinizione;
  }

  public java.lang.String getTracCsvTipo() {
    return this.tracCsvTipo;
  }

  public void setTracCsvTipo(java.lang.String tracCsvTipo) {
    this.tracCsvTipo = tracCsvTipo;
  }

  public java.lang.String getTracCsvHeaderRisposta() {
    return this.tracCsvHeaderRisposta;
  }

  public void setTracCsvHeaderRisposta(java.lang.String tracCsvHeaderRisposta) {
    this.tracCsvHeaderRisposta = tracCsvHeaderRisposta;
  }

  public java.lang.String getTracCsvTemplateRichiesta() {
    return this.tracCsvTemplateRichiesta;
  }

  public void setTracCsvTemplateRichiesta(java.lang.String tracCsvTemplateRichiesta) {
    this.tracCsvTemplateRichiesta = tracCsvTemplateRichiesta;
  }

  public java.lang.String getTracCsvTemplateRisposta() {
    return this.tracCsvTemplateRisposta;
  }

  public void setTracCsvTemplateRisposta(java.lang.String tracCsvTemplateRisposta) {
    this.tracCsvTemplateRisposta = tracCsvTemplateRisposta;
  }

  public boolean isAvvAppIoPromAvvAbilitato() {
    return this.avvAppIoPromAvvAbilitato;
  }

  public boolean getAvvAppIoPromAvvAbilitato() {
    return this.avvAppIoPromAvvAbilitato;
  }

  public void setAvvAppIoPromAvvAbilitato(boolean avvAppIoPromAvvAbilitato) {
    this.avvAppIoPromAvvAbilitato = avvAppIoPromAvvAbilitato;
  }

  public java.lang.String getAvvAppIoPromAvvTipo() {
    return this.avvAppIoPromAvvTipo;
  }

  public void setAvvAppIoPromAvvTipo(java.lang.String avvAppIoPromAvvTipo) {
    this.avvAppIoPromAvvTipo = avvAppIoPromAvvTipo;
  }

  public java.lang.String getAvvAppIoPromAvvOggetto() {
    return this.avvAppIoPromAvvOggetto;
  }

  public void setAvvAppIoPromAvvOggetto(java.lang.String avvAppIoPromAvvOggetto) {
    this.avvAppIoPromAvvOggetto = avvAppIoPromAvvOggetto;
  }

  public java.lang.String getAvvAppIoPromAvvMessaggio() {
    return this.avvAppIoPromAvvMessaggio;
  }

  public void setAvvAppIoPromAvvMessaggio(java.lang.String avvAppIoPromAvvMessaggio) {
    this.avvAppIoPromAvvMessaggio = avvAppIoPromAvvMessaggio;
  }

  public boolean isAvvAppIoPromRicAbilitato() {
    return this.avvAppIoPromRicAbilitato;
  }

  public boolean getAvvAppIoPromRicAbilitato() {
    return this.avvAppIoPromRicAbilitato;
  }

  public void setAvvAppIoPromRicAbilitato(boolean avvAppIoPromRicAbilitato) {
    this.avvAppIoPromRicAbilitato = avvAppIoPromRicAbilitato;
  }

  public java.lang.String getAvvAppIoPromRicTipo() {
    return this.avvAppIoPromRicTipo;
  }

  public void setAvvAppIoPromRicTipo(java.lang.String avvAppIoPromRicTipo) {
    this.avvAppIoPromRicTipo = avvAppIoPromRicTipo;
  }

  public java.lang.String getAvvAppIoPromRicOggetto() {
    return this.avvAppIoPromRicOggetto;
  }

  public void setAvvAppIoPromRicOggetto(java.lang.String avvAppIoPromRicOggetto) {
    this.avvAppIoPromRicOggetto = avvAppIoPromRicOggetto;
  }

  public java.lang.String getAvvAppIoPromRicMessaggio() {
    return this.avvAppIoPromRicMessaggio;
  }

  public void setAvvAppIoPromRicMessaggio(java.lang.String avvAppIoPromRicMessaggio) {
    this.avvAppIoPromRicMessaggio = avvAppIoPromRicMessaggio;
  }

  public Boolean getAvvAppIoPromRicEseguiti() {
    return this.avvAppIoPromRicEseguiti;
  }

  public void setAvvAppIoPromRicEseguiti(Boolean avvAppIoPromRicEseguiti) {
    this.avvAppIoPromRicEseguiti = avvAppIoPromRicEseguiti;
  }

  public boolean isAvvAppIoPromScadAbilitato() {
    return this.avvAppIoPromScadAbilitato;
  }

  public boolean getAvvAppIoPromScadAbilitato() {
    return this.avvAppIoPromScadAbilitato;
  }

  public void setAvvAppIoPromScadAbilitato(boolean avvAppIoPromScadAbilitato) {
    this.avvAppIoPromScadAbilitato = avvAppIoPromScadAbilitato;
  }

  public java.lang.Integer getAvvAppIoPromScadPreavviso() {
    return this.avvAppIoPromScadPreavviso;
  }

  public void setAvvAppIoPromScadPreavviso(java.lang.Integer avvAppIoPromScadPreavviso) {
    this.avvAppIoPromScadPreavviso = avvAppIoPromScadPreavviso;
  }

  public java.lang.String getAvvAppIoPromScadTipo() {
    return this.avvAppIoPromScadTipo;
  }

  public void setAvvAppIoPromScadTipo(java.lang.String avvAppIoPromScadTipo) {
    this.avvAppIoPromScadTipo = avvAppIoPromScadTipo;
  }

  public java.lang.String getAvvAppIoPromScadOggetto() {
    return this.avvAppIoPromScadOggetto;
  }

  public void setAvvAppIoPromScadOggetto(java.lang.String avvAppIoPromScadOggetto) {
    this.avvAppIoPromScadOggetto = avvAppIoPromScadOggetto;
  }

  public java.lang.String getAvvAppIoPromScadMessaggio() {
    return this.avvAppIoPromScadMessaggio;
  }

  public void setAvvAppIoPromScadMessaggio(java.lang.String avvAppIoPromScadMessaggio) {
    this.avvAppIoPromScadMessaggio = avvAppIoPromScadMessaggio;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.TipoVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.TipoVersamento.modelStaticInstance==null){
  			it.govpay.orm.TipoVersamento.modelStaticInstance = new it.govpay.orm.model.TipoVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.TipoVersamentoModel model(){
	  if(it.govpay.orm.TipoVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.TipoVersamento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codTipoVersamento",required=true,nillable=false)
  protected java.lang.String codTipoVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizione",required=true,nillable=false)
  protected java.lang.String descrizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codificaIuv",required=false,nillable=false)
  protected java.lang.String codificaIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipo",required=true,nillable=false)
  protected java.lang.String tipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="pagaTerzi",required=true,nillable=false,defaultValue="false")
  protected boolean pagaTerzi = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="abilitato",required=true,nillable=false)
  protected boolean abilitato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boFormTipo",required=false,nillable=false)
  protected java.lang.String boFormTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boFormDefinizione",required=false,nillable=false)
  protected java.lang.String boFormDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boValidazioneDef",required=false,nillable=false)
  protected java.lang.String boValidazioneDef;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boTrasformazioneTipo",required=false,nillable=false)
  protected java.lang.String boTrasformazioneTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boTrasformazioneDef",required=false,nillable=false)
  protected java.lang.String boTrasformazioneDef;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="boCodApplicazione",required=false,nillable=false)
  protected java.lang.String boCodApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="boAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean boAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagFormTipo",required=false,nillable=false)
  protected java.lang.String pagFormTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagFormDefinizione",required=false,nillable=false)
  protected java.lang.String pagFormDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagValidazioneDef",required=false,nillable=false)
  protected java.lang.String pagValidazioneDef;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagTrasformazioneTipo",required=false,nillable=false)
  protected java.lang.String pagTrasformazioneTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagTrasformazioneDef",required=false,nillable=false)
  protected java.lang.String pagTrasformazioneDef;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagCodApplicazione",required=false,nillable=false)
  protected java.lang.String pagCodApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="pagAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean pagAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvMailPromAvvAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvMailPromAvvAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromAvvPdf",required=false,nillable=false)
  protected Boolean avvMailPromAvvPdf;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromAvvTipo",required=false,nillable=false)
  protected java.lang.String avvMailPromAvvTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromAvvOggetto",required=false,nillable=false)
  protected java.lang.String avvMailPromAvvOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromAvvMessaggio",required=false,nillable=false)
  protected java.lang.String avvMailPromAvvMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvMailPromRicAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvMailPromRicAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromRicPdf",required=false,nillable=false)
  protected Boolean avvMailPromRicPdf;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromRicTipo",required=false,nillable=false)
  protected java.lang.String avvMailPromRicTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromRicOggetto",required=false,nillable=false)
  protected java.lang.String avvMailPromRicOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromRicMessaggio",required=false,nillable=false)
  protected java.lang.String avvMailPromRicMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromRicEseguiti",required=false,nillable=false)
  protected Boolean avvMailPromRicEseguiti;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvMailPromScadAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvMailPromScadAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="avvMailPromScadPreavviso",required=false,nillable=false)
  protected java.lang.Integer avvMailPromScadPreavviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromScadTipo",required=false,nillable=false)
  protected java.lang.String avvMailPromScadTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromScadOggetto",required=false,nillable=false)
  protected java.lang.String avvMailPromScadOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvMailPromScadMessaggio",required=false,nillable=false)
  protected java.lang.String avvMailPromScadMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="visualizzazioneDefinizione",required=false,nillable=false)
  protected java.lang.String visualizzazioneDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tracCsvTipo",required=false,nillable=false)
  protected java.lang.String tracCsvTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tracCsvHeaderRisposta",required=false,nillable=false)
  protected java.lang.String tracCsvHeaderRisposta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tracCsvTemplateRichiesta",required=false,nillable=false)
  protected java.lang.String tracCsvTemplateRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tracCsvTemplateRisposta",required=false,nillable=false)
  protected java.lang.String tracCsvTemplateRisposta;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvAppIoPromAvvAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvAppIoPromAvvAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromAvvTipo",required=false,nillable=false)
  protected java.lang.String avvAppIoPromAvvTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromAvvOggetto",required=false,nillable=false)
  protected java.lang.String avvAppIoPromAvvOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromAvvMessaggio",required=false,nillable=false)
  protected java.lang.String avvAppIoPromAvvMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvAppIoPromRicAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvAppIoPromRicAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromRicTipo",required=false,nillable=false)
  protected java.lang.String avvAppIoPromRicTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromRicOggetto",required=false,nillable=false)
  protected java.lang.String avvAppIoPromRicOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromRicMessaggio",required=false,nillable=false)
  protected java.lang.String avvAppIoPromRicMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromRicEseguiti",required=false,nillable=false)
  protected Boolean avvAppIoPromRicEseguiti;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avvAppIoPromScadAbilitato",required=true,nillable=false,defaultValue="false")
  protected boolean avvAppIoPromScadAbilitato = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="avvAppIoPromScadPreavviso",required=false,nillable=false)
  protected java.lang.Integer avvAppIoPromScadPreavviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromScadTipo",required=false,nillable=false)
  protected java.lang.String avvAppIoPromScadTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromScadOggetto",required=false,nillable=false)
  protected java.lang.String avvAppIoPromScadOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avvAppIoPromScadMessaggio",required=false,nillable=false)
  protected java.lang.String avvAppIoPromScadMessaggio;

}
