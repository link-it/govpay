/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.model.reportistica.statistiche;

public class StatisticaRiscossione extends StatisticaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private Long idDominio;
	private String codDominio;
//	private Long idUo;
	private String codUo;
//	private Long idTipoVersamento;
	private String codTipoVersamento;
	private String direzione;
	private String divisione;
	private String tassonomia;
//	private Long idApplicazione;
	private String codApplicazione;
//	private TipoPagamento tipo;
	
	public StatisticaRiscossione() {
		super();
	}

//	public StatisticaRiscossione(FiltroRiscossioni filtro) {
//		super(filtro);
////		this.setIdApplicazione(filtro.getIdApplicazione());
////		this.setIdDominio(filtro.getIdDominio());
////		this.setIdTipoVersamento(filtro.getIdTipoVersamento());
////		this.setIdUo(filtro.getIdUo());
//	}
	
//	public Long getIdDominio() {
//		return idDominio;
//	}
//	public void setIdDominio(Long idDominio) {
//		this.idDominio = idDominio;
//	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
//	public Long getIdUo() {
//		return idUo;
//	}
//	public void setIdUo(Long idUo) {
//		this.idUo = idUo;
//	}
	public String getCodUo() {
		return codUo;
	}
	public void setCodUo(String codUo) {
		this.codUo = codUo;
	}
//	public Long getIdTipoVersamento() {
//		return idTipoVersamento;
//	}
//	public void setIdTipoVersamento(Long idTipoVersamento) {
//		this.idTipoVersamento = idTipoVersamento;
//	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public String getDirezione() {
		return direzione;
	}
	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}
	public String getDivisione() {
		return divisione;
	}
	public void setDivisione(String divisione) {
		this.divisione = divisione;
	}
//	public Long getIdApplicazione() {
//		return idApplicazione;
//	}
//	public void setIdApplicazione(Long idApplicazione) {
//		this.idApplicazione = idApplicazione;
//	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public String getTassonomia() {
		return tassonomia;
	}
	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

//	public TipoPagamento getTipo() {
//		return tipo;
//	}
//
//	public void setTipo(TipoPagamento tipo) {
//		this.tipo = tipo;
//	}
}
