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
package it.govpay.core.business;

import java.util.List;
import java.util.regex.Pattern;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;

public class Applicazione {

	public it.govpay.bd.model.Applicazione getApplicazioneDominio(BDConfigWrapper configWrapper, Dominio dominio,String iuv) throws GovPayException, ServiceException {
		return getApplicazioneDominio(configWrapper, dominio, iuv, true);
	}
	
	public it.govpay.bd.model.Applicazione getApplicazioneDominio(BDConfigWrapper configWrapper, Dominio dominio,String iuv, boolean throwException) throws GovPayException, ServiceException {
	
		List<String> applicazioni = AnagraficaManager.getListaCodApplicazioni(configWrapper);
		
		for (String codApplicazione : applicazioni) {
			try {
				it.govpay.bd.model.Applicazione applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
				
				if(applicazione.getUtenza().isAutorizzazioneDominiStar() || applicazione.getUtenza().isIdDominioAutorizzato(dominio.getId())) {
					if(applicazione.getRegExp() != null) {
						Pattern pIuv = Pattern.compile(applicazione.getRegExp());
						if(pIuv.matcher(iuv).matches())
							return applicazione;
					}
				}
				
			} catch (NotFoundException e) {
				continue;
			}
		}
		
		if(throwException)
			throw new GovPayException(EsitoOperazione.APP_006, iuv, dominio.getCodDominio());
		 
		return null;
	}
	
	public void autorizzaApplicazione(String codApplicazione, it.govpay.bd.model.Applicazione applicazioneAutenticata, BDConfigWrapper configWrapper) throws GovPayException, ServiceException {
		it.govpay.bd.model.Applicazione applicazione = null;
		try {
			applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}

		if(!applicazione.getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);

		if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
			throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
	}
}
