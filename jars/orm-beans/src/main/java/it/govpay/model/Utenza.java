/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

public class Utenza extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TIPO_UTENZA { ANONIMO, CITTADINO, APPLICAZIONE, OPERATORE };
	
	protected Long id;
	protected String principal;
	protected String principalOriginale;
	protected boolean abilitato;
	protected List<Long> idTipiVersamento;
	protected boolean checkSubject;
	protected boolean autorizzazioneDominiStar;
	protected boolean autorizzazioneTipiVersamentoStar;
	protected List<String> ruoli;
	protected List<IdUnitaOperativa> idDominiUo;
	protected String password;
	
	public void removeIdDominiUo(Long idDominio) {
		if(this.idDominiUo != null && !this.idDominiUo.isEmpty()) {
			List<IdUnitaOperativa>  toRemove = new ArrayList<>();
			for (IdUnitaOperativa idUnitaOperativa : idDominiUo) {
				if(idUnitaOperativa.getIdDominio() != null && idUnitaOperativa.getIdDominio().longValue() == idDominio.longValue()) {
					toRemove.add(idUnitaOperativa);
				}
			}
			for (IdUnitaOperativa idUnitaOperativa : toRemove) {
				this.idDominiUo.remove(idUnitaOperativa);
			}
		}
	}
	
	public void removeIdDominiUo(Long idDominio, Long idUo) {
		if(this.idDominiUo != null && !this.idDominiUo.isEmpty()) {
			List<IdUnitaOperativa>  toRemove = new ArrayList<>();
			for (IdUnitaOperativa idUnitaOperativa : idDominiUo) {
				if(idUnitaOperativa.getIdDominio() != null && idUnitaOperativa.getIdDominio().longValue() == idDominio.longValue() && 
						( (idUo == null && idUnitaOperativa.getIdUnita() == null) || (idUo != null && idUnitaOperativa.getIdUnita() != null && idUnitaOperativa.getIdUnita().longValue() == idUo.longValue()) )
						) {
					toRemove.add(idUnitaOperativa);
				}
			}
			for (IdUnitaOperativa idUnitaOperativa : toRemove) {
				this.idDominiUo.remove(idUnitaOperativa);
			}
		}
	}
	
	public List<IdUnitaOperativa> getIdDominiUo() {
		return idDominiUo;
	}

	public void setIdDominiUo(List<IdUnitaOperativa> idDominiUo) {
		this.idDominiUo = idDominiUo;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipalOriginale() {
		return this.principalOriginale;
	}

	public void setPrincipalOriginale(String principalOriginale) {
		this.principalOriginale = principalOriginale;
	}

	public boolean isAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public boolean isCheckSubject() {
		return this.checkSubject;
	}

	public void setCheckSubject(boolean checkSubject) {
		this.checkSubject = checkSubject;
	}

	public boolean isAutorizzazioneDominiStar() {
		return autorizzazioneDominiStar;
	}

	public void setAutorizzazioneDominiStar(boolean autorizzazioneDominiStar) {
		this.autorizzazioneDominiStar = autorizzazioneDominiStar;
	}

	public boolean isAutorizzazioneTipiVersamentoStar() {
		return autorizzazioneTipiVersamentoStar;
	}

	public void setAutorizzazioneTipiVersamentoStar(boolean autorizzazioneTipiVersamentoStar) {
		this.autorizzazioneTipiVersamentoStar = autorizzazioneTipiVersamentoStar;
	}
	public List<String> getRuoli() {
		return this.ruoli;
	}
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
