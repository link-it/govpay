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

/**
 * Rapppresenta un ente creditore
 */
public class Ente extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private long idDominio;
	private String codEnte;
	private boolean attivo;
	private Anagrafica anagraficaEnte;
	private Long idMailTemplateRPT;
	private Long idMailTemplateRT;
	private boolean invioMailRptAbilitato;
	private boolean invioMailRtAbilitato;
	
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	public Anagrafica getAnagraficaEnte() {
		return anagraficaEnte;
	}
	public void setAnagraficaEnte(Anagrafica anagraficaEnte) {
		this.anagraficaEnte = anagraficaEnte;
	}
	
	@Override
	public boolean equals(Object obj) {
		Ente ente = null;
		if(obj instanceof Ente) {
			ente = (Ente) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(anagraficaEnte, ente.getAnagraficaEnte()) &&
				equals(codEnte, ente.getCodEnte()) &&
				equals(idMailTemplateRPT, ente.getIdMailTemplateRPT()) &&
				equals(idMailTemplateRT, ente.getIdMailTemplateRT()) &&
				equals(codEnte, ente.getCodEnte()) &&
				idDominio == ente.getIdDominio() &&
				invioMailRptAbilitato == ente.isInvioMailRptAbilitato() &&
				invioMailRtAbilitato == ente.isInvioMailRtAbilitato() &&
				attivo == ente.isAttivo();
		
		return equal;
	}
	
	public Long getIdMailTemplateRT() {
		return idMailTemplateRT;
	}
	public void setIdMailTemplateRT(Long idTemplateRT) {
		this.idMailTemplateRT = idTemplateRT;
	}
	public Long getIdMailTemplateRPT() {
		return idMailTemplateRPT;
	}
	public void setIdMailTemplateRPT(Long idTemplateRPT) {
		this.idMailTemplateRPT = idTemplateRPT;
	}
	public boolean isInvioMailRtAbilitato() {
		return invioMailRtAbilitato;
	}
	public void setInvioMailRtAbilitato(boolean invioMailRtAbilitato) {
		this.invioMailRtAbilitato = invioMailRtAbilitato;
	}
	public boolean isInvioMailRptAbilitato() {
		return invioMailRptAbilitato;
	}
	public void setInvioMailRptAbilitato(boolean invioMailRptAbilitato) {
		this.invioMailRptAbilitato = invioMailRptAbilitato;
	}
	

}

