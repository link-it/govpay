/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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


/** <p>Java class for FR complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FR"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="codPsp" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codFlusso" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataOraFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataAcquisizione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoTotalePagamenti" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="codBicRiversamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="xml" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idIncasso" type="{http://www.govpay.it/orm}id-incasso" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="ragioneSocialePsp" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="ragioneSocialeDominio" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="obsoleto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="dataOraPubblicazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="dataOraAggiornamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="revisione" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idRendicontazione" type="{http://www.govpay.it/orm}id-rendicontazione" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/&gt;
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
@XmlType(name = "FR",
  propOrder = {
  	"codPsp",
  	"codDominio",
  	"codFlusso",
  	"stato",
  	"descrizioneStato",
  	"iur",
  	"dataOraFlusso",
  	"dataRegolamento",
  	"dataAcquisizione",
  	"numeroPagamenti",
  	"importoTotalePagamenti",
  	"codBicRiversamento",
  	"xml",
  	"idSingoloVersamento",
  	"idIncasso",
  	"ragioneSocialePsp",
  	"ragioneSocialeDominio",
  	"obsoleto",
  	"dataOraPubblicazione",
  	"dataOraAggiornamento",
  	"revisione",
  	"idRendicontazione",
  	"idDominio"
  }
)

@XmlRootElement(name = "FR")

public class FR extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public FR() {
    super();
  }

  public java.lang.String getCodPsp() {
    return this.codPsp;
  }

  public void setCodPsp(java.lang.String codPsp) {
    this.codPsp = codPsp;
  }

  public java.lang.String getCodDominio() {
    return this.codDominio;
  }

  public void setCodDominio(java.lang.String codDominio) {
    this.codDominio = codDominio;
  }

  public java.lang.String getCodFlusso() {
    return this.codFlusso;
  }

  public void setCodFlusso(java.lang.String codFlusso) {
    this.codFlusso = codFlusso;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.lang.String getDescrizioneStato() {
    return this.descrizioneStato;
  }

  public void setDescrizioneStato(java.lang.String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.util.Date getDataOraFlusso() {
    return this.dataOraFlusso;
  }

  public void setDataOraFlusso(java.util.Date dataOraFlusso) {
    this.dataOraFlusso = dataOraFlusso;
  }

  public java.util.Date getDataRegolamento() {
    return this.dataRegolamento;
  }

  public void setDataRegolamento(java.util.Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  public java.util.Date getDataAcquisizione() {
    return this.dataAcquisizione;
  }

  public void setDataAcquisizione(java.util.Date dataAcquisizione) {
    this.dataAcquisizione = dataAcquisizione;
  }

  public long getNumeroPagamenti() {
    return this.numeroPagamenti;
  }

  public void setNumeroPagamenti(long numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  public java.math.BigDecimal getImportoTotalePagamenti() {
    return this.importoTotalePagamenti;
  }

  public void setImportoTotalePagamenti(java.math.BigDecimal importoTotalePagamenti) {
    this.importoTotalePagamenti = importoTotalePagamenti;
  }

  public java.lang.String getCodBicRiversamento() {
    return this.codBicRiversamento;
  }

  public void setCodBicRiversamento(java.lang.String codBicRiversamento) {
    this.codBicRiversamento = codBicRiversamento;
  }

  public byte[] getXml() {
    return this.xml;
  }

  public void setXml(byte[] xml) {
    this.xml = xml;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
  }

  public IdIncasso getIdIncasso() {
    return this.idIncasso;
  }

  public void setIdIncasso(IdIncasso idIncasso) {
    this.idIncasso = idIncasso;
  }

  public java.lang.String getRagioneSocialePsp() {
    return this.ragioneSocialePsp;
  }

  public void setRagioneSocialePsp(java.lang.String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
  }

  public java.lang.String getRagioneSocialeDominio() {
    return this.ragioneSocialeDominio;
  }

  public void setRagioneSocialeDominio(java.lang.String ragioneSocialeDominio) {
    this.ragioneSocialeDominio = ragioneSocialeDominio;
  }

  public Boolean getObsoleto() {
    return this.obsoleto;
  }

  public void setObsoleto(Boolean obsoleto) {
    this.obsoleto = obsoleto;
  }

  public java.util.Date getDataOraPubblicazione() {
    return this.dataOraPubblicazione;
  }

  public void setDataOraPubblicazione(java.util.Date dataOraPubblicazione) {
    this.dataOraPubblicazione = dataOraPubblicazione;
  }

  public java.util.Date getDataOraAggiornamento() {
    return this.dataOraAggiornamento;
  }

  public void setDataOraAggiornamento(java.util.Date dataOraAggiornamento) {
    this.dataOraAggiornamento = dataOraAggiornamento;
  }

  public java.math.BigInteger getRevisione() {
    return this.revisione;
  }

  public void setRevisione(java.math.BigInteger revisione) {
    this.revisione = revisione;
  }

  public IdRendicontazione getIdRendicontazione() {
    return this.idRendicontazione;
  }

  public void setIdRendicontazione(IdRendicontazione idRendicontazione) {
    this.idRendicontazione = idRendicontazione;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  private static final long serialVersionUID = 1L;

  private static it.govpay.orm.model.FRModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.FR.modelStaticInstance==null){
  			it.govpay.orm.FR.modelStaticInstance = new it.govpay.orm.model.FRModel();
	  }
  }
  public static it.govpay.orm.model.FRModel model(){
	  if(it.govpay.orm.FR.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.FR.modelStaticInstance;
  }


  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codPsp",required=true,nillable=false)
  protected java.lang.String codPsp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codFlusso",required=true,nillable=false)
  protected java.lang.String codFlusso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraFlusso",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraFlusso;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRegolamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRegolamento;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataAcquisizione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataAcquisizione;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numeroPagamenti",required=false,nillable=false)
  protected long numeroPagamenti;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoTotalePagamenti",required=false,nillable=false)
  protected java.math.BigDecimal importoTotalePagamenti;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codBicRiversamento",required=false,nillable=false)
  protected java.lang.String codBicRiversamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="xml",required=false,nillable=false)
  protected byte[] xml;

  @XmlElement(name="idSingoloVersamento",required=false,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

  @XmlElement(name="idIncasso",required=false,nillable=false)
  protected IdIncasso idIncasso;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSocialePsp",required=false,nillable=false)
  protected java.lang.String ragioneSocialePsp;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="ragioneSocialeDominio",required=false,nillable=false)
  protected java.lang.String ragioneSocialeDominio;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="obsoleto",required=true,nillable=false)
  protected Boolean obsoleto;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraPubblicazione",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraPubblicazione;

  @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @jakarta.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraAggiornamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraAggiornamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="integer")
  @XmlElement(name="revisione",required=false,nillable=false)
  protected java.math.BigInteger revisione;

  @XmlElement(name="idRendicontazione",required=false,nillable=false)
  protected IdRendicontazione idRendicontazione;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

}
