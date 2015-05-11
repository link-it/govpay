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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ESITI_NDP database table.
 * 
 */
@Entity
@Table(name="esiti_ndp")
public class EsitiNdp extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Date dataPagamento;
	private String esitoPagamento;
	private short flagRiconciliazione;
	private int idBozzeBonificiRiaccredito;
	private String idRiconciliazione;
	private String idRiscossione;
	private BigDecimal importo;
	private String segno;
	private String codAnomalia;
	
	public static String COD_ANOMALIA_IMPORTO_MINORE = "E_IMPORTO_ESI_MINORE_IMPORTO_DIST";
	public static String COD_ANOMALIA_IMPORTO_MAGGIORE = "E_IMPORTO_ESI_MAGGIORE_IMPORTO_DIST";
	public static String COD_ANOMALIA_IUV_NON_TROVATO = "E_IUV_NON_TROVATO";
	public static String COD_ANOMALIA_IUR_NON_TROVATO = "E_IUR_NON_TROVATO";
	
	//bi-directional many-to-one association to Rendicontazioni
	/*** Persistent Associations ***/
	private Rendicontazioni rendicontazioni;	

	public EsitiNdp() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id; 
	}	

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_PAGAMENTO")
	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	@Column(name="ESITO_PAGAMENTO")
	public String getEsitoPagamento() {
		return this.esitoPagamento;
	}

	public void setEsitoPagamento(String esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}


	@Column(name="FLAG_RICONCILIAZIONE")
	public short getFlagRiconciliazione() {
		return this.flagRiconciliazione;
	}

	public void setFlagRiconciliazione(short flagRiconciliazione) {
		this.flagRiconciliazione = flagRiconciliazione;
	}


	@Column(name="ID_BOZZE_BONIFICI_RIACCREDITO")
	public int getIdBozzeBonificiRiaccredito() {
		return this.idBozzeBonificiRiaccredito;
	}

	public void setIdBozzeBonificiRiaccredito(int idBozzeBonificiRiaccredito) {
		this.idBozzeBonificiRiaccredito = idBozzeBonificiRiaccredito;
	}


	@ManyToOne
	@JoinColumn(name="ID_RENDICONTAZIONI")	
	public Rendicontazioni getRendicontazioni() {
		return this.rendicontazioni;
	}

	public void setRendicontazioni(Rendicontazioni rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}


	@Column(name="ID_RICONCILIAZIONE")
	public String getIdRiconciliazione() {
		return this.idRiconciliazione;
	}

	public void setIdRiconciliazione(String idRiconciliazione) {
		this.idRiconciliazione = idRiconciliazione;
	}


	@Column(name="ID_RISCOSSIONE")
	public String getIdRiscossione() {
		return this.idRiscossione;
	}

	public void setIdRiscossione(String idRiscossione) {
		this.idRiscossione = idRiscossione;
	}


	public BigDecimal getImporto() {
		return this.importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}


	public String getSegno() {
		return this.segno;
	}

	public void setSegno(String segno) {
		this.segno = segno;
	}
	
	@Column(name="COD_ANOMALIA")
	public String getCodAnomalia() {
		return this.codAnomalia;
	}
	
	public void setCodAnomalia(String codAnomalia) {
		this.codAnomalia = codAnomalia;
	}
}