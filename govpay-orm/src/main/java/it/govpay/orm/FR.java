/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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


/** <p>Java class for FR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FR">
 * 		&lt;sequence>
 * 			&lt;element name="idTracciatoXML" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idPsp" type="{http://www.govpay.it/orm}id-psp" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idDominio" type="{http://www.govpay.it/orm}id-dominio" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codFlusso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraFlusso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagamenti" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "FR", 
  propOrder = {
  	"idTracciatoXML",
  	"idPsp",
  	"idDominio",
  	"codFlusso",
  	"annoRiferimento",
  	"dataOraFlusso",
  	"iur",
  	"dataRegolamento",
  	"numeroPagamenti",
  	"importoTotalePagamenti",
  	"stato",
  	"descrizioneStato"
  }
)

@XmlRootElement(name = "FR")

public class FR extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public FR() {
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

  public IdTracciato getIdTracciatoXML() {
    return this.idTracciatoXML;
  }

  public void setIdTracciatoXML(IdTracciato idTracciatoXML) {
    this.idTracciatoXML = idTracciatoXML;
  }

  public IdPsp getIdPsp() {
    return this.idPsp;
  }

  public void setIdPsp(IdPsp idPsp) {
    this.idPsp = idPsp;
  }

  public IdDominio getIdDominio() {
    return this.idDominio;
  }

  public void setIdDominio(IdDominio idDominio) {
    this.idDominio = idDominio;
  }

  public java.lang.String getCodFlusso() {
    return this.codFlusso;
  }

  public void setCodFlusso(java.lang.String codFlusso) {
    this.codFlusso = codFlusso;
  }

  public int getAnnoRiferimento() {
    return this.annoRiferimento;
  }

  public void setAnnoRiferimento(int annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public java.util.Date getDataOraFlusso() {
    return this.dataOraFlusso;
  }

  public void setDataOraFlusso(java.util.Date dataOraFlusso) {
    this.dataOraFlusso = dataOraFlusso;
  }

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.util.Date getDataRegolamento() {
    return this.dataRegolamento;
  }

  public void setDataRegolamento(java.util.Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  public long getNumeroPagamenti() {
    return this.numeroPagamenti;
  }

  public void setNumeroPagamenti(long numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  public double getImportoTotalePagamenti() {
    return this.importoTotalePagamenti;
  }

  public void setImportoTotalePagamenti(double importoTotalePagamenti) {
    this.importoTotalePagamenti = importoTotalePagamenti;
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

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

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


  @XmlElement(name="idTracciatoXML",required=true,nillable=false)
  protected IdTracciato idTracciatoXML;

  @XmlElement(name="idPsp",required=true,nillable=false)
  protected IdPsp idPsp;

  @XmlElement(name="idDominio",required=true,nillable=false)
  protected IdDominio idDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codFlusso",required=true,nillable=false)
  protected java.lang.String codFlusso;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="annoRiferimento",required=true,nillable=false)
  protected int annoRiferimento;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraFlusso",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraFlusso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataRegolamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRegolamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numeroPagamenti",required=true,nillable=false)
  protected long numeroPagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotalePagamenti",required=true,nillable=false)
  protected double importoTotalePagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

}
