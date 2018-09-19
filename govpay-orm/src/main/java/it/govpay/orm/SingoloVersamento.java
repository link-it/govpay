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


/** <p>Java class for SingoloVersamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingoloVersamento">
 * 		&lt;sequence>
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idTributo" type="{http://www.govpay.it/orm}id-tributo" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="statoSingoloVersamento" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="importoSingoloVersamento" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoBollo" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="hashDocumento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="provinciaResidenza" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idIbanAccredito" type="{http://www.govpay.it/orm}id-iban-accredito" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idIbanAppoggio" type="{http://www.govpay.it/orm}id-iban-accredito" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="tipoContabilita" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codiceContabilita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="note" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/>
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
@XmlType(name = "SingoloVersamento", 
  propOrder = {
  	"idVersamento",
  	"idTributo",
  	"codSingoloVersamentoEnte",
  	"statoSingoloVersamento",
  	"importoSingoloVersamento",
  	"annoRiferimento",
  	"tipoBollo",
  	"hashDocumento",
  	"provinciaResidenza",
  	"idIbanAccredito",
  	"idIbanAppoggio",
  	"tipoContabilita",
  	"codiceContabilita",
  	"note"
  }
)

@XmlRootElement(name = "SingoloVersamento")

public class SingoloVersamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public SingoloVersamento() {
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

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  public IdTributo getIdTributo() {
    return this.idTributo;
  }

  public void setIdTributo(IdTributo idTributo) {
    this.idTributo = idTributo;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public java.lang.String getStatoSingoloVersamento() {
    return this.statoSingoloVersamento;
  }

  public void setStatoSingoloVersamento(java.lang.String statoSingoloVersamento) {
    this.statoSingoloVersamento = statoSingoloVersamento;
  }

  public double getImportoSingoloVersamento() {
    return this.importoSingoloVersamento;
  }

  public void setImportoSingoloVersamento(double importoSingoloVersamento) {
    this.importoSingoloVersamento = importoSingoloVersamento;
  }

  public int getAnnoRiferimento() {
    return this.annoRiferimento;
  }

  public void setAnnoRiferimento(int annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public java.lang.String getTipoBollo() {
    return this.tipoBollo;
  }

  public void setTipoBollo(java.lang.String tipoBollo) {
    this.tipoBollo = tipoBollo;
  }

  public java.lang.String getHashDocumento() {
    return this.hashDocumento;
  }

  public void setHashDocumento(java.lang.String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public java.lang.String getProvinciaResidenza() {
    return this.provinciaResidenza;
  }

  public void setProvinciaResidenza(java.lang.String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public IdIbanAccredito getIdIbanAccredito() {
    return this.idIbanAccredito;
  }

  public void setIdIbanAccredito(IdIbanAccredito idIbanAccredito) {
    this.idIbanAccredito = idIbanAccredito;
  }
  
  public IdIbanAccredito getIdIbanAppoggio() {
    return this.idIbanAppoggio;
  }

  public void setIdIbanAppoggio(IdIbanAccredito idIbanAppoggio) {
    this.idIbanAppoggio = idIbanAppoggio;
  }

  public java.lang.String getTipoContabilita() {
    return this.tipoContabilita;
  }

  public void setTipoContabilita(java.lang.String tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public java.lang.String getCodiceContabilita() {
    return this.codiceContabilita;
  }

  public void setCodiceContabilita(java.lang.String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public java.lang.String getNote() {
    return this.note;
  }

  public void setNote(java.lang.String note) {
    this.note = note;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.SingoloVersamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.SingoloVersamento.modelStaticInstance==null){
  			it.govpay.orm.SingoloVersamento.modelStaticInstance = new it.govpay.orm.model.SingoloVersamentoModel();
	  }
  }
  public static it.govpay.orm.model.SingoloVersamentoModel model(){
	  if(it.govpay.orm.SingoloVersamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.SingoloVersamento.modelStaticInstance;
  }


  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @XmlElement(name="idTributo",required=false,nillable=false)
  protected IdTributo idTributo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoSingoloVersamento",required=true,nillable=false)
  protected java.lang.String statoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoSingoloVersamento",required=true,nillable=false)
  protected double importoSingoloVersamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="int")
  @XmlElement(name="annoRiferimento",required=false,nillable=false)
  protected int annoRiferimento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoBollo",required=false,nillable=false)
  protected java.lang.String tipoBollo;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="hashDocumento",required=false,nillable=false)
  protected java.lang.String hashDocumento;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="provinciaResidenza",required=false,nillable=false)
  protected java.lang.String provinciaResidenza;

  @XmlElement(name="idIbanAccredito",required=false,nillable=false)
  protected IdIbanAccredito idIbanAccredito;
  
  @XmlElement(name="idIbanAppoggio",required=false,nillable=false)
  protected IdIbanAccredito idIbanAppoggio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="tipoContabilita",required=false,nillable=false)
  protected java.lang.String tipoContabilita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codiceContabilita",required=false,nillable=false)
  protected java.lang.String codiceContabilita;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="note",required=false,nillable=false)
  protected java.lang.String note;

}
