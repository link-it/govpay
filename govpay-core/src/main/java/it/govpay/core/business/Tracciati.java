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

import java.util.ArrayList;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTO;
import it.govpay.core.business.model.LeggiTracciatoDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.loader.Tracciato.StatoTracciatoType;


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

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO) throws NotAuthorizedException, ServiceException {
		ListaTracciatiDTOResponse inserisciTracciatoDTOResponse = new ListaTracciatiDTOResponse();
		Tracciato tracciato = new Tracciato();
		tracciato.setId(1);
		inserisciTracciatoDTOResponse.setTracciati(new ArrayList<Tracciato>());
		inserisciTracciatoDTOResponse.getTracciati().add(tracciato);
		return inserisciTracciatoDTOResponse;
	}
	
	public LeggiTracciatoDTOResponse leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws NotAuthorizedException, ServiceException {
		LeggiTracciatoDTOResponse leggiTracciatoDTOResponse = new LeggiTracciatoDTOResponse();
		Tracciato tracciato = new Tracciato();
		tracciato.setId(1);
		tracciato.setDataCaricamento(new Date());
		tracciato.setDataUltimoAggiornamento(new Date());
		tracciato.setIdApplicazione(leggiTracciatoDTO.getApplicazione().getId());
		tracciato.setLineaElaborazione(2);
		tracciato.setStato(StatoTracciatoType.CARICAMENTO_OK);
		tracciato.setNomeFile("testfile");
		tracciato.setNumLineeTotali(2);
		tracciato.setNumOperazioniKo(0);
		tracciato.setNumOperazioniOk(2);
		tracciato.setRawDataRichiesta("aaaaa".getBytes());
		tracciato.setRawDataRisposta("bbbbb".getBytes());
		leggiTracciatoDTOResponse.setTracciato(tracciato);
		return leggiTracciatoDTOResponse;
	}
}


