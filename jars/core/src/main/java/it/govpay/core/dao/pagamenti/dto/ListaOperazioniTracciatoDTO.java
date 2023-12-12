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
package it.govpay.core.dao.pagamenti.dto;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.Operazione;

public class ListaOperazioniTracciatoDTO extends BasicFindRequestDTO{

	private Long idTracciato;
	private StatoOperazioneType stato = null;
	private TipoOperazioneType tipo = null;
	
	public ListaOperazioniTracciatoDTO(Authentication user) {
		super(user);
		this.addDefaultSort(Operazione.model().LINEA_ELABORAZIONE,SortOrder.ASC);
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public StatoOperazioneType getStato() {
		return stato;
	}

	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public TipoOperazioneType getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperazioneType tipo) {
		this.tipo = tipo;
	}

	
}
