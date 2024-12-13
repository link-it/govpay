/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.Date;

public class Tracciato extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Date getDataCaricamento() {
		return this.dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public Date getDataCompletamento() {
		return this.dataCompletamento;
	}
	public void setDataCompletamento(Date dataCompletamento) {
		this.dataCompletamento = dataCompletamento;
	}
	public String getBeanDati() {
		return this.beanDati;
	}
	public void setBeanDati(String beanDati) {
		this.beanDati = beanDati;
	}
	public String getFileNameRichiesta() {
		return this.fileNameRichiesta;
	}
	public void setFileNameRichiesta(String fileNameRichiesta) {
		this.fileNameRichiesta = fileNameRichiesta;
	}
	public byte[] getRawRichiesta() {
		return this.rawRichiesta;
	}
	public void setRawRichiesta(byte[] rawRichiesta) {
		this.rawRichiesta = rawRichiesta;
	}
	public String getFileNameEsito() {
		return this.fileNameEsito;
	}
	public void setFileNameEsito(String fileNameEsito) {
		this.fileNameEsito = fileNameEsito;
	}
	public byte[] getRawEsito() {
		return this.rawEsito;
	}
	public void setRawEsito(byte[] rawEsito) {
		this.rawEsito = rawEsito;
	}
	
	public TIPO_TRACCIATO getTipo() {
		return this.tipo;
	}
	public void setTipo(TIPO_TRACCIATO tipo) {
		this.tipo = tipo;
	}

	public STATO_ELABORAZIONE getStato() {
		return this.stato;
	}
	public void setStato(STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}

	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	
	public Long getIdOperatore() {
		return this.idOperatore;
	}
	public void setIdOperatore(Long idOperatore) {
		this.idOperatore = idOperatore;
	}
	
	public FORMATO_TRACCIATO getFormato() {
		return formato;
	}
	public void setFormato(FORMATO_TRACCIATO formato) {
		this.formato = formato;
	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public byte[] getZipStampe() {
		return zipStampe;
	}
	public void setZipStampe(byte[] zipStampe) {
		this.zipStampe = zipStampe;
	}

	public enum STATO_ELABORAZIONE {ELABORAZIONE, COMPLETATO, SCARTATO, IN_STAMPA}
	
	public enum TIPO_TRACCIATO { PENDENZA }
	
	public enum FORMATO_TRACCIATO { CSV, JSON, XML}
	
	private TIPO_TRACCIATO tipo;
	private STATO_ELABORAZIONE stato;
	private FORMATO_TRACCIATO formato;
	private String codTipoVersamento;
	private String descrizioneStato;
	private String codDominio;
	private Date dataCaricamento;
	private Date dataCompletamento;
	private String beanDati;
	private String fileNameRichiesta;
	private byte[] rawRichiesta;
	private String fileNameEsito;
	private byte[] rawEsito;
	private Long id;
	private Long idOperatore;
	private byte[] zipStampe;
}
