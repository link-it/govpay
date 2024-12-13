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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;


/** <p>Java class for Rendicontazione complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Rendicontazione"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="esito" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="anomalie" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idFR" type="{http://www.govpay.it/orm}id-fr" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idPagamento" type="{http://www.govpay.it/orm}id-pagamento" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="0" maxOccurs="1"/&gt;
 * 		&lt;/sequence&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @version $Rev$, $Date$
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rendicontazione",
  propOrder = {
  	"iuv",
  	"iur",
  	"indiceDati",
  	"importoPagato",
  	"esito",
  	"data",
  	"stato",
  	"anomalie",
  	"idFR",
  	"idPagamento",
  	"idSingoloVersamento"
  }
)

@XmlRootElement(name = "Rendicontazione")

public class Rendicontazione extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public Rendicontazione() {
    super();
  }

  public java.lang.String getIuv() {
    return this.iuv;
  }

  public void setIuv(java.lang.String iuv) {
    this.iuv = iuv;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.math.BigInteger getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(java.math.BigInteger indiceDati) {
    this.indiceDati = indiceDati;
  }

  public java.math.BigDecimal getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(java.math.BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.math.BigInteger getEsito() {
    return this.esito;
  }

  public void setEsito(java.math.BigInteger esito) {
    this.esito = esito;
  }

  public java.util.Date getData() {
    return this.data;
  }

  public void setData(java.util.Date data) {
    this.data = data;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getAnomalie() {
    return this.anomalie;
  }

  public void setAnomalie(java.lang.String anomalie) {
    this.anomalie = anomalie;
  }

  public IdFr getIdFR() {
    return this.idFR;
  }

  public void setIdFR(IdFr idFR) {
    this.idFR = idFR;
  }

  public IdPagamento getIdPagamento() {
    return this.idPagamento;
  }

  public void setIdPagamento(IdPagamento idPagamento) {
    this.idPagamento = idPagamento;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.RendicontazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Rendicontazione.modelStaticInstance==null){
  			it.govpay.orm.Rendicontazione.modelStaticInstance = new it.govpay.orm.model.RendicontazioneModel();
	  }
  }
  public static it.govpay.orm.model.RendicontazioneModel model(){
	  if(it.govpay.orm.Rendicontazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Rendicontazione.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="indiceDati",required=false,nillable=false)
  protected java.math.BigInteger indiceDati;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoPagato",required=false,nillable=false)
  protected java.math.BigDecimal importoPagato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="esito",required=false,nillable=false)
  protected java.math.BigInteger esito;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="anomalie",required=false,nillable=false)
  protected java.lang.String anomalie;

  @XmlElement(name="idFR",required=true,nillable=false)
  protected IdFr idFR;

  @XmlElement(name="idPagamento",required=false,nillable=false)
  protected IdPagamento idPagamento;

  @XmlElement(name="idSingoloVersamento",required=false,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

}
