/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.orm.posizionedebitoria;

import it.govpay.orm.BaseEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@Entity
@Table(name = "allegati")
@NamedQueries({
//	@NamedQuery(name = "getAllegatoByIdCondAndIdPend", query = "select a from Allegato a where a.idPendenza = :idPendenza and a.idCondizione = :idCondizione"),
//	@NamedQuery(name = "getAllegatoCondizione", query = "select a from Allegato a where a.idPendenza = :idPendenza and a.idCondizione = :idCondizione "
//			+ " and a.tiAllegato = :tiAllegato and a.tiCodificaBody = :tiCodificaBody") 
})
public class Allegato extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/*** Persistent Values ***/
	private String idAllegato;
	private byte[] datiBody;
	private String flContesto;
	private String idAntifalsific;
	private String idPendenza;
	private String idCondizione;
	private int prVersione;
	private String stRiga;
	private String tiAllegato;
	private String tiCodificaBody;
	private String titolo;
	private Timestamp tsDecorrenza;

	@Id
	@Column(name = "ID_ALLEGATO")
	public String getIdAllegato() {
		return this.idAllegato;
	}

	public void setIdAllegato(String idAllegato) {
		this.idAllegato = idAllegato;
	}

	@Lob
	@Column(name = "DATI_BODY")
	public byte[] getDatiBody() {
		return this.datiBody;
	}

	public void setDatiBody(byte[] datiBody) {
		this.datiBody = datiBody;
	}

	@Column(name = "FL_CONTESTO")
	public String getFlContesto() {
		return this.flContesto;
	}

	public void setFlContesto(String flContesto) {
		this.flContesto = flContesto;
	}

	@Column(name = "ID_ANTIFALSIFIC")
	public String getIdAntifalsific() {
		return this.idAntifalsific;
	}

	public void setIdAntifalsific(String idAntifalsific) {
		this.idAntifalsific = idAntifalsific;
	}

	@Column(name = "ID_PENDENZA")
	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	@Column(name = "PR_VERSIONE")
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "ST_RIGA")
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(name = "TI_ALLEGATO")
	public String getTiAllegato() {
		return this.tiAllegato;
	}

	public void setTiAllegato(String tiAllegato) {
		this.tiAllegato = tiAllegato;
	}

	@Column(name = "TI_CODIFICA_BODY")
	public String getTiCodificaBody() {
		return this.tiCodificaBody;
	}

	public void setTiCodificaBody(String tiCodificaBody) {
		this.tiCodificaBody = tiCodificaBody;
	}

	@Column(name = "TITOLO")
	public String getTitolo() {
		return this.titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	@Column(name = "TS_DECORRENZA")
	public Timestamp getTsDecorrenza() {
		return this.tsDecorrenza;
	}

	public void setTsDecorrenza(Timestamp tsDecorrenza) {
		this.tsDecorrenza = tsDecorrenza;
	}

	@Column(name = "ID_CONDIZIONE")
	public String getIdCondizione() {
		return idCondizione;
	}

	public void setIdCondizione(String idCondizione) {
		this.idCondizione = idCondizione;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Allegato [idAllegato=");
		builder.append(idAllegato);
		builder.append(", datiBody=");
		builder.append(Arrays.toString(datiBody));
		builder.append(", flContesto=");
		builder.append(flContesto);
		builder.append(", idAntifalsific=");
		builder.append(idAntifalsific);
		builder.append(", idPendenza=");
		builder.append(idPendenza);
		builder.append(", prVersione=");
		builder.append(prVersione);
		builder.append(", stRiga=");
		builder.append(stRiga);
		builder.append(", tiAllegato=");
		builder.append(tiAllegato);
		builder.append(", tiCodificaBody=");
		builder.append(tiCodificaBody);
		builder.append(", titolo=");
		builder.append(titolo);
		builder.append(", tsDecorrenza=");
		builder.append(tsDecorrenza);
		// builder.append(", condizionePagamento=");
		// builder.append(condizionePagamento);
		builder.append(", idCondizione=");
		builder.append(idCondizione);
		builder.append(", getOpInserimento()=");
		builder.append(getOpInserimento());
		builder.append(", getOpAggiornamento()=");
		builder.append(getOpAggiornamento());
		builder.append(", getTsInserimento()=");
		builder.append(getTsInserimento());
		builder.append(", getTsAggiornamento()=");
		builder.append(getTsAggiornamento());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAllegato == null) ? 0 : idAllegato.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Allegato other = (Allegato) obj;
		if (idAllegato == null) {
			if (other.idAllegato != null) {
				return false;
			}
		} else if (!idAllegato.equals(other.idAllegato)) {
			return false;
		}
		return true;
	}

	/**
	 * Vedi RFC 127
	 * 
	 * <xs:simpleType name="MIMETypeCode"> <xs:restriction base="Max4Text"> <xs:enumeration value="GIF_"/>
	 * <xs:enumeration value="HTML"/> <xs:enumeration value="JPEG"/> <xs:enumeration value="LNK_"/> <xs:enumeration
	 * value="MSWD"/> <xs:enumeration value="MSEX"/> <xs:enumeration value="MSPP"/> <xs:enumeration value="PDF_"/>
	 * <xs:enumeration value="PNG_"/> <xs:enumeration value="TEXT"/> <xs:enumeration value="XML_"/> </xs:restriction>
	 * </xs:simpleType>
	 */
	public enum TipoCodifica {
		GIF("GIF_"), HTML("HTML"), JPEG("JPEG"), LNK("LNK_"), MSWD("MSWD"), MSEX("MSEX"), MSPP("MSPP"), PDF("PDF_"), PNG(
				"PNG_"), TEXT("TEXT"), XML("XML_");

		private String code;

		TipoCodifica(String code) {
			this.code = code;
		}

		public String getCode() {
			return this.code;
		}
	}
}
