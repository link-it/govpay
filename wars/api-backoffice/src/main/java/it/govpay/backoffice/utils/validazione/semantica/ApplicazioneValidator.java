/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.utils.validazione.semantica;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import it.govpay.core.exceptions.ValidationException;

import it.govpay.bd.model.Applicazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

public class ApplicazioneValidator implements IValidable {
	
	private Applicazione applicazione;
	
	public ApplicazioneValidator(Applicazione applicazione ) {
		this.applicazione = applicazione;
	}

	@Override
	public void validate() throws ValidationException {
		
		if(StringUtils.isNotEmpty(this.applicazione.getRegExp())) {
			try {
				Pattern.compile(this.applicazione.getRegExp());
			}catch(java.util.regex.PatternSyntaxException e) {
				throw new ValidationException("Il campo regExpIuv non contiene un pattern valido.");
			}
		}
		
		if(StringUtils.isNotEmpty(this.applicazione.getCodApplicazioneIuv())) {
			ValidatorFactory vf = ValidatorFactory.newInstance();
			vf.getValidator("codificaIuv", this.applicazione.getCodApplicazioneIuv()).pattern("[0-9]{1,15}");
		}
	}
}
