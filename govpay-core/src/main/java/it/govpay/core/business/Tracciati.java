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
package it.govpay.core.business;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiTracciatiDTO;
import it.govpay.core.business.model.LeggiTracciatiDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.loader.Tracciato;


public class Tracciati extends BasicBD {

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciTracciatoDTOResponse inserisciTracciato(InserisciTracciatoDTO inserisciTracciatoDTO) throws NotAuthorizedException, InternalException {
		InserisciTracciatoDTOResponse inserisciTracciatoDTOResponse = new InserisciTracciatoDTOResponse();
		Tracciato tracciato = new Tracciato();
		tracciato.setId(1);
		inserisciTracciatoDTOResponse.setTracciato(tracciato);
		return inserisciTracciatoDTOResponse;
	}

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaIncassoDTO) throws NotAuthorizedException, ServiceException {
		return null;
	}
	
	public LeggiTracciatiDTOResponse leggiTracciato(LeggiTracciatiDTO leggiIncassoDTO) throws NotAuthorizedException, ServiceException {
		return null;
	}
}


