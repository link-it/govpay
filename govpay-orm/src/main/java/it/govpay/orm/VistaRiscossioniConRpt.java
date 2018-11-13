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


/** <p>Java class for VistaRiscossioniConRpt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VistaRiscossioniConRpt">
 * 		&lt;sequence>
 * 			&lt;element name="codDominio" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iuv" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="cod_flusso" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="fr_iur" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRegolamento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="numeroPagamenti" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoTotalePagamenti" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="importoPagato" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="codVersamentoEnte" type="{http://www.govpay.it/orm}string" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "VistaRiscossioniConRpt", 
  propOrder = {
  	"codDominio",
  	"iuv",
  	"iur",
  	"codFlusso",
  	"frIur",
  	"dataRegolamento",
  	"numeroPagamenti",
  	"importoTotalePagamenti",
  	"importoPagato",
  	"data",
  	"codSingoloVersamentoEnte",
  	"indiceDati",
  	"codVersamentoEnte",
  	"idApplicazione"
  }
)

@XmlRootElement(name = "VistaRiscossioniConRpt")

public class VistaRiscossioniConRpt extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public VistaRiscossioniConRpt() {
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

  public java.lang.String getIur() {
    return this.iur;
  }

  public void setIur(java.lang.String iur) {
    this.iur = iur;
  }

  public java.lang.String getCodFlusso() {
    return this.codFlusso;
  }

  public void setCodFlusso(java.lang.String codFlusso) {
    this.codFlusso = codFlusso;
  }

  public java.lang.String getFrIur() {
    return this.frIur;
  }

  public void setFrIur(java.lang.String frIur) {
    this.frIur = frIur;
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

  public java.lang.Double getImportoTotalePagamenti() {
    return this.importoTotalePagamenti;
  }

  public void setImportoTotalePagamenti(java.lang.Double importoTotalePagamenti) {
    this.importoTotalePagamenti = importoTotalePagamenti;
  }

  public java.lang.Double getImportoPagato() {
    return this.importoPagato;
  }

  public void setImportoPagato(java.lang.Double importoPagato) {
    this.importoPagato = importoPagato;
  }

  public java.util.Date getData() {
    return this.data;
  }

  public void setData(java.util.Date data) {
    this.data = data;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public java.lang.Integer getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(java.lang.Integer indiceDati) {
    this.indiceDati = indiceDati;
  }

  public java.lang.String getCodVersamentoEnte() {
    return this.codVersamentoEnte;
  }

  public void setCodVersamentoEnte(java.lang.String codVersamentoEnte) {
    this.codVersamentoEnte = codVersamentoEnte;
  }

  public long getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(long idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.VistaRiscossioniConRptModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.VistaRiscossioniConRpt.modelStaticInstance==null){
  			it.govpay.orm.VistaRiscossioniConRpt.modelStaticInstance = new it.govpay.orm.model.VistaRiscossioniConRptModel();
	  }
  }
  public static it.govpay.orm.model.VistaRiscossioniConRptModel model(){
	  if(it.govpay.orm.VistaRiscossioniConRpt.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.VistaRiscossioniConRpt.modelStaticInstance;
  }


  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codDominio",required=true,nillable=false)
  protected java.lang.String codDominio;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iuv",required=true,nillable=false)
  protected java.lang.String iuv;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="iur",required=true,nillable=false)
  protected java.lang.String iur;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="cod_flusso",required=true,nillable=false)
  protected java.lang.String codFlusso;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="fr_iur",required=true,nillable=false)
  protected java.lang.String frIur;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="dataRegolamento",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRegolamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="numeroPagamenti",required=false,nillable=false)
  protected long numeroPagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoTotalePagamenti",required=false,nillable=false)
  protected java.lang.Double importoTotalePagamenti;

  @javax.xml.bind.annotation.XmlSchemaType(name="decimal")
  @XmlElement(name="importoPagato",required=false,nillable=false)
  protected java.lang.Double importoPagato;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.DateTime2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="dateTime")
  @XmlElement(name="data",required=false,nillable=false,type=java.lang.String.class)
  protected java.util.Date data;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected java.lang.Integer indiceDati;

  @javax.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codVersamentoEnte;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idApplicazione",required=true,nillable=false)
  protected long idApplicazione;

}
