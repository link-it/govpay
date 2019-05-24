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
 * 			&lt;element name="sottotipoEsito" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametriRichiesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="parametriRisposta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiControparte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idPagamentoPortale" type="{http://www.govpay.it/orm}id-pagamento-portale" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idRpt" type="{http://www.govpay.it/orm}id-rpt" minOccurs="0" maxOccurs="1"/>
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
  	"datiControparte",
  	"idVersamento",
  	"idPagamentoPortale",
  	"idRpt"
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

  public java.lang.Integer getSottotipoEsito() {
    return this.sottotipoEsito;
  }

  public void setSottotipoEsito(java.lang.Integer sottotipoEsito) {
    this.sottotipoEsito = sottotipoEsito;
  }

  public java.lang.String getDettaglioEsito() {
    return this.dettaglioEsito;
  }

  public void setDettaglioEsito(java.lang.String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
  }

  public java.lang.String getParametriRichiesta() {
    return this.parametriRichiesta;
  }

  public void setParametriRichiesta(java.lang.String parametriRichiesta) {
    this.parametriRichiesta = parametriRichiesta;
  }

  public java.lang.String getParametriRisposta() {
    return this.parametriRisposta;
  }

  public void setParametriRisposta(java.lang.String parametriRisposta) {
    this.parametriRisposta = parametriRisposta;
  }

  public java.lang.String getDatiControparte() {
    return this.datiControparte;
  }

  public void setDatiControparte(java.lang.String datiControparte) {
    this.datiControparte = datiControparte;
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  public IdPagamentoPortale getIdPagamentoPortale() {
    return this.idPagamentoPortale;
  }

  public void setIdPagamentoPortale(IdPagamentoPortale idPagamentoPortale) {
    this.idPagamentoPortale = idPagamentoPortale;
  }

  public IdRpt getIdRpt() {
    return this.idRpt;
  }

  public void setIdRpt(IdRpt idRpt) {
    this.idRpt = idRpt;
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

  @javax.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="sottotipoEsito",required=false,nillable=false)
  protected java.lang.Integer sottotipoEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioEsito",required=false,nillable=false)
  protected java.lang.String dettaglioEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="parametriRichiesta",required=false,nillable=false)
  protected java.lang.String parametriRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="parametriRisposta",required=false,nillable=false)
  protected java.lang.String parametriRisposta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiControparte",required=false,nillable=false)
  protected java.lang.String datiControparte;

  @XmlElement(name="idVersamento",required=false,nillable=false)
  protected IdVersamento idVersamento;

  @XmlElement(name="idPagamentoPortale",required=false,nillable=false)
  protected IdPagamentoPortale idPagamentoPortale;

  @XmlElement(name="idRpt",required=false,nillable=false)
  protected IdRpt idRpt;

}
