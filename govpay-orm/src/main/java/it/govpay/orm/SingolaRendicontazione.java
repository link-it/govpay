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


/** <p>Java class for SingolaRendicontazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingolaRendicontazione">
 * 		&lt;sequence>
 * 			&lt;element name="idFr" type="{http://www.govpay.it/orm}id-fr" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="singoloImporto" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codiceEsito" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataEsito" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "SingolaRendicontazione", 
  propOrder = {
  	"idFr",
  	"idSingoloVersamento",
  	"iuv",
  	"iur",
  	"singoloImporto",
  	"codiceEsito",
  	"dataEsito"
  }
)

@XmlRootElement(name = "SingolaRendicontazione")

public class SingolaRendicontazione extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public SingolaRendicontazione() {
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

  public IdFr getIdFr() {
    return this.idFr;
  }

  public void setIdFr(IdFr idFr) {
    this.idFr = idFr;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
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

  public double getSingoloImporto() {
    return this.singoloImporto;
  }

  public void setSingoloImporto(double singoloImporto) {
    this.singoloImporto = singoloImporto;
  }

  public int getCodiceEsito() {
    return this.codiceEsito;
  }

  public void setCodiceEsito(int codiceEsito) {
    this.codiceEsito = codiceEsito;
  }

  public java.util.Date getDataEsito() {
    return this.dataEsito;
  }

  public void setDataEsito(java.util.Date dataEsito) {
    this.dataEsito = dataEsito;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.SingolaRendicontazioneModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.SingolaRendicontazione.modelStaticInstance==null){
  			it.govpay.orm.SingolaRendicontazione.modelStaticInstance = new it.govpay.orm.model.SingolaRendicontazioneModel();
	  }
  }
  public static it.govpay.orm.model.SingolaRendicontazioneModel model(){
	  if(it.govpay.orm.SingolaRendicontazione.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.SingolaRendicontazione.modelStaticInstance;
  }


  @XmlElement(name="idFr",required=true,nillable=false)
  protected IdFr idFr;

  @XmlElement(name="idSingoloVersamento",required=false,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="singoloImporto",required=true,nillable=false)
  protected double singoloImporto;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="codiceEsito",required=true,nillable=false)
  protected int codiceEsito;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataEsito",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataEsito;

}
