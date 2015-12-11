/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.utils;

import it.govpay.bd.model.Ente;

public class NotificaParameters {

	private Long idVersamento;
	private long bundleKey;
	private Long idPdf;
	private Long idEnte;
	private Ente ente;
	private Long idTracciato;
	private String destinatario;
	private String[] cc; 
	private RptPlaceholder valori;
	
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Ente getEnte() {
		return ente;
	}
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	public Long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public RptPlaceholder getValori() {
		return valori;
	}
	public void setValori(RptPlaceholder valori) {
		this.valori = valori;
	}
	public Long getIdPdf() {
		return idPdf;
	}
	public void setIdPdf(Long idPdf) {
		this.idPdf = idPdf;
	}
	public long getBundleKey() {
		return bundleKey;
	}
	public void setBundleKey(long bundleKey) {
		this.bundleKey = bundleKey;
	}
	public Long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}
}
