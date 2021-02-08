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


/** <p>Java class for TracciatoMyPivot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TracciatoMyPivot">
 * 		&lt;sequence>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="nomeFile" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataCreazione" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRtDa" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRtA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataCaricamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="dataCompletamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="requestToken" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="uploadUrl" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="authorizationToken" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="rawContenuto" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="beanDati" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "TracciatoMyPivot", 
  propOrder = {
  	"idDominio",
  	"nomeFile",
  	"stato",
  	"dataCreazione",
  	"dataRtDa",
  	"dataRtA",
  	"dataCaricamento",
  	"dataCompletamento",
  	"requestToken",
  	"uploadUrl",
  	"authorizationToken",
  	"rawContenuto",
  	"beanDati"
  }
)

@XmlRootElement(name = "TracciatoMyPivot")

public class TracciatoMyPivot extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public TracciatoMyPivot() {
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

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public java.lang.String getNomeFile() {
    return this.nomeFile;
  }

  public void setNomeFile(java.lang.String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public java.lang.String getStato() {
    return this.stato;
  }

  public void setStato(java.lang.String stato) {
    this.stato = stato;
  }

  public java.util.Date getDataCreazione() {
    return this.dataCreazione;
  }

  public void setDataCreazione(java.util.Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public java.util.Date getDataRtDa() {
    return this.dataRtDa;
  }

  public void setDataRtDa(java.util.Date dataRtDa) {
    this.dataRtDa = dataRtDa;
  }

  public java.util.Date getDataRtA() {
    return this.dataRtA;
  }

  public void setDataRtA(java.util.Date dataRtA) {
    this.dataRtA = dataRtA;
  }

  public java.util.Date getDataCaricamento() {
    return this.dataCaricamento;
  }

  public void setDataCaricamento(java.util.Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  public java.util.Date getDataCompletamento() {
    return this.dataCompletamento;
  }

  public void setDataCompletamento(java.util.Date dataCompletamento) {
    this.dataCompletamento = dataCompletamento;
  }

  public java.lang.String getRequestToken() {
    return this.requestToken;
  }

  public void setRequestToken(java.lang.String requestToken) {
    this.requestToken = requestToken;
  }

  public java.lang.String getUploadUrl() {
    return this.uploadUrl;
  }

  public void setUploadUrl(java.lang.String uploadUrl) {
    this.uploadUrl = uploadUrl;
  }

  public java.lang.String getAuthorizationToken() {
    return this.authorizationToken;
  }

  public void setAuthorizationToken(java.lang.String authorizationToken) {
    this.authorizationToken = authorizationToken;
  }

  public byte[] getRawContenuto() {
    return this.rawContenuto;
  }

  public void setRawContenuto(byte[] rawContenuto) {
    this.rawContenuto = rawContenuto;
  }

  public java.lang.String getBeanDati() {
    return this.beanDati;
  }

  public void setBeanDati(java.lang.String beanDati) {
    this.beanDati = beanDati;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.TracciatoMyPivotModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.TracciatoMyPivot.modelStaticInstance==null){
  			it.govpay.orm.TracciatoMyPivot.modelStaticInstance = new it.govpay.orm.model.TracciatoMyPivotModel();
	  }
  }
  public static it.govpay.orm.model.TracciatoMyPivotModel model(){
	  if(it.govpay.orm.TracciatoMyPivot.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.TracciatoMyPivot.modelStaticInstance;
  }


  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="nomeFile",required=true,nillable=false)
  protected java.lang.String nomeFile;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCreazione",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCreazione;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRtDa",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRtDa;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRtA",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRtA;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCaricamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCaricamento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataCompletamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataCompletamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="requestToken",required=false,nillable=false)
  protected java.lang.String requestToken;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="uploadUrl",required=false,nillable=false)
  protected java.lang.String uploadUrl;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="authorizationToken",required=false,nillable=false)
  protected java.lang.String authorizationToken;

  @javax.xml.bind.annotation.XmlSchemaType(name="base64Binary")
  @XmlElement(name="rawContenuto",required=false,nillable=false)
  protected byte[] rawContenuto;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="beanDati",required=false,nillable=false)
  protected java.lang.String beanDati;

}
