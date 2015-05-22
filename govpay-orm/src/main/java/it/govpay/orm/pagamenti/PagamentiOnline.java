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
package it.govpay.orm.pagamenti;

import it.govpay.orm.BaseEntity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pagamenti_online")
public class PagamentiOnline extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Timestamp tsOperazione;
	private Timestamp tsInserimento;
	private Timestamp tsAggiornamento;
	private String tiOperazione;
	private String sessionIdToken;
	private String sessionIdTimbro;
	private String sessionIdTerminale;
	private String sessionIdSistema;
	private String opInserimento;
	private String opAggiornamento;
	private String numOperazione;
	private String idOperazione;
	private String deOperazione;
	private String codAutorizzazione;
	private String applicationId;
	private String systemId;

	private String esito = "OK";
	private String codErrore = "OK000000";

	private DistintaPagamento distintaPagamento;

	/*** Auto Generated Identity Property ***/
	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TS_OPERAZIONE")
	public Timestamp getTsOperazione() {
		return tsOperazione;
	}

	public void setTsOperazione(Timestamp tsOperazione) {
		this.tsOperazione = tsOperazione;
	}

	@Column(name = "TI_OPERAZIONE")
	public String getTiOperazione() {
		return tiOperazione;
	}

	public void setTiOperazione(String tiOperazione) {
		this.tiOperazione = tiOperazione;
	}

	@Column(name = "SESSION_ID_TOKEN")
	public String getSessionIdToken() {
		return sessionIdToken;
	}

	public void setSessionIdToken(String sessionIdToken) {
		this.sessionIdToken = sessionIdToken;
	}

	@Column(name = "SESSION_ID_TIMBRO")
	public String getSessionIdTimbro() {
		return sessionIdTimbro;
	}

	public void setSessionIdTimbro(String sessionIdTimbro) {
		this.sessionIdTimbro = sessionIdTimbro;
	}

	@Column(name = "SESSION_ID_TERMINALE")
	public String getSessionIdTerminale() {
		return sessionIdTerminale;
	}

	public void setSessionIdTerminale(String sessionIdTerminale) {
		this.sessionIdTerminale = sessionIdTerminale;
	}

	@Column(name = "SESSION_ID_SISTEMA")
	public String getSessionIdSistema() {
		return sessionIdSistema;
	}

	public void setSessionIdSistema(String sessionIdSistema) {
		this.sessionIdSistema = sessionIdSistema;
	}

	@Column(name = "NUM_OPERAZIONE")
	public String getNumOperazione() {
		return numOperazione;
	}

	public void setNumOperazione(String numOperazione) {
		this.numOperazione = numOperazione;
	}

	@Column(name = "ID_OPERAZIONE")
	public String getIdOperazione() {
		return idOperazione;
	}

	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}

	@Column(name = "DE_OPERAZIONE")
	public String getDeOperazione() {
		return deOperazione;
	}

	public void setDeOperazione(String deOperazione) {
		this.deOperazione = deOperazione;
	}

	@Column(name = "COD_AUTORIZZAZIONE")
	public String getCodAutorizzazione() {
		return codAutorizzazione;
	}

	public void setCodAutorizzazione(String codAutorizzazione) {
		this.codAutorizzazione = codAutorizzazione;
	}

	@Column(name = "APPLICATION_ID")
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "SYSTEM_ID")
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "ESITO")
	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	@Column(name = "COD_ERRORE")
	public String getCodErrore() {
		return codErrore;
	}

	public void setCodErrore(String codErrore) {
		this.codErrore = codErrore;
	}

	@ManyToOne(targetEntity = DistintaPagamento.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISTINTE_PAGAMENTO")
	public DistintaPagamento getDistintaPagamento() {
		return this.distintaPagamento;
	}

	public void setDistintaPagamento(DistintaPagamento flussoDistintaOnline) {
		this.distintaPagamento = flussoDistintaOnline;
	}

	@Override
	public String toString() {
		return "PagamentiOnline [tsOperazione=" + tsOperazione + ", tsInserimento=" + tsInserimento
				+ ", tsAggiornamento=" + tsAggiornamento + ", tiOperazione=" + tiOperazione + ", systemId=" + systemId
				+ ", applicationId=" + applicationId + ", sessionIdToken=" + sessionIdToken + ", sessionIdTimbro="
				+ sessionIdTimbro + ", sessionIdTerminale=" + sessionIdTerminale + ", sessionIdSistema="
				+ sessionIdSistema + ", opInserimento=" + opInserimento + ", opAggiornamento=" + opAggiornamento
				+ ", numOperazione=" + numOperazione + ", idOperazione=" + idOperazione + ", deOperazione="
				+ deOperazione + ", codAutorizzazione=" + codAutorizzazione + ", esito=" + esito + ", codErrore="
				+ codErrore;
	}

}