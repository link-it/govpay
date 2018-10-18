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
package it.govpay.model.avvisi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/** <p>Java class for AvvisoPagamentoInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AvvisoPagamentoInput">
 * 		&lt;sequence>
 * 			&lt;element name="logo_ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="logo_pagopa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="logo_app" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="logo_place" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="logo_posta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="logo_scissors" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="oggetto_del_pagamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cf_ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cf_destinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nome_cognome_destinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_creditore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="settore_ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indirizzo_destinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="info_ente" type="{http://www.govpay.it/orm}InfoEnte" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="del_tuo_ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="di_poste" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="qr_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cbill" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codice_avviso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numero_cc_postale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="intestatario_conto_corrente_postale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="autorizzazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="data_matrix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "AvvisoPagamentoInput", 
  propOrder = {
  	"logoEnte",
  	"logoPagopa",
  	"logoApp",
  	"logoPlace",
  	"logoPosta",
  	"logoScissors",
  	"oggettoDelPagamento",
  	"cfEnte",
  	"cfDestinatario",
  	"nomeCognomeDestinatario",
  	"enteCreditore",
  	"settoreEnte",
  	"indirizzoDestinatario",
  	"infoEnte",
  	"delTuoEnte",
  	"diPoste",
  	"importo",
  	"data",
  	"qrCode",
  	"cbill",
  	"codiceAvviso",
  	"numeroCcPostale",
  	"intestatarioContoCorrentePostale",
  	"autorizzazione",
  	"dataMatrix"
  }
, namespace=""
)

@XmlRootElement(name = "input", namespace="")

public class AvvisoPagamentoInput extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
	
	
  public AvvisoPagamentoInput() {
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

  public java.lang.String getLogoEnte() {
    return this.logoEnte;
  }

  public void setLogoEnte(java.lang.String logoEnte) {
    this.logoEnte = logoEnte;
  }

  public java.lang.String getLogoPagopa() {
    return this.logoPagopa;
  }

  public void setLogoPagopa(java.lang.String logoPagopa) {
    this.logoPagopa = logoPagopa;
  }

  public java.lang.String getLogoApp() {
    return this.logoApp;
  }

  public void setLogoApp(java.lang.String logoApp) {
    this.logoApp = logoApp;
  }

  public java.lang.String getLogoPlace() {
    return this.logoPlace;
  }

  public void setLogoPlace(java.lang.String logoPlace) {
    this.logoPlace = logoPlace;
  }

  public java.lang.String getLogoPosta() {
    return this.logoPosta;
  }

  public void setLogoPosta(java.lang.String logoPosta) {
    this.logoPosta = logoPosta;
  }

  public java.lang.String getLogoScissors() {
    return this.logoScissors;
  }

  public void setLogoScissors(java.lang.String logoScissors) {
    this.logoScissors = logoScissors;
  }

  public java.lang.String getOggettoDelPagamento() {
    return this.oggettoDelPagamento;
  }

  public void setOggettoDelPagamento(java.lang.String oggettoDelPagamento) {
    this.oggettoDelPagamento = oggettoDelPagamento;
  }

  public java.lang.String getCfEnte() {
    return this.cfEnte;
  }

  public void setCfEnte(java.lang.String cfEnte) {
    this.cfEnte = cfEnte;
  }

  public java.lang.String getCfDestinatario() {
    return this.cfDestinatario;
  }

  public void setCfDestinatario(java.lang.String cfDestinatario) {
    this.cfDestinatario = cfDestinatario;
  }

  public java.lang.String getNomeCognomeDestinatario() {
    return this.nomeCognomeDestinatario;
  }

  public void setNomeCognomeDestinatario(java.lang.String nomeCognomeDestinatario) {
    this.nomeCognomeDestinatario = nomeCognomeDestinatario;
  }

  public java.lang.String getEnteCreditore() {
    return this.enteCreditore;
  }

  public void setEnteCreditore(java.lang.String enteCreditore) {
    this.enteCreditore = enteCreditore;
  }

  public java.lang.String getSettoreEnte() {
    return this.settoreEnte;
  }

  public void setSettoreEnte(java.lang.String settoreEnte) {
    this.settoreEnte = settoreEnte;
  }

  public java.lang.String getIndirizzoDestinatario() {
    return this.indirizzoDestinatario;
  }

  public void setIndirizzoDestinatario(java.lang.String indirizzoDestinatario) {
    this.indirizzoDestinatario = indirizzoDestinatario;
  }

  public InfoEnte getInfoEnte() {
    return this.infoEnte;
  }

  public void setInfoEnte(InfoEnte infoEnte) {
    this.infoEnte = infoEnte;
  }

  public java.lang.String getDelTuoEnte() {
    return this.delTuoEnte;
  }

  public void setDelTuoEnte(java.lang.String delTuoEnte) {
    this.delTuoEnte = delTuoEnte;
  }

  public java.lang.String getDiPoste() {
    return this.diPoste;
  }

  public void setDiPoste(java.lang.String diPoste) {
    this.diPoste = diPoste;
  }

  public double getImporto() {
    return this.importo;
  }

  public void setImporto(double importo) {
    this.importo = importo;
  }

  public java.lang.String getData() {
    return this.data;
  }

  public void setData(java.lang.String data) {
    this.data = data;
  }

  public java.lang.String getQrCode() {
    return this.qrCode;
  }

  public void setQrCode(java.lang.String qrCode) {
    this.qrCode = qrCode;
  }

  public java.lang.String getCbill() {
    return this.cbill;
  }

  public void setCbill(java.lang.String cbill) {
    this.cbill = cbill;
  }

  public java.lang.String getCodiceAvviso() {
    return this.codiceAvviso;
  }

  public void setCodiceAvviso(java.lang.String codiceAvviso) {
    this.codiceAvviso = codiceAvviso;
  }

  public java.lang.String getNumeroCcPostale() {
    return this.numeroCcPostale;
  }

  public void setNumeroCcPostale(java.lang.String numeroCcPostale) {
    this.numeroCcPostale = numeroCcPostale;
  }

  public java.lang.String getIntestatarioContoCorrentePostale() {
    return this.intestatarioContoCorrentePostale;
  }

  public void setIntestatarioContoCorrentePostale(java.lang.String intestatarioContoCorrentePostale) {
    this.intestatarioContoCorrentePostale = intestatarioContoCorrentePostale;
  }

  public java.lang.String getAutorizzazione() {
    return this.autorizzazione;
  }

  public void setAutorizzazione(java.lang.String autorizzazione) {
    this.autorizzazione = autorizzazione;
  }

  public java.lang.String getDataMatrix() {
    return this.dataMatrix;
  }

  public void setDataMatrix(java.lang.String dataMatrix) {
    this.dataMatrix = dataMatrix;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_ente",required=true,nillable=false)
  protected java.lang.String logoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_pagopa",required=true,nillable=false)
  protected java.lang.String logoPagopa;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_app",required=true,nillable=false)
  protected java.lang.String logoApp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_place",required=true,nillable=false)
  protected java.lang.String logoPlace;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_posta",required=true,nillable=false)
  protected java.lang.String logoPosta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="logo_scissors",required=true,nillable=false)
  protected java.lang.String logoScissors;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="oggetto_del_pagamento",required=true,nillable=false)
  protected java.lang.String oggettoDelPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cf_ente",required=true,nillable=false)
  protected java.lang.String cfEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cf_destinatario",required=true,nillable=false)
  protected java.lang.String cfDestinatario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nome_cognome_destinatario",required=true,nillable=false)
  protected java.lang.String nomeCognomeDestinatario;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_creditore",required=true,nillable=false)
  protected java.lang.String enteCreditore;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="settore_ente",required=true,nillable=false)
  protected java.lang.String settoreEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="indirizzo_destinatario",required=true,nillable=false)
  protected java.lang.String indirizzoDestinatario;

  @XmlElement(name="info_ente",required=true,nillable=false)
  protected InfoEnte infoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="del_tuo_ente",required=false,nillable=false)
  protected java.lang.String delTuoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="di_poste",required=false,nillable=false)
  protected java.lang.String diPoste;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importo",required=true,nillable=false)
  protected double importo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="data",required=true,nillable=false)
  protected java.lang.String data;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="qr_code",required=true,nillable=false)
  protected java.lang.String qrCode;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cbill",required=true,nillable=false)
  protected java.lang.String cbill;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codice_avviso",required=true,nillable=false)
  protected java.lang.String codiceAvviso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="numero_cc_postale",required=true,nillable=false)
  protected java.lang.String numeroCcPostale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="intestatario_conto_corrente_postale",required=true,nillable=false)
  protected java.lang.String intestatarioContoCorrentePostale;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="autorizzazione",required=true,nillable=false)
  protected java.lang.String autorizzazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="data_matrix",required=true,nillable=false)
  protected java.lang.String dataMatrix;

}
