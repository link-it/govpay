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


/** <p>Java class for Rilevamento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rilevamento">
 * 		&lt;sequence>
 * 			&lt;element name="idSLA" type="{http://www.govpay.it/orm}id-sla" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idApplicazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0" maxOccurs="1"/>
 * 			&lt;element name="idEventoIniziale" type="{http://www.govpay.it/orm}id-evento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="idEventoFinale" type="{http://www.govpay.it/orm}id-evento" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="dataRilevamento" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="1" maxOccurs="1"/>
 * 			&lt;element name="durata" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="1" maxOccurs="1"/>
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
@XmlType(name = "Rilevamento", 
  propOrder = {
  	"idSLA",
  	"idApplicazione",
  	"idEventoIniziale",
  	"idEventoFinale",
  	"dataRilevamento",
  	"durata"
  }
)

@XmlRootElement(name = "Rilevamento")

public class Rilevamento extends org.openspcoop2.utils.beans.BaseBean implements Serializable , Cloneable {
  public Rilevamento() {
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

  public IdSla getIdSLA() {
    return this.idSLA;
  }

  public void setIdSLA(IdSla idSLA) {
    this.idSLA = idSLA;
  }

  public long getIdApplicazione() {
    return this.idApplicazione;
  }

  public void setIdApplicazione(long idApplicazione) {
    this.idApplicazione = idApplicazione;
  }

  public IdEvento getIdEventoIniziale() {
    return this.idEventoIniziale;
  }

  public void setIdEventoIniziale(IdEvento idEventoIniziale) {
    this.idEventoIniziale = idEventoIniziale;
  }

  public IdEvento getIdEventoFinale() {
    return this.idEventoFinale;
  }

  public void setIdEventoFinale(IdEvento idEventoFinale) {
    this.idEventoFinale = idEventoFinale;
  }

  public java.util.Date getDataRilevamento() {
    return this.dataRilevamento;
  }

  public void setDataRilevamento(java.util.Date dataRilevamento) {
    this.dataRilevamento = dataRilevamento;
  }

  public long getDurata() {
    return this.durata;
  }

  public void setDurata(long durata) {
    this.durata = durata;
  }

  private static final long serialVersionUID = 1L;

  @XmlTransient
  private Long id;

  private static it.govpay.orm.model.RilevamentoModel modelStaticInstance = null;
  private static synchronized void initModelStaticInstance(){
	  if(it.govpay.orm.Rilevamento.modelStaticInstance==null){
  			it.govpay.orm.Rilevamento.modelStaticInstance = new it.govpay.orm.model.RilevamentoModel();
	  }
  }
  public static it.govpay.orm.model.RilevamentoModel model(){
	  if(it.govpay.orm.Rilevamento.modelStaticInstance==null){
	  		initModelStaticInstance();
	  }
	  return it.govpay.orm.Rilevamento.modelStaticInstance;
  }


  @XmlElement(name="idSLA",required=true,nillable=false)
  protected IdSla idSLA;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="idApplicazione",required=false,nillable=false)
  protected long idApplicazione;

  @XmlElement(name="idEventoIniziale",required=true,nillable=false)
  protected IdEvento idEventoIniziale;

  @XmlElement(name="idEventoFinale",required=true,nillable=false)
  protected IdEvento idEventoFinale;

  @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openspcoop2.utils.jaxb.Date2String.class)
  @javax.xml.bind.annotation.XmlSchemaType(name="date")
  @XmlElement(name="dataRilevamento",required=true,nillable=false,type=java.lang.String.class)
  protected java.util.Date dataRilevamento;

  @javax.xml.bind.annotation.XmlSchemaType(name="long")
  @XmlElement(name="durata",required=true,nillable=false)
  protected long durata;

}
