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
/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Utenza;
import it.govpay.model.TipoVersamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 * 
 */
public class LeggiProfiloDTOResponse {

	private String nome;
	private Utenza utente;
	private List<Dominio> domini;
	private List<TipoVersamento> tipiVersamento;
	public Utenza getUtente() {
		return this.utente;
	}
	public void setUtente(Utenza utente) {
		this.utente = utente;
	}
	public List<Dominio> getDomini() {
		return this.domini;
	}
	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}
	public List<TipoVersamento> getTipiVersamento() {
		return tipiVersamento;
	}
	public void setTipiVersamento(List<TipoVersamento> tipiVersamento) {
		this.tipiVersamento = tipiVersamento;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
