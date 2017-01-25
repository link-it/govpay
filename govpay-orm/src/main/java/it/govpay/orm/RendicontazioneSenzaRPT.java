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


/** <p>Java class for RendicontazioneSenzaRPT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RendicontazioneSenzaRPT">
 * 		&lt;sequence>
 * 			&lt;element name="idFrApplicazione" type="{http://www.govpay.it/orm}id-fr-applicazione" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idIuv" type="{http://www.govpay.it/orm}id-iuv" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="rendicontazioneData" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "RendicontazioneSenzaRPT", 
  propOrder = {
  	"idFrApplicazione",
  	"importoPagato",
  	"idIuv",
  	"iur",
  	"rendicontazioneData",
  	"idSingoloVersamento"
  }
)

@XmlRootElement(name = "RendicontazioneSenzaRPT")

public class RendicontazioneSenzaRPT extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public RendicontazioneSenzaRPT() {
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

  public IdFrApplicazione getIdFrApplicazione() {
    return this.idFrApplicazione;
  }

  public void setIdFrApplicazione(IdFrApplicazione idFrApplicazione) {
    this.idFrApplicazione = idFrApplicazione;
  }

  public double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(double importoPagato) {
    this.importoPagato = importoPagato;
  }

  public IdIuv getIdIuv() {
    return this.idIuv;
  }

  public void setIdIuv(IdIuv idIuv) {
    this.idIuv = idIuv;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.util.Date getRendicontazioneData() {
    return this.rendicontazioneData;
  }

  public void setRendicontazioneData(java.util.Date rendicontazioneData) {
    this.rendicontazioneData = rendicontazioneData;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.RendicontazioneSenzaRPTModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.RendicontazioneSenzaRPT.modelStaticInstance==null){
  			it.govpay.orm.RendicontazioneSenzaRPT.modelStaticInstance = new it.govpay.orm.model.RendicontazioneSenzaRPTModel();
	  }
  }
  public static it.govpay.orm.model.RendicontazioneSenzaRPTModel model(){
	  if(it.govpay.orm.RendicontazioneSenzaRPT.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.RendicontazioneSenzaRPT.modelStaticInstance;
  }


  @XmlElement(name="idFrApplicazione",required=true,nillable=false)
  protected IdFrApplicazione idFrApplicazione;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoPagato",required=true,nillable=false)
  protected double importoPagato;

  @XmlElement(name="idIuv",required=true,nillable=false)
  protected IdIuv idIuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="rendicontazioneData",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date rendicontazioneData;

  @XmlElement(name="idSingoloVersamento",required=false,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

}
