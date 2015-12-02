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


/** <p>Java class for RT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RT">
 * 		&lt;sequence>
 * 			&lt;element name="idRPT" type="{http://www.govpay.it/orm}id-rpt" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idTracciato" type="{http://www.govpay.it/orm}id-tracciato" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codMsgRicevuta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataOraMsgRicevuta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idAnagraficaAttestante" type="{http://www.govpay.it/orm}id-anagrafica" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codEsitoPagamento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagato" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="stato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="descrizioneStato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "RT", 
  propOrder = {
  	"idRPT",
  	"idTracciato",
  	"codMsgRicevuta",
  	"dataOraMsgRicevuta",
  	"idAnagraficaAttestante",
  	"codEsitoPagamento",
  	"importoTotalePagato",
  	"stato",
  	"descrizioneStato"
  }
)

@XmlRootElement(name = "RT")

public class RT extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public RT() {
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

  public IdRpt getIdRPT() {
    return this.idRPT;
  }

  public void setIdRPT(IdRpt idRPT) {
    this.idRPT = idRPT;
  }

  public IdTracciato getIdTracciato() {
    return this.idTracciato;
  }

  public void setIdTracciato(IdTracciato idTracciato) {
    this.idTracciato = idTracciato;
  }

  public java.lang.String getCodMsgRicevuta() {
    return this.codMsgRicevuta;
  }

  public void setCodMsgRicevuta(java.lang.String codMsgRicevuta) {
    this.codMsgRicevuta = codMsgRicevuta;
  }

  public java.util.Date getDataOraMsgRicevuta() {
    return this.dataOraMsgRicevuta;
  }

  public void setDataOraMsgRicevuta(java.util.Date dataOraMsgRicevuta) {
    this.dataOraMsgRicevuta = dataOraMsgRicevuta;
  }

  public IdAnagrafica getIdAnagraficaAttestante() {
    return this.idAnagraficaAttestante;
  }

  public void setIdAnagraficaAttestante(IdAnagrafica idAnagraficaAttestante) {
    this.idAnagraficaAttestante = idAnagraficaAttestante;
  }

  public int getCodEsitoPagamento() {
    return this.codEsitoPagamento;
  }

  public void setCodEsitoPagamento(int codEsitoPagamento) {
    this.codEsitoPagamento = codEsitoPagamento;
  }

  public double getImportoTotalePagato() {
    return this.importoTotalePagato;
  }

  public void setImportoTotalePagato(double importoTotalePagato) {
    this.importoTotalePagato = importoTotalePagato;
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

  private static it.govpay.orm.model.RTModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.RT.modelStaticInstance==null){
  			it.govpay.orm.RT.modelStaticInstance = new it.govpay.orm.model.RTModel();
	  }
  }
  public static it.govpay.orm.model.RTModel model(){
	  if(it.govpay.orm.RT.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.RT.modelStaticInstance;
  }


  @XmlElement(name="idRPT",required=true,nillable=false)
  protected IdRpt idRPT;

  @XmlElement(name="idTracciato",required=true,nillable=false)
  protected IdTracciato idTracciato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codMsgRicevuta",required=true,nillable=false)
  protected java.lang.String codMsgRicevuta;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataOraMsgRicevuta",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataOraMsgRicevuta;

  @XmlElement(name="idAnagraficaAttestante",required=true,nillable=false)
  protected IdAnagrafica idAnagraficaAttestante;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="codEsitoPagamento",required=true,nillable=false)
  protected int codEsitoPagamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoTotalePagato",required=true,nillable=false)
  protected double importoTotalePagato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="stato",required=true,nillable=false)
  protected java.lang.String stato;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="descrizioneStato",required=true,nillable=false)
  protected java.lang.String descrizioneStato;

}
