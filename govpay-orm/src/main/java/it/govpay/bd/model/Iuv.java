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

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.BasicModel;

public class Iuv extends BasicModel {
	
	public enum TipoIUV {
		ISO11694("I"), 
		NUMERICO("N");
		
		private String codifica;

		TipoIUV(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoIUV toEnum(String codifica) throws ServiceException {
			for(TipoIUV p : TipoIUV.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoIUV. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoIUV.values()));
		}
		
	}
	private static final long serialVersionUID = 1L;
	
	public static final int AUX_DIGIT = 0;
	
	private Long id;
	private long idApplicazione;
	private long idDominio;
	private long prg;
	private String iuv;
	private Date dataGenerazione;
	private TipoIUV tipo;
	private String codVersamentoEnte;
	private int applicationCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public long getPrg() {
		return prg;
	}
	public void setPrg(long prg) {
		this.prg = prg;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public Date getDataGenerazione() {
		return dataGenerazione;
	}
	public void setDataGenerazione(Date dataGenerazione) {
		this.dataGenerazione = dataGenerazione;
	}
	public long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}
	public TipoIUV getTipo() {
		return tipo;
	}
	public void setTipo(TipoIUV tipo) {
		this.tipo = tipo;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public int getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(int applicationCode) {
		this.applicationCode = applicationCode;
	}
	
}
