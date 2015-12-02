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
package it.govpay.bd.model;

import java.util.Arrays;
import java.util.Date;

public class Esito extends EsitoBase {
	private static final long serialVersionUID = 1L;

	public enum StatoSpedizione {DA_SPEDIRE, SPEDITO}
	private StatoSpedizione statoSpedizione;
	private String dettaglioSpedizione;
	private Long tentativiSpedizione;
	private Date dataOraCreazione;
	private Date dataOraUltimaSpedizione;
	private Date dataOraProssimaSpedizione;
	private byte[] xml;
	
	public StatoSpedizione getStatoSpedizione() {
		return statoSpedizione;
	}
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
		this.statoSpedizione = statoSpedizione;
	}
	public String getDettaglioSpedizione() {
		return dettaglioSpedizione;
	}
	public void setDettaglioSpedizione(String dettaglioSpedizione) {
		this.dettaglioSpedizione = dettaglioSpedizione;
	}
	public Long getTentativiSpedizione() {
		return tentativiSpedizione;
	}
	public void setTentativiSpedizione(Long tentativiSpedizione) {
		this.tentativiSpedizione = tentativiSpedizione;
	}
	public Date getDataOraProssimaSpedizione() {
		return dataOraProssimaSpedizione;
	}
	public void setDataOraProssimaSpedizione(Date dataOraProssimaSpedizione) {
		this.dataOraProssimaSpedizione = dataOraProssimaSpedizione;
	}
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte[] xml) {
		this.xml = xml;
	}

	@Override
	public boolean equals(Object obj) {
		Esito esito = null;
		if(obj instanceof Esito) {
			esito = (Esito) obj;
		} else {
			return false;
		}

		boolean equal = 
				equals(idApplicazione, esito.getIdApplicazione()) &&
				equals(statoSpedizione, esito.getStatoSpedizione()) &&
				equals(iuv, esito.getIuv()) &&
				equals(codDominio, esito.getCodDominio()) &&
				equals(dettaglioSpedizione, esito.getDettaglioSpedizione()) &&
				equals(tentativiSpedizione, esito.getTentativiSpedizione()) &&
				equals(dataOraCreazione, esito.getDataOraCreazione()) &&
				equals(dataOraUltimaSpedizione, esito.getDataOraUltimaSpedizione()) &&
				equals(dataOraProssimaSpedizione, esito.getDataOraProssimaSpedizione()) &&
				Arrays.equals(xml, esito.getXml());

		return equal;
	}
	public Date getDataOraCreazione() {
		return dataOraCreazione;
	}
	public void setDataOraCreazione(Date dataOraCreazione) {
		this.dataOraCreazione = dataOraCreazione;
	}
	public Date getDataOraUltimaSpedizione() {
		return dataOraUltimaSpedizione;
	}
	public void setDataOraUltimaSpedizione(Date dataOraUltimaSpedizione) {
		this.dataOraUltimaSpedizione = dataOraUltimaSpedizione;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}


}
