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
 * 			&lt;element name="ente_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="agid_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagopa_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="pagopa90_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="app_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="place_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importo_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="scadenza_logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_area" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_identificativo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_identificativo_split" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_cbill" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_peo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_pec" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="ente_partner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="intestatario_denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="intestatario_identificativo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="intestatario_indirizzo_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="intestatario_indirizzo_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_causale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_mav" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_importo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_scadenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_numero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_numero_split" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_iuv_split" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_barcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="avviso_qrcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
  	"enteLogo",
  	"agidLogo",
  	"pagopaLogo",
  	"pagopa90Logo",
  	"appLogo",
  	"placeLogo",
  	"importoLogo",
  	"scadenzaLogo",
  	"enteDenominazione",
  	"enteArea",
  	"enteIdentificativo",
  	"enteIdentificativoSplit",
  	"enteCbill",
  	"enteUrl",
  	"entePeo",
  	"entePec",
  	"entePartner",
  	"intestatarioDenominazione",
  	"intestatarioIdentificativo",
  	"intestatarioIndirizzo1",
  	"intestatarioIndirizzo2",
  	"avvisoCausale",
  	"avvisoMav",
  	"avvisoImporto",
  	"avvisoScadenza",
  	"avvisoNumero",
  	"avvisoNumeroSplit",
  	"avvisoIuv",
  	"avvisoIuvSplit",
  	"avvisoBarcode",
  	"avvisoQrcode"
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

  public java.lang.String getEnteLogo() {
    return this.enteLogo;
  }

  public void setEnteLogo(java.lang.String enteLogo) {
    this.enteLogo = enteLogo;
  }

  public java.lang.String getAgidLogo() {
    return this.agidLogo;
  }

  public void setAgidLogo(java.lang.String agidLogo) {
    this.agidLogo = agidLogo;
  }

  public java.lang.String getPagopaLogo() {
    return this.pagopaLogo;
  }

  public void setPagopaLogo(java.lang.String pagopaLogo) {
    this.pagopaLogo = pagopaLogo;
  }

  public java.lang.String getPagopa90Logo() {
    return this.pagopa90Logo;
  }

  public void setPagopa90Logo(java.lang.String pagopa90Logo) {
    this.pagopa90Logo = pagopa90Logo;
  }

  public java.lang.String getAppLogo() {
    return this.appLogo;
  }

  public void setAppLogo(java.lang.String appLogo) {
    this.appLogo = appLogo;
  }

  public java.lang.String getPlaceLogo() {
    return this.placeLogo;
  }

  public void setPlaceLogo(java.lang.String placeLogo) {
    this.placeLogo = placeLogo;
  }

  public java.lang.String getImportoLogo() {
    return this.importoLogo;
  }

  public void setImportoLogo(java.lang.String importoLogo) {
    this.importoLogo = importoLogo;
  }

  public java.lang.String getScadenzaLogo() {
    return this.scadenzaLogo;
  }

  public void setScadenzaLogo(java.lang.String scadenzaLogo) {
    this.scadenzaLogo = scadenzaLogo;
  }

  public java.lang.String getEnteDenominazione() {
    return this.enteDenominazione;
  }

  public void setEnteDenominazione(java.lang.String enteDenominazione) {
    this.enteDenominazione = enteDenominazione;
  }

  public java.lang.String getEnteArea() {
    return this.enteArea;
  }

  public void setEnteArea(java.lang.String enteArea) {
    this.enteArea = enteArea;
  }

  public java.lang.String getEnteIdentificativo() {
    return this.enteIdentificativo;
  }

  public void setEnteIdentificativo(java.lang.String enteIdentificativo) {
    this.enteIdentificativo = enteIdentificativo;
  }

  public java.lang.String getEnteIdentificativoSplit() {
    return this.enteIdentificativoSplit;
  }

  public void setEnteIdentificativoSplit(java.lang.String enteIdentificativoSplit) {
    this.enteIdentificativoSplit = enteIdentificativoSplit;
  }

  public java.lang.String getEnteCbill() {
    return this.enteCbill;
  }

  public void setEnteCbill(java.lang.String enteCbill) {
    this.enteCbill = enteCbill;
  }

  public java.lang.String getEnteUrl() {
    return this.enteUrl;
  }

  public void setEnteUrl(java.lang.String enteUrl) {
    this.enteUrl = enteUrl;
  }

  public java.lang.String getEntePeo() {
    return this.entePeo;
  }

  public void setEntePeo(java.lang.String entePeo) {
    this.entePeo = entePeo;
  }

  public java.lang.String getEntePec() {
    return this.entePec;
  }

  public void setEntePec(java.lang.String entePec) {
    this.entePec = entePec;
  }

  public java.lang.String getEntePartner() {
    return this.entePartner;
  }

  public void setEntePartner(java.lang.String entePartner) {
    this.entePartner = entePartner;
  }

  public java.lang.String getIntestatarioDenominazione() {
    return this.intestatarioDenominazione;
  }

  public void setIntestatarioDenominazione(java.lang.String intestatarioDenominazione) {
    this.intestatarioDenominazione = intestatarioDenominazione;
  }

  public java.lang.String getIntestatarioIdentificativo() {
    return this.intestatarioIdentificativo;
  }

  public void setIntestatarioIdentificativo(java.lang.String intestatarioIdentificativo) {
    this.intestatarioIdentificativo = intestatarioIdentificativo;
  }

  public java.lang.String getIntestatarioIndirizzo1() {
    return this.intestatarioIndirizzo1;
  }

  public void setIntestatarioIndirizzo1(java.lang.String intestatarioIndirizzo1) {
    this.intestatarioIndirizzo1 = intestatarioIndirizzo1;
  }

  public java.lang.String getIntestatarioIndirizzo2() {
    return this.intestatarioIndirizzo2;
  }

  public void setIntestatarioIndirizzo2(java.lang.String intestatarioIndirizzo2) {
    this.intestatarioIndirizzo2 = intestatarioIndirizzo2;
  }

  public java.lang.String getAvvisoCausale() {
    return this.avvisoCausale;
  }

  public void setAvvisoCausale(java.lang.String avvisoCausale) {
    this.avvisoCausale = avvisoCausale;
  }

  public boolean isAvvisoMav() {
    return this.avvisoMav;
  }

  public boolean getAvvisoMav() {
    return this.avvisoMav;
  }

  public void setAvvisoMav(boolean avvisoMav) {
    this.avvisoMav = avvisoMav;
  }

  public double getAvvisoImporto() {
    return this.avvisoImporto;
  }

  public void setAvvisoImporto(double avvisoImporto) {
    this.avvisoImporto = avvisoImporto;
  }

  public java.lang.String getAvvisoScadenza() {
    return this.avvisoScadenza;
  }

  public void setAvvisoScadenza(java.lang.String avvisoScadenza) {
    this.avvisoScadenza = avvisoScadenza;
  }

  public java.lang.String getAvvisoNumero() {
    return this.avvisoNumero;
  }

  public void setAvvisoNumero(java.lang.String avvisoNumero) {
    this.avvisoNumero = avvisoNumero;
  }

  public java.lang.String getAvvisoNumeroSplit() {
    return this.avvisoNumeroSplit;
  }

  public void setAvvisoNumeroSplit(java.lang.String avvisoNumeroSplit) {
    this.avvisoNumeroSplit = avvisoNumeroSplit;
  }

  public java.lang.String getAvvisoIuv() {
    return this.avvisoIuv;
  }

  public void setAvvisoIuv(java.lang.String avvisoIuv) {
    this.avvisoIuv = avvisoIuv;
  }

  public java.lang.String getAvvisoIuvSplit() {
    return this.avvisoIuvSplit;
  }

  public void setAvvisoIuvSplit(java.lang.String avvisoIuvSplit) {
    this.avvisoIuvSplit = avvisoIuvSplit;
  }

  public java.lang.String getAvvisoBarcode() {
    return this.avvisoBarcode;
  }

  public void setAvvisoBarcode(java.lang.String avvisoBarcode) {
    this.avvisoBarcode = avvisoBarcode;
  }

  public java.lang.String getAvvisoQrcode() {
    return this.avvisoQrcode;
  }

  public void setAvvisoQrcode(java.lang.String avvisoQrcode) {
    this.avvisoQrcode = avvisoQrcode;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;



  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_logo",required=true,nillable=false)
  protected java.lang.String enteLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="agid_logo",required=true,nillable=false)
  protected java.lang.String agidLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagopa_logo",required=true,nillable=false)
  protected java.lang.String pagopaLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="pagopa90_logo",required=true,nillable=false)
  protected java.lang.String pagopa90Logo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="app_logo",required=true,nillable=false)
  protected java.lang.String appLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="place_logo",required=true,nillable=false)
  protected java.lang.String placeLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="importo_logo",required=true,nillable=false)
  protected java.lang.String importoLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="scadenza_logo",required=true,nillable=false)
  protected java.lang.String scadenzaLogo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_denominazione",required=true,nillable=false)
  protected java.lang.String enteDenominazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_area",required=true,nillable=false)
  protected java.lang.String enteArea;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_identificativo",required=true,nillable=false)
  protected java.lang.String enteIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_identificativo_split",required=true,nillable=false)
  protected java.lang.String enteIdentificativoSplit;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_cbill",required=true,nillable=false)
  protected java.lang.String enteCbill;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_url",required=true,nillable=false)
  protected java.lang.String enteUrl;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_peo",required=true,nillable=false)
  protected java.lang.String entePeo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_pec",required=true,nillable=false)
  protected java.lang.String entePec;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ente_partner",required=true,nillable=false)
  protected java.lang.String entePartner;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="intestatario_denominazione",required=true,nillable=false)
  protected java.lang.String intestatarioDenominazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="intestatario_identificativo",required=true,nillable=false)
  protected java.lang.String intestatarioIdentificativo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="intestatario_indirizzo_1",required=true,nillable=false)
  protected java.lang.String intestatarioIndirizzo1;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="intestatario_indirizzo_2",required=true,nillable=false)
  protected java.lang.String intestatarioIndirizzo2;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_causale",required=true,nillable=false)
  protected java.lang.String avvisoCausale;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="avviso_mav",required=true,nillable=false)
  protected boolean avvisoMav;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="avviso_importo",required=true,nillable=false)
  protected double avvisoImporto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_scadenza",required=true,nillable=false)
  protected java.lang.String avvisoScadenza;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_numero",required=true,nillable=false)
  protected java.lang.String avvisoNumero;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_numero_split",required=true,nillable=false)
  protected java.lang.String avvisoNumeroSplit;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_iuv",required=true,nillable=false)
  protected java.lang.String avvisoIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_iuv_split",required=true,nillable=false)
  protected java.lang.String avvisoIuvSplit;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_barcode",required=true,nillable=false)
  protected java.lang.String avvisoBarcode;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="avviso_qrcode",required=true,nillable=false)
  protected java.lang.String avvisoQrcode;

}
