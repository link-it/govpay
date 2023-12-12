/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPendenzaDTO extends BasicCreateRequestDTO {


	public LeggiPendenzaDTO(Authentication user) {
		super(user);
	}

	private String codA2A;
	private String codPendenza;
	private String idDominio;
	private String numeroAvviso;
	private boolean infoIncasso = false;
	private String identificativoCreazionePendenza;
	private Versamento versamentoFromSession = null;
	private boolean verificaAvviso = false;

	public String getCodA2A() {
		return this.codA2A;
	}
	public void setCodA2A(String codA2A) {
		this.codA2A = codA2A;
	}
	public String getCodPendenza() {
		return this.codPendenza;
	}
	public void setCodPendenza(String codPendenza) {
		this.codPendenza = codPendenza;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}
	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}
	public boolean isInfoIncasso() {
		return infoIncasso;
	}
	public void setInfoIncasso(boolean infoIncasso) {
		this.infoIncasso = infoIncasso;
	}
	public String getIdentificativoCreazionePendenza() {
		return identificativoCreazionePendenza;
	}
	public void setIdentificativoCreazionePendenza(String identificativoCreazionePendenza) {
		this.identificativoCreazionePendenza = identificativoCreazionePendenza;
	}
	public Versamento getVersamentoFromSession() {
		return versamentoFromSession;
	}
	public void setVersamentoFromSession(Versamento versamentoFromSession) {
		this.versamentoFromSession = versamentoFromSession;
	}
	public boolean isVerificaAvviso() {
		return verificaAvviso;
	}
	public void setVerificaAvviso(boolean verificaAvviso) {
		this.verificaAvviso = verificaAvviso;
	}
}
