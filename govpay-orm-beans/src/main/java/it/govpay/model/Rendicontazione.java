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

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Rendicontazione extends BasicModel {

	private static final long serialVersionUID = 1L;

	public enum EsitoRendicontazione {
		ESEGUITO(0), REVOCATO(3), ESEGUITO_SENZA_RPT(9);

		private int codifica;

		EsitoRendicontazione(int codifica) {
			this.codifica = codifica;
		}

		public int getCodifica() {
			return codifica;
		}

		public static EsitoRendicontazione toEnum(String codifica) throws ServiceException {
			return toEnum(Integer.parseInt(codifica));
		}

		public static EsitoRendicontazione toEnum(int codifica) throws ServiceException {
			for(EsitoRendicontazione p : EsitoRendicontazione.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per EsitoRendicontazione. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoRendicontazione.values()));
		}
	}
	
	public enum StatoRendicontazione {
		OK, ANOMALA, ALTRO_INTERMEDIARIO;
	}
	private Long id;
	private String iuv;
	private String iur;
	
	private BigDecimal importo;
	private Date data;
	private EsitoRendicontazione esito;
	private StatoRendicontazione stato;
	private List<Anomalia> anomalie;
	private long idFr;
	private Long idPagamento;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public EsitoRendicontazione getEsito() {
		return esito;
	}
	public void setEsito(EsitoRendicontazione esito) {
		this.esito = esito;
	}
	public StatoRendicontazione getStato() {
		return stato;
	}
	public void setStato(StatoRendicontazione stato) {
		this.stato = stato;
	}
	public long getIdFr() {
		return idFr;
	}
	public void setIdFr(long idFr) {
		this.idFr = idFr;
	}
	public Long getIdPagamento() {
		return idPagamento;
	}
	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
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
	
	public String getAnomalieString() {
		return marshall(getAnomalie());
	}
	
	public void setAnomalie(List<Anomalia> anomalie) {
		this.anomalie = anomalie;
	}
	
	public void setAnomalie(String anomalie) {
		this.anomalie = unmarshall(anomalie);
	}
	
	public void addAnomalia(String codAnomalia, String descrizione) {
		Anomalia a = new Anomalia();
		a.codAnomalia = codAnomalia;
		a.descrizione = descrizione;
		getAnomalie().add(a);
	}
	
	private String marshall(List<Anomalia> anomalie) {
		if(anomalie == null || anomalie.size() == 0) return "";
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
