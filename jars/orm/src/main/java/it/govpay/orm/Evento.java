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


/** <p>Java class for Evento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Evento">
 * 		&lt;sequence>
 * 			&lt;element name="componente" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ruolo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="categoriaEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEvento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="intervallo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="esito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="sottotipoEsito" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametriRichiesta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametriRisposta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiPagoPA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="ccp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idSessione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "Evento", 
  propOrder = {
  	"componente",
  	"ruolo",
  	"categoriaEvento",
  	"tipoEvento",
  	"sottotipoEvento",
  	"data",
  	"intervallo",
  	"esito",
  	"sottotipoEsito",
  	"dettaglioEsito",
  	"parametriRichiesta",
  	"parametriRisposta",
  	"datiPagoPA",
  	"codVersamentoEnte",
  	"codApplicazione",
  	"iuv",
  	"ccp",
  	"codDominio",
  	"idSessione"
  }
)

@XmlRootElement(name = "Evento")

public class Evento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Evento() {
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

  public java.lang.String getComponente() {
    return this.componente;
  }

  public void setComponente(java.lang.String componente) {
    this.componente = componente;
  }

  public java.lang.String getRuolo() {
    return this.ruolo;
  }

  public void setRuolo(java.lang.String ruolo) {
    this.ruolo = ruolo;
  }

  public java.lang.String getCategoriaEvento() {
    return this.categoriaEvento;
  }

  public void setCategoriaEvento(java.lang.String categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
  }

  public java.lang.String getTipoEvento() {
    return this.tipoEvento;
  }

  public void setTipoEvento(java.lang.String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  public java.lang.String getSottotipoEvento() {
    return this.sottotipoEvento;
  }

  public void setSottotipoEvento(java.lang.String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
  }

  public java.util.Date getData() {
    return this.data;
  }

  public void setData(java.util.Date data) {
    this.data = data;
  }

  public long getIntervallo() {
    return this.intervallo;
  }

  public void setIntervallo(long intervallo) {
    this.intervallo = intervallo;
  }

  public java.lang.String getEsito() {
    return this.esito;
  }

  public void setEsito(java.lang.String esito) {
    this.esito = esito;
  }

  public java.lang.String getSottotipoEsito() {
    return this.sottotipoEsito;
  }

  public void setSottotipoEsito(java.lang.String sottotipoEsito) {
    this.sottotipoEsito = sottotipoEsito;
  }

  public java.lang.String getDettaglioEsito() {
    return this.dettaglioEsito;
  }

  public void setDettaglioEsito(java.lang.String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
  }

  public byte[] getParametriRichiesta() {
    return this.parametriRichiesta;
  }

  public void setParametriRichiesta(byte[] parametriRichiesta) {
    this.parametriRichiesta = parametriRichiesta;
  }

  public byte[] getParametriRisposta() {
    return this.parametriRisposta;
  }

  public void setParametriRisposta(byte[] parametriRisposta) {
    this.parametriRisposta = parametriRisposta;
  }

  public java.lang.String getDatiPagoPA() {
    return this.datiPagoPA;
  }

  public void setDatiPagoPA(java.lang.String datiPagoPA) {
    this.datiPagoPA = datiPagoPA;
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

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getCcp() {
    return this.ccp;
  }

  public void setCcp(java.lang.String ccp) {
    this.ccp = ccp;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getIdSessione() {
    return this.idSessione;
  }

  public void setIdSessione(java.lang.String idSessione) {
    this.idSessione = idSessione;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.EventoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Evento.modelStaticInstance==null){
  			it.govpay.orm.Evento.modelStaticInstance = new it.govpay.orm.model.EventoModel();
	  }
  }
  public static it.govpay.orm.model.EventoModel model(){
	  if(it.govpay.orm.Evento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Evento.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="componente",required=false,nillable=false)
  protected java.lang.String componente;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ruolo",required=false,nillable=false)
  protected java.lang.String ruolo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="categoriaEvento",required=false,nillable=false)
  protected java.lang.String categoriaEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoEvento",required=false,nillable=false)
  protected java.lang.String tipoEvento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sottotipoEvento",required=false,nillable=false)
  protected java.lang.String sottotipoEvento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="intervallo",required=false,nillable=false)
  protected long intervallo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="esito",required=false,nillable=false)
  protected java.lang.String esito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="sottotipoEsito",required=false,nillable=false)
  protected java.lang.String sottotipoEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioEsito",required=false,nillable=false)
  protected java.lang.String dettaglioEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="parametriRichiesta",required=false,nillable=false)
  protected byte[] parametriRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="parametriRisposta",required=false,nillable=false)
  protected byte[] parametriRisposta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiPagoPA",required=false,nillable=false)
  protected java.lang.String datiPagoPA;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=false,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=false,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ccp",required=false,nillable=false)
  protected java.lang.String ccp;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=false,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="idSessione",required=false,nillable=false)
  protected java.lang.String idSessione;

}
