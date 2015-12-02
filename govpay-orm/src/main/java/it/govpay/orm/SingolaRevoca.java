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


/** <p>Java class for SingolaRevoca complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingolaRevoca">
 * 		&lt;sequence>
 * 			&lt;element name="idRR" type="{http://www.govpay.it/orm}id-rr" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idER" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idSingoloVersamento" type="{http://www.govpay.it/orm}id-singolo-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="causaleRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="datiAggiuntiviRevoca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="singoloImporto" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="singoloImportoRevocato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="causaleEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="datiAggiuntiviEsito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "SingolaRevoca", 
  propOrder = {
  	"idRR",
  	"idER",
  	"idSingoloVersamento",
  	"causaleRevoca",
  	"datiAggiuntiviRevoca",
  	"singoloImporto",
  	"singoloImportoRevocato",
  	"causaleEsito",
  	"datiAggiuntiviEsito",
  	"stato",
  	"descrizioneStato"
  }
)

@XmlRootElement(name = "SingolaRevoca")

public class SingolaRevoca extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public SingolaRevoca() {
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

  public IdRr getIdRR() {
    return this.idRR;
  }

  public void setIdRR(IdRr idRR) {
    this.idRR = idRR;
  }

  public long getIdER() {
    return this.idER;
  }

  public void setIdER(long idER) {
    this.idER = idER;
  }

  public IdSingoloVersamento getIdSingoloVersamento() {
    return this.idSingoloVersamento;
  }

  public void setIdSingoloVersamento(IdSingoloVersamento idSingoloVersamento) {
    this.idSingoloVersamento = idSingoloVersamento;
  }

  public java.lang.String getCausaleRevoca() {
    return this.causaleRevoca;
  }

  public void setCausaleRevoca(java.lang.String causaleRevoca) {
    this.causaleRevoca = causaleRevoca;
  }

  public java.lang.String getDatiAggiuntiviRevoca() {
    return this.datiAggiuntiviRevoca;
  }

  public void setDatiAggiuntiviRevoca(java.lang.String datiAggiuntiviRevoca) {
    this.datiAggiuntiviRevoca = datiAggiuntiviRevoca;
  }

  public double getSingoloImporto() {
    return this.singoloImporto;
  }

  public void setSingoloImporto(double singoloImporto) {
    this.singoloImporto = singoloImporto;
  }

  public java.lang.Double getSingoloImportoRevocato() {
    return this.singoloImportoRevocato;
  }

  public void setSingoloImportoRevocato(java.lang.Double singoloImportoRevocato) {
    this.singoloImportoRevocato = singoloImportoRevocato;
  }

  public java.lang.String getCausaleEsito() {
    return this.causaleEsito;
  }

  public void setCausaleEsito(java.lang.String causaleEsito) {
    this.causaleEsito = causaleEsito;
  }

  public java.lang.String getDatiAggiuntiviEsito() {
    return this.datiAggiuntiviEsito;
  }

  public void setDatiAggiuntiviEsito(java.lang.String datiAggiuntiviEsito) {
    this.datiAggiuntiviEsito = datiAggiuntiviEsito;
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

  private static it.govpay.orm.model.SingolaRevocaModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.SingolaRevoca.modelStaticInstance==null){
  			it.govpay.orm.SingolaRevoca.modelStaticInstance = new it.govpay.orm.model.SingolaRevocaModel();
	  }
  }
  public static it.govpay.orm.model.SingolaRevocaModel model(){
	  if(it.govpay.orm.SingolaRevoca.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.SingolaRevoca.modelStaticInstance;
  }


  @XmlElement(name="idRR",required=true,nillable=false)
  protected IdRr idRR;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idER",required=false,nillable=false)
  protected long idER;

  @XmlElement(name="idSingoloVersamento",required=true,nillable=false)
  protected IdSingoloVersamento idSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleRevoca",required=true,nillable=false)
  protected java.lang.String causaleRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiAggiuntiviRevoca",required=true,nillable=false)
  protected java.lang.String datiAggiuntiviRevoca;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="singoloImporto",required=true,nillable=false)
  protected double singoloImporto;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="singoloImportoRevocato",required=false,nillable=false)
  protected java.lang.Double singoloImportoRevocato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="causaleEsito",required=false,nillable=false)
  protected java.lang.String causaleEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="datiAggiuntiviEsito",required=false,nillable=false)
  protected java.lang.String datiAggiuntiviEsito;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=false,nillable=false)
  protected java.lang.String descrizioneStato;

}
