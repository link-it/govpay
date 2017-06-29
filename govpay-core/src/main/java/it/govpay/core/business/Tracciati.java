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

import it.govpay.bd.BasicBD;
import it.govpay.bd.loader.TracciatiBD;
import it.govpay.bd.loader.filters.TracciatoFilter;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTO;
import it.govpay.core.business.model.LeggiTracciatoDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;
import it.govpay.model.loader.Tracciato.StatoTracciatoType;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;


public class Tracciati extends BasicBD {

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciTracciatoDTOResponse inserisciTracciato(InserisciTracciatoDTO inserisciTracciatoDTO) throws NotAuthorizedException, InternalException {
		try {
			InserisciTracciatoDTOResponse inserisciTracciatoDTOResponse = new InserisciTracciatoDTOResponse();

			TracciatiBD tracciatiBd = new TracciatiBD(this);
			
			Tracciato tracciato = new Tracciato();
			
			tracciato.setDataCaricamento(new Date());
			tracciato.setDataUltimoAggiornamento(new Date());
			
			if(inserisciTracciatoDTO.getApplicazione() != null)
				tracciato.setIdApplicazione(inserisciTracciatoDTO.getApplicazione().getId());
			
			if(inserisciTracciatoDTO.getOperatore() != null)
				tracciato.setIdOperatore(inserisciTracciatoDTO.getOperatore().getId());
			
			tracciato.setNomeFile(inserisciTracciatoDTO.getNomeTracciato());
			tracciato.setRawDataRichiesta(inserisciTracciatoDTO.getTracciato());
			tracciato.setStato(StatoTracciatoType.NUOVO);
			tracciatiBd.insertTracciato(tracciato);

			inserisciTracciatoDTOResponse.setTracciato(tracciato);
			it.govpay.core.business.Operazioni.setForzaCaricamentoTracciati();
			return inserisciTracciatoDTOResponse;
		} catch (ServiceException e) {
			throw new InternalException(e);
		}
	}

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO) throws NotAuthorizedException, ServiceException {
		ListaTracciatiDTOResponse listaTracciatiDTOResponse = new ListaTracciatiDTOResponse();

		TracciatiBD tracciatiBd = new TracciatiBD(this);
		TracciatoFilter filter = tracciatiBd.newFilter();
		
		if(listaTracciatiDTO.getApplicazione() != null)
			filter.setIdApplicazione(listaTracciatiDTO.getApplicazione().getId());
		
		if(listaTracciatiDTO.getOperatore() != null)
			filter.setIdOperatore(listaTracciatiDTO.getOperatore().getId());
		
		if(listaTracciatiDTO.getFine() != null)
			filter.setDataUltimoAggiornamentoMax(listaTracciatiDTO.getFine());
		
		if(listaTracciatiDTO.getInizio() != null)
			filter.setDataUltimoAggiornamentoMin(listaTracciatiDTO.getInizio());
		
		filter.setOffset(listaTracciatiDTO.getOffset());
		filter.setLimit(listaTracciatiDTO.getLimit());
		
		List<Tracciato> tracciati = tracciatiBd.findAll(filter);
		listaTracciatiDTOResponse.setTracciati(tracciati);

		return listaTracciatiDTOResponse;
	}
	
	public LeggiTracciatoDTOResponse leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws NotAuthorizedException, ServiceException {
		try {
			LeggiTracciatoDTOResponse leggiTracciatoDTOResponse = new LeggiTracciatoDTOResponse();

			TracciatiBD tracciatiBd = new TracciatiBD(this);
			Tracciato tracciato = tracciatiBd.getTracciato(leggiTracciatoDTO.getId());

			if(leggiTracciatoDTO.getApplicazione() != null)
				authorizeByApplicazione(tracciato, leggiTracciatoDTO.getApplicazione());
			
			if(leggiTracciatoDTO.getOperatore() != null)
				authorizeByOperatore(tracciato, leggiTracciatoDTO.getOperatore());
			
			leggiTracciatoDTOResponse.setTracciato(tracciato);
			return leggiTracciatoDTOResponse;
		} catch (NotFoundException e) {
			return null;
		} 
	}

	/**
	 * @param tracciato 
	 * @param operatore
	 */
	private void authorizeByOperatore(Tracciato tracciato, Operatore operatore) throws NotAuthorizedException {
		if(tracciato.getIdOperatore() == null || !tracciato.getIdOperatore().equals(operatore.getId())) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param tracciato 
	 * @param applicazione
	 */
	private void authorizeByApplicazione(Tracciato tracciato, Applicazione applicazione) throws NotAuthorizedException {
		if(tracciato.getIdApplicazione() == null || !tracciato.getIdApplicazione().equals(applicazione.getId())) {
			throw new NotAuthorizedException();
		}
	}
}


