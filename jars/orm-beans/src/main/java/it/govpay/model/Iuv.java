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

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;

import it.govpay.model.exception.CodificaInesistenteException;

public class Iuv extends BasicModel {
	
	public enum TipoIUV {
		ISO11694("I"), 
		NUMERICO("N");
		
		private String codifica;

		TipoIUV(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return this.codifica;
		}
		
		public static TipoIUV toEnum(String codifica) throws CodificaInesistenteException {
			for(TipoIUV p : TipoIUV.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new CodificaInesistenteException("Codifica inesistente per TipoIUV. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoIUV.values()));
		}
		
	}
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idApplicazione;
	private long idDominio;
	private long prg;
	private String iuv;
	private Date dataGenerazione;
	private TipoIUV tipo;
	private String codVersamentoEnte;
	private Integer applicationCode;
	private int auxDigit;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdApplicazione() {
		return this.idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public long getPrg() {
		return this.prg;
	}
	public void setPrg(long prg) {
		this.prg = prg;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public Date getDataGenerazione() {
		return this.dataGenerazione;
	}
	public void setDataGenerazione(Date dataGenerazione) {
		this.dataGenerazione = dataGenerazione;
	}
	public long getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}
	public TipoIUV getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoIUV tipo) {
		this.tipo = tipo;
	}
	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public int getApplicationCode() {
		return this.applicationCode;
	}
	public void setApplicationCode(int applicationCode) {
		this.applicationCode = applicationCode;
	}
	public int getAuxDigit() {
		return this.auxDigit;
	}
	public void setAuxDigit(int auxDigit) {
		this.auxDigit = auxDigit;
	}
}
