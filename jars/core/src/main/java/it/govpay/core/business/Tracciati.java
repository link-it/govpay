/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.beans.tracciati.AnnullamentoPendenza;
import it.govpay.core.beans.tracciati.DettaglioTracciatoPendenzeEsito;
import it.govpay.core.beans.tracciati.EsitoOperazionePendenza;
import it.govpay.core.beans.tracciati.ModalitaAvvisaturaDigitale;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.beans.tracciati.TracciatoPendenzePost;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.business.model.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.business.model.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.AnnullamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.OperazioneFactory;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.orm.constants.StatoTracciatoType;

public class Tracciati extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Tracciati.class);

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public void elaboraTracciatoPendenze(ElaboraTracciatoDTO elaboraTracciatoDTO) throws ServiceException {

		boolean wasAutocommit = this.isAutoCommit();

		if(this.isAutoCommit()) {
			this.setAutoCommit(false);
		}

		TracciatiBD tracciatiBD = new TracciatiBD(this);
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		String codDominio = tracciato.getCodDominio(); 
		FORMATO_TRACCIATO formato = tracciato.getFormato();

		log.info("Avvio elaborazione tracciato "+formato+" [" + tracciato.getId() +"] per il Dominio ["+codDominio+"]");
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = null;
		ISerializer serializer = null;
		try {
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

			beanDati = (it.govpay.core.beans.tracciati.TracciatoPendenza) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.TracciatoPendenza.class);

			switch (formato) {
			case CSV:
				this._elaboraTracciatoCSV(tracciatiBD, tracciato, beanDati, serializer);
				break;
			case JSON:
				this._elaboraTracciatoJSON(tracciatiBD, tracciato, beanDati, serializer);
				break;
			case XML:
				throw new Exception("formato non supportato.");
			}
		} catch(Throwable e) {
			log.error("Errore durante l'elaborazione del tracciato "+formato+" ["+tracciato.getId()+"]: " + e.getMessage(), e);
			if(!this.isAutoCommit()) this.rollback();

			// aggiorno lo stato in errore altrimenti continua a ciclare
			tracciato.setStato(STATO_ELABORAZIONE.SCARTATO);
			String descrizioneStato = "Errore durante l'elaborazione del tracciato: " + e.getMessage();
			tracciato.setDescrizioneStato(descrizioneStato.length() > 256 ? descrizioneStato.substring(0, 255): descrizioneStato);
			tracciato.setDataCompletamento(new Date());
			if(beanDati != null) {
				beanDati.setStepElaborazione(StatoTracciatoType.ANNULLATO.getValue());
				beanDati.setLineaElaborazioneAdd(0);
				beanDati.setLineaElaborazioneDel(0);
				beanDati.setNumAddTotali(0);
				beanDati.setNumDelTotali(0);
				beanDati.setDescrizioneStepElaborazione(descrizioneStato);
				try {
					tracciato.setBeanDati(serializer.getObject(beanDati));
				} catch (IOException e1) {}
			}	
			tracciatiBD.updateFineElaborazione(tracciato);
			if(!this.isAutoCommit()) this.commit();	
		} finally {
			this.setAutoCommit(wasAutocommit);
		}
	}

	private void _elaboraTracciatoJSON(TracciatiBD tracciatiBD, Tracciato tracciato, it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, ISerializer serializer)
			throws ServiceException, ValidationException, IOException {
		String codDominio = tracciato.getCodDominio();
		FORMATO_TRACCIATO formato = tracciato.getFormato();

		TracciatoPendenzePost tracciatoPendenzeRequest = JSONSerializable.parse(new String(tracciato.getRawRichiesta()), TracciatoPendenzePost.class);

		List<PendenzaPost> inserimenti = tracciatoPendenzeRequest.getInserimenti();
		List<AnnullamentoPendenza> annullamenti = tracciatoPendenzeRequest.getAnnullamenti();

		if(beanDati.getStepElaborazione().equals(StatoTracciatoType.NUOVO.getValue())) {
			log.debug("Cambio stato del tracciato da NUOVO a IN_CARICAMENTO");
			beanDati.setStepElaborazione(StatoTracciatoType.IN_CARICAMENTO.getValue());
			beanDati.setLineaElaborazioneAdd(0);
			beanDati.setLineaElaborazioneDel(0);
			beanDati.setNumAddTotali(inserimenti != null ? inserimenti.size() : 0);
			beanDati.setNumDelTotali(annullamenti != null ? annullamenti.size() : 0);
			beanDati.setAvvisaturaAbilitata(tracciatoPendenzeRequest.AvvisaturaDigitale());

			ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale = tracciatoPendenzeRequest.getModalitaAvvisaturaDigitale();
			String modo = (modalitaAvvisaturaDigitale != null && modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.SINCRONA)) ? "S" : "A";
			beanDati.setAvvisaturaModalita(modo);

			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.update(tracciato);
			this.commit();
		}

		OperazioniBD operazioniBD = new OperazioniBD(this);
		OperazioneFactory factory = new OperazioneFactory();
		// eseguo operazioni add
		long numLinea = beanDati.getLineaElaborazioneAdd();

		log.debug("Elaboro le operazioni di caricamento del tracciato saltando le prime " + numLinea + " linee");
		for(long linea = numLinea; linea < beanDati.getNumAddTotali() ; linea ++) {
			PendenzaPost pendenzaPost = inserimenti.get((int) linea);
			String jsonPendenza = pendenzaPost.toJSON(null);

			it.govpay.core.dao.commons.Versamento versamentoToAdd = it.govpay.core.utils.TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);

			// inserisco l'identificativo del dominio
			versamentoToAdd.setCodDominio(codDominio);
			// inserisco le informazioni di avvisatura
			versamentoToAdd.setAvvisaturaAbilitata(beanDati.getAvvisaturaAbilitata());
			versamentoToAdd.setModoAvvisatura(beanDati.getAvvisaturaModalita()); 

			CaricamentoRequest request = new CaricamentoRequest();
			request.setCodApplicazione(pendenzaPost.getIdA2A());
			request.setCodVersamentoEnte(pendenzaPost.getIdPendenza());
			request.setVersamento(versamentoToAdd);
			request.setLinea(linea + 1);
			request.setOperatore(tracciato.getOperatore(this));

			CaricamentoResponse caricamentoResponse = factory.caricaVersamento(request, this);

			this.setAutoCommit(false);

			Operazione operazione = new Operazione();
			operazione.setCodVersamentoEnte(versamentoToAdd.getCodVersamentoEnte());
			operazione.setDatiRichiesta(jsonPendenza.getBytes());
			operazione.setDatiRisposta(caricamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
			operazione.setStato(caricamentoResponse.getStato());
			this.setDescrizioneEsito(caricamentoResponse, operazione);
			this.setApplicazione(caricamentoResponse, operazione);
			operazione.setIdTracciato(tracciato.getId());
			operazione.setLineaElaborazione(linea + 1);
			operazione.setTipoOperazione(TipoOperazioneType.ADD);
			operazione.setCodDominio(codDominio);
			operazioniBD.insertOperazione(operazione);

			this.aggiornaCountOperazioniAdd(beanDati, caricamentoResponse, operazione);				
			beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);	
			log.debug("Inserimento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
			beanDati.setDataUltimoAggiornamento(new Date());

			tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
			this.commit();

			BatchManager.aggiornaEsecuzione(this, Operazioni.BATCH_TRACCIATI);

		}

		// eseguo operazioni del
		numLinea = beanDati.getLineaElaborazioneDel();

		log.debug("Elaboro le operazioni di annullamento del tracciato saltando le prime " + numLinea + " linee");
		for(long linea = numLinea; linea < beanDati.getNumDelTotali() ; linea ++) {
			AnnullamentoPendenza annullamento = annullamenti.get((int) linea);
			AnnullamentoRequest request = new AnnullamentoRequest();
			request.setCodApplicazione(annullamento.getIdA2A());
			request.setCodVersamentoEnte(annullamento.getIdPendenza());
			request.setMotivoAnnullamento(annullamento.getMotivoAnnullamento());
			request.setLinea(beanDati.getNumAddTotali() + linea + 1);
			request.setOperatore(tracciato.getOperatore(this));

			AnnullamentoResponse annullamentoResponse = factory.annullaVersamento(request, this);

			this.setAutoCommit(false);

			Operazione operazione = new Operazione();
			operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
			String jsonPendenza = annullamento.toJSON(null);
			operazione.setDatiRichiesta(jsonPendenza.getBytes());
			operazione.setDatiRisposta(annullamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
			operazione.setStato(annullamentoResponse.getStato());
			this.setDescrizioneEsito(annullamentoResponse, operazione);
			this.setApplicazione(annullamentoResponse, operazione);

			operazione.setIdTracciato(tracciato.getId());
			// proseguo il conteggio delle linee sommandole a quelle delle operazioni di ADD
			operazione.setLineaElaborazione(beanDati.getNumAddTotali() + linea + 1);
			operazione.setTipoOperazione(TipoOperazioneType.DEL);
			operazione.setCodDominio(codDominio);
			operazioniBD.insertOperazione(operazione);

			this.aggiornaCountOperazioniDel(beanDati, annullamentoResponse, operazione);				
			beanDati.setLineaElaborazioneDel(beanDati.getLineaElaborazioneDel()+1);	
			log.debug("Annullamento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
			beanDati.setDataUltimoAggiornamento(new Date());

			tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
			this.commit();

			BatchManager.aggiornaEsecuzione(this, Operazioni.BATCH_TRACCIATI);

		}

		// Elaborazione completata. Processamento tracciato di esito
		DettaglioTracciatoPendenzeEsito esitoElaborazioneTracciato = this.getEsitoElaborazioneTracciato(tracciato, operazioniBD);
		this.setStatoDettaglioTracciato(beanDati);
		tracciato.setRawEsito(esitoElaborazioneTracciato.toJSON(null).getBytes());
		tracciato.setFileNameEsito("esito_" + tracciato.getFileNameRichiesta()); 
		this.setStatoTracciato(tracciato, beanDati);
		tracciato.setDataCompletamento(new Date());
		tracciato.setBeanDati(serializer.getObject(beanDati));
		//			tracciatiBD.update(tracciato);
		tracciatiBD.updateFineElaborazione(tracciato);

		if(!this.isAutoCommit()) this.commit();
		log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato());
	}

	private void _elaboraTracciatoCSV(TracciatiBD tracciatiBD, Tracciato tracciato, it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, ISerializer serializer)
			throws ServiceException, ValidationException, IOException, java.io.IOException {
		String codDominio = tracciato.getCodDominio();
		String codTipoVersamento = tracciato.getCodTipoVersamento();
		FORMATO_TRACCIATO formato = tracciato.getFormato();

		byte[] rawRichiesta = tracciato.getRawRichiesta();
		
		if(beanDati.getStepElaborazione().equals(StatoTracciatoType.NUOVO.getValue())) {
			log.debug("Cambio stato del tracciato da NUOVO a IN_CARICAMENTO");
			beanDati.setStepElaborazione(StatoTracciatoType.IN_CARICAMENTO.getValue());
			beanDati.setLineaElaborazioneAdd(1); // skip intestazione file csv
			beanDati.setLineaElaborazioneDel(0);
			long numLines = rawRichiesta != null ? CSVUtils.countLines2(rawRichiesta) : 0;
			log.debug("Numero linee totali compresa intestazione ["+numLines+"]");
			beanDati.setNumAddTotali(numLines > 0 ? (numLines -1) : 0);
			beanDati.setNumDelTotali(0);
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.update(tracciato);
			this.commit();
		}
		
		if(rawRichiesta == null) {
			throw new ValidationException("Il file CSV ricevuto e' vuoto.");
		}
		
		OperazioniBD operazioniBD = new OperazioniBD(this);
		OperazioneFactory factory = new OperazioneFactory();
		// eseguo operazioni add
		long numLinea = beanDati.getLineaElaborazioneAdd();
		log.debug("Elaboro le operazioni di caricamento del tracciato saltando le prime " + numLinea + " linee");
		List<byte[]> lst = CSVUtils.splitCSV(rawRichiesta, numLinea);
		log.debug("Lette " + lst.size() + " linee");

		TipoVersamentoDominio tipoVersamentoDominio = null;
		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(tracciatiBD, codDominio);
		} catch (NotFoundException e) {	
			throw new ValidationException("Dominio ["+codDominio+"] inesistente.");
		}
		try {
			tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(tracciatiBD, dominio.getId(), codTipoVersamento);
		} catch (NotFoundException e) {	
			throw new ValidationException("Tipo Versamento ["+codTipoVersamento+"] inesistente per il Dominio: ["+codDominio+"].");
		}

		TracciatoCsv tracciatoCsv = null;
		if(tipoVersamentoDominio != null) {
			if(tipoVersamentoDominio.getTracciatoCsvTipo() != null && tipoVersamentoDominio.getTracciatoCsvIntestazione() != null
					&& tipoVersamentoDominio.getTracciatoCsvRichiesta() != null && tipoVersamentoDominio.getTracciatoCsvRisposta() != null) {
				tracciatoCsv = new TracciatoCsv();
				tracciatoCsv.setTipo(tipoVersamentoDominio.getTracciatoCsvTipo());
				tracciatoCsv.setHeaderRisposta(tipoVersamentoDominio.getTracciatoCsvIntestazione());
				tracciatoCsv.setTrasformazioneRichiesta(tipoVersamentoDominio.getTracciatoCsvRichiesta());
				tracciatoCsv.setTrasformazioneRisposta(tipoVersamentoDominio.getTracciatoCsvRisposta());
			}
		}

		// configurazione di sistema
		if(tracciatoCsv == null)
			tracciatoCsv = new it.govpay.core.business.Configurazione(tracciatiBD).getConfigurazione().getTracciatoCsv();

		for(byte[] linea: lst) {

			CaricamentoRequest request = new CaricamentoRequest();
			// inserisco l'identificativo del dominio
			request.setCodDominio(codDominio);
			request.setCodTipoVersamento(codTipoVersamento);
			request.setTipoTemplateTrasformazioneRichiesta(tracciatoCsv.getTipo());
			request.setTemplateTrasformazioneRichiesta(tracciatoCsv.getTrasformazioneRichiesta());
			request.setDati(linea);
			request.setLinea(numLinea + 1);
			request.setOperatore(tracciato.getOperatore(this));
			// inserisco le informazioni di avvisatura
			request.setAvvisaturaAbilitata(beanDati.getAvvisaturaAbilitata());
			request.setAvvisaturaModalita(beanDati.getAvvisaturaModalita()); 

			CaricamentoResponse caricamentoResponse = factory.caricaVersamentoCSV(request, this);

			this.setAutoCommit(false);

			Operazione operazione = new Operazione();
			operazione.setCodVersamentoEnte(caricamentoResponse.getIdPendenza());
			operazione.setDatiRichiesta(caricamentoResponse.getJsonRichiesta().getBytes());
			operazione.setDatiRisposta(caricamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
			operazione.setStato(caricamentoResponse.getStato());
			this.setDescrizioneEsito(caricamentoResponse, operazione);
			this.setApplicazione(caricamentoResponse, operazione);
			operazione.setIdTracciato(tracciato.getId());
			operazione.setLineaElaborazione(numLinea + 1);
			operazione.setTipoOperazione(TipoOperazioneType.ADD);
			operazione.setCodDominio(codDominio);
			operazioniBD.insertOperazione(operazione);

			this.aggiornaCountOperazioniAdd(beanDati, caricamentoResponse, operazione);				
			beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);	
			log.debug("Inserimento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + new String(linea) + "]");
			beanDati.setDataUltimoAggiornamento(new Date());

			tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
			this.commit();

			BatchManager.aggiornaEsecuzione(this, Operazioni.BATCH_TRACCIATI);
			numLinea = numLinea + 1 ;
		}

		// Elaborazione completata. Processamento tracciato di esito
		this.setStatoDettaglioTracciato(beanDati);
		String esitoElaborazioneTracciatoCSV = this.getEsitoElaborazioneTracciatoCSV(tracciato, operazioniBD, dominio, codTipoVersamento, tracciatoCsv.getHeaderRisposta(), tracciatoCsv.getTipo(), tracciatoCsv.getTrasformazioneRisposta());

		//TODO togliere
		log.debug("Tracciato di esito[" + esitoElaborazioneTracciatoCSV+"]");

		tracciato.setRawEsito(esitoElaborazioneTracciatoCSV.getBytes());
		tracciato.setFileNameEsito("esito_" + tracciato.getFileNameRichiesta()); 
		this.setStatoTracciato(tracciato, beanDati);
		tracciato.setDataCompletamento(new Date());
		tracciato.setBeanDati(serializer.getObject(beanDati));
		//			tracciatiBD.update(tracciato);
		tracciatiBD.updateFineElaborazione(tracciato);

		if(!this.isAutoCommit()) this.commit();
		log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato());
	}

	private void aggiornaCountOperazioniAdd(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, CaricamentoResponse caricamentoResponse,
			Operazione operazione) {
		if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
			beanDati.setNumAddOk(beanDati.getNumAddOk()+1);
		} else {
			if(!caricamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY.toString())) {
				beanDati.setNumAddKo(beanDati.getNumAddKo()+1);
				beanDati.setDescrizioneStepElaborazione(caricamentoResponse.getDescrizioneEsito());
			}
		}
	}

	private void aggiornaCountOperazioniDel(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, AnnullamentoResponse annullamentoResponse,
			Operazione operazione) {
		if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
			beanDati.setNumDelOk(beanDati.getNumDelOk()+1);
		} else {
			if(!annullamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY)) {
				beanDati.setNumDelKo(beanDati.getNumDelKo()+1);
				beanDati.setDescrizioneStepElaborazione(annullamentoResponse.getDescrizioneEsito());
			}
		}
	}

	private void setStatoDettaglioTracciato(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati) {
		if((beanDati.getNumAddKo() + beanDati.getNumDelKo()) > 0) {
			beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_KO.getValue());
		} else {
			beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_OK.getValue());
		}
	}

	private void setStatoTracciato(Tracciato tracciato, it.govpay.core.beans.tracciati.TracciatoPendenza beanDati) {
		tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
		String descrizioneStato = beanDati.getDescrizioneStepElaborazione() != null ? beanDati.getDescrizioneStepElaborazione() : "";
		tracciato.setDescrizioneStato(descrizioneStato.length() > 256 ? descrizioneStato.substring(0, 255): descrizioneStato);
	}

	private void setApplicazione(AbstractOperazioneResponse caricamentoResponse, Operazione operazione) {
		try {
			operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, caricamentoResponse.getIdA2A()).getId());
		} catch(Exception e) {
			// CodApplicazione non censito in anagrafica.
		}
	}

	private void setDescrizioneEsito(AbstractOperazioneResponse response, Operazione operazione) {
		if(response.getDescrizioneEsito() != null)
			operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
	}

	public DettaglioTracciatoPendenzeEsito getEsitoElaborazioneTracciato(Tracciato tracciato, OperazioniBD operazioniBD)
			throws ServiceException, ValidationException {
		OperazioneFilter filter = operazioniBD.newFilter();
		filter.setIdTracciato(tracciato.getId());
		filter.setLimit(500);
		filter.setOffset(0);
		List<FilterSortWrapper> fsl = new ArrayList<>();
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setSortOrder(SortOrder.ASC);
		fsw.setField(it.govpay.orm.Operazione.model().LINEA_ELABORAZIONE); 
		fsl.add(fsw );
		filter.setFilterSortList(fsl);

		DettaglioTracciatoPendenzeEsito esitoElaborazioneTracciato = new DettaglioTracciatoPendenzeEsito();
		esitoElaborazioneTracciato.setIdTracciato(tracciato.getFileNameRichiesta()); 
		List<EsitoOperazionePendenza> esitiAnnullamenti = new ArrayList<>();
		List<EsitoOperazionePendenza> esitiInserimenti = new ArrayList<>();

		while(true) {
			// Ciclo finche' non mi ritorna meno record del limit. Altrimenti esco perche' ho finito
			List<Operazione> findAll = operazioniBD.findAll(filter);
			for(Operazione operazione : findAll) {
				switch (operazione.getTipoOperazione()) {
				case ADD:
					esitiInserimenti.add(EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())));
					break;
				case DEL:
					esitiAnnullamenti.add(EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())));
					break;
				case INC:
				case N_V:
				default:
					break;
				}
			}
			if(findAll.size() == 500) {
				filter.setOffset(filter.getOffset() + 500);
			} else {
				break;
			}
		}

		esitoElaborazioneTracciato.setAnnullamenti(esitiAnnullamenti);
		esitoElaborazioneTracciato.setInserimenti(esitiInserimenti);
		return esitoElaborazioneTracciato;
	}

	public String getEsitoElaborazioneTracciatoCSV(Tracciato tracciato, OperazioniBD operazioniBD, Dominio dominio, String codTipoVersamento, String headerRisposta, String tipoTemplate, String trasformazioneRisposta) throws ServiceException, ValidationException, java.io.IOException {
		OperazioneFilter filter = operazioniBD.newFilter();
		filter.setIdTracciato(tracciato.getId());
		filter.setLimit(500);
		filter.setOffset(0);
		List<FilterSortWrapper> fsl = new ArrayList<>();
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setSortOrder(SortOrder.ASC);
		fsw.setField(it.govpay.orm.Operazione.model().LINEA_ELABORAZIONE); 
		fsl.add(fsw );
		filter.setFilterSortList(fsl );

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		BufferedWriter bw = new BufferedWriter(pw);
		
		bw.write(headerRisposta);//.getBytes());
		if(!headerRisposta.endsWith("\n"))
			bw.newLine();//("\n".getBytes());

		if(trasformazioneRisposta.startsWith("\""))
			trasformazioneRisposta = trasformazioneRisposta.substring(1);

		if(trasformazioneRisposta.endsWith("\""))
			trasformazioneRisposta = trasformazioneRisposta.substring(0, trasformazioneRisposta.length() - 1);

		byte[] template = Base64.getDecoder().decode(trasformazioneRisposta.getBytes());

		VersamentiBD versamentiBD = new VersamentiBD(operazioniBD);
		while(true) {
			// Ciclo finche' non mi ritorna meno record del limit. Altrimenti esco perche' ho finito
			List<Operazione> findAll = operazioniBD.findAll(filter);
			for(Operazione operazione : findAll) {
				switch (operazione.getTipoOperazione()) {
				case ADD:
					EsitoOperazionePendenza risposta = null;
					Applicazione applicazione =null;
					Versamento versamento = null;
					try {
						risposta = EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta()));
						applicazione = AnagraficaManager.getApplicazione(operazioniBD,risposta.getIdA2A());
						versamento = versamentiBD.getVersamento(applicazione.getId(), risposta.getIdPendenza());					
					} catch(NotFoundException e) {
					} catch(Exception e) {

					}


					// trasformare il json in csv String trasformazioneOutputCSV = 
					try {
						trasformazioneOutputCSV(log, bw, dominio.getCodDominio(), codTipoVersamento, tipoTemplate,
								new String(operazione.getDatiRisposta()), template, headerRisposta, dominio, applicazione, versamento, operazione.getStato().toString(), operazione.getDettaglioEsito());
						
//						if(trasformazioneOutputCSV.endsWith("\n")) {
//							log.debug("AAAAA");
//						}
						
//						baos.write(trasformazioneOutputCSV.getBytes());
//						if(trasformazioneOutputCSV != null && trasformazioneOutputCSV.length > 0 && trasformazioneOutputCSV[trasformazioneOutputCSV.length-1] != '\n') {
//							baos.write("\n".getBytes());
//						}
					} catch (GovPayException e) {
						bw.write(("Pendenza [IdA2A:"+risposta.getIdA2A()+", Id:"+risposta.getIdPendenza()+"] inserita con esito '"
								+ (operazione.getStato()) +"': scrittura dell'esito sul file csv conclusa con con errore.\n"));//.getBytes());
//						throw new ServiceException(e);
					}
					// esitiInserimenti.add(EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())));
					break;
				case DEL:
				case INC:
				case N_V:
				default:
					break;
				}
			}
			if(findAll.size() == 500) {
				filter.setOffset(filter.getOffset() + 500);
			} else {
				break;
			}
		}

		bw.flush();
		bw.close();
		return baos.toString();
	}

	public LeggiOperazioneDTOResponse fillOperazione(Operazione operazione) throws ServiceException {
		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();

		switch (operazione.getTipoOperazione()) {
		case ADD:
			VersamentiBD versamentiBD = new VersamentiBD(this);
			OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);
			try {
				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					Versamento versamento = versamentiBD.getVersamento(operazione.getIdApplicazione(), operazione.getCodVersamentoEnte());
					versamento.getSingoliVersamenti(this);
					versamento.getDominio(this);
					versamento.getUo(this);
					versamento.getApplicazione(this);
					versamento.getIuv(this);
					operazioneCaricamento.setVersamento(versamento);
				}
			}catch(NotFoundException e) {
				// do nothing
			}
			operazioneCaricamento.getApplicazione(this);
			try {
				operazioneCaricamento.getDominio(this);
			}catch(NotFoundException e) {
				// do nothing
			}

			leggiOperazioneDTOResponse.setOperazione(operazioneCaricamento);
			break;
		case DEL:
			OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);
			try {
				AnnullamentoPendenza annullamentoP = AnnullamentoPendenza.parse(new String(operazione.getDatiRichiesta()));
				operazioneAnnullamento.setMotivoAnnullamento(annullamentoP.getMotivoAnnullamento());

				operazioneAnnullamento.getApplicazione(this);
				try {
					operazioneAnnullamento.getDominio(this);
				} catch (NotFoundException e1) {
				}
			}catch(ValidationException e){

			}
			leggiOperazioneDTOResponse.setOperazione(operazioneAnnullamento);
			break;
		case N_V:
		default:
			leggiOperazioneDTOResponse.setOperazione(operazione);
			break;
		}

		return leggiOperazioneDTOResponse;
	}


	public static String trasformazioneInputCSV(Logger log, String codDominio, String codTipoVersamento, String lineaCSV, String tipoTemplate, String trasformazioneRichiesta) throws GovPayException {
		log.debug("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker ...");
		String name = "TrasformazionePendenzaCSVtoJSON";
		try {
			if(trasformazioneRichiesta.startsWith("\""))
				trasformazioneRichiesta = trasformazioneRichiesta.substring(1);

			if(trasformazioneRichiesta.endsWith("\""))
				trasformazioneRichiesta = trasformazioneRichiesta.substring(0, trasformazioneRichiesta.length() - 1);

			byte[] template = Base64.getDecoder().decode(trasformazioneRichiesta.getBytes());
			
			//log.debug("Template: "+ new String(template) );
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapRichiestaTracciatoCSV(log, dynamicMap, ContextThreadLocal.get(), lineaCSV, codDominio, codTipoVersamento);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , baos );
			// assegno il json trasformato
			log.debug("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker completata con successo.");

			// TODO togliere
			log.debug(baos.toString());

			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}

	public static void trasformazioneOutputCSV(Logger log, BufferedWriter bw, String codDominio, String codTipoVersamento, String jsonEsito, String tipoTemplate, byte[] template,
			String headerRisposta, Dominio dominio, Applicazione applicazione, Versamento versamento, String esitoOperazione, String descrizioneEsitoOperazione) throws GovPayException {
		log.debug("Trasformazione esito caricamento pendenza in formato JSON -> CSV tramite template freemarker ...");
		String name = "TrasformazionePendenzaJSONtoCSV";
		try {

//			 ByteArrayOutputStream	baos = new ByteArrayOutputStream();

			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapRispostaTracciatoCSV(log, dynamicMap, ContextThreadLocal.get(), 
					headerRisposta, jsonEsito, codDominio, codTipoVersamento, dominio, applicazione, versamento, esitoOperazione, descrizioneEsitoOperazione);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , bw );
			// assegno il json trasformato
			log.debug("Trasformazione esito caricamento pendenza JSON -> CSV tramite template freemarker completata con successo.");

			// TODO togliere
//			log.debug("Linea ["+ baos.toString()+ "]");

//			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Trasformazione esito caricamento pendenza JSON -> CSV tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}
}
