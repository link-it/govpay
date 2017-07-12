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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.model.ElaboraLineaDTO;
import it.govpay.core.business.model.ElaboraLineaDTOResponse;
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
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneRequest;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Tracciato.StatoTracciatoType;


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
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		Date dataInizio = new Date();
		LogManager.getLogger().info("Caricamento tracciato ["+tracciato.getNomeFile()+"] iniziato alle ore: " + dataInizio);

		if(tracciato.getStato().equals(StatoTracciatoType.NUOVO)) {
			tracciato.setLineaElaborazione(0);
			tracciato.setStato(StatoTracciatoType.IN_CARICAMENTO);
			tracciatiBD.updateTracciato(tracciato);
		}

		ByteArrayOutputStream baos = null;
		try {
			List<List<byte[]>> lstLst = splitCSV(tracciato);


			long numLinea = tracciato.getLineaElaborazione();
			byte[] rawRispostaPrima = tracciato.getRawDataRisposta();
			baos = new ByteArrayOutputStream();
			if(rawRispostaPrima != null)
				baos.write(rawRispostaPrima);
			
			for(List<byte[]> lst: lstLst) {
				for(byte[] linea: lst) {
					ElaboraLineaDTO elaboraLineaDTO = new ElaboraLineaDTO();
					elaboraLineaDTO.setLinea(linea);
					elaboraLineaDTO.setNumLinea(numLinea++);
					elaboraLineaDTO.setTracciato(tracciato);
					ElaboraLineaDTOResponse elaboraLinea = this.elaboraLinea(elaboraLineaDTO);
					if(elaboraLinea.getOperazione().getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
						tracciato.setNumOperazioniOk(tracciato.getNumOperazioniOk()+1);
					} else {
						tracciato.setNumOperazioniKo(tracciato.getNumOperazioniKo()+1);
					}				
					
					if(elaboraLinea.getOperazione().getDatiRisposta() != null)
						baos.write(elaboraLinea.getOperazione().getDatiRisposta());
					baos.write("\n".getBytes());
					
					tracciato.setLineaElaborazione(tracciato.getLineaElaborazione() + 1);					
				}
				tracciato.setRawDataRisposta(baos.toByteArray());
				tracciatiBD.updateTracciato(tracciato);
				this.commit();
			}

			if(tracciato.getNumOperazioniKo() > 0) {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_KO);
			} else {
				tracciato.setStato(StatoTracciatoType.CARICAMENTO_OK);
			}
			tracciato.setNumLineeTotali(tracciato.getNumOperazioniOk()+tracciato.getNumOperazioniKo());

		} catch(Exception e) {
			LogManager.getLogger().error("Errore durante l'inserimento del tracciato ["+tracciato.getId()+"]: " + e.getMessage(), e);
		} finally {
			if(baos != null) {
				try {baos.flush();} catch(Exception e){}
				try {baos.close();} catch(Exception e){}
			}
		}

		tracciato.setDataUltimoAggiornamento(new Date());
		tracciatiBD.updateTracciato(tracciato);
		Date dataFine = new Date();
		LogManager.getLogger().info("Caricamento tracciato ["+tracciato.getNomeFile()+"] completato in ["+((dataFine.getTime()-dataInizio.getTime()) / 1000)+"] secondi");
	}


	public static List<List<byte[]>> splitCSV(Tracciato tracciato) throws Exception {

		ByteArrayInputStream in = new ByteArrayInputStream(tracciato.getRawDataRichiesta());
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		br.skip(tracciato.getLineaElaborazione()); //skip prime n linee gia' lette

		List<List<byte[]>> lstLst = new ArrayList<List<byte[]>>();
		List<byte[]> lst = new ArrayList<byte[]>(); 
		
		
		while(br.ready()) {
			if(lst.size() < 50) {
				lst.add(br.readLine().getBytes());
			} else { 
				List<byte[]> lstCopy = new ArrayList<byte[]>(); 
				lstCopy.addAll(lst);
				lstLst.add(lstCopy);
				lst = new ArrayList<byte[]>();
				lst.add(br.readLine().getBytes());
			}
		}
		lstLst.add(lst);
		return lstLst;
	}
	
	private static AcquisizioneUtils acquisizioneUtils = new AcquisizioneUtils();
	
	public ElaboraLineaDTOResponse elaboraLinea(ElaboraLineaDTO elaboraLineaDTO) throws ServiceException {

		ElaboraLineaDTOResponse elaboraLineaDTOResponse = new ElaboraLineaDTOResponse();
		AbstractOperazioneRequest request = Tracciati.acquisizioneUtils.acquisisci(elaboraLineaDTO.getLinea(), elaboraLineaDTO.getTracciato(), elaboraLineaDTO.getNumLinea());

		AbstractOperazioneResponse response = Tracciati.acquisizioneUtils.acquisisciResponse(request, elaboraLineaDTO.getTracciato(), this);
		String codApplicazione = Tracciati.acquisizioneUtils.getCodiceApplicazione(request);
		String codVersamentoEnte = Tracciati.acquisizioneUtils.getCodVersamentoEnte(request);
		
		OperazioniBD op = new OperazioniBD(this);
		Operazione operazione = new Operazione();
		operazione.setCodVersamentoEnte(codVersamentoEnte);
		operazione.setDatiRichiesta(elaboraLineaDTO.getLinea());
		operazione.setDatiRisposta(response.getDati());
		operazione.setStato(response.getStato());
		if(response.getDescrizioneEsito() != null)
			operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
		
		if(codApplicazione != null){
			try {
				operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, codApplicazione).getId());
			} catch(NotFoundException e) {
				throw new ServiceException("Applicazione ["+codApplicazione+"] non trovata");
			}
			
		}
			
		
		operazione.setIdTracciato(request.getIdTracciato());
		operazione.setLineaElaborazione(request.getLinea());
		operazione.setTipoOperazione(request.getTipoOperazione());
		op.insertOperazione(operazione);

		elaboraLineaDTOResponse.setOperazione(operazione);
		return elaboraLineaDTOResponse;
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


