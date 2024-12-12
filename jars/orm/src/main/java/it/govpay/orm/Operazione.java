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
 * 			&lt;element name="idTracciato" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="tipoOperazione" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="lineaElaborazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="datiRichiesta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiRisposta" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dettaglioEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.govpay.it/orm}id-applicazione" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="trn" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idStampa" type="{http://www.govpay.it/orm}id-stampa" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="0" maxOccurs="1"/>
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
  	"tipoOperazione",
  	"lineaElaborazione",
  	"stato",
  	"datiRichiesta",
  	"datiRisposta",
  	"dettaglioEsito",
  	"idApplicazione",
  	"codVersamentoEnte",
  	"codDominio",
  	"iuv",
  	"trn",
  	"idStampa",
  	"idVersamento"
  }
)

@XmlRootElement(name = "Operazione")

public class Operazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Operazione() {
	  //donothing
  }

  public Long getId() {
    if(this.id!=null) {
		return this.id;
    } else {
		return Long.valueOf(-1);
    }
  }

  public void setId(Long id) {
    if(id!=null) {
		this.id=id;
    } else {
		this.id=Long.valueOf(-1);
    }
  }

  public IdTracciato getIdTracciato() {
    return this.idTracciato;
  }

  public void setIdTracciato(IdTracciato idTracciato) {
    this.idTracciato = idTracciato;
  }

  public java.lang.String getTipoOperazione() {
    return this.tipoOperazione;
  }

  public void setTipoOperazione(java.lang.String tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
  }

  public long getLineaElaborazione() {
    return this.lineaElaborazione;
  }

  public void setLineaElaborazione(long lineaElaborazione) {
    this.lineaElaborazione = lineaElaborazione;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
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

  public java.lang.String getDettaglioEsito() {
    return this.dettaglioEsito;
  }

  public void setDettaglioEsito(java.lang.String dettaglioEsito) {
    this.dettaglioEsito = dettaglioEsito;
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

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getTrn() {
    return this.trn;
  }

  public void setTrn(java.lang.String trn) {
    this.trn = trn;
  }

  public IdStampa getIdStampa() {
    return this.idStampa;
  }

  public void setIdStampa(IdStampa idStampa) {
    this.idStampa = idStampa;
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
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


  @XmlElement(name="idTracciato",required=true,nillable=false)
  protected IdTracciato idTracciato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoOperazione",required=true,nillable=false)
  protected java.lang.String tipoOperazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="lineaElaborazione",required=true,nillable=false)
  protected long lineaElaborazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="datiRichiesta",required=false,nillable=false)
  protected byte[] datiRichiesta;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="datiRisposta",required=false,nillable=false)
  protected byte[] datiRisposta;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="dettaglioEsito",required=false,nillable=false)
  protected java.lang.String dettaglioEsito;

  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected IdApplicazione idApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=false,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=false,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=false,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="trn",required=false,nillable=false)
  protected java.lang.String trn;

  @XmlElement(name="idStampa",required=false,nillable=false)
  protected IdStampa idStampa;

  @XmlElement(name="idVersamento",required=false,nillable=false)
  protected IdVersamento idVersamento;

}
