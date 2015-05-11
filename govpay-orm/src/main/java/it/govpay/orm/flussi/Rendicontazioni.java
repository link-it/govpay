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
package it.govpay.orm.flussi;

import it.govpay.orm.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the RENDICONTAZIONI database table.
 * 
 */
@Entity
@Table(name="rendicontazioni")
public class Rendicontazioni extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*** Persistent Values ***/
	private Long id;
	private String codRendicontazione;
	private Timestamp dataCreazione;
	private Timestamp dataRicezione;
	private String divisa;
	private short flagElaborazione;
	private BigDecimal importo;
	private int numEsitiInsoluto;
	private int numEsitiPagato;
	private int numeroEsiti;
	private String stato;
	private String utenteCreatore;
	private String idFlusso;
	private String idRegolamento;
	private Date dataRegolamento;
	
	
	/*** Persistent Associations ***/
	private Set<EsitiNdp> esitiNdps;
	private CasellarioInfo casellarioInfo;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id; 
	}		

	
	
	public void setId(Long id) {
		this.id = id;
	}



	/*******************************************/
	/****** Persistent Properties Mapping ******/
	/*******************************************/
	@Column(name="COD_RENDICONTAZIONE")
	public String getCodRendicontazione() {
		return this.codRendicontazione;
	}

	public void setCodRendicontazione(String codRendicontazione) {
		this.codRendicontazione = codRendicontazione;
	}


	@Column(name="DATA_CREAZIONE")
	public Timestamp getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}


	@Column(name="DATA_RICEZIONE")
	public Timestamp getDataRicezione() {
		return this.dataRicezione;
	}

	public void setDataRicezione(Timestamp dataRicezione) {
		this.dataRicezione = dataRicezione;
	}


	public String getDivisa() {
		return this.divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}


	@Column(name="FLAG_ELABORAZIONE")
	public short getFlagElaborazione() {
		return this.flagElaborazione;
	}

	public void setFlagElaborazione(short flagElaborazione) {
		this.flagElaborazione = flagElaborazione;
	}


	public BigDecimal getImporto() {
		return this.importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}


	@Column(name="NUM_ESITI_INSOLUTO")
	public int getNumEsitiInsoluto() {
		return this.numEsitiInsoluto;
	}

	public void setNumEsitiInsoluto(int numEsitiInsoluto) {
		this.numEsitiInsoluto = numEsitiInsoluto;
	}


	@Column(name="NUM_ESITI_PAGATO")
	public int getNumEsitiPagato() {
		return this.numEsitiPagato;
	}

	public void setNumEsitiPagato(int numEsitiPagato) {
		this.numEsitiPagato = numEsitiPagato;
	}


	@Column(name="NUMERO_ESITI")
	public int getNumeroEsiti() {
		return this.numeroEsiti;
	}

	public void setNumeroEsiti(int numeroEsiti) {
		this.numeroEsiti = numeroEsiti;
	}


	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}


	@Column(name="UTENTE_CREATORE")
	public String getUtenteCreatore() {
		return this.utenteCreatore;
	}

	public void setUtenteCreatore(String utenteCreatore) {
		this.utenteCreatore = utenteCreatore;
	}

	
	@Column(name="ID_FLUSSO")
	public String getIdFlusso() {
		return idFlusso;
	}
	
	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}
	
	
	@Column(name="ID_REGOLAMENTO")
	public String getIdRegolamento() {
		return idRegolamento;
	}
	
	public void setIdRegolamento(String idRegolamento) {
		this.idRegolamento = idRegolamento;
	}
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_REGOLAMENTO")
	public Date getDataRegolamento() {
		return dataRegolamento;
	}
	
	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}
	

	

	//bi-directional many-to-one association to EsitiNdp
	@OneToMany(mappedBy="rendicontazioni")
	public Set<EsitiNdp> getEsitiNdps() {
		return this.esitiNdps;
	}
	public void setEsitiNdps(Set<EsitiNdp> esitiNdps) {
		this.esitiNdps = esitiNdps;
	}	
		
	//bi-directional one-to-one association to CasellarioInfo
	@OneToOne
	@JoinColumn(name="ID_CASELLARIO_INFO")
	public CasellarioInfo getCasellarioInfo() {
		return this.casellarioInfo;
	}

	public void setCasellarioInfo(CasellarioInfo casellarioInfo) {
		this.casellarioInfo = casellarioInfo;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rendicontazioni [codRendicontazione=");
		builder.append(codRendicontazione);
		builder.append(", dataCreazione=");
		builder.append(dataCreazione);
		builder.append(", dataRicezione=");
		builder.append(dataRicezione);
		builder.append(", divisa=");
		builder.append(divisa);
		builder.append(", flagElaborazione=");
		builder.append(flagElaborazione);
		builder.append(", importo=");
		builder.append(importo);
		builder.append(", numEsitiInsoluto=");
		builder.append(numEsitiInsoluto);
		builder.append(", numEsitiPagato=");
		builder.append(numEsitiPagato);
		builder.append(", numeroEsiti=");
		builder.append(numeroEsiti);
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", utenteCreatore=");
		builder.append(utenteCreatore);
		builder.append(", idFlusso=");
		builder.append(idFlusso);
		builder.append(", idRegolamento=");
		builder.append(idRegolamento);
		builder.append(", dataRegolamento=");
		builder.append(dataRegolamento);
		//builder.append(", esitiBbs=");
		//builder.append(esitiBbs);
		//builder.append(", esitiBonificiRiaccreditos=");
		//builder.append(esitiBonificiRiaccreditos);
		//builder.append(", esitiRcts=");
		//builder.append(esitiRcts);
		//builder.append(", incassiBonificiRhs=");
		//builder.append(incassiBonificiRhs);
		//builder.append(", casellarioInfo=");
		//builder.append(casellarioInfo);
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getVersion()=");
		builder.append(getVersion());
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
		result = prime * result
				+ ((casellarioInfo == null) ? 0 : casellarioInfo.hashCode());
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
		Rendicontazioni other = (Rendicontazioni) obj;
		if (casellarioInfo == null) {
			if (other.casellarioInfo != null) {
				return false;
			}
		} else if (!casellarioInfo.equals(other.casellarioInfo)) {
			return false;
		}
		return true;
	}

	
}