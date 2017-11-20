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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.OperazioneIncasso;
import it.govpay.bd.model.OperazioneIncasso.SingoloIncasso;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.AvvisiPagamentoBD;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.AvvisoPagamentoFilter;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.model.ElaboraTracciatoDTO;
import it.govpay.core.business.model.InserisciAvvisoDTO;
import it.govpay.core.business.model.InserisciAvvisoDTOResponse;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiOperazioneDTO;
import it.govpay.core.business.model.LeggiOperazioneDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTO;
import it.govpay.core.business.model.LeggiTracciatoDTOResponse;
import it.govpay.core.business.model.ListaOperazioniDTO;
import it.govpay.core.business.model.ListaOperazioniDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneRequest;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.IncassoRequest;
import it.govpay.core.utils.tracciati.operazioni.IncassoResponse;
import it.govpay.core.utils.tracciati.operazioni.OperazioneFactory;
import it.govpay.core.utils.tracciati.operazioni.SingoloIncassoResponse;
import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.model.Tracciato.TipoTracciatoType;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;


public class Tracciati extends BasicBD {

	private static Logger log = LogManager.getLogger();;

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciTracciatoDTOResponse inserisciTracciato(InserisciTracciatoDTO inserisciTracciatoDTO) throws NotAuthorizedException, InternalException {

		try {
			log.info("Inserimento tracciato con nome: " + inserisciTracciatoDTO.getNomeTracciato());
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
			try {
				tracciato.setNumLineeTotali(CSVUtils.countLines(inserisciTracciatoDTO.getTracciato()));
			} catch(Exception e){
				tracciato.setNumLineeTotali(Long.MAX_VALUE);
			}
			tracciato.setStato(StatoTracciatoType.NUOVO);
			tracciato.setTipoTracciato(inserisciTracciatoDTO.getTipo()); 
			
			tracciatiBd.insertTracciato(tracciato);

			inserisciTracciatoDTOResponse.setTracciato(tracciato);
			it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciati();
			log.info("Tracciato inserito con id: " + tracciato.getId());
			return inserisciTracciatoDTOResponse;
		} catch (ServiceException e) {
			log.error("Inserimento tracciato fallito", e);
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
		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();

		OperazioniBD operazioniBD = new OperazioniBD(this);

		Operazione operazione = operazioniBD.getOperazione(leggiOperazioneDTO.getId());

		leggiOperazioneDTOResponse.setOperazione(fillOperazione(operazione));
		return leggiOperazioneDTOResponse;
	}

	public ListaOperazioniDTOResponse listaOperazioni(ListaOperazioniDTO listaOperazioniDTO) throws NotAuthorizedException, ServiceException {
		ListaOperazioniDTOResponse listaOperazioniDTOResponse = new ListaOperazioniDTOResponse();

		OperazioniBD operazioniBD = new OperazioniBD(this);
		OperazioneFilter filter = operazioniBD.newFilter(false);
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setField(it.govpay.orm.Operazione.model().LINEA_ELABORAZIONE);
		fsw.setSortOrder(SortOrder.ASC);
		filter.getFilterSortList().add(fsw);
		filter.setTipoOperazione(listaOperazioniDTO.getTipo());
		filter.setStato(listaOperazioniDTO.getStato()); 
		filter.setIdTracciato(listaOperazioniDTO.getIdTracciato());

		List<Operazione> findAll = operazioniBD.findAll(filter);

		if(findAll != null && findAll.size() > 0){
			List<Operazione> retList = new ArrayList<Operazione>();
			for (Operazione operazione : findAll) {
				retList.add(fillOperazione(operazione));
			}

			listaOperazioniDTOResponse.setOperazioni(retList);
		}

		return listaOperazioniDTOResponse;
	}

	private Operazione fillOperazione(Operazione operazione) 
			throws ServiceException {
		OperazioneFactory factory = new OperazioneFactory();

		switch (operazione.getTipoOperazione()) {
		case ADD:
			CaricamentoRequest caricamentoRequest = (CaricamentoRequest) factory.parseLineaOperazioneRequest(TipoTracciatoType.VERSAMENTI, operazione.getDatiRichiesta());
			AbstractOperazioneResponse abstractOperazioneResponse = factory.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getStato(), operazione.getDatiRisposta());
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
			operazioneCaricamento.setDebitoreIndirizzo(caricamentoRequest.getDebitoreIndirizzo());
			operazioneCaricamento.setDebitoreCivico(caricamentoRequest.getDebitoreCivico());
			operazioneCaricamento.setDebitoreCap(caricamentoRequest.getDebitoreCap());
			operazioneCaricamento.setDebitoreLocalita(caricamentoRequest.getDebitoreLocalita());
			operazioneCaricamento.setDebitoreProvincia(caricamentoRequest.getDebitoreProvincia());

			if(caricamentoResponse != null) {
				operazioneCaricamento.setIuv(caricamentoResponse.getIuv());
				operazioneCaricamento.setBarCode(caricamentoResponse.getBarCode());
				operazioneCaricamento.setQrCode(caricamentoResponse.getQrCode());
			}

			return operazioneCaricamento;
		case DEL:
			AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) factory.parseLineaOperazioneRequest(TipoTracciatoType.VERSAMENTI, operazione.getDatiRichiesta());
			OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);
			operazioneAnnullamento.setMotivoAnnullamento(annullamentoRequest.getMotivoAnnullamento());
			return operazioneAnnullamento;
		case INC: 
			IncassoRequest incassoRequest = (IncassoRequest) factory.parseLineaOperazioneRequest(TipoTracciatoType.INCASSI, operazione.getDatiRichiesta());
			AbstractOperazioneResponse abstractOperazioneResponse2 = factory.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getStato(), operazione.getDatiRisposta());
			IncassoResponse incassoResponse = (abstractOperazioneResponse2 instanceof IncassoResponse) ?  (IncassoResponse) abstractOperazioneResponse2 : null;
			operazione = inserisciInformazioniRelativeAlTipoOperazione(operazione, incassoRequest, incassoResponse);
			OperazioneIncasso operazioneIncasso = new OperazioneIncasso(operazione);
			
			operazioneIncasso.setCausale(incassoRequest.getCausale());
			operazioneIncasso.setDispositivo(incassoRequest.getDispositivo());
			operazioneIncasso.setDataContabile(incassoRequest.getDataContabile());
			operazioneIncasso.setDataValuta(incassoRequest.getDataValuta());
			operazioneIncasso.setImporto(incassoRequest.getImporto());

			if(incassoResponse != null) {
				operazioneIncasso.setFaultCode(incassoResponse.getFaultCode());
				operazioneIncasso.setFaultDescription(incassoResponse.getFaultDescription());
				operazioneIncasso.setFaultString(incassoResponse.getFaultString());
				operazioneIncasso.setDettaglioEsito(incassoResponse.getDescrizioneEsito());
				operazioneIncasso.setEsito(incassoResponse.getEsito());
				operazioneIncasso.setStato(incassoResponse.getStato());

				List<SingoloIncasso> listaSingoloIncasso = this.getListaSingoloincasso(incassoResponse.getLstSingoloIncasso());
				operazioneIncasso.setListaSingoloIncasso(listaSingoloIncasso);
			}
			
			return operazioneIncasso;
		case N_V:
		default:
			return operazione;
		}
	}


	private List<SingoloIncasso> getListaSingoloincasso(List<SingoloIncassoResponse> lstSingoloIncasso) {
		if(lstSingoloIncasso == null)
			return null;
		
		List<SingoloIncasso> lstRet = new ArrayList<OperazioneIncasso.SingoloIncasso>();
		for (SingoloIncassoResponse singoloIncassoResponse : lstSingoloIncasso) {
			SingoloIncasso sI = new OperazioneIncasso().new SingoloIncasso();
			sI.setCodSingoloVersamentoEnte(singoloIncassoResponse.getCodSingoloVersamentoEnte());
			sI.setCodVersamentoEnte(singoloIncassoResponse.getCodVersamentoEnte());
			sI.setCodDominio(singoloIncassoResponse.getDominio());
			sI.setIur(singoloIncassoResponse.getIur());
			sI.setIuv(singoloIncassoResponse.getIuv());
			sI.setImporto(singoloIncassoResponse.getImporto());
			sI.setDataPagamento(singoloIncassoResponse.getDataPagamento());
			sI.setTrn(singoloIncassoResponse.getTrn());
			
			lstRet.add(sI);
		}
		
		return lstRet;
	}

	public void elaboraTracciato(ElaboraTracciatoDTO elaboraTracciatoDTO) throws ServiceException {

		boolean wasAutocommit = this.isAutoCommit();

		if(this.isAutoCommit()) {
			this.setAutoCommit(false);
		}

		TracciatiBD tracciatiBD = new TracciatiBD(this);
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();

		log.info("Avvio elaborazione tracciato [" + tracciato.getId() +"]");

		if(tracciato.getStato().equals(StatoTracciatoType.NUOVO)) {
			log.debug("Cambio stato del tracciato da NUOVO a IN_CARICAMENTO");
			tracciato.setLineaElaborazione(0);
			tracciato.setStato(StatoTracciatoType.IN_CARICAMENTO);
			tracciatiBD.updateTracciato(tracciato);
			this.commit();
		}

		try {
			long numLinea = tracciato.getLineaElaborazione();

			log.debug("Leggo il tracciato saltando le prime " + numLinea + " linee");
			List<byte[]> lst = CSVUtils.splitCSV(tracciato.getRawDataRichiesta(), numLinea);
			log.debug("Lette " + lst.size() + " linee");

			AvvisoPagamento avvisoPagamentoBD = new AvvisoPagamento(this);
			OperazioniBD operazioniBD = new OperazioniBD(this);

			OperazioneFactory factory = new OperazioneFactory();

			for(byte[] linea: lst) {

				numLinea = numLinea + 1 ;

				// Elaboro l'operazione

				AbstractOperazioneRequest request = factory.acquisisci(tracciato.getTipoTracciato(), linea, tracciato.getId(), numLinea);
				AbstractOperazioneResponse response = factory.eseguiOperazione(request, tracciato, this);

				this.setAutoCommit(false);

				Operazione operazione = new Operazione();
				operazione.setDatiRichiesta(linea);
				operazione.setDatiRisposta(response.getDati());
				operazione.setStato(response.getStato());
				if(response.getDescrizioneEsito() != null)
					operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
				operazione.setIdTracciato(request.getIdTracciato());
				operazione.setLineaElaborazione(request.getLinea());
				operazione.setTipoOperazione(request.getTipoOperazione());
				
				operazione = this.inserisciInformazioniRelativeAlTipoOperazione(operazione, request, response);
				
				operazioniBD.insertOperazione(operazione);

				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					if(operazione.getTipoOperazione().equals(TipoOperazioneType.ADD)) {
						log.debug("Linea ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "] di tipo ADD: creazione Avviso Pagamento in corso...");
						Long idAvviso = this.inserisciAvvisoPagamento(operazione, request, response, avvisoPagamentoBD);
						if(idAvviso != null && idAvviso > 0)
							log.debug("Linea ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "] di tipo ADD: creazione Avviso Pagamento avvenuta correttamente.");
						else 
							log.debug("Linea ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "] di tipo ADD: creazione Avviso Pagamento non completata.");
					}

					tracciato.setNumOperazioniOk(tracciato.getNumOperazioniOk()+1);
				} else {
					if(!response.getEsito().equals(CostantiCaricamento.EMPTY.toString()))
						tracciato.setNumOperazioniKo(tracciato.getNumOperazioniKo()+1);
				}				
				tracciato.setLineaElaborazione(tracciato.getLineaElaborazione()+1);	
				log.debug("Linea ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + new String(linea) + "]");
				tracciato.setDataUltimoAggiornamento(new Date());

				tracciatiBD.updateTracciato(tracciato.getId(),tracciato.getStato(), tracciato.getLineaElaborazione(), tracciato.getNumOperazioniOk(), tracciato.getNumOperazioniKo());
				this.commit();

				BatchManager.aggiornaEsecuzione(this, Operazioni.batch_tracciati);
			}


			// Elaborazione completata. Processamento tracciato di esito

			OperazioneFilter filter = operazioniBD.newFilter();
			filter.setIdTracciato(tracciato.getId());
			filter.setLimit(500);
			filter.setOffset(0);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while(true) {

				// Ciclo finche' non mi ritorna meno record del limit. Altrimenti esco perche' ho finito

				List<Operazione> findAll = operazioniBD.findAll(filter);
				for(Operazione operazione : findAll) {
					baos.write(operazione.getDatiRisposta());
					baos.write("\n".getBytes());
				}
				if(findAll.size() == 500) {
					filter.setOffset(filter.getOffset() + 500);
				} else {
					break;
				}

			}

			if(tracciato.getNumOperazioniKo() > 0) {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_KO);
			} else {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_OK);
			}

			tracciato.setRawDataRisposta(baos.toByteArray());
			try {baos.flush();} catch(Exception e){}
			try {baos.close();} catch(Exception e){}

			tracciatiBD.updateTracciato(tracciato);
			if(!isAutoCommit()) this.commit();
			log.info("Elaborazione tracciato ["+tracciato.getId()+"] terminata: " + tracciato.getStato());
		} catch(Throwable e) {
			log.error("Errore durante l'elaborazione del tracciato ["+tracciato.getId()+"]: " + e.getMessage(), e);
			if(!isAutoCommit()) this.rollback();
		} finally {
			this.setAutoCommit(wasAutocommit);
		}
	}
	
	public void controllaStatoStampeTracciato(ElaboraTracciatoDTO elaboraTracciatoDTO) throws ServiceException {
		boolean wasAutocommit = this.isAutoCommit();

		if(this.isAutoCommit()) {
			this.setAutoCommit(false);
		}

		TracciatiBD tracciatiBD = new TracciatiBD(this);
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);

		log.info("Avvio controllo stato stampe avvisi per il tracciato [" + tracciato.getId() +"]");
		
		try {
			// 1. esecuzione count avvisi da stampare.
			AvvisoPagamentoFilter filter = avvisiBD.newFilter();
			filter.setIdTracciato(tracciato.getId());
			filter.setStato(StatoAvviso.DA_STAMPARE);
			filter.setTipoOperazione(TipoOperazioneType.ADD);
			filter.setStatoOperazione(StatoOperazioneType.ESEGUITO_OK);
			long countNumeroAvvisiDaStampare = avvisiBD.count(filter);
			log.info("Stato attuale delle stampe avvisi per il tracciato ["+tracciato.getId()+"] il tracciato ha: " + countNumeroAvvisiDaStampare +" avvisi da stampare.");
			
			if(countNumeroAvvisiDaStampare == 0) {
				tracciato.setStato(StatoTracciatoType.STAMPATO);
				tracciatiBD.updateTracciato(tracciato);
				if(!isAutoCommit()) this.commit();
			}
			log.info("Controllo stato stampe avvisi per il tracciato ["+tracciato.getId()+"] terminata: " + tracciato.getStato());
		} catch(Throwable e) {
			log.error("Errore durante il controllo stato stampe avvisi per il tracciato ["+tracciato.getId()+"]: " + e.getMessage(), e);
			if(!isAutoCommit()) this.rollback();
		} finally {
			this.setAutoCommit(wasAutocommit);
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

	public Long inserisciAvvisoPagamento(Operazione operazione,AbstractOperazioneRequest request, AbstractOperazioneResponse response, AvvisoPagamento avvisoPagamentoBD) throws Exception{
		if(request instanceof CaricamentoRequest && response instanceof CaricamentoResponse) {
			CaricamentoRequest caricamentoRequest = (CaricamentoRequest) request;
			CaricamentoResponse caricamentoResponse = (CaricamentoResponse) response;
			InserisciAvvisoDTO inserisciAvviso = new InserisciAvvisoDTO();
			inserisciAvviso.setCodDominio(caricamentoRequest.getCodDominio());
			inserisciAvviso.setIuv(caricamentoResponse.getIuv());
			inserisciAvviso.setDataCreazione(new Date());
			inserisciAvviso.setStato(StatoAvviso.DA_STAMPARE); 
			InserisciAvvisoDTOResponse inserisciAvvisoDTOResponse = avvisoPagamentoBD.inserisciAvviso(inserisciAvviso );
			return inserisciAvvisoDTOResponse.getAvviso().getId();
		}

		return null;
	}
	
	public Operazione inserisciInformazioniRelativeAlTipoOperazione(Operazione operazione,AbstractOperazioneRequest request, AbstractOperazioneResponse response) {
		// aggiungo dati relativi a codDominio iuv (nel caso ADD) codDominio trn (nel caso INC)
		if(operazione.getTipoOperazione().equals(TipoOperazioneType.ADD)) {
			if(request instanceof CaricamentoRequest && response instanceof CaricamentoResponse) {
				CaricamentoRequest caricamentoRequest = (CaricamentoRequest) request;
				CaricamentoResponse caricamentoResponse = (CaricamentoResponse) response;
				operazione.setCodDominio(caricamentoRequest.getCodDominio());
				operazione.setIuv(caricamentoResponse.getIuv());
				operazione.setCodVersamentoEnte(caricamentoRequest.getCodVersamentoEnte());
				
				try {
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, caricamentoRequest.getCodApplicazione()).getId());
				} catch(Exception e) {
					// CodApplicazione non censito in anagrafica.
				}
			}
		} else if(operazione.getTipoOperazione().equals(TipoOperazioneType.INC)) {
			if(request instanceof IncassoRequest) {
				IncassoRequest incassoRequest = (IncassoRequest) request;
				operazione.setCodDominio(incassoRequest.getDominio());
				operazione.setTrn(incassoRequest.getTrn());
			}
		} else {
			if(request instanceof AnnullamentoRequest) {
				AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) request;
				operazione.setCodVersamentoEnte(annullamentoRequest.getCodVersamentoEnte());
				try {
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, annullamentoRequest.getCodApplicazione()).getId());
				} catch(Exception e) {
					// CodApplicazione non censito in anagrafica.
				}

			}			 
		}
		
		return operazione;
	}
		 
}
