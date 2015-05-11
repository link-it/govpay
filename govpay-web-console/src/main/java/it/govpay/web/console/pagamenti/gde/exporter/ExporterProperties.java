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
package it.govpay.web.console.pagamenti.gde.exporter;

import java.util.Date;

public class ExporterProperties {

	private boolean enableHeaderInfo;
	private boolean mimeThrowExceptionIfNotFound;
	private boolean abilitaMarcamentoTemporaleEsportazione;
	
	private Date dataInizio;
	private Date dataFine;
	
	private String formatoExport = null;
	
	
	public boolean isEnableHeaderInfo() {
		return this.enableHeaderInfo;
	}
	public boolean isMimeThrowExceptionIfNotFound() {
		return this.mimeThrowExceptionIfNotFound;
	}
	
	public void setEnableHeaderInfo(boolean enableHeaderInfo) {
		this.enableHeaderInfo = enableHeaderInfo;
	}
	public void setMimeThrowExceptionIfNotFound(boolean mimeThrowExceptionIfNotFound) {
		this.mimeThrowExceptionIfNotFound = mimeThrowExceptionIfNotFound;
	}
	public boolean isAbilitaMarcamentoTemporaleEsportazione() {
		return this.abilitaMarcamentoTemporaleEsportazione;
	}
	public void setAbilitaMarcamentoTemporaleEsportazione(
			boolean abilitaMarcamentoTemporaleEsportazione) {
		this.abilitaMarcamentoTemporaleEsportazione = abilitaMarcamentoTemporaleEsportazione;
	}
	public Date getDataInizio() {
		return this.dataInizio;
	}
	public Date getDataFine() {
		return this.dataFine;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public String getFormatoExport() {
		return formatoExport;
	}
	public void setFormatoExport(String formatoExport) {
		this.formatoExport = formatoExport;
	}
}
