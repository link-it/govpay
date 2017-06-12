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

import it.govpay.orm.constants.EsitoGovpayType;
import it.govpay.orm.constants.StatoOperazioneType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for Operazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Operazione">
 * 		&lt;sequence>
 * 			&lt;element name="idTracciato" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="lineaElaborazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}StatoOperazioneType" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="datiRichiesta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="datiRisposta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="esito" type="{http://www.govpay.it/orm}EsitoGovpayType" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoOperazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Operazione", 
  propOrder = {
  	"idTracciato",
  	"lineaElaborazione",
  	"stato",
  	"datiRichiesta",
  	"datiRisposta",
  	"esito",
  	"dettaglioEsito",
  	"tipoOperazione",
  	"idApplicazione",
  	"codVersamentoEnte"
  }
)

@XmlRootElement(name = "Operazione")

public class Operazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Operazione() {
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

  public IdTracciato getIdTracciato() {
    return this.idTracciato;
  }

  public void setIdTracciato(IdTracciato idTracciato) {
    this.idTracciato = idTracciato;
  }

  public long getLineaElaborazione() {
    return this.lineaElaborazione;
  }

  public void setLineaElaborazione(long lineaElaborazione) {
    this.lineaElaborazione = lineaElaborazione;
  }

  public void set_value_stato(String value) {
    this.stato = (StatoOperazioneType) StatoOperazioneType.toEnumConstantFromString(value);
  }

  public String get_value_stato() {
    if(this.stato == null){
    	return null;
    }else{
    	return this.stato.toString();
    }
  }

  public it.govpay.orm.constants.StatoOperazioneType getStato() {
    return this.stato;
  }

  public void setStato(it.govpay.orm.constants.StatoOperazioneType stato) {
    this.stato = stato;
  }

  public byte[] getDatiRichiesta() {
    return this.datiRichiesta;
  }

  public void setDatiRichiesta(byte[] datiRichiesta) {
    this.datiRichiesta = datiRichiesta;
  }

  public byte[] getDatiRisposta() {
    return this.datiRisposta;
  }

  public void setDatiRisposta(byte[] datiRisposta) {
    this.datiRisposta = datiRisposta;
  }

  public void set_value_esito(String value) {
    this.esito = (EsitoGovpayType) EsitoGovpayType.toEnumConstantFromString(value);
  }

  public String get_value_esito() {
    if(this.esito == null){
    	return null;
    }else{
    	return this.esito.toString();
    }
  }

  public it.govpay.orm.constants.EsitoGovpayType getEsito() {
    return this.esito;
  }

  public void setEsito(it.govpay.orm.constants.EsitoGovpayType esito) {
    this.esito = esito;
  }

  public java.lang.String getDettaglioEsito() {
    return this.dettaglioEsito;
  }

  public void setDettaglioEsito(java.lang.String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
  }

  public java.lang.String getTipoOperazione() {
    return this.tipoOperazione;
  }

  public void setTipoOperazione(java.lang.String tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
  }

  public IdApplicazione getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(IdApplicazione idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.OperazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Operazione.modelStaticInstance==null){
  			it.govpay.orm.Operazione.modelStaticInstance = new it.govpay.orm.model.OperazioneModel();
	  }
  }
  public static it.govpay.orm.model.OperazioneModel model(){
	  if(it.govpay.orm.Operazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Operazione.modelStaticInstance;
  }


  @XmlElement(name="idTracciato",required=false,nillable=false)
  protected IdTracciato idTracciato;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="lineaElaborazione",required=false,nillable=false)
  protected long lineaElaborazione;

  @XmlTransient
  protected java.lang.String _value_stato;

  @XmlElement(name="stato",required=true,nillable=false)
  protected StatoOperazioneType stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="datiRichiesta",required=true,nillable=false)
  protected byte[] datiRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="datiRisposta",required=true,nillable=false)
  protected byte[] datiRisposta;

  @XmlTransient
  protected java.lang.String _value_esito;

  @XmlElement(name="esito",required=true,nillable=false)
  protected EsitoGovpayType esito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioEsito",required=false,nillable=false)
  protected java.lang.String dettaglioEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoOperazione",required=true,nillable=false)
  protected java.lang.String tipoOperazione;

  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

}
