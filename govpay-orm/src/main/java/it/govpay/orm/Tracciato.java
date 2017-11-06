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

import it.govpay.orm.constants.StatoTracciatoType;
import it.govpay.orm.constants.TipoTracciatoType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for Tracciato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tracciato">
 * 		&lt;sequence>
 * 			&lt;element name="idOperatore" type="{http://www.govpay.it/orm}id-operatore" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataCaricamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataUltimoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}StatoTracciatoType" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="lineaElaborazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="numLineeTotali" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numOperazioniOk" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numOperazioniKo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nomeFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rawDataRichiesta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rawDataRisposta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoTracciato" type="{http://www.govpay.it/orm}TipoTracciatoType" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Tracciato", 
  propOrder = {
  	"idOperatore",
  	"idApplicazione",
  	"dataCaricamento",
  	"dataUltimoAggiornamento",
  	"stato",
  	"lineaElaborazione",
  	"descrizioneStato",
  	"numLineeTotali",
  	"numOperazioniOk",
  	"numOperazioniKo",
  	"nomeFile",
  	"rawDataRichiesta",
  	"rawDataRisposta",
  	"tipoTracciato"
  }
)

@XmlRootElement(name = "Tracciato")

public class Tracciato extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Tracciato() {
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

  public IdOperatore getIdOperatore() {
    return this.idOperatore;
  }

  public void setIdOperatore(IdOperatore idOperatore) {
    this.idOperatore = idOperatore;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.util.Date getDataCaricamento() {
    return this.dataCaricamento;
  }

  public void setDataCaricamento(java.util.Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  public java.util.Date getDataUltimoAggiornamento() {
    return this.dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(java.util.Date dataUltimoAggiornamento) {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public void set_value_stato(String value) {
    this.stato = (StatoTracciatoType) StatoTracciatoType.toEnumConstantFromString(value);
  }

  public String get_value_stato() {
    if(this.stato == null){
    	return null;
    }else{
    	return this.stato.toString();
    }
  }

  public it.govpay.orm.constants.StatoTracciatoType getStato() {
    return this.stato;
  }

  public void setStato(it.govpay.orm.constants.StatoTracciatoType stato) {
    this.stato = stato;
  }

  public long getLineaElaborazione() {
    return this.lineaElaborazione;
  }

  public void setLineaElaborazione(long lineaElaborazione) {
    this.lineaElaborazione = lineaElaborazione;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public long getNumLineeTotali() {
    return this.numLineeTotali;
  }

  public void setNumLineeTotali(long numLineeTotali) {
    this.numLineeTotali = numLineeTotali;
  }

  public long getNumOperazioniOk() {
    return this.numOperazioniOk;
  }

  public void setNumOperazioniOk(long numOperazioniOk) {
    this.numOperazioniOk = numOperazioniOk;
  }

  public long getNumOperazioniKo() {
    return this.numOperazioniKo;
  }

  public void setNumOperazioniKo(long numOperazioniKo) {
    this.numOperazioniKo = numOperazioniKo;
  }

  public java.lang.String getNomeFile() {
    return this.nomeFile;
  }

  public void setNomeFile(java.lang.String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public byte[] getRawDataRichiesta() {
    return this.rawDataRichiesta;
  }

  public void setRawDataRichiesta(byte[] rawDataRichiesta) {
    this.rawDataRichiesta = rawDataRichiesta;
  }

  public byte[] getRawDataRisposta() {
    return this.rawDataRisposta;
  }

  public void setRawDataRisposta(byte[] rawDataRisposta) {
    this.rawDataRisposta = rawDataRisposta;
  }

  public void set_value_tipoTracciato(String value) {
    this.tipoTracciato = (TipoTracciatoType) TipoTracciatoType.toEnumConstantFromString(value);
  }

  public String get_value_tipoTracciato() {
    if(this.tipoTracciato == null){
    	return null;
    }else{
    	return this.tipoTracciato.toString();
    }
  }

  public it.govpay.orm.constants.TipoTracciatoType getTipoTracciato() {
    return this.tipoTracciato;
  }

  public void setTipoTracciato(it.govpay.orm.constants.TipoTracciatoType tipoTracciato) {
    this.tipoTracciato = tipoTracciato;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.TracciatoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Tracciato.modelStaticInstance==null){
  			it.govpay.orm.Tracciato.modelStaticInstance = new it.govpay.orm.model.TracciatoModel();
	  }
  }
  public static it.govpay.orm.model.TracciatoModel model(){
	  if(it.govpay.orm.Tracciato.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Tracciato.modelStaticInstance;
  }


  @XmlElement(name="idOperatore",required=false,nillable=false)
  protected IdOperatore idOperatore;

  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCaricamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCaricamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataUltimoAggiornamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataUltimoAggiornamento;

  @XmlTransient
  protected java.lang.String _value_stato;

  @XmlElement(name="stato",required=true,nillable=false)
  protected StatoTracciatoType stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="lineaElaborazione",required=true,nillable=false)
  protected long lineaElaborazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numLineeTotali",required=true,nillable=false)
  protected long numLineeTotali;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numOperazioniOk",required=true,nillable=false)
  protected long numOperazioniOk;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numOperazioniKo",required=true,nillable=false)
  protected long numOperazioniKo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nomeFile",required=true,nillable=false)
  protected java.lang.String nomeFile;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawDataRichiesta",required=true,nillable=false)
  protected byte[] rawDataRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawDataRisposta",required=false,nillable=false)
  protected byte[] rawDataRisposta;

  @XmlTransient
  protected java.lang.String _value_tipoTracciato;

  @XmlElement(name="tipoTracciato",required=true,nillable=false)
  protected TipoTracciatoType tipoTracciato;

}
