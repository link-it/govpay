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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fr extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	public enum StatoFr {
		ACCETTATA,
		ANOMALA
	}
	
	private Long id;
	private String codPsp;
	private String codDominio;
	private String codFlusso;
	private StatoFr stato;
	private String iur;
	private String codBicRiversamento;
	private Date dataFlusso;
	private Date dataRegolamento;
	private Date dataAcquisizione;
	private long numeroPagamenti;
	private BigDecimal importoTotalePagamenti;
	private byte[] xml;
	private List<Anomalia> anomalie;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
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
		return marshall(getAnomalie());
	}
	public void setDescrizioneStato(String descrizioneStato) {
		if(descrizioneStato != null)
			this.anomalie = unmarshall(descrizioneStato);
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
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
	public BigDecimal getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(BigDecimal importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte[] xml) {
		this.xml = xml;
	}
	
	public String getCodBicRiversamento() {
		return codBicRiversamento;
	}
	public void setCodBicRiversamento(String codBicRiversamento) {
		this.codBicRiversamento = codBicRiversamento;
	}
	public Date getDataAcquisizione() {
		return dataAcquisizione;
	}
	public void setDataAcquisizione(Date dataAcquisizione) {
		this.dataAcquisizione = dataAcquisizione;
	}
	
	public class Anomalia {
		String codAnomalia;
		String descrizione;
		
		public String getCodice(){
			return codAnomalia;
		}
		
		public String getDescrizione(){
			return descrizione;
		}
	}
	
	public List<Anomalia> getAnomalie() {
		if(anomalie == null)
			anomalie = new ArrayList<Anomalia>();
		return anomalie;
	}
	
	public void addAnomalia(String codAnomalia, String descrizione) {
		Anomalia a = new Anomalia();
		a.codAnomalia = codAnomalia;
		a.descrizione = descrizione;
		getAnomalie().add(a);
	}
	
	private String marshall(List<Anomalia> anomalie) {
		if(anomalie == null || anomalie.size() == 0) return null;
		StringBuffer sb = new StringBuffer();
		
		for(Anomalia a : anomalie){
			sb.append(a.codAnomalia);
			sb.append("#");
			sb.append(a.descrizione);
			sb.append("|");
		}
		
		// Elimino l'ultimo pipe
		String txt = sb.toString();
		return txt.substring(0, txt.length()-1);
	}
	
	private List<Anomalia> unmarshall(String anomalie) {
		List<Anomalia> list = new ArrayList<Anomalia>();
		
		if(anomalie == null || anomalie.isEmpty()) return list;
		
		String[] split = anomalie.split("\\|");
		for(String s : split){
			String[] split2 = s.split("#");
			Anomalia a = new Anomalia();
			a.codAnomalia = split2[0];
			a.descrizione = split2[1];
			list.add(a);
		}
		return list;
	}
}
