/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

public class Fr extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	public enum StatoFr {
		ACCETTATA,
		RIFIUTATA
	}
	
	private Long id;
	private long idPsp;
	private long idDominio;
	private String codFlusso;
	private StatoFr stato;
	private String descrizioneStato;
	private String iur;
	private String codBicRiversamento;
	private int annoRiferimento;
	private Date dataFlusso;
	private Date dataRegolamento;
	private long numeroPagamenti;
	private double importoTotalePagamenti;
	private byte[] xml;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(long idPsp) {
		this.idPsp = idPsp;
	}
	public long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public StatoFr getStato() {
		return stato;
	}
	public void setStato(StatoFr stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public int getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	public Date getDataFlusso() {
		return dataFlusso;
	}
	public void setDataFlusso(Date dataFlusso) {
		this.dataFlusso = dataFlusso;
	}
	public Date getDataRegolamento() {
		return dataRegolamento;
	}
	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}
	public long getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(long numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public double getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(double importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte[] xml) {
		this.xml = xml;
	}
	
	// Business
	private Dominio dominio;
	private Psp psp;
	
	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(dominio == null){
			dominio = AnagraficaManager.getDominio(bd, idDominio);
		}
		return dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public Psp getPsp(BasicBD bd) throws ServiceException {
		if(psp == null){
			psp = AnagraficaManager.getPsp(bd, idPsp);
		}
		return psp;
	}
	public void setPsp(Psp psp) {
		this.psp = psp;
	}
	public String getCodBicRiversamento() {
		return codBicRiversamento;
	}
	public void setCodBicRiversamento(String codBicRiversamento) {
		this.codBicRiversamento = codBicRiversamento;
	}

	
	
}
