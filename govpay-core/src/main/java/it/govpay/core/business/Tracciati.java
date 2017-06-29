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
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.bd.pagamento.filters.OperazioneFilter.SortFields;
import it.govpay.core.business.model.ElaboraTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiOperazioneDTO;
import it.govpay.core.business.model.LeggiOperazioneDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTO;
import it.govpay.core.business.model.LeggiTracciatoDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.tracciati.AcquisizioneUtils;
import it.govpay.core.utils.tracciati.TimerCaricamentoTracciatoSmistatore;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Tracciato.StatoTracciatoType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;


public class Tracciati extends BasicBD {

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciTracciatoDTOResponse inserisciTracciato(InserisciTracciatoDTO inserisciTracciatoDTO) throws NotAuthorizedException, InternalException {
		try {
			LogManager.getLogger().info("Inserimento tracciato");
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
			it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciati();
			LogManager.getLogger().info("Tracciato inserito con id: " + tracciato.getId());
			return inserisciTracciatoDTOResponse;
		} catch (ServiceException e) {
			LogManager.getLogger().error("Inserimento tracciato fallito", e);
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

		filter.addSortField(it.govpay.bd.pagamento.filters.TracciatoFilter.SortFields.DATA_CARICAMENTO, false);
		
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

	public LeggiOperazioneDTOResponse leggiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws NotAuthorizedException, ServiceException {
		try {
			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();

			OperazioniBD operazioniBD = new OperazioniBD(this);

			Operazione operazione = operazioniBD.getOperazione(leggiOperazioneDTO.getId());

			AcquisizioneUtils acquisizioneUtils = new AcquisizioneUtils();

			switch (operazione.getTipoOperazione()) {
			case ADD:
				CaricamentoRequest caricamentoRequest = (CaricamentoRequest) acquisizioneUtils.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
				AbstractOperazioneResponse abstractOperazioneResponse = acquisizioneUtils.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getDatiRisposta());
				CaricamentoResponse caricamentoResponse = (abstractOperazioneResponse instanceof CaricamentoResponse) ?  (CaricamentoResponse) abstractOperazioneResponse : null;

				OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);

				operazioneCaricamento.setAnagraficaDebitore(caricamentoRequest.getAnagraficaDebitore());
				operazioneCaricamento.setBundleKey(caricamentoRequest.getBundleKey());
				operazioneCaricamento.setCausale(caricamentoRequest.getCausale());
				operazioneCaricamento.setCfDebitore(caricamentoRequest.getCfDebitore());
				operazioneCaricamento.setCodDominio(caricamentoRequest.getCodDominio());
				operazioneCaricamento.setCodTributo(caricamentoRequest.getCodTributo());
				operazioneCaricamento.setIdDebito(caricamentoRequest.getIdDebito());
				operazioneCaricamento.setImporto(caricamentoRequest.getImporto());
				operazioneCaricamento.setNote(caricamentoRequest.getNote());
				operazioneCaricamento.setScadenza(caricamentoRequest.getScadenza());

				if(caricamentoResponse != null) {
					operazioneCaricamento.setIuv(caricamentoResponse.getIuv());
					operazioneCaricamento.setBarCode(caricamentoResponse.getBarCode());
					operazioneCaricamento.setQrCode(caricamentoResponse.getQrCode());
				}

				leggiOperazioneDTOResponse.setOperazione(operazioneCaricamento);
				break;
			case DEL:
				AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) acquisizioneUtils.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
				//AnnullamentoResponse annullamentoResponse = (AnnullamentoResponse) acquisizioneUtils.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getDatiRisposta());

				OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);

				operazioneAnnullamento.setMotivoAnnullamento(annullamentoRequest.getMotivoAnnullamento());

				leggiOperazioneDTOResponse.setOperazione(operazioneAnnullamento);
				break;
			case N_V:
			default:
				leggiOperazioneDTOResponse.setOperazione(operazione);
				break;
			}

			return leggiOperazioneDTOResponse;
		} catch (NotFoundException e) {
			return null;
		} 
	}


	public void elaboraTracciato(ElaboraTracciatoDTO elaboraTracciatoDTO) throws ServiceException {

		TracciatiBD tracciatiBD = new TracciatiBD(this);
		OperazioniBD operazioniBD = new OperazioniBD(this);
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		Date dataInizio = new Date();
		LogManager.getLogger().info("Caricamento tracciato ["+tracciato.getNomeFile()+"] iniziato alle ore: " + dataInizio);

		if(!tracciato.getStato().equals(StatoTracciatoType.IN_CARICAMENTO)) {
			tracciato.setLineaElaborazione(0);
			tracciato.setStato(StatoTracciatoType.IN_CARICAMENTO);
			tracciatiBD.updateTracciato(tracciato);
		}

		try {
			TimerCaricamentoTracciatoSmistatore smist = new TimerCaricamentoTracciatoSmistatore(this);
			List<List<List<byte[]>>> lstLst = splitCSV(tracciato);


			for(List<List<byte[]>> lst: lstLst) {
				int numLineeElaborate = smist.smista(lst, tracciato, tracciato.getLineaElaborazione());
				tracciato.setLineaElaborazione(tracciato.getLineaElaborazione() + numLineeElaborate);
				tracciatiBD.updateTracciato(tracciato);
			}

			OperazioneFilter operazioneFilter = operazioniBD.newFilter();
			operazioneFilter.setIdTracciato(tracciato.getId());
			operazioneFilter.addSortField(SortFields.LINEA, true);
			List<Operazione> operazioniInserite = operazioniBD.findAll(operazioneFilter);

			long countErrori = 0;
			long countOK = 0;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for(Operazione op: operazioniInserite) {

				if(op.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					countOK++;
				} else {
					countErrori++;
				}

				if(op.getDatiRisposta() != null)
					baos.write(op.getDatiRisposta());

				baos.write("\n".getBytes());
			}

			tracciato.setRawDataRisposta(baos.toByteArray());

			tracciato.setNumOperazioniKo(countErrori);
			if(countErrori > 0) {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_KO);
			} else {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_OK);
			}


			tracciato.setNumOperazioniOk(countOK);

		} catch(Exception e) {
			LogManager.getLogger().error("Errore durante l'inserimento del tracciato ["+tracciato.getId()+"]: " + e.getMessage(), e);
			tracciato.setDataUltimoAggiornamento(new Date());
		}

		tracciatiBD.updateTracciato(tracciato);
		Date dataFine = new Date();
		LogManager.getLogger().info("Caricamento tracciato ["+tracciato.getNomeFile()+"] completato in ["+((dataFine.getTime()-dataInizio.getTime()) / 1000)+"] secondi");
	}


	private List<List<List<byte[]>>> splitCSV(Tracciato tracciato) throws Exception {

		ByteArrayInputStream in = new ByteArrayInputStream(tracciato.getRawDataRichiesta());
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		br.skip(tracciato.getLineaElaborazione()); //skip prime n linee gia' lette

		List<List<List<byte[]>>> lst = new ArrayList<List<List<byte[]>>>();
		List<List<byte[]>> lstLst = new ArrayList<List<byte[]>>();
		List<byte[]> lstLstLst = new ArrayList<byte[]>(); 
		while(br.ready()) {
			lstLstLst.add(br.readLine().getBytes());
		}
		lstLst.add(lstLstLst);
		lst.add(lstLst);
		return lst;
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


