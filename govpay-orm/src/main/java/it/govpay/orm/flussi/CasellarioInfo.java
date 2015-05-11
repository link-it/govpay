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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the CASELLARIO_INFO database table.
 * 
 */
@Entity
@Table(name="casellario_info")
public class CasellarioInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*** Persistent Values ***/
	private Long id;
	private String idSupporto;	
	private Timestamp dataElaborazione;
	private String descErrore;
	private int dimensione;
	private short flagElaborazione;
	private byte[] flussoCbi;
	private String nomeSupporto;
	private int numeroRecord;
	private short tipoErrore;
	private String tipoFlusso;
	private String mittente;
	private String ricevente;

	/*** Transient Properties ***/
	private Date dataCreazione;
	
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
	@Column(name="ID_SUPPORTO", nullable=false, insertable=true, updatable=false, unique=true) 
	public String getIdSupporto() {
		return this.idSupporto;
	}
	
	public void setIdSupporto(String idSupporto) {
		this.idSupporto = idSupporto;
	}

	@Column(name="DATA_ELABORAZIONE")
	public Timestamp getDataElaborazione() {
		return this.dataElaborazione;
	}

	public void setDataElaborazione(Timestamp dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}


	@Column(name="DESC_ERRORE")
	public String getDescErrore() {
		return this.descErrore;
	}

	public void setDescErrore(String descErrore) {
		this.descErrore = descErrore;
	}

	public int getDimensione() {
		return this.dimensione;
	}

	public void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	@Column(name="FLAG_ELABORAZIONE")
	public short getFlagElaborazione() {
		return this.flagElaborazione;
	}

	public void setFlagElaborazione(short flagElaborazione) {
		this.flagElaborazione = flagElaborazione;
	}

    @Lob()
	@Column(name="FLUSSO_CBI")
	public byte[] getFlussoCbi() {
		return this.flussoCbi;
	}

	public void setFlussoCbi(byte[] flussoCbi) {
		this.flussoCbi = flussoCbi;
	}


	@Column(name="NOME_SUPPORTO")
	public String getNomeSupporto() {
		return this.nomeSupporto;
	}

	public void setNomeSupporto(String nomeSupporto) {
		this.nomeSupporto = nomeSupporto;
	}


	@Column(name="NUMERO_RECORD")
	public int getNumeroRecord() {
		return this.numeroRecord;
	}

	public void setNumeroRecord(int numeroRecord) {
		this.numeroRecord = numeroRecord;
	}
	

	@Column(name="TIPO_ERRORE")
	public short getTipoErrore() {
		return this.tipoErrore;
	}

	public void setTipoErrore(short tipoErrore) {
		this.tipoErrore = tipoErrore;
	}


	@Column(name="TIPO_FLUSSO")
	public String getTipoFlusso() {
		return this.tipoFlusso;
	}

	public void setTipoFlusso(String tipoFlusso) {
		this.tipoFlusso = tipoFlusso;
	}
	
	@Column(name="MITTENTE")
	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	@Column(name="RICEVENTE")
	public String getRicevente() {
		return ricevente;
	}

	public void setRicevente(String ricevente) {
		this.ricevente = ricevente;
	}

	@Transient
	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
		
	
	/*** LifeCycle CallBacks ***/
	/*** Composite property to ensure the unicity of CBI record ***/
	@PrePersist
	public void setDefaultIdSupporto() {
		if (this.idSupporto==null) {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
			StringBuilder sb = new StringBuilder();
			sb.append(this.getTipoFlusso()).
				append(this.getMittente()).
				append(this.getRicevente()).
				append(this.getDataCreazione()!=null?sdf.format(this.getDataCreazione()):"").
				append(this.getNomeSupporto());
			this.setIdSupporto(sb.toString());
		}	
	}
	
	/*** Overriding Methods ***/
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CasellarioInfo [idSupporto=");
		builder.append(idSupporto);
		builder.append(", dataElaborazione=");
		builder.append(dataElaborazione);
		builder.append(", descErrore=");
		builder.append(descErrore);
		builder.append(", dimensione=");
		builder.append(dimensione);
		builder.append(", flagElaborazione=");
		builder.append(flagElaborazione);
		builder.append(", flussoCbi=");
		builder.append(Arrays.toString(flussoCbi));
		builder.append(", nomeSupporto=");
		builder.append(nomeSupporto);
		builder.append(", numeroRecord=");
		builder.append(numeroRecord);
		builder.append(", tipoErrore=");
		builder.append(tipoErrore);
		builder.append(", tipoFlusso=");
		builder.append(tipoFlusso);
		//builder.append(", rendicontazioni=");
		//builder.append(rendicontazioni);
		builder.append(", mittente=");
		builder.append(mittente);
		builder.append(", ricevente=");
		builder.append(ricevente);
		builder.append(", dataCreazione=");
		builder.append(dataCreazione);
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


	


}