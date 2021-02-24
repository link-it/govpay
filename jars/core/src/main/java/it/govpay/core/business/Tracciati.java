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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
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
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.beans.tracciati.AnnullamentoPendenza;
import it.govpay.core.beans.tracciati.DettaglioTracciatoPendenzeEsito;
import it.govpay.core.beans.tracciati.EsitoOperazionePendenza;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.beans.tracciati.TracciatoPendenzePost;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.AnnullamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.OperazioneFactory;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.thread.CaricamentoTracciatoThread;
import it.govpay.core.utils.thread.CreaStampeTracciatoThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.core.utils.tracciati.TracciatiPendenzeManager;
import it.govpay.core.utils.tracciati.TracciatiUtils;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.constants.StatoTracciatoType;

public class Tracciati {

	private static Logger log = LoggerWrapperFactory.getLogger(Tracciati.class);

	public Tracciati() {
	}

	public void elaboraTracciatoPendenze(ElaboraTracciatoDTO elaboraTracciatoDTO, IContext ctx) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);

		TracciatiBD tracciatiBD = null;
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		String codDominio = tracciato.getCodDominio(); 
		FORMATO_TRACCIATO formato = tracciato.getFormato();

		log.info("Avvio elaborazione tracciato "+formato+" [" + tracciato.getId() +"] per il Dominio ["+codDominio+"]");
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = null;
		ISerializer serializer = null;
		try {
			tracciatiBD = new TracciatiBD(configWrapper);
			
			tracciatiBD.setupConnection(configWrapper.getTransactionID());
			
			tracciatiBD.setAtomica(false);
			
			tracciatiBD.setAutoCommit(false); 
			
			
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

			beanDati = (it.govpay.core.beans.tracciati.TracciatoPendenza) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.TracciatoPendenza.class);

			switch (formato) {
			case CSV:
				this._elaboraTracciatoCSV(tracciatiBD, tracciato, beanDati, serializer, ctx);
				break;
			case JSON:
				this._elaboraTracciatoJSON(tracciatiBD, tracciato, beanDati, serializer, ctx);
				break;
			case XML:
				throw new Exception("formato non supportato."); 
			}
		} catch(Throwable e) {
			log.error("Errore durante l'elaborazione del tracciato "+formato+" ["+tracciato.getId()+"]: " + e.getMessage(), e);
			tracciatiBD.rollback();

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
				beanDati.setDataUltimoAggiornamento(new Date());
				try {
					tracciato.setBeanDati(serializer.getObject(beanDati));
				} catch (IOException e1) {}
			}	
			tracciatiBD.updateFineElaborazione(tracciato);
			tracciatiBD.commit();	
		} finally {
			if(tracciatiBD != null) {
				tracciatiBD.closeConnection();
			}
		}
	}

	private void _elaboraTracciatoJSON(TracciatiBD tracciatiBD, Tracciato tracciato, it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, ISerializer serializer, IContext ctx)
			throws ServiceException, ValidationException, IOException, java.io.IOException {
		String codDominio = tracciato.getCodDominio();
		FORMATO_TRACCIATO formato = tracciato.getFormato();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
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
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.updateBeanDati(tracciato);
			tracciatiBD.commit();
		}

		OperazioniBD operazioniBD = new OperazioniBD(tracciatiBD);
		operazioniBD.setAtomica(false);
		
		OperazioneFactory factory = new OperazioneFactory();
		// eseguo operazioni add
		long numLinea = beanDati.getLineaElaborazioneAdd();

		if(tracciatiBD.isAutoCommit())
			tracciatiBD.setAutoCommit(false);

		log.debug("Elaboro le operazioni di caricamento del tracciato saltando le prime " + numLinea + " linee");
		for(long linea = numLinea; linea < beanDati.getNumAddTotali() ; linea ++) {
			PendenzaPost pendenzaPost = inserimenti.get((int) linea);
			String jsonPendenza = pendenzaPost.toJSON(null);

			it.govpay.core.dao.commons.Versamento versamentoToAdd = it.govpay.core.utils.TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);

			// inserisco l'identificativo del dominio
			versamentoToAdd.setCodDominio(codDominio);

			CaricamentoRequest request = new CaricamentoRequest();
			request.setCodApplicazione(pendenzaPost.getIdA2A());
			request.setCodVersamentoEnte(pendenzaPost.getIdPendenza());
			request.setVersamento(versamentoToAdd);
			request.setLinea(linea + 1);
			request.setOperatore(tracciato.getOperatore(configWrapper));
			request.setIdTracciato(tracciato.getId());

			CaricamentoResponse caricamentoResponse = factory.caricaVersamento(request, tracciatiBD);

			tracciatiBD.setAutoCommit(false);

			Operazione operazione = new Operazione();
			operazione.setCodVersamentoEnte(versamentoToAdd.getCodVersamentoEnte());
			operazione.setDatiRichiesta(jsonPendenza.getBytes());
			operazione.setDatiRisposta(caricamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
			operazione.setStato(caricamentoResponse.getStato());
			TracciatiUtils.setDescrizioneEsito(caricamentoResponse, operazione);
			TracciatiUtils.setApplicazione(caricamentoResponse, operazione, configWrapper);
			operazione.setIdTracciato(tracciato.getId());
			operazione.setLineaElaborazione(linea + 1);
			operazione.setTipoOperazione(TipoOperazioneType.ADD);
			operazione.setCodDominio(codDominio);
			operazione.setIdStampa(caricamentoResponse.getIdStampa());
			operazione.setIdVersamento(caricamentoResponse.getIdVersamento());
			operazioniBD.insertOperazione(operazione);

			TracciatiUtils.aggiornaCountOperazioniAdd(beanDati, caricamentoResponse, operazione);		
			beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);	
			log.debug("Inserimento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
			beanDati.setDataUltimoAggiornamento(new Date());

			tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
			tracciatiBD.commit();
			BatchManager.aggiornaEsecuzione(configWrapper, Operazioni.BATCH_TRACCIATI);

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
			request.setOperatore(tracciato.getOperatore(configWrapper));

			AnnullamentoResponse annullamentoResponse = factory.annullaVersamento(request, tracciatiBD);

			tracciatiBD.setAutoCommit(false);

			Operazione operazione = new Operazione();
			operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
			String jsonPendenza = annullamento.toJSON(null);
			operazione.setDatiRichiesta(jsonPendenza.getBytes());
			operazione.setDatiRisposta(annullamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
			operazione.setStato(annullamentoResponse.getStato());
			TracciatiUtils.setDescrizioneEsito(annullamentoResponse, operazione);
			TracciatiUtils.setApplicazione(annullamentoResponse, operazione, configWrapper);

			operazione.setIdTracciato(tracciato.getId());
			// proseguo il conteggio delle linee sommandole a quelle delle operazioni di ADD
			operazione.setLineaElaborazione(beanDati.getNumAddTotali() + linea + 1);
			operazione.setTipoOperazione(TipoOperazioneType.DEL);
			operazione.setCodDominio(codDominio);
			operazioniBD.insertOperazione(operazione);

			TracciatiUtils.aggiornaCountOperazioniDel(beanDati, annullamentoResponse, operazione);				
			beanDati.setLineaElaborazioneDel(beanDati.getLineaElaborazioneDel()+1);	
			log.debug("Annullamento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
			beanDati.setDataUltimoAggiornamento(new Date());

			tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
			tracciatiBD.commit();

			BatchManager.aggiornaEsecuzione(configWrapper, Operazioni.BATCH_TRACCIATI);

		}
		
		// Elaborazione completata. Processamento tracciato di esito
		TracciatiUtils.setStatoDettaglioTracciato(beanDati);
		DettaglioTracciatoPendenzeEsito esitoElaborazioneTracciato = this.getEsitoElaborazioneTracciato(tracciato, operazioniBD);

		//		log.debug("Tracciato di esito[" + esitoElaborazioneTracciatoCSV+"]");
		tracciato.setRawEsito(esitoElaborazioneTracciato.toJSON(null).getBytes());
		tracciato.setFileNameEsito("esito_" + tracciato.getFileNameRichiesta()); 
		
		if(beanDati.isStampaAvvisi()) {
			beanDati.setNumStampeTotali(beanDati.getNumAddOk()); // il numero di stampe che mi aspetto corrisponde al numero di pendenze caricate con esito ok
			beanDati.setNumStampeOk(0);
			beanDati.setNumStampeKo(0); 
			beanDati.setDataUltimoAggiornamento(new Date());
			
			tracciato.setStato(STATO_ELABORAZIONE.IN_STAMPA);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.updateFineElaborazione(tracciato);

			if(!tracciatiBD.isAutoCommit()) tracciatiBD.commit();
			log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato() + ", Creazione stampe avvisi...");
		
			// Tengo traccia degli avvisi inseriti nello zip per tenere solo l'ultima versione.
			Set<String> numeriAvviso = new HashSet<String>();
			Set<String> numeriDocumento = new HashSet<String>();
			
			IdTracciato idTracciato = new IdTracciato();
			idTracciato.setId(tracciato.getId());
			idTracciato.setIdTracciato(tracciato.getId());
	
			OutputStream oututStreamDestinazione = null;
			Long oid = null;
			Blob blobStampe = null;
	
			if(tracciatiBD.isAutoCommit())
				tracciatiBD.setAutoCommit(false);
	
			TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();
	
			switch (tipoDatabase) {
			case MYSQL:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case ORACLE:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case SQLSERVER:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case POSTGRESQL:
				org.openspcoop2.utils.datasource.Connection wrappedConn = (org.openspcoop2.utils.datasource.Connection) tracciatiBD.getConnection();
				Connection wrappedConnection = wrappedConn.getWrappedConnection();
	
				Connection underlyingConnection = null;
				try {
					Method method = wrappedConnection.getClass().getMethod("getUnderlyingConnection");
	
					Object invoke = method.invoke(wrappedConnection);
	
					underlyingConnection = (Connection) invoke;
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Errore durante la lettura dell'oggetto connessione: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
	
				org.postgresql.PGConnection pgConnection = null;
				try {
					if(underlyingConnection.isWrapperFor(org.postgresql.PGConnection.class)) {
						pgConnection = underlyingConnection.unwrap(org.postgresql.PGConnection.class);
					} else {
						pgConnection = (org.postgresql.PGConnection) underlyingConnection;				
					}
	
					// Get the Large Object Manager to perform operations with
					LargeObjectManager lobj = pgConnection.getLargeObjectAPI();
	
					// Create a new large object
					oid = lobj.createLO(LargeObjectManager.WRITE);
	
					// Open the large object for writing
					LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
	
					oututStreamDestinazione = obj.getOutputStream();
				} catch (SQLException e) {
					log.error("Errore durante la creazione dell'outputstream: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case DB2:
			case DEFAULT:
			case DERBY:
			case HSQL:
			default:
				throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
			}
	
			TracciatiBD tracciatiBeanDatiBD = null;
			try (ZipOutputStream zos = new ZipOutputStream(oututStreamDestinazione);) {
	
				int offset = 0;
				int limit = 500; 
				
				int sommaStampeOk = 0;
				int sommaStampeKo = 0;
	
				int stampePerThread = GovpayConfig.getInstance().getBatchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread();
	
				VersamentiBD versamentiBD = new VersamentiBD(tracciatiBD);
				versamentiBD.setAtomica(false);
	
				List<Versamento> versamentiDaStampare = versamentiBD.findVersamentiDiUnTracciato(tracciato.getId(), offset, limit);
				log.debug("Trovati ["+versamentiDaStampare.size()+"] Versamenti per cui stampare l'avviso");
	
				if(versamentiDaStampare.size() > 0) {
					tracciatiBeanDatiBD = new TracciatiBD(configWrapper);
					tracciatiBeanDatiBD.setupConnection(configWrapper.getTransactionID());
					tracciatiBeanDatiBD.setAtomica(false);
					tracciatiBeanDatiBD.setAutoCommit(false); 
					
					
					do {
						if(versamentiDaStampare.size() > 0) {
							List<CreaStampeTracciatoThread> threadsStampe = new ArrayList<CreaStampeTracciatoThread>();
	
							if(stampePerThread > versamentiDaStampare.size()) {
								CreaStampeTracciatoThread sender = new CreaStampeTracciatoThread(versamentiDaStampare, idTracciato, ("ThreadStampe_" + (threadsStampe.size() + 1)), ctx); 
								ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciatiStampeAvvisi().execute(sender);
								threadsStampe.add(sender);
							} else {
								for (int i = 0; i < versamentiDaStampare.size(); i += stampePerThread) {
									int end = Math.min(versamentiDaStampare.size(), i + stampePerThread);
	
									CreaStampeTracciatoThread sender = new CreaStampeTracciatoThread(versamentiDaStampare.subList(i, end), idTracciato, ("ThreadStampe_" + (threadsStampe.size() + 1)), ctx); 
									ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciatiStampeAvvisi().execute(sender);
									threadsStampe.add(sender);
								}
							}
	
							while(true){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
	
								}
								boolean completed = true;
								for(CreaStampeTracciatoThread sender : threadsStampe) {
									if(!sender.isCompleted()) {
										completed = false;
									} else {
										if(!sender.isCommit()) {
											sender.setCommit(true);
											synchronized (this) {
												List<PrintAvvisoDTOResponse> stampe = sender.getStampe();
												
												sommaStampeOk += sender.getStampeOk();
												sommaStampeKo += sender.getStampeKo();
			
												log.debug(sender.getNomeThread() + " ha eseguito ["+stampe.size()+"] stampe");
			
												for (PrintAvvisoDTOResponse stampa : stampe) {
													// inserisco l'eventuale pdf nello zip
													TracciatiUtils.aggiungiStampaAvviso(zos, numeriAvviso, numeriDocumento, stampa, log);
												}
												
												beanDati.setNumStampeOk(sommaStampeOk);
												beanDati.setNumStampeKo(sommaStampeKo);
												beanDati.setDataUltimoAggiornamento(new Date());
												
												log.debug("Aggiornamento delle informazioni progresso stampa...");
												tracciato.setBeanDati(serializer.getObject(beanDati));
												tracciatiBeanDatiBD.updateBeanDati(tracciato);
	
												if(!tracciatiBeanDatiBD.isAutoCommit()) tracciatiBeanDatiBD.commit();
												log.debug("Aggiornamento delle informazioni progresso stampa completato.");
											}
										}
									}
								}
	
								if(completed) { 
									log.debug("Completata Esecuzione dei ["+threadsStampe.size()+"] Threads di stampa");
									break; // esco
								}
							}
	
						}
	
						offset += limit;
						versamentiDaStampare = versamentiBD.findVersamentiDiUnTracciato(tracciato.getId(), offset, limit);
						log.debug("Trovati ["+versamentiDaStampare.size()+"] Versamenti per cui stampare l'avviso");
					} while (versamentiDaStampare.size() > 0);
				}
	
				if(numeriAvviso.isEmpty() && numeriDocumento.isEmpty()){ // non ho aggiunto neanche un pdf
					ZipEntry tracciatoOutputEntry = new ZipEntry("errore.txt");
					zos.putNextEntry(tracciatoOutputEntry);
					zos.write("Attenzione: non sono presenti inserimenti andati a buon fine nel tracciato selezionato.".getBytes());
					zos.flush();
					zos.closeEntry();
				}
	
				zos.flush();
				zos.close();
			} catch (java.io.IOException e) {
				log.error(e.getMessage(), e);
			}finally {
				if(tracciatiBeanDatiBD != null)
					tracciatiBeanDatiBD.closeConnection();
			}
			
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));

			this.salvaZipStampeTracciato(tracciatiBD, tracciato, oid, blobStampe, tipoDatabase);
			
		} else {
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.updateFineElaborazione(tracciato);
			if(!tracciatiBD.isAutoCommit()) tracciatiBD.commit();
		}

		log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato() + ", Creazione stampe avvisi completata.");
	}

	private void _elaboraTracciatoCSV(TracciatiBD tracciatiBD, Tracciato tracciato, it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, ISerializer serializer, IContext ctx)
			throws ServiceException, ValidationException, IOException, java.io.IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		String codDominio = tracciato.getCodDominio();
		String codTipoVersamento = tracciato.getCodTipoVersamento();
		FORMATO_TRACCIATO formato = tracciato.getFormato();

		byte[] rawRichiesta = tracciato.getRawRichiesta();

		if(beanDati.getStepElaborazione().equals(StatoTracciatoType.NUOVO.getValue())) {
			log.debug("Cambio stato del tracciato da NUOVO a IN_CARICAMENTO");
			beanDati.setStepElaborazione(StatoTracciatoType.IN_CARICAMENTO.getValue());
			beanDati.setLineaElaborazioneAdd(1); // skip intestazione file csv
			beanDati.setLineaElaborazioneDel(0);
			long numLines = rawRichiesta != null ? CSVUtils.countLines(rawRichiesta) : 0;
			log.debug("Numero linee totali compresa intestazione ["+numLines+"]");
			beanDati.setNumAddTotali(numLines > 0 ? (numLines -1) : 0);
			beanDati.setNumDelTotali(0);
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			
			tracciatiBD.updateBeanDati(tracciato);
			tracciatiBD.commit();
		}

		if(rawRichiesta == null) {
			throw new ValidationException("Il file CSV ricevuto e' vuoto.");
		}

		OperazioniBD operazioniBD = new OperazioniBD(tracciatiBD);
		operazioniBD.setAtomica(false);
	
		// eseguo operazioni add
		long numLinea = beanDati.getLineaElaborazioneAdd();
		log.debug("Elaboro le operazioni di caricamento del tracciato saltando le prime " + numLinea + " linee");
		List<byte[]> lst = CSVUtils.splitCSV(rawRichiesta, numLinea);
		log.debug("Lette " + lst.size() + " linee");

		TipoVersamentoDominio tipoVersamentoDominio = null;
		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(configWrapper, codDominio); 
		} catch (NotFoundException e) {	
			throw new ValidationException("Dominio ["+codDominio+"] inesistente.");
		}
		try {
			if(codTipoVersamento != null) {
				tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, dominio.getId(), codTipoVersamento);
			}
		} catch (NotFoundException e) {	
			throw new ValidationException("Tipo Versamento ["+codTipoVersamento+"] inesistente per il Dominio: ["+codDominio+"].");
		}

		TracciatoCsv tracciatoCsv = null;
		if(tipoVersamentoDominio != null) {
			if(tipoVersamentoDominio.getTracciatoCsvTipo() != null && tipoVersamentoDominio.getTracciatoCsvIntestazione() != null
					&& tipoVersamentoDominio.getTracciatoCsvRichiesta() != null && tipoVersamentoDominio.getTracciatoCsvRisposta() != null) {
				tracciatoCsv = new TracciatoCsv();
				tracciatoCsv.setTipo(tipoVersamentoDominio.getTracciatoCsvTipo());
				tracciatoCsv.setIntestazione(tipoVersamentoDominio.getTracciatoCsvIntestazione());
				tracciatoCsv.setRichiesta(tipoVersamentoDominio.getTracciatoCsvRichiesta());
				tracciatoCsv.setRisposta(tipoVersamentoDominio.getTracciatoCsvRisposta());
			}
		}

		// configurazione di sistema
		if(tracciatoCsv == null)
			tracciatoCsv = new it.govpay.core.business.Configurazione().getConfigurazione().getTracciatoCsv();

		List<CaricamentoTracciatoThread> threads = new ArrayList<CaricamentoTracciatoThread>();

		int maxRichiestePerThread = GovpayConfig.getInstance().getBatchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread();

		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciato.getId());
		idTracciato.setIdTracciato(tracciato.getId());

		List<CaricamentoRequest> richiesteThread = new ArrayList<>();
		
		TracciatiPendenzeManager manager = new TracciatiPendenzeManager();

		for(int i = 0; i < lst.size() ; i ++) {
			byte[] linea = lst.get(i);

			CaricamentoRequest request = new CaricamentoRequest();
			// inserisco l'identificativo del dominio
			request.setCodDominio(codDominio);
			request.setCodTipoVersamento(codTipoVersamento);
			request.setTipoTemplateTrasformazioneRichiesta(tracciatoCsv.getTipo());
			request.setTemplateTrasformazioneRichiesta(tracciatoCsv.getRichiesta());
			request.setDati(linea);
			request.setLinea(numLinea + 1);
			request.setOperatore(tracciato.getOperatore(configWrapper));
			request.setIdTracciato(tracciato.getId());

			richiesteThread.add(request);

			if(richiesteThread.size() == maxRichiestePerThread) {
				CaricamentoTracciatoThread sender = new CaricamentoTracciatoThread(richiesteThread, idTracciato, manager, ctx);
				ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciati().execute(sender);
				threads.add(sender);
				richiesteThread = new ArrayList<CaricamentoRequest>();
			}

			numLinea = numLinea + 1 ;
		}

		// richieste residue
		if(richiesteThread.size() > 0) {
			CaricamentoTracciatoThread sender = new CaricamentoTracciatoThread(richiesteThread, idTracciato, manager, ctx);
			ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciati().execute(sender);
			threads.add(sender);
		}

		int sommaAddOk = 0;
		int sommaAddKo = 0;
		int sommaDelOk = 0;
		int sommaDelKo = 0;
		String descrizioneEsito = null;
		List<Long> lineeElaborate = new ArrayList<Long>();
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
			boolean completed = true;
			for(CaricamentoTracciatoThread sender : threads) {
				if(!sender.isCompleted()) {
					completed = false;
				} else {
					if(!sender.isCommit()) {
						sender.setCommit(true); 
						synchronized (this) {
							lineeElaborate.addAll(sender.getLineeElaborate());
							sommaAddOk += sender.getNumeroAddElaborateOk();
							sommaAddKo += sender.getNumeroAddElaborateKo();
							sommaDelOk += sender.getNumeroDelElaborateOk();
							sommaDelKo += sender.getNumeroDelElaborateKo();

							if(sender.getDescrizioneEsito() != null)
								descrizioneEsito = sender.getDescrizioneEsito();
							
							// ordino al contrario cosi l'ultima elaborata e' in cima
							Collections.sort(lineeElaborate, Collections.reverseOrder());
							if(lineeElaborate.size() > 0) {
								beanDati.setLineaElaborazioneAdd(lineeElaborate.get(0));
							} else {
								beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);
							}
							beanDati.setNumAddOk(sommaAddOk);
							beanDati.setNumAddKo(sommaAddKo);
							beanDati.setNumDelOk(sommaDelOk);
							beanDati.setNumDelKo(sommaDelKo);
							beanDati.setDescrizioneStepElaborazione(descrizioneEsito);
							beanDati.setDataUltimoAggiornamento(new Date());

							tracciatiBD.setAutoCommit(false);
							tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
							tracciatiBD.commit();
							
							BatchManager.aggiornaEsecuzione(configWrapper, Operazioni.BATCH_TRACCIATI);
						}
					}
				}
			}

			if(completed) { 
//				for(CaricamentoTracciatoThread sender : threads) {
//					lineeElaborate.addAll(sender.getLineeElaborate());
//					sommaAddOk += sender.getNumeroAddElaborateOk();
//					sommaAddKo += sender.getNumeroAddElaborateKo();
//					sommaDelOk += sender.getNumeroDelElaborateOk();
//					sommaDelKo += sender.getNumeroDelElaborateKo();
//
//					if(sender.getDescrizioneEsito() != null)
//						descrizioneEsito = sender.getDescrizioneEsito();
//				}
//
//				// ordino al contrario cosi l'ultima elaborata e' in cima
//				Collections.sort(lineeElaborate, Collections.reverseOrder());
//				if(lineeElaborate.size() > 0) {
//					beanDati.setLineaElaborazioneAdd(lineeElaborate.get(0));
//				} else {
//					beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);
//				}
//				beanDati.setNumAddOk(sommaAddOk);
//				beanDati.setNumAddKo(sommaAddKo);
//				beanDati.setNumDelOk(sommaDelOk);
//				beanDati.setNumDelKo(sommaDelKo);
//				beanDati.setDescrizioneStepElaborazione(descrizioneEsito);
//
//				tracciatiBD.setAutoCommit(false);
//				tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
//				tracciatiBD.commit();

				log.debug("Completata Esecuzione dei ["+threads.size()+"] Threads, ADDOK ["+sommaAddOk+"], ADDKO ["+sommaAddKo+"] DELOK ["+sommaDelOk+"], DELKO ["+sommaDelKo+"]");
				break; // esco
			}
		}

		// Elaborazione completata. Processamento tracciato di esito
		TracciatiUtils.setStatoDettaglioTracciato(beanDati);
		String esitoElaborazioneTracciatoCSV = this.getEsitoElaborazioneTracciatoCSV(tracciato, operazioniBD, dominio, codTipoVersamento, tracciatoCsv.getIntestazione(), tracciatoCsv.getTipo(), tracciatoCsv.getRisposta());

		//		log.debug("Tracciato di esito[" + esitoElaborazioneTracciatoCSV+"]");

		tracciato.setRawEsito(esitoElaborazioneTracciatoCSV.getBytes());
		tracciato.setFileNameEsito("esito_" + tracciato.getFileNameRichiesta()); 
		
		if(beanDati.isStampaAvvisi()) {
			beanDati.setNumStampeTotali(beanDati.getNumAddOk()); // il numero di stampe che mi aspetto corrisponde al numero di pendenze caricate con esito ok
			beanDati.setNumStampeOk(0);
			beanDati.setNumStampeKo(0); 
			beanDati.setDataUltimoAggiornamento(new Date());
			
			tracciato.setStato(STATO_ELABORAZIONE.IN_STAMPA);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.updateFineElaborazione(tracciato);

			if(!tracciatiBD.isAutoCommit()) tracciatiBD.commit();
			log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato() + ", Creazione stampe avvisi...");
			
			// produzione stampe
			// Tengo traccia degli avvisi inseriti nello zip per tenere solo l'ultima versione.
			Set<String> numeriAvviso = new HashSet<String>();
			Set<String> numeriDocumento = new HashSet<String>();

			OutputStream oututStreamDestinazione = null;
			Long oid = null;
			Blob blobStampe = null;

			if(tracciatiBD.isAutoCommit())
				tracciatiBD.setAutoCommit(false);

			TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();

			switch (tipoDatabase) {
			case MYSQL:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case ORACLE:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case SQLSERVER:
				try {
					blobStampe = tracciatiBD.getConnection().createBlob();
					oututStreamDestinazione = blobStampe.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case POSTGRESQL:
				org.openspcoop2.utils.datasource.Connection wrappedConn = (org.openspcoop2.utils.datasource.Connection) tracciatiBD.getConnection();
				Connection wrappedConnection = wrappedConn.getWrappedConnection();

				Connection underlyingConnection = null;
				try {
					Method method = wrappedConnection.getClass().getMethod("getUnderlyingConnection");

					Object invoke = method.invoke(wrappedConnection);

					underlyingConnection = (Connection) invoke;
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Errore durante la lettura dell'oggetto connessione: " + e.getMessage(), e);
					throw new ServiceException(e);
				}

				org.postgresql.PGConnection pgConnection = null;
				try {
					if(underlyingConnection.isWrapperFor(org.postgresql.PGConnection.class)) {
						pgConnection = underlyingConnection.unwrap(org.postgresql.PGConnection.class);
					} else {
						pgConnection = (org.postgresql.PGConnection) underlyingConnection;				
					}

					// Get the Large Object Manager to perform operations with
					LargeObjectManager lobj = pgConnection.getLargeObjectAPI();

					// Create a new large object
					oid = lobj.createLO(LargeObjectManager.WRITE);

					// Open the large object for writing
					LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

					oututStreamDestinazione = obj.getOutputStream();
				} catch (SQLException e) {
					log.error("Errore durante la creazione dell'outputstream: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case DB2:
			case DEFAULT:
			case DERBY:
			case HSQL:
			default:
				throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
			}

			TracciatiBD tracciatiBeanDatiBD = null;
			try (ZipOutputStream zos = new ZipOutputStream(oututStreamDestinazione);) {

				int offset = 0;
				int limit = 500; 

				int stampePerThread = GovpayConfig.getInstance().getBatchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread();

				VersamentiBD versamentiBD = new VersamentiBD(tracciatiBD);
				versamentiBD.setAtomica(false);

				List<Versamento> versamentiDaStampare = versamentiBD.findVersamentiDiUnTracciato(tracciato.getId(), offset, limit);
				log.debug("Trovati ["+versamentiDaStampare.size()+"] Versamenti per cui stampare l'avviso");

				int sommaStampeOk = 0;
				int sommaStampeKo = 0;
				
				if(versamentiDaStampare.size() > 0) {
					tracciatiBeanDatiBD = new TracciatiBD(configWrapper);
					tracciatiBeanDatiBD.setupConnection(configWrapper.getTransactionID());
					tracciatiBeanDatiBD.setAtomica(false);
					tracciatiBeanDatiBD.setAutoCommit(false); 
					do {
						if(versamentiDaStampare.size() > 0) {
							List<CreaStampeTracciatoThread> threadsStampe = new ArrayList<CreaStampeTracciatoThread>();

							if(stampePerThread > versamentiDaStampare.size()) {
								CreaStampeTracciatoThread sender = new CreaStampeTracciatoThread(versamentiDaStampare, idTracciato, ("ThreadStampe_" + (threadsStampe.size() + 1)), ctx); 
								ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciatiStampeAvvisi().execute(sender);
								threadsStampe.add(sender);
							} else {
								for (int i = 0; i < versamentiDaStampare.size(); i += stampePerThread) {
									int end = Math.min(versamentiDaStampare.size(), i + stampePerThread);

									CreaStampeTracciatoThread sender = new CreaStampeTracciatoThread(versamentiDaStampare.subList(i, end), idTracciato, ("ThreadStampe_" + (threadsStampe.size() + 1)), ctx); 
									ThreadExecutorManager.getClientPoolExecutorCaricamentoTracciatiStampeAvvisi().execute(sender);
									threadsStampe.add(sender);
								}
							}

							while(true){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {

								}
								boolean completed = true;
								for(CreaStampeTracciatoThread sender : threadsStampe) {
									if(!sender.isCompleted()) { 
										completed = false;
									} else {
										if(!sender.isCommit()) {
											sender.setCommit(true);
											synchronized (this) {
												
												List<PrintAvvisoDTOResponse> stampe = sender.getStampe();
												
												sommaStampeOk += sender.getStampeOk();
												sommaStampeKo += sender.getStampeKo();
			
												log.debug(sender.getNomeThread() + " ha eseguito ["+stampe.size()+"] stampe");
			
												for (PrintAvvisoDTOResponse stampa : stampe) {
													// inserisco l'eventuale pdf nello zip
													TracciatiUtils.aggiungiStampaAvviso(zos, numeriAvviso, numeriDocumento, stampa, log);
												}
												
												beanDati.setNumStampeOk(sommaStampeOk);
												beanDati.setNumStampeKo(sommaStampeKo);
												beanDati.setDataUltimoAggiornamento(new Date());
												
												log.debug("Aggiornamento delle informazioni progresso stampa...");
												tracciato.setBeanDati(serializer.getObject(beanDati));
												tracciatiBeanDatiBD.updateBeanDati(tracciato);
	
												if(!tracciatiBeanDatiBD.isAutoCommit()) tracciatiBeanDatiBD.commit();
												log.debug("Aggiornamento delle informazioni progresso stampa completato.");
												
												
											}
										}
									}
								}

								if(completed) { 
									log.debug("Completata Esecuzione dei ["+threadsStampe.size()+"] Threads di stampa");
									break; // esco
								}
							}

						}

						offset += limit;
						versamentiDaStampare = versamentiBD.findVersamentiDiUnTracciato(tracciato.getId(), offset, limit);
						log.debug("Trovati ["+versamentiDaStampare.size()+"] Versamenti per cui stampare l'avviso");
					} while (versamentiDaStampare.size() > 0);
				}

				if(numeriAvviso.isEmpty() && numeriDocumento.isEmpty()){ // non ho aggiunto neanche un pdf
					ZipEntry tracciatoOutputEntry = new ZipEntry("errore.txt");
					zos.putNextEntry(tracciatoOutputEntry);
					zos.write("Attenzione: non sono presenti inserimenti andati a buon fine nel tracciato selezionato.".getBytes());
					zos.flush();
					zos.closeEntry();
				}

				zos.flush();
				zos.close();
			} catch (java.io.IOException e) {
				log.error(e.getMessage(), e);
			}finally {
				if(tracciatiBeanDatiBD != null)
					tracciatiBeanDatiBD.closeConnection();
			}
			
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));

			this.salvaZipStampeTracciato(tracciatiBD, tracciato, oid, blobStampe, tipoDatabase);
			
		} else {
			beanDati.setDataUltimoAggiornamento(new Date());
			tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.updateFineElaborazione(tracciato);
			if(!tracciatiBD.isAutoCommit()) tracciatiBD.commit();
		}
		
		log.info("Elaborazione tracciato "+formato+" ["+tracciato.getId()+"] terminata: " + tracciato.getStato() + ", Creazione stampe avvisi completata.");
	}

	private void salvaZipStampeTracciato(TracciatiBD tracciatiBD, Tracciato tracciato, Long oid, Blob blobStampe,
			TipiDatabase tipoDatabase) throws ServiceException {
		switch (tipoDatabase) {
		case MYSQL:
		case ORACLE:
		case SQLSERVER:
			tracciatiBD.updateFineElaborazioneStampeBlob(tracciato,blobStampe);
			break;
		case POSTGRESQL:
			tracciatiBD.updateFineElaborazioneStampeOid(tracciato,oid);
			break;
		case DB2:
		case DEFAULT:
		case DERBY:
		case HSQL:
		default:
			throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
		}

		if(!tracciatiBD.isAutoCommit()) tracciatiBD.commit();
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
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
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
				case DEL:
					EsitoOperazionePendenza risposta = null;
					Applicazione applicazione =null;
					Versamento versamento = null;
					Documento documento = null;
					try {
						risposta = EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta()));
						applicazione = AnagraficaManager.getApplicazione(configWrapper,risposta.getIdA2A());
						versamento = versamentiBD.getVersamento(applicazione.getId(), risposta.getIdPendenza());
						documento = versamento.getDocumento(operazioniBD);
						codTipoVersamento =  versamento.getTipoVersamento(configWrapper).getCodTipoVersamento();
					} catch(NotFoundException e) {
					} catch(Exception e) {

					}

					// trasformare il json in csv String trasformazioneOutputCSV = 
					try {
						ByteArrayOutputStream baostmp = new ByteArrayOutputStream();
						PrintWriter pwtmp = new PrintWriter(baostmp);
						BufferedWriter bwtmp = new BufferedWriter(pwtmp);
						TracciatiUtils.trasformazioneOutputCSV(log, bwtmp, dominio.getCodDominio(), codTipoVersamento, tipoTemplate,
								new String(operazione.getDatiRisposta()), template, headerRisposta, dominio, applicazione, versamento,
								documento, operazione.getStato().toString(), operazione.getDettaglioEsito(), operazione.getTipoOperazione().toString());
						bw.write(baostmp.toString());
					} catch (GovPayException e) {
						bw.write(("Pendenza [IdA2A:"+risposta.getIdA2A()+", Id:"+risposta.getIdPendenza()+"] inserita con esito '"
								+ (operazione.getStato()) +"': scrittura dell'esito sul file csv conclusa con con errore.\n"));//.getBytes());
					}
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

		bw.flush();
		bw.close();
		return baos.toString();
	}

	public LeggiOperazioneDTOResponse fillOperazione(Operazione operazione) throws ServiceException {
		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		switch (operazione.getTipoOperazione()) {
		case ADD:
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);
			try {
				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					Versamento versamento = versamentiBD.getVersamento(operazione.getIdApplicazione(), operazione.getCodVersamentoEnte());
					versamento.getSingoliVersamenti(configWrapper);
					versamento.getDominio(configWrapper);
					versamento.getUo(configWrapper);
					versamento.getApplicazione(configWrapper);
					versamento.getIuv(configWrapper);
					operazioneCaricamento.setVersamento(versamento);
				}
			}catch(NotFoundException e) {
				// do nothing
			}
			operazioneCaricamento.getApplicazione(configWrapper);
			try {
				operazioneCaricamento.getDominio(configWrapper);
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

				operazioneAnnullamento.getApplicazione(configWrapper);
				try {
					operazioneAnnullamento.getDominio(configWrapper);
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
}
