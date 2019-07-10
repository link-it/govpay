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
 * 			&lt;element name="formTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="formDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="validazioneDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="trasformazioneTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="trasformazioneDefinizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codApplicazione" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaAvvisoPdf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="promemoriaAvvisoTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaAvvisoOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaAvvisoMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaRicevutaTipo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaRicevutaPdf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="1" maxOccurs="1" default="false"/>
 * 			&lt;element name="promemoriaRicevutaOggetto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="promemoriaRicevutaMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
  	"formTipo",
  	"formDefinizione",
  	"validazioneDefinizione",
  	"trasformazioneTipo",
  	"trasformazioneDefinizione",
  	"codApplicazione",
  	"promemoriaAvvisoPdf",
  	"promemoriaAvvisoTipo",
  	"promemoriaAvvisoOggetto",
  	"promemoriaAvvisoMessaggio",
  	"promemoriaRicevutaTipo",
  	"promemoriaRicevutaPdf",
  	"promemoriaRicevutaOggetto",
  	"promemoriaRicevutaMessaggio"
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

  public java.lang.String getFormTipo() {
    return this.formTipo;
  }

  public void setFormTipo(java.lang.String formTipo) {
    this.formTipo = formTipo;
  }

  public java.lang.String getFormDefinizione() {
    return this.formDefinizione;
  }

  public void setFormDefinizione(java.lang.String formDefinizione) {
    this.formDefinizione = formDefinizione;
  }

  public java.lang.String getValidazioneDefinizione() {
    return this.validazioneDefinizione;
  }

  public void setValidazioneDefinizione(java.lang.String validazioneDefinizione) {
    this.validazioneDefinizione = validazioneDefinizione;
  }

  public java.lang.String getTrasformazioneTipo() {
    return this.trasformazioneTipo;
  }

  public void setTrasformazioneTipo(java.lang.String trasformazioneTipo) {
    this.trasformazioneTipo = trasformazioneTipo;
  }

  public java.lang.String getTrasformazioneDefinizione() {
    return this.trasformazioneDefinizione;
  }

  public void setTrasformazioneDefinizione(java.lang.String trasformazioneDefinizione) {
    this.trasformazioneDefinizione = trasformazioneDefinizione;
  }

  public java.lang.String getCodApplicazione() {
    return this.codApplicazione;
  }

  public void setCodApplicazione(java.lang.String codApplicazione) {
    this.codApplicazione = codApplicazione;
  }

  public boolean isPromemoriaAvvisoPdf() {
    return this.promemoriaAvvisoPdf;
  }

  public boolean getPromemoriaAvvisoPdf() {
    return this.promemoriaAvvisoPdf;
  }

  public void setPromemoriaAvvisoPdf(boolean promemoriaAvvisoPdf) {
    this.promemoriaAvvisoPdf = promemoriaAvvisoPdf;
  }

  public java.lang.String getPromemoriaAvvisoTipo() {
    return this.promemoriaAvvisoTipo;
  }

  public void setPromemoriaAvvisoTipo(java.lang.String promemoriaAvvisoTipo) {
    this.promemoriaAvvisoTipo = promemoriaAvvisoTipo;
  }

  public java.lang.String getPromemoriaAvvisoOggetto() {
    return this.promemoriaAvvisoOggetto;
  }

  public void setPromemoriaAvvisoOggetto(java.lang.String promemoriaAvvisoOggetto) {
    this.promemoriaAvvisoOggetto = promemoriaAvvisoOggetto;
  }

  public java.lang.String getPromemoriaAvvisoMessaggio() {
    return this.promemoriaAvvisoMessaggio;
  }

  public void setPromemoriaAvvisoMessaggio(java.lang.String promemoriaAvvisoMessaggio) {
    this.promemoriaAvvisoMessaggio = promemoriaAvvisoMessaggio;
  }

  public java.lang.String getPromemoriaRicevutaTipo() {
    return this.promemoriaRicevutaTipo;
  }

  public void setPromemoriaRicevutaTipo(java.lang.String promemoriaRicevutaTipo) {
    this.promemoriaRicevutaTipo = promemoriaRicevutaTipo;
  }

  public boolean isPromemoriaRicevutaPdf() {
    return this.promemoriaRicevutaPdf;
  }

  public boolean getPromemoriaRicevutaPdf() {
    return this.promemoriaRicevutaPdf;
  }

  public void setPromemoriaRicevutaPdf(boolean promemoriaRicevutaPdf) {
    this.promemoriaRicevutaPdf = promemoriaRicevutaPdf;
  }

  public java.lang.String getPromemoriaRicevutaOggetto() {
    return this.promemoriaRicevutaOggetto;
  }

  public void setPromemoriaRicevutaOggetto(java.lang.String promemoriaRicevutaOggetto) {
    this.promemoriaRicevutaOggetto = promemoriaRicevutaOggetto;
  }

  public java.lang.String getPromemoriaRicevutaMessaggio() {
    return this.promemoriaRicevutaMessaggio;
  }

  public void setPromemoriaRicevutaMessaggio(java.lang.String promemoriaRicevutaMessaggio) {
    this.promemoriaRicevutaMessaggio = promemoriaRicevutaMessaggio;
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
  @XmlElement(name="formTipo",required=false,nillable=false)
  protected java.lang.String formTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="formDefinizione",required=false,nillable=false)
  protected java.lang.String formDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="validazioneDefinizione",required=false,nillable=false)
  protected java.lang.String validazioneDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="trasformazioneTipo",required=false,nillable=false)
  protected java.lang.String trasformazioneTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="trasformazioneDefinizione",required=false,nillable=false)
  protected java.lang.String trasformazioneDefinizione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codApplicazione",required=false,nillable=false)
  protected java.lang.String codApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="promemoriaAvvisoPdf",required=true,nillable=false,defaultValue="false")
  protected boolean promemoriaAvvisoPdf = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaAvvisoTipo",required=false,nillable=false)
  protected java.lang.String promemoriaAvvisoTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaAvvisoOggetto",required=false,nillable=false)
  protected java.lang.String promemoriaAvvisoOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaAvvisoMessaggio",required=false,nillable=false)
  protected java.lang.String promemoriaAvvisoMessaggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaRicevutaTipo",required=false,nillable=false)
  protected java.lang.String promemoriaRicevutaTipo;

  @javax.xml.bind.annotation.XmlSchemaType(name="boolean")
  @XmlElement(name="promemoriaRicevutaPdf",required=true,nillable=false,defaultValue="false")
  protected boolean promemoriaRicevutaPdf = false;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaRicevutaOggetto",required=false,nillable=false)
  protected java.lang.String promemoriaRicevutaOggetto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="promemoriaRicevutaMessaggio",required=false,nillable=false)
  protected java.lang.String promemoriaRicevutaMessaggio;

}
