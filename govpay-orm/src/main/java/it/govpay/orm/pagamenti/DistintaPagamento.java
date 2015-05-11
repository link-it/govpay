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
import it.govpay.orm.configurazione.CfgGatewayPagamento;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "distinte_pagamento")
@NamedQueries({
	@NamedQuery(name = "getDistintaByIdFiscaleCreditoreIuvAndCcp", query = "select d from DistintaPagamento d where d.identificativoFiscaleCreditore = :idFiscaleCreditore and d.iuv = :iuv and d.codTransazionePSP = :ccp"),
	@NamedQuery(name = "getDistintePendenti", query = "select d from DistintaPagamento d where d.identificativoFiscaleCreditore = :idFiscaleCreditore AND ( d.stato = 'ESEGUITO SBF' or (d.stato = 'IN CORSO' and d.cfgGatewayPagamento.modelloVersamento <> '4'))"),
	@NamedQuery(name = "getDistinteDaSpedire", query = "select d from DistintaPagamento d where d.identificativoFiscaleCreditore = :idFiscaleCreditore and d.stato = 'IN CORSO' and d.cfgGatewayPagamento.modelloVersamento = '4'")
})
public class DistintaPagamento extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private BigDecimal importoCommissioni;
	private String codTransazione;
	private int numeroDisposizioni;
	private String stato;
	private Timestamp tmbcreazione;
	private BigDecimal importo;
	private String divisa;
	private String utentecreatore;
	private String emailVersante = "";
	private Timestamp dataSpedizione;
	private CfgGatewayPagamento cfgGatewayPagamento;

	/*** Persistent Collections ***/
	private Set<Pagamento> pagamenti;
	private Set<PagamentiOnline> pagamentiOnline;
	private Set<DocumentoDiPagamento> documentiPagamento;
	private Long idDocumentoRepository;
	private String codPagamento;
	private String codTransazionePSP;
	private String autenticazioneSoggetto;
	private String tipoFirma;
	private String iuv;
	private String identificativoFiscaleCreditore;
	private Timestamp tsAnnullamento = new Timestamp(0);
	private String idGruppo;
	private String ibanAddebito;
	
	
	private DatiAnagraficiVersante datiAnagraficiVersante;

	
	
	/*** Auto Generated Identity Property ***/
	private Long id;

	@Column(name = "DIVISA")
	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	@Column(name = "IMPORTO_COMMISSIONI")
	public BigDecimal getImportoCommissioni() {
		return this.importoCommissioni;
	}

	public void setImportoCommissioni(BigDecimal importoCommissioni) {
		this.importoCommissioni = importoCommissioni;
	}

	@Column(name = "COD_TRANSAZIONE")
	public String getCodTransazione() {
		return codTransazione;
	}

	public void setCodTransazione(String codTransazione) {
		this.codTransazione = codTransazione;
	}

	@Column(name = "COD_PAGAMENTO")
	public String getCodPagamento() {
		return codPagamento;
	}

	public void setCodPagamento(String codPagamento) {
		this.codPagamento = codPagamento;
	}

	@Column(name = "COD_TRANSAZIONE_PSP")
	public String getCodTransazionePSP() {
		return codTransazionePSP;
	}

	public void setCodTransazionePSP(String codTransazionePSP) {
		this.codTransazionePSP = codTransazionePSP;
	}

	@Column(name = "STATO")
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "DATA_CREAZIONE")
	public Timestamp getTmbcreazione() {
		return this.tmbcreazione;
	}

	public void setTmbcreazione(Timestamp tmbcreazione) {
		this.tmbcreazione = tmbcreazione;
	}

	@Column(name = "IMPORTO")
	public BigDecimal getImporto() {
		return this.importo;
	}

	public void setImporto(BigDecimal totimportipositivi) {
		this.importo = totimportipositivi;
	}

	@Column(name = "UTENTE_CREATORE")
	public String getUtentecreatore() {
		return this.utentecreatore;
	}

	public void setUtentecreatore(String utentecreatore) {
		this.utentecreatore = utentecreatore;
	}

	@Column(name = "NUMERO_DISPOSIZIONI")
	public int getNumeroDisposizioni() {
		return numeroDisposizioni;
	}

	public void setNumeroDisposizioni(int numeroDisposizioni) {
		this.numeroDisposizioni = numeroDisposizioni;
	}

	@OneToMany(mappedBy = "distintaPagamento", targetEntity = Pagamento.class, fetch = FetchType.LAZY, cascade = {
			CascadeType.MERGE, CascadeType.PERSIST })
	public Set<Pagamento> getPagamenti() {
		return this.pagamenti;
	}

	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	@OneToMany(mappedBy = "distintaPagamento", targetEntity = PagamentiOnline.class, fetch = FetchType.LAZY, cascade = {
			CascadeType.MERGE, CascadeType.PERSIST })
	public Set<PagamentiOnline> getPagamentiOnline() {
		return pagamentiOnline;
	}

	public void setPagamentiOnline(Set<PagamentiOnline> pagamentiOnline) {
		this.pagamentiOnline = pagamentiOnline;
	}

	@ManyToOne(targetEntity = CfgGatewayPagamento.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_GATEWAY_PAGAMENTO")
	public CfgGatewayPagamento getCfgGatewayPagamento() {
		return cfgGatewayPagamento;
	}

	public void setCfgGatewayPagamento(CfgGatewayPagamento cfgGatewayPagamento) {
		this.cfgGatewayPagamento = cfgGatewayPagamento;
	}

	@Column(name = "DATA_SPEDIZIONE")
	public Timestamp getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(Timestamp dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	@OneToMany(mappedBy = "distinta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<DocumentoDiPagamento> getDocumentiPagamento() {
		return this.documentiPagamento;
	}

	public void setDocumentiPagamento(Set<DocumentoDiPagamento> documenti) {
		this.documentiPagamento = documenti;
	}

	@Column(name = "ID_DOCUMENTO_REPOSITORY")
	public Long getIdDocumentoRepository() {
		return idDocumentoRepository;
	}

	public void setIdDocumentoRepository(Long idDocumentoRepository) {
		this.idDocumentoRepository = idDocumentoRepository;
	}

	@Column(name = "EMAIL_VERSANTE", nullable = false)
	public String getEmailVersante() {
		return emailVersante;
	}

	public void setEmailVersante(String emailVersante) {
		this.emailVersante = emailVersante;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AUTENTICAZIONE_SOGGETTO", length = 1)
	public String getAutenticazioneSoggetto() {
		return this.autenticazioneSoggetto;
	}

	public void setAutenticazioneSoggetto(String autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}

	@Column(name = "TIPO_FIRMA", length = 1)
	public String getTipoFirma() {
		return this.tipoFirma;
	}

	public void setTipoFirma(String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}

	@Column(name = "IUV", length = 35)
	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	@Column(name = "ID_FISCALE_CREDITORE", length = 16)
	public String getIdentificativoFiscaleCreditore() {
		return this.identificativoFiscaleCreditore;
	}

	public void setIdentificativoFiscaleCreditore(String identificativoFiscaleCreditore) {
		this.identificativoFiscaleCreditore = identificativoFiscaleCreditore;
	}

	@Column(name = "TS_ANNULLAMENTO")
	public Timestamp getTsAnnullamento() {
		return this.tsAnnullamento;
	}

	public void setTsAnnullamento(Timestamp tsAnnullamento) {
		this.tsAnnullamento = tsAnnullamento;
	}

	@Column(name = "ID_GRUPPO", length = 35)
	public String getIdGruppo() {
		return this.idGruppo;
	}

	public void setIdGruppo(String idGruppo) {
		this.idGruppo = idGruppo;
	}

	@Column(name = "IBAN_ADDEBITO", length = 35)
	public String getIbanAddebito() {
		return this.ibanAddebito;
	}

	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}

	
	@OneToOne(mappedBy = "distintaPagamento", fetch = FetchType.LAZY)
	public DatiAnagraficiVersante getDatiAnagraficiVersante() {
		return datiAnagraficiVersante;
	}

	public void setDatiAnagraficiVersante(DatiAnagraficiVersante datiAnagraficiVersante) {
		this.datiAnagraficiVersante = datiAnagraficiVersante;
	}

}
