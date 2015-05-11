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
package it.govpay.orm.profilazione;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "proprieta_connettori")
@NamedQuery(name = "listaProprietaConnettore", query = "select p from ProprietaConnettore p where p.idConnettore = :idConnettore")
@IdClass(value = ProprietaConnettoreId.class)
public class ProprietaConnettore extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long idConnettore;
	private String nomeProperieta;
	private String valoreProprieta;

	public ProprietaConnettore() {
		super();
	}

	public ProprietaConnettore(Long idConnettore, String nomeProperieta, String valoreProprieta) {
		super();
		this.idConnettore = idConnettore;
		this.nomeProperieta = nomeProperieta;
		this.valoreProprieta = valoreProprieta;
	}

	@Id
	@Column(name = "ID_CONNETTORE")
	public Long getIdConnettore() {
		return idConnettore;
	}

	public void setIdConnettore(Long idConnettore) {
		this.idConnettore = idConnettore;
	}

	@Id
	@Column(name = "NOME_PROPRIETA")
	public String getNomeProperieta() {
		return nomeProperieta;
	}

	public void setNomeProperieta(String nomeProperieta) {
		this.nomeProperieta = nomeProperieta;
	}

	@Column(name = "VALORE_PROPRIETA")
	public String getValoreProprieta() {
		return valoreProprieta;
	}

	public void setValoreProprieta(String valoreProprieta) {
		this.valoreProprieta = valoreProprieta;
	}

}
