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


/** <p>Java class for id-singolo-versamento complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="id-singolo-versamento"&gt;
 * 		&lt;sequence&gt;
 * 			&lt;element name="idVersamento" type="{http://www.govpay.it/orm}id-versamento" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="codSingoloVersamentoEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="indiceDati" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="1" maxOccurs="1"/&gt;
 * 			&lt;element name="idTributo" type="{http://www.govpay.it/orm}id-tributo" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="note" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="statoSingoloVersamento" type="{http://www.govpay.it/orm}string" minOccurs="0" maxOccurs="1"/&gt;
 * 			&lt;element name="importoSingoloVersamento" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0" maxOccurs="1"/&gt;
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
@XmlType(name = "id-singolo-versamento",
  propOrder = {
  	"idVersamento",
  	"codSingoloVersamentoEnte",
  	"indiceDati",
  	"idTributo",
  	"note",
  	"statoSingoloVersamento",
  	"importoSingoloVersamento"
  }
)

@XmlRootElement(name = "id-singolo-versamento")

public class IdSingoloVersamento extends org.openspcoop2.utils.beans.BaseBeanWithId implements Serializable , Cloneable {
  public IdSingoloVersamento() {
    super();
  }

  public IdVersamento getIdVersamento() {
    return this.idVersamento;
  }

  public void setIdVersamento(IdVersamento idVersamento) {
    this.idVersamento = idVersamento;
  }

  public java.lang.String getCodSingoloVersamentoEnte() {
    return this.codSingoloVersamentoEnte;
  }

  public void setCodSingoloVersamentoEnte(java.lang.String codSingoloVersamentoEnte) {
    this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
  }

  public java.math.BigInteger getIndiceDati() {
    return this.indiceDati;
  }

  public void setIndiceDati(java.math.BigInteger indiceDati) {
    this.indiceDati = indiceDati;
  }

  public IdTributo getIdTributo() {
    return this.idTributo;
  }

  public void setIdTributo(IdTributo idTributo) {
    this.idTributo = idTributo;
  }

  public java.lang.String getNote() {
    return this.note;
  }

  public void setNote(java.lang.String note) {
    this.note = note;
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

  private static final long serialVersionUID = 1L;



  @XmlElement(name="idVersamento",required=true,nillable=false)
  protected IdVersamento idVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="codSingoloVersamentoEnte",required=true,nillable=false)
  protected java.lang.String codSingoloVersamentoEnte;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="positiveInteger")
  @XmlElement(name="indiceDati",required=true,nillable=false)
  protected java.math.BigInteger indiceDati;

  @XmlElement(name="idTributo",required=false,nillable=false)
  protected IdTributo idTributo;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="note",required=false,nillable=false)
  protected java.lang.String note;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="string")
  @XmlElement(name="statoSingoloVersamento",required=false,nillable=false)
  protected java.lang.String statoSingoloVersamento;

  @jakarta.xml.bind.annotation.XmlSchemaType(name="double")
  @XmlElement(name="importoSingoloVersamento",required=false,nillable=false)
  protected double importoSingoloVersamento;

}
