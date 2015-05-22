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
package it.govpay.orm.profilazione;

import it.govpay.orm.BaseEntity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tributi_ente")
public class TributoEnte extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** PK Reference ***/
	private EnteId tribEnId;

	/*** Persistent Properties ***/
	private String idTributo;
	private String deTrb;
	private String flIniziativa;
	private String flPredeterm;
	private String flRicevutaAnonimo;
	private String flNotificaPagamento;
	private String soggEsclusi;
	private String stato;
	private int prVersione;

	private String SIL;
	private String IBAN;
	private String infoTributo;
	private String urlInfoService;
	private String flNascostoFe;
	private String urlUpdService;
	private String istruzioniPagamento;
	private String ibanContoTecnico;

	/*** Persistent References ***/
	private Ente ente;
	private CategoriaTributo categoria;
	private SistemaEnte sistemaEnte;

	public TributoEnte() {
		this.tribEnId = new EnteId();
	}

	public TributoEnte(EnteId id) {
		this.tribEnId = id;
	}

	public TributoEnte(String idEnte, String cdTrbEnte, int prVersione, String opInserimento, Timestamp tsInserimento) {
		this.tribEnId.setCdTrbEntePk(cdTrbEnte);
		this.tribEnId.setIdEntePk(idEnte);
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);
	}

	public TributoEnte(String idEnte, String cdTrbEnte, String idTributo, String deTrb, String flIniziativa,
			String soggEsclusi, String stato, int prVersione, String opInserimento, Timestamp tsInserimento,
			String opAggiornamento, Timestamp tsAggiornamento) {
		this.tribEnId.setCdTrbEntePk(cdTrbEnte);
		this.tribEnId.setIdEntePk(idEnte);
		this.idTributo = idTributo;
		this.deTrb = deTrb;
		this.flIniziativa = flIniziativa;
		this.soggEsclusi = soggEsclusi;
		this.stato = stato;
		this.prVersione = prVersione;
		super.setOpInserimento(opInserimento);
		super.setTsInserimento(tsInserimento);
		super.setOpAggiornamento(opAggiornamento);
		super.setTsAggiornamento(tsAggiornamento);
	}

	@Id
	public EnteId getTribEnId() {
		return tribEnId;
	}

	public void setTribEnId(EnteId tribEnId) {
		this.tribEnId = tribEnId;
	}

	@Column(name = "IBAN")
	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iban) {
		IBAN = iban;
	}

	@Column(name = "ID_TRIBUTO")
	public String getIdTributo() {
		return this.idTributo;
	}

	public void setIdTributo(String idTributo) {
		this.idTributo = idTributo;
	}

	@Column(name = "DE_TRB")
	public String getDeTrb() {
		return this.deTrb;
	}

	public void setDeTrb(String deTrb) {
		this.deTrb = deTrb;
	}

	@Column(name = "FL_INIZIATIVA")
	public String getFlIniziativa() {
		return this.flIniziativa;
	}

	public void setFlIniziativa(String flIniziativa) {
		this.flIniziativa = flIniziativa;
	}

	@Column(name = "SOGG_ESCLUSI")
	public String getSoggEsclusi() {
		return this.soggEsclusi;
	}

	public void setSoggEsclusi(String soggEsclusi) {
		this.soggEsclusi = soggEsclusi;
	}

	@Column(name = "STATO")
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "PR_VERSIONE")
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "ID_ENTE", insertable = false, updatable = false)
	public String getIdEnte() {
		return this.tribEnId.getIdEntePk();
	}

	public void setIdEnte(String idEnte) {
		this.tribEnId.setIdEntePk(idEnte);
	}

	@Column(name = "CD_TRB_ENTE", insertable = false, updatable = false)
	public String getCdTrbEnte() {
		return tribEnId.getCdTrbEntePk();
	}

	public void setCdTrbEnte(String cdTrbEnte) {
		this.tribEnId.setCdTrbEntePk(cdTrbEnte);
	}

	@Column(name = "ID_SYSTEM")
	public String getSIL() {
		return SIL;
	}

	public void setSIL(String sil) {
		SIL = sil;
	}

	@Column(name = "FL_PREDETERM")
	public String getFlPredeterm() {
		return flPredeterm;
	}

	public void setFlPredeterm(String flPredeterm) {
		this.flPredeterm = flPredeterm;
	}

	@Column(name = "FL_RICEVUTA_ANONIMO")
	public String getFlRicevutaAnonimo() {
		return flRicevutaAnonimo;
	}

	public void setFlRicevutaAnonimo(String flRicevutaAnonimo) {
		this.flRicevutaAnonimo = flRicevutaAnonimo;
	}

	@Column(name = "FL_NOTIFICA_PAGAMENTO")
	public String getFlNotificaPagamento() {
		return flNotificaPagamento;
	}

	public void setFlNotificaPagamento(String flNotificaPagamento) {
		this.flNotificaPagamento = flNotificaPagamento;
	}

	@Column(name = "URL_INFO_SERVICE")
	public String getUrlInfoService() {
		return urlInfoService;
	}

	public void setUrlInfoService(String urlInfoService) {
		this.urlInfoService = urlInfoService;
	}

	@ManyToOne(targetEntity = Ente.class)
	@JoinColumn(name = "ID_ENTE", insertable = false, updatable = false)
	public Ente getEnte() {
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = (Ente) ente;
	}

	@ManyToOne(targetEntity = CategoriaTributo.class)
	@JoinColumn(name = "ID_TRIBUTO", insertable = false, updatable = false)
	public CategoriaTributo getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaTributo categoria) {
		this.categoria = (CategoriaTributo) categoria;
	}

	@ManyToOne(targetEntity = SistemaEnte.class)
	@JoinColumns({ 
		@JoinColumn(name = "ID_ENTE", referencedColumnName="ID_ENTE", insertable = false, updatable = false),
		@JoinColumn(name = "ID_SYSTEM", referencedColumnName="ID_SYSTEM", insertable = false, updatable = false) 
	})
	public SistemaEnte getSistemaEnte() {
		return sistemaEnte;
	}

	public void setSistemaEnte(SistemaEnte sistemaEnte) {
		this.sistemaEnte = (SistemaEnte) sistemaEnte;
	}

	@Column(name = "INFO_TRIBUTO")
	public String getInfoTributo() {
		return infoTributo;
	}

	public void setInfoTributo(String infoTributo) {
		this.infoTributo = infoTributo;
	}

	@Column(name = "FL_NASCOSTO_FE")
	public String getFlNascostoFe() {
		return flNascostoFe;
	}

	public void setFlNascostoFe(String flNascostoFe) {
		this.flNascostoFe = flNascostoFe;
	}

	@Column(name = "URL_UPD_SERVICE")
	public String getUrlUpdService() {
		return urlUpdService;
	}

	public void setUrlUpdService(String urlUpdService) {
		this.urlUpdService = urlUpdService;
	}

	@Column(name = "ISTRUZIONI_PAGAMENTO")
	public String getIstruzioniPagamento() {
		return istruzioniPagamento;
	}

	public void setIstruzioniPagamento(String istruzioniPagamento) {
		this.istruzioniPagamento = istruzioniPagamento;
	}

	@Column(name = "IBAN_CONTO_TECNICO")
	public String getIbanContoTecnico() {
		return ibanContoTecnico;
	}

	public void setIbanContoTecnico(String ibanContoTecnico) {
		this.ibanContoTecnico = ibanContoTecnico;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TributoEnte [getTribEnId()=");
		builder.append(getTribEnId());
		builder.append(", getIBAN()=");
		builder.append(getIBAN());
		builder.append(", getIdTributo()=");
		builder.append(getIdTributo());
		builder.append(", getDeTrb()=");
		builder.append(getDeTrb());
		builder.append(", getFlIniziativa()=");
		builder.append(getFlIniziativa());
		builder.append(", getSoggEsclusi()=");
		builder.append(getSoggEsclusi());
		builder.append(", getStato()=");
		builder.append(getStato());
		builder.append(", getPrVersione()=");
		builder.append(getPrVersione());
		builder.append(", getIdEnte()=");
		builder.append(getIdEnte());
		builder.append(", getCdTrbEnte()=");
		builder.append(getCdTrbEnte());
		builder.append(", getSIL()=");
		builder.append(getSIL());
		builder.append(", getFlPredeterm()=");
		builder.append(getFlPredeterm());
		builder.append(", getFlRicevutaAnonimo()=");
		builder.append(getFlRicevutaAnonimo());
		builder.append(", getFlNotificaPagamento()=");
		builder.append(getFlNotificaPagamento());
		builder.append(", getUrlInfoService()=");
		builder.append(getUrlInfoService());
		builder.append(", getEnte()=");
		builder.append(getEnte());
		builder.append(", getCategoria()=");
		builder.append(getCategoria());
		builder.append(", getSistemaEnte()=");
		builder.append(getSistemaEnte());
		builder.append(", getInfoTributo()=");
		builder.append(getInfoTributo());
		builder.append(", getFlNascostoFe()=");
		builder.append(getFlNascostoFe());
		builder.append(", getUrlUpdService()=");
		builder.append(getUrlUpdService());
		builder.append(", getIstruzioniPagamento()=");
		builder.append(getIstruzioniPagamento());
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
