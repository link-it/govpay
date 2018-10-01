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
package it.govpay.model;

public class Operazione {
	
	public enum StatoOperazioneType {
		NON_VALIDO, ESEGUITO_KO, ESEGUITO_OK;
	}
	
	public enum TipoOperazioneType {
		ADD, DEL, INC, N_V
	}
	
	private long id;
	private long idTracciato;
	private long lineaElaborazione;
	private byte[] datiRichiesta;
	private byte[] datiRisposta;
	private StatoOperazioneType stato;
	private String dettaglioEsito;
	private String idOperazione;
	private TipoOperazioneType tipoOperazione;
	private Long idApplicazione;
	private String codVersamentoEnte;
	private String codDominio;
	private String iuv;
	private String trn;
	
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdTracciato() {
		return this.idTracciato;
	}
	public void setIdTracciato(long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public long getLineaElaborazione() {
		return this.lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public byte[] getDatiRichiesta() {
		return this.datiRichiesta;
	}
	public void setDatiRichiesta(byte[] datiRichiesta) {
		this.datiRichiesta = datiRichiesta;
	}
	public byte[] getDatiRisposta() {
		return this.datiRisposta;
	}
	public void setDatiRisposta(byte[] datiRisposta) {
		this.datiRisposta = datiRisposta;
	}
	public String getDettaglioEsito() {
		return this.dettaglioEsito;
	}
	public void setDettaglioEsito(String dettaglioEsito) {
		this.dettaglioEsito = dettaglioEsito;
	}
	public String getIdOperazione() {
		return this.idOperazione;
	}
	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}
	public TipoOperazioneType getTipoOperazione() {
		return this.tipoOperazione;
	}
	public void setTipoOperazione(TipoOperazioneType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public Long getIdApplicazione() {
		return this.idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public StatoOperazioneType getStato() {
		return this.stato;
	}
	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getTrn() {
		return this.trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	
}
