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
package it.govpay.pagamento.utils.validazione.semantica;

import java.time.LocalDate;
import java.util.Date;

import it.govpay.core.exceptions.ValidationException;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.validator.ValidatorFactory;

public class NuovoPagamentoValidator {
	private ValidatorFactory vf;
	public NuovoPagamentoValidator() {
		this.vf = ValidatorFactory.newInstance();
	}

	public void valida(PagamentiPortaleDTO pagamentiPortaleDTO) throws UnprocessableEntityException {
		this.validaDataEsecuzionePagamento(pagamentiPortaleDTO.getDataEsecuzionePagamento());
	}
	
	public void validaDataEsecuzionePagamento(Date dataEsecuzionePagamento) throws UnprocessableEntityException {
		try {
			vf.getValidator("dataEsecuzionePagamento", dataEsecuzionePagamento).after(LocalDate.now()).insideDays(30);
		} catch (ValidationException e) {
			throw new UnprocessableEntityException(e.getMessage());
		}
	}
}
