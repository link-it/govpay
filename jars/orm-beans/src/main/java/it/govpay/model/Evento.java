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
import org.openspcoop2.generic_project.exception.ServiceException;

public class Evento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idVersamento;
	private Long idPagamentoPortale;
	private String codDominio;
	private String iuv;
	private String ccp;
	private CategoriaEvento categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Long intervallo;
	private Date data;
	private String dettaglio;
	private String classnameDettaglio;
	
	public enum CategoriaEvento {
		INTERNO ("B"), INTERFACCIA_COOPERAZIONE ("C"), INTERFACCIA_INTEGRAZIONE ("I"), UTENTE ("U");
		
		private String codifica;

		CategoriaEvento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static CategoriaEvento toEnum(String codifica) throws ServiceException {
			
			for(CategoriaEvento p : CategoriaEvento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per CategoriaEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(CategoriaEvento.values()));
		}
		
		@Override
		public String toString() {
			return String.valueOf(this.codifica);
		}
	}
	
	public Evento() {
		this.categoriaEvento = CategoriaEvento.INTERFACCIA_COOPERAZIONE;
		this.data  = new Date();
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
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public CategoriaEvento getCategoriaEvento() {
		return this.categoriaEvento;
	}
	public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}
	public String getSottotipoEvento() {
		return this.sottotipoEvento;
	}
	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}

	public Long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public Long getIdPagamentoPortale() {
		return idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Long getIntervallo() {
		return intervallo;
	}

	public void setIntervallo(Long intervallo) {
		this.intervallo = intervallo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}

	public String getClassnameDettaglio() {
		return classnameDettaglio;
	}

	public void setClassnameDettaglio(String classnameDettaglio) {
		this.classnameDettaglio = classnameDettaglio;
	}
}
